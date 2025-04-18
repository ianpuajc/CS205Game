package io.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.ArrayList;

public abstract class GameLevel {
    protected Player player;
    protected ArrayList<Obstacle> obstacles;
    protected Texture background; // New background texture
    private InstructionRegister instructionRegister;
    private float spawnTimer = 0f;
    private final float PROCESS_SPAWN_INTERVAL = 5f;

    public GameLevel(String backgroundPath) {
        player = new Player();
        obstacles = new ArrayList<>();
        background = new Texture(backgroundPath); // Load background image
        instructionRegister = new InstructionRegister(3);
        setupLevel();
    }

    protected abstract void setupLevel();

    public void update(float deltaTime) {
        player.update(deltaTime, obstacles);

        // Auto-spawn processes
        spawnTimer += deltaTime;
        if (spawnTimer >= PROCESS_SPAWN_INTERVAL) {
            spawnTimer = 0f;
            if (!instructionRegister.isFull()) {
                Process.ProcessColor color = randomProcessColor();
                instructionRegister.addProcess(new Process("P" + System.currentTimeMillis(), color));
            }
        }
    }

    private Process.ProcessColor randomProcessColor() {
        int r = (int)(Math.random() * 2);
        return Process.ProcessColor.values()[r];
    }

    public Texture getBackground() { return background; }

    public Player getPlayer() { return player; }
    public ArrayList<Obstacle> getObstacles() { return obstacles; }

    public InstructionRegister getInstructionRegister() {
        return instructionRegister;
    }

    public void dispose() {
        player.dispose();
        background.dispose(); // Dispose of background texture
        for (Obstacle obstacle : obstacles) {
            obstacle.dispose();
        }
    }
}
