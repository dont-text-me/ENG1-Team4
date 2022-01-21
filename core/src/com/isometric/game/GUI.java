package com.isometric.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class GUI extends Game {
    SpriteBatch batch;
    BitmapFont font;
    ShapeRenderer shapeRenderer;
    Texture texture;
    float totalHealth = 10;
    float enemyDamage = 1;
    float currentHealth = totalHealth;
    int score = 0;
    int gold = 0;


    @Override
    public void create () {
        shapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.getData().scale(1);
        texture = new Texture(Gdx.files.internal("Gold/Gold_1.png"));
    }

    @Override
    public void render () {
        if (Gdx.input.isKeyPressed(Input.Keys.W) && currentHealth > 0) {
            currentHealth -= enemyDamage;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.E)) {
            score += 200;
            gold += 100;
        }

        Gdx.gl.glClearColor(.25f, .25f, .25f, 1); //set colour of health bar
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 1, 0, 1);
        shapeRenderer.rect(Gdx.graphics.getWidth()/2 - 250, 50, (currentHealth * 50), 20); //draw health bar rectangle
        shapeRenderer.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(1, 1, 1, 1);
        shapeRenderer.rect(Gdx.graphics.getWidth()/2 - 248, 49, (totalHealth * 50), 20); //draw health bar rectangle
        shapeRenderer.end();
        batch.begin();
        font.draw(batch, ("SCORE: " + Integer.toString(score)), Gdx.graphics.getWidth() - 250, Gdx.graphics.getHeight()-50);
        font.draw(batch, (("X ") + Integer.toString(gold)), Gdx.graphics.getWidth() - 1480, Gdx.graphics.getHeight()-50);
        batch.draw(texture, Gdx.graphics.getWidth() - 1550, Gdx.graphics.getHeight()-85);;
        batch.end();


    }

    @Override
    public void dispose () {
        shapeRenderer.dispose();
        batch.dispose();
        font.dispose();
        texture.dispose();
    }

}
