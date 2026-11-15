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

    @Query("SELECT * FROM elements WHERE name_en = :engName LIMIT 1")
    Element getElementByEnglishName(String engName);
    @Query("SELECT * FROM elements WHERE id = :id LIMIT 1")
    Element getElementById(int id);
}
