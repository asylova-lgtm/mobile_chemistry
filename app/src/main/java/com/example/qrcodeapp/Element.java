package com.example.qrcodeapp;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "elements", foreignKeys = @ForeignKey(
        entity = Categories.class,
        parentColumns = "id",
        childColumns = "categories_id",
        onDelete = ForeignKey.SET_NULL))

//создание класса-сущности для элементов
public class Element {
    @PrimaryKey(autoGenerate = true) //под вопросом, как вариант сделать id атомный номер элемента в таблице М.
    @ColumnInfo(name = "element_id")
    public int ElementId;

    @ColumnInfo(name = "symbol")
    public String symbol;

    @ColumnInfo(name = "name_ru")
    public String NameRu;

    @ColumnInfo(name = "name_en")
    public String NameEn;

    @ColumnInfo(name = "atomic_number")
    public int AtomicNumber;

    @ColumnInfo(name = "description")
    public String Description;

    @ColumnInfo(name = "uses")
    public String Uses;

    @ColumnInfo(name = "categories_id")
    public String CategoriesId;

    public Element() {}

}
