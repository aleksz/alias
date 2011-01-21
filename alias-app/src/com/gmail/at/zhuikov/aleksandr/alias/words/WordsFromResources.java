package com.gmail.at.zhuikov.aleksandr.alias.words;

import android.content.res.Resources;

import com.gmail.at.zhuikov.aleksandr.alias.R;


public class WordsFromResources implements Words {

	private Resources resources;

	public WordsFromResources(Resources resources) {
		this.resources = resources;
	}

	public String next() {
		String[] words = resources.getStringArray(R.array.eng);
		return words[(int) (Math.random() * words.length)];
	}
}
