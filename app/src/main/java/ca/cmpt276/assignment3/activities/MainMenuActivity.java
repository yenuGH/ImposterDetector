package ca.cmpt276.assignment3.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import ca.cmpt276.assignment3.R;
import ca.cmpt276.assignment3.model.Game;
import ca.cmpt276.assignment3.model.GameManager;
import ca.cmpt276.assignment3.model.GameOptions;

public class MainMenuActivity extends AppCompatActivity {

    public final String GAME_MANAGER_PREFERENCES = "Game Manager Preferences";
    public final String GAME_OPTION_PREFERENCES = "Game Option Preferences";

    GameOptions gameOptions;
    GameManager gameManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        setupMenuButtons();

        // Load all options and game data into game manager
        loadOptionsData();
        loadGamesList();

        // If game was in progress, it will resume
        checkGameInProgress();
    }

    private void checkGameInProgress() {
        SharedPreferences preferences = getSharedPreferences("Game Preferences", MODE_PRIVATE);
        String gameJSON = preferences.getString("GameJSON", "");

        if (gameJSON != "") {
            // Go straight to the game screen
            Intent gameIntent = new Intent(MainMenuActivity.this, GameActivity.class);
            startActivity(gameIntent);
        }
    }

    // Load options data from shared preferences
    private void loadOptionsData(){
        gameOptions = GameOptions.getInstance();
        SharedPreferences sharedPreferences = getSharedPreferences("Options Preferences", MODE_PRIVATE);
        Gson gson = new Gson();

        // Convert the shared preferences instance to type GameOptions
        String json = sharedPreferences.getString(GAME_OPTION_PREFERENCES, null);
        Type type = new TypeToken<GameOptions>() {}.getType();

        // If null, keep old instance
        if (json == null){
            return;
        }

        // Retrieve saved options data
        GameOptions tempGameOptions = gson.fromJson(json, type);
        gameOptions.setInstance(tempGameOptions);
    }

    // Load games list from shared preferences
    public void loadGamesList(){
        gameManager = GameManager.getInstance();

        SharedPreferences sharedPreferences = getSharedPreferences("Manager Preferences", MODE_PRIVATE);
        Gson gson = new Gson();

        // Convert the shared preferences instance to type GameManager
        String json = sharedPreferences.getString(GAME_MANAGER_PREFERENCES, null);
        Type type = new TypeToken<ArrayList<Game>>() {}.getType();

        // If null, keep old instance
        if (json == null){
            return;
        }

        // Set retrieved data if not null to game manager's game list
        gameManager.loadGameList(gson.fromJson(json, type));
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