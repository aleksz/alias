package com.gmail.at.zhuikov.aleksandr.alias.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

public class AliasDatabase extends SQLiteOpenHelper {

	private static final String TAG = AliasDatabase.class.getName();

    protected static final String DATABASE_NAME = "alias";
    protected static final String PLAYER_PAIR_TABLE = "player_pair";
    protected static final String GAME_TABLE = "game";
    protected static final int DATABASE_VERSION = 1;

    public interface GameColumns extends BaseColumns {
    }

    public interface PlayerPairColumns extends BaseColumns {
        String COLOR = "color";
        String SCORE = "score";
        String GAME = "game";
    }

    public AliasDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + GAME_TABLE + " ("
                + GameColumns._ID + " INTEGER PRIMARY KEY"
                + ");");

        db.execSQL("CREATE TABLE " + PLAYER_PAIR_TABLE + " ("
                + PlayerPairColumns._ID + " INTEGER PRIMARY KEY,"
                + PlayerPairColumns.GAME + " INTEGER,"
                + PlayerPairColumns.COLOR + " TEXT,"
                + PlayerPairColumns.SCORE + " INTEGER"
                + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");

        db.execSQL("DROP TABLE IF EXISTS " + PLAYER_PAIR_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + GAME_TABLE);
        onCreate(db);
    }
}
