package ca.cmpt276.assignment3.model;

import java.util.ArrayList;

/*
    Stores a list of games played, along with 
    Gets highest score for each game type
    Delete highscore for game configuration
*/
public class GameManager {
    ArrayList<Game> games;
    Game lastGameState; // Saved game state for if the application is closed mid-game

    // Singleton support for options
    private static GameManager instance;

    private GameManager() {
        // Private to prevent anything else instantiating this
        games = new ArrayList<>();
    }

    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    public void addGame(Game game) {
        games.add(game);
    }

    // Returns the amount of games played for a specific configuration
    public int getGamesPlayed(int rowNumber, int columnNumber, int mineNumber){
        int gamesPlayed = 0;
        for (Game game : games){
            if (game.getRowValue() == rowNumber && game.getColumnValue() == columnNumber && game.getTotalMines() == mineNumber){
                gamesPlayed++;
            }
        }
        return gamesPlayed;
    }

    public void resetGamesPlayed() {
        games.clear();
    }

    /**
     * Pass in parameters of game you want to look for, will iterate
     * through list of games and find one with the highest score
     */
    public Game findBestScoringGame(int rowNumber, int columnNumber, int mineNumber) {
        Game topScore = null;

        // Iterate through all played games
        for (Game game : games) {
            // Does the game match the parameters we're looking for?
            if (game.getRowValue() == rowNumber && game.getColumnValue() == columnNumber && game.getTotalMines() == mineNumber) {
                // Compare the scores
                if (topScore == null){
                    topScore = game;
                }
                else {
                    int currentTopScore = topScore.getScans();
                    int newScore = game.getScans();

                    // Replace the old score if the new one is higher
                    // Lower amount of scans = higher score
                    if (newScore < currentTopScore) {
                        topScore = game;
                    }
                }
            }
        }

        return topScore;
    }
}
