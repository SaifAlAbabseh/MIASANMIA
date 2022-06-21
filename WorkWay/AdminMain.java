package com.example.workway;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;

public class AdminMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_admin_main);
        StatusBarColor.ChangeColor(this);
    }
    public void GoToPostCompanyAd(View v){
        finish();
        startActivity(new Intent(AdminMain.this,PostCompanyAd.class));
    }
    public void GoToRegisterCompanyAcc(View v){
        finish();
        startActivity(new Intent(AdminMain.this,RegisterCompanyAccount.class));
    }
    public void Logout(View v){
        new AlertDialog.Builder(this)
                .setTitle("Sign Out")
                .setMessage("Are you sure want to logout ?")
                .setIcon(android.R.drawable.stat_sys_warning)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        finish();
                        MainActivity.ToWhere=AdminLogin.class;
                        startActivity(new Intent(AdminMain.this,MainActivity.class));
                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }
    public void SlideMenu(View v){
        DrawerLayout layout=findViewById(R.id.AdminDrawerLayout);
        layout.openDrawer(GravityCompat.START);
    }
}