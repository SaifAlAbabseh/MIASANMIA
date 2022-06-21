package com.example.workway;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


public class ContactUs extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        StatusBarColor.ChangeColor(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_contact_us);
    }
    public void GoBack(View v){
        finish();
        startActivity(new Intent(this,Intro.class));
    }
    public void SendConcern(View v){
        EditText Concern=(EditText)findViewById(R.id.ContactUserConcern);
        EditText Email=(EditText)findViewById(R.id.ContactUserEmail);
        if(Concern.getText().toString().trim().length()>0 && Email.getText().toString().trim().length()>0){
            startActivity(new Intent(ContactUs.this,TempLoading.class));
            JavaMailAPI mail=new JavaMailAPI(this,"workway.j0@gmail.com","A concern from : "+Email.getText().toString().trim(),"His concern : \n\n"+Concern.getText().toString());
            mail.execute();
            Concern.setText("");
            Email.setText("");
            Handler h=new Handler();
            h.postDelayed(new Runnable(){
                public void run(){
                    Toast.makeText(ContactUs.this,"Thanks for contacting us :)",Toast.LENGTH_SHORT).show();
                }
            },2000);
        }
        else{
            Toast.makeText(ContactUs.this,"Make sure fields are not empty",Toast.LENGTH_SHORT).show();
        }
    }
}