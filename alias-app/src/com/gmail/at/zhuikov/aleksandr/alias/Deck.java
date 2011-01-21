package com.gmail.at.zhuikov.aleksandr.alias;

import com.gmail.at.zhuikov.aleksandr.alias.domain.Game;
import com.gmail.at.zhuikov.aleksandr.alias.words.Words;
import com.gmail.at.zhuikov.aleksandr.alias.words.WordsFromResources;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Deck extends Activity {

	private Words words;
    private TextView wordView;
    private ProgressBar progress;
    private boolean isRoundFinished;
    private int score;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        words = new WordsFromResources(getResources());
        setContentView(R.layout.deck);
        wordView = (TextView) findViewById(R.id.word);
        progress = (ProgressBar) findViewById(R.id.ProgressBar01);
        new Timer().start();
    }

    public void skipWord(View button) {

    	score--;

    	if (isRoundFinished) {
    		Log.i("Deck", "Round is finished, closing activity");
    		finish();
    	}

    	wordView.setText(words.next());
    }

    public void nextWord(View button) {

    	score++;

    	if (isRoundFinished) {
    		Log.i("Deck", "Round is finished, closing activity");
    		finish();
    	}

    	wordView.setText(words.next());
    }

	private class Timer extends CountDownTimer {

		public Timer() {
			super(1000 * 60, 600);
		}

		@Override
		public void onFinish() {
			isRoundFinished = true;
		}

		@Override
		public void onTick(long millisUntilFinished) {
			progress.incrementProgressBy(1);
		}
    }
}
