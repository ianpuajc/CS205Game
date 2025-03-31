package io.game;

public class IOdevice extends Obstacle{

    private Animation<TextureRegion> activationAnimation;
    private float stateTime;
    private boolean isActive;
    private float activationRadius;

    public ActivatableObstacle(float x, float y, float width, float height, float activationRadius, TextureAtlas atlas) {
        super(x, y, width, height);
        this.activationRadius = activationRadius;
        this.isActive = false;
        this.stateTime = 0f;

        // Assuming the animation frames are labeled "obstacle_frame1", "obstacle_frame2", etc.
        activationAnimation = new Animation<TextureRegion>(0.1f, atlas.findRegions(), Animation.PlayMode.LOOP);
    }

    public void update(float deltaTime, Vector2 playerPosition) {
        float distance = playerPosition.dst(getBounds().x + getBounds().width / 2, getBounds().y + getBounds().height / 2);
        if (distance <= activationRadius) {
            if (!isActive) {
                isActive = true;
                stateTime = 0f; // Reset animation time when activated
            }
            stateTime += deltaTime;
        } else {
            isActive = false;
            stateTime = 0f; // Optionally reset animation when deactivated
        }
    }

    public TextureRegion getCurrentFrame() {
        if (isActive) {
            return activationAnimation.getKeyFrame(stateTime);
        } else {
            // Return a default frame or null when not active
            return activationAnimation.getKeyFrame(0);
        }
    }

    public boolean isActive() {
        return isActive;
    }
}
