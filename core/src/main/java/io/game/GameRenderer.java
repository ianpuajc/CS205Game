package io.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class GameRenderer {
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private GameLevel gameLevel;
    private Stage stage;

    private Player player;

    public GameRenderer(SpriteBatch batch, OrthographicCamera camera, GameLevel gameLevel, Stage stage) {
        this.batch = batch;
        this.camera = camera;
        this.gameLevel = gameLevel;
        this.stage = stage;
        this.player = gameLevel.getPlayer();
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
        batch.end();

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }
}
