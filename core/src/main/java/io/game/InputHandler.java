package io.game;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.math.Vector2;

public class InputHandler {
    private Player player;
    private Touchpad touchpad;
    private GameLevel gameLevel;

    public InputHandler(GameLevel gameLevel, Stage stage) {
        this.gameLevel = gameLevel;
        this.player = gameLevel.getPlayer();
        this.touchpad = TouchpadController.createTouchpad();
        stage.addActor(touchpad);
    }

    public void update(float deltaTime) {
        float knobPercentX = touchpad.getKnobPercentX();
        float knobPercentY = touchpad.getKnobPercentY();

        // Calculate movement based on touchpad input
        float moveX = knobPercentX * player.getSpeed() * deltaTime;
        float moveY = knobPercentY * player.getSpeed() * deltaTime;

        // Update player's position and animation state
        if (moveX != 0 || moveY != 0) {
            Vector2 direction = new Vector2(knobPercentX, knobPercentY).nor();
            player.move(moveX, moveY, gameLevel.getObstacles());
            player.updateAnimationState(direction);
        } else {
            player.setIdleAnimation();
        }
    }
}
