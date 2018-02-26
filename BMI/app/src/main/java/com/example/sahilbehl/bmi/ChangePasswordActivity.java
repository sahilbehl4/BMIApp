package com.example.sahilbehl.bmi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText editTextOldPassword;
    private EditText editTextNewPassword;
    private EditText editTextNewRePassword;

    private String oldPassword;
    private String newPassword;
    private String newRePassword;
    String uname;

    private SQLRecordHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        Bundle userData = getIntent().getExtras();
        if(userData!=null)
        {
            uname = userData.getString("uname");
            Log.d("username",uname);
        }
    }

    public void onChangePassword(View v){
        editTextOldPassword = (EditText) findViewById(R.id.inputOldPassword);
        editTextNewPassword = (EditText) findViewById(R.id.inputNewPassword);
        editTextNewRePassword = (EditText) findViewById(R.id.inputNewRePassword);
        oldPassword = editTextOldPassword.getText().toString().trim();
        newPassword = editTextNewPassword.getText().toString().trim();
        newRePassword = editTextNewRePassword.getText().toString().trim();

        databaseHelper = new SQLRecordHelper(ChangePasswordActivity.this);

        if(validate())
        {
            databaseHelper.onChangePassword(newPassword);
            Intent loginIntent1 = new Intent(ChangePasswordActivity.this, LoginActivity.class);
            startActivity(loginIntent1);
            Toast.makeText(this,"Password Changed", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this,"Password Change failed", Toast.LENGTH_SHORT).show();
        }

    }

    public void onHomeClick(View v){
        Intent homeIntent1 = new Intent(ChangePasswordActivity.this, HomeActivity.class);
        startActivity(homeIntent1);
    }

    public boolean validate()
    {
        Pattern passwordpattern = Pattern.compile("[0-9]*");
        Matcher passwordmatcher = passwordpattern.matcher(newPassword);
        boolean valid = true;
        if(oldPassword.isEmpty()||oldPassword.length()>32||oldPassword.length()<5)
        {
            editTextOldPassword.setError("Please enter valid password between 5 and 32");
            valid = false;
        }
        if(newPassword.isEmpty()||newPassword.length()>32||newPassword.length()<5)
        {
            editTextNewPassword.setError("Please enter valid password between 5 and 32");
            valid = false;
        }
        if(newRePassword.isEmpty()||newRePassword.length()>32||newRePassword.length()<5)
        {
            editTextNewRePassword.setError("Please enter valid password between 5 and 32");
            valid = false;
        }
        if(!newPassword.contentEquals(newRePassword))
        {
            editTextNewPassword.setError("Passwords do not match");
            valid = false;
        }
        if(!passwordmatcher.matches())
        {
            editTextNewPassword.setError("Only Numbers");
            valid = false;
        }
        if(!databaseHelper.checkPassword(uname,oldPassword))
        {
            editTextOldPassword.setError("Invalid Previous Password");
            valid = false;
        }

        return valid;
    }
}
