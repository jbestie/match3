package org.jbestie.game.object;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import org.jbestie.game.enums.ItemType;

public class GameObject extends Sprite implements Comparable{
    private ItemType type;
    private GridPosition gridPosition;

    public GameObject(Texture texture, ItemType type) {
        super(texture);
        this.type = type;
    }

    public GridPosition getGridPosition() {
        return gridPosition;
    }

    public void setGridPosition(GridPosition gridPosition) {
        this.gridPosition = gridPosition;
    }

    public ItemType getType() {
        return type;
    }

    public boolean isNeighbour(GameObject potentialNeighbour) {
        GridPosition gridPosition = potentialNeighbour.getGridPosition();

        boolean neighbour = ((this.gridPosition.getColPosition() == gridPosition.getColPosition())
                || (this.gridPosition.getRowPosition() == gridPosition.getRowPosition())) &&
                (Math.abs(this.gridPosition.getColPosition() - gridPosition.getColPosition()) <= 1)
                && (Math.abs(this.gridPosition.getRowPosition() - gridPosition.getRowPosition()) <= 1);

        return neighbour;
    }

    @Override
    public int compareTo(Object o) {
        if (equals(o)) {
            return 0;
        } else {
            return -1;
        }

    }
}
