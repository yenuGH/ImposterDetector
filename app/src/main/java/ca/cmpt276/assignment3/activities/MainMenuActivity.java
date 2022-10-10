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

        setupPlayButton();
        setupHelpButton();
        setupOptionsButton();

    }

    // When the back button is pressed while on the main menu screen
    // Just exit the app c:
    @Override
    public void onBackPressed(){
        finishAffinity();
    }

    private void setupPlayButton(){
        ImageButton playButton = findViewById(R.id.ibPlayButton);
        // setup intent for playing a new game
    }
    private void setupHelpButton(){
        ImageButton helpButton = findViewById(R.id.ibHelpButton);
    }
    private void setupOptionsButton(){
        ImageButton optionsButton = findViewById(R.id.ibHelpButton);
    }

}