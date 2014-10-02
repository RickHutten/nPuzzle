package rickhutten.npuzzle;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

import android.widget.ImageView;
import android.widget.TextView;


public class WinActivity extends Activity {

    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win);

        System.gc();

        sharedpreferences = getSharedPreferences("prefs", Context.MODE_MULTI_PROCESS);
        sharedpreferences.edit().clear().apply();

        Bundle extras = getIntent().getExtras();
        int image = extras.getInt("image");
        int move_count = extras.getInt("moves");
        String time = extras.getString("time");

        final AnimationSet anim_set = new AnimationSet(false);
        RotateAnimation rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setInterpolator(new LinearInterpolator());
        rotate.setRepeatCount(Animation.INFINITE);
        rotate.setDuration(10000);
        anim_set.addAnimation(rotate);

        ImageView image_view = (ImageView) findViewById(R.id.image_whole);
        image_view.setImageResource(image);

        image_view.startAnimation(anim_set);

        image_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                anim_set.addAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.grow_n_shrink));
            }
        });

        TextView text_move = (TextView) findViewById(R.id.txt_move_count);
        text_move.setText(getResources().getText(R.string.moves) + "\n" + move_count);

        TextView text_time = (TextView) findViewById(R.id.txt_time);
        text_time.setText(getResources().getText(R.string.time) + "\n" + time);

        TextView text_play_again = (TextView) findViewById(R.id.play_again);
        text_play_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        this.finish();
        this.overridePendingTransition(R.anim.slide_in_left , R.anim.slide_out_right );
    }
}
