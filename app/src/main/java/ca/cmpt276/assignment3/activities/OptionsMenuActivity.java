package ca.cmpt276.assignment3.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import ca.cmpt276.assignment3.R;
import ca.cmpt276.assignment3.model.GameOptions;

public class OptionsMenuActivity extends AppCompatActivity {

    // Maybe create a dialog saying that if options were changed and save wasn't clicked and user tries exiting
    // https://www.youtube.com/watch?v=_yaP4etGKlU

    // Could also add the radio buttons iteratively through code, rather than using xml to place them

    // 5.3 - Use SharedPreferences to save options data between application runs
    // Use Google Gson for this? Game properties may use LocalDateTime which requires Google Gson for TypeAdapter.

    // 5.4 - Button for resetting game data for each configuration

    // https://stackoverflow.com/questions/17120199/change-the-circle-color-of-radio-button
    // Creating a custom colour palette of the radio buttons as our background is black.
    ColorStateList colorStateList = new ColorStateList(
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
        //createMineCountButtons();

        rgBoardSize = findViewById(R.id.rgBoardSize);
        rgMineCount = findViewById(R.id.rgMineCount);
        gameOptions = GameOptions.getInstance();

        // Fill up the RadioGroups with dynamically created buttons
        createBoardSizeButtons();
        createMineCountButtons();
        setupSaveButton();

        // When the options screen is opened, setup the last checked options
        getGameOptions();

    }

    private void createBoardSizeButtons(){
        int[] boardRowSizes = getResources().getIntArray(R.array.board_row_sizes);
        int[] boardColumnSizes = getResources().getIntArray(R.array.board_column_sizes);

        for (int i = 0; i < boardRowSizes.length; i++){
            int rowValue = boardRowSizes[i];
            int columnValue = boardColumnSizes[i];

            RadioButton radioButton = new RadioButton(OptionsMenuActivity.this);
            radioButton.setText(rowValue + " rows by " + columnValue + " columns");
            radioButton.setTextColor(Color.rgb(255, 255, 255));
            radioButton.setButtonTintList(colorStateList);

            radioButton.setOnClickListener( view -> {
                selectedBoardRowValue = rowValue;
                selectedBoardColumnValue = columnValue;
            });

            rgBoardSize.addView(radioButton);
            // Set the first one to default
            if (i == 0){
                rgBoardSize.check(radioButton.getId());
            }
        }
    }

    private void createMineCountButtons(){
        int[] mineCount = getResources().getIntArray(R.array.mine_count);

        for (int i = 0; i < mineCount.length; i++){
            int mineCountValue = mineCount[i];

            RadioButton radioButton = new RadioButton(OptionsMenuActivity.this);
            radioButton.setText(mineCountValue + " mines");
            radioButton.setTextColor(Color.rgb(255, 255, 255));
            radioButton.setButtonTintList(colorStateList);

            radioButton.setOnClickListener(view -> {
                selectedMineCountValue = mineCountValue;
            });

            rgMineCount.addView(radioButton);
            // Set the first one to default
            if (i == 0){
                rgMineCount.check(radioButton.getId());
            }
        }
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

    private void getGameOptions(){
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