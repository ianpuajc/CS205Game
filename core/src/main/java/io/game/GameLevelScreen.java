package io.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class GameLevelScreen implements Screen {
    private Main game;
    private int levelNumber;
    private GameLevel gameLevel;
    private GameRenderer renderer;
    private InputHandler inputHandler;
    private Stage stage;
    private final ExitButton exitButton;

    public GameLevelScreen(Main game, int levelNumber) {
        this.game = game;
        this.levelNumber = levelNumber;
        stage = new Stage(new ScreenViewport());
        // Load the level based on levelNumber.
        gameLevel = LevelLoader.loadLevel(levelNumber);

        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
        this.exitButton = new ExitButton(skin, game);
        stage.addActor(exitButton);

        renderer = new GameRenderer(game.batch, game.camera, gameLevel, stage);
        inputHandler = new InputHandler(gameLevel, stage);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() { }

    @Override
    public void render(float delta) {
        // Update game logic and render the level.
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        gameLevel.update(delta);
        inputHandler.update(delta);
        renderer.render();
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        exitButton.updatePosition(width, height);
    }

    @Override public void pause() { }
    @Override public void resume() { }
    @Override public void hide() { }

    @Override
    public void dispose() {
        stage.dispose();
        gameLevel.dispose();
    }
}
