package com.gmail.at.zhuikov.aleksandr.alias.domain;

import static android.content.ContentUris.parseId;
import static android.content.ContentUris.withAppendedId;
import static android.text.TextUtils.isEmpty;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;

public class GameProvider extends ContentProvider {

	static final String TABLE_NAME = "game";
	private static final int GAMES_URI_CODE = 1;
	private static final int GAME_ID_URI_CODE = 2;
	public static final String AUTHORITY = GameProvider.class.getName();
	public static final Uri CONTENT_URI =  Uri.parse("content://" + AUTHORITY + "/games");
	/**
	 * The MIME type of a {@link GameProvider#CONTENT_URI} sub-directory of a single game.
	 */
	public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.aleksz.game";
	/**
	 * The MIME type of {@link GameProvider#CONTENT_URI} providing a directory of games.
	 */
	public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.aleksz.game";

	public static final class Columns implements BaseColumns {
		public static String MODIFIED = "modified";
		public static String LAST_PLAYER_ID = "last_player_id";
    }

	private DatabaseHelper mOpenHelper;
	private static UriMatcher sUriMatcher;

	static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(AUTHORITY, "games", GAMES_URI_CODE);
        sUriMatcher.addURI(AUTHORITY, "games/#", GAME_ID_URI_CODE);
    }

	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int count;
        switch (sUriMatcher.match(uri)) {
        case GAMES_URI_CODE:
            count = db.delete(TABLE_NAME, where, whereArgs);
            break;

        case GAME_ID_URI_CODE:
            count = db.delete(TABLE_NAME, Columns._ID+ "=" + parseId(uri)
                    + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
            break;

        default:
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
	}

	@Override
	public String getType(Uri uri) {
		switch (sUriMatcher.match(uri)) {
		case GAMES_URI_CODE:
			return CONTENT_TYPE;

		case GAME_ID_URI_CODE:
			return CONTENT_ITEM_TYPE;

		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		long rowId = db.insert(TABLE_NAME, Columns.MODIFIED, values);

		if (rowId > 0) {
			Uri pairUri = withAppendedId(CONTENT_URI, rowId);
			getContext().getContentResolver().notifyChange(pairUri, null);
			return pairUri;
		}

		throw new SQLException("Failed to insert row into " + uri);
	}

	@Override
	public boolean onCreate() {
		mOpenHelper = new DatabaseHelper(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String orderBy) {

		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(TABLE_NAME);

		switch (sUriMatcher.match(uri)) {
		case GAMES_URI_CODE:
			break;
		case GAME_ID_URI_CODE:
			qb.appendWhere(Columns._ID + "=" + parseId(uri));
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		SQLiteDatabase db = mOpenHelper.getReadableDatabase();
		Cursor c = qb.query(db, projection, selection, selectionArgs, null,
				null, orderBy);
		c.setNotificationUri(getContext().getContentResolver(), uri);

		return c;
	}

	@Override
	public int update(Uri uri, ContentValues values, String where,
			String[] whereArgs) {

		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		int count;
		long id = ContentUris.parseId(uri);
		count = db.update(TABLE_NAME, values, Columns._ID + "=" + id
				+ (!isEmpty(where) ? " AND (" + where + ')' : ""),
				whereArgs);
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	protected DatabaseHelper getDatabaseHelper() {
		return mOpenHelper;
	}
}
