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
    Main game;
    private int levelNumber;
    private GameLevel gameLevel;
    private GameRenderer renderer;
    private InputHandler inputHandler;
    private Stage stage;
    private final ExitButton exitButton;
    private InstructionRegisterUI irUI;
    private Inventory inventory;

    private float timeLeft = 120f;        // 120‑second timer
    private boolean paused   = false;
    private boolean gameOver = false;

    private GameOverOverlay gameOverUI;  // shown when the timer hits 0
    private PauseOverlay    pauseUI;     // shown when “X” is pressed

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
            new Texture("Overclocked Assets/Data Packets/Single_Undone.PNG"),
            new Texture("Overclocked Assets/Data Packets/Single_Done.PNG"),
            new Texture("Overclocked Assets/Data Packets/Double_Undone.PNG"),
            new Texture("Overclocked Assets/Data Packets/Double_HalfA.PNG"),
            new Texture("Overclocked Assets/Data Packets/Double_HalfB.PNG"),
            new Texture("Overclocked Assets/Data Packets/Double_Done.PNG")
        );

        // 3. Add it to the stage
        stage.addActor(irUI);
        irUI.toFront();
        irUI.setTouchable(Touchable.enabled);
        irUI.setVisible(true);   // Make sure it's not hidden
        irUI.pack();             // Force layout sizing
        irUI.invalidateHierarchy(); // Recalculate children bounds/layouts

        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
        this.exitButton = new ExitButton(skin, this);
        stage.addActor(exitButton);

        renderer = new GameRenderer(game.batch, game.camera, gameLevel, stage);
        inputHandler = new InputHandler(gameLevel, stage, inventory);

        Gdx.input.setInputProcessor(stage);
        stage.setDebugAll(true);
        gameOverUI = new GameOverOverlay(stage, game, gameLevel);   // create overlay actors but hidden
        pauseUI    = new PauseOverlay(   stage, this);
    }

    public void exitLevel() {
        if (gameOver) return;
        gameOver = true;

        int finalScore = 0;
        for (Obstacle o : gameLevel.getObstacles())
            if (o instanceof OutputRegister){
                finalScore = ((OutputRegister)o).getScore();
                break;
            }

        game.leaderboard.addScore(finalScore);
        gameOverUI.show(finalScore);     // overlay with “GAME OVER”
    }

    @Override
    public void show() { }

    @Override
    public void render(float delta) {
        // Clear the screen – original functionality
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Game logic update only if not paused or over – from suggested version
        if (!paused && !gameOver) {
            timeLeft -= delta;
            if (timeLeft <= 0) {
                timeLeft = 0;
                exitLevel();
            }

            gameLevel.update(delta);
            inputHandler.update(delta);
            irUI.update();
        }

        // Rendering – combine new timeLeft logic with original renderer call
        renderer.render(timeLeft); // Assume renderer can handle timeLeft (overloaded method)

        // Stage updates and draws regardless – same as both versions
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
        renderer.dispose(); // Dispose the HUD font too

    }

    public void pauseGame()   { paused = true;  pauseUI.show(); }
    public void resumeGame()  { paused = false; pauseUI.hide(); }
}
