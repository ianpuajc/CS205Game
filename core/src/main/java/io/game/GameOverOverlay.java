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

public class GameOverOverlay {
    private final Table root;
    private final Label scoreLabel;
    private final Stage stage;
    private final Main  game;


    GameOverOverlay(Stage stage, Main game) {
        this.stage = stage; this.game = game;
        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

        root = new Table(skin);
        root.setFillParent(true);
        root.setVisible(false);

        root.setBackground((Drawable) null);

        Label title = new Label("GAME OVER", skin);
        scoreLabel = new Label("Score: 0", skin);

        TextButton menu = new TextButton("Main Menu", skin);
        menu.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent e, Actor a) {
                game.setScreen(new GameMenuScreen(game));
            }
        });

        root.add(title).pad(20);
        root.row();
        root.add(scoreLabel).pad(10);
        root.row();
        root.add(menu).size(200,80).pad(20);

        stage.addActor(root);
    }

    void show(int score) {
        scoreLabel.setText("Score: " + score);
        root.setVisible(true);
    }


}
