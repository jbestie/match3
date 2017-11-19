package org.jbestie.game.animation;

import org.jbestie.game.object.GameObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AnimationSequence extends Animation {
    List<Animation> sequence = new ArrayList<Animation>();

    public AnimationSequence(GameObject gameObject, float duration, Animation ... animations) {
        super(gameObject, duration);
        for (Animation anim : animations) {
            sequence.add(anim);
        }
    }


    @Override
    protected void finishAnimation() {
        // nothing yet;
    }

    @Override
    protected void performAction(float deltaTime) {
        for (Iterator<Animation> iterator = sequence.iterator(); iterator.hasNext();) {
            Animation animation = iterator.next();

            if (animation.isFinished()) {
                continue;
            }

            if (!animation.isAnimating()) {
                animation.start();
            }

            animation.update();

        }

        animating = false;
    }
}
