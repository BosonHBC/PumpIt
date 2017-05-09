package bochhuang2.scm.pumpit;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //setContentView(new PumpIt(this));
        gameStart = false;
        getSupportActionBar().hide(); // hide Title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);   // full screen
    }


    boolean gameStart = false;

    @Override
    protected void onResume() {
        super.onResume();
        gameStart = false;

    }

    @Override
    protected void onPause() {
        super.onPause();
        gameStart = true;
    }

    public void StartGame(View v){
        if(!gameStart)
        {
            gameStart = true;
            // new activity
            Log.d("ss", "Button is ok");
            Intent i = new Intent(MainActivity.this,game.class);
            startActivity(i);


        }
    }

}
