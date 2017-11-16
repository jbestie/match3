package org.jbestie.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
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
    private static final int WORLD_WIDTH = 640;
    private static final int WORLD_HEIGHT = 480;
    private SpriteBatch batch;
	private Sprite background;
    private List<Texture> textures;
    private List<Sprite> field;

    private Viewport viewport;
    private Camera camera;
    private Logger logger;




	@Override
	public void create () {

        camera = new OrthographicCamera(Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight());
        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
        camera.update();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);

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
        logger = new Logger(getClass().getSimpleName());

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                Vector3 tempVec =  new Vector3();
                camera.unproject(tempVec.set(screenX, screenY, 0));

                int x = (int) tempVec.x;
                int y = (int) tempVec.y;

                for (Sprite object : field) {
                    int ox = Math.round(object.getX());
                    int oy = Math.round(object.getY());
                    if ((x > ox && x <= (ox + object.getWidth()))
                        && (y > oy && y <= (oy + object.getHeight()))) {
                        logger.info("I'm hit!!!!");
                        field.remove(object);
                        break;
                    }

                }
                return true;
            }
        });
    }


	private List<Sprite> generateField() {
	    List<Sprite> result = new ArrayList<Sprite>();

	    float startXPosition = (WINDOW_WIDTH - ITEM_WIDTH * HORIZONTAL_CELLS_COUNT - (HORIZONTAL_CELLS_COUNT - 1) * CELL_SPACING_COEFFICIENT)/ 2;
	    float startYPosition = ITEM_HEIGHT * VERTICAL_CELLS_COUNT + (VERTICAL_CELLS_COUNT - 1) * CELL_SPACING_COEFFICIENT ;

        Random rand = new Random();
        for (int i = 0; i < VERTICAL_CELLS_COUNT ; i++) {
            for (int j = 0; j < HORIZONTAL_CELLS_COUNT; j++) {

                Sprite sprite = new Sprite(textures.get(rand.nextInt(textures.size() - 1)));
                sprite.setPosition(startXPosition + i * ITEM_WIDTH + CELL_SPACING_COEFFICIENT * i, startYPosition - j * ITEM_HEIGHT + CELL_SPACING_COEFFICIENT * j);
                sprite.setSize(ITEM_WIDTH, ITEM_HEIGHT);
                result.add(sprite);
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

    private void drawField(List<Sprite> field, SpriteBatch batch) {
        for (Sprite sprite : field) {
            sprite.draw(batch);
        }
	}

    @Override
	public void dispose () {
		batch.dispose();
	}


}
