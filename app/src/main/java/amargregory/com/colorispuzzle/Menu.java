package amargregory.com.colorispuzzle;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ToggleButton;

public class Menu extends AppCompatActivity {
    private Button mButton, buttonPlay;
    ToggleButton Toggle1;


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
        Toggle1 =(ToggleButton)findViewById(R.id.toggleButton);
        final MediaPlayer mp=MediaPlayer.create(this, R.raw.son);
        Toggle1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Toggle1.isChecked())
                {
                    mp.start();
                }
                else {
                    mp.pause();
                }

            }
        });
    }
}
