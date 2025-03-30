package io.game;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Main extends Game {
    public SpriteBatch batch;
    public OrthographicCamera camera;

    @Override
    public void create() { // called on initial running
        batch = new SpriteBatch();
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f, 0);
        camera.update();

        setScreen(new GameMenuScreen(this));

        //stage = new Stage(new ScreenViewport());
        //loadLevel(LevelLoader.getCurrentLevel());
        // i suppose this is the code that gets up th actual obstacle and what it looks like
        // but with menu screen, the menu screen should be created first, not all this.
    }

    @Override
    public void dispose() {
        batch.dispose();
        getScreen().dispose();
    }
}
