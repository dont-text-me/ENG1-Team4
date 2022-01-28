package com.isometric.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Coins {
    public static final int small_tile_width = 64;
    public static final int small_tile_height = 64;
    private final Texture sprite;
    private final Vector2 position;
    private final Vector2 tilePosition;
    public Coins (int x, int y) {
        tilePosition = new Vector2(x, y);
        // converting tile position to screen position:
        float pos_x;
        float pos_y;
        pos_x = (x - y) * (small_tile_width / 2f);
        pos_y = ((x + y) * (small_tile_height / 4f)) - 17;
        position = new Vector2(pos_x, pos_y);
        //----------------------------------------------
        sprite = new Texture(Gdx.files.internal("Gold/Gold_1.png"));
    }

    public void render(SpriteBatch batch){
        batch.draw(sprite, position.x - 30, position.y -30, 30, 30);
    }
}
