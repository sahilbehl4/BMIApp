package com.example.sahilbehl.bmi;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import android.view.View.OnClickListener;

import java.util.Calendar;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "SignUpActivity";
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    private EditText editTextUsername;
    private EditText editTextPassword;
    private EditText editTextName;
    private EditText editTextRePassword;
    private EditText editTextHealthNumber;
    private TextView textDOB;

    String username ;
    String name ;
    String password;
    String repassword ;
    String dob ;
    String healthnumber;

    private SQLRecordHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Button button= (Button) findViewById(R.id.signupButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSignClick(v);
            }
        });
        TextView loginText = (TextView) findViewById(R.id.loginText);
        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLoginTextClick(v);
            }
        });

        editTextUsername = (EditText) findViewById(R.id.inputUsername);
        editTextName = (EditText) findViewById(R.id.inputName);
        editTextPassword = (EditText) findViewById(R.id.inputPassword);
        editTextRePassword = (EditText) findViewById(R.id.inputRePassword);
        editTextHealthNumber = (EditText) findViewById(R.id.inputHealthNumber);
        textDOB = (TextView) findViewById(R.id.inputDOB);

        textDOB.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        SignUpActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);

                String date = month + "/" + day + "/" + year;
                textDOB.setText(date);
            }
        };


    }

    public void onSignClick(View v)
    {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        databaseHelper = new SQLRecordHelper(SignUpActivity.this);

        if(validate())
        {
            ;
            if(databaseHelper.addUser(username,password,name,dob,healthnumber))
            {
                Toast.makeText(this,"Sign Up Successful", Toast.LENGTH_SHORT).show();
                Intent loginIntent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(loginIntent);
                emptyInputEditText();
            }else{
                Toast.makeText(this,"SignUp has failed", Toast.LENGTH_SHORT).show();
            }

        }else{
            Toast.makeText(this,"SignUp has failed", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean validate(){
        boolean valid = true;
         username = editTextUsername.getText().toString().trim();
         name = editTextName.getText().toString().trim();
         password = editTextPassword.getText().toString().trim();
         repassword = editTextRePassword.getText().toString().trim();
         dob = textDOB.getText().toString().trim();
         healthnumber = editTextHealthNumber.getText().toString().trim();

        Pattern usernamepattern = Pattern.compile("[A-Za-z]*");
        Matcher usernamematcher = usernamepattern.matcher(username);
        Pattern passwordpattern = Pattern.compile("[0-9]*");
        Matcher passwordmatcher = passwordpattern.matcher(password);
        Matcher healthmatcher = passwordpattern.matcher(healthnumber);
        Pattern namepattern = Pattern.compile("[A-z ]*");
        Matcher namematcher = namepattern.matcher(name);

        if(username.isEmpty()||username.length()>32)
        {
            editTextUsername.setError("Please enter valid username between 1 and 32");
            valid = false;
        }

        if(name.isEmpty()||username.length()>32)
        {
            editTextName.setError("Please enter valid name between 1 and 32");
            valid = false;
        }

        if(password.isEmpty()||password.length()>32||password.length()<5)
        {
            editTextPassword.setError("Please enter valid password between 5 and 32");
            editTextPassword.setText(null);
            editTextRePassword.setText(null);
            valid = false;
        }

        if(dob.isEmpty())
        {
            textDOB.setError("Please enter valid date");
            valid = false;
        }
        if(healthnumber.isEmpty()||healthnumber.length()!=10)
        {
            editTextHealthNumber.setError("Please enter valid health card number of 10 digits");
            valid = false;
        }

        if(!usernamematcher.matches())
        {
            editTextUsername.setError("Only Letters");
            valid = false;
        }
        if(!namematcher.matches())
        {
            editTextName.setError("Only Letters and spaces");
            valid = false;
        }
        if(!passwordmatcher.matches())
        {
            editTextPassword.setError("Only Numbers");
            editTextPassword.setText(null);
            editTextRePassword.setText(null);
            valid = false;
        }

        if(!healthmatcher.matches())
        {
            editTextHealthNumber.setError("Only Numbers");
            valid = false;
        }

        if(!password.contentEquals(repassword))
        {
            editTextPassword.setError("Passwords do not match");
            valid = false;
        }

        if (databaseHelper.checkUser(username) ){
            editTextUsername.setError("Username already exists");
            valid = false;
        }

        return valid;
    }
    private void emptyInputEditText(){
        editTextUsername.setText(null);
        editTextPassword.setText(null);
    }

    public void onLoginTextClick(View view){
        Intent loginIntent = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(loginIntent);
        finish();
    }
}
