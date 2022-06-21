package com.example.workway;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

public class EditCV extends AppCompatActivity {
    DatabaseReference reff;
    public void GoBack(View v){
        finish();
        startActivity(new Intent(EditCV.this,MainScreen.class));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        StatusBarColor.ChangeColor(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_edit_cv);
        reff= FirebaseDatabase.getInstance().getReference().child("UserCV");
        startActivity(new Intent(EditCV.this,TempLoading.class));
        SetOldCVData();
    }

    private interface FireBaseCallBack{
        void onCallBack();
    }
    private void readData(EditCV.FireBaseCallBack callBack){
        EditText UserEmail=(EditText)findViewById(R.id.UserEmail);
        EditText UserPassword=(EditText)findViewById(R.id.UserPassword);
        reff.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data:snapshot.getChildren()){
                    if(data.getKey().equals(MainScreen.userEmail.replace(".",","))){
                        CV DB=data.getValue(CV.class);
                        SetOld(DB);
                        break;
                    }
                }
                callBack.onCallBack();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
    public void SetOld(CV DB){
        String[] name=DB.FullName.split(" ");
        EditText fname=(EditText)findViewById(R.id.EditCVFirstName);
        fname.setText(name[0]);
        EditText lname=(EditText)findViewById(R.id.EditCVLastName);
        lname.setText(name[1]);
        String[] date=DB.DateOfBirth.split("/");
        EditText day=(EditText)findViewById(R.id.EditDay);
        EditText month=(EditText)findViewById(R.id.EditMonth);
        EditText year=(EditText)findViewById(R.id.EditYear);
        day.setText(date[0]);
        month.setText(date[1]);
        year.setText(date[2]);
        EditText contactemail=(EditText)findViewById(R.id.EditCVEmail);
        contactemail.setText(DB.Email);
        EditText PNumber=(EditText)findViewById(R.id.EditCVPhoneNumber);
        PNumber.setText(DB.PhoneNumber);
        EditText ITskills=(EditText)findViewById(R.id.EditCVITSkills);
        ITskills.setText(DB.ITSkills);
        EditText Otherskills=(EditText)findViewById(R.id.EditCVOtherSkills);
        Otherskills.setText(DB.OtherSkills);
        EditText Languages=(EditText)findViewById(R.id.EditCVLanguages);
        Languages.setText(DB.Languages);
        if(DB.GPA.equals("Accepted")){
            RadioButton accepted=(RadioButton)findViewById(R.id.EditAccepted);
            accepted.setChecked(true);
        }
        else if(DB.GPA.equals("Good")){
            RadioButton accepted=(RadioButton)findViewById(R.id.EditGood);
            accepted.setChecked(true);
        }
        else if(DB.GPA.equals("Very Good")){
            RadioButton accepted=(RadioButton)findViewById(R.id.EditVeryGood);
            accepted.setChecked(true);
        }
        else if(DB.GPA.equals("Excellent")){
            RadioButton accepted=(RadioButton)findViewById(R.id.EditExcellent);
            accepted.setChecked(true);
        }
        if(DB.YearsOfExp.equals("Less than 5 years")){
            RadioButton lessthan5=(RadioButton)findViewById(R.id.EditLessThan5);
            lessthan5.setChecked(true);
        }
        else if(DB.YearsOfExp.equals("Between 5 and 10 years")){
            RadioButton between=(RadioButton)findViewById(R.id.EditBetween5And10);
            between.setChecked(true);
        }
        else if(DB.YearsOfExp.equals("More than 10 years")){
            RadioButton more=(RadioButton)findViewById(R.id.EditMoreThan10);
            more.setChecked(true);
        }
    }
    public void SetOldCVData(){
        readData(new FireBaseCallBack() {
            @Override
            public void onCallBack() {}
        });
    }
    public void ExcellentClicked(View v){
        RadioButton Good=(RadioButton)findViewById(R.id.EditGood);
        RadioButton VeryGood=(RadioButton)findViewById(R.id.EditVeryGood);
        RadioButton Accepted=(RadioButton)findViewById(R.id.EditAccepted);
        VeryGood.setChecked(false);
        Good.setChecked(false);
        Accepted.setChecked(false);
    }
    public void GoodClicked(View v){
        RadioButton VeryGood=(RadioButton)findViewById(R.id.EditVeryGood);
        RadioButton Accepted=(RadioButton)findViewById(R.id.EditAccepted);
        RadioButton Excellent=(RadioButton)findViewById(R.id.EditExcellent);
        Excellent.setChecked(false);
        VeryGood.setChecked(false);
        Accepted.setChecked(false);
    }
    public void VeryGoodClicked(View v){
        RadioButton Good=(RadioButton)findViewById(R.id.EditGood);
        RadioButton Accepted=(RadioButton)findViewById(R.id.EditAccepted);
        RadioButton Excellent=(RadioButton)findViewById(R.id.EditExcellent);
        Excellent.setChecked(false);
        Good.setChecked(false);
        Accepted.setChecked(false);
    }
    public void AcceptedClicked(View v){
        RadioButton Good=(RadioButton)findViewById(R.id.EditGood);
        RadioButton VeryGood=(RadioButton)findViewById(R.id.EditVeryGood);
        RadioButton Excellent=(RadioButton)findViewById(R.id.EditExcellent);
        Excellent.setChecked(false);
        VeryGood.setChecked(false);
        Good.setChecked(false);
    }
    public void Between5And10Clicked(View v){
        RadioButton lessthan5=(RadioButton)findViewById(R.id.EditLessThan5);
        RadioButton morethan10=(RadioButton)findViewById(R.id.EditMoreThan10);
        lessthan5.setChecked(false);
        morethan10.setChecked(false);
    }
    public void MoreThan10Clicked(View v){
        RadioButton lessthan5=(RadioButton)findViewById(R.id.EditLessThan5);
        RadioButton between5and10=(RadioButton)findViewById(R.id.EditBetween5And10);
        lessthan5.setChecked(false);
        between5and10.setChecked(false);
    }
    public void LessThan5Clicked(View v){
        RadioButton between5and10=(RadioButton)findViewById(R.id.EditBetween5And10);
        RadioButton morethan10=(RadioButton)findViewById(R.id.EditMoreThan10);
        between5and10.setChecked(false);
        morethan10.setChecked(false);
    }

    private boolean CheckForFields(){
        EditText FName=(EditText)findViewById(R.id.EditCVFirstName);
        EditText LName=(EditText)findViewById(R.id.EditCVLastName);
        EditText Day=(EditText)findViewById(R.id.EditDay);
        EditText Month=(EditText)findViewById(R.id.EditMonth);
        EditText Year=(EditText)findViewById(R.id.EditYear);
        EditText PhoneNumber=(EditText)findViewById(R.id.EditCVPhoneNumber);
        EditText Email=(EditText)findViewById(R.id.EditCVEmail);
        EditText ITSkills=(EditText)findViewById(R.id.EditCVITSkills);
        EditText OtherSkills=(EditText)findViewById(R.id.EditCVOtherSkills);
        EditText CVLanguages=(EditText)findViewById(R.id.EditCVLanguages);
        if(FName.getText().toString().trim().length()>0 && LName.getText().toString().trim().length()>0 && Day.getText().toString().trim().length()>0 && Month.getText().toString().trim().length()>0 && Year.getText().toString().trim().length()>0 && PhoneNumber.getText().toString().trim().length()>0 && Email.getText().toString().trim().length()>0 && ITSkills.getText().toString().trim().length()>0 && OtherSkills.getText().toString().trim().length()>0 && CVLanguages.getText().toString().trim().length()>0){
            return true;
        }
        return false;
    }
    private boolean CheckForYearsOfExp(){
        RadioButton LessThan5=(RadioButton)findViewById(R.id.EditLessThan5);
        RadioButton Between5And10=(RadioButton)findViewById(R.id.EditBetween5And10);
        RadioButton MoreThan10=(RadioButton)findViewById(R.id.EditMoreThan10);
        if(LessThan5.isChecked() || Between5And10.isChecked() || MoreThan10.isChecked()){
            return true;
        }
        else{
            return false;
        }
    }
    private boolean CheckForGPA(){
        RadioButton Good=(RadioButton)findViewById(R.id.EditGood);
        RadioButton VeryGood=(RadioButton)findViewById(R.id.EditVeryGood);
        RadioButton Accepted=(RadioButton)findViewById(R.id.EditAccepted);
        RadioButton Excellent=(RadioButton)findViewById(R.id.EditExcellent);
        if(Good.isChecked() || VeryGood.isChecked() || Accepted.isChecked() || Excellent.isChecked()){
            return true;
        }
        else{
            return false;
        }
    }
    private boolean CheckTheDate(){
        EditText Day=(EditText)findViewById(R.id.EditDay);
        int day=Integer.parseInt(Day.getText().toString());
        EditText Month=(EditText)findViewById(R.id.EditMonth);
        int month=Integer.parseInt(Month.getText().toString());
        EditText Year=(EditText)findViewById(R.id.EditYear);
        int year=Integer.parseInt(Year.getText().toString());
        Date date=new Date();
        int DateYear=date.getYear()+1900;
        if((day>=1 && day<=31) && (month>=1 && month<=12) && (year>=1950 && year<=DateYear)){
            return true;
        }
        return false;
    }

    public void EditCVData(){
        CV userCV=new CV();
        userCV.FullName=((EditText)findViewById(R.id.EditCVFirstName)).getText().toString()+" "+((EditText)findViewById(R.id.EditCVLastName)).getText().toString();
        userCV.DateOfBirth=((EditText)findViewById(R.id.EditDay)).getText().toString()+"/"+((EditText)findViewById(R.id.EditMonth)).getText().toString()+"/"+((EditText)findViewById(R.id.EditYear)).getText().toString();
        userCV.PhoneNumber=((EditText)findViewById(R.id.EditCVPhoneNumber)).getText().toString();
        userCV.Email=((EditText)findViewById(R.id.EditCVEmail)).getText().toString();
        userCV.ITSkills=((EditText)findViewById(R.id.EditCVITSkills)).getText().toString();
        userCV.OtherSkills=((EditText)findViewById(R.id.EditCVOtherSkills)).getText().toString();
        userCV.Languages=((EditText)findViewById(R.id.EditCVLanguages)).getText().toString();
        if(((RadioButton)findViewById(R.id.EditLessThan5)).isChecked()){
            userCV.YearsOfExp="Less than 5 years";
        }
        else if(((RadioButton)findViewById(R.id.EditBetween5And10)).isChecked()){
            userCV.YearsOfExp="Between 5 and 10 years";
        }
        else if(((RadioButton)findViewById(R.id.EditMoreThan10)).isChecked()){
            userCV.YearsOfExp="More than 10 years";
        }
        if(((RadioButton)findViewById(R.id.EditGood)).isChecked()){
            userCV.GPA="Good";
        }
        else if(((RadioButton)findViewById(R.id.EditExcellent)).isChecked()){
            userCV.GPA="Excellent";
        }
        else if(((RadioButton)findViewById(R.id.EditAccepted)).isChecked()){
            userCV.GPA="Accepted";
        }
        else if(((RadioButton)findViewById(R.id.EditVeryGood)).isChecked()){
            userCV.GPA="Very Good";
        }
        reff.child(""+MainScreen.userEmail.replace(".",",")).setValue(userCV);
    }
    public void CheckForEditCV(View v){
        if(CheckForFields()){
            if(CheckForYearsOfExp()){
                if(CheckForGPA()){
                    if(CheckTheDate()){
                        new AlertDialog.Builder(this)
                                .setTitle("Check")
                                .setMessage("Are you sure about your information?")
                                .setIcon(android.R.drawable.stat_sys_warning)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        EditCVData();
                                        finish();
                                        MainActivity.ToWhere=MainScreen.class;
                                        startActivity(new Intent(EditCV.this,MainActivity.class));
                                    }})
                                .setNegativeButton(android.R.string.no, null).show();
                    }
                    else{
                        Toast.makeText(EditCV.this,"Check the Date of Birth fields",Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(EditCV.this,"You didnt pick a GPA",Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(EditCV.this,"You didnt pick for years of experience",Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(EditCV.this,"Make sure that every field is not empty",Toast.LENGTH_SHORT).show();
        }
    }

}