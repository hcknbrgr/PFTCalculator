package com.usmc.usmcdrummer.pftcalculator;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

public class pftwhatiffrag extends Fragment implements AdapterView.OnItemSelectedListener {
    boolean gender = true;
    String agegroup = "";
    int ageGroupPos= 0;
    boolean pullupsSelected = true;
    boolean runningSelected = true;
    int scoreClass = 0;
    boolean elevation = true;

    public pftwhatiffrag() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_pftwhatiffrag, container, false);
        //Generate Content in spinner and preselect Radio Button
        Spinner ageSpinner = view.findViewById(R.id.age_spinner);
        ageSpinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity(),
                R.array.age_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ageSpinner.setAdapter(adapter);

        Spinner pushpullSpinner = view.findViewById(R.id.pushpull_spinner);
        pushpullSpinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> pushpulladapter = ArrayAdapter.createFromResource(this.getActivity(),
                R.array.pushpull_array, android.R.layout.simple_spinner_item);
        pushpulladapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pushpullSpinner.setAdapter(pushpulladapter);

        Spinner runrowSpinner = view.findViewById(R.id.runrow_spinner);
        runrowSpinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> runrowadapter = ArrayAdapter.createFromResource(this.getActivity(),
                R.array.runrow_array, android.R.layout.simple_spinner_item);
        runrowadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        runrowSpinner.setAdapter(runrowadapter);

        Spinner scoreSpinner = view.findViewById(R.id.score_spinner);
        scoreSpinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> scoreadapter = ArrayAdapter.createFromResource(this.getActivity(),
                R.array.score_array, android.R.layout.simple_spinner_item);
        scoreadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        scoreSpinner.setAdapter(scoreadapter);

        RadioButton maleRadioButton = view.findViewById(R.id.radio_male);
        maleRadioButton.setChecked(true);

        Button calculateButton = view.findViewById(R.id.calculate_button);
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateScore(view);
            }
        });

        final RadioGroup radioGroup = view.findViewById(R.id.radio_gender);

        radioGroup.setOnCheckedChangeListener(
                new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch(checkedId) {
                            case R.id.radio_male: //Male
                                gender=true;
                                break;
                            case R.id.radio_female: //Female
                                gender=false;
                                break;
                            default:
                                break;
                        }
                    }
                });
        CheckBox checkboxvariable=view.findViewById(R.id.elevation_checkbox);

        checkboxvariable.setOnCheckedChangeListener(
                new CheckBox.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        elevation = !elevation;
                    }
                });

        return view;
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
            case R.id.score_spinner:
                scoreClass = pos; //0=first, 1=second, 2-third
                break;
        }
    }


    public void onNothingSelected(AdapterView<?> parent) {
            // Another interface callback
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
    public void calculateScore(View view){
        int tempPull = 0;

        int tempCrunch = 0;
        int tempRunMin = 0;
        int tempRunSec = 0;
        SpannableStringBuilder results = new SpannableStringBuilder();


        tempPull = retrieveValue((EditText)view.findViewById(R.id.pullups_text_input));
        tempCrunch = retrieveValue((EditText)view.findViewById(R.id.crunches_text_input));
        tempRunMin = retrieveValue((EditText)view.findViewById(R.id.runtime_minutes_text_input));
        tempRunSec = retrieveValue((EditText)view.findViewById(R.id.runtime_seconds_text_input));

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.getActivity());

        if(tempRunSec >59) {
            results.append("Please enter a valid time for seconds (<60)");
        }
        else {
            PFT pft = new PFT(tempPull, pullupsSelected, tempCrunch, tempRunMin, tempRunSec, runningSelected, gender, ageGroupPos, elevation);
            results.append(pft.getWhatIfResults(scoreClass));
        }

        alertDialogBuilder.setMessage(results);
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {}
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}
