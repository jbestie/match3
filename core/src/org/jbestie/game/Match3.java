package org.jbestie.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Match3 extends ApplicationAdapter {
    private static final int HORIZONTAL_CELLS_COUNT = 3;
    private static final int VERTICAL_CELLS_COUNT = 3;
    private static final float ITEM_WIDTH = 100.0f;
    private static final float ITEM_HEIGHT = 100.0f;
    private static final float WINDOW_WIDTH = 640.0f;
    private static final float WINDOW_HEIGHT = 480.0f;
    private static final float CELL_SPACING_COEFFICIENT = 0.2f;
    private SpriteBatch batch;
	private Sprite background;
    private List<Texture> textures;
    private Sprite[][] field;

	@Override
	public void create () {
		batch = new SpriteBatch();
        background  = new Sprite( new Texture(Gdx.files.internal("background/bg.png")));
        background.setPosition(0.0f, 0.0f);

        Texture blueJelly = new Texture(Gdx.files.internal("sprites/jelly_blue.png"));
        Texture greenJelly = new Texture(Gdx.files.internal("sprites/jelly_green.png"));
        Texture greyJelly = new Texture(Gdx.files.internal("sprites/jelly_grey.png"));
        Texture purpleJelly = new Texture(Gdx.files.internal("sprites/jelly_purple.png"));
        Texture redJelly = new Texture(Gdx.files.internal("sprites/jelly_red.png"));
        Texture yellowJelly = new Texture(Gdx.files.internal("sprites/jelly_yellow.png"));
        textures = Arrays.asList(blueJelly, greenJelly, greyJelly, purpleJelly, redJelly, yellowJelly);

        field = generateField();
    }


	private Sprite[][] generateField() {
	    Sprite[][] result = new Sprite[VERTICAL_CELLS_COUNT][HORIZONTAL_CELLS_COUNT];

	    float startXPosition = (WINDOW_WIDTH - ITEM_WIDTH * HORIZONTAL_CELLS_COUNT - (HORIZONTAL_CELLS_COUNT - 1) * CELL_SPACING_COEFFICIENT)/ 2;
	    float startYPosition = ITEM_HEIGHT * VERTICAL_CELLS_COUNT + (VERTICAL_CELLS_COUNT - 1) * CELL_SPACING_COEFFICIENT ;

        Random rand = new Random();
	    for (int i = 0; i < VERTICAL_CELLS_COUNT; i++) {
	        for (int j = 0; j < HORIZONTAL_CELLS_COUNT; j++) {
                result[i][j] = new Sprite(textures.get(rand.nextInt(textures.size() - 1)));
                result[i][j].setPosition(startXPosition + i * ITEM_WIDTH + CELL_SPACING_COEFFICIENT * i, startYPosition -  j * ITEM_HEIGHT + CELL_SPACING_COEFFICIENT * j);
                result[i][j].setSize(ITEM_WIDTH, ITEM_HEIGHT);
            }
        }

        return result;
    }


	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		//place draw logic here
        background.draw(batch);

        drawField(field, batch);

		//end of draw logic
		batch.end();
	}

    private void drawField(Sprite[][] field, SpriteBatch batch) {
        for (int i = 0; i < VERTICAL_CELLS_COUNT; i++) {
            for (int j = 0; j < HORIZONTAL_CELLS_COUNT; j++) {
                field[i][j].draw(batch);
            }
        }
	}

    @Override
	public void dispose () {
		batch.dispose();
	}
}
