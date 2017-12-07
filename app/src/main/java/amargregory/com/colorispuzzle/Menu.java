package amargregory.com.colorispuzzle;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

public class Menu extends AppCompatActivity {
    private Button mButton, buttonPlay;
    ToggleButton Toggle1;
    EditText actualScore;
    TextView bestScore;
    TextView actualScore1;
    int score;
    int score1;
    int highscore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        mButton = (Button) findViewById(R.id.button);
        buttonPlay = (Button) findViewById(R.id.boutonPlay);
        mButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Intent intent = new Intent(Menu.this, About.class);
                startActivity(intent);
            }
        });

        buttonPlay.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Intent intent = new Intent(Menu.this, ColorisPuzzle.class);
                startActivity(intent);
            }
        });
        Toggle1 = (ToggleButton) findViewById(R.id.toggleButton);
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.son);
        Toggle1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Toggle1.isChecked()) {
                    mp.start();
                } else {
                    mp.pause();
                }

            }
        });

        actualScore = (EditText) findViewById(R.id.actualScore);
        bestScore = (TextView) findViewById(R.id.bestScore);
        actualScore1 = (TextView) findViewById(R.id.actualScore1);
        }





    public void displayScore (View view) {
        score1= Integer.parseInt(actualScore1.getText().toString());
        highscore=Integer.parseInt(bestScore.getText().toString());
        SharedPreferences sharedPref = getSharedPreferences("bestScore", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("actualScore", actualScore.getText().toString());
        editor.apply();

        String score = sharedPref.getString("actualScore", "");
        actualScore1.setText(score);
        if (score1 > highscore) {
            bestScore.setText(score);
        }

    }

}



