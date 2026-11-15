package com.example.qrcodeapp;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE) // <--- ВОТ ЭТО ДОБАВИТЬ
    void insert(Categories category);

    @Query("SELECT * FROM categories WHERE name_categories = :name LIMIT 1")
    Categories getCategoryByName(String name);

    @Query("SELECT * FROM categories WHERE categories_id = :id LIMIT 1")
    Categories getCategoryById(int id);
}
