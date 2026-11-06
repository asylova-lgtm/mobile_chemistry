package com.example.qrcodeapp;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MediaDao {
    @Insert
    void insert(Media media);

    @Query("Select * from media where element_id = :ElID and type = 'image'")
    List<Media> getImagesElement(int ElID);
}
