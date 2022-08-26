package com.example.lungcovid19api24;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

public class HomeActivity extends AppCompatActivity {

    private Handler handler;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home);
        getSupportActionBar().hide();

        handler = new Handler();

        runnable = new Runnable() {
            public void run() {
                Intent intent = new Intent(HomeActivity.this, MenuActivity.class);
                startActivity(intent);
                finish();
            }
        };
    }
    public void onResume() {
        super.onResume();
        handler.postDelayed(runnable, 5000);
    }

    public void onStop() {
        super.onStop();
        handler.removeCallbacks(runnable);
    }
}