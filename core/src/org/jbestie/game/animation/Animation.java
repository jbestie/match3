package org.jbestie.game.animation;

import com.badlogic.gdx.Gdx;
import org.jbestie.game.object.GameObject;

public abstract class Animation {
    protected boolean animating = false;
    protected GameObject gameObject;
    protected float duration;
    protected float time = 0.0f;
    protected boolean finished = false;

    public Animation(GameObject gameObject, float duration) {
        this.gameObject = gameObject;
        this.duration = duration;
    }

    public void start() {
        animating = true;
        time = 0.0f;
        finished = false;
    }

    public void update() {
        time += Gdx.graphics.getDeltaTime();
        if (animating) {
            performAction(Gdx.graphics.getDeltaTime());
        }

        if (time >= duration) {
            animating = false;
            finished = true;
            finishAnimation();
        }
    }

    public boolean isFinished() {
        return finished;
    }

    protected abstract void finishAnimation();

    public boolean isAnimating() {
        return animating;
    }

    protected abstract void performAction(float deltaTime);
}
