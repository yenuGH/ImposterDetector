package ca.cmpt276.assignment3.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import ca.cmpt276.assignment3.R;

public class OptionsMenuActivity extends AppCompatActivity {

    // 5.3 - Use SharedPreferences to save options data between application runs
    // Use Google Gson for this? Game proprties may use LocalDateTime which requires Google Gson for TypeAdapter.

    // 5.4 - Button for resetting game data for each configuration

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options_menu);
    }
}