package com.isometric.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class IsometricRenderer {

    public static final int small_tile_width = 64;
    public static final int small_tile_height = 64;
    public int board_size;

//    These are globals
//    For drawCoords, placed here for efficiency.
    public TextureHandler tiles = new TextureHandler();
    private final BitmapFont coordsFont = new BitmapFont();
    private final Texture pinpoint = new Texture(Gdx.files.internal("pinpoint.png"));
    public String[] map;
    public String[][] board2d;

    public IsometricRenderer(boolean randomMap) {
//      This runs once; the constructor.
        if (randomMap) {
            MapGenerator PirateGame = new MapGenerator();
            board_size = (PirateGame.size - 1);
            map = PirateGame.generateMap();
            board2d = PirateGame.loadBoard();
        }
        else {
            MapGenerator PirateGame = new MapGenerator();
            board_size = (PirateGame.size - 1);
            map = PirateGame.loadMap();
            board2d = PirateGame.loadBoard();
        }
    }

    /** This is used for debug and should not be deleted.
     * Coordinates are drawn down and to the right of the pinpoint they are linked to.
     * @param batch Current sprite batch
     * @param drawTextCoords true if draw text coordinates.
     */
    @SuppressWarnings("unused")
    public void drawCoordinates(SpriteBatch batch, boolean drawTextCoords) {
        if (drawTextCoords) {
            float x;
            float y;
            for (int row = board_size; row >= 0; row--) {
                for (int col = board_size; col >= 0; col--) {
                    x = (col - row) * (small_tile_width / 2f);
                    y = ((col + row) * (small_tile_height / 4f)) - 17;
                    coordsFont.draw(batch, ("(" + row + ", " + col + ")"), x + (small_tile_width / 2f), y + (small_tile_height / 2f));

                    batch.draw(pinpoint, x, y);
                }
            }
        }
    }

    public void drawBoard(SpriteBatch batch) {
        String axis;
        char ch;
        float x;
        float y;
        int t;

        for (int row = board_size; row >= 0; row--) {
             axis = map[row];

            for (int col = board_size; col >= 0; col--) {

                x = (col - row) * (small_tile_width / 2f);
                y = (col + row) * (small_tile_height / 4f);
                ch = axis.charAt(col);

                if (Character.isDigit(ch)) {
                    t = Character.getNumericValue(ch);
                }
                else {
                    t = (int)ch - (int)'a' + 10;
                }

                batch.draw(tiles.tilemap[t], x, y);
            }
        }
    }
}


