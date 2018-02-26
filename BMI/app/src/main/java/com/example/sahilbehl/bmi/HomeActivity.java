package com.example.sahilbehl.bmi;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {

    SQLiteDatabase sqLiteDatabase;
    SQLRecordHelper db;
    String uname;
    String BMI;
    TextView bmi;
    TextView health;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        SQLRecordHelper databaseHelper = new SQLRecordHelper(HomeActivity.this);
        bmi = (TextView) findViewById(R.id.textBMI);
        health = (TextView) findViewById(R.id.textHealth);
        Bundle userData = getIntent().getExtras();
        if(userData!=null)
        {
            uname = userData.getString("uname");
        }

        Cursor data = databaseHelper.getRecords(uname);
        if(data.getCount() > 0)
        {
            while(data.moveToNext())
            {
                BMI = data.getString(3);
                Log.d("BMI", BMI);
            }
        }

        setBmi();

    }


    protected void onStart(Bundle savedInstanceState){
       setBmi();
    }
    public void setBmi()
    {
        if(BMI!=null)
        {
            bmi.setText(BMI);
            double value = Double.parseDouble(BMI);
            if(value <= 18.5)
            {
                health.setText("Underweight");
            }
            else if((value <= 24.9) && (value > 18.5))
            {
                health.setText("Normal weight");
            }
            else if((value <= 29.9) && (value >  24.9))
            {
                health.setText("Overweight");
            }
            else
            {
                health.setText("Obesity");
            }
        }
    }
    public void onCalculateClick(View v)
    {
        Intent calculateIntent = new Intent(HomeActivity.this, CalculateActivity.class);
        calculateIntent.putExtra("uname",uname);
        startActivity(calculateIntent);
    }

    public void onRecordsClick(View v)
    {
        Intent calculateIntent = new Intent(HomeActivity.this, RecordsActivity.class);
        calculateIntent.putExtra("uname",uname);
        startActivity(calculateIntent);
    }

    public void onProfileClick(View v)
    {
        Intent profileIntent = new Intent(HomeActivity.this, ProfileActivity.class);
        profileIntent.putExtra("uname",uname);
        startActivity(profileIntent);

    }
}
