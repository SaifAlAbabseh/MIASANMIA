package com.example.workway;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;

public class MainActivity extends AppCompatActivity {
    public static Class ToWhere=Intro.class;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        StatusBarColor.ChangeColor(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        Handler h=new Handler();
        h.postDelayed(new Runnable(){
            public void run(){
                finish();
                startActivity(new Intent(MainActivity.this,ToWhere));
            }
        },2000);
    }
}