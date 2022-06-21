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

class RegisterCompany{
    public String ID;
    public String Password;
}
public class RegisterCompanyAccount extends AppCompatActivity {
    DatabaseReference reff;
    int DBCount=0;
    boolean IfIDExists=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_register_company_account);
        StatusBarColor.ChangeColor(this);
        reff= FirebaseDatabase.getInstance().getReference().child("CompanyInfo");
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
    public void GoBack(View v){
        finish();
        startActivity(new Intent(this,AdminMain.class));
    }
    public void CheckForRegister(View v){
        IfIDExists=false;
        EditText ComID=(EditText)findViewById(R.id.RegisterCompanyID);
        EditText ComPassword=(EditText)findViewById(R.id.RegisterCompanyPassword);
        if(ComID.getText().toString().trim().length()>0 && ComPassword.getText().toString().trim().length()>0){
            readData(new FireBaseCallBack() {
                @Override
                public void onCallBack() {
                    if(IfIDExists){
                        Toast.makeText(RegisterCompanyAccount.this,"ID already registered",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        new AlertDialog.Builder(RegisterCompanyAccount.this)
                                .setTitle("Check")
                                .setMessage("Sure ?")
                                .setIcon(android.R.drawable.stat_sys_warning)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        RegisterCompany reg=new RegisterCompany();
                                        reg.ID=ComID.getText().toString();
                                        reg.Password=ComPassword.getText().toString();
                                        reff.child(""+(DBCount+1)).setValue(reg);
                                        startActivity(new Intent(RegisterCompanyAccount.this,TempLoading.class));
                                        ComID.setText("");
                                        ComPassword.setText("");
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(RegisterCompanyAccount.this,"Successfully created company account",Toast.LENGTH_SHORT).show();
                                            }
                                        },2000);
                                    }})
                                .setNegativeButton(android.R.string.no, null).show();
                    }
                }
            });
        }
        else{
            Toast.makeText(RegisterCompanyAccount.this,"Make sure fields are not empty",Toast.LENGTH_SHORT).show();
        }
    }
    private interface FireBaseCallBack{
        void onCallBack();
    }
    private void readData(RegisterCompanyAccount.FireBaseCallBack callBack){
        EditText ComID=(EditText)findViewById(R.id.RegisterCompanyID);
        reff.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data:snapshot.getChildren()){
                    RegisterCompany DBID=data.getValue(RegisterCompany.class);
                    if(ComID.getText().toString().equals(DBID.ID)){
                        IfIDExists=true;
                        break;
                    }
                }
                callBack.onCallBack();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
}