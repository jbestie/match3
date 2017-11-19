package org.jbestie.game.animation;

import org.jbestie.game.object.GameObject;

public class ScaleAnimation extends Animation {
    private float scaleX;
    private float scaleY;

    public ScaleAnimation(GameObject gameObject, float duration, float scaleX, float scaleY) {
        super(gameObject, duration);
        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }

    @Override
    protected void finishAnimation() {
        gameObject.setScale(scaleX, scaleY);
    }

    @Override
    protected void performAction(float deltaTime) {
        float tmpScaleX = scaleX * deltaTime/duration;
        float tmpScaleY = scaleY * deltaTime/duration;
        gameObject.setScale(tmpScaleX, tmpScaleY);
    }
}
