package rickhutten.npuzzle;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class GameActivity extends Activity {

    int sleep_time = 3000;
    int image;
    String difficulty;
    RelativeLayout game_layout;
    TableLayout table_layout;
    View begin_image;
    int n; //Amount tiles per row/column

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            setAlpha(view);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Bundle extras = getIntent().getExtras();
        difficulty = extras.getString("Difficulty");
        image = extras.getInt("Image");

        begin_image = setImage();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                delete_image(begin_image);
                setN(difficulty);
                createTableLayout();
            }
        }, sleep_time);

    }

    private void createTableLayout() {
        table_layout = (TableLayout)findViewById(R.id.table_layout);
        TableRow.LayoutParams lp_row = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT,(float)1);
        TableRow.LayoutParams lp_imageview = new TableRow.LayoutParams(0, 0,(float)1);

        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics out_metrics = new DisplayMetrics ();
        display.getMetrics(out_metrics);
        lp_imageview.width = (out_metrics.widthPixels / n);
        lp_imageview.setMargins(5,0,5,0);
        lp_imageview.height = (out_metrics.widthPixels / n) - (1 + ((5 - n) / 2)) * 10;

        for (int i = 0; i < n; i++) {
            TableRow row = new TableRow(this);
            for (int j = 0; j < n; j++) {
                ImageView imageview = new ImageView(this);
                /* i.e. if n = 3, the imageviews will have id's according to their 'x0y' coordinates
                top left will have id 101, bottom left id 103, bottom right id 303 etc.*/
                imageview.setId((j + 1) * 100 + (i + 1));
                imageview.setImageResource(R.drawable.square_manhattan_small);
                imageview.setLayoutParams(lp_imageview);
                imageview.setOnClickListener(listener);
                row.addView(imageview);
            }
            row.setLayoutParams(lp_row);
            table_layout.addView(row);
        }
    }

    private void setAlpha(View view) {
        view.setAlpha((float)0.5);
        TextView textView = (TextView)findViewById(R.id.textView2);
        textView.setText("" + view.getId());
    }

    private void setN(String difficulty) {
        if (difficulty.equals("Easy")) {
            n = 3;
        } else if (difficulty.equals("Normal")) {
            n = 4;
        } else if (difficulty.equals("Hard")) {
            n = 5;
        }
    }

    private View setImage() {
        System.gc();
        ImageView image_view = new ImageView(this);
        image_view.setImageResource(image);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);

        game_layout = (RelativeLayout)findViewById(R.id.GameLayout);
        game_layout.addView(image_view, 0, params);
        return image_view;
    }

    private void delete_image(View view) {
        game_layout.removeView(view);
        System.gc();
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
