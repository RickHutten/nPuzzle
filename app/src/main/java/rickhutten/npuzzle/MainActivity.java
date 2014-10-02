package rickhutten.npuzzle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.SeekBar;


public class MainActivity extends Activity {

    private static final int difficulty_easy = 0;
    private static final int difficulty_normal = 49;
    private static final int difficulty_hard = 99;

    String difficulty;
    Button button_start;
    Button button_continue;
    SeekBar seekbar;
    TextView textview;
    ImageView img_flower;
    ImageView img_ice;
    ImageView img_cupcake;
    ImageView img_manhattan;
    SharedPreferences sharedpreferences;

    View.OnClickListener c = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            selectImage(view);
        }
    };

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
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            int progress = seekBar.getProgress();
            if (0 <= progress && progress < 25) {
                seekBar.setProgress(difficulty_easy);
            } else if (25 <= progress && progress < 75) {
                seekBar.setProgress(difficulty_normal);
            } else if (75 <= progress && progress < 100) {
                seekBar.setProgress(difficulty_hard);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        System.gc();

        // Declare images and set OnClickListener
        img_flower = (ImageView) findViewById(R.id.img_flower);
        img_ice = (ImageView) findViewById(R.id.img_ice);
        img_cupcake = (ImageView) findViewById(R.id.img_cupcake);
        img_manhattan = (ImageView) findViewById(R.id.img_manhattan);

        img_flower.setOnClickListener(c);
        img_ice.setOnClickListener(c);
        img_cupcake.setOnClickListener(c);
        img_manhattan.setOnClickListener(c);

        // Button to start the game and add an OnClickListener
        button_start = (Button) findViewById(R.id.button_start);
        button_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonStartClick();
            }
        });

        button_continue = (Button) findViewById(R.id.button_continue);
        button_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonContinueClick();
            }
        });

        seekbar = (SeekBar) findViewById(R.id.seekbar);
        RelativeLayout.LayoutParams params =
                (RelativeLayout.LayoutParams) seekbar.getLayoutParams();
        seekbar.setProgress(difficulty_normal);

        // Textview of difficulty
        textview = (TextView) (findViewById(R.id.set_difficulty));
        setDifficultyText(difficulty_normal);

        seekbar.setOnSeekBarChangeListener(l);

        // Seekbar width 2/3 of screen width
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        params.width = (int) (outMetrics.widthPixels / 1.5);
        seekbar.setLayoutParams(params);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("onResume", "onResume Mainactivity");
        sharedpreferences = getSharedPreferences("prefs", Context.MODE_MULTI_PROCESS);
        Button button_continue = (Button) findViewById(R.id.button_continue);
        if (sharedpreferences.contains("preference")) {
            Log.i("sharedPreferences found in main", "true");
            button_continue.setEnabled(true);
        } else {
            button_continue.setEnabled(false);
        }
    }

    private void buttonContinueClick() {
        Intent intent = new Intent(MainActivity.this, GameActivity.class);
        difficulty = sharedpreferences.getString("difficulty", "");
        int image = sharedpreferences.getInt("image", -1);
        intent.putExtra("difficulty", difficulty);
        intent.putExtra("image", image);
        intent.putExtra("preferences", true);

        startActivity(intent);
        this.overridePendingTransition(R.anim.slide_in_right , R.anim.slide_out_left );
    }

    // Go to game activity
    private void buttonStartClick() {
        switch (seekbar.getProgress()) {
            case difficulty_easy:
                difficulty = "easy";
                break;
            case difficulty_normal:
                difficulty = "normal";
                break;
            case difficulty_hard:
                difficulty = "hard";
                break;
        }
        Intent intent = new Intent(MainActivity.this, GameActivity.class);
        intent.putExtra("difficulty", difficulty);
        intent.putExtra("preferences", false);
        if (img_flower.isSelected()) {
            intent.putExtra("image", R.drawable.square_flower);
        } else if (img_ice.isSelected()) {
            intent.putExtra("image", R.drawable.square_ice);
        } else if (img_cupcake.isSelected()) {
            intent.putExtra("image", R.drawable.square_cupcake);
        } else if (img_manhattan.isSelected()) {
            intent.putExtra("image", R.drawable.square_manhattan);
        }
        startActivity(intent);
        this.overridePendingTransition(R.anim.slide_in_right , R.anim.slide_out_left );
    }

    private void selectImage(View view) {

        button_start.setEnabled(true);
        final ImageView[] images = {img_flower, img_ice, img_cupcake, img_manhattan};
        int id_pressed = view.getId();

        for (int i = 0; i < images.length; i++) {
            final int j = i;
            if (images[i].getId() == id_pressed) {
                if (images[i].getAlpha() != 1) {
                    Animation anim = AnimationUtils.loadAnimation(getApplicationContext(),
                            R.anim.fade_in_partially);
                    anim.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            // The animation wont wrok properly if not set to 1
                            images[j].setAlpha((float) 1);
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            images[j].setAlpha((float) 1);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }
                    });
                    images[i].startAnimation(anim);
                }
                images[i].setSelected(true);
            } else {
                if (images[i].getAlpha() == 1.0) {
                    Animation anim = AnimationUtils.loadAnimation(getApplicationContext(),
                            R.anim.fade_out_partially);
                    anim.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            // Probably a bug, but this seems to work
                            images[j].setAlpha((float) 0.99);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }
                    });
                    images[i].startAnimation(anim);
                }
                images[i].setSelected(false);
            }
        }
    }

    private void setDifficultyText(int progress) {
        switch (progress)
        {
            case difficulty_easy:
                textview.setText(R.string.easy);
                break;
            case difficulty_normal:
                textview.setText(R.string.normal);
                break;
            case difficulty_hard:
                textview.setText(R.string.hard);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        this.finish();
        this.overridePendingTransition(R.anim.slide_in_left , R.anim.slide_out_right );
    }
}
