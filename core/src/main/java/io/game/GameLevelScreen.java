package io.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class GameLevelScreen implements Screen {
    private Main game;
    private int levelNumber;
    private GameLevel gameLevel;
    private GameRenderer renderer;
    private InputHandler inputHandler;
    private Stage stage;

    public GameLevelScreen(Main game, int levelNumber) {
        this.game = game;
        this.levelNumber = levelNumber;
        stage = new Stage(new ScreenViewport());
        // Load the level based on levelNumber.
        gameLevel = LevelLoader.loadLevel(levelNumber);

        renderer = new GameRenderer(game.batch, game.camera, gameLevel, stage);
        inputHandler = new InputHandler(gameLevel, stage);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() { }

    @Override
    public void render(float delta) {
        // Update game logic and render the level.
        gameLevel.update(delta);
        inputHandler.update(delta);
        renderer.render();
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
        gameLevel.dispose();
    }
}
