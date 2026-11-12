package com.example.qrcodeapp;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "scan_history", foreignKeys = @ForeignKey(
        entity = Element.class,
        parentColumns = "element_id",
        childColumns = "element_Id",
        onDelete = ForeignKey.SET_NULL
))
public class ScanHistory {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "scan_id")
    public int ScanId;

    @ColumnInfo(name = "element_id")
    public int Element;

    @ColumnInfo(name = "scanned_date")
    public int ScannedDate;

    @ColumnInfo(name = "data_type")
    public int DataType;

    @ColumnInfo(name = "display_text")
    public int DisplayText;

    public ScanHistory() {}
}
