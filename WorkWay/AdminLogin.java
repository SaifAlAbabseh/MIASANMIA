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

public class AdminLogin extends AppCompatActivity {
    DatabaseReference reff;
    boolean IfCorrect=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        StatusBarColor.ChangeColor(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_admin_login);
        reff= FirebaseDatabase.getInstance().getReference().child("AdminInfo");
    }
    public void GoBack(View v){
        finish();
        startActivity(new Intent(AdminLogin.this,Login.class));
    }
    public void CheckForAdmin(View v){
        EditText passcode=(EditText)findViewById(R.id.PassCode);
        if(passcode.getText().toString().trim().length()>0){
            readData(new FireBaseCallBack() {
                @Override
                public void onCallBack() {
                    if(IfCorrect){
                        finish();
                        MainActivity.ToWhere=AdminMain.class;
                        startActivity(new Intent(AdminLogin.this,MainActivity.class));
                    }
                    else{
                        Toast.makeText(AdminLogin.this,"INVALID PassCode",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else{
            Toast.makeText(AdminLogin.this,"Field cannot be empty",Toast.LENGTH_SHORT).show();
        }
    }
    private interface FireBaseCallBack{
        void onCallBack();
    }
    private void readData(AdminLogin.FireBaseCallBack callBack){
        EditText passcode=(EditText)findViewById(R.id.PassCode);
        reff.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data:snapshot.getChildren()){
                    if(passcode.getText().toString().equals(data.child("PassCode").getValue().toString())){
                        IfCorrect=true;
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