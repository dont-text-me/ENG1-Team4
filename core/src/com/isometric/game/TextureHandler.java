package com.isometric.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class TextureHandler {
    public final Texture[] tilemap = new Texture[32];

    public TextureHandler() {

        tilemap[0] = new Texture(Gdx.files.internal("ts_grass0/straight/45/0.png"));

        tilemap[1] = new Texture(Gdx.files.internal("ts_grass-beach0/straight/45/0.png"));
        tilemap[2] = new Texture(Gdx.files.internal("ts_grass-beach0/straight/135/0.png"));
        tilemap[3] = new Texture(Gdx.files.internal("ts_grass-beach0/straight/225/0.png"));
        tilemap[4] = new Texture(Gdx.files.internal("ts_grass-beach0/straight/315/0.png"));

        tilemap[5] = new Texture(Gdx.files.internal("ts_beach-shallow0/straight/45/0.png"));
        tilemap[6] = new Texture(Gdx.files.internal("ts_beach-shallow0/straight/135/0.png"));
        tilemap[7] = new Texture(Gdx.files.internal("ts_beach-shallow0/straight/225/0.png"));
        tilemap[8] = new Texture(Gdx.files.internal("ts_beach-shallow0/straight/315/0.png"));

        tilemap[9] = new Texture(Gdx.files.internal("ts_shallow0/straight/45/0.png"));

        tilemap[10] = new Texture(Gdx.files.internal("ts_grass-beach0/curve_out/45/0.png"));
        tilemap[11] = new Texture(Gdx.files.internal("ts_grass-beach0/curve_out/135/0.png"));
        tilemap[12] = new Texture(Gdx.files.internal("ts_grass-beach0/curve_out/225/0.png"));
        tilemap[13] = new Texture(Gdx.files.internal("ts_grass-beach0/curve_out/315/0.png"));

        tilemap[14] = new Texture(Gdx.files.internal("ts_beach-shallow0/curve_out/45/0.png"));
        tilemap[15] = new Texture(Gdx.files.internal("ts_beach-shallow0/curve_out/135/0.png"));
        tilemap[16] = new Texture(Gdx.files.internal("ts_beach-shallow0/curve_out/225/0.png"));
        tilemap[17] = new Texture(Gdx.files.internal("ts_beach-shallow0/curve_out/315/0.png"));

        tilemap[18] = new Texture(Gdx.files.internal("ts_grass-beach0/curve_in/45/0.png"));
        tilemap[19] = new Texture(Gdx.files.internal("ts_grass-beach0/curve_in/135/0.png"));
        tilemap[20] = new Texture(Gdx.files.internal("ts_grass-beach0/curve_in/225/0.png"));
        tilemap[21] = new Texture(Gdx.files.internal("ts_grass-beach0/curve_in/315/0.png"));

    }
}

