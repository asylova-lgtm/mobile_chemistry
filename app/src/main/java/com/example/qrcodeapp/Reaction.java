package com.example.qrcodeapp;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "reactions",
        foreignKeys = {
                @ForeignKey(entity = Element.class, parentColumns = "id", childColumns = "element1Id", onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Element.class, parentColumns = "id", childColumns = "element2Id", onDelete = ForeignKey.CASCADE)
        })
public class Reaction {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "reactions_id") public int reactionsId;

    @ColumnInfo(name = "element1Id") public int element1Id;
    @ColumnInfo(name = "element2Id") public int element2Id;
    @ColumnInfo(name = "equation") public String equation;
    @ColumnInfo(name = "conditions") public String conditions;
    @ColumnInfo(name = "products") public String products;
    @ColumnInfo(name = "description_reaction") public String descriptionReaction;

    public Reaction() {}
}
