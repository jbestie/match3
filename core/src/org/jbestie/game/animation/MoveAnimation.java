package org.jbestie.game.animation;

import org.jbestie.game.object.GameObject;

public class MoveAnimation extends Animation {
    protected float distanceX;
    protected float distanceY;
    protected float toX;
    protected float toY;

    public MoveAnimation(GameObject gameObject, float toX, float toY, float duration) {
        super(gameObject, duration);
        distanceX = toX - gameObject.getX();
        distanceY = toY - gameObject.getY();
        this.toX = toX;
        this.toY = toY;
    }

    @Override
    protected void finishAnimation() {
        gameObject.setX(toX);
        gameObject.setY(toY);
    }

    @Override
    protected void performAction(float deltaTime) {
        gameObject.setX(gameObject.getX() + distanceX * deltaTime / duration);
        gameObject.setY(gameObject.getY() + distanceY * deltaTime / duration);
    }
}
