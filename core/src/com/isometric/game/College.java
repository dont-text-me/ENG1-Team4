package com.isometric.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;


public class College {
    public static final int small_tile_width = 64;
    public static final int small_tile_height = 64;
    private final Texture sprite;
    private final Vector2 position;
    private final Vector2 tilePosition;
    private boolean isDefeated = false;
    private int health = 100;
    private int reloadTime; // prevents the college from continuously firing at the player when they are in range
    private boolean firing; // true when the college will shoot in a particular frame
    public College (int x, int y) {
        tilePosition = new Vector2(x, y);
        // converting tile position to screen position:
        float pos_x;
        float pos_y;
        pos_x = (x - y) * (small_tile_width / 2f);
        pos_y = ((x + y) * (small_tile_height / 4f));
        position = new Vector2(pos_x, pos_y);
        //----------------------------------------------
        sprite = new Texture(Gdx.files.internal("college_building/tower_NE.png"));
        reloadTime = 60; // will fire every 30 frames, change as needed
        firing = false;
    }


    /**
     * Renders the college and its health bar. If college is defeated, renders the college and a white flag.
     * */
    public void render(SpriteBatch batch, PlayerShip p){
        if (health == 100){
            batch.draw(
                    new Texture(Gdx.files.internal("healthbar_components/green.png")),
                    position.x - (small_tile_width / 4f),
                    position.y + (small_tile_height) + 5,
                    100,
                    3.0f
            );
        }
        else if ((health < 100) && (health > 0)) {
            batch.draw(
                    new Texture(Gdx.files.internal("healthbar_components/green.png")),
                    position.x - (small_tile_width / 4f),
                    position.y + (small_tile_height) + 5,
                    health,
                    3.0f
            ); // drawing out the green part of the health bar
            batch.draw(
                    new Texture(Gdx.files.internal("healthbar_components/red.png")),
                    position.x - (small_tile_width / 4f) + health, // red part is rendered immediately to the right of the green part
                    position.y + (small_tile_height) + 5,
                    100 - health,
                    3.0f
            ); // drawing out the red part of the health bar
        }
        else{
            batch.draw(
                    new Texture (Gdx.files.internal("white_flag/white_flag.png")),
                    position.x + (small_tile_width / 2f),
                    position.y + small_tile_height); // drawing out the white flag for defeated colleges
        }
        if ((p.tilePosition.dst2(tilePosition.y, tilePosition.x) <= 100) && (health > 0)) {
            batch.draw(new Texture(Gdx.files.internal("college_building/firing_marker.png")), position.x + 16, position.y + 70); // when this is rendered, college is about to shoot the player
        }
        batch.draw(sprite, position.x, position.y);
    }

    public int update(PlayerShip p){
        if (health <= 0){
            if (!isDefeated) {
                isDefeated = true;
                firing = false;
                return 250;
            }
        }
        else{
            if (p.tilePosition.dst2(tilePosition.y, tilePosition.x) <= 100){
                if (reloadTime == 0){
                    reloadTime = 30;
                    firing = true;
                }
                else{
                    reloadTime -= 1;
                    firing = false;
                }
            }
            else{
                reloadTime = 60; // if player approaches college, will fire after 1 frame
            }
            return 0;
        }
        return 0;
    }

    public boolean isFiring(){
        return firing;
    }

    public void takeDamage(int dmg){
        if (health > 0) {
            health = health - dmg;
        }
    }

    public Vector2 getTilePosition(){
        return tilePosition;
    }
    //for testing, TODO remove this
    public void setHealth(int num){
        health = num;
    }

    public boolean isDefeated(){
        return isDefeated;
    }

    /**
     * Returns a projectile aimed in the direction of the input vector.
     * @param pos Specifies the target of the projectile
     * @return The newly created projectile
     * */
    public Projectile shoot (Vector2 pos){
        return new Projectile (
                tilePosition.x + 2, // to make the projectile start off from the top of the tower visually
                tilePosition.y + 2, // see above
                pos.y - tilePosition.x - 0.5f, // to make the projectile aim at the center of the ship
                pos.x - tilePosition.y - 0.5f, // see above
                false // needed so that buildings do not hurt each other
        );
    }
}
