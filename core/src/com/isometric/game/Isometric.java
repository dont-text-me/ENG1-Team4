package com.isometric.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Isometric extends Game {
	private SpriteBatch batch;

	@Override
	public void create () {
		batch = new SpriteBatch();
		GameScreen gScreen = new GameScreen();
		setScreen(gScreen);
	}

	@Override
	public void render () {
		super.render();
	}

	@Override
	public void dispose () {
		batch.dispose();
		super.dispose();
	}

}
