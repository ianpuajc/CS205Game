package io.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.ReentrantLock;

public abstract class GameLevel {
    protected Player player;
    protected ArrayList<Obstacle> obstacles;
    protected Texture background; // New background texture
    private InstructionRegister instructionRegister;
    private float spawnTimer = 0f;
    private final float PROCESS_SPAWN_INTERVAL = 5f;
    private ConcurrentLinkedQueue<Process> processQueue = new ConcurrentLinkedQueue<>();
    private final ReentrantLock queueLock = new ReentrantLock(); // Mutex for processQueue
    private Thread spawnThread;
    private boolean running = true;

    public GameLevel(String backgroundPath) {
        player = new Player();
        obstacles = new ArrayList<>();
        background = new Texture(backgroundPath); // Load background image
        instructionRegister = new InstructionRegister(3);
        setupLevel();
        startSpawningThread();
    }

    protected abstract void setupLevel();

    public void update(float deltaTime) {
        player.update(deltaTime, obstacles);

        queueLock.lock(); // Lock before accessing the queue
        try {
            while (!processQueue.isEmpty() && !instructionRegister.isFull()) {
                instructionRegister.addProcess(processQueue.poll());
            }
        } finally {
            queueLock.unlock(); // Always unlock
        }
    }

    public ReentrantLock getQueueLock() {
        return queueLock;
    }

    private void startSpawningThread() {
        spawnThread = new Thread(() -> {
            while (running) {
                try {
                    Thread.sleep((long) (PROCESS_SPAWN_INTERVAL * 1000)); // 5s delay
                } catch (InterruptedException e) {
                    break;
                }

                if (!instructionRegister.isFull()) {
                    Process.ProcessColor color = randomProcessColor();
                    queueLock.lock(); // Lock before adding to queue
                    try {
                        processQueue.add(new Process("P" + System.currentTimeMillis(), color));
                    } finally {
                        queueLock.unlock();
                    }
                }
            }
        });
        spawnThread.start();
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
        running = false;
        spawnThread.interrupt();

        player.dispose();
        background.dispose(); // Dispose of background texture
        for (Obstacle obstacle : obstacles) {
            obstacle.dispose();
        }
    }
}
