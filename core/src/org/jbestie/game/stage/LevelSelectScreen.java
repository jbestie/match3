package org.jbestie.game.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import org.jbestie.game.Match3;
import org.jbestie.game.utils.GameConstants;


public class LevelSelectScreen implements Screen {
    private static final int LEVELS_COUNT = 7;
    private final Stage stage;
    private Texture background;


    LevelSelectScreen(final Match3 game) {
        background = new Texture(Gdx.files.local("sprites/level_select.png"));

        OrthographicCamera camera = new OrthographicCamera(Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight());
        camera.position.set(GameConstants.WORLD_WIDTH / 2, GameConstants.WORLD_HEIGHT / 2, 0);
        camera.update();

        stage = new Stage( new FitViewport(GameConstants.WORLD_WIDTH, GameConstants.WORLD_HEIGHT, camera)); //Set up a stage for the ui
        Gdx.input.setInputProcessor(stage);


        for (int i = 0; i < LEVELS_COUNT; i++) {
            float buttonWidth = GameConstants.WORLD_WIDTH / 8;

            Button btn = new Button(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.local("sprites/level_"+ (i + 1)+ ".png")))));
            float buttonHeight = 1.5f * buttonWidth;
            int xValue = i % 4;
            int yValue = i / 4;
            btn.setPosition(xValue * buttonWidth + xValue * buttonWidth + buttonWidth / 2, GameConstants.WINDOW_HEIGHT / 2 - yValue * buttonHeight - yValue * buttonHeight /4);
            btn.setSize(buttonWidth, buttonHeight);
            btn.addListener(new EventListener() {
                @Override
                public boolean handle(Event event) {
                    if (event instanceof InputEvent) { // dumb part of dumb code
                        if (((InputEvent)event).getType() == InputEvent.Type.touchDown) {
                            game.setScreen(new GameScreen(game));
                        }
                    }
                    return false;
                }
            });
            stage.addActor(btn);
        }

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        //level_select.png
        stage.act(Gdx.graphics.getDeltaTime()); //Perform ui logic
        stage.getBatch().begin();
        stage.getBatch().draw(background, 0, 0, GameConstants.WORLD_WIDTH, GameConstants.WORLD_HEIGHT);
        stage.getBatch().end();
        stage.draw(); //Draw the ui
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, false);
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
