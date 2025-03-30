package io.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class SimpleScreen implements Screen {
    private Stage stage;
    private Label label;
    private Skin skin;

    public SimpleScreen() {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        // Load the skin (make sure to have the correct path for your skin files)
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        // Create a label with a simple message
        label = new Label("Hello, World!", new Label.LabelStyle(skin.getFont("default-font"), null));
        label.setPosition(Gdx.graphics.getWidth() / 2f - label.getWidth() / 2f, Gdx.graphics.getHeight() / 2f - label.getHeight() / 2f);

        // Add the label to the stage
        stage.addActor(label);
    }

    @Override
    public void show() {
        // Called when this screen is set as the current screen
    }

    @Override
    public void render(float delta) {
        // Clear the screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Draw the stage (this will render the label)
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        // Called when the screen is resized
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void hide() {
        // Called when this screen is no longer the current screen
    }

    @Override
    public void pause() {
        // Called when the game is paused
    }

    @Override
    public void resume() {
        // Called when the game is resumed
    }

    @Override
    public void dispose() {
        // Clean up resources when this screen is disposed of
        stage.dispose();
        skin.dispose();
    }
}
