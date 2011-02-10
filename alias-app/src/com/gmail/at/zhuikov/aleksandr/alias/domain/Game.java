package com.gmail.at.zhuikov.aleksandr.alias.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Game implements Serializable {

	private static final long serialVersionUID = 1L;

	private long id;
	private final Set<PlayerPair> playerPairs = new HashSet<PlayerPair>();

	public Game(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public Set<PlayerPair> getPlayerPairs() {
		return playerPairs;
	}

	@Override
	public String toString() {
		return "Game [playerPairs=" + playerPairs + "]";
	}
}
