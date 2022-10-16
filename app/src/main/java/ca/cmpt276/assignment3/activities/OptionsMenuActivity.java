package ca.cmpt276.assignment3.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import ca.cmpt276.assignment3.R;
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
    public  final String GAME_OPTION_PREFERENCES = "Game Option Preferences";
    public final String GAME_MANAGER_PREFERENCES = "Game Manager Preferences";

    private GameManager gameManager;
    private GameOptions gameOptions;
    private RadioGroup rgBoardSize;
    private RadioGroup rgMineCount;

    private int selectedBoardSizeId;
    private int selectedBoardRowValue;
    private int selectedBoardColumnValue;

    private int selectedMineCountId;
    private int selectedMineCountValue;

    private boolean clickedSaveButton = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options_menu);

        rgBoardSize = findViewById(R.id.rgBoardSize);
        rgMineCount = findViewById(R.id.rgMineCount);
        gameOptions = GameOptions.getInstance();
        gameManager = GameManager.getInstance();

        // When the options screen is opened, setup the last checked options
        // Fill up the RadioGroups with dynamically created buttons
        createBoardSizeButtons();
        createMineCountButtons();
        loadOptions();
        setupSaveButton();
        setupClearScoresButton();
    }

    @Override
    public void onBackPressed(){
        if (clickedSaveButton == false){
            createSaveWarningDialog();
        }
        else {
            super.onBackPressed();
        }
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
            clickedSaveButton = true;
            setOptions();
            saveOptionsData();
        });
    }

    private void createSaveWarningDialog() {
        // When the user tries to exit the options screen without clicking the save button
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(OptionsMenuActivity.this);
        alertDialogBuilder.setTitle("You haven't clicked save!");
        alertDialogBuilder.setMessage("Hey there!\n\nYou haven't clicked on the save button, so any changes made to " +
                "your scores or game options will not be saved!\n\nAre you sure you want to exit without saving?");

        // If the user wishes to exit without saving changes
        alertDialogBuilder.setPositiveButton("Yes, cancel changes and exit.", (dialogInterface, i) -> {
           finish();
        });

        // If the user would like to save changes, save data and exit activity
        alertDialogBuilder.setNegativeButton("No, save changes and exit.", ((dialogInterface, i) -> {
            setOptions();
            saveOptionsData();
            finish();
        }));

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    private void setupClearScoresButton() {
        Button btnClearScores = findViewById(R.id.btnClearScores);
        btnClearScores.setOnClickListener( view -> {
            gameManager.resetGamesPlayed();
            saveGamesList();
        });
    }

    private void setOptions(){

        int boardSizeId = rgBoardSize.getCheckedRadioButtonId();
        int mineCountId = rgMineCount.getCheckedRadioButtonId();

        gameOptions.setGameOptions(boardSizeId,
                                    mineCountId,
                                    selectedBoardRowValue,
                                    selectedBoardColumnValue,
                                    selectedMineCountValue);

    }

    private void loadOptions() {
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

    // Save options data into shared preferences
    private void saveOptionsData(){
        SharedPreferences sharedPreferences = getSharedPreferences("Options Preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();

        // Convert the current instance of game options to json string
        String json = gson.toJson(gameOptions);

        // Save data into sharedpreferences
        editor.putString(GAME_OPTION_PREFERENCES, json);
        editor.apply();
    }

    // If user decides to clear game scores, save empty games list to shared preferences
    public void saveGamesList(){
        SharedPreferences sharedPreferences = getSharedPreferences("Manager Preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();

        // Convert the current instance of game manager to json string
        String json = gson.toJson(gameManager.getGameList());

        // Save data into shared preferences
        editor.putString(GAME_MANAGER_PREFERENCES, json);
        editor.apply();
    }

}