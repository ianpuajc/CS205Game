package io.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
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
    private InstructionRegisterUI irUI;
    private Inventory inventory;

    public GameLevelScreen(Main game, int levelNumber) {
        this.game = game;
        this.levelNumber = levelNumber;
        stage = new Stage(new ScreenViewport());
        // Load the level based on levelNumber.
        gameLevel = LevelLoader.loadLevel(levelNumber);

        // 1. Create inventory to hold process items
        inventory = new Inventory(12);

        // 2. Create and attach IR UI with textures
        irUI = new InstructionRegisterUI(
            gameLevel.getInstructionRegister(),
            inventory,
            new Texture("Overclocked Assets/Data Packets/Single_Undone.png"),
            new Texture("Overclocked Assets/Data Packets/Single_Done.png"),
            new Texture("Overclocked Assets/Data Packets/Double_Undone.png"),
            new Texture("Overclocked Assets/Data Packets/Double_HalfA.png"),
            new Texture("Overclocked Assets/Data Packets/Double_HalfB.png"),
            new Texture("Overclocked Assets/Data Packets/Double_Done.png")
        );

        // 3. Add it to the stage
        stage.addActor(irUI);
        irUI.toFront();
        irUI.setTouchable(Touchable.enabled);

        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
        this.exitButton = new ExitButton(skin, game);
        stage.addActor(exitButton);

        renderer = new GameRenderer(game.batch, game.camera, gameLevel, stage);
        inputHandler = new InputHandler(gameLevel, stage, inventory);
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
        irUI.update();
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
