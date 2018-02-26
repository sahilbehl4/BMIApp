package com.example.sahilbehl.bmi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LoginActivity extends AppCompatActivity
{

    private EditText editTextUsername;
    private EditText editTextPassword;

    private SQLRecordHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void onLoginClick(View view)
    {
        editTextUsername = (EditText) findViewById(R.id.inputUsername);
        editTextPassword = (EditText) findViewById(R.id.inputPassword);
        databaseHelper = new SQLRecordHelper(LoginActivity.this);

        if(validate())
        {
            Intent homeIntent = new Intent(LoginActivity.this, HomeActivity.class);
            homeIntent.putExtra("uname",editTextUsername.getText().toString().trim());
            startActivity(homeIntent);
            emptyInputEditText();
        }else{
            Toast.makeText(this,"Login has failed", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean validate(){
        boolean valid = true;
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        Pattern usernamepattern = Pattern.compile("[A-Za-z]*");
        Matcher usernamematcher = usernamepattern.matcher(username);
        Pattern passwordpattern = Pattern.compile("[0-9]*");
        Matcher passwordmatcher = passwordpattern.matcher(password);
        if(username.isEmpty()||username.length()>32)
        {
            editTextUsername.setError("Please enter valid username between 1 and 32");
            valid = false;
        }

        if(password.isEmpty()||password.length()>32||password.length()<5)
        {
            editTextPassword.setError("Please enter valid password between 5 and 32");
            valid = false;
        }
        if(!usernamematcher.matches())
        {
            editTextUsername.setError("Only Letters");
            valid = false;
        }
        if(!passwordmatcher.matches())
        {
            editTextPassword.setError("Only Numbers");
            valid = false;
        }
        if(!databaseHelper.checkUser(username, password))
        {
            valid = false;
        }
        return valid;
    }


    private void emptyInputEditText(){
        editTextUsername.setText(null);
        editTextPassword.setText(null);
    }


    public void onSignUpClick(View view) {
        Intent homeIntent = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(homeIntent);
        finish();
    }
}
