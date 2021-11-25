package com.isometric.game;

import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.Game;
//import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
//import sun.security.provider.ConfigFile;

public class IsometricRenderer {

    public static final int TILE_WIDTH = 640;
    public static final int TILE_HEIGHT = 320;

    Texture water_deep;
    Texture water_shallow;
    Texture pinpoint;

    public IsometricRenderer() {
        water_deep = new Texture(Gdx.files.internal("ts_deep0/straight/45/0.png"));
        water_shallow = new Texture(Gdx.files.internal("ts_shallow0/straight/45/0.png"));
        pinpoint = new Texture(Gdx.files.internal("pinpoint.png"));
    }

    private void gridDisplay(SpriteBatch batch, Texture texture) {
        for (int row = 15; row >= 0; row--) {
            for (int col = 15; col >= 0; col--) {
                float x = (col - row) * ((TILE_WIDTH) / 4f);
                float y = (col + row) * ((TILE_HEIGHT) / 4f);
                batch.draw(texture, x, y, TILE_WIDTH / 2f, TILE_HEIGHT / 2f);
            }
        }
    }

    public void drawCoordinates(SpriteBatch batch) {
        BitmapFont font = new BitmapFont();

        gridDisplay(batch, pinpoint);
        for (int row = 15; row >= 0; row--) {
            for (int col = 15; col >= 0; col--) {
                float x = (col - row) * ((TILE_WIDTH) / 4f);
                float y = (col + row) * ((TILE_HEIGHT) / 4f);
                font.draw(batch, ( "(" + row + ", " + col + ")"), x+(TILE_WIDTH/4f), y+(TILE_HEIGHT/4f));
            }
        }
    }


    public void drawGround(SpriteBatch batch) {
        gridDisplay(batch, water_deep);
    }
}
