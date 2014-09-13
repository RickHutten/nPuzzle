package rickhutten.npuzzle;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;


public class SplashActivity extends Activity {

    // Duration of splash screen in milliseconds
    final int splash_time = 2000;
    Bitmap bitmap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView splash_view = (ImageView)findViewById(R.id.imgSplash);
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.splash2);
        splash_view.setImageBitmap(bitmap);

        // Handler to start the MainActivity after SPLASH_DURATION time
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                bitmap.recycle();
                bitmap = null;
                System.gc();
                // Make intent that will start MainActivity
                Intent main_intent = new Intent("android.intent.action.SETTING");
                SplashActivity.this.startActivity(main_intent);
                SplashActivity.this.finish();
            }
        }, splash_time);
    }
}