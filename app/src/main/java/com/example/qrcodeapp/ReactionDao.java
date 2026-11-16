package com.example.qrcodeapp;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ReactionDao {
    @Insert void insert(Reaction reaction);
    @Query("select * from reactions where element1Id = :elID")
    List<Reaction> getReactionsForElement(int elID);
}