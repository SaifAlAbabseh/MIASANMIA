package com.example.workway;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainScreen extends AppCompatActivity {
    public static String userEmail="";
    public static int WhichKey=0;
    DatabaseReference reff;
    public static boolean IfCVCreatedBefore=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        StatusBarColor.ChangeColor(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main_screen);
        reff= FirebaseDatabase.getInstance().getReference().child("UserInfo");
        TextView ShowEmail=(TextView)findViewById(R.id.ShowUserEmailLabel);
        ShowEmail.setText(userEmail);
        ShowOrHideCVButton();
        CheckIfCVCreated();
    }
    public void GoToSearchCompanies(View v){
        finish();
        startActivity(new Intent(MainScreen.this,SearchForCompanies.class));
    }
    private void CheckIfCVCreated() {
        TextView ShowIfCVCreated = (TextView) findViewById(R.id.ShowUserIfCVCreated);
        if (IfCVCreatedBefore) {
            ShowIfCVCreated.setVisibility(View.VISIBLE);
            ShowIfCVCreated.setTextColor(Color.BLUE);
            ShowIfCVCreated.setText("Ready to send");
        } else {
            ShowIfCVCreated.setVisibility(View.VISIBLE);
            ShowIfCVCreated.setTextColor(Color.RED);
            ShowIfCVCreated.setText("Has not been created");
        }
    }
    public void ShowOrHideCVButton(){
        if(IfCVCreatedBefore){
            Button createCVButton=(Button)findViewById(R.id.CreateCV);
            createCVButton.setVisibility(View.INVISIBLE);
            Button editCVButton=(Button)findViewById(R.id.EditCV);
            editCVButton.setVisibility(View.VISIBLE);
        }
    }
    public void SlideMenu(View v){
        DrawerLayout layout=findViewById(R.id.layout);
        layout.openDrawer(GravityCompat.START);
    }
    public void Logout(View v){
        new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to sign out?")
                .setIcon(android.R.drawable.stat_sys_warning)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        finish();
                        MainActivity.ToWhere=Login.class;
                        startActivity(new Intent(MainScreen.this,MainActivity.class));
                    }})
                .setNegativeButton(android.R.string.no,null).show();
    }
    public void GoToEditCV(View v){
        finish();
        startActivity(new Intent(MainScreen.this,EditCV.class));
    }
    public void GoToCreateCV(View v){
        CreateCV.WhichKey=WhichKey;
        finish();
        startActivity(new Intent(MainScreen.this,CreateCV.class));
    }
}