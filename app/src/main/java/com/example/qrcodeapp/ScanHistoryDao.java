package com.example.qrcodeapp;

import androidx.room.Dao;
import androidx.room.Insert;

@Dao
public interface ScanHistoryDao {
    @Insert void insert(ScanHistory scanHistory);
}
