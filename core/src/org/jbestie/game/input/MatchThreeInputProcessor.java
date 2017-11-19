package org.jbestie.game.input;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import org.jbestie.game.object.GameObject;
import org.jbestie.game.utils.GameConstants;

import java.util.List;

public class MatchThreeInputProcessor extends InputAdapter {
    private Camera camera;
    private GameObject[][] gameMap;
    private List<GameObject> selectedElements;

    public MatchThreeInputProcessor(Camera camera, GameObject[][] gameMap, List<GameObject> selectedElements) {
        this.camera = camera;
        this.gameMap = gameMap;
        this.selectedElements = selectedElements;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
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
                if ((x > ox && x <= (ox + object.getWidth()))
                        && (y > oy && y <= (oy + object.getHeight()))) {
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
        return true;
    }
}
