package org.jbestie.game.animation;

import org.jbestie.game.object.GameObject;

public class ScaleAnimation extends Animation {
    private float scaleX;
    private float scaleY;

    public ScaleAnimation(GameObject gameObject, float duration, float scaleX, float scaleY) {
        super(gameObject, duration);
        this.scaleX = scaleX - gameObject.getScaleX();
        this.scaleY = scaleY - gameObject.getScaleY();
    }


    @Override
    public void start() {
        super.start();
        gameObject.setOriginCenter();
    }

    @Override
    protected void finishAnimation() {
        gameObject.setScale(scaleX, scaleY);
    }

    @Override
    protected void performAction(float deltaTime) {
        float tmpScaleX = gameObject.getScaleX() + scaleX * deltaTime/duration;
        float tmpScaleY = gameObject.getScaleY() + scaleY * deltaTime/duration;
        gameObject.setScale(tmpScaleX, tmpScaleY);
    }
}
