package com.example.qrcodeapp;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "media",
        foreignKeys = @ForeignKey(
                entity = Element.class,
                parentColumns = "id",
                childColumns = "elementId",
                onDelete = ForeignKey.CASCADE))
public class Media {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "media_id") public int mediaId;

    @ColumnInfo(name = "elementId") public int elementId;
    @ColumnInfo(name = "type") public String type;
    @ColumnInfo(name = "file_path") public String filePath;
    @ColumnInfo(name = "description") public String description;

    public Media() {}
}
