package bochhuang2.scm.pumpit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

public class game extends AppCompatActivity {
    PumpIt pumpIt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pumpIt = new PumpIt(this);
        setContentView(pumpIt);

        getSupportActionBar().hide(); // hide Title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);   // full screen
    }

    @Override
    protected void onPause() {
        super.onPause();
        pumpIt.Destroy();
    }
}
