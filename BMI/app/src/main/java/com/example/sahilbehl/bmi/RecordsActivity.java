package com.example.sahilbehl.bmi;


import android.content.Context;
import android.os.Bundle;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ListView;
import android.widget.Toast;
import android.view.View;

import java.util.ArrayList;

public class RecordsActivity extends AppCompatActivity {

    String uname;
    ListView listView;
    SQLiteDatabase sqLiteDatabase;
    SQLRecordHelper db;
    Cursor cursor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Bundle userData = getIntent().getExtras();
        if(userData!=null)
        {
            uname = userData.getString("uname");
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);
        SQLRecordHelper databaseHelper = new SQLRecordHelper(RecordsActivity.this);
        listView = (ListView) findViewById(R.id.listview);

        ArrayList<String> theList = new ArrayList<>();
        Cursor data = databaseHelper.getRecords(uname);

        if(data.getCount() == 0)
        {
            Toast.makeText(RecordsActivity.this , "No data found", Toast.LENGTH_LONG).show();
        }
        else
        {
            while(data.moveToNext())
            {
                String item = "Height = " + data.getString(1) + "cm\n" +
                        "Weight = " + data.getString(2) + "Kg\n" +
                        "BMI = " + data.getString(3) + "\n" +
                        "Date = " + data.getString(4).substring(0,11);
                theList.add(item);
                ListAdapter listAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,theList);
                listView.setAdapter(listAdapter);
            }
        }
    }

    public void onBackClick(View v)
    {
        Intent backIntent = new Intent(RecordsActivity.this, HomeActivity.class);
        backIntent.putExtra("uname",uname);
        startActivity(backIntent);
    }
}
