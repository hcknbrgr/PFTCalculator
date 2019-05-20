package com.usmc.usmcdrummer.pftcalculator;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
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



public class cftfrag extends Fragment implements AdapterView.OnItemSelectedListener {

    boolean gender = true;
    String agegroup = "";
    int ageGroupPos = 0;
    boolean elevation = true;

    public cftfrag() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.cftfrag, container, false);

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

        RadioButton maleRadioButton = view.findViewById(R.id.radio_male);
        RadioButton femaleRadioButton = view.findViewById(R.id.radio_female);
        if(userGender.equals("0"))
            maleRadioButton.setChecked(true);
        else {
            femaleRadioButton.setChecked(true);
            gender = false;
        }
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

        final EditText MTCMinET = view.findViewById(R.id.MTC_minutes_text_input);
        final EditText MTCSecET = view.findViewById(R.id.MTC_seconds_text_input);
        final EditText MUFMinET = view.findViewById(R.id.MUF_minutes_text_input);
        final EditText MUFSecET = view.findViewById(R.id.MUF_seconds_text_input);

        MTCMinET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(MTCMinET.getText().toString().length()==1)
                    MTCSecET.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        MUFMinET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(MUFMinET.getText().toString().length()==1)
                    MUFSecET.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }

    private void calculateScore(View view){
        int tempACL = 0;

        int tempMUFMin = 0;
        int tempMUFSec = 0;
        int tempMTCMin = 0;
        int tempMTCSec = 0;

        tempACL = retrieveValue((EditText) view.findViewById(R.id.ACL_input));
        tempMUFMin = retrieveValue((EditText) view.findViewById(R.id.MUF_minutes_text_input));
        tempMUFSec = retrieveValue((EditText) view.findViewById(R.id.MUF_seconds_text_input));
        tempMTCMin = retrieveValue((EditText) view.findViewById(R.id.MTC_minutes_text_input));
        tempMTCSec = retrieveValue((EditText) view.findViewById(R.id.MTC_seconds_text_input));

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.getActivity());
        String results;
        if (tempMUFSec > 59 || tempMTCSec > 59) {
            results = "Please enter a valid time for seconds (<60)";
        } else {
            CFT cft= new CFT(tempACL, tempMTCMin,tempMTCSec,tempMUFMin, tempMUFSec,gender, ageGroupPos, elevation);
            results = cft.getResults(agegroup);
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        switch (parent.getId()) {
            case R.id.age_spinner:
                agegroup = parent.getItemAtPosition(pos).toString();
                ageGroupPos = pos;
                break;

        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }


}
