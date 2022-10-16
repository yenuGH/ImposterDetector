package ca.cmpt276.assignment3.model;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

// A class for storing the game options
public class GameOptions {

    // Contains the IDs of the options that is selected
    private int selectedBoardSizeOptionId;
    private int selectedMineCountOptionId;

    private final int DEFAULT_ROW_SIZE = 4;
    private final int DEFAULT_COLUMN_SIZE = 6;
    private final int DEFAULT_MINE_COUNT = 6;

    private int rowValue;
    private int columnValue;
    private int mineCountValue;

    // Singleton support for options
    private static GameOptions instance;
    private GameOptions() {
        // Private to prevent anything else instantiating this

        // Set options to -1 when instance is created for the first time
        selectedBoardSizeOptionId = -1;
        selectedMineCountOptionId = -1;
        // Set the values to their default values
        rowValue = DEFAULT_ROW_SIZE;
        columnValue = DEFAULT_COLUMN_SIZE;
        mineCountValue = DEFAULT_MINE_COUNT;
    }
    public static GameOptions getInstance() {
        if (instance == null) {
            instance = new GameOptions();
        }
        return instance;
    }

    public void setGameOptions(int boardSizeId, int mineCountId, int rowValue, int columnValue, int mineCountValue) {
        // These values are used when we set up the options screen again
        // So we can set the checked button to the proper button ID
        this.selectedBoardSizeOptionId = boardSizeId;
        this.selectedMineCountOptionId = mineCountId;

        this.rowValue = rowValue;
        this.columnValue = columnValue;
        this.mineCountValue = mineCountValue;
    }

    public int getSelectedBoardSizeOptionId() {
        return selectedBoardSizeOptionId;
    }

    public int getSelectedMineCountOptionId() {
        return selectedMineCountOptionId;
    }

    public int getRowValue() {
        return rowValue;
    }

    public int getColumnValue() {
        return columnValue;
    }

    public int getMineCountValue() {
        return mineCountValue;
    }
}
