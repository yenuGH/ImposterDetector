package ca.cmpt276.assignment3.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import ca.cmpt276.assignment3.R;
import ca.cmpt276.assignment3.model.Game;
import ca.cmpt276.assignment3.model.GameManager;
import ca.cmpt276.assignment3.model.GameOptions;

public class OptionsMenuActivity extends AppCompatActivity {

    // Maybe create a dialog saying that if options were changed and save wasn't clicked and user tries exiting
    // 5.4 - Button for resetting game data for each configuration

    // https://stackoverflow.com/questions/17120199/change-the-circle-color-of-radio-button
    // Creating a custom colour palette of the radio buttons as our background is black.
    private final ColorStateList COLORSTATELIST = new ColorStateList(
            new int[][]
                    {
                            new int[]{-android.R.attr.state_enabled}, // Disabled
                            new int[]{android.R.attr.state_enabled}   // Enabled
                    },
            new int[]
                    {
                            Color.rgb(255, 255, 255), // disabled
                            Color.rgb(128, 203, 196)   // enabled
                    }
    );
    private final String RB_BOARD_SIZE_ID_PREFIX = "board";
    private final String RB_MINE_COUNT_ID_PREFIX = "mine";
    public static final String GAME_OPTION_PREFERENCES = "Game Option Preferences";

    private GameOptions gameOptions;
    private RadioGroup rgBoardSize;
    private RadioGroup rgMineCount;

    private int selectedBoardSizeId;
    private int selectedBoardRowValue;
    private int selectedBoardColumnValue;

    private int selectedMineCountId;
    private int selectedMineCountValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options_menu);

        rgBoardSize = findViewById(R.id.rgBoardSize);
        rgMineCount = findViewById(R.id.rgMineCount);
        gameOptions = GameOptions.getInstance();

        // When the options screen is opened, setup the last checked options
        // Fill up the RadioGroups with dynamically created buttons
        createBoardSizeButtons();
        createMineCountButtons();
        loadGameOptions();
        setupSaveButton();
        setupClearScoresButton();
    }

    // https://www.youtube.com/watch?v=_yaP4etGKlU
    private void createBoardSizeButtons(){
        int[] boardRowSizes = getResources().getIntArray(R.array.board_row_sizes);
        int[] boardColumnSizes = getResources().getIntArray(R.array.board_column_sizes);

        for (int i = 0; i < boardRowSizes.length; i++){
            int rowValue = boardRowSizes[i];
            int columnValue = boardColumnSizes[i];

            RadioButton radioButton = new RadioButton(OptionsMenuActivity.this);
            radioButton.setId(createCustomKey(RB_BOARD_SIZE_ID_PREFIX) + i);
            radioButton.setText(getString(R.string.board_size_text, rowValue, columnValue));
            radioButton.setTextColor(Color.rgb(255, 255, 255));
            radioButton.setButtonTintList(COLORSTATELIST);

            radioButton.setOnClickListener( view -> {
                selectedBoardRowValue = rowValue;
                selectedBoardColumnValue = columnValue;
            });

            rgBoardSize.addView(radioButton);
            // Set the first one to default if first time run
            if (i == 0 && selectedBoardSizeId == 0){
                rgBoardSize.check(radioButton.getId());
            }

        }
    }

    // https://www.youtube.com/watch?v=_yaP4etGKlU
    private void createMineCountButtons(){
        int[] mineCount = getResources().getIntArray(R.array.mine_count);

        for (int i = 0; i < mineCount.length; i++){
            int mineCountValue = mineCount[i];

            RadioButton radioButton = new RadioButton(OptionsMenuActivity.this);
            radioButton.setId(createCustomKey(RB_MINE_COUNT_ID_PREFIX) + i);
            radioButton.setText(getString(R.string.mine_count_text, mineCountValue));
            radioButton.setTextColor(Color.rgb(255, 255, 255));
            radioButton.setButtonTintList(COLORSTATELIST);

            radioButton.setOnClickListener(view -> {
                selectedMineCountValue = mineCountValue;
            });

            rgMineCount.addView(radioButton);
            // Set the first one to default if first time run
            if (i == 0 && selectedMineCountId == 0){
                rgMineCount.check(radioButton.getId());
            }

        }
    }

    private int createCustomKey(String key){
        StringBuilder stringKey = new StringBuilder();
        for (int i = 0; i < key.length(); i++){
            stringKey.append(key.charAt(i) - 'a' + 1);
        }
        // https://stackoverflow.com/questions/54092485/removing-all-non-digits-from-string
        String sbString = stringKey.toString().replaceAll("\\D", "");
        return Integer.parseInt(sbString);
    }

    private void setupSaveButton() {
        Button btnSaveOptions = findViewById(R.id.btnSaveOptions);
        btnSaveOptions.setOnClickListener( view -> {
            saveGameOptions();
        });
    }


    private void setupClearScoresButton() {
        Button btnClearScores = findViewById(R.id.btnClearScores);
        btnClearScores.setOnClickListener( view -> {
            GameManager.getInstance().resetGamesPlayed();
        });
    }

    private void saveGameOptions(){

        int boardSizeId = rgBoardSize.getCheckedRadioButtonId();
        int mineCountId = rgMineCount.getCheckedRadioButtonId();

        gameOptions.setGameOptions(boardSizeId,
                                    mineCountId,
                                    selectedBoardRowValue,
                                    selectedBoardColumnValue,
                                    selectedMineCountValue);

        // Save the data into shared preferences
        saveData();
    }

    private void loadGameOptions() {

        // Load saved data (if there is any) from shared preferences
        loadData();

        selectedBoardSizeId = gameOptions.getSelectedBoardSizeOptionId();
        selectedMineCountId = gameOptions.getSelectedMineCountOptionId();

        // If either of these IDs are -1, it means they have not been initialized yet.
        // Proceed with default values, can just exit

        selectedBoardRowValue = gameOptions.getRowValue();
        selectedBoardColumnValue = gameOptions.getColumnValue();
        selectedMineCountValue = gameOptions.getMineCountValue();

        if (selectedBoardSizeId == -1 || selectedMineCountId == -1){
            return;
        }

        rgBoardSize.check(selectedBoardSizeId);
        rgMineCount.check(selectedMineCountId);
    }

    public void saveData(){
        // Use the context of the GameOptionsActivity to access shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences("Game Options Preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();

        // Convert the current instance of game options to json string
        String json = gson.toJson(gameOptions);

        // Put json string into editor
        editor.putString(GAME_OPTION_PREFERENCES, json);
        editor.apply();
    }

    public void loadData(){
        // Use the context of the GameOptionsActivity to access shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences("Game Options Preferences", MODE_PRIVATE);
        Gson gson = new Gson();

        // Convert the current instance of game options to json string
        String json = sharedPreferences.getString(GAME_OPTION_PREFERENCES, null);
        Type type = new TypeToken<GameOptions>() {}.getType();

        // If null, keep old instance
        if (json == null){
            return;
        }

        // Retrieve saved options data
        gameOptions = gson.fromJson(json, type);
    }

}