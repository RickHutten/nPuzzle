package rickhutten.npuzzle;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;


public class WinActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win);

        Bundle extras = getIntent().getExtras();
        int image = extras.getInt("Image");
        int move_count = extras.getInt("Moves");
        String time = extras.getString("Time");

        ImageView image_view = (ImageView)findViewById(R.id.image_whole);
        image_view.setImageResource(image);

        TextView text_move = (TextView)findViewById(R.id.txt_move_count);
        text_move.setText("Moves\n" + move_count);

        TextView text_time = (TextView)findViewById(R.id.txt_time);
        text_time.setText("Time\n" + time);

    }

/*
 *   @Override
 *   public boolean onCreateOptionsMenu(Menu menu) {
 *       // Inflate the menu; this adds items to the action bar if it is present.
 *       getMenuInflater().inflate(R.menu.win, menu);
 *       return true;
 *   }
 *
 *   @Override
 *   public boolean onOptionsItemSelected(MenuItem item) {
 *       // Handle action bar item clicks here. The action bar will
 *       // automatically handle clicks on the Home/Up button, so long
 *       // as you specify a parent activity in AndroidManifest.xml.
 *       int id = item.getItemId();
 *       if (id == R.id.action_settings) {
 *           return true;
 *       }
 *       return super.onOptionsItemSelected(item);
 *   }
 */
}
