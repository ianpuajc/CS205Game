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

public class GameMenuScreen implements Screen {
    private Main game;
    private Stage stage;
    private Skin skin;
    private Texture backgroundTexture;

    public GameMenuScreen(Main game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Load a skin for your UI (make sure "uiskin.json" is in your assets)
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        // Load background art (place "menu_background.png" in your assets folder)
        backgroundTexture = new Texture("menu_bg2.png");
        Image background = new Image(backgroundTexture);
        background.setFillParent(true);
        stage.addActor(background);

        // Create a table for layout.
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        // Create level buttons.
        TextButton level1Button = new TextButton("Play Game", skin);
        TextButton instructionsButton = new TextButton("Instructions", skin);
        TextButton leaderboardButton = new TextButton("Leaderboard", skin); // New button

        // Add listener for playing the game.
        level1Button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new GameLevelScreen(game, 1));
            }
        });

        // Listener for instructions.
        instructionsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new GameInstructionsScreen(game));
            }
        });

        // Listener for leaderboard.
        leaderboardButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new LeaderboardScreen(game));
            }
        });

        // Add buttons to the table with some spacing.
        table.add(level1Button).size(400, 150).pad(10);
        table.row();
        table.add(instructionsButton).size(400, 150).pad(10);
        table.row();
        table.add(leaderboardButton).size(400, 150).pad(10);
    }

    @Override
    public void show() { }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override public void pause() { }
    @Override public void resume() { }
    @Override public void hide() { }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        backgroundTexture.dispose();
    }
}
