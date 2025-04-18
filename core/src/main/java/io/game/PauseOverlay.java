package io.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class PauseOverlay {
    private final Table root;
    private final GameLevelScreen level;

    PauseOverlay(Stage stage, GameLevelScreen level) {
        this.level = level;
        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

        root = new Table(skin);
        root.setFillParent(true);
        root.setVisible(false);

        root.setBackground((Drawable) null);

        Label paused = new Label("PAUSED", skin);

        TextButton resume = new TextButton("Resume", skin);
        resume.addListener(new ChangeListener() {
            @Override public void changed(ChangeListener.ChangeEvent e, Actor a) {
                level.resumeGame();
            }
        });

        TextButton menu = new TextButton("Main Menu", skin);
        menu.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                level.game.setScreen(new GameMenuScreen(level.game));
            }
        });

        root.add(paused).pad(20).row();
        root.add(resume).size(200,80).pad(10).row();
        root.add(menu)  .size(200,80).pad(10);

        stage.addActor(root);
    }


    void show() { root.setVisible(true); }
    void hide() { root.setVisible(false); }
}
