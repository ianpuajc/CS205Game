package io.game;

public class Level1 extends GameLevel {
    public Level1() {
        super("background1.png"); // Set specific background
    }

    @Override
    protected void setupLevel() {
        obstacles.add(new Obstacle(400, 300, 100, 100));
        obstacles.add(new Obstacle(600, 500, 200, 200));
    }
}
