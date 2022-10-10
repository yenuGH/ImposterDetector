package ca.cmpt276.assignment3.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioGroup;

import ca.cmpt276.assignment3.R;
import ca.cmpt276.assignment3.model.GameOptions;

public class OptionsMenuActivity extends AppCompatActivity {

    // Maybe create a dialog saying that if options were changed and save wasn't clicked and user tries exiting

    // 5.3 - Use SharedPreferences to save options data between application runs
    // Use Google Gson for this? Game proprties may use LocalDateTime which requires Google Gson for TypeAdapter.

    // 5.4 - Button for resetting game data for each configuration

    private GameOptions gameOptions;
    private RadioGroup rgBoardSize;
    private RadioGroup rgMineCount;
    private int rbSelectedBoardSizeId;
    private int rbSelectedMineCountId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options_menu);

        gameOptions = GameOptions.getInstance();
        rgBoardSize = findViewById(R.id.rgBoardSize);
        rgMineCount = findViewById(R.id.rgMineCount);
        setupSaveButton();

        // When the options screen is opened, setup the last checked options
        getGameOptions();

    }

    private void setupSaveButton(){
        Button btnSaveOptions = findViewById(R.id.btnSaveOptions);
        btnSaveOptions.setOnClickListener( view -> {
            saveGameOptions();
        });
    }

    private void getGameOptions(){
        int boardSizeOption = gameOptions.getSelectedBoardSizeOptionId();
        int mineCountOption = gameOptions.getSelectedMineCountOptionId();

        // if either one is set to -1, that means default options have not been saved yet.
        if (boardSizeOption == -1 || mineCountOption == -1){
            // If they have not been set, just set the id's to the default selected option
            rbSelectedBoardSizeId = rgBoardSize.getCheckedRadioButtonId();
            rbSelectedMineCountId = rgMineCount.getCheckedRadioButtonId();
            return;
        }

        // Set the ID's
        rbSelectedBoardSizeId = boardSizeOption;
        rbSelectedMineCountId = mineCountOption;
        // Then set the correlated radio button as checked
        rgBoardSize.check(rbSelectedBoardSizeId);
        rgMineCount.check(rbSelectedMineCountId);
    }

    private void saveGameOptions(){
        // Grab the id's of the selected radio buttons
        // Pass them into the game options to save
        rbSelectedBoardSizeId = rgBoardSize.getCheckedRadioButtonId();
        rbSelectedMineCountId = rgMineCount.getCheckedRadioButtonId();
        gameOptions.setGameOptions(rbSelectedBoardSizeId, rbSelectedMineCountId);
    }

}