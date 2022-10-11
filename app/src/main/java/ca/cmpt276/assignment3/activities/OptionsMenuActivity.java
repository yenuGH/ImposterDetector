package ca.cmpt276.assignment3.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import ca.cmpt276.assignment3.R;
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
            radioButton.setText(rowValue + " rows by " + columnValue + " columns");
            radioButton.setTextColor(Color.rgb(255, 255, 255));
            radioButton.setButtonTintList(COLORSTATELIST);

            radioButton.setOnClickListener( view -> {
                selectedBoardRowValue = rowValue;
                selectedBoardColumnValue = columnValue;
            });

            rgBoardSize.addView(radioButton);
            // Set the first one to default if first time run
            if (i == 0 && selectedBoardSizeId == -1){
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
            radioButton.setText(mineCountValue + " mines");
            radioButton.setTextColor(Color.rgb(255, 255, 255));
            radioButton.setButtonTintList(COLORSTATELIST);

            radioButton.setOnClickListener(view -> {
                selectedMineCountValue = mineCountValue;
            });

            rgMineCount.addView(radioButton);
            // Set the first one to default if first time run
            if (i == 0 && selectedMineCountId == -1){
                rgMineCount.check(radioButton.getId());
            }

        }
    }

    private int createCustomKey(@NonNull String key){
        StringBuilder stringKey = new StringBuilder();
        for (int i = 0; i < key.length(); i++){
            stringKey.append(key.charAt(i) - 'a' + 1);
        }
        String sbString = stringKey.toString().replaceAll("\\D", "");
        return Integer.parseInt(sbString);
    }

    private void setupSaveButton(){
        Button btnSaveOptions = findViewById(R.id.btnSaveOptions);
        btnSaveOptions.setOnClickListener( view -> {
            saveGameOptions();
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

    }

    private void loadGameOptions(){
        selectedBoardSizeId = gameOptions.getSelectedBoardSizeOptionId();
        selectedMineCountId = gameOptions.getSelectedMineCountOptionId();

        // If either of these IDs are -1, it means they have not been initialized yet.
        // Proceed with default values, can just exit
        if (selectedBoardSizeId == -1 || selectedMineCountId == -1){
            return;
        }

        rgBoardSize.check(selectedBoardSizeId);
        rgMineCount.check(selectedMineCountId);
    }

}