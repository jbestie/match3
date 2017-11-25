package org.jbestie.game.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import org.jbestie.game.Match3;
import org.jbestie.game.utils.GameConstants;

public class MenuScreen implements Screen {
    private Viewport viewport;
    private final Match3 game;
    private Texture background;
    private OrthographicCamera camera;
    private Texture playButton;

    public MenuScreen(Match3 game) {
        this.game = game;
        background  = new Texture(Gdx.files.internal("background/logo_bg.png"));
        playButton  = new Texture(Gdx.files.internal("sprites/play_button.png"));
        camera = new OrthographicCamera();
        camera.setToOrtho(false, GameConstants.WINDOW_WIDTH, GameConstants.WINDOW_HEIGHT);
        viewport = new FitViewport(GameConstants.WORLD_WIDTH, GameConstants.WORLD_HEIGHT, camera);
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.batch.draw(background, 0.0f, 0.0f, GameConstants.WINDOW_WIDTH, GameConstants.WINDOW_HEIGHT);
        game.batch.draw(playButton, GameConstants.WINDOW_WIDTH / 2 , GameConstants.WINDOW_HEIGHT / 6, GameConstants.WINDOW_WIDTH / 6, GameConstants.WINDOW_WIDTH / 6);

        game.batch.end();

        if (Gdx.input.isTouched()) {
            game.setScreen(new LevelSelectScreen(game));
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, false);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
