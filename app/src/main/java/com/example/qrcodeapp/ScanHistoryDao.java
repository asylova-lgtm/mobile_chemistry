package com.example.qrcodeapp;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ScanHistoryDao {
    @Insert void insert(ScanHistory scanHistory);
    @Query("select * from scan_history order by scanned_date desc")
    List<ScanHistory> getAllHistory();
    @Query("DELETE FROM scan_history")
    void deleteAll();
}
