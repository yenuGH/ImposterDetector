package ca.cmpt276.assignment3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import ca.cmpt276.assignment3.activities.MainMenuActivity;

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