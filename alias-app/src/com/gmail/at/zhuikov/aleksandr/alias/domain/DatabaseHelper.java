package com.gmail.at.zhuikov.aleksandr.alias.domain;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	static final String DATABASE_NAME = "alias.db";

    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + GameProvider.TABLE_NAME + " ("
                + GameProvider.Columns._ID + " INTEGER PRIMARY KEY,"
                + GameProvider.Columns.MODIFIED + " INTEGER,"
                + GameProvider.Columns.LAST_PLAYER_ID + " INTEGER"
                + ");");

        db.execSQL("CREATE TABLE " + PlayerPairProvider.TABLE_NAME + " ("
                + PlayerPairProvider.Columns._ID + " INTEGER PRIMARY KEY,"
                + PlayerPairProvider.Columns.COLOR + " INTEGER,"
                + PlayerPairProvider.Columns.SCORE + " INTEGER,"
                + PlayerPairProvider.Columns.GAME + " INTEGER"
                + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + GameProvider.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + PlayerPairProvider.TABLE_NAME);
        onCreate(db);
    }
}