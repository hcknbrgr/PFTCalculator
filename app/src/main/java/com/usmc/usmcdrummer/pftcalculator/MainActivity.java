/*
Schedule of Releases:
**** 0.9.1 - Consolidate Push ups and Pullups into one input field changed by drop down -- DONE 20181125
**** 0.9.1 - Remove or alter the banner.
**** 0.9.1 - Change results to show only performed exercise
**** 0.9.1 - Correct XML Coding with moving strings to strings.xml
**** 0.9.1 - Experiment with MarginStart for textInputs and match_content for linearlayout for app
**** 0.9.1 - ****** RELEASE*********

**** 0.9.2 - Add Rowing results into calculator
**** 0.9.2 - Check rowtime and rowtime with elevation
**** 0.9.2 - Make formatting good.
**** 0.9.2 - Modify text color to be consistent throughout push/pull spinner -- make this grey?
**** 0.9.2 - Remove autofocus from edittext
**** 0.9.2 - *******RELEASE AND TEST!*****

**** 0.9.3 - Add "What-if" calculator that calculates required repetitions or time to score desired class
**** 0.9.3 - Change results to be in order of events
**** ***** - RELEASE

**** 1.0.0 - Get rid of toast
**** 1.0.0 - Format what if run and row time to have 2 digits for seconds
**** 1.0.0 - Add SpannableStringBuilder to format BOLD for required scores to make easier to read
**** 1.0.0 - Update with new 2019 standards per MCO 6100.13A W/ch 1
**** 1.0.0 - Update sceenshots
**** 1.0.0 - Write meaningful information on storefront
**** 1.0.0 - *******RELEASE**********

**** 1.1.0 - Add app bar back in
**** 1.1.0 - Change font color and center text in tool bar
**** 1.1.0 - Add shading in drop downs to be light gray, find light gray color, find out how to keep arrow on dropdown
TODO 1.1.0 - Make white go all the way to the bottom
TODO 1.1.0 - Make tabs to change between calculator and what-if
TODO 1.1.0 - Add Bottom red background again
TODO 1.1.0 - TEST AND RELEASE

TODO 1.2.0 - Restructure PFT.java to be more usable and take up less space?
TODO 1.2.0 - RELEASE

TODO 2.0.0 - Make a bottom menu bar to switch activities to include CFT and coming soon Body Fat
TODO 2.0.0 - Add CFT Calculator

TODO 3.0.0 - Add Body Fat Calculator

TODO 4.0.0 - Add access to charts as data tables
 */

package com.usmc.usmcdrummer.pftcalculator;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    boolean gender = true;
    String agegroup = "";
    int ageGroupPos= 0;
    boolean pullupsSelected = true;
    boolean runningSelected = true;
    private Toolbar toolbar;
    boolean elevation = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
       // toolbar.setTitleTextColor(Color.parseColor("#FFD700"));


        //Generate Content in spinner and preselect Radio Button
        Spinner ageSpinner = findViewById(R.id.age_spinner);
        ageSpinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.age_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ageSpinner.setAdapter(adapter);

        Spinner pushpullSpinner = findViewById(R.id.pushpull_spinner);
        pushpullSpinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> pushpulladapter = ArrayAdapter.createFromResource(this,
                R.array.pushpull_array, android.R.layout.simple_spinner_item);
        pushpulladapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pushpullSpinner.setAdapter(pushpulladapter);

        Spinner runrowSpinner = findViewById(R.id.runrow_spinner);
        runrowSpinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> runrowadapter = ArrayAdapter.createFromResource(this,
                R.array.runrow_array, android.R.layout.simple_spinner_item);
        runrowadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        runrowSpinner.setAdapter(runrowadapter);

        RadioButton maleRadioButton = findViewById(R.id.radio_male);
        maleRadioButton.setChecked(true);

    }

    private int retrieveValue(EditText edit){
        int value =0;
        String userInput = "";
        userInput = edit.getText().toString();
        try{
            value = Integer.parseInt(userInput);
        }catch(NumberFormatException ex){
            value = 0;
        }
        return value;
    }

    public void showWhatIf(View view){
        Intent intent = new Intent(this, WhatIfCalculator.class);
        startActivity(intent);
    }

    public void calculateScore(View view){

        int tempPull = 0;

        int tempCrunch = 0;
        int tempRunMin = 0;
        int tempRunSec = 0;

        tempPull = retrieveValue((EditText)findViewById(R.id.pullups_text_input));
        tempCrunch = retrieveValue((EditText)findViewById(R.id.crunches_text_input));
        tempRunMin = retrieveValue((EditText)findViewById(R.id.runtime_minutes_text_input));
        tempRunSec = retrieveValue((EditText)findViewById(R.id.runtime_seconds_text_input));

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        String results;
        if(tempRunSec >59) {
            results = "Please enter a valid time for seconds (<60)";
        }
        else {
            PFT pft = new PFT(tempPull, pullupsSelected, tempCrunch, tempRunMin, tempRunSec, runningSelected, gender, ageGroupPos, elevation);
            results = pft.getResults(agegroup);
        }

        alertDialogBuilder.setMessage(results);
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {}
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        switch (parent.getId()) {
            case R.id.age_spinner:
                agegroup = parent.getItemAtPosition(pos).toString();
                ageGroupPos = pos;
                break;
            case R.id.pushpull_spinner:
                if (parent.getItemAtPosition(pos).toString().equals("Pullups"))
                    pullupsSelected = true;
                else pullupsSelected = false;
                break;
            case R.id.runrow_spinner:
                if (parent.getItemAtPosition(pos).toString().equals("Row Time"))
                    runningSelected = false;
                else runningSelected = true;
                break;
        }

    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_male:
                if (checked)
                    gender=true;
                break;
            case R.id.radio_female:
                if (checked)
                    // female
                    gender=false;
                break;
        }
    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();
        if(checked)
            elevation=false;
        else
            elevation=true;

    }

}
