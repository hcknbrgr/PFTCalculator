package com.usmc.usmcdrummer.pftcalculator;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import android.widget.TextView;


public class pftfrag extends Fragment implements AdapterView.OnItemSelectedListener {
    boolean gender = true;
    String agegroup = "";
    int ageGroupPos = 0;
    boolean pullupsSelected = true;
    boolean runningSelected = true;
    boolean elevation = true;
    boolean plankSelected = false;
    View rootView;

    public pftfrag() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_pftfrag, container, false);
        rootView = view;
        MainActivity profileGetter = (MainActivity)getActivity();
        String userProfile = profileGetter.getUserProfile();
        String userGender = userProfile.substring(0,1);//0 male 1 female
        String userAge = userProfile.substring(1);//position of spinner

        Spinner ageSpinner = view.findViewById(R.id.age_spinner);
        ageSpinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity(),
                R.array.age_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ageSpinner.setAdapter(adapter);
        ageSpinner.setSelection(Integer.parseInt(userAge));

        Spinner crunchSpinner = view.findViewById(R.id.crunchplank_spinner);
        crunchSpinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> crunchAdapter = ArrayAdapter.createFromResource(this.getActivity(),R.array.crunchplank_array, android.R.layout.simple_spinner_item);
        crunchAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        crunchSpinner.setAdapter(crunchAdapter);

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

        RadioButton maleRadioButton = view.findViewById(R.id.radio_male);
        RadioButton femaleRadioButton = view.findViewById(R.id.radio_female);
        if(userGender.equals("0"))
            maleRadioButton.setChecked(true);
        else {
            femaleRadioButton.setChecked(true);
            gender = false;
        }
        final EditText runMinEditText = view.findViewById(R.id.runtime_minutes_text_input);
        final EditText runSecEditText = view.findViewById(R.id.runtime_seconds_text_input);
        runMinEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(runMinEditText.getText().toString().length()==2)
                    runSecEditText.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        final EditText plankMinutes = view.findViewById(R.id.crunches_text_input);
        final EditText plankSeconds = view.findViewById(R.id.plank_seconds);
        plankMinutes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(plankSelected)
                  if(plankMinutes.getText().toString().length()==1)
                        plankSeconds.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        final RadioGroup radioGroup = view.findViewById(R.id.radio_gender);

        radioGroup.setOnCheckedChangeListener(
                new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch (checkedId) {
                            case R.id.radio_male: //Male
                                gender = true;
                                break;
                            case R.id.radio_female: //Female
                                gender = false;
                                break;
                            default:
                                break;
                        }
                    }
                });

        CheckBox checkboxvariable = view.findViewById(R.id.elevation_checkbox);
        checkboxvariable.setOnCheckedChangeListener(
                new CheckBox.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        elevation = !elevation;
                    }
                });

        Button calculateButton = view.findViewById(R.id.calculate_button);
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateScore(view);
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
            case R.id.crunchplank_spinner:
                if (parent.getItemAtPosition(pos).toString().equals("Plank Time"))
                {
                    plankSelected = true;
                    TextView colon = rootView.findViewById(R.id.plank_colon);
                    colon.setVisibility(View.VISIBLE);
                    EditText plankSeconds = rootView.findViewById(R.id.plank_seconds);
                    plankSeconds.setVisibility(View.VISIBLE);
                    EditText crunchInput = rootView.findViewById(R.id.crunches_text_input);
                    crunchInput.setNextFocusDownId(R.id.plank_seconds);
                    crunchInput.requestFocus();
                }
                else {
                    plankSelected = false;
                    TextView colon = rootView.findViewById(R.id.plank_colon);
                    colon.setVisibility(View.GONE);
                    EditText plankSeconds = rootView.findViewById(R.id.plank_seconds);
                    plankSeconds.setVisibility(View.GONE);
                    EditText crunchInput = rootView.findViewById(R.id.crunches_text_input);
                    crunchInput.setNextFocusDownId(R.id.runtime_minutes_text_input);
                }
        }

    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    private int retrieveValue(EditText edit) {
        int value = 0;
        String userInput = "";
        userInput = edit.getText().toString();
        try {
            value = Integer.parseInt(userInput);
        } catch (NumberFormatException ex) {
            value = 0;
        }
        return value;
    }


    public void calculateScore(View view) {

        int tempPull = 0;

        int tempCrunch = 0;
        int tempPlankSeconds = 0;

        int tempRunMin = 0;
        int tempRunSec = 0;


        tempPull = retrieveValue((EditText) view.findViewById(R.id.pullups_text_input));
        tempCrunch = retrieveValue((EditText) view.findViewById(R.id.crunches_text_input));
        tempPlankSeconds = retrieveValue((EditText) view.findViewById(R.id.plank_seconds));
        tempRunMin = retrieveValue((EditText) view.findViewById(R.id.runtime_minutes_text_input));
        tempRunSec = retrieveValue((EditText) view.findViewById(R.id.runtime_seconds_text_input));



        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.getActivity());
        String results;
        if (tempRunSec > 59 || tempPlankSeconds > 59) {
            results = "Please enter a valid time for seconds (<60)";
        } else {
            PFT pft = new PFT(tempPull, pullupsSelected, tempCrunch,tempPlankSeconds,plankSelected, tempRunMin, tempRunSec, runningSelected, gender, ageGroupPos, elevation);
            results = pft.getResults(agegroup);
        }

        alertDialogBuilder.setMessage(results);
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }




}
