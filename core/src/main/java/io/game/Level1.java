package io.game;

import com.badlogic.gdx.Gdx;

public class Level1 extends GameLevel {
    public Level1() {
        super("Overclocked Assets/Background and Deco/Background.PNG"); // Set specific background
    }

    @Override
    protected void setupLevel() {
        // Add the output register at the desired position (for example, where the red box should be)
        obstacles.add(new OutputRegister(1700, 300, 120, 160));
        obstacles.add(new GPU(200, Gdx.graphics.getHeight()-500, 120, 160));
        obstacles.add(new GPU(400, Gdx.graphics.getHeight()-500, 120, 160));
        obstacles.add(new HardDrive(1700, 500, 120,160));
    }
}
