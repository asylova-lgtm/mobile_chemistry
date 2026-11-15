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


        ScanHistory history = new ScanHistory();
        history.elementId = goldId; history.scannedDate = Math.toIntExact(System.currentTimeMillis());
        history.dataType = "Хим.эл."; history.displayText = "Золото";
        scanHistoryDao.insert(history);

        Log.d("DB_test", "База успешно заполнена из DatabaseInitializer!");
    }
}
