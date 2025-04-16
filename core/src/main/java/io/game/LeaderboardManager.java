package io.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import java.util.ArrayList;
import java.util.Collections;

public class LeaderboardManager {
    private static final String PREF_NAME = "leaderboard";
    private static final String KEY_SCORES = "top_scores"; // Stores scores as a comma-separated string
    private Preferences prefs;

    public LeaderboardManager() {
        prefs = Gdx.app.getPreferences(PREF_NAME);
    }

    /**
     * Adds a new score to the leaderboard. The leaderboard is kept to the top 3 scores.
     */
    public void addScore(int newScore) {
        ArrayList<Integer> scores = getScores();
        scores.add(newScore);
        // Sort scores in descending order
        Collections.sort(scores, Collections.reverseOrder());
        // Keep only the top 3 scores
        if (scores.size() > 3) {
            scores = new ArrayList<>(scores.subList(0, 3));
        }
        saveScores(scores);
    }

    /**
     * Retrieves the list of scores from preferences.
     */
    public ArrayList<Integer> getScores() {
        String scoreString = prefs.getString(KEY_SCORES, "");
        ArrayList<Integer> scores = new ArrayList<>();
        if (!scoreString.isEmpty()) {
            String[] parts = scoreString.split(",");
            for (String part : parts) {
                try {
                    scores.add(Integer.parseInt(part.trim()));
                } catch (NumberFormatException e) {
                    // Skip if the score is invalid
                }
            }
        }
        return scores;
    }

    /**
     * Resets the leaderboard by clearing all saved scores.
     */
    public void resetLeaderboard() {
        prefs.remove(KEY_SCORES);
        prefs.flush();
    }

    /**
     * Saves the list of scores back to Preferences.
     */
    private void saveScores(ArrayList<Integer> scores) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < scores.size(); i++) {
            sb.append(scores.get(i));
            if (i < scores.size() - 1) {
                sb.append(",");
            }
        }
        prefs.putString(KEY_SCORES, sb.toString());
        prefs.flush();
    }
}
