package io.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.Game;

public class Main extends Game  {
    public SpriteBatch batch;
    public OrthographicCamera camera;
    private Stage stage;
    private GameLevel gameLevel;
    private GameRenderer renderer;
    private InputHandler inputHandler;
    private Inventory inventory;
    private HotbarUI hotbarUI;
    private InventoryUI inventoryUI;
    private InstructionRegisterUI irUI;
    public LeaderboardManager leaderboard;

    public void create() { // called on initial running
        batch = new SpriteBatch();
        leaderboard = new LeaderboardManager();
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f, 0);
        camera.update();

        setScreen(new GameMenuScreen(this));
    }

    @Override
    public void dispose() {
        batch.dispose();
        getScreen().dispose();
    }

    /*

    @Override
    public void create() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f, 0);
        camera.update();
        inventory = new Inventory(12);

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
    }*/




}
