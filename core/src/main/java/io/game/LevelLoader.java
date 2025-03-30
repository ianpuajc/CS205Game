package io.game;

public class LevelLoader {
    private static int currentLevel = 1;

    public static GameLevel loadLevel(int levelNumber) {
        switch (levelNumber) {
            case 1: return new Level1();
            case 2: return new Level2();
            case 3: return new Level3();
            default: return new Level1();
        }
    }

    public static int getCurrentLevel() {

        return currentLevel;
    }

    // Call this when a level is completed successfully.
    public static void unlockNextLevel() {
        currentLevel++;
    }
}
