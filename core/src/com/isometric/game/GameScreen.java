package com.isometric.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameScreen extends ScreenAdapter {
    @SuppressWarnings("FieldMayBeFinal")
    private SpriteBatch batch;
    private OrthographicCamera camera;
//  Screen Size
    public static final int HEIGHT = 180 * 5;
    public static final int WIDTH = 320 * 5;
    private IsometricRenderer renderer;

    public GameScreen(SpriteBatch batch) {
        this.batch = batch;
    }

    @Override
    public void show() {
        camera = new OrthographicCamera(WIDTH, HEIGHT);
        //noinspection IntegerDivisionInFloatingPointContext
        camera.position.set(0, HEIGHT / 2, 10);
        camera.zoom = 0.625f;
        renderer = new IsometricRenderer();
    }

    @Override
    public void render(float delta) {
//        Delta is the time between frames
        Gdx.gl.glClearColor(44f/255,97f/255,129f/255,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);
        camera.update();
        handleInput();
//      All rendering in libgdx is done in the sprite batch
        batch.begin();
        renderer.drawBoard(batch);
//        renderer.drawCoordinates(batch, true);
        batch.end();
    }

    @Override
    public void dispose() {}


    private void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.valueOf("="))) {camera.zoom -= 0.004f;}
        if (Gdx.input.isKeyPressed(Input.Keys.valueOf("-"))) {camera.zoom += 0.004f;}
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {camera.position.y += 3 + camera.zoom;}
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {camera.position.y -= 3 + camera.zoom;}
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {camera.position.x -= 3 + camera.zoom;}
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {camera.position.x += 3 + camera.zoom;}
/*
        Todo: set boundaries on the map (later in development)
        System.out.println(camera.zoom);
        System.out.println(camera.position.x + ", " + camera.position.y);
*/
    }
}
