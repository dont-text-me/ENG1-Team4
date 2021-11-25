package com.isometric.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.Game;

public class Isometric extends Game {
	private SpriteBatch batch;
	@SuppressWarnings("FieldCanBeLocal")
	private GameScreen gScreen;

	@Override
	public void create () {
		batch = new SpriteBatch();
		gScreen = new GameScreen(batch);
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
