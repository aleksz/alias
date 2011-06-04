package com.gmail.at.zhuikov.aleksandr.alias;

import static android.content.ContentUris.parseId;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.test.ActivityInstrumentationTestCase2;

import com.gmail.at.zhuikov.aleksandr.alias.domain.GameProvider;
import com.gmail.at.zhuikov.aleksandr.alias.domain.PlayerPairProvider;
import com.jayway.android.robotium.solo.Solo;

public class GameActivityTest extends ActivityInstrumentationTestCase2<GameActivity> {

	private Uri gameUri;
	private Uri playerPair;
	private Solo solo;

	public GameActivityTest() {
		super(GameActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		gameUri = getInstrumentation().getContext().getContentResolver()
				.insert(GameProvider.CONTENT_URI, new ContentValues());
		ContentValues contentValues = new ContentValues();
		contentValues.put(PlayerPairProvider.Columns.GAME, parseId(gameUri));
		playerPair = getInstrumentation().getContext().getContentResolver()
				.insert(PlayerPairProvider.CONTENT_URI, contentValues);
		setActivityIntent(new Intent(null, gameUri));
		solo = new Solo(getInstrumentation(), getActivity());
		super.setUp();
	}

	@Override
	public void tearDown() throws Exception {
//		getInstrumentation().getContext().getContentResolver().delete(gameUri, null, null);
//		getInstrumentation().getContext().getContentResolver().delete(playerPair, null, null);
		try {
			solo.finalize();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		getActivity().finish();
		super.tearDown();
	}

	public void testAddPlayerPair() {
		solo.clickOnMenuItem("New player pair");
		solo.clickOnText("Blue");
		assertEquals(2, solo.getCurrentListViews().get(0).getCount());
	}

	public void testDeletePlayerPair() {
		solo.clickLongInList(0);
		solo.clickOnText("Delete");
		solo.waitForDialogToClose(1000);
		assertEquals(0, solo.getCurrentListViews().get(0).getCount());
	}

	public void testDeleteAllPlayerPairs() {
		solo.clickOnMenuItem("Delete all player pairs");
		assertEquals(0, solo.getCurrentListViews().get(0).getCount());
	}
}
