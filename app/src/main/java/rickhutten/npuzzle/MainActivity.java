package rickhutten.npuzzle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.SeekBar;


public class MainActivity extends Activity {

    Button button_start;
    SeekBar seekBar;
    TextView TextView;
    Intent intent = new Intent("android.intent.action.GAME");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Button to start the game and add an OnClickListener
        button_start = (Button)findViewById(R.id.button_start);
        button_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonStartClick();
            }
        });

        // Seekbar width 2/3 of screen width
        seekBar = (SeekBar)findViewById(R.id.seekBar);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)seekBar.getLayoutParams();
        seekBar.setProgress(49);

        // Textview of difficulty
        TextView = (TextView)(findViewById(R.id.setDifficulty));
        setDifficultyText(49);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (0 <= progress && progress < 24) {
                    setDifficultyText(0);
                } else if (24 <= progress && progress < 74) {
                    setDifficultyText(49);
                } else if (74 <= progress && progress <= 99) {
                    setDifficultyText(99);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                if (0 <= progress && progress < 25) {
                    seekBar.setProgress(0);
                } else if (25 <= progress && progress < 75) {
                    seekBar.setProgress(49);
                } else if (75 <= progress && progress < 100) {
                    seekBar.setProgress(99);
                }
            }
        });

        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics ();
        display.getMetrics(outMetrics);

        params.width = (int)(outMetrics.widthPixels / 1.5);
        seekBar.setLayoutParams(params);
    }

    // Go to game activity
    private void buttonStartClick() {
        intent.putExtra("Difficulty", seekBar.getProgress());
        startActivity(intent);
    }

    private void setDifficultyText(int progress) {
        switch (progress)
        {
            case 0:
                TextView.setText(R.string.Easy);
                break;
            case 49:
                TextView.setText(R.string.Medium);
                break;
            case 99:
                TextView.setText(R.string.Hard);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }
}
