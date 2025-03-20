package io.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.ArrayList;

public abstract class GameLevel {
    protected Player player;
    protected ArrayList<Obstacle> obstacles;
    protected Texture background; // New background texture

    public GameLevel(String backgroundPath) {
        player = new Player();
        obstacles = new ArrayList<>();
        background = new Texture(backgroundPath); // Load background image
        setupLevel();
    }

    protected abstract void setupLevel();

    public void update(float deltaTime) {
        player.update(deltaTime, obstacles);
    }

    public Texture getBackground() { return background; }

    public Player getPlayer() { return player; }
    public ArrayList<Obstacle> getObstacles() { return obstacles; }

    public void dispose() {
        player.dispose();
        background.dispose(); // Dispose of background texture
        for (Obstacle obstacle : obstacles) {
            obstacle.dispose();
        }
    }
}
