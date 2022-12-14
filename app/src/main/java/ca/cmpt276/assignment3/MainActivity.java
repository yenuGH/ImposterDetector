package ca.cmpt276.assignment3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import ca.cmpt276.assignment3.activities.WelcomeScreenActivity;

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