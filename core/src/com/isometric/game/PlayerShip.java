package com.isometric.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import javax.swing.plaf.IconUIResource;
import java.util.Random;

public class PlayerShip{

    private Texture shipImage;
    public Vector2 position;
    public Vector2 tilePosition;
    public Vector2 futurePosition;
    private float _currentTime;
    private float alpha;



    public PlayerShip(IsometricRenderer renderer){
        shipImage = new Texture(Gdx.files.internal("ship/ship_light_NW.png"));
        tilePosition = new Vector2();
        //Randomly setting ships' tile position
        while (tilePosition.x == 0 && tilePosition.y == 0 ){
            int x = new Random().nextInt(61) + 1;
            String row = renderer.map[x];
            int y = new Random().nextInt(61) + 1;
            if (Character.getNumericValue(row.charAt(y)) == 9){
                tilePosition.x = x;
                tilePosition.y = y;
            }
        }
        position = new Vector2((tilePosition.y - tilePosition.x) * 32, (tilePosition.y + tilePosition.x) * 16);
        futurePosition = new Vector2(position.x, position.y);
    }

//    private float calculateAlpha() {
//        _currentTime += Gdx.graphics.getDeltaTime();
//        return 0.8f / _currentTime;
//    }



    public void render(SpriteBatch batch) {
        batch.draw(shipImage, position.x, position.y);
    }


    public void update(IsometricRenderer renderer){
//        System.out.println(position.toString() + futurePosition.toString());
        if (position != futurePosition) {
//            alpha = calculateAlpha();
//            if (alpha != 1) {
                position.lerp(futurePosition, 0.1f);
            }
            else {
//                System.out.println("YAY");
            }
//        }

        move(renderer);
    }
    //All the movement logic with the use of possibleMove method
    private void move(IsometricRenderer renderer){
        if (Gdx.input.isKeyJustPressed(Input.Keys.W)){
            if (tilePosition.x == 62){}
            else if (possibleMove(renderer, "W")){
                shipImage = new Texture(Gdx.files.internal("ship/ship_light_NW.png"));
                tilePosition.x += 1;
                futurePosition.x = (tilePosition.y - tilePosition.x) * 32;
                futurePosition.y = (tilePosition.y + tilePosition.x) * 16;
            }
        }

        else if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {

            if (tilePosition.x == 1){}
            else if(possibleMove(renderer, "S")){
                shipImage = new Texture(Gdx.files.internal("ship/ship_light_SE.png"));
                tilePosition.x -= 1;
                futurePosition.x = (tilePosition.y - tilePosition.x) * 32;
                futurePosition.y = (tilePosition.y + tilePosition.x) * 16;
            }
        }

        else if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            if (tilePosition.y == 62){}
            else if (possibleMove(renderer, "D")) {
                shipImage = new Texture(Gdx.files.internal("ship/ship_light_NE.png"));
                tilePosition.y += 1;
                futurePosition.x = (tilePosition.y - tilePosition.x) * 32;
                futurePosition.y = (tilePosition.y + tilePosition.x) * 16;
            }
        }

        else if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
            if (tilePosition.y == 1){}
            else if (possibleMove(renderer, "A")) {
                shipImage = new Texture(Gdx.files.internal("ship/ship_light_SW.png"));
                tilePosition.y -= 1;
                futurePosition.x = (tilePosition.y - tilePosition.x) * 32;
                futurePosition.y = (tilePosition.y + tilePosition.x) * 16;
            }
        }
    }
    //This method checks if the next tile is water for a given move
    public boolean possibleMove(IsometricRenderer renderer, String input) {
        if (input == "W") {
            int rowIndex = (int) tilePosition.x + 1;
            int columnIndex = (int) tilePosition.y;
            String row = renderer.map[rowIndex];
            if (Character.getNumericValue(row.charAt(columnIndex)) != 9) {
                return false;
            }else{
                return true;
            }
        }
        else if (input == "S"){
            int rowIndex = (int) tilePosition.x - 1;
            int columnIndex = (int) tilePosition.y;
            String row = renderer.map[rowIndex];
            if (Character.getNumericValue(row.charAt(columnIndex)) != 9) {
                return false;
            }else{
                return true;
            }
        }

        else if(input == "D"){
            int rowIndex = (int) tilePosition.x;
            int columnIndex = (int) tilePosition.y + 1;
            String row = renderer.map[rowIndex];
            if (Character.getNumericValue(row.charAt(columnIndex)) != 9) {
                return false;
            }else{
                return true;
            }
        }

        else if(input == "A"){
            int rowIndex = (int) tilePosition.x;
            int columnIndex = (int) tilePosition.y - 1;
            String row = renderer.map[rowIndex];
            if (Character.getNumericValue(row.charAt(columnIndex)) != 9) {
                return false;
            }else{
                return true;
            }
        }
        return false;
    }
}
