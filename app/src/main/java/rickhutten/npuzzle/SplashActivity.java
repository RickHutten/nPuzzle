package rickhutten.npuzzle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;


public class SplashActivity extends Activity {

    // Duration of splash screen in milliseconds
    final int splash_time = 2000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView splash_view = (ImageView)findViewById(R.id.img_splash);
        splash_view.setImageResource(R.drawable.splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Make intent that will start MainActivity
                Intent main_intent = new Intent(SplashActivity.this, MainActivity.class);
                SplashActivity.this.startActivity(main_intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                SplashActivity.this.finish();
            }
        }, splash_time);
    }

    @Override
    public void onBackPressed() {}
}