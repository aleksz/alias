package com.gmail.at.zhuikov.aleksandr.alias.domain;

import static android.content.ContentUris.withAppendedId;
import static com.gmail.at.zhuikov.aleksandr.alias.domain.GameProvider.AUTHORITY;
import static com.gmail.at.zhuikov.aleksandr.alias.domain.GameProvider.CONTENT_URI;
import static com.gmail.at.zhuikov.aleksandr.alias.domain.GameProvider.TABLE_NAME;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.ProviderTestCase2;


public class GameProviderTest extends ProviderTestCase2<GameProvider> {

	private SQLiteDatabase db;

	public GameProviderTest() {
		super(GameProvider.class, AUTHORITY);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		db = ((GameProvider) getProvider()).getDatabaseHelper().getWritableDatabase();
	}

	public void testDelete() {
		createTestGame(1, 2);
		createTestGame(2, 3);
		int r = getMockContentResolver().delete(withAppendedId(CONTENT_URI, 2), null, null);
		assertEquals(1, r);
		Cursor c = db.query(TABLE_NAME, null, null, null, null, null, null);
		assertEquals(1, c.getCount());
	}

	public void testQueryAllPairs() {
		createTestGame(1, 2);
		createTestGame(3, 4);

		Cursor c = getMockContentResolver().query(CONTENT_URI, null, null, null, null);

		assertTrue(c.moveToFirst());
		assertEquals(1, c.getInt(0));
		assertEquals(2, c.getInt(1));
		assertEquals(1, c.getInt(2));

		assertTrue(c.moveToNext());
		assertEquals(2, c.getInt(0));
		assertEquals(4, c.getInt(1));
		assertEquals(3, c.getInt(2));

		assertFalse(c.moveToNext());
	}

	public void testQueryById() {
		createTestGame(1, 2);
		createTestGame(3, 4);

		Cursor c = getMockContentResolver().query(withAppendedId(CONTENT_URI, 2), null, null, null, null);

		assertTrue(c.moveToFirst());
		assertEquals(2, c.getInt(0));
		assertEquals(4, c.getInt(1));
		assertEquals(3, c.getInt(2));
		assertFalse(c.moveToNext());
	}

	private void createTestGame(long lastPlayer, long modified) {
		ContentValues values = new ContentValues();
		values.put(GameProvider.Columns.LAST_PLAYER_ID, lastPlayer);
		values.put(GameProvider.Columns.MODIFIED, modified);

		db.insert(TABLE_NAME, null, values);
	}
}
