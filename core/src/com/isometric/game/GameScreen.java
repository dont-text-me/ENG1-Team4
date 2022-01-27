package com.isometric.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayList;
import java.util.Arrays;

public class GameScreen extends ScreenAdapter {

    enum Screen {
        MAIN_MENU, MAIN_GAME, PAUSE_MENU
    }

    Screen currentScreen = Screen.MAIN_MENU;

//  Screen Size
    private SpriteBatch batch, batchS;
    private OrthographicCamera camera;
    private ExtendViewport viewport;

//    GUI
    private Skin skin;
    private Stage stage;
    private Table table;
    private TextButton playButton;
    private TextButton quitButton;

    public static final int HEIGHT = 180 * 5;
    public static final int WIDTH = 320 * 5;
    public boolean MUTED = false;
    private IsometricRenderer renderer;
    private PlayerShip player;

    //  Main Menu
    Rectangle play_button;
    Rectangle exit_button;
    Rectangle mute_button;
    private BitmapFont font, fontS;
    private Circle mouse_pointer;
    private ShapeRenderer shapeRenderer, shapeRendererS;

    Texture texture;
    float totalHealth = 10;
    float enemyDamage = 1;
    float currentHealth = totalHealth;
    int score = 0;
    int gold = 0;

    public College [] colleges;
    public ArrayList <Projectile> balls;
    public int whichCollege = 0;

    //POSITION OF PLAY BUTTON
    float play_button_X = 290f;
    float play_button_Y = 360f;

    //POSITION OF EXIT BUTTON
    float exit_button_X = 290f;
    float exit_button_Y = 260f;

    //Background Music
    Music BackgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("Sound Effects and Music/1700s sea shanties.mp3"));

    @Override
    public void show() {
//        GAME
        camera = new OrthographicCamera(WIDTH, HEIGHT);
        viewport =  new ExtendViewport(WIDTH,HEIGHT,camera);
        viewport.apply();
        renderer = new IsometricRenderer();
        player = new PlayerShip(renderer);
        camera.zoom = 0.625f;

//        GUI
        stage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        table = new Table();
        table.setWidth(stage.getWidth());
        table.align(Align.center|Align.top);

        table.setPosition(0,Gdx.graphics.getHeight());

        playButton = new TextButton("New Game", skin);
        quitButton = new TextButton("Quit Game", skin);

        table.add(playButton);
        table.add(quitButton);

        stage.addActor(table);

        Gdx.input.setInputProcessor(stage);

        playButton.setWidth(100);
        playButton.setHeight(50);

        final Dialog dialog = new Dialog("Click Message", skin);

        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dialog.show(stage);
            }
        });



//        MAIN MENU
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
        shapeRendererS = new ShapeRenderer();
        batch = new SpriteBatch();
        batchS = new SpriteBatch();
        fontS = new BitmapFont();
        fontS.getData().scale(1);
        texture = new Texture(Gdx.files.internal("Gold/Gold_1.png"));
        colleges = place_colleges(5);
        balls = new ArrayList<>();
    }

    private void batchRender() {
        batch.begin();
        renderer.drawBoard(batch);
        for (Projectile ball : balls) {
            if (ball.isActive()) {
                ball.render(batch);
            }
        }
        for (College c : colleges){
            c.render(batch);
        }
        player.render(batch);
        batch.end();
    }

    @Override
    public void resize(int width, int height){
        viewport.update(width, height);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
    }

    @Override
    public void render(float delta) {

        if(Gdx.input.isKeyJustPressed(Input.Keys.M)){
            if(!MUTED){
                BackgroundMusic.setVolume(0f);
                MUTED=true;
            } else {
                BackgroundMusic.setVolume(0.2f);
                MUTED=false;
            }
        }

//        Updates for calculating ball travel vectors.
        handleInput();
        balls = filter_projectiles(balls);
        check_collisions(balls, colleges);
        for (Projectile ball : balls) {
            ball.update();
        }
        for (College c : colleges) {
            c.update();
        }

        if(currentScreen == Screen.MAIN_GAME){
            Gdx.gl.glClearColor(44f/255,97f/255,129f/255,1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            camera.position.set(player.position.x, player.position.y, 0);
            batch.setProjectionMatrix(camera.combined);
            camera.update();
            player.update(renderer);
            handleInput();
//      All rendering in libgdx is done in the sprite batch
            batchRender();
            batchS.begin();
            fontS.draw(batchS, ("SCORE: " + score), Gdx.graphics.getWidth() - 250, Gdx.graphics.getHeight()-50);
            fontS.draw(batchS, (("X ") + gold), Gdx.graphics.getWidth() - 1480, Gdx.graphics.getHeight()-50);
            batchS.draw(texture, Gdx.graphics.getWidth() - 1550, Gdx.graphics.getHeight()-85);
            batchS.end();
            shapeRendererS.begin(ShapeRenderer.ShapeType.Filled);
            shapeRendererS.setColor(0, 1, 0, 1);
            shapeRendererS.rect(Gdx.graphics.getWidth()/2f - 250, 50, (currentHealth * 50), 20); //draw health bar rectangle
            shapeRendererS.end();
            shapeRendererS.begin(ShapeRenderer.ShapeType.Line);
            shapeRendererS.setColor(1, 1, 1, 1);
            shapeRendererS.rect(Gdx.graphics.getWidth()/2f - 248, 49, (totalHealth * 50), 20); //draw health bar rectangle
            shapeRendererS.end();
            //
        }
        else if(currentScreen == Screen.MAIN_MENU || currentScreen == Screen.PAUSE_MENU){
            Gdx.gl.glClearColor(44f/255,97f/255,129f/255,1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            batch.setProjectionMatrix(camera.combined);
            camera.position.set(player.position.x, player.position.y, 0);
            camera.update();
//            player.update(renderer);
//      All rendering in libgdx is done in the sprite batch
            batchRender();
            stage.act(delta);
            stage.draw();
            //INTERACTION WITH MENU
            //IF PLAYER CLICKS ON THE PLAY BUTTON AND EXIT BUTTON. INPUT ADAPTER CLASS CHECKS IF USER PRESSED MOUSE PAD OR KEYBOARD BUTTON
//            Gdx.input.setInputProcessor(new InputAdapter() {
//                @Override
//                public boolean touchDown(int x, int y, int pointer, int button) {
//                    if(button == Input.Buttons.LEFT) {
//                        if(play_button.contains(mouse_pointer.x, mouse_pointer.y)) {
//                            currentScreen = Screen.MAIN_GAME;
//                        }
//                        else if(exit_button.contains(mouse_pointer.x, mouse_pointer.y)) {
//                            Gdx.app.exit(); //EXIT THE GAME
//                        }
//                        else if(mute_button.contains(mouse_pointer.x, mouse_pointer.y)) {
//                            if(!MUTED) {
//                                BackgroundMusic.setVolume(0f);
//                                MUTED=true;
//                            } else {
//                                BackgroundMusic.setVolume(0.2f);
//                                MUTED=false;
//                            }
//                        }
//                    }
//                    return true;
//                }
//            });

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
                if (!MUTED) {
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
                if(!MUTED){
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
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            currentScreen = Screen.PAUSE_MENU;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.valueOf("="))) {camera.zoom -= 0.004f;}
        if (Gdx.input.isKeyPressed(Input.Keys.valueOf("-"))) {camera.zoom += 0.004f;}
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                balls.add(colleges[whichCollege].shoot(new Vector2(63, 63)));
                whichCollege += 1;
                if (whichCollege == colleges.length){
                    whichCollege = 0;
                }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.R)) {
            for (College c : colleges){
                c.setHealth(100);
            }
        }
    }
/**
 * Returns an array of colleges of specified length.
 * Colleges are placed on islands with 1 patch of grass surrounded by beach
 * @param num the desired number of colleges
 * @return the array containing College objects, placed on islands on the game map
 * */
    public College [] place_colleges(int num){
        College [] colleges = new College[num];
        Vector2[] college_locations = new Vector2[num];
        for (int i = 0; i < num; i ++){
            for (int y = renderer.board_size; y > 0; y --){
                for (int x = renderer.board_size; x > 0; x --){
                    if (
                            renderer.map[y].charAt(x) == '0' &&
                            renderer.map[y].charAt(x - 1) == '1' &&
                            renderer.map[y].charAt(x + 1) == '3' &&
                            renderer.map[y - 1].charAt(x) == '2'
//                            renderer.map[y + 1].charAt(x) == '4'
                    ){
                        Vector2 pos = new Vector2 (x, y);
                        if (!(Arrays.asList(college_locations).contains(pos))){
                            college_locations[i] = pos;
                        }
                    }
                }
            }
        }
        for (int i = 0; i < num; i ++){
            colleges[i] = new College((int)college_locations[i].x, (int)college_locations[i].y, 0);
        }
        return colleges;
    }

    /**
     * Checks whether any of the projectiles entered a tile where a college is located.
     * If a projectile intersects with a college that is still not defeated, the college takes damage and the projectie
     * gets deactivated. Otherwise, the projectile continues to travel straignt through the college.
     * @param balls ArrayList of Projectile objects
     * @param colleges Array of College objects
     * */
    public void check_collisions(ArrayList<Projectile> balls, College [] colleges){
        for (Projectile ball : balls) {
            for (College college : colleges) {
                if (college.getTilePosition().epsilonEquals(ball.nearestTile())) {
                    if (ball.isByPlayer()) {
                        college.takeDamage(20);
                    }
                    if (college.isDefeated()) {
                        ball.deactivate(); // if college is defeated, projectiles will travel through.
                    }
                }
            }
        }
    }
    /**
     * Iterates over projectile list and removes deactivated ones.
     * @param balls ArrayList of projectiles, before filtering
     * @return ArrayList of projectiles, filtered.
     * */
    public ArrayList <Projectile> filter_projectiles(ArrayList<Projectile> balls){
        ArrayList <Projectile> output = new ArrayList<>();
        for (Projectile ball : balls){
            if (ball.isActive()){
                output.add(ball);
            }
        }
        return output;
    }
}

