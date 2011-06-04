package com.gmail.at.zhuikov.aleksandr.alias;

import static android.content.ContentUris.parseId;
import static com.gmail.at.zhuikov.aleksandr.alias.domain.PlayerPairProvider.CONTENT_URI;
import android.app.ListActivity;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;

import com.gmail.at.zhuikov.aleksandr.alias.domain.PlayerPair;
import com.gmail.at.zhuikov.aleksandr.alias.domain.PlayerPairDao;
import com.gmail.at.zhuikov.aleksandr.alias.domain.PlayerPairDaoImpl;
import com.gmail.at.zhuikov.aleksandr.alias.domain.PlayerPairProvider;

public class GameActivity extends ListActivity {

	private Cursor mCursor;
	private long gameId;
	private PlayerPairDao playerPairDao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		gameId = parseId(getIntent().getData());

		mCursor = this.getContentResolver().query(
				CONTENT_URI,
				new String[] { PlayerPairProvider.Columns._ID, PlayerPairProvider.Columns.COLOR },
				PlayerPairProvider.Columns.GAME + "= ?",
				new String[] { String.valueOf(gameId) },
				null);

        startManagingCursor(mCursor);

        ListAdapter adapter = new SimpleCursorAdapter(
                this,
                android.R.layout.simple_list_item_1,
                mCursor,
                new String[] { PlayerPairProvider.Columns.COLOR },
                new int[] { android.R.id.text1 });

        setListAdapter(adapter);
        registerForContextMenu(getListView());
        playerPairDao = new PlayerPairDaoImpl(getContentResolver());
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		getMenuInflater().inflate(R.menu.game_ctx, menu);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.game, menu);
	    return true;
	}

	private void newPlayerPair(int color) {
		playerPairDao.save(new PlayerPair(color, gameId));
	}

	private void deletePlayerPair(long id) {
		playerPairDao.delete(id);
	}

	private void deleteAllPlayerPairs() {
		playerPairDao.deleteAllFromGame(gameId);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		switch (item.getItemId()) {
		case R.id.delete_player_pair:
			deletePlayerPair(info.id);
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.player_pair_color_1:
			newPlayerPair(Color.BLUE);
			return true;
		case R.id.player_pair_color_2:
			newPlayerPair(Color.RED);
			return true;
		case R.id.delete_all_player_pairs:
			deleteAllPlayerPairs();
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
