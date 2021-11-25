package com.isometric.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.LinkedList;
import java.util.Random;
import java.awt.Point;

public class IsometricRenderer {

    public static final int small_tile_width = 64;
    public static final int small_tile_height = 64;

    Texture water_shallow;
    Texture pinpoint;
    Texture island_grass;

    int board_size = 64;
    int islands_to_spawn = 40;
    @SuppressWarnings("FieldMayBeFinal")
    private LinkedList<Point> grass_locations = new LinkedList<Point>();

    public IsometricRenderer() {
        water_shallow = new Texture(Gdx.files.internal("ts_shallow0/straight/45/0.png"));
        pinpoint = new Texture(Gdx.files.internal("pinpoint.png"));
        island_grass = new Texture(Gdx.files.internal("ts_grass0/straight/45/0.png"));

        Random rand = new Random();
        for (int i=islands_to_spawn; i>=1; i--) {
            Point point = new Point(rand.nextInt(board_size-10)+5, rand.nextInt(board_size-10)+5);
            grass_locations.add(point);
        }
        System.out.println(grass_locations);
    }

    public void drawCoordinates(SpriteBatch batch) {
        BitmapFont font = new BitmapFont();

        //noinspection DuplicatedCode
        for (int row = board_size; row >= 0; row--) {
            for (int col = board_size; col >= 0; col--) {
                float x = (col - row) * (small_tile_width /2f);
                float y = (col + row) * (small_tile_height /4f);
                batch.draw(pinpoint, x, y);
            }
        }

        for (int row = board_size; row >= 0; row--) {
            for (int col = board_size; col >= 0; col--) {
                float x = (col - row) * (small_tile_width/2f);
                float y = (col + row) * (small_tile_height/4f);
                font.draw(batch, ( "(" + row + ", " + col + ")"), x+(small_tile_width /2f), y+(small_tile_height /2f));
            }
        }
    }


    public void drawOcean(SpriteBatch batch) {
        for (int row = board_size; row >= 0; row--) {
            for (int col = board_size; col >= 0; col--) {
                Point point = new Point(row, col);
                if (!grass_locations.contains(point)) {
                    float x = (col - row) * (small_tile_width / 2f);
                    float y = (col + row) * (small_tile_height / 4f);
                    batch.draw(water_shallow, x, y);
                }
            }
        }
    }

    public void drawGrass(SpriteBatch batch) {
        for (int row = board_size; row >= 0; row--) {
            for (int col = board_size; col >= 0; col--) {
                Point point = new Point(row, col);
                if (grass_locations.contains(point)) {
                    float x = (col - row) * (small_tile_width / 2f);
                    float y = (col + row) * (small_tile_height / 4f);
                    batch.draw(island_grass, x, y);
                }
            }
        }
    }

    public void drawBeach(SpriteBatch batch) {
    }
}
