package com.example.workway;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ResetPassword extends AppCompatActivity {
    boolean IfExists=false;
    DatabaseReference reff;
    Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        StatusBarColor.ChangeColor(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_reset_password);
        EditText password=(EditText)findViewById(R.id.ResetUserPassword);
        dialog=new Dialog(ResetPassword.this);
        dialog.setContentView(R.layout.pass_req_dialog);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.pass_req_dialog_background));
        }
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().getAttributes().windowAnimations=R.style.pass_req_animation;
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String userChangedPasswordTo=((EditText)findViewById(R.id.ResetUserPassword)).getText().toString();
                CheckPassForReq req=new CheckPassForReq();
                if(userChangedPasswordTo.length()>=8 && userChangedPasswordTo.length()<=15){
                    TextView t=(TextView)dialog.findViewById(R.id.IfCorrectLength);
                    t.setTextColor(Color.GREEN);
                }
                else{
                    TextView t=(TextView)dialog.findViewById(R.id.IfCorrectLength);
                    t.setTextColor(Color.RED);
                }
                if(userChangedPasswordTo.contains(" ")){
                    TextView t=(TextView)dialog.findViewById(R.id.IfContainsSpaces);
                    t.setTextColor(Color.RED);
                }
                else{
                    TextView t=(TextView)dialog.findViewById(R.id.IfContainsSpaces);
                    t.setTextColor(Color.GREEN);
                }
                if(!req.IfThereACapitalLetter(userChangedPasswordTo)){
                    TextView t=(TextView)dialog.findViewById(R.id.IfContainsCapitalLetter);
                    t.setTextColor(Color.RED);
                }
                else{
                    TextView t=(TextView)dialog.findViewById(R.id.IfContainsCapitalLetter);
                    t.setTextColor(Color.GREEN);
                }
                if(!req.IfThereASmallLetter(userChangedPasswordTo)){
                    TextView t=(TextView)dialog.findViewById(R.id.IfContainsSmallLetter);
                    t.setTextColor(Color.RED);
                }
                else{
                    TextView t=(TextView)dialog.findViewById(R.id.IfContainsSmallLetter);
                    t.setTextColor(Color.GREEN);
                }
                if(!req.IfThereANumber(userChangedPasswordTo)){
                    TextView t=(TextView)dialog.findViewById(R.id.IfContainsNumber);
                    t.setTextColor(Color.RED);
                }
                else{
                    TextView t=(TextView)dialog.findViewById(R.id.IfContainsNumber);
                    t.setTextColor(Color.GREEN);
                }
                if(!req.IfThereASymbol(userChangedPasswordTo)){
                    TextView t=(TextView)dialog.findViewById(R.id.IfContainsSymbol);
                    t.setTextColor(Color.RED);
                }
                else{
                    TextView t=(TextView)dialog.findViewById(R.id.IfContainsSymbol);
                    t.setTextColor(Color.GREEN);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        reff= FirebaseDatabase.getInstance().getReference().child("UserInfo");
    }
    public void GoBack(View v){
        finish();
        startActivity(new Intent(this,Login.class));
    }
    private boolean isEmpty(){
        EditText resetUserEmail=(EditText)findViewById(R.id.ResetUserEmail);
        EditText resetUserPassword=(EditText)findViewById(R.id.ResetUserPassword);
        EditText resetConfirmPassword=(EditText)findViewById(R.id.ResetConfirmPassword);
        if(resetUserEmail.getText().toString().trim().length()==0  || resetUserPassword.getText().toString().length()==0 || resetConfirmPassword.getText().toString().length()==0){
            return true;
        }
        return false;
    }
    private interface FireBaseCallBack{
        void onCallBack();
    }
    private void readData(ResetPassword.FireBaseCallBack callBack){
        EditText userEmail = (EditText) findViewById(R.id.ResetUserEmail);
        reff.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data:snapshot.getChildren()){
                    AddUser DBEmail=data.getValue(AddUser.class);
                    if(userEmail.getText().toString().equals(DBEmail.Email)){
                        IfExists=true;
                        int Where=Integer.parseInt(data.getKey());
                        CheckEmail.ResetPassWhere=Where;
                        break;
                    }
                }
                callBack.onCallBack();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
    public void CheckForReset(View v){
        IfExists=false;
        if(isEmpty()){
            Toast.makeText(ResetPassword.this,"Make sure text fields are not empty",Toast.LENGTH_SHORT).show();
        }
        else{
            if(isPasswordsEqual()){
                EditText resetUserPassword=(EditText)findViewById(R.id.ResetUserPassword);
                CheckPassForReq check=new CheckPassForReq();
                if(check.CheckPassIfMeetTheReq(resetUserPassword.getText().toString()).equals("good")){
                    readData(new FireBaseCallBack() {
                        @Override
                        public void onCallBack() {
                            if(IfExists){
                                EditText resetUserEmail=(EditText)findViewById(R.id.ResetUserEmail);
                                EditText resetUserPassword=(EditText)findViewById(R.id.ResetUserPassword);
                                finish();
                                CheckEmail.FromWhere=ResetPassword.this;
                                CheckEmail.userEmail=resetUserEmail.getText().toString().trim();
                                CheckEmail.ResetForNewPassword=resetUserPassword.getText().toString();
                                startActivity(new Intent(ResetPassword.this,CheckEmail.class));
                            }
                            else{
                                Toast.makeText(ResetPassword.this,"This email is not used",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(ResetPassword.this,"Check the password requirements",Toast.LENGTH_SHORT).show();
                    dialog.show();
                }
            }
            else{
                Toast.makeText(ResetPassword.this,"Passwords are not the same",Toast.LENGTH_SHORT).show();
            }
        }
    }
    private boolean isPasswordsEqual(){
        EditText resetUserPassword=(EditText)findViewById(R.id.ResetUserPassword);
        EditText resetConfirmPassword=(EditText)findViewById(R.id.ResetConfirmPassword);
        if(resetUserPassword.getText().toString().equals(resetConfirmPassword.getText().toString())){
            return true;
        }
        return false;
    }
    public void ShowPassReqDialog(View v){
        dialog.show();
    }
}