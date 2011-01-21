package com.gmail.at.zhuikov.aleksandr.alias.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Game implements Serializable {

	private static final long serialVersionUID = 1L;

	private final Set<PlayerPair> playerPairs = new HashSet<PlayerPair>();

	public Set<PlayerPair> getPlayerPairs() {
		return playerPairs;
	}

	public void addPlayer(String color) {
		playerPairs.add(new PlayerPair(color));
	}

	@Override
	public String toString() {
		return "Game [playerPairs=" + playerPairs + "]";
	}
}
