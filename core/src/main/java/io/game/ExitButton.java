package io.game;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ExitButton extends TextButton {

    public ExitButton(Skin skin, final GameLevelScreen game) {
        super("X", skin);
        setupButton(game);
    }

    private void setupButton(final GameLevelScreen game) {
        this.setSize(50, 50);

        this.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.exitLevel();          // <── call the helper on the screen
            }
        });
    }

    public void updatePosition(float screenWidth, float screenHeight) {
        this.setPosition(screenWidth - 60, screenHeight - 60);
    }
}
