package io.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import java.util.ArrayList;

public class LeaderboardScreen implements Screen {
    private Main game;
    private Stage stage;
    private LeaderboardManager leaderboardManager;
    private Table table;
    private Skin skin;

    // Accept a reference to your main game so that we can go back to the menu.
    public LeaderboardScreen(Main game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("uiskin.json"));
        leaderboardManager = new LeaderboardManager();

        // Load background art (place "menu_background.png" in your assets folder)
        Texture backgroundTexture = new Texture("leaderboard_bg.png");
        Image background = new Image(backgroundTexture);
        background.setFillParent(true);
        stage.addActor(background);

        // Create and setup the table that will hold our leaderboard entries and buttons.
        table = new Table();
        table.setFillParent(true);
        table.center();
        stage.addActor(table);

        // Populate the table with the current leaderboard and our buttons.
        populateTable();
    }

    // This method clears and repopulates the table.
    private void populateTable() {
        table.clear();

        // Get current top three scores.
        ArrayList<Integer> scores = leaderboardManager.getScores();

        if (scores.isEmpty()) {
            Label noScoresLabel = new Label("No scores yet", new Label.LabelStyle(new BitmapFont(), Color.BLACK));
            table.add(noScoresLabel).pad(10);
            table.row();
        } else {
            for (int i = 0; i < scores.size(); i++) {
                Label scoreLabel = new Label("Rank " + (i + 1) + ": " + scores.get(i), new Label.LabelStyle(new BitmapFont(), Color.BLACK));
                table.add(scoreLabel).pad(10);
                table.row();
            }
        }

        // Add a row for the buttons.
        // Create the "Back" button.
        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new GameMenuScreen(game));
            }
        });

        // Create the "Reset Leaderboard" button.
        TextButton resetButton = new TextButton("Reset Leaderboard", skin);
        resetButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                leaderboardManager.resetLeaderboard();
                // Refresh the table after clearing the leaderboard.
                populateTable();
            }
        });

        // Add the buttons in the same row with some space.
        table.row().padTop(20);
        table.add(backButton).size(200, 80).pad(10);
        table.add(resetButton).size(200, 80).pad(10);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

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

    @Override
    public void pause() { }

    @Override
    public void resume() { }

    @Override
    public void hide() { }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
