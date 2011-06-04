package com.gmail.at.zhuikov.aleksandr.alias.domain;

import static java.util.Arrays.binarySearch;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

public class Game implements Serializable {

	private static final long serialVersionUID = 1L;

	private long id;
	private Date modified;
	private long lastPlayerId;
	private long[] playerPairIds;

	public Game() {
	}

	protected Game(long id, Date modified, long lastPlayerId, long[] playerPairIds) {
		this.id = id;
		this.modified = modified;
		this.lastPlayerId = lastPlayerId;
		this.playerPairIds = playerPairIds;
	}

	public long getLastPlayerId() {
		return lastPlayerId;
	}

	public void setLastPlayerId(long lastPlayerId) {
		this.lastPlayerId = lastPlayerId;
	}

	public long getNextPlayerId() {
		int lastIndex = binarySearch(playerPairIds, lastPlayerId);
		return playerPairIds[(lastIndex + 1) % playerPairIds.length];
	}

	public long[] getPlayerPairIds() {
		return playerPairIds;
	}

	public long getId() {
		return id;
	}

	protected void setId(long id) {
		this.id = id;
	}

	public boolean isNew() {
		return getId() == 0;
	}

	public Date getModified() {
		return modified;
	}

	@Override
	public String toString() {
		return "Game [id=" + id + ", modified=" + modified
				+ ", lastPlayerId=" + lastPlayerId + ", playerPairIds="
				+ Arrays.toString(playerPairIds) + "]";
	}
}
