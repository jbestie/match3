package org.jbestie.game.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import org.jbestie.game.Match3;
import org.jbestie.game.animation.Animation;
import org.jbestie.game.animation.AnimationGroup;
import org.jbestie.game.animation.MoveAnimation;
import org.jbestie.game.animation.ScaleAnimation;
import org.jbestie.game.enums.GameState;
import org.jbestie.game.enums.ItemType;
import org.jbestie.game.input.MatchThreeInputProcessor;
import org.jbestie.game.object.GameObject;
import org.jbestie.game.object.GridPosition;
import org.jbestie.game.utils.GameConstants;
import org.jbestie.game.utils.MatchUtils;

import java.util.*;

public class GameScreen implements Screen {
    private static final float ANIMATION_DURATION = 0.2f;
    private SpriteBatch batch;
    private Sprite background;
    private Sprite menuBackground;
    private List<Texture> textures;
    private GameObject[][] gameMap;
    private List<GameObject> selectedElements = new ArrayList<GameObject>();

    private Viewport viewport;
    private Random random = new Random();
    private BitmapFont font;
    private GlyphLayout fontLayout = new GlyphLayout();
    private int score = 0;

    private GameState gameState = GameState.IDLE;
    private final Match3 game;
    private List<Animation> animatedObjects = new ArrayList<Animation>();
    private Camera camera;
    private Stage stage;

    GameScreen(final Match3 game) {
        camera = new OrthographicCamera(Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight());
        camera.position.set(GameConstants.WORLD_WIDTH / 2, GameConstants.WORLD_HEIGHT / 2, 0);
        camera.update();
        viewport = new FitViewport(GameConstants.WORLD_WIDTH, GameConstants.WORLD_HEIGHT, camera);
        stage = new Stage(viewport, game.batch);

        stage.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                return false;
            }
        });

        batch = game.batch;
        this.game = game;
        background  = new Sprite( new Texture(Gdx.files.internal("background/bg.png")));
        background.setPosition(0.0f, 0.0f);
        menuBackground  = new Sprite( new Texture(Gdx.files.internal("background/blocking_panel_bg.png")));
        menuBackground.setPosition(0.0f, 0.0f);
        font  = new BitmapFont();

        Texture blueJelly = new Texture(Gdx.files.local("sprites/jelly_blue.png"));
        Texture greenJelly = new Texture(Gdx.files.local("sprites/jelly_green.png"));
        Texture greyJelly = new Texture(Gdx.files.local("sprites/jelly_grey.png"));
        Texture purpleJelly = new Texture(Gdx.files.local("sprites/jelly_purple.png"));
        Texture redJelly = new Texture(Gdx.files.local("sprites/jelly_red.png"));
        Texture yellowJelly = new Texture(Gdx.files.local("sprites/jelly_yellow.png"));
        textures = Arrays.asList(blueJelly, greenJelly, greyJelly, purpleJelly, redJelly, yellowJelly);

        gameMap = generateField();
        checkMatchesAndFillEmptyCells();

        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(stage);
        inputMultiplexer.addProcessor(new MatchThreeInputProcessor(this, selectedElements));

//        Gdx.input.setInputProcessor(new MatchThreeInputProcessor(this, selectedElements));

        final Button restart = createRestartButton(game);
        final Button levelSelect = createLevelRestartButton(game);
        stage.addActor(restart);
        stage.addActor(levelSelect);
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    private Button createLevelRestartButton(final Match3 game) {
        final Button levelSelect = new Button(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.local("sprites/choose_level.png")))));

        float buttonWidth = GameConstants.WINDOW_WIDTH / 8;
        float buttonHeight = GameConstants.WINDOW_WIDTH / 8;
        levelSelect.setPosition(GameConstants.WINDOW_WIDTH / 2 - buttonWidth, GameConstants.WINDOW_HEIGHT / 2 - buttonWidth );
        levelSelect.setSize(buttonWidth, buttonHeight);
        levelSelect.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                if (gameState != GameState.PAUSED) {
                    return false;
                }

                if (levelSelect.isPressed()) {
                    game.setScreen(new LevelSelectScreen(game));
                    return true;
                }
                return false;
            }
        });
        return levelSelect;
    }

    private Button createRestartButton(final Match3 game) {
        final Button restart = new Button(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.local("sprites/restart_button.png")))));

        float buttonWidth = GameConstants.WINDOW_WIDTH / 8;
        float buttonHeight = GameConstants.WINDOW_WIDTH / 8;
        restart.setPosition(GameConstants.WINDOW_WIDTH / 2 + buttonWidth / 2, GameConstants.WINDOW_HEIGHT / 2 - buttonWidth );
        restart.setSize(buttonWidth, buttonHeight);
        restart.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                if (gameState != GameState.PAUSED) {
                    return false;
                }

                if (restart.isPressed()) {
                    game.setScreen(new GameScreen(game));
                    return true;
                }
                return false;
            }
        });
        return restart;
    }


    @Override
    public void show() {

    }

    @Override
    public void render (float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //place draw logic here
        switch (gameState) {
            case IDLE:
                batch.begin();
                drawBackground();
                animateMatchedElementsBeforeRemoving();
                processSelectedElements();
                drawField(gameMap, batch);
                drawScore();
                batch.end();
                break;
            case PAUSED:
                drawMenuBackground();
                stage.draw();
                break;
            case MOVING_ITEMS:
                batch.begin();
                if (animationsFinished()) {
                    gameState = GameState.IDLE;
                    animatedObjects.clear();
                }
                drawBackground();
                animate();
                drawField(gameMap, batch);
                drawScore();
                batch.end();
                break;
            case REMOVING_MATCHES:
                batch.begin();
                if (animationsFinished()) {
                    checkMatchesAndFillEmptyCells();
                    gameState = GameState.IDLE;
                    animatedObjects.clear();
                }
                drawBackground();
                animate();
                drawField(gameMap, batch);
                drawScore();
                batch.end();
                break;
            case GAME_OVER:
                break;
            case LEVEL_END:
                break;
            default:
                throw new IllegalStateException("State " + gameState + " is unknown!");
        }


    }

    private void drawMenuBackground() {
        stage.getBatch().begin();
        stage.getBatch().draw(menuBackground, 0.0f, 0.0f, GameConstants.WINDOW_WIDTH, GameConstants.WINDOW_HEIGHT);
        stage.getBatch().end();
    }


    @Override
    public void dispose () {
        batch.dispose();
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


    private void animate() {
        for (Iterator<Animation> iterator = animatedObjects.iterator(); iterator.hasNext();) {
            Animation item = iterator.next();
            if (item.isFinished()) {
                iterator.remove();
            } else if (!item.isAnimating() && !item.isFinished()){
                item.start();
            } else {
                item.update(); // perform animation
            }
        }
    }


    private GameObject[][] generateField() {
        GameObject[][] result = new GameObject[GameConstants.VERTICAL_CELLS_COUNT][GameConstants.HORIZONTAL_CELLS_COUNT];

        float startXPosition = (GameConstants.WINDOW_WIDTH - GameConstants.ITEM_WIDTH * GameConstants.HORIZONTAL_CELLS_COUNT - (GameConstants.HORIZONTAL_CELLS_COUNT - 1) * GameConstants.CELL_SPACING_COEFFICIENT)/ 2;
        float startYPosition = GameConstants.ITEM_HEIGHT * GameConstants.VERTICAL_CELLS_COUNT + (GameConstants.VERTICAL_CELLS_COUNT - 1) * GameConstants.CELL_SPACING_COEFFICIENT ;

        for (int i = 0; i < GameConstants.VERTICAL_CELLS_COUNT ; i++) {
            for (int j = 0; j < GameConstants.HORIZONTAL_CELLS_COUNT; j++) {

                int index = random.nextInt(textures.size() - 1);
                GameObject sprite = new GameObject(textures.get(index), ItemType.values()[index]);
                sprite.setPosition(startXPosition + i * GameConstants.ITEM_WIDTH + GameConstants.CELL_SPACING_COEFFICIENT * i, startYPosition - j * GameConstants.ITEM_HEIGHT + GameConstants.CELL_SPACING_COEFFICIENT * j);
                sprite.setSize(GameConstants.ITEM_WIDTH, GameConstants.ITEM_HEIGHT);
                sprite.setGridPosition(new GridPosition(i, j));
                result[i][j] = sprite;
            }
        }

        return result;
    }

    private boolean animationsFinished() {
        boolean animationInProgress = false;

        for (Animation animation : animatedObjects) {
            animationInProgress |= animation.isFinished();
        }

        return  animationInProgress;
    }

    private void drawScore() {
        String text = String.format("Score: %s", score);
        fontLayout.setText(font, text);
        font.draw(batch, text, 20, fontLayout.height + 20);
    }


    private void processSelectedElements() {

        if (selectedElements.size() == 2) {
            GameObject firstElement = selectedElements.get(0);
            GameObject secondElement = selectedElements.get(1);
            if (areNeighbors(firstElement, secondElement)) {
                Vector2 position = new Vector2(firstElement.getX(), firstElement.getY());
                GridPosition gridPosition = firstElement.getGridPosition();

                Animation firstElementAnimation = new MoveAnimation(firstElement, secondElement.getX(), secondElement.getY(), ANIMATION_DURATION);
                MoveAnimation secondElementAnimation = new MoveAnimation(secondElement, position.x, position.y, ANIMATION_DURATION);

                AnimationGroup animationGroup = new AnimationGroup(ANIMATION_DURATION, firstElementAnimation, secondElementAnimation);
                animatedObjects.add(animationGroup);

                firstElement.setGridPosition(secondElement.getGridPosition());
                secondElement.setGridPosition(gridPosition);

                gameMap[firstElement.getGridPosition().getRowPosition()][firstElement.getGridPosition().getColPosition()] = firstElement;
                gameMap[secondElement.getGridPosition().getRowPosition()][secondElement.getGridPosition().getColPosition()] = secondElement;

                gameState = GameState.MOVING_ITEMS;
            }
            firstElement.setAlpha(1.0f);
            secondElement.setAlpha(1.0f);
            selectedElements.clear();
        }
    }

    private void animateMatchedElementsBeforeRemoving() {
        Set<GameObject> matchedItems = MatchUtils.getMatchesOnGameMap(gameMap);
        if (matchedItems.size() != 0 && gameState.equals(GameState.IDLE)) {
            gameState = GameState.REMOVING_MATCHES;
            AnimationGroup group = new AnimationGroup(ANIMATION_DURATION);
            for (GameObject object : matchedItems) {
                group.add(new ScaleAnimation(object, ANIMATION_DURATION, 0.1f, 0.1f));
            }

            animatedObjects.add(group);
        }
    }

    private void drawBackground() {
        background.draw(batch);
    }

    private void checkMatchesAndFillEmptyCells() {
        Set<GameObject> matchedItems;
        do {
            matchedItems = MatchUtils.getMatchesOnGameMap(gameMap);
            score += matchedItems.size() * GameConstants.SCORE_PER_ITEM;
            for (GameObject object : matchedItems) {
                int index = random.nextInt(textures.size() - 1);
                GameObject sprite = new GameObject(textures.get(index), ItemType.values()[index]);
                sprite.setPosition(object.getX(), object.getY());
                sprite.setSize(GameConstants.ITEM_WIDTH, GameConstants.ITEM_HEIGHT);
                sprite.setGridPosition(object.getGridPosition());
                gameMap[object.getGridPosition().getRowPosition()][object.getGridPosition().getColPosition()] = sprite;
            }
        } while (matchedItems.size() > 0);
    }

    private boolean areNeighbors(GameObject firstElement, GameObject secondElement) {
        GridPosition position = firstElement.getGridPosition();
        GridPosition gridPosition = secondElement.getGridPosition();

        return ((position.getColPosition() == gridPosition.getColPosition()) || (position.getRowPosition() == gridPosition.getRowPosition())) &&
                (Math.abs(position.getColPosition() - gridPosition.getColPosition()) <= 1)
                && (Math.abs(position.getRowPosition() - gridPosition.getRowPosition()) <= 1);
    }

    private void drawField(GameObject[][] map, SpriteBatch batch) {
        for (int i = 0; i < GameConstants.VERTICAL_CELLS_COUNT ; i++) {
            for (int j = 0; j < GameConstants.HORIZONTAL_CELLS_COUNT; j++) {
                if (map[i][j] != null) {
                    map[i][j].draw(batch);
                }
            }
        }
    }

    public Camera getCamera() {
        return camera;
    }

    public GameObject[][] getGameMap() {
        return gameMap;
    }

    public void setState(GameState state) {
        this.gameState = state;
    }

    public GameState getState() {
        return gameState;
    }
}
