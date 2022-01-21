package com.isometric.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;

public class GameScreen extends ScreenAdapter {

    enum Screen {
        MAIN_MENU, MAIN_GAME, PAUSE_MENU
    }

    Screen currentScreen = Screen.MAIN_MENU;

    @SuppressWarnings("FieldMayBeFinal")
    private SpriteBatch batch;
    private OrthographicCamera camera;
    //  Screen Size
    public static final int HEIGHT = 180 * 5;
    public static final int WIDTH = 320 * 5;
    public boolean MUTED = false;
    private IsometricRenderer renderer;
    //  Main Menu
    Rectangle play_button;
    Rectangle exit_button;
    Rectangle mute_button;
    private BitmapFont font;
    private Circle mouse_pointer;
    private ShapeRenderer shapeRenderer;

    public GameScreen(SpriteBatch batch){
        this.batch = batch;
    }

    //POSITION OF PLAY BUTTON
    float play_button_X = 290f;
    float play_button_Y = 360f;

    //POSITION OF EXIT BUTTON
    float exit_button_X = 290f;
    float exit_button_Y = 260f;

    //Sound
    //Background Music
    Music BackgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("Sound Effects and Music/1700s Sea Shanties 1Hour.mp3"));

    @Override
    public void show() {
        //GAME
        camera = new OrthographicCamera(WIDTH, HEIGHT);
        //noinspection IntegerDivisionInFloatingPointContext
        camera.position.set(0, HEIGHT / 2, 10);
        camera.zoom = 0.625f;
        renderer = new IsometricRenderer();

        //MAIN MENU
        play_button = new Rectangle();
        exit_button = new Rectangle();
        mute_button = new Rectangle();
        font = new BitmapFont();
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        mouse_pointer = new Circle();

        BackgroundMusic.setLooping(true);
        BackgroundMusic.setVolume(0.2f);
        BackgroundMusic.play();
    }

    @Override
    public void render(float delta) {

        //MUTE MUSIC
/*        if(Gdx.input.isKeyPressed(Input.Keys.M)){
            if(MUTED==false){
                BackgroundMusic.setVolume(0f);
                MUTED=true;
            } else if(MUTED){
                BackgroundMusic.setVolume(0.2f);
                MUTED=false;
            }
        }*/

        if(currentScreen == Screen.MAIN_GAME){
//        Delta is the time between frames
            Gdx.gl.glClearColor(44f/255,97f/255,129f/255,1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            batch.setProjectionMatrix(camera.combined);
            camera.update();
            handleInput();
//      All rendering in libgdx is done in the sprite batch
            batch.begin();
            renderer.drawOcean(batch);
            renderer.drawGrass(batch);
            renderer.drawBeach(batch);
//        renderer.drawCoordinates(batch);
            batch.end();
        }
        else if(currentScreen == Screen.MAIN_MENU || currentScreen == Screen.PAUSE_MENU){
            Gdx.gl.glClearColor(44f/255,97f/255,129f/255,1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            batch.setProjectionMatrix(camera.combined);
            camera.update();
//      All rendering in libgdx is done in the sprite batch
            batch.begin();
            renderer.drawOcean(batch);
            renderer.drawGrass(batch);
            renderer.drawBeach(batch);
//        renderer.drawCoordinates(batch);
            batch.end();

            //INTERACTION WITH MENU
            //IF PLAYER CLICKS ON THE PLAY BUTTON AND EXIT BUTTON. INPUT ADAPTER CLASS CHECKS IF USER PRESSED MOUSE PAD OR KEYBOARD BUTTON
            Gdx.input.setInputProcessor(new InputAdapter() {
                @Override
                public boolean touchDown(int x, int y, int pointer, int button) {
                    if(button == Input.Buttons.LEFT) {
                        if(play_button.contains(mouse_pointer.x, mouse_pointer.y)) {
                            currentScreen = Screen.MAIN_GAME;
                        }
                        else if(exit_button.contains(mouse_pointer.x, mouse_pointer.y)) {
                            Gdx.app.exit(); //EXIT THE GAME
                        }
                        else if(mute_button.contains(mouse_pointer.x, mouse_pointer.y)) {
                            if(MUTED == false) {
                                BackgroundMusic.setVolume(0f);
                                MUTED=true;
                            } else if(MUTED){
                                BackgroundMusic.setVolume(0.2f);
                                MUTED=false;
                            }
                        }
                    }
                    return true;
                }
            });

            batch = new SpriteBatch();
            //TITLE
            batch.begin();
            font.setColor(Color.GOLD);
            font.getData().setScale(4f, 3f);
            font.draw(batch, "Pirate Hygiene", 270f, 520f);
            batch.end();

            //MOUSE POINTER IS A CIRCLE OF RADIUS 1 PIXEL
            mouse_pointer.set( 800f - Gdx.input.getX(), 900f - Gdx.input.getY(), 1);

            //PLAY BUTTON IS A RECTANGLE OF WIDTH 210 PIXEL AND HEIGHT 50 PIXEL
            play_button.set(play_button_X, play_button_Y, 210, 50);

            //EXIT BUTTON IS A RECTANGLE OF WIDTH 210 PIXEL AND HEIGHT 50 PIXEL
            exit_button.set(exit_button_X, exit_button_Y, 210, 50);

            //MUTE BUTTON IS A RECTANGLE OF WIDTH 60 PIXEL AND HEIGHT 40 PIXEL
            mute_button.set(-775f, 840f, 60, 40);

            //HOVERING EFFECT OF MOUSE POINTER AND MUTE BUTTON
            if(mute_button.contains(mouse_pointer.x, mouse_pointer.y)) {
                if (MUTED == false) {
                    shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                    shapeRenderer.setColor(Color.GRAY);
                    shapeRenderer.triangle(1550, 880, 1510, 860, 1550, 840);
                    shapeRenderer.rect(1560, 840, 10, 40);
                    shapeRenderer.end();
                } else {
                    shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                    shapeRenderer.setColor(Color.GRAY);
                    shapeRenderer.triangle(1550, 880, 1510, 860, 1550, 840);
                    shapeRenderer.rect(1560, 840, 10, 40);
                    shapeRenderer.end();

                    Gdx.gl.glLineWidth(3);
                    shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
                    shapeRenderer.setColor(Color.RED);
                    shapeRenderer.circle(1540, 860, 32);
                    shapeRenderer.line(1570,860,1510,860);
                    shapeRenderer.end();
                }
            } else {
                if(MUTED==false){
                    shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                    shapeRenderer.setColor(Color.DARK_GRAY);
                    shapeRenderer.triangle(1550, 880, 1510, 860, 1550, 840);
                    shapeRenderer.rect(1560, 840, 10, 40);
                    shapeRenderer.end();
                } else {
                    shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                    shapeRenderer.setColor(Color.DARK_GRAY);
                    shapeRenderer.triangle(1550, 880, 1510, 860, 1550, 840);
                    shapeRenderer.rect(1560, 840, 10, 40);
                    shapeRenderer.end();

                    Gdx.gl.glLineWidth(3);
                    shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
                    shapeRenderer.setColor(Color.FIREBRICK);
                    shapeRenderer.circle(1540, 860, 32);
                    shapeRenderer.line(1570,860,1510,860);
                    shapeRenderer.end();
                }
            }


            //HOVERING EFFECT OF MOUSE POINTER AND PLAY BUTTON
            if(play_button.contains(mouse_pointer.x, mouse_pointer.y)) {
                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                shapeRenderer.setColor(Color.GREEN);
                shapeRenderer.rect(play_button_X, play_button_Y, 210, 50);
                shapeRenderer.end();

                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                shapeRenderer.setColor(Color.BLACK);
                shapeRenderer.triangle(300, 400, 340, 385, 300, 370);
                shapeRenderer.end();

                batch.begin();
                font.setColor(Color.BLACK);
                font.getData().setScale(3f, 2f);
                font.draw(batch, "P L A Y",play_button_X + 55, play_button_Y + 35);
                batch.end();

            } else {
                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                shapeRenderer.setColor(Color.FOREST);
                shapeRenderer.rect(play_button_X, play_button_Y, 210, 50);
                shapeRenderer.end();

                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                shapeRenderer.setColor(Color.BLACK);
                shapeRenderer.triangle(300, 400, 340, 385, 300, 370);
                shapeRenderer.end();

                batch.begin();
                font.setColor(Color.BLACK);
                font.getData().setScale(3f, 2f);
                font.draw(batch, "P L A Y", play_button_X + 55, play_button_Y + 35);
                batch.end();
            }

            //HOVERING EFFECT OF MOUSE POINTER AND EXIT BUTTON
            if(exit_button.contains(mouse_pointer.x, mouse_pointer.y)) {
                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                shapeRenderer.setColor(Color.RED);
                shapeRenderer.rect(exit_button_X, exit_button_Y, 210, 50);
                shapeRenderer.end();

                batch.begin();
                font.setColor(Color.BLACK);
                font.getData().setScale(3.5f, 2.5f);
                font.draw(batch, "E X I T", exit_button_X + 40, exit_button_Y + 40);
                batch.end();

            } else {
                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                shapeRenderer.setColor(Color.FIREBRICK);
                shapeRenderer.rect(exit_button_X, exit_button_Y, 210, 50);
                shapeRenderer.end();

                batch.begin();
                font.setColor(Color.BLACK);
                font.getData().setScale(3.5f, 2.5f);
                font.draw(batch, "E X I T", exit_button_X + 40, exit_button_Y + 40);
                batch.end();
            }
        }

    }

    @Override
    public void dispose() {}


    private void handleInput() {
        //        if (Gdx.input.isKeyPressed(Input.Keys.valueOf("=")))
//            if (camera.zoom >= 0.35f) {camera.zoom -= 0.004f;}
//        if (Gdx.input.isKeyPressed(Input.Keys.valueOf("-")))
//            if (camera.zoom <= 0.625f) {camera.zoom += 0.004f;}
            if (Gdx.input.isKeyPressed(Input.Keys.UP))
                if  (camera.position.y <= 28*64)
                {camera.position.y += 3 + camera.zoom;}
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
                if (camera.position.y >= 4.5*64)
                {camera.position.y -= 3 + camera.zoom;}
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
                if (camera.position.x >= -24*64)
                {camera.position.x -= 3 + camera.zoom;}
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
                if (camera.position.x <= 25*64)
                {camera.position.x += 3 + camera.zoom;}
//        System.out.println(camera.zoom);
//        System.out.println(camera.position.x + ", " + camera.position.y);
            if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE))
                currentScreen = Screen.PAUSE_MENU;
    }
}
