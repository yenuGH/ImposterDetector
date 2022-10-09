package ca.cmpt276.assignment3.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import ca.cmpt276.assignment3.R;

/**
 * Activity where the game is played
 */
public class GameActivity extends AppCompatActivity {
    private static int NUM_ROWS = 6;
    private static int NUM_COLUMNS = 15;

    Button[][] buttons = new Button[NUM_ROWS][NUM_COLUMNS];

    private int numberOfGamesPlayed;
    private int scans = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        numberOfGamesPlayed = 3;

        populateButtons();
        updateGameText(5,42, scans, numberOfGamesPlayed);
    }

    /**
     * Update the game's text fields
     * @param foundMines Number of mines the player has found
     * @param totalMines Total number of mines on the game board
     * @param scansUsed Number of scans the player has used
     * @param timesPlayed Number of times the player has played a game
     */
    private void updateGameText(int foundMines, int totalMines, int scansUsed, int timesPlayed) {
        // Update the textview displaying the number of mines found/total
        String foundMinesString = String.format(getString(R.string.found_mines),foundMines,totalMines);
        updateTextView(R.id.tvFoundMines, foundMinesString);

        // Update the textview displaying the number of scans used
        String scansUsedString = String.format(getString(R.string.scans_used), scansUsed);
        updateTextView(R.id.tvScansUsed, scansUsedString);

        // Update the textview displaying the number of times a game has been played
        String timesPlayedString = String.format("Times Played: %d",timesPlayed);
        updateTextView(R.id.tvTimesPlayed, timesPlayedString);
    }

    /**
     * Updates the contents of a textview
     * @param textViewID Id of the textview
     * @param newString String to set the contents of the textview to
     */
    private void updateTextView(int textViewID, String newString) {
        TextView textView = findViewById(textViewID);
        textView.setText(newString);
    }

    /**
     * Setup/populate buttons formating and contents
     */
    private void populateButtons() {
        // Get the table containing the buttons
        TableLayout table = findViewById(R.id.tlButtons);

        // Populate the table with rows
        for (int row = 0; row < NUM_ROWS; row++) {
            // Create a new tablerow to add buttons to
            TableRow tableRow = new TableRow(this);

            // Scale the rows to fill the table
            tableRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT, 1.0f));

            // Add the row to the table
            table.addView(tableRow);

            // Populate the row with buttons
            for (int col = 0; col < NUM_COLUMNS; col++){
                // Create a new button, and add it to the row
                Button button = new Button(this);

                // Scale the buttons to fill the row
                button.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1.0f));

                // Debug
//                button.setText("" + col + "," + row);

                // Stop text from clipping on smaller buttons
                button.setPadding(0, 0, 0, 0);

                // Create final ints for passing which button has been pressed into the inner class
                final int FINAL_COLUMN = col;
                final int FINAL_ROW = row;

                // Create listener for when the button is pressed
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gridButtonClicked(FINAL_COLUMN, FINAL_ROW);
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
        for (int row = 0; row < NUM_ROWS; row++) {
            for (int col = 0; col < NUM_COLUMNS; col++) {
                // If a button has been scanned, update the number of mines inside it
                if (isButtonScanned(row, col)) {
                    Button button = buttons[row][col];
                    int mineCount = getMinesFromCell(row, col);
                    button.setText("" + mineCount);
                }
            }
        }
    }

    /**
     * Checks if a cell is a mine
     * @param row Row of the cell
     * @param col Column of the cell
     * @return True/False if the cell is a mine or not
     */
    private boolean isButtonMine(int row, int col) {
        // Todo: get if the button is a mine through the game model
        return false;
    }

    /**
     * Checks if a cell has been scanned
     * @param row Row of the cell
     * @param col Column of the cell
     * @return True/False if the cell has been scanned
     */
    private boolean isButtonScanned(int row, int col) {
        // Todo: get if the button has been scanned through game model
        return false;
    }

    /**
     * Gets the adjacent mines from a cell
     * @param row Row of the cell
     * @param col Column of the cell
     * @return Number of mines in the cell's row/column
     */
    private int getMinesFromCell(int row, int col) {
        // Todo: get the number of mines in adjacent cells through game model
        return -1;
    }



    /**
     * Handle a grid button being clicked
     * @param col The column the button was clicked in
     * @param row The row the button was clicked in
     */
    private void gridButtonClicked(int col, int row) {
        // Do nothing if the button has already been scanned
        if (isButtonScanned(col, row)) {
            return;
        }

        // Debug toast
        Toast.makeText(this, "Button clicked: " + col + "," + row, Toast.LENGTH_SHORT).show();

        // Get the button
        Button button = buttons[row][col];

        // Lock Button Sizes:
        lockButtonSizes();

        // If the button is unscaned, reveal the mine
        if (isButtonMine(row, col) && !isButtonScanned(row, col)) {
            // Get the size of the button
            int width = button.getWidth();
            int height = button.getHeight();

            // Scale the bitmap using a bitmap factory
            Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.action_tapped);
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, width, height, true);

            // Import the resource
            Resources resource = getResources();
            button.setBackground(new BitmapDrawable(resource, scaledBitmap));
        } else {
            performScan(row, col);
        }

        // Debug clear the button text
        button.setText("5");

        // Refresh the scanned results
        updateScannedButtons();
    }

    /**
     * Marks a cell as scanned
     * @param row Row of the cell that was scanned
     * @param col Column of the cell that was scanned
     */
    private void performScan(int row, int col) {
        scans ++;

        // Todo: Game logic here performing a scan
    }

    /**
     * Locks the size of a button to avoid changing scales when an image is
     * loaded/contents are changed
     */
    private void lockButtonSizes() {
        // Iterate through every button, lock their sizes
        for (int row = 0; row < NUM_ROWS; row++) {
            for (int col = 0; col < NUM_COLUMNS; col++) {
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

}