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

    private static final int difficulty_easy = 0;
    private static final int difficulty_normal = 49;
    private static final int difficulty_hard = 99;

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
                setDifficultyText(difficulty_easy);
            } else if (24 <= progress && progress < 74) {
                setDifficultyText(difficulty_normal);
            } else if (74 <= progress && progress <= 99) {
                setDifficultyText(difficulty_hard);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) { }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            int progress = seekBar.getProgress();
            if (0 <= progress && progress < 25) {
                seekBar.setProgress(0);
                if (orig_difficulty.equals("easy")) {
                    button_change_difficulty.setEnabled(false);
                } else {
                    button_change_difficulty.setEnabled(true);
                }
            } else if (25 <= progress && progress < 75) {
                seekBar.setProgress(49);
                if (orig_difficulty.equals("normal")) {
                    button_change_difficulty.setEnabled(false);
                } else {
                    button_change_difficulty.setEnabled(true);
                }
            } else if (75 <= progress && progress < 100) {
                seekBar.setProgress(99);
                if (orig_difficulty.equals("hard")) {
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

        seekbar = (SeekBar)findViewById(R.id.seekbar);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)seekbar.getLayoutParams();

        textview = (TextView)findViewById(R.id.set_difficulty);
        if (orig_difficulty.equals("easy")) {
            orig_difficulty_int = difficulty_easy;
        } else if (orig_difficulty.equals("normal")) {
            orig_difficulty_int = difficulty_normal;
        } else if (orig_difficulty.equals("hard")) {
            orig_difficulty_int = difficulty_hard;
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
                        difficulty = "easy";
                        break;
                    case 49:
                        difficulty = "normal";
                        break;
                    case 99:
                        difficulty = "hard";
                        break;
                }
                Intent intent = new Intent(ChangeDifficultyActivity.this, GameActivity.class);
                intent.putExtra("difficulty", difficulty);
                intent.putExtra("image", image);

                /* Close the previous activity if starting a new game with
                a different difficulty. */
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                ChangeDifficultyActivity.this.startActivity(intent);
                overridePendingTransition(R.anim.fade_in_scale, 0);

                ChangeDifficultyActivity.this.finish();
            }
        });
    }

    private void setDifficultyText(int progress) {
        switch (progress)
        {
            case 0:
                textview.setText(R.string.easy);
                break;
            case 49:
                textview.setText(R.string.normal);
                break;
            case 99:
                textview.setText(R.string.hard);
                break;
        }
    }
}
