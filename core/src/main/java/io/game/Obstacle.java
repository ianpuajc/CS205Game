package io.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class Obstacle {
    private Texture texture;
    private Rectangle bounds;

    public Obstacle(float x, float y, float width, float height) {
        texture = new Texture("obstacle.png");
        bounds = new Rectangle(x, y, width, height);
    }

    public Texture getTexture() { return texture; }
    public Rectangle getBounds() { return bounds; }
    public void dispose() { texture.dispose(); }
}
