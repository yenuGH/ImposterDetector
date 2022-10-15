package ca.cmpt276.assignment3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import ca.cmpt276.assignment3.activities.GameActivity;
import ca.cmpt276.assignment3.activities.MainMenuActivity;
import ca.cmpt276.assignment3.model.Game;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // The entry point to the main menu screen
        Intent mainMenu = new Intent(MainActivity.this, MainMenuActivity.class);
        startActivity(mainMenu);

    }
}