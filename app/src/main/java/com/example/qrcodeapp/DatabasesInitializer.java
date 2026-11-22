package com.example.qrcodeapp;

import android.content.Context;
import android.util.Log;

public class DatabasesInitializer {
    public static void populateDatabase(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        ElementDao elementDao = db.elementDao();
        CategoryDao categoryDao = db.categoryDao();
        MediaDao mediaDao = db.mediaDao();
        ScanHistoryDao scanHistoryDao = db.scanHistoryDao();

        if(!elementDao.getAllElements().isEmpty()){
            return;
        }

        Log.d("DB_test", "База пуста");

        Categories metals = new Categories();
        metals.categoriesId = 1; // Принудительно ставим ID 1
        metals.nameCategories = "Металлы";
        categoryDao.insert(metals);

        Categories noMetals = new Categories();
        noMetals.categoriesId = 2; // Принудительно ставим ID 2
        noMetals.nameCategories = "Неметаллы";
        categoryDao.insert(noMetals);

        int mId = 1;
        int nId = 2;


        //добавление элементов
        Element hydrogen = new Element();
        hydrogen.symbol = "H"; hydrogen.nameEn = "hydrogen"; hydrogen.nameRu = "Водород";
        hydrogen.categoryId = nId; hydrogen.atomicNumber = 1;
        hydrogen.description = "Самый легкий элемент, бесцветный газ без вкуса и запаха. Состоит из одного протона и одного электрона.";
        hydrogen.uses = "Используется в производстве аммиака, ракетном топливе, гидрогенизации жиров.";
        elementDao.insert(hydrogen);

        Element sodium = new Element();
        sodium.symbol = "Na"; sodium.nameEn = "sodium"; sodium.nameRu = "Натрий";
        sodium.categoryId = mId; sodium.atomicNumber = 11;
        sodium.description = "Мягкий серебристо-белый металл. Очень активен, быстро окисляется на воздухе, бурно реагирует с водой.";
        sodium.uses = "Используется в производстве натриевых ламп, мыла, металлургии, в ядерных реакторах в качестве теплоносителя.";
        elementDao.insert(sodium);

        Element sulfur = new Element();
        sulfur.symbol = "S"; sulfur.nameEn = "sulfur"; sulfur.nameRu = "Сера";
        sulfur.categoryId = nId; sulfur.atomicNumber = 16;
        sulfur.description = "Желтый хрупкий неметалл. В природе встречается в самородном виде.";
        sulfur.uses = "Используется в производстве серной кислоты, вулканизации резины, удобрений.";
        elementDao.insert(sulfur);

        Element gold = new Element();
        gold.symbol = "Au"; gold.nameEn = "gold"; gold.nameRu = "Золото";
        gold.categoryId = mId; gold.atomicNumber = 79;
        gold.description = "Желтый драгоценный металл.";
        gold.uses = "Ювелирные изделия.";
        elementDao.insert(gold);

        Element oxygen = new Element();
        oxygen.symbol = "O"; oxygen.nameEn = "oxygen"; oxygen.nameRu = "Кислород";
        oxygen.categoryId = nId; oxygen.atomicNumber = 8;
        oxygen.description = "Газ без цвета и запаха. Необходим для дыхания всех живых организмов и горения.";
        oxygen.uses = "Используется в медицине, металлургии, ракетном топливе, химической промышленности.";
        elementDao.insert(oxygen);

        Element nitrogen = new Element();
        nitrogen.symbol = "N"; nitrogen.nameEn = "nitrogen"; nitrogen.nameRu = "Азот";
        nitrogen.categoryId = nId; nitrogen.atomicNumber = 7;
        nitrogen.description = "Инертный газ без цвета и запаха. Основной компонент атмосферы Земли (78%).";
        nitrogen.uses = "Используется для создания инертной среды, в производстве аммиака и удобрений, для замораживания продуктов.";
        elementDao.insert(nitrogen);

        Element iron = new Element();
        iron.symbol = "Fe"; iron.nameEn = "iron"; iron.nameRu = "Железо";
        iron.categoryId = mId; iron.atomicNumber = 26;
        iron.description = "Ковкий металл серебристо-белого цвета. Основной компонент стали.";
        iron.uses = "Используется в строительстве, машиностроении, транспорте, биологии (гемоглобин).";
        elementDao.insert(iron);

        int goldId = elementDao.getElementByEnglishName("gold").id;
        int oxygenId = elementDao.getElementByEnglishName("oxygen").id;
        int nitrogenId = elementDao.getElementByEnglishName("nitrogen").id;
        int ironId = elementDao.getElementByEnglishName("iron").id;
        int hydrogenId = elementDao.getElementByEnglishName("hydrogen").id;
        int sodiumId = elementDao.getElementByEnglishName("sodium").id;
        int sulfurId = elementDao.getElementByEnglishName("sulfur").id;

        ReactionDao reactionDao = db.reactionDao();

        // 1. Водород + Сера → Сероводород
        Reaction r1 = new Reaction();
        r1.element1Id = hydrogenId;
        r1.element2Name = "Сера";
        r1.equation = "H₂ + S → H₂S";
        r1.conditions = "Температура: 300-400°C";
        r1.products = "Сероводород (H₂S)";
        r1.descriptionReaction = "Водород реагирует с серой при нагревании, образуя сероводород — газ с запахом тухлых яиц.";
        reactionDao.insert(r1);

        // 2. Натрий + Водород → Гидрид натрия
        Reaction r2 = new Reaction();
        r2.element1Id = sodiumId;
        r2.element2Name = "Водород";
        r2.equation = "2Na + H₂ → 2NaH";
        r2.conditions = "Температура: 300-400°C";
        r2.products = "Гидрид натрия (NaH)";
        r2.descriptionReaction = "Натрий реагирует с водородом при нагревании, образуя гидрид натрия — белое твёрдое вещество.";
        reactionDao.insert(r2);

        // 3. Водород + Кислород → Вода
        Reaction r3 = new Reaction();
        r3.element1Id = hydrogenId;
        r3.element2Name = "Кислород";
        r3.equation = "2H₂ + O₂ → 2H₂O";
        r3.conditions = "Воспламенение, 500-600°C";
        r3.products = "Вода (H₂O)";
        r3.descriptionReaction = "Водород сгорает в кислороде с образованием воды. Реакция экзотермическая.";
        reactionDao.insert(r3);

        // 4. Сера + Кислород → Сернистый газ
        Reaction r4 = new Reaction();
        r4.element1Id = sulfurId;
        r4.element2Name = "Кислород";
        r4.equation = "S + O₂ → SO₂";
        r4.conditions = "Горение, 200-300°C";
        r4.products = "Сернистый газ (SO₂)";
        r4.descriptionReaction = "Сера сгорает в кислороде, образуя сернистый газ. Наблюдается синее пламя и резкий запах.";
        reactionDao.insert(r4);

        // 5. Азот + Кислород → Оксид азота
        Reaction r5 = new Reaction();
        r5.element1Id = nitrogenId;
        r5.element2Name = "Кислород";
        r5.equation = "N₂ + O₂ → 2NO";
        r5.conditions = "Высокая температура (электрическая дуга)";
        r5.products = "Оксид азота (NO)";
        r5.descriptionReaction = "При высоких температурах азот реагирует с кислородом, образуя оксид азота.";
        reactionDao.insert(r5);

        // 6. Железо + Кислород → Железная окалина
        Reaction r6 = new Reaction();
        r6.element1Id = ironId;
        r6.element2Name = "Кислород";
        r6.equation = "3Fe + 2O₂ → Fe₃O₄";
        r6.conditions = "Нагревание до 500-600°C";
        r6.products = "Железная окалина (Fe₃O₄)";
        r6.descriptionReaction = "Железо сгорает в кислороде, образуя смесь оксидов железа.";
        reactionDao.insert(r6);

        // 7. Натрий + Кислород → Оксид натрия
        Reaction r7 = new Reaction();
        r7.element1Id = sodiumId;
        r7.element2Name = "Кислород";
        r7.equation = "4Na + O₂ → 2Na₂O";
        r7.conditions = "Горение на воздухе";
        r7.products = "Оксид натрия (Na₂O)";
        r7.descriptionReaction = "Натрий сгорает на воздухе, образуя оксид натрия.";
        reactionDao.insert(r7);

        // 8. Железо + Сера → Сульфид железа
        Reaction r8 = new Reaction();
        r8.element1Id = ironId;
        r8.element2Name = "Сера";
        r8.equation = "Fe + S → FeS";
        r8.conditions = "Нагревание до 500-600°C";
        r8.products = "Сульфид железа (FeS)";
        r8.descriptionReaction = "При нагревании железо реагирует с серой, образуя сульфид железа — чёрное твёрдое вещество.";
        reactionDao.insert(r8);

        Media imgGold = new Media();
        imgGold.elementId = goldId; imgGold.type = "image"; imgGold.filePath = "gold";
        mediaDao.insert(imgGold);

        Media imgOxygen = new Media();
        imgOxygen.elementId = oxygenId; imgOxygen.type = "image"; imgOxygen.filePath = "oxygen";
        mediaDao.insert(imgOxygen);

        Media imgNitrogen = new Media();
        imgNitrogen.elementId = nitrogenId; imgNitrogen.type = "image"; imgNitrogen.filePath = "nitrogen";
        mediaDao.insert(imgNitrogen);

        Media imgIron = new Media();
        imgIron.elementId = ironId; imgIron.type = "image"; imgIron.filePath = "iron";
        mediaDao.insert(imgIron);

        Media imgHydrogen = new Media();
        imgHydrogen.elementId = hydrogenId; imgHydrogen.type = "image"; imgHydrogen.filePath = "hydrogen";
        mediaDao.insert(imgHydrogen);

        Media imgSodium = new Media();
        imgSodium.elementId = sodiumId; imgSodium.type = "image"; imgSodium.filePath = "sodium";
        mediaDao.insert(imgSodium);

        Media imgSulfur = new Media();
        imgSulfur.elementId = sulfurId; imgSulfur.type = "image"; imgSulfur.filePath = "sulfur";
        mediaDao.insert(imgSulfur);

        Media modelGold = new Media();
        modelGold.elementId = goldId;
        modelGold.type = "model3d";
        modelGold.filePath = "test_model";
        mediaDao.insert(modelGold);


        ScanHistory history = new ScanHistory();
        history.elementId = goldId; history.scannedDate = Math.toIntExact(System.currentTimeMillis());
        history.dataType = "Хим.эл."; history.displayText = "Золото";
        scanHistoryDao.insert(history);

        Log.d("DB_test", "База успешно заполнена из DatabaseInitializer!");
    }
}
