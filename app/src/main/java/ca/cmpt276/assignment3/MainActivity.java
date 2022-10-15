package ca.cmpt276.assignment3;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import ca.cmpt276.assignment3.activities.GameActivity;
import ca.cmpt276.assignment3.activities.MainMenuActivity;
import ca.cmpt276.assignment3.activities.WelcomeScreenActivity;
import ca.cmpt276.assignment3.model.Game;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Starts the splash screen
        new Handler().postDelayed(() -> {
            Intent welcomeScreen = new Intent(MainActivity.this, WelcomeScreenActivity.class);
            startActivity(welcomeScreen);
        }, 0);

    }
}