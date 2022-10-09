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

public class GameActivity extends AppCompatActivity {
    private static int NUM_ROWS = 4;
    private static int NUM_COLUMNS = 5;

    Button[][] buttons = new Button[NUM_ROWS][NUM_COLUMNS];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        populateButtons();
        updateText(5,42,0,0);
    }

    private void updateText(int foundMines, int totalMines, int scansUsed, int timesPlayed) {
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
                button.setText("" + col + "," + row);

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

    private void gridButtonClicked(int col, int row) {
        // Debug toast
        Toast.makeText(this, "Button clicked: " + col + "," + row, Toast.LENGTH_SHORT).show();

        // Get the button
        Button button = buttons[row][col];

        // Lock Button Sizes:
        lockButtonSizes();

        // Get the size of the button
        int width = button.getWidth();
        int height = button.getHeight();

        // Scale the bitmap using a bitmap factory
        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.action_tapped);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, width, height, true);

        // Import the resource
        Resources resource = getResources();
        button.setBackground(new BitmapDrawable(resource, scaledBitmap));

        // Debug clear the button text
        button.setText("Tapped!");
    }

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