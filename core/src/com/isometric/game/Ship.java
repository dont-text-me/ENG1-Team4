package com.isometric.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface Ship {

    public void render(SpriteBatch batch);
    public void update(IsometricRenderer renderer);
}
