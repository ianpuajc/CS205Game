package io.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;


public class GameRenderer {
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private GameLevel gameLevel;
    private Stage stage;

    private Player player;

    private BitmapFont scoreFont;
    private GlyphLayout layout;
    private BitmapFont timerFont = new BitmapFont();

    public GameRenderer(SpriteBatch batch, OrthographicCamera camera, GameLevel gameLevel, Stage stage) {
        this.batch = batch;
        this.camera = camera;
        this.gameLevel = gameLevel;
        this.stage = stage;
        this.player = gameLevel.getPlayer();

        scoreFont = new BitmapFont(); // Default font
        scoreFont.setColor(Color.WHITE);
        scoreFont.getData().setScale(2); // Make it bigger

        layout = new GlyphLayout();
    }
       // white by default

    public void render(float timeLeft) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        batch.draw(gameLevel.getBackground(), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.draw(player.getCurrentFrame(), player.getPosition().x, player.getPosition().y);

        for (Obstacle obstacle : gameLevel.getObstacles()) {
            batch.draw(obstacle.getTexture(), obstacle.getBounds().x, obstacle.getBounds().y, obstacle.getBounds().width, obstacle.getBounds().height);
        }

        // Draw the total score at top center of screen
        for (Obstacle o : gameLevel.getObstacles()) {
            if (o instanceof OutputRegister) {
                OutputRegister output = (OutputRegister) o;
                String scoreText = "Score: " + output.getScore();

                layout.setText(scoreFont, scoreText);
                float x = (Gdx.graphics.getWidth() - layout.width) / 2;
                float y = Gdx.graphics.getHeight() - 30;

                scoreFont.draw(batch, scoreText, x, y);
            }
        }

        // Draw TIMER centered below the score
        String secs = String.format("%02d", (int) timeLeft);
        layout.setText(timerFont, secs);
        float x = (Gdx.graphics.getWidth() - layout.width) / 2;
        float y = Gdx.graphics.getHeight() - 60; // Lower than the score (adjust padding as needed)

        timerFont.setColor(Color.BLACK); // Optional: ensure it's visible
        timerFont.draw(batch, secs, x, y);


        batch.end();
    }

    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        batch.draw(gameLevel.getBackground(), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.draw(player.getCurrentFrame(), player.getPosition().x, player.getPosition().y);

        for (Obstacle obstacle : gameLevel.getObstacles()) {
            batch.draw(obstacle.getTexture(), obstacle.getBounds().x, obstacle.getBounds().y, obstacle.getBounds().width, obstacle.getBounds().height);
        }

        // Draw the total score at top center of screen
        for (Obstacle o : gameLevel.getObstacles()) {
            if (o instanceof OutputRegister) {
                OutputRegister output = (OutputRegister) o;
                String scoreText = "Score: " + output.getScore();

                layout.setText(scoreFont, scoreText);
                float x = (Gdx.graphics.getWidth() - layout.width) / 2;
                float y = Gdx.graphics.getHeight() - 30;

                scoreFont.draw(batch, scoreText, x, y);
            }
        }

        batch.end();

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    public void dispose() {
        if (scoreFont != null) scoreFont.dispose();
    }

}
