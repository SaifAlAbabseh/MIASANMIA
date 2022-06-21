package com.example.workway;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {
    boolean IfUserExists = false;
    static int Tries = 0;
    static double HowLong=5.0;
    DatabaseReference reff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        StatusBarColor.ChangeColor(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_login);
        reff = FirebaseDatabase.getInstance().getReference().child("UserInfo");
    }

    public void GoBack(View v) {
        finish();
        startActivity(new Intent(this, Intro.class));
    }

    public void GoToResetPass(View v) {
        finish();
        startActivity(new Intent(this, ResetPassword.class));
    }

    public void GoToAdminLogin(View v) {
        finish();
        startActivity(new Intent(Login.this, AdminLogin.class));
    }

    public void GoToCompanyLogin(View v) {
        finish();
        startActivity(new Intent(Login.this, CompanyLogin.class));
    }

    public void CheckForLogin(View v) {
        if (isEmpty()) {
            Toast.makeText(Login.this, "Make sure text fields are not empty", Toast.LENGTH_SHORT).show();
        } else {
            if (Tries == 5) {
                TimerForTries.setTimer();
                TimerForTries.AlreadyBlocked=true;
                Toast.makeText(Login.this, "Blocked for : "+String.format("%.2f",HowLong)+" Minutes", Toast.LENGTH_SHORT).show();
            } else {
                readData(new FireBaseCallBack() {
                    @Override
                    public void onCallBack() {
                        if (IfUserExists) {
                            //When the email and password are correct...
                            EditText UserEmail = (EditText) findViewById(R.id.UserEmail);
                            MainScreen.userEmail = UserEmail.getText().toString().trim();
                            finish();
                            MainActivity.ToWhere = MainScreen.class;
                            startActivity(new Intent(Login.this, MainActivity.class));
                        } else {
                            Toast.makeText(Login.this, "Email or Password is incorrect", Toast.LENGTH_SHORT).show();
                            Tries++;
                        }
                    }
                });
            }
        }
    }

    private boolean isEmpty() {
        EditText UserEmail = (EditText) findViewById(R.id.UserEmail);
        EditText UserPassword = (EditText) findViewById(R.id.UserPassword);
        if (UserEmail.getText().toString().trim().length() == 0 || UserPassword.getText().toString().length() == 0) {
            return true;
        }
        return false;
    }

    private interface FireBaseCallBack {
        void onCallBack();
    }

    private void readData(Login.FireBaseCallBack callBack) {
        EditText UserEmail = (EditText) findViewById(R.id.UserEmail);
        EditText UserPassword = (EditText) findViewById(R.id.UserPassword);
        reff.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    AddUser DB = data.getValue(AddUser.class);
                    if (UserEmail.getText().toString().equals(DB.Email) && UserPassword.getText().toString().equals(DB.Password)) {
                        IfUserExists = true;
                        int Where = Integer.parseInt(data.getKey());
                        MainScreen.WhichKey = Where;
                        if (DB.IfCVCreated == 0) {
                            MainScreen.IfCVCreatedBefore = false;
                        } else if (DB.IfCVCreated == 1) {
                            MainScreen.IfCVCreatedBefore = true;
                        }
                        break;
                    }
                }
                callBack.onCallBack();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}