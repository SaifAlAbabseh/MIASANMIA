package com.example.workway;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Company_Main extends AppCompatActivity {
    DatabaseReference reff;
    public static String ID;
    private int countHowManyReq=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        StatusBarColor.ChangeColor(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_company_main);
        reff= FirebaseDatabase.getInstance().getReference().child("Requests");
        CountTheRequesters();
    }
    private interface FireBaseCallBack{
        void onCallBack();
    }
    private void readData(Company_Main.FireBaseCallBack callBack){
        reff.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data:snapshot.getChildren()){
                    Request r=data.getValue(Request.class);
                    if(r.RequestedCompanyID.equals(ID)){
                        countHowManyReq++;
                    }
                }
                callBack.onCallBack();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
    public void CountTheRequesters(){
        readData(new FireBaseCallBack() {
            @Override
            public void onCallBack() {
                TextView countText=(TextView)findViewById(R.id.NoOfReq);
                countText.setText(countText.getText().toString()+countHowManyReq);
            }
        });
    }
    public void GoToPostAd(View v){
        finish();
        startActivity(new Intent(this,CompanyPostAd.class));
    }
    public void Logout(View v){
        new AlertDialog.Builder(this)
                .setTitle("Sign Out")
                .setMessage("Are you sure want to logout ?")
                .setIcon(android.R.drawable.stat_sys_warning)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        finish();
                        MainActivity.ToWhere=CompanyLogin.class;
                        startActivity(new Intent(Company_Main.this,MainActivity.class));
                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }
    public void SlideMenu(View v){
        DrawerLayout layout=findViewById(R.id.DrawerLayoutCompanyMain);
        layout.openDrawer(GravityCompat.START);
    }
    public void DisplayRequestersCV(View v){
        finish();
        startActivity(new Intent(Company_Main.this,DisplayRequestersCVScreen.class));
    }
}