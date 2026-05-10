package com.example.qrcodeapp;

import java.util.HashMap;
import java.util.Map;

public class ChemicalReactions {

    public static class Reaction {
        private String reactants;      // реагенты (что вступает в реакцию)
        private String products;       // продукты (что получается)
        private String conditions;     // условия реакции
        private String equation;       // уравнение реакции (с коэффициентами)
        private String description;    // описание реакции

        public Reaction(String reactants, String products, String conditions,
                        String equation, String description) {
            this.reactants = reactants;
            this.products = products;
            this.conditions = conditions;
            this.equation = equation;
            this.description = description;
        }

        public String getReactants() { return reactants; }
        public String getProducts() { return products; }
        public String getConditions() { return conditions; }
        public String getEquation() { return equation; }
        public String getDescription() { return description; }
    }

    private static final Map<String, Reaction> reactions = new HashMap<>();

    static {
        // ========== РЕАКЦИИ С ВОДОРОДОМ (H) ==========

        // 1. H₂ + S → H₂S  (сероводород)
        reactions.put("H+S", new Reaction(
                "Водород + Сера",
                "Сероводород (H₂S)",
                "Температура: 300-400°C",
                "H₂ + S → H₂S",
                "Водород реагирует с серой при нагревании, образуя сероводород — газ с запахом тухлых яиц."
        ));
        reactions.put("S+H", reactions.get("H+S"));

        // 2. 2Na + H₂ → 2NaH  (исправлено: гидрид натрия)
        reactions.put("Na+H", new Reaction(
                "Натрий + Водород",
                "Гидрид натрия (NaH)",
                "Температура: 300-400°C",
                "2Na + H₂ → 2NaH",
                "Натрий реагирует с водородом при нагревании, образуя гидрид натрия — белое твёрдое вещество."
        ));
        reactions.put("H+Na", reactions.get("Na+H"));

        // 3. H₂ + O₂ → 2H₂O  (вода)
        reactions.put("H+O", new Reaction(
                "Водород + Кислород",
                "Вода (H₂O)",
                "Воспламенение, температура 500-600°C",
                "2H₂ + O₂ → 2H₂O",
                "Водород сгорает в кислороде с образованием воды. Реакция сопровождается выделением большого количества тепла (экзотермическая)."
        ));
        reactions.put("O+H", reactions.get("H+O"));

        // ========== РЕАКЦИИ С КИСЛОРОДОМ (O) ==========

        // 4. S + O₂ → SO₂  (сернистый газ)
        reactions.put("S+O", new Reaction(
                "Сера + Кислород",
                "Сернистый газ (SO₂)",
                "Горение, температура 200-300°C",
                "S + O₂ → SO₂",
                "Сера сгорает в кислороде, образуя сернистый газ. При этом наблюдается синее пламя и резкий запах."
        ));
        reactions.put("O+S", reactions.get("S+O"));

        // 5. N₂ + O₂ → 2NO  (оксид азота)
        reactions.put("N+O", new Reaction(
                "Азот + Кислород",
                "Оксид азота (NO)",
                "Высокая температура (электрическая дуга)",
                "N₂ + O₂ → 2NO",
                "При высоких температурах (например, в электрической дуге) азот реагирует с кислородом, образуя оксид азота. Это одна из реакций образования смога."
        ));
        reactions.put("O+N", reactions.get("N+O"));

        // 6. 3Fe + 2O₂ → Fe₃O₄  (железная окалина / магнетит)
        reactions.put("Fe+O", new Reaction(
                "Железо + Кислород",
                "Железная окалина (Fe₃O₄)",
                "Нагревание до 500-600°C",
                "3Fe + 2O₂ → Fe₃O₄",
                "Железо сгорает в кислороде, образуя смесь оксидов железа (FeO + Fe₂O₃), называемую железной окалиной."
        ));
        reactions.put("O+Fe", reactions.get("Fe+O"));

        // 7. 2Fe + O₂ → 2FeO  (только если нужен отдельный оксид)
        reactions.put("FeO+O", new Reaction(
                "Железо + Кислород",
                "Оксид железа(II) (FeO)",
                "Недостаток кислорода, температура > 800°C",
                "2Fe + O₂ → 2FeO",
                "При недостатке кислорода и высокой температуре железо образует оксид FeO."
        ));

        // ========== РЕАКЦИИ С НАТРИЕМ (Na) ==========

        // 8. 4Na + O₂ → 2Na₂O  (оксид натрия) — простая реакция
        reactions.put("Na+O", new Reaction(
                "Натрий + Кислород",
                "Оксид натрия (Na₂O)",
                "Горение на воздухе",
                "4Na + O₂ → 2Na₂O",
                "Натрий сгорает на воздухе, образуя оксид натрия, который активно реагирует с водой."
        ));
        reactions.put("O+Na", reactions.get("Na+O"));

        // 9. 2Na + O₂ → Na₂O₂  (пероксид натрия) — из твоего скриншота
        reactions.put("Na+O2", new Reaction(
                "Натрий + Кислород (избыток)",
                "Пероксид натрия (Na₂O₂)",
                "Горение в избытке кислорода",
                "2Na + O₂ → Na₂O₂",
                "При горении натрия в избытке кислорода образуется пероксид натрия — жёлтое вещество, которое реагирует с водой с выделением кислорода."
        ));

        // ========== РЕАКЦИИ С ЖЕЛЕЗОМ И СЕРОЙ ==========

        // 10. Fe + S → FeS  (сульфид железа) — из твоего скриншота
        reactions.put("Fe+S", new Reaction(
                "Железо + Сера",
                "Сульфид железа (FeS)",
                "Нагревание до 500-600°C",
                "Fe + S → FeS",
                "При нагревании железо реагирует с серой, образуя сульфид железа — чёрное твёрдое вещество."
        ));
        reactions.put("S+Fe", reactions.get("Fe+S"));

        // ========== КОМБИНИРОВАННЫЕ РЕАКЦИИ (для сложных случаев) ==========

        // 11. Na₃FeFe + S → Na₂S (как пример сложной реакции)
        reactions.put("NaFe+S", new Reaction(
                "Железо-натриевое соединение + Сера",
                "Сульфид натрия",
                "Высокая температура",
                "Na₃Fe + S → Na₂S + FeS?",
                "Требуется уточнение — это модель реакции."
        ));

        // 12. FeO·Fe₂O₃ — магнетит (Fe₃O₄) уже описан выше
    }

    public static Reaction getReaction(String symbol1, String symbol2) {
        if (symbol1 == null || symbol2 == null) return null;

        String key1 = symbol1 + "+" + symbol2;
        String key2 = symbol2 + "+" + symbol1;

        if (reactions.containsKey(key1)) return reactions.get(key1);
        if (reactions.containsKey(key2)) return reactions.get(key2);

        return null;
    }

    public static boolean hasReaction(String symbol1, String symbol2) {
        return getReaction(symbol1, symbol2) != null;
    }
}