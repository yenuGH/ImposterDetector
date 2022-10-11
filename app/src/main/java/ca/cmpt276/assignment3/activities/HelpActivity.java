package ca.cmpt276.assignment3.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import ca.cmpt276.assignment3.R;

/**
 * Class containing the help activity, updates the text inside the help activity's
 * textview.
 */
public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        // Get the textview containing the text
        TextView textView = findViewById(R.id.tvHelpText);

        // Make links in the textview clickable
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }
}