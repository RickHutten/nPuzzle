package rickhutten.npuzzle;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
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

        ImageView image_view = (ImageView)findViewById(R.id.image_whole);
        image_view.setImageResource(image);

        TextView text_move = (TextView)findViewById(R.id.txt_move_count);
        text_move.setText(getResources().getText(R.string.moves) + "\n" + move_count);

        TextView text_time = (TextView)findViewById(R.id.txt_time);
        text_time.setText(getResources().getText(R.string.time) +  "\n" + time);

    }
}
