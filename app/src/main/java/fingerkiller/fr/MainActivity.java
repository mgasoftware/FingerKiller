package fingerkiller.fr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private ConstraintLayout myLayout;
    TextView highScore;
    int score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activitymain2);

        SharedPreferences bestScore = this.getSharedPreferences("HighScore", Context.MODE_PRIVATE);
        score = bestScore.getInt("score", 0);

        this.myLayout = findViewById(R.id.myDynamicLayout);
        highScore = findViewById(R.id.highScoreMenu);

        final Button startButton = findViewById(R.id.button2);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent otherActivity = new Intent(getApplicationContext(), MainGame.class);
                Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.bounce);
                startButton.startAnimation(animation);
                startActivity(otherActivity);
                finish();
            }
        });

        highScore.setTextSize(20);
        highScore.setTypeface(highScore.getTypeface(), Typeface.BOLD);
        highScore.setText("High Score: " + score);
    }
}
