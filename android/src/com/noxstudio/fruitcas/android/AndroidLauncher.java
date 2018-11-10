package com.noxstudio.fruitcas.android;

import android.os.Bundle;
import android.util.Log;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.noxstudio.fruitcas.FruitSlotsGame;

public class AndroidLauncher extends AndroidApplication {

	private Tracker mTracker;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Fruitcas application = (Fruitcas) getApplication();
		mTracker = application.getDefaultTracker();

		Log.i(AndroidLauncher.class.getName(), "Setting screen name: " + AndroidLauncher.class.getName());
		mTracker.setScreenName( AndroidLauncher.class.getName());
		mTracker.send(new HitBuilders.ScreenViewBuilder().build());

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new FruitSlotsGame(), config);
	}
}
