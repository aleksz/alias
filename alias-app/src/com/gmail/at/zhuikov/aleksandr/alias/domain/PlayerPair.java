package com.gmail.at.zhuikov.aleksandr.alias.domain;

import java.io.Serializable;


public class PlayerPair implements Serializable {

	private static final long serialVersionUID = 1L;

	private long id;
	private Game game;
	private final String color;//replace it with int?
	private int score;

	public PlayerPair(long id, Game game, String color, int score) {
		this.id = id;
		this.game = game;
		this.color = color;
		this.score = score;
	}

	public long getId() {
		return id;
	}

	public String getColor() {
		return color;
	}

	public int getScore() {
		return score;
	}

	public Game getGame() {
		return game;
	}

	@Override
	public String toString() {
		return color + "[" + score + "]";
	}
}
