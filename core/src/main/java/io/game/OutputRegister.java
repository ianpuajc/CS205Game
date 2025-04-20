package io.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.utils.Array;

public class OutputRegister extends Obstacle {

    private Texture idleTexture;
    private TextureRegion currentFrame;
    private Animation<TextureRegion> submissionAnimation;

    private boolean isAnimating = false;
    private float stateTime = 0f;

    private int score;
    private BitmapFont font;
    private ProcessItem submittedProcessItem;

    public OutputRegister(float x, float y, float width, float height) {
        super(x, y, width, height);

        idleTexture = new Texture("Overclocked Assets/Workstations/Monitor_Idle.PNG");
        currentFrame = new TextureRegion(idleTexture);
        loadAnimation();

        score = 0;
        font = new BitmapFont();
    }

    private void loadAnimation() {
        Array<TextureRegion> frames = new Array<>();

        for (int i = 1; i <= 19; i++) {
            String filename = (i < 10)
                ? "Overclocked Assets/Workstations/Monitor_Loading/Monitor_Loading_000" + i + ".png"
                : "Overclocked Assets/Workstations/Monitor_Loading/Monitor_Loading_00" + i + ".png";
            Texture frameTexture = new Texture(filename);
            frames.add(new TextureRegion(frameTexture));
        }

        submissionAnimation = new Animation<>(0.05f, frames);
        submissionAnimation.setPlayMode(Animation.PlayMode.NORMAL);
    }

    public void update(float delta) {
        if (isAnimating) {
            stateTime += delta;
            currentFrame = submissionAnimation.getKeyFrame(stateTime, false);

            if (submissionAnimation.isAnimationFinished(stateTime)) {
                isAnimating = false;
                stateTime = 0f;
                currentFrame = new TextureRegion(idleTexture);
            }
        }
    }

    public boolean submitProcess(ProcessItem item) {
        Process.ProcessColor color = item.getProcess().getColor();
        Gdx.app.log("OutputRegister", "Submitted item color: " + color);

        if (item.getProcess().isCompleted() && !isAnimating) {
            isAnimating = true;
            switch (color) {
                case SINGLE:
                    score += 50;
                    break;
                case DOUBLE:
                    score += 100;
                    break;
                default:
                    Gdx.app.log("OutputRegister", "⚠️ Unknown color: " + color);
                    break;
            }

            // Start animation

            stateTime = 0f;

            return true;
        }

        submittedProcessItem = item;
        return false;
    }

    public int getScore() {
        return score;
    }

    public void draw(SpriteBatch batch) {
        batch.draw(currentFrame, getBounds().x, getBounds().y, getBounds().width, getBounds().height);
        font.draw(batch, "Score: " + score, getBounds().x, getBounds().y + getBounds().height + 20);

        if (submittedProcessItem != null) {
            Texture itemTexture = submittedProcessItem.getTexture();
            float itemWidth = itemTexture.getWidth();
            float itemHeight = itemTexture.getHeight();
            float itemX = getBounds().x + (getBounds().width - itemWidth) / 2;
            float itemY = getBounds().y + (getBounds().height - itemHeight) / 2;
            batch.draw(itemTexture, itemX, itemY);
        }
    }

    @Override
    public Texture getTexture() {
        return currentFrame.getTexture(); // For consistency
    }

    @Override
    public void dispose() {
        idleTexture.dispose();
        font.dispose();
        // Optionally dispose animation textures if needed, depending on resource handling
    }
}
