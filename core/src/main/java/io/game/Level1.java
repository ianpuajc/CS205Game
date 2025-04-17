package io.game;

public class Level1 extends GameLevel {
    public Level1() {
        super("Overclocked Assets/Background and Deco/Background.PNG"); // Set specific background
    }

    @Override
    protected void setupLevel() {
        // Add the output register at the desired position (for example, where the red box should be)
        obstacles.add(new OutputRegister(1700, 500, 150, 200));
    }
}
