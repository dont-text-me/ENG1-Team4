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
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import sun.jvm.hotspot.tools.SysPropsDumper;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
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

    public GameScreen() {
    }

    enum Screen {
        MAIN_MENU, MAIN_GAME, PAUSE_MENU
    }

    Screen currentScreen = Screen.MAIN_MENU;

//  Screen Size
    private SpriteBatch batch, batchS;
    private OrthographicCamera camera;
    private ExtendViewport viewport;

    private Stage stage;

    public static final int HEIGHT = 180 * 5;
    public static final int WIDTH = 320 * 5;
    public boolean MUTED = false;
    private IsometricRenderer renderer;

    //Ships
    private PlayerShip player;
    private Array<EnemyShip> enemyShips;
    private EnemyShip enemy1;
    private EnemyShip enemy2;
    private EnemyShip enemy3;
    private EnemyShip enemy4;
    private EnemyShip enemy5;

    //Collision
    private Polygon playerBox;
    private Polygon enemy1Box;

    private BitmapFont fontS;
    private ShapeRenderer shapeRendererS;

    Texture texture;
    float totalHealth = 10;
//    float enemyDamage = 1;
    float currentHealth = totalHealth;
    int score = 0;
    int gold = 0;

    public College [] colleges;
    public ArrayList <Projectile> balls;
    public int whichCollege = 0;

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
        playerBox = new Polygon(new float[]{player.position.x, player.position.y - 32, player.position.x + 64, player.position.y, player.position.x, player.position.y + 32, player.position.x -64, player.position.y});
        enemy1 = new EnemyShip(renderer);
        enemy2 = new EnemyShip(renderer);
        enemy3 = new EnemyShip(renderer);
        enemy4 = new EnemyShip(renderer);
        enemy5 = new EnemyShip(renderer);
        enemyShips = new Array<EnemyShip>();
        enemyShips.add(enemy1);
        enemyShips.add(enemy2);
        enemyShips.add(enemy3);
        enemyShips.add(enemy4);
        enemyShips.add(enemy5);
        camera.zoom = 0.625f;

//        GUI
        stage = new Stage(new ScreenViewport());
        //    GUI
        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

        Table table = new Table();
        table.setWidth(stage.getWidth());
        table.align(Align.center|Align.top);

        table.setPosition(0,Gdx.graphics.getHeight());

        TextButton playButton = new TextButton("Play Game", skin);
        TextButton quitButton = new TextButton("Quit Game", skin);
        final TextButton muteButton = new TextButton("Mute Music", skin);
        TextButton regenerateButton = new TextButton("Generate New Map", skin);

        int p = 20;
        table.pad(350);
        table.add(playButton).padBottom(p);
        table.row();
        table.add(regenerateButton).padBottom(p);
        table.row();
        table.add(muteButton).padBottom(p);
        table.row();
        table.add(quitButton).padBottom(p);

        stage.addActor(table);

        Gdx.input.setInputProcessor(stage);

        playButton.setWidth(100);
        playButton.setHeight(50);

        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                currentScreen = Screen.MAIN_GAME;
            }
        });

        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        regenerateButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                renderer = new IsometricRenderer();
                player = new PlayerShip(renderer);
                colleges = place_colleges(5);
                balls = new ArrayList<>();
            }
        });


        muteButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!MUTED) {
                    BackgroundMusic.setVolume(0f);
                    MUTED = true;
                    muteButton.setText("Muted Music");
                } else {
                    BackgroundMusic.setVolume(0.2f);
                    MUTED = false;
                    muteButton.setText("Playing Music");
                }
            }
        });

        BackgroundMusic.setLooping(true);
        BackgroundMusic.setVolume(0.2f);
        BackgroundMusic.play();

//        In game GUI
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
        for (EnemyShip enemyShip : enemyShips){
            enemyShip.render(batch);
        }
        batch.end();
    }

    @Override
    public void resize(int width, int height){
        viewport.update(width, height);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
    }

    @Override
    public void render(float delta) {

        if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
            if (!MUTED) {
                BackgroundMusic.setVolume(0f);
                MUTED = true;
            } else {
                BackgroundMusic.setVolume(0.2f);
                MUTED = false;
            }
        }

//        Updates for calculating ball travel vectors.
        handleInput();

        if (currentScreen == Screen.MAIN_GAME) {
            Gdx.gl.glClearColor(44f / 255, 97f / 255, 129f / 255, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            camera.position.set(player.position.x, player.position.y, 0);
            batch.setProjectionMatrix(camera.combined);
            camera.update();
            player.update(renderer);
            playerBox.setPosition(player.position.x, player.position.y);
            for (EnemyShip enemyShip : enemyShips){
                enemyShip.update(renderer);
            }

            balls = filter_projectiles(balls);
            check_collisions(balls, colleges);
            for (Projectile ball : balls) {
                ball.update();
            }
            for (College c : colleges) {
                c.update(player);
                if (c.isFiring()){
                    balls.add(c.shoot(player.tilePosition));
                }
            }
            handleInput();
//      All rendering in libgdx is done in the sprite batch
            batchRender();
            batchS.begin();
            fontS.draw(batchS, ("SCORE: " + score), Gdx.graphics.getWidth() - 250, Gdx.graphics.getHeight() - 50);
            fontS.draw(batchS, (("X ") + gold), Gdx.graphics.getWidth() - 1480, Gdx.graphics.getHeight() - 50);
            batchS.draw(texture, Gdx.graphics.getWidth() - 1550, Gdx.graphics.getHeight() - 85);
            batchS.end();
            shapeRendererS.begin(ShapeRenderer.ShapeType.Filled);
            shapeRendererS.setColor(0, 1, 0, 1);
            shapeRendererS.rect(Gdx.graphics.getWidth() / 2f - 250, 50, (currentHealth * 50), 20); //draw health bar rectangle
            shapeRendererS.end();
            shapeRendererS.begin(ShapeRenderer.ShapeType.Line);
            shapeRendererS.setColor(1, 1, 1, 1);
            shapeRendererS.rect(Gdx.graphics.getWidth() / 2f - 248, 49, (totalHealth * 50), 20); //draw health bar rectangle
            shapeRendererS.end();
            //
        } else if (currentScreen == Screen.MAIN_MENU || currentScreen == Screen.PAUSE_MENU) {
            Gdx.gl.glClearColor(44f / 255, 97f / 255, 129f / 255, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            batch.setProjectionMatrix(camera.combined);
            camera.position.set(player.position.x, player.position.y, 0);
            camera.update();
//            player.update(renderer);
//      All rendering in libgdx is done in the sprite batch
            batchRender();
            stage.act(delta);
            stage.draw();

            batch = new SpriteBatch();
            //TITLE
            batch.begin();
            BitmapFont titleFont = new BitmapFont(Gdx.files.internal("gothicpirate.fnt"), false);
            titleFont.setColor(Color.GOLD);
            titleFont.getData().setScale(2f, 2f);
            titleFont.draw(batch, "Pirate Hygiene", 150, 800);
            batch.end();
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
            colleges[i] = new College((int)college_locations[i].x, (int)college_locations[i].y);
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
                    if (!(college.isDefeated())) {
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

