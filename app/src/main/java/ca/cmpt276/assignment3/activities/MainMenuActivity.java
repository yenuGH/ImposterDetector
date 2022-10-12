package ca.cmpt276.assignment3.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import ca.cmpt276.assignment3.R;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        setupMenuButtons();
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