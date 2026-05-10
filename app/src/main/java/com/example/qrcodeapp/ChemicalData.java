package com.example.qrcodeapp;

import java.util.HashMap;
import java.util.Map;

public class ChemicalData {

    private static final Map<String, ChemicalElement> elements = new HashMap<>();

    static {
        // 1. Водород (Hydrogen)
        elements.put("H", new ChemicalElement(
                "Водород",
                "H",
                1,
                "Неметалл",
                "Самый легкий элемент, бесцветный газ без вкуса и запаха. Состоит из одного протона и одного электрона.",
                "Используется в производстве аммиака, ракетном топливе, гидрогенизации жиров.",
                1766,
                1.00784,
                13.99,
                20.271,
                0.08988,
                1270,
                2
        ));

        // 2. Азот (Nitrogen)
        elements.put("N", new ChemicalElement(
                "Азот",
                "N",
                7,
                "Неметалл",
                "Инертный газ без цвета и запаха. Основной компонент атмосферы Земли (78%).",
                "Используется для создания инертной среды, в производстве аммиака и удобрений, для замораживания продуктов.",
                1772,
                14.00643,
                63.15,
                73.355,
                1.2504,
                353,
                2
        ));

        // 3. Кислород (Oxygen)
        elements.put("O", new ChemicalElement(
                "Кислород",
                "O",
                8,
                "Неметалл",
                "Газ без цвета и запаха. Необходим для дыхания всех живых организмов и горения.",
                "Используется в медицине, металлургии, ракетном топливе, химической промышленности.",
                1774,
                15.999,
                54.36,
                90.20,
                1.429,
                317.5,
                3
        ));

        // 4. Железо (Iron)
        elements.put("Fe", new ChemicalElement(
                "Железо",
                "Fe",
                26,
                "Переходный металл",
                "Ковкий металл серебристо-белого цвета. Основной компонент стали.",
                "Используется в строительстве, машиностроении, транспорте, биологии (гемоглобин).",
                0,  // год открытия неизвестен (известно с древности)
                55.845,
                1811,
                3134,
                7.874,
                4910,
                4
        ));

        // 5. Натрий (Sodium)
        elements.put("Na", new ChemicalElement(
                "Натрий",
                "Na",
                11,
                "Щелочной металл",
                "Мягкий серебристо-белый металл. Очень активен, быстро окисляется на воздухе, бурно реагирует с водой.",
                "Используется в производстве натриевых ламп, мыла, металлургии, в ядерных реакторах в качестве теплоносителя.",
                1807,
                22.98977,
                370.944,
                1156.09,
                0.968,
                3200,
                1
        ));

        // 6. Сера (Sulfur)
        elements.put("S", new ChemicalElement(
                "Сера",
                "S",
                16,
                "Неметалл",
                "Желтый хрупкий неметалл. В природе встречается в самородном виде.",
                "Используется в производстве серной кислоты, вулканизации резины, удобрений.",
                0,  // известна с древности
                32.06,
                388.36,
                717.87,
                2.07,
                0,  // скорость звука не указана
                4
        ));
    }

    public static ChemicalElement getElement(String symbol) {
        if (symbol == null) return null;
        return elements.get(symbol);
    }

    public static boolean exists(String symbol) {
        if (symbol == null) return false;
        return elements.containsKey(symbol);
    }

    public static class ChemicalElement {
        private String name;
        private String symbol;
        private int atomicNumber;
        private String group;
        private String description;
        private String uses;
        private int discoveryYear;      // год открытия
        private double atomicMass;       // атомная масса
        private double meltingPoint;     // температура плавления (К)
        private double boilingPoint;     // температура кипения (К)
        private double density;          // плотность
        private double soundSpeed;       // скорость звука
        private int stableIsotopes;      // число стабильных изотопов

        // Расширенный конструктор
        public ChemicalElement(String name, String symbol, int atomicNumber, String group,
                               String description, String uses, int discoveryYear,
                               double atomicMass, double meltingPoint, double boilingPoint,
                               double density, double soundSpeed, int stableIsotopes) {
            this.name = name;
            this.symbol = symbol;
            this.atomicNumber = atomicNumber;
            this.group = group;
            this.description = description;
            this.uses = uses;
            this.discoveryYear = discoveryYear;
            this.atomicMass = atomicMass;
            this.meltingPoint = meltingPoint;
            this.boilingPoint = boilingPoint;
            this.density = density;
            this.soundSpeed = soundSpeed;
            this.stableIsotopes = stableIsotopes;
        }

        // Геттеры
        public String getName() { return name; }
        public String getSymbol() { return symbol; }
        public int getAtomicNumber() { return atomicNumber; }
        public String getGroup() { return group; }
        public String getDescription() { return description; }
        public String getUses() { return uses; }
        public int getDiscoveryYear() { return discoveryYear; }
        public double getAtomicMass() { return atomicMass; }
        public double getMeltingPoint() { return meltingPoint; }
        public double getBoilingPoint() { return boilingPoint; }
        public double getDensity() { return density; }
        public double getSoundSpeed() { return soundSpeed; }
        public int getStableIsotopes() { return stableIsotopes; }
    }
}