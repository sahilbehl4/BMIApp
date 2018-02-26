package com.example.sahilbehl.bmi;

import android.content.Context;
import android.os.Bundle;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CalculateActivity extends AppCompatActivity {

    String uname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate);
        Bundle userData = getIntent().getExtras();
        if(userData!=null)
        {
            uname = userData.getString("uname");
        }
    }

    String res;
    String heighttxt;
    String weighttxt;
    private SQLRecordHelper databaseHelper;

    public void onCalClick(View v)
    {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        if(validate())
        {
            calculate();
        }
        else{
            Toast.makeText(this,"Calculation has failed", Toast.LENGTH_SHORT).show();
        }

    }

    public boolean validate(){
        boolean valid = true;
        EditText height = (EditText) findViewById(R.id.inputHeight);
        heighttxt =  height.getText().toString();
        EditText weight = (EditText) findViewById(R.id.inputWeight);
        weighttxt =  weight.getText().toString();
        Double heightAsInt = Double.parseDouble(heighttxt);
        Double WeightAsInt = Double.parseDouble(weighttxt);
        if(heighttxt.isEmpty()||heighttxt.length()>3||heightAsInt > 300 || heightAsInt < 20)
        {
            height.setError("Please enter valid height between 20 and 300 cm");
            valid = false;
        }

        if(weighttxt.isEmpty()||weighttxt.length()>3||WeightAsInt > 300 || WeightAsInt < 3)
        {
            weight.setError("Please enter valid 2ight between 3 and 300 kg");
            valid = false;
        }

        return valid;
    }

    public void calculate()
    {
        Double heightAsInt = Double.parseDouble(heighttxt);
        heightAsInt = heightAsInt/100;
        Double WeightAsInt = Double.parseDouble(weighttxt);
        Double calc= (WeightAsInt/ (heightAsInt* heightAsInt));

        TextView result = (TextView) findViewById(R.id.bmi);
        // use DecimalFormat("0.##") to limit to 2 decimal places
        calc = Math.round(calc*100)/100.0d;
        res = calc.toString();
        result.setText("BMI = " + res);
    }

    public void onSaveClick(View v)
    {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        if(validate())
        {
            calculate();
            databaseHelper = new SQLRecordHelper(CalculateActivity.this);
            if(databaseHelper.addRecord(heighttxt,weighttxt,res,uname))
            {
                Toast.makeText(this,"Saved", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this,"Saving has failed", Toast.LENGTH_SHORT).show();
            }

        }else{
            Toast.makeText(this,"Saving has failed", Toast.LENGTH_SHORT).show();
        }
    }


    public void onBackClick(View v)
    {
        Intent backIntent1 = new Intent(CalculateActivity.this, HomeActivity.class);
        backIntent1.putExtra("uname",uname);
        startActivity(backIntent1);
    }
}
