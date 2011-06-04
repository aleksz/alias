package com.gmail.at.zhuikov.aleksandr.alias;

import static android.content.ContentUris.parseId;
import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gmail.at.zhuikov.aleksandr.alias.domain.PlayerPair;
import com.gmail.at.zhuikov.aleksandr.alias.domain.PlayerPairDao;
import com.gmail.at.zhuikov.aleksandr.alias.domain.PlayerPairDaoImpl;
import com.gmail.at.zhuikov.aleksandr.alias.words.Words;
import com.gmail.at.zhuikov.aleksandr.alias.words.WordsFromResources;

public class DeckActivity extends Activity {

	public static final int NEW_ROUND_REQUEST = 1;

	private Words words;
    private TextView wordView;
    private ProgressBar progress;
    private boolean isRoundFinished;
    private PlayerPair playerPair;
    private PlayerPairDao playerPairDao;
    private MediaPlayer roundEndAlarm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        words = new WordsFromResources(getResources());
		playerPairDao = new PlayerPairDaoImpl(getContentResolver());
		playerPair = playerPairDao.load(parseId(getIntent().getData()));

		setContentView(R.layout.deck);
		wordView = (TextView) findViewById(R.id.word);
        progress = (ProgressBar) findViewById(R.id.ProgressBar01);
        new Timer().start();
        roundEndAlarm = MediaPlayer.create(this, R.raw.camera_click);
    }

    public void skipWord(View button) {

    	playerPair.decreaseScore();

    	if (isRoundFinished) {
    		onRoundFinished();
    		return;
    	}

    	wordView.setText(words.next());
    }

    public void nextWord(View button) {

    	playerPair.increaseScore();

    	if (isRoundFinished) {
    		onRoundFinished();
    		return;
    	}

    	wordView.setText(words.next());
    }

    private void onRoundFinished() {
    	Log.i("DeckActivity", "Round is finished, closing activity");
    	playerPairDao.save(playerPair);
    	setResult(RESULT_OK);
    	finish();
    }

	private class Timer extends CountDownTimer {

		public Timer() {
			super(1000 * 60, 600);
		}

		@Override
		public void onFinish() {
			roundEndAlarm.start();
			isRoundFinished = true;
		}

		@Override
		public void onTick(long millisUntilFinished) {
			progress.incrementProgressBy(1);
		}
    }
}
