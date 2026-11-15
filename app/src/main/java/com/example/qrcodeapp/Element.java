package com.example.qrcodeapp;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "elements",
        foreignKeys = @ForeignKey(
                entity = Categories.class,
                parentColumns = "categories_id",
                childColumns = "categoryId",
                onDelete = ForeignKey.SET_NULL))
public class Element {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;

    @ColumnInfo(name = "symbol") public String symbol;
    @ColumnInfo(name = "name_ru") public String nameRu;
    @ColumnInfo(name = "name_en") public String nameEn;
    @ColumnInfo(name = "atomic_number") public int atomicNumber;
    @ColumnInfo(name = "description") public String description;
    @ColumnInfo(name = "uses") public String uses;

    @ColumnInfo(name = "categoryId")
    public Integer categoryId; // Может быть null

    public Element() {}
}
