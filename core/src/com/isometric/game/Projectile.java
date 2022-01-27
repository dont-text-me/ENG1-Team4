package com.isometric.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Projectile {
    private final Texture sprite;
    private final Vector2 position;
    private Vector2 tilePosition;
    public static final int small_tile_width = 64;
    public static final int small_tile_height = 64;
    private int lifetime; // determines how many frames the projectile will be on screen for
    private boolean active = true; // if active, the projectile hasn't collided with anything or exceeded its lifetime yet
    private final boolean byPlayer; // determines whether the projectile has been shot by the player
    private Vector2 delta; // specifies the movement in *Tiles* of the projectile
    public Projectile(float x, float y, float delta_x, float delta_y, boolean player){
        lifetime = 30;
        tilePosition = new Vector2(x, y);
        // converting tile position to screen position:
        position = new Vector2(
                (tilePosition.x - tilePosition.y) * (small_tile_width / 2f),
                ((tilePosition.x + tilePosition.y) * (small_tile_height / 4f)) - 17
        );
        delta = new Vector2(delta_x, delta_y);
        delta = delta.nor(); // normalising the vector so that all projectiles travel at the same speed
        sprite = new Texture(Gdx.files.internal("cannon_ball/cannonBall_NE.png"));
        byPlayer = player;
    }

    public void render(SpriteBatch batch){
        batch.draw(sprite, position.x, position.y);
    }

    public void update(){
        if (
                (tilePosition.x >= 64) ||
                (tilePosition.x < 0) ||
                (tilePosition.y >= 64) ||
                (tilePosition.y < 0) ||
                (lifetime <= 0)
        ){
            active = false;
        }
        else{
            tilePosition = tilePosition.add(delta);
            // converting tile position to screen position:
            position.x = (tilePosition.x - tilePosition.y) * (small_tile_width / 2f);
            position.y = (tilePosition.x + tilePosition.y) * (small_tile_height / 4f) - 17;
            lifetime -= 1;
        }
    }

    public boolean isActive(){
        return active;
    }

    public void deactivate(){
        active = false;
    }

    public boolean isByPlayer(){
        return byPlayer;
    }

    /**
     * Returns the coordinates of the tile the projectile is currently in.
     * Rounds down the projectile's tile position value.
     * @return a Vector2 object containing the x and y position of the tile the projectile is currently over.
     * */
    public Vector2 nearestTile(){
        return new Vector2 ((float)Math.floor(tilePosition.x), (float)Math.floor(tilePosition.y));
    }

}
