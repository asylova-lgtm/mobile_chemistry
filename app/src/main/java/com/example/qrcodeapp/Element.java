package com.example.qrcodeapp;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "element") //создание класса-сущности для элементов
public class Element {
    @PrimaryKey(autoGenerate = true) //под вопросом, как вариант сделать id атомный номер элемента в таблице М.
    public int id;

    @ColumnInfo(name = "symbol")
    public String symbol;

    @ColumnInfo(name = "name_ru")
    public String NameRu;

    @ColumnInfo(name = "name_en")
    public String NameEn;

    public Element() {}

}
