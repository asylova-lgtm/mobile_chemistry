package com.example.qrcodeapp;

import androidx.room.Dao;
import androidx.room.Insert;

@Dao
public interface ReactionDao {
    @Insert void insert(Reaction reaction);
}
