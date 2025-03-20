package io.game;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;

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
        float moveX = touchpad.getKnobPercentX() * player.speed * deltaTime;
        float moveY = touchpad.getKnobPercentY() * player.speed * deltaTime;
        player.move(moveX, moveY, gameLevel.getObstacles());
    }
}
