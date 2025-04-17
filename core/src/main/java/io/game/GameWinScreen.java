package io.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class GameWinScreen implements Screen {
    private Main game;
    private Stage stage;
    private Skin skin;
    private Texture backgroundTexture;
    private int nextLevel;

    public GameWinScreen(Main game, int currentLevel) {
        this.game = game;
        this.nextLevel = currentLevel + 1; // Calculate next level
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Load UI assets
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        backgroundTexture = new Texture("win_background.png"); // Create this asset
        Image background = new Image(backgroundTexture);
        background.setFillParent(true);
        stage.addActor(background);

        // Layout
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        // Buttons
        //TextButton nextLevelButton = new TextButton("Next Level", skin);
        TextButton menuButton = new TextButton("Back to Menu", skin);

        // Button listeners
//        nextLevelButton.addListener(new ChangeListener() {
//            @Override
//            public void changed(ChangeEvent event, Actor actor) {
//                if (LevelLoader.levelExists(nextLevel)) {
//                    game.setScreen(new GameLevelScreen(game, nextLevel));
//                } else {
//                    game.setScreen(new GameMenuScreen(game)); // No more levels
//                }
//            }
//        });

        menuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new GameMenuScreen(game));
            }
        });

        // Add buttons with padding
        //table.add(nextLevelButton).pad(20).row();
        table.add(menuButton).pad(20);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    // Other required Screen methods...
    @Override public void show() {}
    @Override public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {
        stage.dispose();
        skin.dispose();
        backgroundTexture.dispose();
    }
}
