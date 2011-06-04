package com.gmail.at.zhuikov.aleksandr.alias.domain;

public interface GameDao {

	void save(Game game);

	Game load(long id);

	void delete(long id);

	void deleteAllGames();

}