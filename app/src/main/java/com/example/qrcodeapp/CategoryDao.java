package com.example.qrcodeapp;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface CategoryDao {
    @Insert void insert(Categories categories);

    @Query("select * from categories where categories_id = :id limit 1")
    Categories getCategoryById(int id);
}
