package com.gmail.at.zhuikov.aleksandr.alias.domain;


public interface PlayerPairDao {

	void save(PlayerPair playerPair);

	PlayerPair load(long id);

	void delete(long id);

	void deleteAllFromGame(long gameId);
}
