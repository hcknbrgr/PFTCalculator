package com.usmc.usmcdrummer.pftcalculator;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableStringBuilder;
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

public class pftwhatiffrag extends Fragment implements AdapterView.OnItemSelectedListener {
    boolean gender = true;
    String agegroup = "";
    int ageGroupPos= 0;
    boolean pullupsSelected = true;
    boolean runningSelected = true;
    int scoreClass = 0;
    int desiredScore = 0;
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

        Button howToButton= view.findViewById(R.id.howto_button);
        howToButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHowTo(view);
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
                scoreClass = pos; //0=first, 1=second, 2-third, 3-user defined
                if(scoreClass == 0)
                    desiredScore=235;
                else if(scoreClass == 1)
                    desiredScore=200;
                else if(scoreClass ==2)
                    desiredScore = 150;
                else if (scoreClass ==3) {
                    setUserDefinedScore();
                }
                break;
        }
    }

    private void setUserDefinedScore()
    {
        LayoutInflater alertInflater = LayoutInflater.from(this.getActivity());
        final View inflator = alertInflater.inflate(R.layout.userdefinedscorelayout, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.getActivity());
        alertDialogBuilder.setView(inflator);
        final EditText editTextScore = (EditText) inflator.findViewById(R.id.score_text_input);

        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //https://stackoverflow.com/questions/6626006/android-custom-dialog-cant-get-text-from-edittext/14091604

                desiredScore = retrieveValue(editTextScore);
                //todo change text to tell user to input between 150-300, if outside those parameters, new alert saying wrong.
                //todo update spinner to reflect change by adding and setting a new line to the score - should allow user to click on new score when another new score is entered, delete previous entries
                setScoreClass(desiredScore);

            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int whichButton) {
        }
    });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }
    private void setScoreClass(int score)
    {
        if (score >= 235)
            scoreClass =0;
        else if (score >=200)
            scoreClass=1;
        else scoreClass = 2;
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
            results.append(pft.getWhatIfResults(scoreClass,desiredScore));
        }

        alertDialogBuilder.setMessage(results);
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {}
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void showHowTo(View view){

        SpannableStringBuilder message = new SpannableStringBuilder();

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.getActivity());
        message.append("Select what score you wish to achieve and leave one, two, or all three events empty.  \n\n" +
                "The calculator will tell you what you need to achieve to get the desired score.");
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {}
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}
