package io.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class OutputRegister extends Obstacle {
    // Unique texture for the output register background
    private Texture registerTexture;
    private int score;
    private BitmapFont font;
    // Holds the last submitted process item (if you want to show it)
    private ProcessItem submittedProcessItem;

    public OutputRegister(float x, float y, float width, float height) {
        // Call Obstacle constructor to set up the bounds.
        super(x, y, width, height);
        // Load the output register's unique texture.
        registerTexture = new Texture("monitor_idle.png");
        score = 0;
        font = new BitmapFont();
    }

    /**
     * Override getTexture() so that when the game draws this obstacle using getTexture(),
     * it will use the output register's texture.
     */
    @Override
    public Texture getTexture() {
        return registerTexture;
    }

    public boolean submitProcess(ProcessItem item) {
        Process.ProcessColor color = item.getProcess().getColor();
        Gdx.app.log("OutputRegister", "Submitted item color: " + color); // ✅ Add this

        switch (color) {
            case GREEN:
                score += 50;
                break;
            case RED:
                score += 100;
                break;
            default:
                Gdx.app.log("OutputRegister", "⚠️ Unknown color: " + color);
                break;
        }

        submittedProcessItem = item;
        return true;
    }

    /**
     * Returns the current score.
     */
    public int getScore() {
        return score;
    }

    /**
     * Draws the output register including its background, score, and, if available,
     * the submitted process item.
     *
     * @param batch The SpriteBatch used for drawing.
     */
    public void draw(SpriteBatch batch) {
        // Draw the output register background using its unique texture.
        batch.draw(registerTexture, getBounds().x, getBounds().y, getBounds().width, getBounds().height);
        // Draw the score above the register.
        font.draw(batch, "Score: " + score, getBounds().x, getBounds().y + getBounds().height + 20);

        // If a ProcessItem was submitted, draw its texture centered in the register area.
        if (submittedProcessItem != null) {
            Texture itemTexture = submittedProcessItem.getTexture();
            float itemWidth = itemTexture.getWidth();
            float itemHeight = itemTexture.getHeight();
            float itemX = getBounds().x + (getBounds().width - itemWidth) / 2;
            float itemY = getBounds().y + (getBounds().height - itemHeight) / 2;
            batch.draw(itemTexture, itemX, itemY);
        }
    }

    /**
     * Disposes of textures and font to free resources.
     * We deliberately do not call super.dispose() here to avoid disposing of the Obstacle texture,
     * which may be shared elsewhere (such as in the inventory UI).
     */
    @Override
    public void dispose() {
        // Note: We no longer call super.dispose(), so the obstacle texture remains intact.
        registerTexture.dispose();
        font.dispose();
    }
}

