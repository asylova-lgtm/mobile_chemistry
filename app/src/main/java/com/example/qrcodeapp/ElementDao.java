package com.example.qrcodeapp;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ElementDao {
    @Insert
    void insert(Element elements); //заполнение таблицы

    @Query("Select * from elements") // метод для получения всех элементов из таблицы
    List<Element> getAllElements();

    @Query("Select * from elements where name_en = :engName limit 1") //метод для поиска по анг. названию
    Element getElementByEnglishName(String engName);
}
