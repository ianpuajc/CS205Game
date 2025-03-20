package io.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Rectangle;
import java.util.List;

public class Player {
    private Texture texture;
    private Vector2 position;
    private Rectangle bounds;
    float speed = 500;

    public Player() {
        texture = new Texture("player.png");
        position = new Vector2(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f);
        bounds = new Rectangle(position.x, position.y, texture.getWidth(), texture.getHeight());
    }

    public void update(float deltaTime, List<Obstacle> obstacles) {
        // Movement is handled externally by InputHandler
    }

    public void move(float moveX, float moveY, List<Obstacle> obstacles) {
        float newX = position.x + moveX;
        float newY = position.y + moveY;
        Rectangle newBounds = new Rectangle(newX, newY, texture.getWidth(), texture.getHeight());

        // Prevent player from going out of bounds & handle collisions
        if (newBounds.x >= 0 && newBounds.x + newBounds.width <= Gdx.graphics.getWidth() &&
            newBounds.y >= 0 && newBounds.y + newBounds.height <= Gdx.graphics.getHeight()) {
            for (Obstacle obstacle : obstacles) {
                if (newBounds.overlaps(obstacle.getBounds())) return;
            }
            position.set(newX, newY);
            bounds.setPosition(newX, newY);
        }
    }

    public Vector2 getPosition() { return position; }
    public Texture getTexture() { return texture; }
    public void dispose() { texture.dispose(); }
}
