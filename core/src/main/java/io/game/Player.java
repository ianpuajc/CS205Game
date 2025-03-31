package io.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import java.util.List;

public class Player {
    private Animation<TextureRegion> walkUpAnimation;
    private Animation<TextureRegion> walkDownAnimation;
    private Animation<TextureRegion> walkLeftAnimation;
    private Animation<TextureRegion> walkRightAnimation;
    private TextureRegion idleFrame;

    final private int Wall_boundary = 260
    private Vector2 position;
    private Rectangle bounds;
    private float speed = 500;
    private float stateTime = 0;
    private boolean isMoving = false;
    private Direction currentDirection = Direction.DOWN;
    private static final float ANIMATION_DELAY = 0.2f; // Increased delay
    private enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    public Player() {
        walkUpAnimation = createAnimation("walk_up_spritesheet.png", 4, ANIMATION_DELAY);
        walkDownAnimation = createAnimation("walk_down_spritesheet.png", 4, ANIMATION_DELAY);
        walkLeftAnimation = createAnimation("walk_left_spritesheet.png", 2, ANIMATION_DELAY);
        walkRightAnimation = createAnimation("walk_right_spritesheet.png", 2, ANIMATION_DELAY);
        idleFrame = walkDownAnimation.getKeyFrame(0);
        position = new Vector2(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f);
        bounds = new Rectangle(position.x, position.y, idleFrame.getRegionWidth(), idleFrame.getRegionHeight());
    }



    private Animation<TextureRegion> createAnimation(String spriteSheetPath, int frameCols, float frameDuration) {
        Texture sheet = new Texture(spriteSheetPath);
        int frameRows = 1;
        int frameWidth = sheet.getWidth() / frameCols;
        int frameHeight = sheet.getHeight() / frameRows;
        TextureRegion[][] tmp = TextureRegion.split(sheet, frameWidth, frameHeight);
        TextureRegion[] frames = new TextureRegion[frameCols];
        for (int j = 0; j < frameCols; j++) {
            frames[j] = tmp[0][j];
        }
        Animation<TextureRegion> animation = new Animation<>(frameDuration, frames);
        animation.setPlayMode(Animation.PlayMode.LOOP);
        return animation;
    }

    public void update(float deltaTime, List<Obstacle> obstacles) {
        // Movement is handled externally by InputHandler
    }

    public void updateAnimationState(Vector2 direction) {
        if (direction.isZero()) {
            setIdleAnimation();
            return;
        }

        float angle = direction.angleDeg();

        if (angle >= 45 && angle < 135) {
            setAnimation(Direction.UP);
        } else if (angle >= 135 && angle < 225) {
            setAnimation(Direction.LEFT);
        } else if (angle >= 225 && angle < 315) {
            setAnimation(Direction.DOWN);
        } else {
            setAnimation(Direction.RIGHT);
        }
    }

    public void setIdleAnimation() {
        idleFrame = walkDownAnimation.getKeyFrame(0);
    }

    private void setAnimation(Direction direction) {
        currentDirection = direction;
    }

    public void move(float moveX, float moveY, List<Obstacle> obstacles) {
        if (moveX == 0 && moveY == 0) {
            isMoving = false;
            return;
        }

        float newX = position.x + moveX;
        float newY = position.y + moveY;
        Rectangle newBounds = new Rectangle(newX, newY, bounds.width, bounds.height);

        for (Obstacle obstacle : obstacles) {
            if (newBounds.overlaps(obstacle.getBounds())) {
                isMoving = false;
                return;
            }
        }

        if (newBounds.x >= 0 && newBounds.x + newBounds.width <= Gdx.graphics.getWidth() &&
            newBounds.y >= 0 && newBounds.y + newBounds.height <= Gdx.graphics.getHeight()-Wall_boundary) {
            position.set(newX, newY);
            bounds.setPosition(newX, newY);
            isMoving = true;

            Vector2 direction = new Vector2(moveX, moveY).nor();
            updateAnimationState(direction);
        } else {
            isMoving = false;
        }
    }

    public TextureRegion getCurrentFrame() {
        if (!isMoving) {
            return idleFrame;
        }

        switch (currentDirection) {
            case UP:
                return walkUpAnimation.getKeyFrame(stateTime, true);
            case DOWN:
                return walkDownAnimation.getKeyFrame(stateTime, true);
            case LEFT:
                return walkLeftAnimation.getKeyFrame(stateTime, true);
            case RIGHT:
                return walkRightAnimation.getKeyFrame(stateTime, true);
            default:
                return idleFrame;
        }
    }

    public Vector2 getPosition() { return position; }

    public float getSpeed() {return speed;}

    public void dispose() {
        walkUpAnimation.getKeyFrames()[0].getTexture().dispose();
        walkDownAnimation.getKeyFrames()[0].getTexture().dispose();
        walkLeftAnimation.getKeyFrames()[0].getTexture().dispose();
        walkRightAnimation.getKeyFrames()[0].getTexture().dispose();
    }
}
