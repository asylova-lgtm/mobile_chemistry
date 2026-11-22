package com.example.qrcodeapp;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ReactionDao {
    @Insert void insert(Reaction reaction);
    @Query("SELECT * FROM reactions WHERE element1Id = :id1 AND element2Name = :name2 LIMIT 1")
    Reaction getReaction(int id1, String name2);
}