package ca.cmpt276.assignment3.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import ca.cmpt276.assignment3.R;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        TextView textView = findViewById(R.id.tvHelpText);
        //textView.setText(R.string.help_about);
        textView.setMovementMethod(LinkMovementMethod.getInstance());


//

        // Make the textview clickable (https://stackoverflow.com/questions/2734270/how-to-make-links-in-a-textview-clickable)
//        textView.setMovementMethod(LinkMovementMethod.getInstance());
//        textView.setClickable(true);

//        String text = "<a href='http://www.google.com'> Google </a>";
//            textView.setText("Foo." + fromHtml(text));
    }
}