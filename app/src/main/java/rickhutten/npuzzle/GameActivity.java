package rickhutten.npuzzle;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class GameActivity extends Activity {

    int image;
    String Difficulty;
    RelativeLayout GameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Bundle extras = getIntent().getExtras();
        Difficulty = extras.getString("Difficulty");
        image = extras.getInt("Image");
        ImageView ImageView = new ImageView(this);
        try {
            ImageView.setImageResource(image);
        } catch (OutOfMemoryError e ){
            Log.i("Out of Memory Error", "You got fucked in the ass.");
            System.gc();
            try {
                ImageView.setImageResource(image);
            } catch (OutOfMemoryError r ) {
                Log.i("Out of Memory Error", "You got fucked in the ass so badly it gonna hurt for a month.");
            }
        }
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);

        GameLayout = (RelativeLayout)findViewById(R.id.GameLayout);
        GameLayout.addView(ImageView, 0, params);

        Log.i("intent", "" + Difficulty);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }
}
