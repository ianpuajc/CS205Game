package io.game;

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

    /**
     * Attempts to submit a ProcessItem to the output register.
     * If the Process inside the item is completed, the register updates the score
     * according to the process color:
     *   - GREEN: CPU only = 5 points
     *   - BLUE: CPU + I/O Module = 10 points
     *   - RED: CPU + I/O Module + CPU = 15 points
     *
     * @param item The process item being submitted.
     * @return true if the process is completed and accepted; false otherwise.
     */
    public boolean submitProcess(ProcessItem item) {
        if (item.getProcess().isCompleted()) {
            switch (item.getProcess().getColor()) {
                case GREEN:
                    score += 5;
                    break;
                case BLUE:
                    score += 10;
                    break;
                case RED:
                    score += 15;
                    break;
            }
            // Store the submitted item if you want to display it
            submittedProcessItem = item;
            return true;
        }
        return false;
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

