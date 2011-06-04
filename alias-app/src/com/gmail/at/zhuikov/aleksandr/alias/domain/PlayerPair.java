package com.gmail.at.zhuikov.aleksandr.alias.domain;

import static android.content.ContentUris.withAppendedId;
import static com.gmail.at.zhuikov.aleksandr.alias.domain.PlayerPairProvider.CONTENT_URI;

import java.io.Serializable;

import android.net.Uri;


public class PlayerPair implements Serializable {

	private static final long serialVersionUID = 1L;

	private long id;
	private final int color;
	private int score;
	private final long gameId;

	public PlayerPair(int color, long gameId) {
		this.color = color;
		this.gameId = gameId;
	}

	public long getId() {
		return id;
	}

	protected PlayerPair(long id, int color, int score, long gameId) {
		this.id = id;
		this.color = color;
		this.score = score;
		this.gameId = gameId;
	}

	public long getGameId() {
		return gameId;
	}

	public boolean isNew() {
		return getId() == 0;
	}

	protected void setId(long id) {
		this.id = id;
	}

	public int getColor() {
		return color;
	}

	public int getScore() {
		return score;
	}

	public void increaseScore() {
		score++;
	}

	public void decreaseScore() {
		score--;
	}

	public Uri getUri() {

		if (isNew()) {
			return CONTENT_URI;
		}

		return withAppendedId(CONTENT_URI, id);
	}

	@Override
	public String toString() {
		return color + "[" + score + "]";
	}
}
