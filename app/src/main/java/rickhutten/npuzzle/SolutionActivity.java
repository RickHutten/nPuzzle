package rickhutten.npuzzle;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;


public class SolutionActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solution);

        Bundle extras = getIntent().getExtras();
        int image = extras.getInt("image");

        ImageView image_view = (ImageView)findViewById(R.id.solution_view);
        image_view.setImageResource(image);
    }
}
