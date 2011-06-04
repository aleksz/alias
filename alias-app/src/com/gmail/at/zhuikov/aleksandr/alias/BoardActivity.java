package com.gmail.at.zhuikov.aleksandr.alias;

import static android.content.ContentUris.parseId;
import static android.content.ContentUris.withAppendedId;
import static com.gmail.at.zhuikov.aleksandr.alias.DeckActivity.NEW_ROUND_REQUEST;
import static com.gmail.at.zhuikov.aleksandr.alias.domain.PlayerPairProvider.CONTENT_URI;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.gmail.at.zhuikov.aleksandr.alias.domain.Game;
import com.gmail.at.zhuikov.aleksandr.alias.domain.GameDao;
import com.gmail.at.zhuikov.aleksandr.alias.domain.GameDaoImpl;
import com.gmail.at.zhuikov.aleksandr.alias.domain.PlayerPairDao;
import com.gmail.at.zhuikov.aleksandr.alias.domain.PlayerPairDaoImpl;


public class BoardActivity extends Activity {

	private TextView stats;
	private PlayerPairDao playerPairDao;
	private Game game;
	private GameDao gameDao;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board);
        gameDao = new GameDaoImpl(getContentResolver());
		game = gameDao.load(parseId(getIntent().getData()));
        playerPairDao = new PlayerPairDaoImpl(getContentResolver());
        stats = (TextView) findViewById(R.id.statistics);
        updateGameState();
    }

	public void startRound(View button) {
    	Uri uri = withAppendedId(CONTENT_URI, game.getNextPlayerId());
		startActivityForResult(new Intent(null, uri, this, DeckActivity.class), NEW_ROUND_REQUEST);
    }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK && requestCode == NEW_ROUND_REQUEST) {
			game.setLastPlayerId(game.getNextPlayerId());
			gameDao.save(game);
			updateGameState();
		}
	}

	private void updateGameState() {
		StringBuilder s = new StringBuilder();
		s.append(game);
		s.append("\n");
		for (long playerPairId : game.getPlayerPairIds()) {
			s.append(playerPairDao.load(playerPairId));
			s.append("\n");
		}
		stats.setText(s);
	}
}
