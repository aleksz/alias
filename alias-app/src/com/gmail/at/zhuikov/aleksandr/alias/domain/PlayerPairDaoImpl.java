package com.gmail.at.zhuikov.aleksandr.alias.domain;

import static android.content.ContentUris.withAppendedId;
import static com.gmail.at.zhuikov.aleksandr.alias.domain.PlayerPairProvider.CONTENT_URI;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

public class PlayerPairDaoImpl implements PlayerPairDao {

	private final ContentResolver resolver;

	public PlayerPairDaoImpl(ContentResolver resolver) {
		this.resolver = resolver;
	}

	public void save(PlayerPair playerPair) {

		ContentValues values = new ContentValues();
		values.put(PlayerPairProvider.Columns.COLOR, playerPair.getColor());
		values.put(PlayerPairProvider.Columns.SCORE, playerPair.getScore());
		values.put(PlayerPairProvider.Columns.GAME, playerPair.getGameId());

		if (playerPair.isNew()) {
			Uri uri = resolver.insert(playerPair.getUri(), values);
			playerPair.setId(ContentUris.parseId(uri));
		} else {
			resolver.update(playerPair.getUri(), values, null, null);
		}
	}

	public PlayerPair load(long id) {
		String[] projection = { PlayerPairProvider.Columns._ID,
				PlayerPairProvider.Columns.COLOR,
				PlayerPairProvider.Columns.SCORE,
				PlayerPairProvider.Columns.GAME };

		Cursor cursor = resolver.query(withAppendedId(CONTENT_URI, id), projection, null, null, null);
		cursor.moveToFirst();

		return new PlayerPair(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2), cursor.getInt(3));
	}

	public void delete(long id) {
		resolver.delete(withAppendedId(CONTENT_URI, id), null, null);
	}

	public void deleteAllFromGame(long gameId) {
		resolver.delete(
				CONTENT_URI,
				PlayerPairProvider.Columns.GAME +"=?",
				new String[] { String.valueOf(gameId) });
	}
}
