package com.example.pixelmonweaknesshud;

import java.util.*;

final class TypeChart {
    private static final Map<String, Integer> TYPE_INDEX = new LinkedHashMap<>();
    private static final List<String> ORDER = Arrays.asList(
            "normal","fire","water","electric","grass","ice","fighting","poison","ground","flying",
            "psychic","bug","rock","ghost","dragon","dark","steel","fairy"
    );

    // attacker (row) vs defender (col) multipliers in quarter-steps: 0,1,2,4 means 0x,0.5x,1x,2x; 8 means 4x
    private static final int[][] MULT = new int[ORDER.size()][ORDER.size()];

    static {
        for (int i = 0; i < ORDER.size(); i++) TYPE_INDEX.put(ORDER.get(i), i);
        // Fill with 1x (2)
        for (int r = 0; r < ORDER.size(); r++) Arrays.fill(MULT[r], 2);
        // Populate standard gen 6+ chart (abbrev):
        set("fire","grass",4); set("fire","ice",4); set("fire","bug",4); set("fire","steel",4);
        set("fire","fire",1); set("fire","water",1); set("fire","rock",1); set("fire","dragon",1);
        set("water","fire",4); set("water","ground",4); set("water","rock",4);
        set("grass","water",4); set("grass","ground",4); set("grass","rock",4);
        set("grass","fire",1); set("grass","grass",1); set("grass","poison",1); set("grass","flying",1); set("grass","bug",1); set("grass","dragon",1); set("grass","steel",1);
        set("electric","water",4); set("electric","flying",4); set("electric","electric",1); set("electric","grass",1); set("electric","dragon",1); setImmune("electric","ground");
        set("ice","grass",4); set("ice","ground",4); set("ice","flying",4); set("ice","dragon",4); set("ice","fire",1); set("ice","water",1); set("ice","ice",1); set("ice","steel",1);
        set("fighting","normal",4); set("fighting","ice",4); set("fighting","rock",4); set("fighting","dark",4); set("fighting","steel",4);
        set("fighting","poison",1); set("fighting","flying",1); set("fighting","psychic",1); set("fighting","bug",1); set("fighting","fairy",1); setImmune("fighting","ghost");
        set("poison","grass",4); set("poison","fairy",4); set("poison","poison",1); set("poison","ground",1); set("poison","rock",1); set("poison","ghost",1); setImmune("poison","steel");
        set("ground","fire",4); set("ground","electric",4); set("ground","poison",4); set("ground","rock",4); set("ground","steel",4); set("ground","grass",1); set("ground","bug",1); setImmune("ground","flying");
        set("flying","grass",4); set("flying","fighting",4); set("flying","bug",4); set("flying","electric",1); set("flying","rock",1); set("flying","steel",1);
        set("psychic","fighting",4); set("psychic","poison",4); set("psychic","psychic",1); set("psychic","steel",1); setImmune("psychic","dark");
        set("bug","grass",4); set("bug","psychic",4); set("bug","dark",4); set("bug","fire",1); set("bug","fighting",1); set("bug","poison",1); set("bug","flying",1); set("bug","ghost",1); set("bug","steel",1); set("bug","fairy",1);
        set("rock","fire",4); set("rock","ice",4); set("rock","flying",4); set("rock","bug",4); set("rock","fighting",1); set("rock","ground",1); set("rock","steel",1);
        set("ghost","psychic",4); set("ghost","ghost",4); setImmune("ghost","normal");
        set("ghost","dark",1);
        set("dragon","dragon",4); setImmune("dragon","fairy"); set("dragon","steel",1);
        set("dark","psychic",4); set("dark","ghost",4); set("dark","fighting",1); set("dark","dark",1); set("dark","fairy",1);
        set("steel","ice",4); set("steel","rock",4); set("steel","fairy",4); set("steel","fire",1); set("steel","water",1); set("steel","electric",1); set("steel","steel",1);
        set("fairy","fighting",4); set("fairy","dragon",4); set("fairy","dark",4); set("fairy","fire",1); set("fairy","poison",1); set("fairy","steel",1);
        // Normal
        setImmune("normal","ghost"); set("normal","rock",1); set("normal","steel",1);
    }

    private static void set(String atk, String def, int mult) {
        Integer a = TYPE_INDEX.get(atk), d = TYPE_INDEX.get(def);
        if (a != null && d != null) MULT[a][d] = mult;
    }

    private static void setImmune(String atk, String def) { set(atk, def, 0); }

    static List<String> computeWeaknesses(List<String> defenderTypes) {
        if (defenderTypes == null || defenderTypes.isEmpty()) return Collections.emptyList();
        int[] accum = new int[ORDER.size()];
        Arrays.fill(accum, 2); // start at 1x
        for (String def : defenderTypes) {
            Integer d = TYPE_INDEX.get(normalizeTypeName(def));
            if (d == null) continue;
            for (int atk = 0; atk < ORDER.size(); atk++) {
                // multiply quarter-steps: 0 dominates, else scale
                int cur = accum[atk];
                int m = MULT[atk][d];
                if (cur == 0 || m == 0) { accum[atk] = 0; continue; }
                // map 1/4->1, 1/2->2, 1x->4, 2x->8, 4x->16 by doubling
                int curScaled = cur == 1 ? 1 : (cur == 2 ? 2 : (cur == 4 ? 4 : (cur == 8 ? 8 : 4)));
                int mScaled = m == 1 ? 1 : (m == 2 ? 2 : (m == 4 ? 4 : (m == 8 ? 8 : 4)));
                int res = curScaled * mScaled;
                // back to display buckets: consider weaknesses res >= 8 (>=2x)
                accum[atk] = res;
            }
        }
        List<String> out = new ArrayList<>();
        for (int atk = 0; atk < ORDER.size(); atk++) {
            int res = accum[atk];
            if (res >= 8) out.add(ORDER.get(atk));
        }
        return out;
    }

    static String normalizeTypeName(String raw) {
        String s = raw.toLowerCase(Locale.ROOT).trim();
        // handle Pixelmon enums like Type.FIRE -> "FIRE"
        int dot = s.lastIndexOf('.')
                ;
        if (dot >= 0) s = s.substring(dot + 1);
        s = s.replace("type_", "");
        return s;
    }

    static int getIconU(String type) {
        int idx = ORDER.indexOf(normalizeTypeName(type));
        if (idx < 0) idx = 0;
        int col = idx % 8;
        return col * 16;
    }

    static int getIconV(String type) {
        int idx = ORDER.indexOf(normalizeTypeName(type));
        if (idx < 0) idx = 0;
        int row = idx / 8;
        return row * 16;
    }
}