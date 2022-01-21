package com.isometric.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class PlayerShip implements Ship{

    private Texture shipImage;
    public Vector2 position;
    public Vector2 tilePosition;


    public PlayerShip(){
        shipImage = new Texture(Gdx.files.internal("ship/ship_light_NW.png"));
        position = new Vector2(16,32);
        tilePosition = new Vector2(1,1);
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(shipImage, position.x, position.y);
    }

    @Override
    public void update(){
        move();
    }

    private void move(){
        if (Gdx.input.isKeyJustPressed(Input.Keys.W)){
            shipImage = new Texture(Gdx.files.internal("ship/ship_light_Nw.png"));
            if (tilePosition.x == 62){}
            else {
                position.x -= 32;
                position.y += 16;
                tilePosition.x += 1;
                }
        }
        else if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            shipImage = new Texture(Gdx.files.internal("ship/ship_light_SE.png"));
            if (tilePosition.x == 1){}
            else {
                position.x += 32;
                position.y -= 16;
                tilePosition.x -= 1;
            }
        }
        else if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            shipImage = new Texture(Gdx.files.internal("ship/ship_light_NE.png"));
            if (tilePosition.y == 61){}
            else {
                position.x += 32;
                position.y += 16;
                tilePosition.y += 1;
            }
        }
        else if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
            shipImage = new Texture(Gdx.files.internal("ship/ship_light_SW.png"));
            if (tilePosition.y == 1){}
            else {
                position.x -= 32;
                position.y -= 16;
                tilePosition.y -= 1;
            }
        }
    }
}
