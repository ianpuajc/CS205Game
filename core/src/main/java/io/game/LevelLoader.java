package io.game;

public class LevelLoader {
    private static int currentLevel = 1;

    public static GameLevel loadLevel(int levelNumber) {
        switch (levelNumber) {
            case 1: return new Level1();
            default: return new Level1();
        }
    }

    public static int getCurrentLevel() {
        return currentLevel;
    }
}
