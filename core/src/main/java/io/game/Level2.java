package io.game;

public class Level2 extends GameLevel {
    public Level2() {
        super("background2.png"); // Set specific background for Level 2
    }

    @Override
    protected void setupLevel() {
        // Add obstacles unique to Level 2
        obstacles.add(new Obstacle(200, 400, 150, 150));
        obstacles.add(new Obstacle(500, 350, 100, 200));
        obstacles.add(new Obstacle(800, 600, 120, 120));
    }
}
