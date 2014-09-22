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

        ImageView splash_view = (ImageView)findViewById(R.id.imgSplash);
        splash_view.setImageResource(R.drawable.splash2);

        // Handler to start the MainActivity after splash_time time
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                // Make intent that will start MainActivity
                Intent main_intent = new Intent("android.intent.action.SETTING");
                SplashActivity.this.startActivity(main_intent);
                SplashActivity.this.finish();
            }
        }, splash_time);
    }
}