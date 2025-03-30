package io.game;

public class Level3 extends GameLevel {
    public Level3() {
        super("background3.png"); // Set specific background for Level 3
    }

    @Override
    protected void setupLevel() {
        // Add obstacles unique to Level 3
        obstacles.add(new Obstacle(300, 200, 200, 100));
        obstacles.add(new Obstacle(700, 400, 150, 150));
        obstacles.add(new Obstacle(1000, 500, 100, 200));
        obstacles.add(new Obstacle(1200, 650, 120, 120));
    }
}
