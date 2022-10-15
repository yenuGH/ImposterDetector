package ca.cmpt276.assignment3.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;

import ca.cmpt276.assignment3.MainActivity;
import ca.cmpt276.assignment3.R;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        setupMenuButtons();

        SharedPreferences preferences = getSharedPreferences("Game Preferences", MODE_PRIVATE);
        String gameJSON = preferences.getString("GameJSON", "");

        if (gameJSON != "") {
            // Go straight to the game screen
            Intent gameIntent = new Intent(MainMenuActivity.this, GameActivity.class);
            startActivity(gameIntent);
        }
    }

    // When the back button is pressed while on the main menu screen
    // Just exit the app c:
    @Override
    public void onBackPressed(){
        finishAffinity();
    }

    private void setupMenuButtons() {
        setupImageButton(R.id.ibOptionsButton, OptionsMenuActivity.class);
        setupImageButton(R.id.ibHelpButton, HelpActivity.class);
        setupImageButton(R.id.ibPlayButton, GameActivity.class);
    }

    private void setupImageButton(int buttonID, Class<?> activityClass) {
        ImageButton button = findViewById(buttonID);
        Intent menu = new Intent(MainMenuActivity.this, activityClass);
        button.setOnClickListener( view -> {
            startActivity(menu);
        });
    }
}