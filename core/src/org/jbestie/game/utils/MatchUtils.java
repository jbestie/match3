package org.jbestie.game.utils;

import org.jbestie.game.enums.ItemType;
import org.jbestie.game.object.GameObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public final class MatchUtils {
    private MatchUtils() {
    }


    public static Set<GameObject>  getMatchesOnGameMap(GameObject[][] gameMap) {
        //List<List<GameObject>> result = new List<List<GameObject>>();

        Set<GameObject> result = new TreeSet<GameObject>();

        for (int row = 0; row < GameConstants.VERTICAL_CELLS_COUNT; row++) {
            for (int col = 0; col < GameConstants.HORIZONTAL_CELLS_COUNT; col++) {
                List<GameObject> horizontalMatches = getHorizontalMatches(gameMap, row, col);
                if (horizontalMatches.size() >= GameConstants.MIN_COUNT_TO_MATCH) {
                    result.addAll(horizontalMatches);
                }

                List<GameObject> verticalMatches = getVerticalMatches(gameMap, row, col);
                if (verticalMatches.size() >= GameConstants.MIN_COUNT_TO_MATCH) {
                    result.addAll(verticalMatches);
                }
            }
        }

        return result;
    }


    private static List<GameObject> getHorizontalMatches(GameObject[][] gameMap, int startRow, int startCol) {
        List<GameObject> result = new ArrayList<GameObject>();
        if (gameMap[startRow][startCol] == null) {
            return result;
        }

        result.add(gameMap[startRow][startCol]);
        ItemType cellType = gameMap[startRow][startCol].getType();

        for (int col = 1; col + startCol < GameConstants.HORIZONTAL_CELLS_COUNT; col++) {
            if (gameMap[startRow][startCol + col] == null) {
                return result;
            }

            if (cellType.equals(gameMap[startRow][startCol + col].getType())) {
                result.add(gameMap[startRow][startCol + col]);
            } else {
                return result;
            }

        }

        return result;
    }


    private static List<GameObject> getVerticalMatches(GameObject[][] gameMap, int startRow, int startCol) {
        List<GameObject> result = new ArrayList<GameObject>();
        if (gameMap[startRow][startCol] == null) {
            return result;
        }

        result.add(gameMap[startRow][startCol]);

        ItemType type = gameMap[startRow][startCol].getType();

        for (int row = 1; startRow + row < GameConstants.VERTICAL_CELLS_COUNT; row++) {
            if (gameMap[startRow + row][startCol] == null) {
                return result;
            }

            if (type.equals(gameMap[startRow + row][startCol].getType())) {
                result.add(gameMap[startRow + row][startCol]);
            } else {
                return result;
            }

        }

        return result;
    }
}
