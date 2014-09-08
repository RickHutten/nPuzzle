package rickhutten.npuzzle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;


public class SplashActivity extends Activity {

    // Duration of splash screen in milliseconds
    final int SPLASH_DURATION = 1000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Handler to start the MainActivity after SPLASH_DURATION time
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                // Make intent that will start MainActivity
                Intent mainIntent = new Intent("android.intent.action.SETTING");
                SplashActivity.this.startActivity(mainIntent);
                SplashActivity.this.finish();
            }
        }, SPLASH_DURATION);
    }
}
// SplashActivity.this,MainActivity.class