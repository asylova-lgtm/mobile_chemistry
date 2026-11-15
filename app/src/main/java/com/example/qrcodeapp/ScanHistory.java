package com.example.qrcodeapp;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "scan_history",
        foreignKeys = @ForeignKey(
                entity = Element.class,
                parentColumns = "id",
                childColumns = "elementId",
                onDelete = ForeignKey.SET_NULL
        ))
public class ScanHistory {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "scan_id")
    public int scanId;

    // ОБЯЗАТЕЛЬНО Integer, чтобы можно было сохранить NULL для обычного текста
    @ColumnInfo(name = "elementId")
    public Integer elementId;

    @ColumnInfo(name = "scanned_date")
    public long scannedDate;

    @ColumnInfo(name = "data_type")
    public String dataType;

    @ColumnInfo(name = "display_text")
    public String displayText;

    public ScanHistory() {}
}
