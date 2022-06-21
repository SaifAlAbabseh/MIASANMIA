package com.example.workway;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CompanyPostAd extends AppCompatActivity {
    DatabaseReference reff;
    private int HowMuchAds = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_company_post_ad);
        StatusBarColor.ChangeColor(this);
        reff = FirebaseDatabase.getInstance().getReference().child("CompaniesAds");
    }

    public void GoBack(View v) {
        finish();
        startActivity(new Intent(this, Company_Main.class));
    }

    public void PostCompanyAdFun(View v) {
        HowMuchAds = 1;
        EditText CompName = (EditText) findViewById(R.id.CompanyEnteredCompanyName);
        EditText CompConstraints = (EditText) findViewById(R.id.CompanyEnteredCompanyConstraints);
        if (CompName.getText().toString().trim().length() > 0 && CompConstraints.getText().toString().trim().length() > 0) {
            new AlertDialog.Builder(CompanyPostAd.this)
                    .setTitle("Check")
                    .setMessage("Sure ?")
                    .setIcon(android.R.drawable.stat_sys_warning)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            readData2(new FireBaseCallBack2() {
                                @Override
                                public void onCallBack() {
                                    PostAd newAd = new PostAd();
                                    newAd.CompanyID = Company_Main.ID;
                                    newAd.CompanyName = CompName.getText().toString();
                                    newAd.CompanyRequirements = CompConstraints.getText().toString();
                                    reff.child("" + Company_Main.ID+","+HowMuchAds).setValue(newAd);
                                    startActivity(new Intent(CompanyPostAd.this, TempLoading.class));
                                    CompName.setText("");
                                    CompConstraints.setText("");
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(CompanyPostAd.this, "Successfully posted", Toast.LENGTH_SHORT).show();
                                        }
                                    }, 2000);
                                }
                            });
                        }
                    })
                    .setNegativeButton(android.R.string.no, null).show();
        } else {
            Toast.makeText(CompanyPostAd.this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
        }
    }

    private interface FireBaseCallBack2 {
        void onCallBack();
    }

    private void readData2(CompanyPostAd.FireBaseCallBack2 callBack) {
        reff.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    PostAd DBID = data.getValue(PostAd.class);
                    if ((""+Company_Main.ID).equals(DBID.CompanyID)) {
                        HowMuchAds++;
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