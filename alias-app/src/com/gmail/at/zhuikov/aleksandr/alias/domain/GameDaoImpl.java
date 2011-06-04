package com.gmail.at.zhuikov.aleksandr.alias.domain;

import static android.content.ContentUris.parseId;
import static android.content.ContentUris.withAppendedId;
import static com.gmail.at.zhuikov.aleksandr.alias.domain.GameProvider.CONTENT_URI;

import java.util.Date;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

public class GameDaoImpl implements GameDao {

	private final ContentResolver resolver;
	private PlayerPairDao playerPairDao;

	public GameDaoImpl(ContentResolver resolver) {
		this.resolver = resolver;
		playerPairDao = new PlayerPairDaoImpl(resolver);
	}

	public void save(Game game) {

		ContentValues values = new ContentValues();
		values.put(GameProvider.Columns.MODIFIED, new Date().getTime());
		values.put(GameProvider.Columns.LAST_PLAYER_ID, game.getLastPlayerId());

		if (game.isNew()) {
			Uri uri = resolver.insert(CONTENT_URI, values);
			game.setId(parseId(uri));
		} else {
			resolver.update(withAppendedId(CONTENT_URI, game.getId()), values, null, null);
		}
	}

	public Game load(long id) {

		Cursor cursor = resolver.query(withAppendedId(CONTENT_URI, id), null, null, null, null);
		Cursor pairsCursor = resolver.query(
				PlayerPairProvider.CONTENT_URI,
				new String[] { PlayerPairProvider.Columns._ID },
				PlayerPairProvider.Columns.GAME + "=?",
				new String[] { String.valueOf(id) },
				null);

	    long[] pairs = new long[pairsCursor.getCount()];

	    for (int i = 0; pairsCursor.moveToNext(); i++) {
	    	pairs[i] = pairsCursor.getLong(0);
	    }

	    cursor.moveToFirst();
	    return new Game(cursor.getLong(0), new Date(cursor.getLong(1)), cursor.getLong(2), pairs);
	}

	public void delete(long id) {
		playerPairDao.deleteAllFromGame(id);
		resolver.delete(withAppendedId(GameProvider.CONTENT_URI, id), null, null);
	}

	public void deleteAllGames() {
		resolver.delete(PlayerPairProvider.CONTENT_URI, null, null);
		resolver.delete(GameProvider.CONTENT_URI, null, null);
	}
}
