package ca.cmpt276.assignment3.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;

import ca.cmpt276.assignment3.MainActivity;
import ca.cmpt276.assignment3.R;

public class WelcomeScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);

        Button skipWelcome = findViewById(R.id.btnSkipWelcome);
        skipWelcome.setOnClickListener( view -> {
            launchMainMenu();
            finish();
        });

        new Handler().postDelayed(() -> {
            launchMainMenu();
            finish();
        }, 10000);

    }

    private void launchMainMenu(){
        Intent mainMenu = new Intent(WelcomeScreenActivity.this, MainMenuActivity.class);
        startActivity(mainMenu);
    }
}