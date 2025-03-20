package io.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Stage stage;
    private GameLevel gameLevel;
    private GameRenderer renderer;
    private InputHandler inputHandler;

    @Override
    public void create() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f, 0);
        camera.update();

        stage = new Stage(new ScreenViewport());
        loadLevel(LevelLoader.getCurrentLevel());
    }

    private void loadLevel(int level) {
        if (gameLevel != null) gameLevel.dispose();
        gameLevel = LevelLoader.loadLevel(level);
        renderer = new GameRenderer(batch, camera, gameLevel, stage);
        inputHandler = new InputHandler(gameLevel, stage);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render() {
        float deltaTime = Gdx.graphics.getDeltaTime();
        gameLevel.update(deltaTime);
        inputHandler.update(deltaTime);
        renderer.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
        gameLevel.dispose();
        stage.dispose();
    }
}
