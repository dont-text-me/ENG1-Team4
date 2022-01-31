package com.isometric.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

public class EnemyShip{

    private Texture shipImage;
    public Vector2 position;
    public Vector2 tilePosition;
    public Vector2 futurePosition;
    private float time;
    private double timeLimit;
    private String direction;
    private final String[] directionsList;
    private int numOfMoves;


    public EnemyShip(IsometricRenderer renderer, PlayerShip playerShip){
        directionsList = new String[]{"EnemyShip/ship_dark_NW.png", "EnemyShip/ship_dark_SE.png", "EnemyShip/ship_dark_NE.png", "EnemyShip/ship_dark_SW.png"};
        direction = directionsList[new Random().nextInt(4)];
        shipImage = new Texture(Gdx.files.internal(direction));
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
        timeLimit = new Random().nextInt(10) + 3;
        timeLimit /= 10;
        System.out.println(timeLimit);
        numOfMoves = new Random().nextInt(20) + 1;
    }
    public void render(SpriteBatch batch) {
        batch.draw(shipImage, position.x, position.y);
    }

    public void update(IsometricRenderer renderer, PlayerShip playerShip, Array<EnemyShip> enemyShips) {
        time += Gdx.graphics.getDeltaTime();
        if (position != futurePosition){
            position.lerp(futurePosition, 0.1f);
            if (time > timeLimit){
                move(renderer, playerShip, calcNearest(tilePosition, enemyShips));
            }
        }
    }

    private static EnemyShip calcNearest(Vector2 playerPos, Array<EnemyShip> enemyShips){
        EnemyShip closest =  enemyShips.get(0);

        for (int i = 1; i < enemyShips.size; i++){
            EnemyShip current = enemyShips.get(i);
            if (playerPos.dst(current.tilePosition) < playerPos.dst(closest.tilePosition)){
                closest = current;
            }
        }
        return closest;
    }

    public void move(IsometricRenderer renderer, PlayerShip playerShip, EnemyShip nearestEnemyShip){
        if (numOfMoves != 0){
            if(direction == "EnemyShip/ship_dark_NW.png") {
                if  (tilePosition.x + 2 == playerShip.tilePosition.x && tilePosition.y == playerShip.tilePosition.y ||
                        tilePosition.x + 2 == playerShip.tilePosition.x && tilePosition.y - 1 == playerShip.tilePosition.y ||
                        tilePosition.x + 2 == playerShip.tilePosition.x && tilePosition.y + 1 == playerShip.tilePosition.y){}
                else if (tilePosition.x != 61) {
                    String row = renderer.map[(int) tilePosition.x + 1];
                    if (Character.getNumericValue(row.charAt((int) tilePosition.y)) != 9) {
                        direction = directionsList[new Random().nextInt(4)];
                    } else {
                        shipImage = new Texture(Gdx.files.internal("EnemyShip/ship_dark_NW.png"));
                        tilePosition.x += 1;
                        numOfMoves -= 1;
                    }
                }else {
                    direction = "EnemyShip/ship_dark_SE.png";
                }

            }else if(direction == "EnemyShip/ship_dark_SE.png"){
                if (tilePosition.x - 2 == playerShip.tilePosition.x && tilePosition.y == playerShip.tilePosition.y ||
                        tilePosition.x - 2 == playerShip.tilePosition.x && tilePosition.y - 1 == playerShip.tilePosition.y ||
                        tilePosition.x - 2 == playerShip.tilePosition.x && tilePosition.y + 1 == playerShip.tilePosition.y){}
                else if (tilePosition.x != 1){
                    String row = renderer.map[(int) tilePosition.x - 1];
                    if (Character.getNumericValue(row.charAt((int) tilePosition.y)) != 9){
                        direction = directionsList[new Random().nextInt(4)];
                    }else{
                        shipImage = new Texture(Gdx.files.internal("EnemyShip/ship_dark_SE.png"));
                        tilePosition.x -= 1;
                        numOfMoves -= 1;
                    }
                }else{
                    direction = "EnemyShip/ship_dark_NW.png";
                }
            }else if (direction == "EnemyShip/ship_dark_NE.png"){
                if (tilePosition.x == playerShip.tilePosition.x && tilePosition.y + 2 == playerShip.tilePosition.y ||
                        tilePosition.x - 1  == playerShip.tilePosition.x && tilePosition.y + 2 == playerShip.tilePosition.y ||
                        tilePosition.x + 1 == playerShip.tilePosition.x && tilePosition.y + 2 == playerShip.tilePosition.y){}
                else if (tilePosition.y != 62){
                    String row = renderer.map[(int) tilePosition.x];
                    if (Character.getNumericValue(row.charAt((int) tilePosition.y + 1)) != 9) {
                        direction = directionsList[new Random().nextInt(4)];
                    }else{
                        shipImage = new Texture(Gdx.files.internal("EnemyShip/ship_dark_NE.png"));
                        tilePosition.y += 1;
                        numOfMoves -= 1;
                    }
                }else{
                    direction = "EnemyShip/ship_dark_SW.png";
                }
            }else if (direction == "EnemyShip/ship_dark_SW.png"){
                if (tilePosition.x == playerShip.tilePosition.x && tilePosition.y - 2 == playerShip.tilePosition.y ||
                        tilePosition.x - 1  == playerShip.tilePosition.x && tilePosition.y - 2 == playerShip.tilePosition.y ||
                        tilePosition.x + 1 == playerShip.tilePosition.x && tilePosition.y - 2 == playerShip.tilePosition.y){}
                else if (tilePosition.y != 1){
                    String row = renderer.map[(int) tilePosition.x];
                    if (Character.getNumericValue(row.charAt((int) tilePosition.y - 1)) != 9){
                        direction = directionsList[new Random().nextInt(4)];
                    }else{
                        shipImage = new Texture(Gdx.files.internal("EnemyShip/ship_dark_SW.png"));
                        tilePosition.y -= 1;
                        numOfMoves -= 1;
                    }
                }else{
                    direction = "EnemyShip/ship_dark_NE.png";
                }
            }
        }else{
            timeLimit = new Random().nextInt(10) + 3;
            timeLimit /= 10;
            numOfMoves = new Random().nextInt(20) + 1;
            direction = directionsList[new Random().nextInt(4)];
        }
        futurePosition.x = (tilePosition.y - tilePosition.x) * 32;
        futurePosition.y = (tilePosition.y + tilePosition.x) * 16;
        time = 0;
    }
}
