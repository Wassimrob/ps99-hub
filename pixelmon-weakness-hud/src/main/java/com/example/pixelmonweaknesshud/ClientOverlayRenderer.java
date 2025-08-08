package com.example.pixelmonweaknesshud;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Mod.EventBusSubscriber(modid = PixelmonWeaknessHud.MOD_ID, value = Dist.CLIENT)
public final class ClientOverlayRenderer {
    private static final Minecraft MC = Minecraft.getInstance();

    private static final ResourceLocation TYPE_ICONS = new ResourceLocation(PixelmonWeaknessHud.MOD_ID, "textures/gui/types.png");

    private ClientOverlayRenderer() {}

    public static void ensureRegistered() {}

    @SubscribeEvent
    public static void onRenderOverlay(RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.ALL) {
            return;
        }
        Screen current = MC.currentScreen;
        if (current == null) return;

        if (!isPixelmonBattleScreen(current)) return;

        List<String> enemyTypes = getEnemyTypes(current);
        if (enemyTypes.isEmpty()) return;

        List<String> weaknesses = TypeChart.computeWeaknesses(enemyTypes);
        renderWeaknessIcons(event.getMatrixStack(), weaknesses, current);
    }

    private static boolean isPixelmonBattleScreen(Screen screen) {
        String name = screen.getClass().getName();
        // Known class names tend to include these tokens across 9.1.x
        return name.contains("pixelmon") && name.toLowerCase().contains("battle");
    }

    @SuppressWarnings("unchecked")
    private static List<String> getEnemyTypes(Screen battleScreen) {
        try {
            // Heuristic reflection since we don't compile against Pixelmon API here
            // Try method: getController()->getOpponentTypes() or getOpponents()[0].getTypes()
            Object controller = invokeGetterChain(battleScreen, "getController");
            if (controller != null) {
                Object opp = tryInvoke(controller, "getOpponents");
                if (opp instanceof List) {
                    Object first = ((List<?>) opp).isEmpty() ? null : ((List<?>) opp).get(0);
                    List<String> types = extractTypes(first);
                    if (!types.isEmpty()) return types;
                }
                List<String> types = extractTypes(controller);
                if (!types.isEmpty()) return types;
            }

            // Fallback: search fields for something that looks like opponent/target with types
            for (Field f : battleScreen.getClass().getDeclaredFields()) {
                f.setAccessible(true);
                Object val = f.get(battleScreen);
                List<String> t = extractTypes(val);
                if (!t.isEmpty()) return t;
            }
        } catch (Throwable ignored) {}
        return Collections.emptyList();
    }

    private static List<String> extractTypes(Object obj) {
        if (obj == null) return Collections.emptyList();
        try {
            Object types = tryInvoke(obj, "getTypes");
            if (types instanceof List) {
                List<String> result = new ArrayList<>();
                for (Object o : (List<?>) types) {
                    if (o == null) continue;
                    String s = o.toString();
                    if (!s.isEmpty()) result.add(TypeChart.normalizeTypeName(s));
                }
                return result;
            }
            // Some versions have primary/secondary
            Object t1 = tryInvoke(obj, "getPrimaryType");
            Object t2 = tryInvoke(obj, "getSecondaryType");
            List<String> result = new ArrayList<>();
            if (t1 != null) result.add(TypeChart.normalizeTypeName(t1.toString()));
            if (t2 != null) result.add(TypeChart.normalizeTypeName(t2.toString()));
            return result;
        } catch (Throwable ignored) {}
        return Collections.emptyList();
    }

    private static Object invokeGetterChain(Object target, String... getters) {
        Object cur = target;
        for (String g : getters) {
            if (cur == null) return null;
            cur = tryInvoke(cur, g);
        }
        return cur;
    }

    private static Object tryInvoke(Object target, String methodName) {
        try {
            Method m = findNoArgMethod(target.getClass(), methodName);
            if (m != null) {
                m.setAccessible(true);
                return m.invoke(target);
            }
        } catch (Throwable ignored) {}
        return null;
    }

    private static Method findNoArgMethod(Class<?> c, String name) {
        for (Method m : c.getMethods()) {
            if (m.getName().equals(name) && m.getParameterCount() == 0) return m;
        }
        for (Method m : c.getDeclaredMethods()) {
            if (m.getName().equals(name) && m.getParameterCount() == 0) return m;
        }
        return null;
    }

    private static void renderWeaknessIcons(MatrixStack ms, List<String> weaknesses, Screen battleScreen) {
        if (weaknesses.isEmpty()) return;

        boolean hasAtlas = MC.getResourceManager().hasResource(TYPE_ICONS);

        // Position near the enemy name at top-left of the battle HUD
        int baseX = 20;
        int baseY = 35;

        if (hasAtlas) {
            MC.getTextureManager().bindTexture(TYPE_ICONS);
            int size = 12; // draw 12x12 icons
            int pad = 2;
            int x = baseX;
            for (String type : weaknesses) {
                int u = TypeChart.getIconU(type);
                int v = TypeChart.getIconV(type);
                Screen.blit(ms, x, baseY, 0, (float) u, (float) v, size, size, 256, 256);
                x += size + pad;
            }
        } else {
            // Fallback: draw abbreviated type labels if atlas not present
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < weaknesses.size(); i++) {
                String t = weaknesses.get(i);
                sb.append(t.substring(0, Math.min(3, t.length())).toUpperCase());
                if (i < weaknesses.size() - 1) sb.append(" ");
            }
            MC.fontRenderer.drawStringWithShadow(ms, sb.toString(), baseX, baseY, 0xFFFFFF);
        }
    }
}