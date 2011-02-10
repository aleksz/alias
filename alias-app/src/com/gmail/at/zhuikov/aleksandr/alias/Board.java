package com.gmail.at.zhuikov.aleksandr.alias;

import com.gmail.at.zhuikov.aleksandr.alias.domain.Game;
import com.gmail.at.zhuikov.aleksandr.alias.domain.PlayerPair;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


public class Board extends Activity {

	private Game game;
	private TextView stats;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board);
        game = new Game(1);
        game.getPlayerPairs().add(new PlayerPair(1, game, "red", 0));
        stats = (TextView) findViewById(R.id.statistics);
        stats.setText(game.toString());
    }

	public void startRound(View button) {
    	Intent intent = new Intent(this, Deck.class);
    	intent.putExtra("game", game);
		startActivity(intent);
    }
}
