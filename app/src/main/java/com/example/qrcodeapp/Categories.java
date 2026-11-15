package com.example.qrcodeapp;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "categories")
public class Categories {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "categories_id")
    public int categoriesId;

    @ColumnInfo(name = "name_categories")
    public String nameCategories;

    public Categories() {}
}
