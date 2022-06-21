package com.example.workway;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DisplayRequestersCVScreen extends AppCompatActivity {
    DatabaseReference reff;
    DatabaseReference reff2;
    ArrayList<String> Requesters;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        StatusBarColor.ChangeColor(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_display_requesters_cvscreen);
        reff= FirebaseDatabase.getInstance().getReference().child("Requests");
        reff2= FirebaseDatabase.getInstance().getReference().child("UserCV");
        FillTheArr();
        startActivity(new Intent(DisplayRequestersCVScreen.this,TempLoading.class));
    }
    public void FillTheArr(){
        Requesters=new ArrayList<String>();
        readData(new FireBaseCallBack() {
            @Override
            public void onCallBack() {
                for(int i=0;i<Requesters.size();i++){
                    readData2(new FireBaseCallBack2() {
                        @Override
                        public void onCallBack() {}
                    },i);
                }
            }
        });
    }
    public void Refresh(View v){
        finish();
        startActivity(new Intent(DisplayRequestersCVScreen.this,DisplayRequestersCVScreen.class));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(DisplayRequestersCVScreen.this,"Refreshed",Toast.LENGTH_SHORT).show();
            }
        },1500);
    }
    public void GoBack(View v){
        finish();
        startActivity(new Intent(DisplayRequestersCVScreen.this,Company_Main.class));
    }
    private interface FireBaseCallBack{
        void onCallBack();
    }
    private void readData(DisplayRequestersCVScreen.FireBaseCallBack callBack){
        reff.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data:snapshot.getChildren()){
                    Request r=data.getValue(Request.class);
                    if(r.RequestedCompanyID.equals(Company_Main.ID)){
                        Requesters.add(r.RequesterEmail);
                    }
                }
                callBack.onCallBack();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private interface FireBaseCallBack2{
        void onCallBack();
    }
    private void readData2(DisplayRequestersCVScreen.FireBaseCallBack2 callBack,int index){
        LinearLayout layout=(LinearLayout)findViewById(R.id.CVResults);
        reff2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data:snapshot.getChildren()){
                    if(data.getKey().equals(Requesters.get(index).replace(".",","))){
                        CV cv=data.getValue(CV.class);

                        TextView Name=new TextView(getApplicationContext());
                        TextView DateBirth=new TextView(getApplicationContext());
                        TextView ReqName=new TextView(getApplicationContext());
                        TextView ReqDateBirth=new TextView(getApplicationContext());
                        Button Contact=new Button(getApplicationContext());
                        TableLayout Tlayout=new TableLayout(getApplicationContext());
                        TextView Email=new TextView(getApplicationContext());
                        TextView ReqEmail=new TextView(getApplicationContext());
                        TextView PNumber=new TextView(getApplicationContext());
                        TextView ReqPNumber=new TextView(getApplicationContext());
                        TextView GPA=new TextView(getApplicationContext());
                        TextView ReqGPA=new TextView(getApplicationContext());
                        TextView ExpYears=new TextView(getApplicationContext());
                        TextView ReqExpYears=new TextView(getApplicationContext());
                        TextView ITSkills=new TextView(getApplicationContext());
                        TextView ReqITSkills=new TextView(getApplicationContext());
                        TextView Languages=new TextView(getApplicationContext());
                        TextView ReqLanguages=new TextView(getApplicationContext());
                        TextView OtherSkills=new TextView(getApplicationContext());
                        TextView ReqOtherSkills=new TextView(getApplicationContext());



                        OtherSkills.setText("Other Skills");
                        OtherSkills.setGravity(Gravity.CENTER);
                        OtherSkills.setTextSize(20);
                        OtherSkills.setTextColor(Color.parseColor("#aad241"));
                        ReqOtherSkills.setText(cv.OtherSkills);
                        ReqOtherSkills.setTextColor(Color.BLACK);
                        ReqOtherSkills.setTextSize(15);
                        ReqOtherSkills.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.WRAP_CONTENT));

                        Languages.setText("Languages");
                        Languages.setGravity(Gravity.CENTER);
                        Languages.setTextSize(20);
                        Languages.setTextColor(Color.parseColor("#aad241"));
                        ReqLanguages.setText(cv.Languages);
                        ReqLanguages.setTextColor(Color.BLACK);
                        ReqLanguages.setTextSize(15);
                        ReqLanguages.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.WRAP_CONTENT));

                        ITSkills.setText("IT Skills");
                        ITSkills.setGravity(Gravity.CENTER);
                        ITSkills.setTextSize(20);
                        ITSkills.setTextColor(Color.parseColor("#aad241"));
                        ReqITSkills.setText(cv.ITSkills);
                        ReqITSkills.setTextColor(Color.BLACK);
                        ReqITSkills.setTextSize(15);
                        ReqITSkills.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.WRAP_CONTENT));

                        ExpYears.setText("Exp Years");
                        ExpYears.setGravity(Gravity.CENTER);
                        ExpYears.setTextSize(20);
                        ExpYears.setTextColor(Color.parseColor("#aad241"));
                        ReqExpYears.setText(cv.YearsOfExp);
                        ReqExpYears.setTextColor(Color.BLACK);
                        ReqExpYears.setTextSize(15);
                        ReqExpYears.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.WRAP_CONTENT));

                        GPA.setText("GPA");
                        GPA.setGravity(Gravity.CENTER);
                        GPA.setTextSize(20);
                        GPA.setTextColor(Color.parseColor("#aad241"));
                        ReqGPA.setText(cv.GPA);
                        ReqGPA.setTextColor(Color.BLACK);
                        ReqGPA.setTextSize(15);
                        ReqGPA.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.WRAP_CONTENT));

                        PNumber.setText("Phone Number");
                        PNumber.setGravity(Gravity.CENTER);
                        PNumber.setTextSize(20);
                        PNumber.setTextColor(Color.parseColor("#aad241"));
                        ReqPNumber.setText(cv.PhoneNumber);
                        ReqPNumber.setTextColor(Color.BLACK);
                        ReqPNumber.setTextSize(15);
                        ReqPNumber.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.WRAP_CONTENT));

                        Email.setText("Email");
                        Email.setGravity(Gravity.CENTER);
                        Email.setTextSize(20);
                        Email.setTextColor(Color.parseColor("#aad241"));
                        ReqEmail.setText(cv.Email);
                        ReqEmail.setTextColor(Color.BLACK);
                        ReqEmail.setTextSize(15);
                        ReqEmail.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.WRAP_CONTENT));

                        Name.setText("Name");
                        Name.setGravity(Gravity.CENTER);
                        Name.setTextSize(20);
                        Name.setTextColor(Color.parseColor("#aad241"));
                        ReqName.setText(cv.FullName);
                        ReqName.setTextColor(Color.BLACK);
                        ReqName.setTextSize(15);
                        ReqName.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.WRAP_CONTENT));


                        DateBirth.setText("Birth Date");
                        DateBirth.setGravity(Gravity.CENTER);
                        DateBirth.setTextSize(20);
                        DateBirth.setTextColor(Color.parseColor("#aad241"));
                        ReqDateBirth.setText(cv.DateOfBirth);
                        ReqDateBirth.setTextColor(Color.BLACK);
                        ReqDateBirth.setTextSize(15);
                        ReqDateBirth.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.WRAP_CONTENT));

                        Contact.setText("Contact");
                        Contact.setTextColor(Color.WHITE);
                        GradientDrawable shape =  new GradientDrawable();
                        shape.setCornerRadius(0);
                        shape.setColor(Color.parseColor("#0162AF"));
                        Contact.setBackground(shape);
                        Contact.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

                        Space s=new Space(getApplicationContext());
                        s.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,180));


                        TableRow row1=new TableRow(getApplicationContext());
                        TableRow row2=new TableRow(getApplicationContext());
                        TableRow row3=new TableRow(getApplicationContext());
                        TableRow row4=new TableRow(getApplicationContext());
                        TableRow row5=new TableRow(getApplicationContext());
                        TableRow row6=new TableRow(getApplicationContext());
                        TableRow row7=new TableRow(getApplicationContext());
                        TableRow row8=new TableRow(getApplicationContext());
                        TableRow row9=new TableRow(getApplicationContext());
                        TableRow row10=new TableRow(getApplicationContext());
                        TableRow row11=new TableRow(getApplicationContext());
                        TableRow row12=new TableRow(getApplicationContext());
                        TableRow row13=new TableRow(getApplicationContext());
                        TableRow row14=new TableRow(getApplicationContext());
                        TableRow row15=new TableRow(getApplicationContext());
                        TableRow row16=new TableRow(getApplicationContext());
                        TableRow row17=new TableRow(getApplicationContext());
                        TableRow row18=new TableRow(getApplicationContext());
                        TableRow row19=new TableRow(getApplicationContext());
                        TableRow line=new TableRow(getApplicationContext());
                        TableRow line2=new TableRow(getApplicationContext());
                        TableRow line3=new TableRow(getApplicationContext());
                        TableRow line4=new TableRow(getApplicationContext());
                        TableRow line5=new TableRow(getApplicationContext());
                        TableRow line6=new TableRow(getApplicationContext());
                        TableRow line7=new TableRow(getApplicationContext());
                        TableRow line8=new TableRow(getApplicationContext());
                        TableRow line9=new TableRow(getApplicationContext());

                        row1.setGravity(Gravity.CENTER);
                        row2.setGravity(Gravity.CENTER);
                        row3.setGravity(Gravity.CENTER);
                        row4.setGravity(Gravity.CENTER);
                        row5.setGravity(Gravity.CENTER);
                        row6.setGravity(Gravity.CENTER);
                        row7.setGravity(Gravity.CENTER);
                        row8.setGravity(Gravity.CENTER);
                        row9.setGravity(Gravity.CENTER);
                        row10.setGravity(Gravity.CENTER);
                        row11.setGravity(Gravity.CENTER);
                        row12.setGravity(Gravity.CENTER);
                        row13.setGravity(Gravity.CENTER);
                        row14.setGravity(Gravity.CENTER);
                        row15.setGravity(Gravity.CENTER);
                        row16.setGravity(Gravity.CENTER);
                        row17.setGravity(Gravity.CENTER);
                        row18.setGravity(Gravity.CENTER);
                        row19.setGravity(Gravity.CENTER);


                        row1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                        row2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                        row3.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                        row4.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                        row5.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                        row6.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                        row7.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                        row8.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                        row9.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                        row10.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                        row11.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                        row12.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                        row13.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                        row14.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                        row15.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                        row16.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                        row17.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                        row18.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                        row19.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                        line.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                        line2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                        line3.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                        line4.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                        line5.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                        line6.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                        line7.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                        line8.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                        line9.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

                        line.setBackgroundResource(R.color.ProgressBarColor);
                        line2.setBackgroundResource(R.color.ProgressBarColor);
                        line3.setBackgroundResource(R.color.ProgressBarColor);
                        line4.setBackgroundResource(R.color.ProgressBarColor);
                        line5.setBackgroundResource(R.color.ProgressBarColor);
                        line6.setBackgroundResource(R.color.ProgressBarColor);
                        line7.setBackgroundResource(R.color.ProgressBarColor);
                        line8.setBackgroundResource(R.color.ProgressBarColor);
                        line9.setBackgroundResource(R.color.ProgressBarColor);

                        row1.addView(Name);
                        row2.addView(ReqName);
                        row3.addView(DateBirth);
                        row4.addView(ReqDateBirth);
                        row5.addView(Email);
                        row6.addView(ReqEmail);
                        row7.addView(PNumber);
                        row8.addView(ReqPNumber);
                        row9.addView(GPA);
                        row10.addView(ReqGPA);
                        row11.addView(ExpYears);
                        row12.addView(ReqExpYears);
                        row13.addView(ITSkills);
                        row14.addView(ReqITSkills);
                        row15.addView(Languages);
                        row16.addView(ReqLanguages);
                        row17.addView(OtherSkills);
                        row18.addView(ReqOtherSkills);
                        row19.addView(Contact);

                        TextView t11=new TextView(getApplicationContext());
                        t11.setHeight(2);
                        TextView t22=new TextView(getApplicationContext());
                        t22.setHeight(2);
                        TextView t33=new TextView(getApplicationContext());
                        t33.setHeight(2);
                        TextView t44=new TextView(getApplicationContext());
                        t44.setHeight(2);
                        TextView t55=new TextView(getApplicationContext());
                        t55.setHeight(2);
                        TextView t66=new TextView(getApplicationContext());
                        t66.setHeight(2);
                        TextView t77=new TextView(getApplicationContext());
                        t77.setHeight(2);
                        TextView t88=new TextView(getApplicationContext());
                        t88.setHeight(2);
                        TextView t99=new TextView(getApplicationContext());
                        t99.setHeight(2);
                        line.addView(t11);
                        line2.addView(t22);
                        line3.addView(t33);
                        line4.addView(t44);
                        line5.addView(t55);
                        line6.addView(t66);
                        line7.addView(t77);
                        line8.addView(t88);
                        line9.addView(t99);

                        Tlayout.setBackgroundResource(R.drawable.borderwithgraybackground);
                        Tlayout.setLayoutParams(new TableLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                        Tlayout.setPadding(10,40,10,40);

                        Tlayout.addView(row1);
                        Tlayout.addView(row2);
                        Tlayout.addView(line);
                        Tlayout.addView(row3);
                        Tlayout.addView(row4);
                        Tlayout.addView(line2);
                        Tlayout.addView(row5);
                        Tlayout.addView(row6);
                        Tlayout.addView(line3);
                        Tlayout.addView(row7);
                        Tlayout.addView(row8);
                        Tlayout.addView(line4);
                        Tlayout.addView(row9);
                        Tlayout.addView(row10);
                        Tlayout.addView(line5);
                        Tlayout.addView(row11);
                        Tlayout.addView(row12);
                        Tlayout.addView(line6);
                        Tlayout.addView(row13);
                        Tlayout.addView(row14);
                        Tlayout.addView(line7);
                        Tlayout.addView(row15);
                        Tlayout.addView(row16);
                        Tlayout.addView(line8);
                        Tlayout.addView(row17);
                        Tlayout.addView(row18);
                        Tlayout.addView(line9);
                        Tlayout.addView(row19);

                        Contact.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                Uri data = Uri.parse("mailto:"+cv.Email+"?subject=" + Uri.encode("") + "&body=" + Uri.encode(""));
                                intent.setData(data);
                                startActivity(intent);
                            }
                        });

                        layout.addView(Tlayout);
                        layout.addView(s);

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