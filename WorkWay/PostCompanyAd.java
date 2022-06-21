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

class PostAd {
    public String CompanyID;
    public String CompanyName;
    public String CompanyRequirements;
}

public class PostCompanyAd extends AppCompatActivity {
    DatabaseReference reff;
    DatabaseReference reff2;
    public boolean IfIDExists = false;
    public boolean IfAlreadyPosted = false;
    private int HowMuchAds = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_post_company_ad);
        StatusBarColor.ChangeColor(this);
        reff = FirebaseDatabase.getInstance().getReference().child("CompaniesAds");
        reff2 = FirebaseDatabase.getInstance().getReference().child("CompanyInfo");
    }

    public void GoBack(View v) {
        finish();
        startActivity(new Intent(this, AdminMain.class));
    }

    public void PostCompanyAdFun(View v) {
        IfIDExists = false;
        IfAlreadyPosted = false;
        HowMuchAds = 1;
        EditText CompID = (EditText) findViewById(R.id.EnteredCompanyID);
        EditText CompName = (EditText) findViewById(R.id.EnteredCompanyName);
        EditText CompConstraints = (EditText) findViewById(R.id.EnteredCompanyConstraints);
        if (CompID.getText().toString().trim().length() > 0 && CompName.getText().toString().trim().length() > 0 && CompConstraints.getText().toString().trim().length() > 0) {
            //Check if the ID is registered or not...
            readData(new FireBaseCallBack() {
                @Override
                public void onCallBack() {
                    if (IfIDExists) {
                        new AlertDialog.Builder(PostCompanyAd.this)
                                .setTitle("Check")
                                .setMessage("Sure ?")
                                .setIcon(android.R.drawable.stat_sys_warning)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        readData2(new FireBaseCallBack2() {
                                            @Override
                                            public void onCallBack() {
                                                PostAd newAd = new PostAd();
                                                newAd.CompanyID = CompID.getText().toString();
                                                newAd.CompanyName = CompName.getText().toString();
                                                newAd.CompanyRequirements = CompConstraints.getText().toString();
                                                reff.child("" + CompID.getText().toString() + "," + HowMuchAds).setValue(newAd);
                                                startActivity(new Intent(PostCompanyAd.this, TempLoading.class));
                                                CompID.setText("");
                                                CompName.setText("");
                                                CompConstraints.setText("");
                                                new Handler().postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(PostCompanyAd.this, "Successfully posted ad", Toast.LENGTH_SHORT).show();
                                                    }
                                                }, 2000);
                                            }
                                        });
                                    }
                                })
                                .setNegativeButton(android.R.string.no, null).show();
                    } else {
                        Toast.makeText(PostCompanyAd.this, "Company ID is not registered", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(PostCompanyAd.this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
        }
    }

    private interface FireBaseCallBack {
        void onCallBack();
    }

    private void readData(PostCompanyAd.FireBaseCallBack callBack) {
        EditText ComID = (EditText) findViewById(R.id.EnteredCompanyID);
        reff2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    RegisterCompany DBID = data.getValue(RegisterCompany.class);
                    if (ComID.getText().toString().equals(DBID.ID)) {
                        IfIDExists = true;
                    }
                }
                callBack.onCallBack();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private interface FireBaseCallBack2 {
        void onCallBack();
    }

    private void readData2(PostCompanyAd.FireBaseCallBack2 callBack) {
        EditText ComID = (EditText) findViewById(R.id.EnteredCompanyID);
        reff.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    PostAd DBID = data.getValue(PostAd.class);
                    if (ComID.getText().toString().equals(DBID.CompanyID)) {
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