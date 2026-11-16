package com.example.qrcodeapp;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MediaDao {
    @Insert
    void insert(Media media);

    @Query("SELECT * FROM media WHERE elementId = :elId")
    List<Media> getMediaForElement(int elId);


}
