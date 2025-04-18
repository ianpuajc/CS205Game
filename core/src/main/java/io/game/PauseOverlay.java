package io.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;

public class PauseOverlay {
    private final Table root;
    private final GameLevelScreen level;

    PauseOverlay(Stage stage, GameLevelScreen level) {
        this.level = level;
        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

        root = new Table(skin);
        root.setFillParent(true);
        root.setVisible(false);
        root.align(Align.center);

        Pixmap black = new Pixmap(1, 1, Format.RGBA8888);
        black.setColor(0, 0, 0, 0.8f); // Solid black with alpha
        black.fill();
        TextureRegionDrawable solidBlack = new TextureRegionDrawable(new TextureRegion(new Texture(black)));
        root.setBackground(solidBlack);

        // Container for actual menu content
        Table menu = new Table(skin);
        menu.align(Align.center);

        Label paused = new Label("PAUSED", skin);
        paused.setFontScale(2f);
        paused.setColor(Color.WHITE); // Ensures full brightness

        TextButton resume = new TextButton("Resume", skin);
        TextButton menuBtn = new TextButton("Main Menu", skin);

        resume.getLabel().setColor(Color.WHITE);
        menuBtn.getLabel().setColor(Color.WHITE);

        resume.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                level.resumeGame();
            }
        });

        menuBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                level.game.setScreen(new GameMenuScreen(level.game));
            }
        });

        menu.add(paused).padBottom(30).row();
        menu.add(resume).width(250).height(60).padBottom(20).row();
        menu.add(menuBtn).width(250).height(60);

        root.add(menu);
        stage.addActor(root);
    }

    void show() { root.setVisible(true); }
    void hide() { root.setVisible(false); }
}
