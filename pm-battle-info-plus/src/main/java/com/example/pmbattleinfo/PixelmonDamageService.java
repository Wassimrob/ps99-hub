package com.example.pmbattleinfo;

import com.pixelmonmod.pixelmon.api.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.api.battles.model.Battle;
import com.pixelmonmod.pixelmon.api.battles.model.actor.BattleActor;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.StatsType;
import com.pixelmonmod.pixelmon.api.moves.Move;
import com.pixelmonmod.pixelmon.api.moves.MoveCategory;
import com.pixelmonmod.pixelmon.api.moveset.Moveset;
import com.pixelmonmod.pixelmon.api.util.ITranslatable;
import com.pixelmonmod.pixelmon.battles.api.rules.BattleRules;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ITextComponent;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class PixelmonDamageService {
    private static final DecimalFormat PCT = new DecimalFormat("0.0");

    public static String[] describePredictedDamage() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) {
            return new String[]{"PMBI+: No player"};
        }

        Battle battle = BattleRegistry.getBattle(mc.player.getUUID());
        if (battle == null) {
            return new String[]{"PMBI+: Not in battle"};
        }

        BattleActor playerActor = battle.getActor(mc.player.getUUID());
        if (playerActor == null) {
            return new String[]{"PMBI+: No actor"};
        }

        Pokemon attacker = playerActor.getFirstAlivePokemon();
        BattleActor opponentActor = battle.getOpposingActor(playerActor);
        Pokemon defender = opponentActor != null ? opponentActor.getFirstAlivePokemon() : null;

        if (attacker == null || defender == null) {
            return new String[]{"PMBI+: Missing mons"};
        }

        List<String> lines = new ArrayList<>();
        String atkName = attacker.getDisplayName().getString();
        String defName = defender.getDisplayName().getString();
        lines.add(atkName + " vs " + defName);

        Moveset moveset = attacker.getMoveset();
        if (moveset == null || moveset.size() == 0) {
            lines.add("No moves");
            return lines.toArray(new String[0]);
        }

        for (int i = 0; i < moveset.size(); i++) {
            Move move = moveset.get(i);
            if (move == null) continue;
            String moveName = getMoveName(move);
            DamageRange range = predictDamagePercent(attacker, defender, move);
            if (range == null) continue;
            lines.add((i + 1) + ". " + moveName + ": " + PCT.format(range.min) + "%-" + PCT.format(range.max) + "%");
        }

        return lines.toArray(new String[0]);
    }

    private static String getMoveName(Move move) {
        try {
            ITextComponent comp = move.getMove().getDisplayName();
            if (comp != null) return comp.getString();
        } catch (Throwable ignored) {}
        try {
            ITranslatable t = move.getMove();
            if (t != null) return t.getLocalizedName();
        } catch (Throwable ignored) {}
        return move.getAttackName();
    }

    private static DamageRange predictDamagePercent(Pokemon attacker, Pokemon defender, Move move) {
        try {
            if (move.getMove().getPower() <= 0) {
                return null;
            }

            int level = attacker.getLevel();
            boolean isPhysical = move.getMove().getCategory() == MoveCategory.PHYSICAL;

            int attackStat = attacker.getStat(isPhysical ? StatsType.ATTACK : StatsType.SPECIAL_ATTACK);
            int defenseStat = defender.getStat(isPhysical ? StatsType.DEFENSE : StatsType.SPECIAL_DEFENSE);

            double power = move.getMove().getPower();

            // Base damage before modifiers
            double base = Math.floor(Math.floor(Math.floor((2.0 * level) / 5.0 + 2.0) * power * attackStat / defenseStat) / 50.0) + 2.0;

            // STAB
            double stab = (attacker.getForm().getTypes().contains(move.getMove().getAttackType())) ? 1.5 : 1.0;

            // Type effectiveness
            double typeEffect = com.pixelmonmod.pixelmon.api.types.Type.getMultiplier(move.getMove().getAttackType(), defender.getForm().getTypes());

            // Burn (basic approximation; does not check abilities)
            double burn = (isPhysical && attacker.isBurned()) ? 0.5 : 1.0;

            // Other modifiers ignored for simplicity (weather, items, screens, etc.)
            double modifiers = stab * typeEffect * burn;

            // Random factor 0.85 - 1.00 (no crit). Report percentage of defender HP
            int defHp = defender.getStat(StatsType.HP);
            if (defHp <= 0) defHp = 1;

            double minDmg = Math.floor(base * 0.85 * modifiers);
            double maxDmg = Math.floor(base * 1.00 * modifiers);

            double minPct = Math.min(100.0, (minDmg / defHp) * 100.0);
            double maxPct = Math.min(100.0, (maxDmg / defHp) * 100.0);

            return new DamageRange(minPct, maxPct);
        } catch (Throwable t) {
            return null;
        }
    }

    private static class DamageRange {
        final double min;
        final double max;
        DamageRange(double min, double max) {
            this.min = min;
            this.max = max;
        }
    }
}