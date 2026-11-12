package com.example.qrcodeapp;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "reactions", foreignKeys = {
        @ForeignKey(
                entity = Element.class, parentColumns = "element_id",
                childColumns = "element1_id",onDelete = ForeignKey.CASCADE),
        @ForeignKey(
                entity = Element.class, parentColumns = "element_id",
                childColumns = "element2_id",onDelete = ForeignKey.CASCADE)
})
public class Reaction {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "reaction_id")
    public int ReactionId;

    @ColumnInfo(name = "element1_id")
    public int Element1;

    @ColumnInfo(name = "element2_id")
    public int Element2;

    @ColumnInfo(name = "equation")
    public String Equation;

    @ColumnInfo(name = "conditions")
    public String Conditions;

    @ColumnInfo(name = "products")
    public String Products;

    @ColumnInfo(name = "description")
    public String Description;

    public Reaction() {}
}
