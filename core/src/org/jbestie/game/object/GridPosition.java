package org.jbestie.game.object;

public class GridPosition {
    private int rowPosition;
    private int colPosition;

    public GridPosition(int rowPosition, int colPosition) {
        this.rowPosition = rowPosition;
        this.colPosition = colPosition;
    }

    public int getRowPosition() {
        return rowPosition;
    }

    public int getColPosition() {
        return colPosition;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GridPosition that = (GridPosition) o;

        if (rowPosition != that.rowPosition) return false;
        return colPosition == that.colPosition;
    }

    @Override
    public int hashCode() {
        int result = rowPosition;
        result = 31 * result + colPosition;
        return result;
    }

    @Override
    public String toString() {
        return "GridPosition{" +
                "rowPosition=" + rowPosition +
                ", colPosition=" + colPosition +
                '}';
    }
}
