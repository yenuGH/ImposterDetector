package ca.cmpt276.assignment3.model;

// A class for storing the game options
public class GameOptions {

    private int selectedBoardSizeOptionIndex;
    private int selectedMineCountOptionIndex;

    // Singleton support for options
    private static GameOptions instance;
    private GameOptions() {
        // Private to prevent anything else instantiating this

        // Set options to -1 when instance is created for the first time
        selectedBoardSizeOptionIndex = -1;
        selectedMineCountOptionIndex = -1;
    }
    public static GameOptions getInstance(){
        if (instance == null){
            instance = new GameOptions();
        }
        return instance;
    }

    public void setGameOptions(int selectedBoardSizeOption, int selectedMineCountOption){
        this.selectedBoardSizeOptionIndex = selectedBoardSizeOption;
        this.selectedMineCountOptionIndex = selectedMineCountOption;
    }

    public int getSelectedBoardSizeOptionIndex() {
        return selectedBoardSizeOptionIndex;
    }
    public int getSelectedMineCountOptionId(){
        return selectedMineCountOptionIndex;
    }
}
