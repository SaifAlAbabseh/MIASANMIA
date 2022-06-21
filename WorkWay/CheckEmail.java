package com.example.workway;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

class AddUser{
    public String Email;
    public String Password;
    public int IfCVCreated;
}
public class CheckEmail extends AppCompatActivity {
    int DBCount=0;
    DatabaseReference reff;
    private String CodeForSecurity="";
    public static String userEmail="";
    public static Activity FromWhere=null;
    public static String ResetForNewPassword="";
    public static String SignUpPassword="";
    public static int ResetPassWhere;
    private void CreateCode(){
        CodeForSecurity="";
        for (int i=1;i<=5;++i) {
            if (i % 2 == 0) {
                CodeForSecurity += "" + (int) (Math.random() * 10);
            }
            if (i % 2 != 0) {
                CodeForSecurity += "" + (char) ((int) (Math.random() * 26) + 97);
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        StatusBarColor.ChangeColor(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_check_email);
        reff= FirebaseDatabase.getInstance().getReference().child("UserInfo");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    DBCount=(int)(snapshot.getChildrenCount());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private void startTimer(){
        new CountDownTimer(300000, 1000) {
            public void onTick(long millisUntilFinished) {}
            public void onFinish() {
                TimerFinished();
            }
        }.start();
    }
    private void TimerFinished(){
        EditText entercodefield=(EditText)findViewById(R.id.userEnteredCode);
        Button checkcodebutton=(Button)findViewById(R.id.CheckCodeButton);
        entercodefield.setEnabled(false);
        checkcodebutton.setEnabled(false);
        Button Send=(Button)findViewById(R.id.SendCodeButton);
        Send.setVisibility(View.VISIBLE);
        Send.setText("Send another code");
    }
    public void SendCodeToEmail(View v){
        CreateCode();
        JavaMailAPI mail=new JavaMailAPI(CheckEmail.this,userEmail,"Check The Code","Hi :)\n\nYour code is : \t"+CodeForSecurity);
        mail.execute();
        Button Send=(Button)findViewById(R.id.SendCodeButton);
        Send.setVisibility(View.INVISIBLE);
        EditText entercodefield=(EditText)findViewById(R.id.userEnteredCode);
        Button checkcodebutton=(Button)findViewById(R.id.CheckCodeButton);
        entercodefield.setEnabled(true);
        checkcodebutton.setEnabled(true);
        startTimer();
        Toast.makeText(CheckEmail.this,"Successfully sent a code to your email",Toast.LENGTH_SHORT).show();
    }
    private void CheckForSignUp(){
        ProgressBar LoadingCircle=(ProgressBar)findViewById(R.id.LoadingCircle);
        LoadingCircle.setVisibility(View.VISIBLE);
        //Create user account...
        AddUser user=new AddUser();
        user.Email=userEmail;
        user.Password=SignUpPassword;
        user.IfCVCreated=0;
        reff.child(""+(DBCount+1)).setValue(user);
        Handler h=new Handler();
        h.postDelayed(new Runnable(){
            public void run(){
                finish();
                FromWhere.finish();
                MainActivity.ToWhere=Intro.class;
                startActivity(new Intent(CheckEmail.this,MainActivity.class));
            }
        },2000);
    }
    private void CheckForReset(){
        ProgressBar LoadingCircle=(ProgressBar)findViewById(R.id.LoadingCircle);
        LoadingCircle.setVisibility(View.VISIBLE);
        reff.child(""+ResetPassWhere).child("Password").setValue(ResetForNewPassword);
        Handler h=new Handler();
        h.postDelayed(new Runnable(){
            public void run(){
                finish();
                FromWhere.finish();
                MainActivity.ToWhere=Intro.class;
                startActivity(new Intent(CheckEmail.this,MainActivity.class));
            }
        },2000);
    }
    public void CheckUserEmail(View v){
        EditText CodeField=(EditText)findViewById(R.id.userEnteredCode);
        if(CodeField.getText().toString().trim().length()>0){
            if(CodeField.getText().toString().equals(CodeForSecurity)){
                if(FromWhere.getClass().getSimpleName().toString().equals("ResetPassword")){
                    CheckForReset();
                }
                if(FromWhere.getClass().getSimpleName().toString().equals("SignUp")){
                    CheckForSignUp();
                }
            }
            else{
                Toast.makeText(CheckEmail.this,"INVALID CODE , try again",Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(CheckEmail.this,"Make sure you entered a code",Toast.LENGTH_SHORT).show();
        }
    }
}