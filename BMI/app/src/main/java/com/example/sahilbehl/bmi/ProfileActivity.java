package com.example.sahilbehl.bmi;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {

    String uname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        TextView name = (TextView) findViewById(R.id.name);
        TextView username = (TextView) findViewById(R.id.username);
        TextView healthNumber = (TextView) findViewById(R.id.healthNumber);
        TextView dob = (TextView) findViewById(R.id.dob);

        Bundle userData = getIntent().getExtras();
        if(userData!=null)
        {
            uname = userData.getString("uname");
            Log.d("username",uname);
        }

        SQLRecordHelper databaseHelper = new SQLRecordHelper(ProfileActivity.this);
        Cursor data = databaseHelper.getUser(uname);
        if(data!=null) {
            while (data.moveToNext()) {

                name.setText("Username = " + data.getString(2));
                username.setText("Name = " + data.getString(1));
                healthNumber.setText("Health Card Number = " + data.getString(4));
                dob.setText("DOB = " + data.getString(5));
            }
        }

    }

    public void onClickSignOut(View v)
    {
        Intent signoutIntent = new Intent(ProfileActivity.this, LoginActivity.class);
        signoutIntent.putExtra("uname",uname);
        startActivity(signoutIntent);
    }

    public void onClickChange(View v)
    {
        Intent changeIntent = new Intent(ProfileActivity.this, ChangePasswordActivity.class);
        changeIntent.putExtra("uname",uname);
        startActivity(changeIntent);
    }
}
