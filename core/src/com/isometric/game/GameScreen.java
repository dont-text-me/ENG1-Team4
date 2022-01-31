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
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.*;


public class GameScreen extends ScreenAdapter {




    enum Screen {
        MAIN_MENU, MAIN_GAME, PAUSE_MENU, LOSE_SCREEN, WIN_SCREEN
    }

    Screen currentScreen = Screen.MAIN_MENU;

    //  Screen Size
    private SpriteBatch batch, batchS;
    private OrthographicCamera camera;
    private ScreenViewport viewport;

    private Stage stage;

    public static final int HEIGHT = 180 * 5;
    public static final int WIDTH = 320 * 5;
    public boolean MUTED = true;
    private IsometricRenderer renderer;

    //Ships
    private PlayerShip player;
    private Array<EnemyShip> enemyShips;
    @SuppressWarnings("All")
    private boolean isPlayerDead = false;

    private BitmapFont fontS;
    private ShapeRenderer shapeRendererS;

    Texture texture;
    float totalHealth = 10;
    //    float enemyDamage = 1;
    float currentHealth = totalHealth;
    int score = 0;
    int gold = 0;

    public College [] colleges;
    public ArrayList<Projectile> balls;
    public LinkedList<Coin> coins;
    private float point = 0f;
    //Background Music
    Music BackgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("Sound Effects and Music/1700s sea shanties.mp3"));
    public Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

    @Override
    public void show() {
//        GAME
        camera = new OrthographicCamera(WIDTH, HEIGHT);
//        viewport =  new ExtendViewport(WIDTH,HEIGHT,camera);
        viewport = new ScreenViewport(camera);
        viewport.apply();
        renderer = new IsometricRenderer(false);
        player = new PlayerShip(renderer);
        playerBox = new Polygon(new float[]{player.position.x, player.position.y - 32, player.position.x + 64, player.position.y, player.position.x, player.position.y + 32, player.position.x -64, player.position.y});
        EnemyShip enemy1 = new EnemyShip(renderer);
        EnemyShip enemy2 = new EnemyShip(renderer);
        EnemyShip enemy3 = new EnemyShip(renderer);
        EnemyShip enemy4 = new EnemyShip(renderer);
        EnemyShip enemy5 = new EnemyShip(renderer);
        enemyShips = new Array<>();
        enemyShips.add(enemy1);
        enemyShips.add(enemy2);
        enemyShips.add(enemy3);
        enemyShips.add(enemy4);
        enemyShips.add(enemy5);
        camera.zoom = 0.625f;

//        GUI
        stage = new Stage(new ScreenViewport());


        Table table = new Table();
        table.setWidth(stage.getWidth());
        table.align(Align.center|Align.top);

        table.setPosition(0,Gdx.graphics.getHeight());

        final TextButton playButton = new TextButton("Play Game", skin);
        final TextButton muteButton = new TextButton("Mute Music", skin);
        final TextButton howToPlayButton = new TextButton("How to Play", skin);
        final TextButton quitButton = new TextButton("Quit Game", skin);
//        TextButton regenerateButton = new TextButton("Generate New Map", skin);

        int p = 20;
        table.pad(350);
        table.add(playButton).padBottom(p);
        table.row();
        table.add(howToPlayButton).padBottom(p);
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

        howToPlayButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("To Add");
            }
        });

//        regenerateButton.addListener(new ClickListener() {
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//                renderer = new IsometricRenderer(true);
//                player = new PlayerShip(renderer);
//                colleges = place_colleges(5);
//                coins = placeCoins(20);
//                balls = new ArrayList<>();
//                currentHealth = 10;
//            }
//        });


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
        BackgroundMusic.setVolume(0f);
        BackgroundMusic.play();

//        In game GUI
        shapeRendererS = new ShapeRenderer();
        batch = new SpriteBatch();
        batchS = new SpriteBatch();
        fontS = new BitmapFont();
        fontS.getData().scale(1);
        texture = new Texture(Gdx.files.internal("Gold/Gold_1.png"));
        colleges = place_colleges(5);
        coins = placeCoins(20);
        balls = new ArrayList<>();
    }

    private void batchRender() {
        batch.begin();
        renderer.drawBoard(batch);
//        renderer.drawCoordinates(batch, true);
        for (Coin c: coins) {
            c.render(batch);
        }
        for (Projectile ball : balls) {
            if (ball.isActive()) {
                ball.render(batch);
            }
        }
        for (College c : colleges){
            c.render(batch, player);
        }
        player.render(batch);
        //noinspection GDXJavaUnsafeIterator
        for (EnemyShip enemyShip : enemyShips){
            enemyShip.render(batch);
        }
        batch.end();
    }

    @Override
    public void resize(int width, int height){
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        stage.getViewport().update(width,height);
        viewport.update(width, height);
    }

    @Override
    public void render(float delta) {

        if (currentHealth < 0.1) {
            isPlayerDead = true;
            currentScreen = Screen.LOSE_SCREEN;
        }

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
        Gdx.gl.glClearColor(44f / 255, 97f / 255, 129f / 255, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);

        if (currentScreen == Screen.MAIN_GAME) {
            point += delta;
            if (point > 1) {
                score ++;
                point = 0;
            }
            camera.position.set(player.position.x, player.position.y, 0);
            camera.update();
            player.update(renderer, enemyShips);
            for (EnemyShip enemyShip : enemyShips){
                enemyShip.update(renderer, player, enemyShips);
            }
            balls = filter_projectiles(balls);
            check_collisions(balls, colleges);
            for (Projectile ball : balls) {
                ball.update();
            }
            for (int j = 0; j < coins.size(); j++) {
                if (coins.get(j).update(player)) {
                    //noinspection SuspiciousListRemoveInLoop
                    coins.remove(j);
                    gold += 1;
                    score += 50;
                }
            }
            for (College c : colleges) {
                score += c.update(player);
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
            camera.position.set(player.position.x, player.position.y, 0);
            camera.update();
//            player.update(renderer);
//      All rendering in libgdx is done in the sprite batch
            batchRender();
            stage.act(delta);
            stage.draw();

            //TITLE
            batch = new SpriteBatch();
            batch.begin();
            BitmapFont titleFont = new BitmapFont(Gdx.files.internal("gothicpirate.fnt"), false);
            titleFont.setColor(Color.GOLD);
            titleFont.getData().setScale(2f, 2f);
            titleFont.draw(batch, "Pirate Hygiene", 150, 800);
            batch.end();
        } else if (currentScreen == Screen.LOSE_SCREEN) {
            camera.position.set(player.position.x, player.position.y, 0);
            camera.update();
            batchRender();
            Dialog winnerDialog = new Dialog("You Win", skin);
            stage.act(delta);
            stage.draw();

            //TITLE
            batch = new SpriteBatch();
            batch.begin();
            BitmapFont titleFont = new BitmapFont(Gdx.files.internal("gothicpirate.fnt"), false);
            titleFont.setColor(Color.GOLD);
            titleFont.getData().setScale(2f, 2f);
            titleFont.draw(batch, "Pirate Hygiene", 150, 800);
            batch.end();
        }
    }

    public LinkedList<Coin> placeCoins(int num) {
        LinkedList<Coin> generatedCoins = new LinkedList<>();
        Vector2[] coinLocations = new Vector2[num];
        int i = 0;
        while (i < num) {
            Random random = new Random();
            int x = random.nextInt(57) + 4;
            int y = random.nextInt(57) + 4;


            if (renderer.board2d[y][x].equals("9")) {
                Vector2 pos = new Vector2(x, y);
                if (!(Arrays.asList(coinLocations).contains(pos))) {
                    coinLocations[i] = pos;
                    i++;
                }
            }
        }
        for (int j = 0; j < num; j ++){
            generatedCoins.add(j, new Coin((int) coinLocations[j].x, (int) coinLocations[j].y));
        }
        return generatedCoins;
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
            balls.add(new Projectile(
                    player.tilePosition.y + 1f,
                    player.tilePosition.x + 0.25f,
                    player.getCurrentDirection() == 2f ? 1f : (player.getCurrentDirection() == 3f ? -1f : 0f),
                    player.getCurrentDirection() == 0f ? 1f : (player.getCurrentDirection() == 1f ? -1f : 0f),
                    true));
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
                if (college.getTilePosition().epsilonEquals(ball.nearestTile().x, ball.nearestTile().y, 1.0f)){ // giving the player a little help aiming
                    if (ball.isByPlayer()) {
                        college.takeDamage(5);
                        ball.deactivate();
                    }
                }
            }
            //noinspection SuspiciousNameCombination
            if ((player.tilePosition.epsilonEquals(ball.nearestTile().y, ball.nearestTile().x, 1.0f)) && !(ball.isByPlayer())){
                if (currentHealth > 0) {
                    currentHealth -= 0.5;
                }
                ball.deactivate();
                // Player takes damage here
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
