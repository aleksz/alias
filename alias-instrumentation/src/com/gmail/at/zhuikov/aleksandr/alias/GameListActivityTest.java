package com.gmail.at.zhuikov.aleksandr.alias;

import java.util.Date;

import android.content.ContentValues;
import android.test.ActivityInstrumentationTestCase2;

import com.gmail.at.zhuikov.aleksandr.alias.domain.GameProvider;
import com.jayway.android.robotium.solo.Solo;

public class GameListActivityTest extends ActivityInstrumentationTestCase2<GameListActivity> {

	private Solo solo;

	public GameListActivityTest() {
		super(GameListActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		solo = new Solo(getInstrumentation(), getActivity());
		super.setUp();
	}

	@Override
	public void tearDown() throws Exception {
		try {
			solo.finalize();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		getActivity().finish();
		super.tearDown();
	}

	public void testCreateNewGame() {
		int numberOfGamesBefore = solo.getCurrentListViews().get(0).getCount();
		solo.clickOnMenuItem("New game");
		solo.assertCurrentActivity("Expected GameActivity", GameActivity.class);
		solo.goBack();
		assertEquals(numberOfGamesBefore + 1, solo.getCurrentListViews().get(0).getCount());
	}

	public void testDeleteAll() {
		addGame();
		addGame();
		solo.clickOnMenuItem("Delete all");
		assertEquals(0, solo.getCurrentListViews().get(0).getCount());
	}

	public void testDeleteGame() {
		addGame();
		int numberOfGamesBefore = solo.getCurrentListViews().get(0).getCount();
		solo.clickLongInList(1);
		solo.clickOnText("Delete");
		solo.waitForDialogToClose(1000);
		assertEquals(numberOfGamesBefore - 1, solo.getCurrentListViews().get(0).getCount());
		assertEquals(numberOfGamesBefore - 1, solo.getCurrentListViews().get(0).getCount());
	}

	private void addGame() {
		ContentValues values = new ContentValues();
		values.put(GameProvider.Columns.MODIFIED, new Date().getTime());
		getActivity().getContentResolver().insert(GameProvider.CONTENT_URI, values);
	}
}
