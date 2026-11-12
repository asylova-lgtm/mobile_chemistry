package com.example.qrcodeapp;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "media", foreignKeys = @ForeignKey(
        entity = Element.class,
        parentColumns = "id",
        childColumns = "element_id",
        onDelete = ForeignKey.CASCADE
))

public class Media {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "media_id")
    public int MediaId;

    @ColumnInfo(name = "element_id")
    public int elementID;

    @ColumnInfo(name = "type")
    public String type;

    @ColumnInfo(name = "file_path")
    public String filePath;

    @ColumnInfo(name = "description")
    public String Description;

    public Media(){}
}
