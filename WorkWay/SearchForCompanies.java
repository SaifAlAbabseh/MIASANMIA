package com.example.workway;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
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

class Request{
    public String RequestedCompanyID;
    public String RequesterEmail;
}
public class SearchForCompanies extends AppCompatActivity {
    DatabaseReference reff;
    DatabaseReference reff2;
    int DBCount=0;
    boolean IfRequestBefore=false;
    public void Refresh(View v){
        finish();
        startActivity(new Intent(SearchForCompanies.this,SearchForCompanies.class));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(SearchForCompanies.this,"Refreshed",Toast.LENGTH_SHORT).show();
            }
        },1500);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        StatusBarColor.ChangeColor(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_search_for_companies);
        reff= FirebaseDatabase.getInstance().getReference().child("CompaniesAds");
        ShowResults();
        reff2= FirebaseDatabase.getInstance().getReference().child("Requests");
        reff2.addValueEventListener(new ValueEventListener() {
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
        startActivity(new Intent(SearchForCompanies.this,TempLoading.class));
    }
    public void GoBack(View v){
        finish();
        startActivity(new Intent(SearchForCompanies.this,MainScreen.class));
    }
    public void ShowResults(){
        readData(new FireBaseCallBack() {
            @Override
            public void onCallBack() {}
        });
    }
    private interface FireBaseCallBack2{
        void onCallBack();
    }
    private void readData2(SearchForCompanies.FireBaseCallBack2 callBack,String tempID){
        reff2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data:snapshot.getChildren()){
                    Request r=data.getValue(Request.class);
                    if(r.RequesterEmail.equals(MainScreen.userEmail) && r.RequestedCompanyID.equals(tempID)){
                        IfRequestBefore=true;
                        break;
                    }
                }
                callBack.onCallBack();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
    private interface FireBaseCallBack{
        void onCallBack();
    }
    private void readData(SearchForCompanies.FireBaseCallBack callBack){
        LinearLayout layout=(LinearLayout)findViewById(R.id.ResultBox);
        reff.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data:snapshot.getChildren()){

                    PostAd company=data.getValue(PostAd.class);
                    TextView Name=new TextView(getApplicationContext());
                    TextView Constraints=new TextView(getApplicationContext());
                    TextView ComName=new TextView(getApplicationContext());
                    TextView ComConstraints=new TextView(getApplicationContext());
                    Button SendCV=new Button(getApplicationContext());
                    TableLayout Tlayout=new TableLayout(getApplicationContext());
                    Tlayout.setLayoutParams(new TableLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));

                    Name.setText("Company Name");
                    Name.setGravity(Gravity.CENTER);
                    Name.setTextSize(20);
                    Name.setTextColor(Color.parseColor("#aad241"));
                    ComName.setText(company.CompanyName);
                    ComName.setTextColor(Color.BLACK);
                    ComName.setTextSize(15);
                    ComName.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.WRAP_CONTENT));

                    Constraints.setText("Description");
                    Constraints.setGravity(Gravity.CENTER);
                    Constraints.setTextSize(20);
                    Constraints.setTextColor(Color.parseColor("#aad241"));

                    ComConstraints.setText(company.CompanyRequirements);
                    ComConstraints.setTextColor(Color.BLACK);
                    ComConstraints.setTextSize(15);
                    ComConstraints.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.WRAP_CONTENT));

                    SendCV.setText("Send CV");
                    SendCV.setTextColor(Color.WHITE);
                    GradientDrawable shape =  new GradientDrawable();
                    shape.setCornerRadius(0);
                    shape.setColor(Color.parseColor("#0162AF"));
                    SendCV.setBackground(shape);
                    SendCV.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    SendCV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            IfRequestBefore=false;
                                if (MainScreen.IfCVCreatedBefore) {
                                    readData2(new FireBaseCallBack2() {
                                        @Override
                                        public void onCallBack() {
                                            if (IfRequestBefore){
                                                Toast.makeText(SearchForCompanies.this,"You've already requested for this company's ads",Toast.LENGTH_SHORT).show();
                                            }
                                            else{
                                                new AlertDialog.Builder(SearchForCompanies.this)
                                                        .setTitle("Check")
                                                        .setMessage("Are you sure?")
                                                        .setIcon(android.R.drawable.stat_sys_warning)
                                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int whichButton) {
                                                                String CompanyID= company.CompanyID;
                                                                String UserEmail=MainScreen.userEmail;
                                                                Request r=new Request();
                                                                r.RequestedCompanyID=CompanyID;
                                                                r.RequesterEmail=UserEmail;
                                                                reff2.child(""+(DBCount+1)).setValue(r);
                                                                startActivity(new Intent(SearchForCompanies.this,TempLoading.class));
                                                                new Handler().postDelayed(new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        Toast.makeText(SearchForCompanies.this,"Successfully sent the CV :)",Toast.LENGTH_SHORT).show();
                                                                    }
                                                                },2000);
                                                            }})
                                                        .setNegativeButton(android.R.string.no, null).show();
                                            }
                                        }
                                    }, company.CompanyID);
                                } else {
                                    Toast.makeText(SearchForCompanies.this, "You did not create a CV", Toast.LENGTH_SHORT).show();
                                }
                        }
                    });
                    Space s=new Space(getApplicationContext());
                    s.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,180));


                    TableRow row1=new TableRow(getApplicationContext());
                    TableRow row2=new TableRow(getApplicationContext());
                    TableRow row3=new TableRow(getApplicationContext());
                    TableRow row4=new TableRow(getApplicationContext());
                    TableRow row5=new TableRow(getApplicationContext());
                    TableRow space1=new TableRow(getApplicationContext());
                    TableRow space2=new TableRow(getApplicationContext());
                    TableRow line=new TableRow(getApplicationContext());
                    TableRow line2=new TableRow(getApplicationContext());


                    Space s2=new Space(getApplicationContext());
                    s2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,50));
                    Space s3=new Space(getApplicationContext());
                    s3.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,50));

                    row1.setGravity(Gravity.CENTER);
                    row2.setGravity(Gravity.CENTER);
                    row3.setGravity(Gravity.CENTER);
                    row4.setGravity(Gravity.CENTER);
                    row5.setGravity(Gravity.CENTER);


                    row1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    row2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    row3.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    row4.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    row5.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    space1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    space2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    line.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    line2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));


                    line.setBackgroundResource(R.color.ProgressBarColor);
                    line2.setBackgroundResource(R.color.ProgressBarColor);

                    row1.addView(Name);
                    row2.addView(ComName);
                    row3.addView(Constraints);
                    row4.addView(ComConstraints);
                    row5.addView(SendCV);
                    space1.addView(s2);
                    space2.addView(s3);
                    TextView t11=new TextView(getApplicationContext());
                    t11.setHeight(2);
                    TextView t22=new TextView(getApplicationContext());
                    t22.setHeight(2);
                    line.addView(t11);
                    line2.addView(t22);

                    Tlayout.setBackgroundResource(R.drawable.borderwithgraybackground);
                    Tlayout.setPadding(10,40,10,40);

                    Tlayout.addView(row1);
                    Tlayout.addView(row2);
                    Tlayout.addView(space1);
                    Tlayout.addView(line);
                    Tlayout.addView(row3);
                    Tlayout.addView(row4);
                    Tlayout.addView(space2);
                    Tlayout.addView(line2);
                    Tlayout.addView(row5);

                    layout.addView(Tlayout);
                    layout.addView(s);

                }
                callBack.onCallBack();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
}