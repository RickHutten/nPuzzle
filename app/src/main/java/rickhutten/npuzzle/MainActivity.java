package rickhutten.npuzzle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.view.Display;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.*;

import org.w3c.dom.Text;

public class MainActivity extends Activity implements View.OnClickListener {

    Button button_start;
    SeekBar seekBar;
    TextView TextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button_start = (Button)findViewById(R.id.button_start);
        button_start.setOnClickListener(this);

        // Seekbar width 2/3 of screen width
        seekBar = (SeekBar)findViewById(R.id.seekBar);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)seekBar.getLayoutParams();
        seekBar.setProgress(1);

        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics ();
        display.getMetrics(outMetrics);

        params.width = (int)(outMetrics.widthPixels / 1.5);
        seekBar.setLayoutParams(params);

        // Textview of difficulty
        TextView = (TextView)(findViewById(R.id.setDifficulty));
        switch (Integer.parseInt(String.valueOf(seekBar.getProgress())))
        {
            case 0:
                TextView.setText("Easy");
                break;
            case 1:
                TextView.setText("Medium");
                break;
            case 2:
                TextView.setText("Hard");
                break;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void buttonStartClick() {
        startActivity(new Intent("android.intent.action.GAME"));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.button_start:
                /*buttonStartClick();
                break;*/
                switch (Integer.parseInt(String.valueOf(seekBar.getProgress()))) {
                    case 0:
                        TextView.setText("Easy");
                        break;
                    case 1:
                        TextView.setText("Medium");
                        break;
                    case 2:
                        TextView.setText("Hard");
                        break;
                }
        }
    }
}
