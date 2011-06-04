package com.gmail.at.zhuikov.aleksandr.alias.domain;

import static android.content.ContentUris.parseId;
import static android.content.ContentUris.withAppendedId;
import static android.text.TextUtils.isEmpty;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;

public class PlayerPairProvider extends ContentProvider {

	static final String TABLE_NAME = "player_pair";
	private static final int PLAYER_PAIRS_URI_CODE = 1;
	private static final int PLAYER_PAIR_ID_URI_CODE = 2;
	private static final UriMatcher sUriMatcher;

	public static final String AUTHORITY = PlayerPairProvider.class.getName();
	public static final Uri CONTENT_URI =  Uri.parse("content://" + AUTHORITY + "/player_pairs");

    /**
     * The MIME type of {@link #CONTENT_URI} providing a directory of player pairs.
     */
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.aleksz.player_pair";

    /**
     * The MIME type of a {@link #CONTENT_URI} sub-directory of a single player pair.
     */
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.aleksz.player_pair";

	public static final class Columns implements BaseColumns {
        public static final String COLOR = "color";
        public static final String SCORE = "score";
        public static final String GAME = "game_id";
    }

	private DatabaseHelper mOpenHelper;

	static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(AUTHORITY, "player_pairs", PLAYER_PAIRS_URI_CODE);
        sUriMatcher.addURI(AUTHORITY, "player_pairs/#", PLAYER_PAIR_ID_URI_CODE);
    }

	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int count;
        switch (sUriMatcher.match(uri)) {
        case PLAYER_PAIRS_URI_CODE:
            count = db.delete(TABLE_NAME, where, whereArgs);
            break;

        case PLAYER_PAIR_ID_URI_CODE:
            count = db.delete(TABLE_NAME, Columns._ID+ "=" + parseId(uri)
                    + (!isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
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
		case PLAYER_PAIRS_URI_CODE:
			return CONTENT_TYPE;

		case PLAYER_PAIR_ID_URI_CODE:
			return CONTENT_ITEM_TYPE;

		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		long rowId = db.insert(TABLE_NAME, null, values);

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
		case PLAYER_PAIRS_URI_CODE:
			break;
		case PLAYER_PAIR_ID_URI_CODE:
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
		count = db.update(TABLE_NAME, values, Columns._ID
				+ "=" + parseId(uri)
				+ (!isEmpty(where) ? " AND (" + where + ')' : ""),
				whereArgs);
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	protected DatabaseHelper getDatabaseHelper() {
		return mOpenHelper;
	}
}
