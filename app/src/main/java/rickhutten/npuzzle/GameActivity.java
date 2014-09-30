package rickhutten.npuzzle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends Activity {

    private static final int shuffle_amount = 20;

	int sleep_time = 2000;
	int image;
    int elapsed_time;
    int move_count;
    int n; //Amount tiles per row/column
	String difficulty;
    String time;
	Boolean save_state = true;
	Boolean preference;
    View begin_image;
	RelativeLayout game_layout;
	TableLayout table_layout;
	TextView text_move;
	TextView text_time;
	ArrayList<Integer> tile_setup = new ArrayList<Integer>();
	ArrayList<Bitmap> bitmap_array = new ArrayList<Bitmap>();
	ArrayList<ImageView> imageviews_array = new ArrayList<ImageView>();
    Timer timer;
	SharedPreferences sharedpreferences;

	View.OnClickListener listener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			tileClick(view);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i("onCreate", "onCreate GameActivity");
		setContentView(R.layout.activity_game);
		sharedpreferences = getSharedPreferences("prefs", Context.MODE_MULTI_PROCESS);

		Bundle extras = getIntent().getExtras();
		difficulty = extras.getString("Difficulty");
		image = extras.getInt("Image");
		preference = extras.getBoolean("preferences");


		if (!preference) {
			sharedpreferences.edit().clear().apply();
		}

		setN(difficulty);

		try {
			begin_image = setImage();
		} catch (OutOfMemoryError e) {
			Log.i("OutOfMemoryError, trying again.", "begin_image = setImage();");
			System.gc();
			begin_image = setImage();
		}

		createTableLayout();
		table_layout.setVisibility(View.INVISIBLE);

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				deleteImage(begin_image);

				table_layout.setVisibility(View.VISIBLE);


			}
		}, sleep_time);
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.i("onPauze", "onPauze GameActivity");
		if (save_state) {
			saveState();
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.i("onStop", "onStop GameActivity");
		if (save_state) {
			saveState();
		}

		for (int i = 0; i < bitmap_array.size(); i++) {
			bitmap_array.get(i).recycle();
		}
		for (int i = 0; i < imageviews_array.size(); i++) {
			ImageView imageview = imageviews_array.get(i);
			imageviews_array.remove(i);
			imageview.setImageDrawable(null);
		}
		Log.i("onStop", "Bitmaps recycled");
        timer.cancel();
		bitmap_array.clear();
		System.gc();
	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.i("onStart", "onStart GameActivity");
		System.gc();

		getArray();
		try {
			createBitmaps(image);
		} catch (OutOfMemoryError e) {
			Log.i("OutOfMemoryError, trying again.", "createBitmaps(image);");
			System.gc();
			createBitmaps(image);
		}
		Log.i("tile_setup", "" + tile_setup);
		setBitmapsInTableLayout(tile_setup);

        move_count = sharedpreferences.getInt("move_count", 0);

		TableRow tablerow = (TableRow)findViewById(R.id.tablerow_time_moves);
		tablerow.setVisibility(View.VISIBLE);
		text_move = (TextView)findViewById(R.id.txt_move_count);
		text_move.setText(getResources().getText(R.string.moves) + "\n" + move_count);

		text_time = (TextView)findViewById(R.id.txt_time);
        String elapsed_time = parseTime(sharedpreferences.getInt("elapsed_time", 0));
		text_time.setText(getResources().getText(R.string.time) +  "\n" + elapsed_time);

        setTimer();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Intent intent = new Intent(GameActivity.this, MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		this.startActivity(intent);
	}

	private void getArray() {
		if (sharedpreferences.contains("preference")){
			Log.i("getArray()", "sharedpreferences found");
			tile_setup.clear();
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					int imageview_id = (j + 1) * 100 + (i + 1);
					tile_setup.add(sharedpreferences.getInt("" + imageview_id, -1));
				}
			}
		} else {
			Log.i("getArray()", "No sharedpreferences found");
			makeArray();
		}

	}

	public void makeArray() {
		ArrayList<Integer> bitmaps_int_array = new ArrayList<Integer>();
		for (int i = 0; i < Math.pow(n, 2); i++) {
			bitmaps_int_array.add(i);
		}

		/*** Shuffle bitmaps_array ***/
		Random r = new Random();
        // Shuffle "shuffle_amount" times two images randomly
        // It must be an even amount for the puzzle to be able to be completed
		for (int i = 0; i < shuffle_amount; i++) {
            // The ".. - 1" excludes the blank tile
			int random_1 = r.nextInt(bitmaps_int_array.size() - 1);
			int random_2 = r.nextInt(bitmaps_int_array.size() - 1);
			while (random_1 == random_2) {
				// random_2 has to be different than random_1
				random_2 = r.nextInt(bitmaps_int_array.size() - 1);
			}
			int temp_value = bitmaps_int_array.get(random_1);
			bitmaps_int_array.set(random_1, bitmaps_int_array.get(random_2));
			bitmaps_int_array.set(random_2, temp_value);
		}

		for (int i = 0; i < bitmaps_int_array.size(); i++) {
			tile_setup.add(bitmaps_int_array.get(i));
		}
	}

	private void saveState() {
		sharedpreferences = getSharedPreferences("prefs", Context.MODE_MULTI_PROCESS);
		sharedpreferences.edit().clear().apply();
		sharedpreferences.edit().putBoolean("preference", true).apply();
		sharedpreferences.edit().putString("difficulty", difficulty).apply();
		sharedpreferences.edit().putInt("image", image).apply();
        sharedpreferences.edit().putInt("elapsed_time", elapsed_time).apply();
        sharedpreferences.edit().putInt("move_count", move_count).apply();
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				int imageview_id = (j + 1) * 100 + (i + 1);
				ImageView imageview = (ImageView)findViewById(imageview_id);
				int image_id = (Integer)imageview.getTag();
				sharedpreferences.edit().putInt("" + imageview.getId(), image_id).apply();
			}
		}
		Log.i("saveState()", "State saved succesfully");
	}

	private void setTimer() {

        Log.i("setTimer()", "Timer started");
        final int offset = sharedpreferences.getInt("elapsed_time", 0);

		final long time_start = System.currentTimeMillis();

		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						long time_now = System.currentTimeMillis();
						elapsed_time = (int) (((time_now - time_start) / 1000) + offset);
						time = parseTime(elapsed_time);
						text_time.setText(getResources().getText(R.string.time) +  "\n" + time);
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
        // Change bitmap configuration from ARGB_888 to RGB_565 to decrease bitmap size
        Bitmap bitmap_smaller = bitmap.copy(Bitmap.Config.RGB_565, true);
        bitmap.recycle();

		int tile_width = bitmap_smaller.getWidth() / n;
		int tile_height = bitmap_smaller.getHeight() / n;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++){
				Bitmap cropped_image = Bitmap.createBitmap(
                        bitmap_smaller, tile_width * j, tile_height * i, tile_width, tile_height);
				bitmap_array.add(cropped_image);
			}
		}
		bitmap_smaller.recycle();
	}

	private void setBitmapsInTableLayout(ArrayList<Integer> array) {
		// The array is in the form of {imageview_id_1, filename_1, imageview_id_2, filename_2,...}
		Log.i("setBitmapsInTableLayout: array", "" + array);
		int array_index = -1;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
                array_index++;
                ImageView imageview = (ImageView) findViewById((j + 1) * 100 + (i + 1));
                // Set tag to know which bitmap was placed in the imageview
                imageview.setTag(array.get(array_index));
                imageview.setImageBitmap(bitmap_array.get(array.get(array_index)));

                if ((Integer) imageview.getTag() == Math.pow(n, 2) - 1) {

                    imageview.setAlpha((float) 0.2);
                }

            }
        }
	}

	private void createTableLayout() {
        // This creates the tiles, does not put in the images
        table_layout = (TableLayout) findViewById(R.id.table_layout);
        TableRow.LayoutParams lp_row = new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, (float) 1);
        TableRow.LayoutParams lp_imageview = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, (float) 1);

        for (int i = 0; i < n; i++) {
            TableRow row = new TableRow(this);
            for (int j = 0; j < n; j++) {
                ImageView imageview = new ImageView(this);
                // i.e. if n = 3, the imageviews will have id's according to their 'x0y' coordinates
                // top left will have id 101, bottom left id 103, bottom right id 303 etc.
                imageview.setId((j + 1) * 100 + (i + 1));

                lp_imageview.setMargins(5, 5, 5, 5);

                imageview.setLayoutParams(lp_imageview);
                imageview.setAdjustViewBounds(true);
                imageview.setOnClickListener(listener);
                imageviews_array.add(imageview);
                row.addView(imageview);
            }
            row.setLayoutParams(lp_row);
            table_layout.addView(row);
        }
    }

	private void checkIfCompleted() {
        int count = -1;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                count++;
                int imageview_id = (j + 1) * 100 + (i + 1);
                ImageView imageview = (ImageView) findViewById(imageview_id);
                int image_id = (Integer) imageview.getTag();


                if (image_id != count) {
                    // The game is not won
                    return;
                }
            }
        }
        // The game is won
        Intent win_intent = new Intent(GameActivity.this, WinActivity.class);
        win_intent.putExtra("image", image);
        win_intent.putExtra("moves", move_count);
        win_intent.putExtra("time", time);
        win_intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        sharedpreferences.edit().clear().apply();
        save_state = false;
        GameActivity.this.startActivity(win_intent);
        this.finish();
    }

    private void switchViews(Integer image_name, int id_new_image_holder, int id_pressed_view) {
        Log.i("Switching views", "" + id_new_image_holder + " " + id_pressed_view);
        ImageView imageview_picture = (ImageView) findViewById(id_new_image_holder);
        ImageView imageview_pressed = (ImageView) findViewById(id_pressed_view);

        imageview_picture.setImageBitmap(bitmap_array.get(image_name));
        imageview_picture.setAlpha((float) 1);
        imageview_picture.setTag(image_name);

        imageview_pressed.setImageBitmap(bitmap_array.get(bitmap_array.size() - 1));
        imageview_pressed.setTag(bitmap_array.size() - 1);
        imageview_pressed.setAlpha((float) 0.2);

        move_count++;
        text_move.setText(getResources().getText(R.string.moves) + "\n" + move_count);
    }

    private void tileClick(View view) {
        if (view.getAlpha() == 1) {
            int view_id = view.getId();
            try {
                ImageView imageview_top = (ImageView) findViewById(view_id - 1);
                if (imageview_top.getAlpha() != 1) {
                    switchViews((Integer) view.getTag(), view_id - 1, view_id);
                }
            } catch (Exception e) {
                // There is no imageview on the top (border)
            }
            try {
                ImageView imageview_under = (ImageView) findViewById(view_id + 1);

                if (imageview_under.getAlpha() != 1) {
                    switchViews((Integer) view.getTag(), view_id + 1, view_id);
                }
            } catch (Exception e) {
                // There is no imageview on the bottom (border)
            }
            try {
                ImageView imageview_left = (ImageView) findViewById(view_id - 100);
                if (imageview_left.getAlpha() != 1) {
                    switchViews((Integer) view.getTag(), view_id - 100, view_id);
                }
            } catch (Exception e) {
                // There is no imageview on the left (border)
            }
            try {
                ImageView imageview_right = (ImageView) findViewById(view_id + 100);
                if (imageview_right.getAlpha() != 1) {
                    switchViews((Integer) view.getTag(), view_id + 100, view_id);
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
        int padding_in_px = (int) (padding_in_dp * scale + (float) 0.5); // Extra 0.5 for rounding

        image_view.setPadding(0, padding_in_px, 0, 0);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        image_view.setAdjustViewBounds(true);
        game_layout = (RelativeLayout) findViewById(R.id.GameLayout);
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
				Intent change_diff_intent = new Intent(GameActivity.this,
                        ChangeDifficultyActivity.class);
				change_diff_intent.putExtra("difficulty", difficulty);
				change_diff_intent.putExtra("image", image);
				GameActivity.this.startActivity(change_diff_intent);
				return true;
			case R.id.menu_solution:
				Intent see_solution = new Intent(GameActivity.this, SolutionActivity.class);
				see_solution.putExtra("image", image);
				GameActivity.this.startActivity(see_solution);
				return true;
			case R.id.quit:
				Intent quit_to_main = new Intent(GameActivity.this, MainActivity.class);
				quit_to_main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				GameActivity.this.startActivity(quit_to_main);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

}
