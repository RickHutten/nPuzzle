package rickhutten.npuzzle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends Activity {

    int sleep_time = 2000;
    int image;
    String difficulty;
    RelativeLayout game_layout;
    TableLayout table_layout;
    View begin_image;
    int move_count;
    TextView text_move;
    TextView text_time;
    String time;
    int n; //Amount tiles per row/column

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            tileClick(view);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        move_count = 0;

        Bundle extras = getIntent().getExtras();
        difficulty = extras.getString("Difficulty");
        image = extras.getInt("Image");

        begin_image = setImage();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                deleteImage(begin_image);
                setN(difficulty);
                createBitmaps(image);
                createTableLayout();

                TableRow tablerow = (TableRow)findViewById(R.id.tablerow_time_moves);
                tablerow.setVisibility(View.VISIBLE);
                text_move = (TextView)findViewById(R.id.txt_move_count);
                text_move.setText("Moves\n" + move_count);

                text_time = (TextView)findViewById(R.id.txt_time);
                text_time.setText("Time\n00:00");

                setTimer();
            }
        }, sleep_time);
    }

    private void setTimer() {
        final long time_start = System.currentTimeMillis();
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        long time_now = System.currentTimeMillis();
                        int elapsed_time = (int)((time_now - time_start) / 1000);
                        time = parseTime(elapsed_time);
                        text_time.setText("Time\n" + time);
                    }
                });
            }
        }, 1000, 1000);
    }

    private String parseTime(int seconds) {
        int remaining_seconds = seconds % 60;
        int minutes = (seconds - (remaining_seconds)) / 60;
        String sec;
        String min;
        if (remaining_seconds < 10) {
            sec = "0" + remaining_seconds;
        } else {
            sec = "" + remaining_seconds;
        }
        if (minutes < 10) {
            min = "0" + minutes;
        } else {
            min = "" + minutes;
        }

        return min + ":" + sec;
    }

    public void createBitmaps(int image_id) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), image_id);
        int tile_width = bitmap.getWidth() / n;
        int tile_height = bitmap.getHeight() / n;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++){
                Bitmap cropped_image = Bitmap.createBitmap(bitmap, tile_width * j, tile_height * i, tile_width, tile_height);
                String file_name = "" + ((j + 1) * 100 + (i + 1));
                saveBitmap(cropped_image, file_name, getApplicationContext());
            }
        }
        bitmap.recycle();
    }

    public void saveBitmap(Bitmap bitmap, String file_name, Context context) {
        FileOutputStream output;
        try {
            output = context.openFileOutput(file_name, Context.MODE_PRIVATE);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, output);
            output.close();
        } catch (Exception e) {
            Log.i("Failed to save bitmap:", file_name);
            e.printStackTrace();
        }
    }

    public Bitmap loadBitmap(String file_name, Context context) {
        try{
            FileInputStream input = context.openFileInput(file_name);
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            input.close();
            return bitmap;
        } catch(Exception e){
            Log.i("Failed to load bitmap:", file_name);
            e.printStackTrace();
            return null;
        }
    }

    private void createTableLayout() {
        table_layout = (TableLayout)findViewById(R.id.table_layout);
        TableRow.LayoutParams lp_row = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT,(float)1);
        TableRow.LayoutParams lp_imageview = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT,(float)1);

        for (int i = 0; i < n; i++) {
            TableRow row = new TableRow(this);
            for (int j = 0; j < n; j++) {
                ImageView imageview = new ImageView(this);
                /* i.e. if n = 3, the imageviews will have id's according to their 'x0y' coordinates
                top left will have id 101, bottom left id 103, bottom right id 303 etc.*/
                imageview.setId((j + 1) * 100 + (i + 1));
                imageview.setImageBitmap(loadBitmap("" + imageview.getId(), getApplicationContext()));
                // Set tag to know which bitmap was placed in the imageview
                imageview.setTag("" + imageview.getId());
                lp_imageview.setMargins(5,5,5,5);
                if (i == (n-1) && j == (n-1)) {
                    imageview.setAlpha((float)0);
                }
                imageview.setLayoutParams(lp_imageview);
                imageview.setAdjustViewBounds(true);
                imageview.setOnClickListener(listener);
                row.addView(imageview);
            }

            row.setLayoutParams(lp_row);
            table_layout.addView(row);
        }
    }

    private void checkIfCompleted() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int id = (j + 1) * 100 + (i + 1);
                ImageView imageview = (ImageView)findViewById(id);
                int image_id = Integer.parseInt((String)imageview.getTag());

                if (id != image_id) {
                    // The game is not won
                    return;
                }
            }
        }
        // The game is won
        Intent win_intent = new Intent("android.intent.action.WIN");
        win_intent.putExtra("Image", image);
        win_intent.putExtra("Moves", move_count);
        win_intent.putExtra("Time", time);
        win_intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

        GameActivity.this.startActivity(win_intent);
        this.finish();
    }

    private void switchViews(String image_name, int id_new_image_holder, int id_pressed_view) {
        ImageView imageview_picture = (ImageView)findViewById(id_new_image_holder);
        imageview_picture.setImageBitmap(loadBitmap(image_name, getApplicationContext()));
        imageview_picture.setAlpha((float)1);
        imageview_picture.setTag(image_name);
        ImageView imageview_pressed = (ImageView)findViewById(id_pressed_view);
        imageview_pressed.setTag("" + (n * 100 + n));
        imageview_pressed.setAlpha((float)0);
        move_count++;
        text_move.setText("Moves\n" + move_count);
    }

    private void tileClick(View view) {
        if (view.getAlpha() == 1) {
            int view_id = view.getId();
            try {
                ImageView imageview_top = (ImageView)findViewById(view_id - 1);
                if (imageview_top.getAlpha() == 0) {
                    switchViews((String)view.getTag(), view_id - 1, view_id);
                }
            } catch (Exception e) {
                // There is no imageview on the top (border)
            }
            try {
                ImageView imageview_under = (ImageView)findViewById(view_id + 1);
                if (imageview_under.getAlpha() == 0) {
                    switchViews((String)view.getTag(), view_id + 1, view_id);
                }
            } catch (Exception e) {
                // There is no imageview on the bottom (border)
            }
            try {
                ImageView imageview_left = (ImageView)findViewById(view_id - 100);
                if (imageview_left.getAlpha() == 0) {
                    switchViews((String)view.getTag(), view_id - 100, view_id);
                }
            } catch (Exception e) {
                // There is no imageview on the left (border)
            }
            try {
                ImageView imageview_right = (ImageView)findViewById(view_id + 100);
                if (imageview_right.getAlpha() == 0) {
                    switchViews((String)view.getTag(), view_id + 100, view_id);
                }
            } catch (Exception e) {
                // There is no imageview on the right (border)
            }
            checkIfCompleted();
        }
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

        int padding_in_dp = 50;  // Padding = 50dp
        float scale = getResources().getDisplayMetrics().density;
        int padding_in_px = (int)(padding_in_dp * scale + (float)0.5); // Extra 0.5 for rounding

        image_view.setPadding(0,padding_in_px,0,0);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        image_view.setAdjustViewBounds(true);
        game_layout = (RelativeLayout)findViewById(R.id.GameLayout);
        game_layout.addView(image_view, 0, params);
        return image_view;
    }

    private void deleteImage(View view) {
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
        switch (item.getItemId()) {
            case R.id.menu_change_difficulty:
                Intent change_diff_intent = new Intent("android.intent.action.DIFFICULTY");
                change_diff_intent.putExtra("difficulty", difficulty);
                change_diff_intent.putExtra("Image", image);
                GameActivity.this.startActivity(change_diff_intent);
                return true;
            case R.id.menu_solution:
                Intent see_solution = new Intent("android.intent.action.SOLUTION");
                see_solution.putExtra("Image", image);
                GameActivity.this.startActivity(see_solution);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}