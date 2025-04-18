package io.game;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ExitButton extends TextButton {

    public ExitButton(Skin skin, final GameLevelScreen level) {
        super("⏸", skin);          // pause icon / text
        setSize(50, 50);
        addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                level.pauseGame();  // show pause overlay
            }
        });
    }

    public void updatePosition(float screenWidth, float screenHeight) {
        this.setPosition(screenWidth - 60, screenHeight - 60);
    }
}
