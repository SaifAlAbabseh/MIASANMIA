package com.example.workway;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

class CV{
    public String FullName;
    public String DateOfBirth;
    public String PhoneNumber;
    public String Email;
    public String ITSkills;
    public String OtherSkills;
    public String Languages;
    public String YearsOfExp;
    public String GPA;
}
public class CreateCV extends AppCompatActivity {
    public static int WhichKey=0;
    DatabaseReference reff;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        StatusBarColor.ChangeColor(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_create_cv);
        reff= FirebaseDatabase.getInstance().getReference().child("UserCV");
    }
    public void ExcellentClicked(View v){
        RadioButton Good=(RadioButton)findViewById(R.id.Good);
        RadioButton VeryGood=(RadioButton)findViewById(R.id.VeryGood);
        RadioButton Accepted=(RadioButton)findViewById(R.id.Accepted);
        VeryGood.setChecked(false);
        Good.setChecked(false);
        Accepted.setChecked(false);
    }
    public void GoodClicked(View v){
        RadioButton VeryGood=(RadioButton)findViewById(R.id.VeryGood);
        RadioButton Accepted=(RadioButton)findViewById(R.id.Accepted);
        RadioButton Excellent=(RadioButton)findViewById(R.id.Excellent);
        Excellent.setChecked(false);
        VeryGood.setChecked(false);
        Accepted.setChecked(false);
    }
    public void VeryGoodClicked(View v){
        RadioButton Good=(RadioButton)findViewById(R.id.Good);
        RadioButton Accepted=(RadioButton)findViewById(R.id.Accepted);
        RadioButton Excellent=(RadioButton)findViewById(R.id.Excellent);
        Excellent.setChecked(false);
        Good.setChecked(false);
        Accepted.setChecked(false);
    }
    public void AcceptedClicked(View v){
        RadioButton Good=(RadioButton)findViewById(R.id.Good);
        RadioButton VeryGood=(RadioButton)findViewById(R.id.VeryGood);
        RadioButton Excellent=(RadioButton)findViewById(R.id.Excellent);
        Excellent.setChecked(false);
        VeryGood.setChecked(false);
        Good.setChecked(false);
    }
    public void Between5And10Clicked(View v){
        RadioButton lessthan5=(RadioButton)findViewById(R.id.LessThan5);
        RadioButton morethan10=(RadioButton)findViewById(R.id.MoreThan10);
        lessthan5.setChecked(false);
        morethan10.setChecked(false);
    }
    public void MoreThan10Clicked(View v){
        RadioButton lessthan5=(RadioButton)findViewById(R.id.LessThan5);
        RadioButton between5and10=(RadioButton)findViewById(R.id.Between5And10);
        lessthan5.setChecked(false);
        between5and10.setChecked(false);
    }
    public void LessThan5Clicked(View v){
        RadioButton between5and10=(RadioButton)findViewById(R.id.Between5And10);
        RadioButton morethan10=(RadioButton)findViewById(R.id.MoreThan10);
        between5and10.setChecked(false);
        morethan10.setChecked(false);
    }
    private boolean CheckForFields(){
        EditText FName=(EditText)findViewById(R.id.CVFirstName);
        EditText LName=(EditText)findViewById(R.id.CVLastName);
        EditText Day=(EditText)findViewById(R.id.Day);
        EditText Month=(EditText)findViewById(R.id.Month);
        EditText Year=(EditText)findViewById(R.id.Year);
        EditText PhoneNumber=(EditText)findViewById(R.id.CVPhoneNumber);
        EditText Email=(EditText)findViewById(R.id.CVEmail);
        EditText ITSkills=(EditText)findViewById(R.id.CVITSkills);
        EditText OtherSkills=(EditText)findViewById(R.id.CVOtherSkills);
        EditText CVLanguages=(EditText)findViewById(R.id.CVLanguages);
        if(FName.getText().toString().trim().length()>0 && LName.getText().toString().trim().length()>0 && Day.getText().toString().trim().length()>0 && Month.getText().toString().trim().length()>0 && Year.getText().toString().trim().length()>0 && PhoneNumber.getText().toString().trim().length()>0 && Email.getText().toString().trim().length()>0 && ITSkills.getText().toString().trim().length()>0 && OtherSkills.getText().toString().trim().length()>0 && CVLanguages.getText().toString().trim().length()>0){
            return true;
        }
        return false;
    }
    private boolean CheckForYearsOfExp(){
        RadioButton LessThan5=(RadioButton)findViewById(R.id.LessThan5);
        RadioButton Between5And10=(RadioButton)findViewById(R.id.Between5And10);
        RadioButton MoreThan10=(RadioButton)findViewById(R.id.MoreThan10);
        if(LessThan5.isChecked() || Between5And10.isChecked() || MoreThan10.isChecked()){
            return true;
        }
        else{
            return false;
        }
    }
    private boolean CheckForGPA(){
        RadioButton Good=(RadioButton)findViewById(R.id.Good);
        RadioButton VeryGood=(RadioButton)findViewById(R.id.VeryGood);
        RadioButton Accepted=(RadioButton)findViewById(R.id.Accepted);
        RadioButton Excellent=(RadioButton)findViewById(R.id.Excellent);
        if(Good.isChecked() || VeryGood.isChecked() || Accepted.isChecked() || Excellent.isChecked()){
            return true;
        }
        else{
            return false;
        }
    }
    private boolean CheckTheDate(){
        EditText Day=(EditText)findViewById(R.id.Day);
        int day=Integer.parseInt(Day.getText().toString());
        EditText Month=(EditText)findViewById(R.id.Month);
        int month=Integer.parseInt(Month.getText().toString());
        EditText Year=(EditText)findViewById(R.id.Year);
        int year=Integer.parseInt(Year.getText().toString());
        Date date=new Date();
        int DateYear=date.getYear()+1900;
        if((day>=1 && day<=31) && (month>=1 && month<=12) && (year>=1950 && year<=DateYear)){
            return true;
        }
        return false;
    }
    private void SendCVData(){
        CV userCV=new CV();
        userCV.FullName=((EditText)findViewById(R.id.CVFirstName)).getText().toString()+" "+((EditText)findViewById(R.id.CVLastName)).getText().toString();
        userCV.DateOfBirth=((EditText)findViewById(R.id.Day)).getText().toString()+"/"+((EditText)findViewById(R.id.Month)).getText().toString()+"/"+((EditText)findViewById(R.id.Year)).getText().toString();
        userCV.PhoneNumber=((EditText)findViewById(R.id.CVPhoneNumber)).getText().toString();
        userCV.Email=((EditText)findViewById(R.id.CVEmail)).getText().toString();
        userCV.ITSkills=((EditText)findViewById(R.id.CVITSkills)).getText().toString();
        userCV.OtherSkills=((EditText)findViewById(R.id.CVOtherSkills)).getText().toString();
        userCV.Languages=((EditText)findViewById(R.id.CVLanguages)).getText().toString();
        if(((RadioButton)findViewById(R.id.LessThan5)).isChecked()){
            userCV.YearsOfExp="Less than 5 years";
        }
        else if(((RadioButton)findViewById(R.id.Between5And10)).isChecked()){
            userCV.YearsOfExp="Between 5 and 10 years";
        }
        else if(((RadioButton)findViewById(R.id.MoreThan10)).isChecked()){
            userCV.YearsOfExp="More than 10 years";
        }
        if(((RadioButton)findViewById(R.id.Good)).isChecked()){
            userCV.GPA="Good";
        }
        else if(((RadioButton)findViewById(R.id.Excellent)).isChecked()){
            userCV.GPA="Excellent";
        }
        else if(((RadioButton)findViewById(R.id.Accepted)).isChecked()){
            userCV.GPA="Accepted";
        }
        else if(((RadioButton)findViewById(R.id.VeryGood)).isChecked()){
            userCV.GPA="Very Good";
        }
        reff.child(""+MainScreen.userEmail.replace(".",",")).setValue(userCV);
        reff= FirebaseDatabase.getInstance().getReference().child("UserInfo");
        reff.child(""+WhichKey).child("IfCVCreated").setValue(1);
    }
    public void CheckForCreateCV(View v){
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
                                        SendCVData();
                                        finish();
                                        MainScreen.IfCVCreatedBefore=true;
                                        MainActivity.ToWhere=MainScreen.class;
                                        startActivity(new Intent(CreateCV.this,MainActivity.class));
                                    }})
                                .setNegativeButton(android.R.string.no, null).show();
                    }
                    else{
                        Toast.makeText(CreateCV.this,"Check the Date of Birth fields",Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(CreateCV.this,"You didnt pick a GPA",Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(CreateCV.this,"You didnt pick for years of experience",Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(CreateCV.this,"Make sure that every field is not empty",Toast.LENGTH_SHORT).show();
        }
    }
    public void GoBack(View v){
        finish();
        startActivity(new Intent(CreateCV.this,MainScreen.class));
    }
}