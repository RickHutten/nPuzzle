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
        img_flower = (ImageView) findViewById(R.id.imgFlower);
        img_ice = (ImageView) findViewById(R.id.imgIce);
        img_cupcake = (ImageView) findViewById(R.id.imgCupcake);
        img_manhattan = (ImageView) findViewById(R.id.imgManhattan);

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
        textview = (TextView) (findViewById(R.id.setDifficulty));
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
        intent.putExtra("Difficulty", difficulty);
        intent.putExtra("Image", image);
        intent.putExtra("preferences", true);

        startActivity(intent);
    }

    // Go to game activity
    private void buttonStartClick() {
        switch (seekbar.getProgress()) {
            case difficulty_easy:
                difficulty = "Easy";
                break;
            case difficulty_normal:
                difficulty = "Normal";
                break;
            case difficulty_hard:
                difficulty = "Hard";
                break;
        }
        Intent intent = new Intent(MainActivity.this, GameActivity.class);
        intent.putExtra("Difficulty", difficulty);
        intent.putExtra("preferences", false);
        if (img_flower.isSelected()) {
            intent.putExtra("Image", R.drawable.square_flower);
        } else if (img_ice.isSelected()) {
            intent.putExtra("Image", R.drawable.square_ice);
        } else if (img_cupcake.isSelected()) {
            intent.putExtra("Image", R.drawable.square_cupcake);
        } else if (img_manhattan.isSelected()) {
            intent.putExtra("Image", R.drawable.square_manhattan);
        }
        startActivity(intent);
    }

    private void selectImage(View view) {

        button_start.setEnabled(true);
        ImageView[] images = {img_flower, img_ice, img_cupcake, img_manhattan};
        int id_pressed = view.getId();

        for (int i = 0; i < images.length; i++) {
            if (images[i].getId() == id_pressed) {
                images[i].setAlpha((float) 1);
                images[i].setSelected(true);
            } else {
                images[i].setAlpha((float) 0.2);
                images[i].setSelected(false);
            }
        }
    }

    private void setDifficultyText(int progress) {
        switch (progress)
        {
            case difficulty_easy:
                textview.setText(R.string.Easy);
                break;
            case difficulty_normal:
                textview.setText(R.string.Normal);
                break;
            case difficulty_hard:
                textview.setText(R.string.Hard);
                break;
        }
    }

}

