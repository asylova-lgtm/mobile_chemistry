package com.example.qrcodeapp;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Element.class, Media.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private  static  AppDatabase instance;
    public abstract ElementDao elementDao();
    public abstract MediaDao mediaDao();
    public static synchronized AppDatabase getInstance(Context context){
        if(instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "test_bd").allowMainThreadQueries().fallbackToDestructiveMigration().build();
        }
        return instance;
    }
}
