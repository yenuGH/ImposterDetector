package ca.cmpt276.assignment3.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import ca.cmpt276.assignment3.R;
import ca.cmpt276.assignment3.model.Game;
import ca.cmpt276.assignment3.model.GameManager;

/**
 * Activity where the game is played
 */
public class GameActivity extends AppCompatActivity {
    private int rowNumber;
    private int columnNumber;
    private int mineCount;
    private int foundMineCount;

    GameManager gameManager;
    Game currentGame;

    Button[][] buttons;

    private int numberOfGamesPlayed;
    private int scans = 0;

    MediaPlayer mediaPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);


        currentGame = new Game();

        loadGame();

        gameManager = GameManager.getInstance();

        columnNumber = currentGame.getColumnValue();
        rowNumber = currentGame.getRowValue();
        mineCount = currentGame.getTotalMines();
        numberOfGamesPlayed = gameManager.getSpecificGamesPlayed(rowNumber, columnNumber, mineCount);
        foundMineCount = currentGame.getFoundMines();

        buttons = new Button[rowNumber][columnNumber];

        updateGameInfo(foundMineCount, mineCount, scans, numberOfGamesPlayed);
        populateGrid();

        updateHighScoreText();
    }

    private void saveGame() {
        // Convert the game into json
        Gson gson = new Gson();
        String gameJSON = gson.toJson(currentGame);

        // Save the game into preferences
        SharedPreferences preferences = getSharedPreferences("Game Preferences",MODE_PRIVATE);
        SharedPreferences.Editor editor  = preferences.edit();

        editor.putString("GameJSON", gameJSON);
        editor.apply();
    }

    private void loadGame() {
        // Save the game into preferences
        SharedPreferences preferences = getSharedPreferences("Game Preferences",MODE_PRIVATE);
        String gameJSON = preferences.getString("GameJSON","");

        if (gameJSON == "") {
            return;
        }

        Gson gson = new Gson();
        Game jsonGame = gson.fromJson(gameJSON, Game.class);
        currentGame = jsonGame;
    }


    /**
     * Update the game's text fields
     * 
     * @param foundMines  Number of mines the player has found
     * @param totalMines  Total number of mines on the game board
     * @param scansUsed   Number of scans the player has used
     * @param timesPlayed Number of times the player has played a game
     */
    private void updateGameInfo(int foundMines, int totalMines, int scansUsed, int timesPlayed) {
        // Update the textview displaying the number of mines found/total
        String foundMinesString = String.format(getString(R.string.found_mines), foundMines, totalMines);
        updateTextView(R.id.tvFoundMines, foundMinesString);

        // Update the textview displaying the number of scans used
        String scansUsedString = String.format(getString(R.string.scans_used), scansUsed);
        updateTextView(R.id.tvScansUsed, scansUsedString);

        // Update the textview displaying the number of times a game has been played
        String timesPlayedString = String.format("Times Played: %d", timesPlayed);
        updateTextView(R.id.tvTimesPlayed, timesPlayedString);
    }

    /**
     * Updates the contents of a textview
     * 
     * @param textViewID Id of the textview
     * @param newString  String to set the contents of the textview to
     */
    private void updateTextView(int textViewID, String newString) {
        TextView textView = findViewById(textViewID);
        textView.setText(newString);
    }

    /**
     * Setup/populate buttons formatting and contents
     */
    private void populateGrid() {
        // Get the table containing the buttons
        TableLayout table = findViewById(R.id.tlButtons);

        // Populate the table with rows
        for (int row = 0; row < rowNumber; row++) {
            // Create a new tablerow to add buttons to
            TableRow tableRow = new TableRow(this);

            // Scale the rows to fill the table
            tableRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT, 1.0f));

            // Add the row to the table
            table.addView(tableRow);

            // Populate the row with buttons
            for (int col = 0; col < columnNumber; col++) {
                // Create a new button, and add it to the row
                Button button = new Button(this);

                // Scale the buttons to fill the row
                button.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT, 1.0f));


                // Stop text from clipping on smaller buttons
                button.setPadding(0, 0, 0, 0);

                // Create final ints for passing which button has been pressed into the inner
                // class
                final int FINAL_COLUMN = col;
                final int FINAL_ROW = row;

                // Create listener for when the button is pressed
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gridButtonClicked(FINAL_ROW, FINAL_COLUMN);
                    }
                });

                // Add the button to the row
                tableRow.addView(button);

                // Store a reference to the button
                buttons[row][col] = button;
            }

        }
    }

    /**
     * Updates every scanned button to show the new number of undiscovered mines
     */
    private void updateScannedButtons() {
        // Iterate through every button, update their scanned text
        for (int row = 0; row < rowNumber; row++) {
            for (int col = 0; col < columnNumber; col++) {
                // If a button has been scanned, update the number of mines inside it
                if (currentGame.isScanned(row, col)) {
                    Button button = buttons[row][col];
                    int mineCount = currentGame.getAdjacentMines(row, col);
                    button.setText("" + mineCount);
                }
            }
        }
    }

    /**
     * Handle a grid button being clicked
     * 
     * @param col The column the button was clicked in
     * @param row The row the button was clicked in
     */
    private void gridButtonClicked(int row, int col) {
        // Do nothing if the button has already been scanned
        if (currentGame.isScanned(row, col)) {
            return;
        }
        
        // Get the button
        Button button = buttons[row][col];

        // Lock Button Sizes
        lockButtonSizes();

        interactWithCell(row, col);
        // If mine was found, update foundMineCount
        foundMineCount = currentGame.getFoundMines();

        // If the button is unscanned, reveal the mine
        if (currentGame.isMine(row, col) && !currentGame.isScanned(row, col)) {
            // Get the size of the button
            int width = button.getWidth();
            int height = button.getHeight();

            // Scale the bitmap using a bitmap factory
            Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.action_tapped);
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, width, height, true);

            // Import the resource
            Resources resource = getResources();
            button.setBackground(new BitmapDrawable(resource, scaledBitmap));

            // Play impostor discovered sound
            playSound(R.raw.impostor_discovered);
        } else {

            // Animation tutorial: https://www.youtube.com/watch?v=JvyA7seYiCI
            // Flash cells in row/column
            for (int i = 0; i < rowNumber; i++) {
                flashCell(row,col, i, col);
            }

            for (int i = 0; i < columnNumber; i++) {
                flashCell(row,col, row,i);
            }

            // Play scan sound
            playSound(R.raw.scan_cell);
        }

        // Refresh the scanned results
        updateScannedButtons();

        // Check to see if all mines have been found
        // If so, user wins game - show win dialog message
        if (foundMineCount == currentGame.getTotalMines()){
            createWinDialog();
        }

        updateGameInfo(foundMineCount, mineCount, scans, numberOfGamesPlayed);
        saveGame();
    }

    private void flashCell(int originRow, int originCol, int row, int col) {
        Button button = buttons[row][col];
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
        int distanceFromOrigin = Math.abs(row-originRow) + Math.abs(col-originCol);
        animation.setStartOffset(100*distanceFromOrigin);
        button.setAnimation(animation);
    }

    /**
     * Marks a cell as scanned
     * 
     * @param row Row of the cell that was scanned
     * @param col Column of the cell that was scanned
     */
    private void interactWithCell(int row, int col) {
        currentGame.interactCell(row, col);
        scans = currentGame.getScans();
    }

    private void playSound(int soundID) {
        // Free the previous mediaplayer if it existed
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }

        mediaPlayer = MediaPlayer.create(getApplicationContext(), soundID);
        mediaPlayer.start();
    }

    /**
     * Locks the size of a button to avoid changing scales when an image is
     * loaded/contents are changed
     */
    private void lockButtonSizes() {
        // Iterate through every button, lock their sizes
        for (int row = 0; row < rowNumber; row++) {
            for (int col = 0; col < columnNumber; col++) {
                Button button = buttons[row][col];

                // Lock the width of the button
                int width = button.getWidth();
                button.setMinWidth(width);
                button.setMaxWidth(width);

                // Lock the height of the button
                int height = button.getHeight();
                button.setMinHeight(height);
                button.setMaxHeight(height);
            }
        }
    }

    private void createWinDialog(){

        // https://stackoverflow.com/questions/28937106/how-to-make-custom-dialog-with-rounded-corners-in-android
        // https://stackoverflow.com/questions/12102777/prevent-android-activity-dialog-from-closing-on-outside-touch
        // https://stackoverflow.com/questions/13341560/how-to-create-a-custom-dialog-box-in-android
        Dialog winDialog = new Dialog(GameActivity.this);
        winDialog.setContentView(R.layout.dialog_game_win);
        winDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        winDialog.setCancelable(false);
        winDialog.setCanceledOnTouchOutside(false);

        winDialog.show();


        Button closeButton = winDialog.findViewById(R.id.btnExitGame);
        closeButton.setOnClickListener( view -> {
            // Save the game in game manager
            gameManager.addGame(currentGame);
            finish();
        });
    }

    private void updateHighScoreText() {
        Game highScoreGame = GameManager.getInstance().findBestScoringGame(rowNumber,columnNumber,mineCount);
        String highScoreString = "High Score: ";
        if (highScoreGame == null) {
            highScoreString += "None";
        } else {
            highScoreString += Integer.toString(highScoreGame.getScans());
        }

        TextView highScoreTextView = findViewById(R.id.tvHighScore);
        highScoreTextView.setText(highScoreString);
    }



}