package com.isometric.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

public class PlayerShip{

    private final Texture[] shipImage = new Texture[16];
    private int currentDirection = 0;
    public Vector2 position;
    public Vector2 tilePosition;
    public Vector2 futurePosition;
    private float _currentTime;
    @SuppressWarnings("FieldCanBeLocal")
    private float alpha;



    public PlayerShip(IsometricRenderer renderer){
//        shipImage = new Texture(Gdx.files.internal("ship/ship_light_NW.png"));
        shipImage[0] = new Texture(Gdx.files.internal("ship/ship_light_NW.png"));
        shipImage[1] = new Texture(Gdx.files.internal("ship/ship_light_SE.png"));
        shipImage[2] = new Texture(Gdx.files.internal("ship/ship_light_NE.png"));
        shipImage[3] = new Texture(Gdx.files.internal("ship/ship_light_SW.png"));

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

    private float calculateAlpha() {
        _currentTime += Gdx.graphics.getDeltaTime();
        return 0.6f / _currentTime;
    }


    public void render(SpriteBatch batch) {
        batch.draw(new Texture(Gdx.files.internal("ship/player_pointer.png")), position.x + 20, position.y + 64); // to differentiate from non player ships
        batch.draw(shipImage[currentDirection], position.x, position.y);
    }


    public void update(IsometricRenderer renderer){
        if (position != futurePosition) {
//            alpha = calculateAlpha();
//            if (alpha != 1) {
                position.lerp(futurePosition, 0.1f);
            }
        move(renderer);
    }
//    All the movement logic with the use of possibleMove method
    private void move(IsometricRenderer renderer){



        if (Gdx.input.isKeyJustPressed(Input.Keys.W)){
            if (tilePosition.x == 62){
//                do nothing
                assert true;
            }
            else if (possibleMove(renderer, "W")){
                currentDirection = 0;
                tilePosition.x += 1;
                futurePosition.x = (tilePosition.y - tilePosition.x) * 32;
                futurePosition.y = (tilePosition.y + tilePosition.x) * 16;
            }
        }

        else if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {

            if (tilePosition.x == 1){
//                do nothing
                assert true;
            }
            else if(possibleMove(renderer, "S")){
                currentDirection = 1;
                tilePosition.x -= 1;
                futurePosition.x = (tilePosition.y - tilePosition.x) * 32;
                futurePosition.y = (tilePosition.y + tilePosition.x) * 16;
            }
        }

        else if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            if (tilePosition.y == 62){
//                do nothing
                assert true;
            }
            else if (possibleMove(renderer, "D")) {
                currentDirection = 2;
                tilePosition.y += 1;
                futurePosition.x = (tilePosition.y - tilePosition.x) * 32;
                futurePosition.y = (tilePosition.y + tilePosition.x) * 16;
            }
        }

        else if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
            if (tilePosition.y == 1){
//                do nothing
                assert true;
            }
            else if (possibleMove(renderer, "A")) {
                currentDirection = 3;
                tilePosition.y -= 1;
                futurePosition.x = (tilePosition.y - tilePosition.x) * 32;
                futurePosition.y = (tilePosition.y + tilePosition.x) * 16;
            }
        }
    }
//    This method checks if the next tile is water for a given move
    public boolean possibleMove(IsometricRenderer renderer, String input) {
        switch (input) {
            case "W": {
                int rowIndex = (int) tilePosition.x + 1;
                int columnIndex = (int) tilePosition.y;
                String row = renderer.map[rowIndex];
                return Character.getNumericValue(row.charAt(columnIndex)) == 9;
            }
            case "S": {
                int rowIndex = (int) tilePosition.x - 1;
                int columnIndex = (int) tilePosition.y;
                String row = renderer.map[rowIndex];
                return Character.getNumericValue(row.charAt(columnIndex)) == 9;
            }
            case "D": {
                int rowIndex = (int) tilePosition.x;
                int columnIndex = (int) tilePosition.y + 1;
                String row = renderer.map[rowIndex];
                return Character.getNumericValue(row.charAt(columnIndex)) == 9;
            }
            case "A": {
                int rowIndex = (int) tilePosition.x;
                int columnIndex = (int) tilePosition.y - 1;
                String row = renderer.map[rowIndex];
                return Character.getNumericValue(row.charAt(columnIndex)) == 9;
            }
        }
        return false;
    }
    /**
     * Returns the ship's current direction as an int. <br>
     * Options:
     * <ul>
     *     <li>0 - northwest</li>
     *     <li>1 - southeast</li>
     *     <li>2 - northeast</li>
     *     <li>3 - southwest</li>
     * </ul>
     * */
    public int getCurrentDirection(){
        return currentDirection;
    }
}
