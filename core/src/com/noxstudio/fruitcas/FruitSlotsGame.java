package com.noxstudio.fruitcas;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class FruitSlotsGame extends Game {

    public Viewport screenPort;

    @Override
    public void create() {

        screenPort = new ScreenViewport();
        setScreen(new FruitSlots(this));

    }

}
