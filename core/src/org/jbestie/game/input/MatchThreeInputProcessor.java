package org.jbestie.game.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import org.jbestie.game.enums.GameState;
import org.jbestie.game.object.GameObject;
import org.jbestie.game.stage.GameScreen;
import org.jbestie.game.stage.LevelSelectScreen;
import org.jbestie.game.utils.GameConstants;

import java.util.List;

public class MatchThreeInputProcessor extends InputAdapter {
    private Camera camera;
    private GameObject[][] gameMap;
    private List<GameObject> selectedElements;
    private GameScreen screen;

    public MatchThreeInputProcessor(GameScreen screen, List<GameObject> selectedElements) {
        this.screen = screen;
        this.camera = screen.getCamera();
        this.gameMap = screen.getGameMap();
        this.selectedElements = selectedElements;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (screen.getState() == GameState.IDLE){
            return processMatchEvent(screenX, screenY);
        } else if (screen.getState() == GameState.PAUSED) {
            return processPausedEvents(screenX, screenY);
        }
        return true;
    }

    private boolean processPausedEvents(int screenX, int screenY) {
        Vector3 tempVec = new Vector3();
        camera.unproject(tempVec.set(screenX, screenY, 0));

        int x = (int) tempVec.x;
        int y = (int) tempVec.y;

        GameObject chooseLevelButton = screen.getChooseLevelButton();
        GameObject restartButton = screen.getRestartButton();
        int levelSelectOX = Math.round(chooseLevelButton.getX());
        int levelSelectOY = Math.round(chooseLevelButton.getY());
        int restartOX = Math.round(restartButton.getX());
        int restartOY = Math.round(restartButton.getY());
        if (matchesRange(chooseLevelButton, x, y, levelSelectOX, levelSelectOY)) {
            screen.getGame().setScreen(new LevelSelectScreen(screen.getGame()));
        } else if (matchesRange(restartButton, x, y, restartOX, restartOY)){
            screen.getGame().setScreen(new GameScreen(screen.getGame()));
        }

        return true;
    }

    private boolean processMatchEvent(int screenX, int screenY) {
        if (selectedElements.size() == 2) {
            return true;
        }

        Vector3 tempVec = new Vector3();
        camera.unproject(tempVec.set(screenX, screenY, 0));

        int x = (int) tempVec.x;
        int y = (int) tempVec.y;

        for (int i = 0; i < GameConstants.VERTICAL_CELLS_COUNT; i++) {
            for (int j = 0; j < GameConstants.HORIZONTAL_CELLS_COUNT; j++) {
                GameObject object = gameMap[i][j];
                int ox = Math.round(object.getX());
                int oy = Math.round(object.getY());
                if (matchesRange(object, x, y, ox, oy)) {
                    if (!selectedElements.contains(object)) {
                        selectedElements.add(object);
                        object.setAlpha(0.6f);
                    } else {
                        object.setAlpha(1.0f);
                        selectedElements.remove(object);
                    }
                    break;
                }
            }

        }
        return false;
    }

    private boolean matchesRange( GameObject object, int x, int y, int ox, int oy) {
        return (x > ox && x <= (ox + object.getWidth()))
                && (y > oy && y <= (oy + object.getHeight()));
    }


    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.ESCAPE:
                if (screen.getState().equals(GameState.IDLE)) {
                    screen.setState(GameState.PAUSED);
                } else {
                    screen.setState(GameState.IDLE);
                }
                break;
            default:
                break;
        }

        return true;
    }
}
