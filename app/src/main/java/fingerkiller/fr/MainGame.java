package fingerkiller.fr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Paint.Style;
import android.graphics.Color;

public class MainGame extends AppCompatActivity implements View.OnTouchListener {

    GameView ourSurfaceView;
    Bitmap swordIcon, back, fingerIcon;
    Paint paint = new Paint();
    TextView info;
    float x, y, sX, sY, sX2, sY2, sX3, sY3, sX4, sY4, sX5, sY5, fX, fY;
    int score, scoreSave;
    private float screenWidth, screenHeight;
    boolean verify, finger, afficheInfo;
    Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ourSurfaceView = new GameView(this);
        ourSurfaceView.setOnTouchListener(this);
        setContentView(ourSurfaceView);

        swordIcon = BitmapFactory.decodeResource(getResources(), R.drawable.sword);
        back = BitmapFactory.decodeResource(getResources(), R.drawable.background);
        fingerIcon = BitmapFactory.decodeResource(getResources(), R.drawable.finger);
        vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);

        x = y = fX = fY = score =  0;
        sY = sY2 = sY3 = sY4 = sY5 = -200;
        verify = afficheInfo = true;
        finger = false;

        paint.setColor(Color.WHITE);
        paint.setStyle(Style.FILL);
        paint.setTextSize(50);

        WindowManager wm = getWindowManager();
        Display disp = wm.getDefaultDisplay();
        Point size = new Point();
        disp.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;

        sX = sX2 = sX3 = sX4 = sX5 = (float)Math.random() * (screenWidth - 0) ;

        SharedPreferences bestScore = this.getSharedPreferences("HighScore", Context.MODE_PRIVATE);
        scoreSave = bestScore.getInt("score", 0);
    }

    @Override
    protected void onPause(){
        super.onPause();
        ourSurfaceView.pause();
    }

    @Override
    protected void onResume(){
        super.onResume();
        ourSurfaceView.resume();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        x = motionEvent.getX();
        y = motionEvent.getY();

        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_DOWN:

                Toast.makeText(getApplicationContext(), "Game Start", Toast.LENGTH_SHORT).show();
                finger = true;
                afficheInfo = false;
                ourSurfaceView.resume();
                break;

            case MotionEvent.ACTION_UP:

                Toast.makeText(getApplicationContext(), "Game Pause", Toast.LENGTH_SHORT).show();
                finger = false;
                afficheInfo = true;
                ourSurfaceView.pause();
                break;

            case MotionEvent.ACTION_MOVE:
                break;
        }
        return true;
    }

    public class GameView extends SurfaceView implements Runnable{

        SurfaceHolder ourHolder;
        Thread ourThread = null;
        boolean isRunning = false;

        public GameView(Context context){
            super(context);
            ourHolder = getHolder();
        }

        public void pause(){
            isRunning = false;
            if (verify == true){
                while (true){
                    try {
                        ourThread.join();
                    } catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    break;
                }
                ourThread = null;
            }
        }

        public void resume(){
            if (verify == true){
                isRunning = true;
                ourThread = new Thread(this);
                ourThread.start();
            }
        }

        @Override
        public void run() {

            while (isRunning){
                if ((!ourHolder.getSurface().isValid()))
                    continue;
                Canvas canvas = ourHolder.lockCanvas();
                Rect src = new Rect(0, 0, back.getWidth()-1, back.getHeight()-1);
                Rect dest = new Rect(0, 0,(int)screenWidth ,(int)screenHeight);
                canvas.drawBitmap(back, src, dest, null);
                if (finger){
                    fX = x - (fingerIcon.getWidth() / 2);
                    fY = y - (fingerIcon.getHeight() / 2);
                    canvas.drawBitmap(fingerIcon, fX, fY,null);
                }
                if (afficheInfo){
                    canvas.drawText("Hold The Screen To Start The Game", (screenWidth/8), (screenHeight/2), paint);
                }

                if (x != 0){
                    sY = sY + 20;
                    canvas.drawBitmap(swordIcon, sX, sY, null);
                    canvas.drawText("Score: " + String.valueOf(score), (screenWidth/3), 50, paint);
                    if (sY > (screenHeight + (screenHeight/4))){
                        sX = (float)Math.random() * (screenWidth - 10) ;
                        sY = -(screenHeight/4);
                        score ++;
                    }
                    if (score > 5){
                        sY2 = sY2 + 25;
                        canvas.drawBitmap(swordIcon, sX2, sY2, null);
                        if (sY2 > (screenHeight + (screenHeight/4))){
                            sX2 = x ;
                            sY2 = -(screenHeight/4);
                            score++;
                        }
                    }
                    if (score > 15){
                        sY3 = sY3 + 30;
                        canvas.drawBitmap(swordIcon, sX3, sY3, null);
                        if (sY3 > (screenHeight + (screenHeight/4))){
                            sX3 = (float)Math.random() * (screenWidth - 0) ;
                            sY3 = -(screenHeight/4);
                            score++;
                        }
                    }
                    if (score > 30){
                        sY4 = sY4 + 35;
                        canvas.drawBitmap(swordIcon, sX4, sY4, null);
                        if (sY4 > (screenHeight + (screenHeight/4))){
                            sX4 = (float)Math.random() * (screenWidth - 0) ;
                            sY4 = -(screenHeight/4);
                            score++;
                        }
                    }
                    if (score > 50){
                        sY5 = sY5 + 55;
                        canvas.drawBitmap(swordIcon, sX5, sY5, null);
                        if (sY5 > (screenHeight + (screenHeight/4))){
                            sX5 = (float)Math.random() * (screenWidth - 0) ;
                            sY5 = -(screenHeight/4);
                            score++;
                        }
                    }

                    if (x >= sX && x < (sX + swordIcon.getWidth()) && y >= sY && y < (sY + swordIcon.getHeight())){
                        verify = false;
                        vibrator.vibrate(1000);
                        if (score > scoreSave){
                            SharedPreferences bestScore = getSharedPreferences("HighScore", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = bestScore.edit();
                            editor.putInt("score", score);
                            editor.commit();
                        }
                        Intent tempActivity = new Intent(getApplicationContext(), GameOver.class);
                        startActivity(tempActivity);
                        finish();
                    }
                    if (x >= sX2 && x < (sX2 + swordIcon.getWidth()) && y >= sY2 && y < (sY2 + swordIcon.getHeight())){
                        verify = false;
                        if (score > scoreSave){
                            SharedPreferences bestScore = getSharedPreferences("HighScore", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = bestScore.edit();
                            editor.putInt("score", score);
                            editor.commit();
                        }
                        Intent tempActivity = new Intent(getApplicationContext(), GameOver.class);
                        startActivity(tempActivity);
                        finish();
                    }
                    if (x >= sX3 && x < (sX3 + swordIcon.getWidth()) && y >= sY3 && y < (sY3 + swordIcon.getHeight())){
                        verify = false;
                        if (score > scoreSave){
                            SharedPreferences bestScore = getSharedPreferences("HighScore", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = bestScore.edit();
                            editor.putInt("score", score);
                            editor.commit();
                        }
                        Intent tempActivity = new Intent(getApplicationContext(), GameOver.class);
                        startActivity(tempActivity);
                        finish();
                    }
                    if (x >= sX4 && x < (sX4 + swordIcon.getWidth()) && y >= sY4 && y < (sY4 + swordIcon.getHeight())){
                        verify = false;
                        if (score > scoreSave){
                            SharedPreferences bestScore = getSharedPreferences("HighScore", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = bestScore.edit();
                            editor.putInt("score", score);
                            editor.commit();
                        }
                        Intent tempActivity = new Intent(getApplicationContext(), GameOver.class);
                        startActivity(tempActivity);
                        finish();
                    }
                    if (x >= sX5 && x < (sX5 + swordIcon.getWidth()) && y >= sY5 && y < (sY5 + swordIcon.getHeight())){
                        verify = false;
                        if (score > scoreSave){
                            SharedPreferences bestScore = getSharedPreferences("HighScore", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = bestScore.edit();
                            editor.putInt("score", score);
                            editor.commit();
                        }
                        Intent tempActivity = new Intent(getApplicationContext(), GameOver.class);
                        startActivity(tempActivity);
                        finish();
                    }
                }
                ourHolder.unlockCanvasAndPost(canvas);
            }
        }
    }
}
