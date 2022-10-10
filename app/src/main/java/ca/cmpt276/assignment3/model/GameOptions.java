package ca.cmpt276.assignment3.model;

// A class for storing the game options
public class GameOptions {

    private int selectedBoardSizeOptionId;
    private int selectedMineCountOptionId;

    // Singleton support for options
    private static GameOptions instance;
    private GameOptions() {
        // Private to prevent anything else instantiating this

        // Set options to -1 when instance is created for the first time
        selectedBoardSizeOptionId = -1;
        selectedMineCountOptionId = -1;
    }
    public static GameOptions getInstance(){
        if (instance == null){
            instance = new GameOptions();
        }
        return instance;
    }

    public void setGameOptions(int selectedBoardSizeOption, int selectedMineCountOption){
        this.selectedBoardSizeOptionId = selectedBoardSizeOption;
        this.selectedMineCountOptionId = selectedMineCountOption;
    }

    public int getSelectedBoardSizeOptionId() {
        return selectedBoardSizeOptionId;
    }
    public int getSelectedMineCountOptionId(){
        return selectedMineCountOptionId;
    }
}
