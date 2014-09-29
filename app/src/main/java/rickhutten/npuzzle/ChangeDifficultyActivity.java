package rickhutten.npuzzle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;


public class ChangeDifficultyActivity extends Activity {

    TextView textview;
    SeekBar seekbar;
    String difficulty;
    String orig_difficulty;
    int orig_difficulty_int;
    int image;
    Button button_change_difficulty;

    SeekBar.OnSeekBarChangeListener l = new SeekBar.OnSeekBarChangeListener() {
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
                if (orig_difficulty.equals("Easy")) {
                    button_change_difficulty.setEnabled(false);
                } else {
                    button_change_difficulty.setEnabled(true);
                }
            } else if (25 <= progress && progress < 75) {
                seekBar.setProgress(49);
                if (orig_difficulty.equals("Normal")) {
                    button_change_difficulty.setEnabled(false);
                } else {
                    button_change_difficulty.setEnabled(true);
                }
            } else if (75 <= progress && progress < 100) {
                seekBar.setProgress(99);
                if (orig_difficulty.equals("Hard")) {
                    button_change_difficulty.setEnabled(false);
                } else {
                    button_change_difficulty.setEnabled(true);
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_difficulty);

        Bundle extras = getIntent().getExtras();
        orig_difficulty = extras.getString("difficulty");
        image = extras.getInt("image");

        seekbar = (SeekBar)findViewById(R.id.seekbar2);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)seekbar.getLayoutParams();

        textview = (TextView)findViewById(R.id.setDifficulty2);
        if (orig_difficulty.equals("Easy")) {
            orig_difficulty_int = 0;
        } else if (orig_difficulty.equals("Normal")) {
            orig_difficulty_int = 49;
        } else if (orig_difficulty.equals("Hard")) {
            orig_difficulty_int = 99;
        }
        seekbar.setProgress(orig_difficulty_int);
        setDifficultyText(orig_difficulty_int);

        seekbar.setOnSeekBarChangeListener(l);

        // Seekbar width 2/3 of screen width
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics ();
        display.getMetrics(outMetrics);
        params.width = (int)(outMetrics.widthPixels / 1.5);
        seekbar.setLayoutParams(params);

        button_change_difficulty = (Button)findViewById(R.id.button_change_difficulty);
        button_change_difficulty.setEnabled(false);
        button_change_difficulty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (seekbar.getProgress()) {
                    case 0:
                        difficulty = "Easy";
                        break;
                    case 49:
                        difficulty = "Normal";
                        break;
                    case 99:
                        difficulty = "Hard";
                        break;
                }
                Intent intent = new Intent(ChangeDifficultyActivity.this, GameActivity.class);
                intent.putExtra("Difficulty", difficulty);
                intent.putExtra("Image", image);

                /* Close the previous activity if starting a new game with
                a different difficulty. */
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                ChangeDifficultyActivity.this.startActivity(intent);
                ChangeDifficultyActivity.this.finish();
            }
        });
    }

    private void setDifficultyText(int progress) {
        switch (progress)
        {
            case 0:
                textview.setText(R.string.Easy);
                break;
            case 49:
                textview.setText(R.string.Normal);
                break;
            case 99:
                textview.setText(R.string.Hard);
                break;
        }
    }

}
