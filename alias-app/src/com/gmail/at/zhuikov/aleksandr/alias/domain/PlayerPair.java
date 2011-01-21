package com.gmail.at.zhuikov.aleksandr.alias.domain;

import java.io.Serializable;


public class PlayerPair implements Serializable {

	private static final long serialVersionUID = 1L;

	private final String color;
	private int score;

	public PlayerPair(String color) {
		this.color = color;
	}

	public String getColor() {
		return color;
	}

	public int getScore() {
		return score;
	}

	@Override
	public String toString() {
		return color + "[" + score + "]";
	}
}
