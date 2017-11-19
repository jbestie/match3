package org.jbestie.game.animation;

import org.jbestie.game.object.GameObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AnimationGroup extends Animation {
    private List<Animation> animationList = new ArrayList<Animation>();

    public AnimationGroup(float duration, Animation ... animations) {
        super(null, duration);
        this.animationList.addAll(Arrays.asList(animations));
    }

    @Override
    protected void finishAnimation() {
        for (Animation animation : animationList) {
            animation.finishAnimation();
        }
    }

    @Override
    protected void performAction(float deltaTime) {
        for (Animation animation : animationList) {
            if (animation.isFinished()) {
                continue;
            }

            if (!animation.isAnimating() && !animation.isFinished()) {
                animation.start();
            }

            animation.update();
        }
    }
}
