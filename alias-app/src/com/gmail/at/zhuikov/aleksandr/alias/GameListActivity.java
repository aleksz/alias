package com.gmail.at.zhuikov.aleksandr.alias;

import static android.content.ContentUris.withAppendedId;

import java.util.Date;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.gmail.at.zhuikov.aleksandr.alias.domain.Game;
import com.gmail.at.zhuikov.aleksandr.alias.domain.GameDao;
import com.gmail.at.zhuikov.aleksandr.alias.domain.GameDaoImpl;
import com.gmail.at.zhuikov.aleksandr.alias.domain.GameProvider;

public class GameListActivity extends ListActivity {

	private GameDao gameDao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Cursor mCursor = this.getContentResolver().query(GameProvider.CONTENT_URI, null, null, null, null);

        startManagingCursor(mCursor);

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                this,
                android.R.layout.simple_list_item_1,
                mCursor,
                new String[] { GameProvider.Columns.MODIFIED },
                new int[] { android.R.id.text1 });

		adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {

			public boolean setViewValue(View view, Cursor cursor, int column) {
				if (column == 1) {
					((TextView) view).setText(new Date(cursor.getLong(column)).toString());
					return true;
				}
				return false;
			}
		});

        setListAdapter(adapter);
        registerForContextMenu(getListView());

        gameDao = new GameDaoImpl(getContentResolver());
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		switch (item.getItemId()) {
		case R.id.delete_game:
			deleteGame(info.id);
			return true;
		case R.id.edit_game:
			editGame(info.id);
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		getMenuInflater().inflate(R.menu.game_list_ctx, menu);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.games, menu);
	    return true;
	}

	private void deleteGame(long id) {
		gameDao.delete(id);
	}

	private void deleteAllGames() {
		gameDao.deleteAllGames();
	}

	private void editGame(long id) {
		startActivity(new Intent(null, withAppendedId(GameProvider.CONTENT_URI, id), this, GameActivity.class));
	}

	private void newGame() {
		Game game = new Game();
		gameDao.save(game);
		editGame(game.getId());
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
        startActivity(new Intent(null, withAppendedId(GameProvider.CONTENT_URI, id), this, BoardActivity.class));
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.new_game:
				newGame();
				return true;
			case R.id.delete_all_games:
				deleteAllGames();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
}
