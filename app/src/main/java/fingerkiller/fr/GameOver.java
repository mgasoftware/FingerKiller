package fingerkiller.fr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class GameOver extends AppCompatActivity {

    Animation topAnim, bottomAnim;
    ImageView gameOverText;
    Button resetGame;
    TextView highScore;
    int score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game_over);

        SharedPreferences bestScore = this.getSharedPreferences("HighScore", Context.MODE_PRIVATE);
        score = bestScore.getInt("score", 0);

        topAnim = AnimationUtils.loadAnimation(this, R.anim.topanimation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottomanimation);

        gameOverText = findViewById(R.id.imageView);
        resetGame = findViewById(R.id.button3);
        highScore = findViewById(R.id.highScore);

        gameOverText.setAnimation(topAnim);
        resetGame.setAnimation(bottomAnim);

        resetGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gameActivity = new Intent(getApplicationContext(), MainGame.class);
                startActivity(gameActivity);
                finish();
            }
        });

        highScore.setTextSize(20);
        highScore.setTypeface(highScore.getTypeface(), Typeface.BOLD);
        highScore.setText("High Score: " + score);
    }
}
