package rickhutten.npuzzle;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.SeekBar;


public class MainActivity extends Activity {

    Button button_start;
    SeekBar seekbar;
    TextView textview;
    Intent intent = new Intent("android.intent.action.GAME");
    ImageView img_flower;
    ImageView img_ice;
    ImageView img_cupcake;
    ImageView img_manhattan;
    String Difficulty;

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
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        System.gc();

        ActionBar actionbar = getActionBar();
        ColorDrawable color_drawable = new ColorDrawable(getResources().getColor(R.color.orange));
        if (actionbar != null) {
            actionbar.setBackgroundDrawable(color_drawable);
        }

        // Declare images and set OnClickListener
        img_flower = (ImageView)findViewById(R.id.imgFlower);
        img_ice = (ImageView)findViewById(R.id.imgIce);
        img_cupcake = (ImageView)findViewById(R.id.imgCupcake);
        img_manhattan = (ImageView)findViewById(R.id.imgManhattan);

        img_flower.setOnClickListener(c);
        img_ice.setOnClickListener(c);
        img_cupcake.setOnClickListener(c);
        img_manhattan.setOnClickListener(c);

        // Button to start the game and add an OnClickListener
        button_start = (Button)findViewById(R.id.button_start);
        button_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonStartClick();
            }
        });

        seekbar = (SeekBar)findViewById(R.id.seekbar);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)seekbar.getLayoutParams();
        seekbar.setProgress(49);

        // Textview of difficulty
        textview = (TextView)(findViewById(R.id.setDifficulty));
        setDifficultyText(49);

        seekbar.setOnSeekBarChangeListener(l);

        // Seekbar width 2/3 of screen width
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics ();
        display.getMetrics(outMetrics);
        params.width = (int)(outMetrics.widthPixels / 1.5);
        seekbar.setLayoutParams(params);
    }

    // Go to game activity
    private void buttonStartClick() {
        switch (seekbar.getProgress()) {
            case 0:
                Difficulty = "Easy";
                break;
            case 49:
                Difficulty = "Normal";
                break;
            case 99:
                Difficulty = "Hard";
                break;
        }

        intent.putExtra("Difficulty", Difficulty);
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
        switch (view.getId())
        {
            case R.id.imgFlower:
                button_start.setEnabled(true);
                img_flower.setAlpha((float)1);
                img_ice.setAlpha((float)0.5);
                img_cupcake.setAlpha((float)0.5);
                img_manhattan.setAlpha((float)0.5);
                img_flower.setSelected(true);
                img_ice.setSelected(false);
                img_cupcake.setSelected(false);
                img_manhattan.setSelected(false);
                break;

            case R.id.imgIce:
                button_start.setEnabled(true);
                img_ice.setAlpha((float)1);
                img_flower.setAlpha((float)0.5);
                img_cupcake.setAlpha((float)0.5);
                img_manhattan.setAlpha((float)0.5);
                img_flower.setSelected(false);
                img_ice.setSelected(true);
                img_cupcake.setSelected(false);
                img_manhattan.setSelected(false);
                break;

            case R.id.imgCupcake:
                button_start.setEnabled(true);
                img_cupcake.setAlpha((float)1);
                img_ice.setAlpha((float)0.5);
                img_flower.setAlpha((float)0.5);
                img_manhattan.setAlpha((float)0.5);
                img_flower.setSelected(false);
                img_ice.setSelected(false);
                img_cupcake.setSelected(true);
                img_manhattan.setSelected(false);
                break;

            case R.id.imgManhattan:
                button_start.setEnabled(true);
                img_manhattan.setAlpha((float)1);
                img_ice.setAlpha((float)0.5);
                img_cupcake.setAlpha((float)0.5);
                img_flower.setAlpha((float)0.5);
                img_flower.setSelected(false);
                img_ice.setSelected(false);
                img_cupcake.setSelected(false);
                img_manhattan.setSelected(true);
                break;
        }

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
