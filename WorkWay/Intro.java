package com.example.workway;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

class StatusBarColor{
    public static void ChangeColor(Activity act){//To change the status bar background color...
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            act.getWindow().setStatusBarColor(act.getResources().getColor(R.color.ProgressBarColor, act.getTheme()));
        }
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            act.getWindow().setStatusBarColor(act.getResources().getColor(R.color.ProgressBarColor));
        }
    }
}
public class Intro extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        StatusBarColor.ChangeColor(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_intro);
    }
    public void GoToLogin(View v){
        finish();
        startActivity(new Intent(this,Login.class));
    }
    public void GoToSignUp(View v) {
        finish();
        startActivity(new Intent(this, SignUp.class));
    }
    public void GoToAboutUs(View v)
    {
        finish();
        startActivity(new Intent(this, AboutUs.class));
    }
    public void GoToContactUs(View v){
        finish();
        startActivity(new Intent(this,ContactUs.class));
    }
}