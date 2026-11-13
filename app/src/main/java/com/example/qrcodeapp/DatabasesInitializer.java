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

        Categories noMetals = new Categories();
        noMetals.NameCategories = "Неметалл";
        categoryDao.insert(noMetals);

        Categories metals = new Categories();
        metals.NameCategories = "Металл";
        categoryDao.insert(metals);

        int mId = categoryDao.getCategoryById(1).CategoriesId;
        int nMid = categoryDao.getCategoryById(2).CategoriesId; //id неметалла

        //добавление элементов
        Element gold = new Element();
        gold.symbol = "Au"; gold.NameEn = "gold"; gold.NameRu = "Золото";
        gold.CategoriesId = mId; gold.AtomicNumber = 79;
        gold.Description = "Желтый драгоценный металл.";
        gold.Uses = "Ювелирные изделия.";
        elementDao.insert(gold);

        Element oxygen = new Element();
        oxygen.symbol = "O"; oxygen.NameEn = "oxygen"; oxygen.NameRu = "Кислород";
        oxygen.CategoriesId = nMid; oxygen.AtomicNumber = 8;
        oxygen.Description = "Газ без цвета и запаха. Необходим для дыхания всех живых организмов и горения.";
        oxygen.Uses = "Используется в медицине, металлургии, ракетном топливе, химической промышленности.";
        elementDao.insert(oxygen);

        Element nitrogen = new Element();
        nitrogen.symbol = "N"; nitrogen.NameEn = "nitrogen"; nitrogen.NameRu = "Азот";
        nitrogen.CategoriesId = nMid; nitrogen.AtomicNumber = 7;
        nitrogen.Description = "Инертный газ без цвета и запаха. Основной компонент атмосферы Земли (78%).";
        nitrogen.Uses = "Используется для создания инертной среды, в производстве аммиака и удобрений, для замораживания продуктов.";
        elementDao.insert(nitrogen);

        Element iron = new Element();
        iron.symbol = "Fe"; iron.NameEn = "iron"; iron.NameRu = "Железо";
        iron.CategoriesId = mId; iron.AtomicNumber = 26;
        iron.Description = "Ковкий металл серебристо-белого цвета. Основной компонент стали.";
        iron.Uses = "Используется в строительстве, машиностроении, транспорте, биологии (гемоглобин).";
        elementDao.insert(iron);

        int goldId = elementDao.getElementByEnglishName("gold").ElementId;
        int oxygenId = elementDao.getElementByEnglishName("oxygen").ElementId;
        int nitrogenId = elementDao.getElementByEnglishName("nitrogen").ElementId;
        int ironId = elementDao.getElementByEnglishName("iron").ElementId;

        Media imgGold = new Media();
        imgGold.elementID = goldId; imgGold.type = "image"; imgGold.filePath = "gold.png";
        mediaDao.insert(imgGold);

        Media imgOxygen = new Media();
        imgOxygen.elementID = oxygenId; imgOxygen.type = "image"; imgOxygen.filePath = "oxygen.png";
        mediaDao.insert(imgOxygen);

        Media imgNitrogen = new Media();
        imgNitrogen.elementID = nitrogenId; imgNitrogen.type = "image"; imgNitrogen.filePath = "nitrogen.png";
        mediaDao.insert(imgNitrogen);

        Media imgIron = new Media();
        imgIron.elementID = ironId; imgIron.type = "image"; imgIron.filePath = "gold.png";
        mediaDao.insert(imgIron);

    }
}
