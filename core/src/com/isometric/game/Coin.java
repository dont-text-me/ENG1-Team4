package com.isometric.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Coin {
    private final Texture sprite;
    private final Vector2 position;
    private final Vector2 tilePosition;

    public Coin (int x, int y) {
        tilePosition = new Vector2(x, y);
        // converting tile position to screen position:
        float pos_x;
        float pos_y;
        pos_x = (x - y) * (64 / 2f);
        pos_y = ((x + y) * (64 / 4f));
        position = new Vector2(pos_x, pos_y);
        //----------------------------------------------
        sprite = new Texture(Gdx.files.internal("coin.png"));
    }

    public void render(SpriteBatch batch) {
        batch.draw(sprite, position.x-16, position.y+16);
    }

    public boolean update(PlayerShip p) {
        return (p.tilePosition.x == nearestTile().y + 1) && (p.tilePosition.y == nearestTile().x);
    }

    public Vector2 nearestTile(){
        return new Vector2 ((float)Math.floor(tilePosition.x), (float)Math.floor(tilePosition.y));
    }
}
