package ca.cmpt276.assignment3.model;

// A class for storing the game options
public class GameOptions {

    // Singleton support for options
    private static GameOptions instance;
    private GameOptions() {
        // Private to prevent anything else instantiating this
        // Setting the default values for the options
    }
    public static GameOptions getInstance(){
        if (instance == null){
            instance = new GameOptions();
        }
        return instance;
    }

}
