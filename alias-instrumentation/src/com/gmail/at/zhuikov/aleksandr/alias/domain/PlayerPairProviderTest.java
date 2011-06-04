package com.gmail.at.zhuikov.aleksandr.alias.domain;

import static android.content.ContentUris.withAppendedId;
import static com.gmail.at.zhuikov.aleksandr.alias.domain.PlayerPairProvider.AUTHORITY;
import static com.gmail.at.zhuikov.aleksandr.alias.domain.PlayerPairProvider.CONTENT_URI;
import static com.gmail.at.zhuikov.aleksandr.alias.domain.PlayerPairProvider.TABLE_NAME;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.ProviderTestCase2;


public class PlayerPairProviderTest extends ProviderTestCase2<PlayerPairProvider> {

	private SQLiteDatabase db;

	public PlayerPairProviderTest() {
		super(PlayerPairProvider.class, AUTHORITY);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		db = ((PlayerPairProvider) getProvider()).getDatabaseHelper().getWritableDatabase();
	}

	public void testDelete() {
		createTestPair(111, 222, 333);
		createTestPair(111, 222, 333);
		int r = getMockContentResolver().delete(withAppendedId(CONTENT_URI, 2), null, null);
		assertEquals(1, r);
		Cursor c = db.query(TABLE_NAME, null, null, null, null, null, null);
		assertEquals(1, c.getCount());
	}

	public void testDeleteWithCondition() {
		createTestPair(111, 222, 332);
		createTestPair(111, 222, 333);
		int r = getMockContentResolver().delete(
				CONTENT_URI,
				PlayerPairProvider.Columns.GAME + "=?",
				new String[] { "333" });
		assertEquals(1, r);
		Cursor c = db.query(TABLE_NAME, null, null, null, null, null, null);
		assertEquals(1, c.getCount());
	}

	public void testQueryAllPairs() {
		createTestPair(111, 222, 333);
		createTestPair(112, 223, 334);

		Cursor c = getMockContentResolver().query(CONTENT_URI, null, null, null, null);

		assertTrue(c.moveToFirst());
		assertEquals(1, c.getInt(0));
		assertEquals(111, c.getInt(1));
		assertEquals(222, c.getInt(2));
		assertEquals(333, c.getInt(3));

		assertTrue(c.moveToNext());
		assertEquals(2, c.getInt(0));
		assertEquals(112, c.getInt(1));
		assertEquals(223, c.getInt(2));
		assertEquals(334, c.getInt(3));

		assertFalse(c.moveToNext());
	}

	public void testQueryById() {
		createTestPair(111, 222, 333);
		createTestPair(112, 223, 334);

		Cursor c = getMockContentResolver().query(withAppendedId(CONTENT_URI, 2), null, null, null, null);

		assertTrue(c.moveToFirst());
		assertEquals(2, c.getInt(0));
		assertEquals(112, c.getInt(1));
		assertEquals(223, c.getInt(2));
		assertEquals(334, c.getInt(3));
		assertFalse(c.moveToNext());
	}

	private void createTestPair(int color, int score, int game) {
		ContentValues values = new ContentValues();
		values.put(PlayerPairProvider.Columns.COLOR, color);
		values.put(PlayerPairProvider.Columns.SCORE, score);
		values.put(PlayerPairProvider.Columns.GAME, game);

		db.insert(TABLE_NAME, null, values);
	}
}
