package com.example.explorev3;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.explorev3.FavoriteContract.*;

import androidx.annotation.Nullable;

public class FavoriteDBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "favorite.db";
    public static final int DATABASE_VERSION = 1;

    public FavoriteDBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_FAVORITE_TABLE = "CREATE TABLE " +
            FavoriteEntry.TABLE_NAME + " (" +
                FavoriteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FavoriteEntry.COLLUMN_XID + " TEXT NOT NULL, " +
                FavoriteEntry.COLLUMN_NAME + " TEXT NOT NULL, " +
                FavoriteEntry.COLLUMN_RATING + " TEXT NOT NULL, " +
                FavoriteEntry.COLLUMN_LAT + " TEXT NOT NULL, " +
                FavoriteEntry.COLLUMN_LON + " TEXT NOT NULL, " +
                FavoriteEntry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ");";

        db.execSQL(SQL_CREATE_FAVORITE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FavoriteEntry.TABLE_NAME);
        onCreate(db);
    }
}
