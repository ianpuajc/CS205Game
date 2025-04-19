package io.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class GameInstructionsScreen implements Screen {
    private Main game;
    private Stage stage;
    private Skin skin;

    public GameInstructionsScreen(Main game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        // Load background art (place "menu_background.png" in your assets folder)
        Texture backgroundTexture = new Texture("instruction_bg.png");
        Image background = new Image(backgroundTexture);
        background.setFillParent(true);
        stage.addActor(background);

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        // Set up instructions text.
        Label instructionsLabel = new Label(
            "Objective:\n" +
                "- Submit as many finished processes as possible to the Output Register.\n\n" +
                "Controls:\n" +
                "- Bottom Left: Touchpad to move around and interact with IO devices.\n" +
                "- Bottom Center: Inventory to collect processes. Tap squares to select inventory spaces. Tap the square with 3 dots to open more inventory slots. Drag and drop to rearrange.\n" +
                "- Bottom Right: Interact Button.\n\n" +
                "Gameplay:\n" +
                "- Processes are created in the Instruction Register Overlay (top left).\n" +
                "- Use the Interact Button to place them into a selected inventory square.\n" +
                "- Interact with GPUs to progress a selected process, and retrieve it once done.\n" +
                "- Half-finished processes require interaction with the Hard Disk.\n" +
                "- Once a process is completely green, submit it to the Output Register to gain points.",
            skin
        );

        instructionsLabel.setColor(Color.valueOf("#000000"));


        // Button to go back to the menu.
        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new GameMenuScreen(game));
            }
        });


        table.add(instructionsLabel).pad(10);
        table.row();
        table.add(backButton).size(200, 80).pad(10);
    }

    @Override
    public void show() { }
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1);
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
    }
}
