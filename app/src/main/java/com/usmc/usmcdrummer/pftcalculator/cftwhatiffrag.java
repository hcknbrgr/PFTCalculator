package com.usmc.usmcdrummer.pftcalculator;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class cftwhatiffrag extends Fragment implements AdapterView.OnItemSelectedListener
{

    boolean gender = true;
    String agegroup = "";
    int ageGroupPos = 0;
    boolean elevation = true;
    ArrayList<String> scoreArrayList = new ArrayList<>();
    ArrayAdapter<String> scoreAdapter;
    int scoreClass=0;
    int desiredScore=0;
    Spinner scoreSpinner;

    public cftwhatiffrag() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.cftwhatiffrag, container, false);
        //Generate Content in spinner and preselect Radio Button

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





        scoreSpinner = view.findViewById(R.id.score_spinner);
        scoreAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, scoreArrayList);
        scoreAdapter.add("First Class (235+)");
        scoreAdapter.add("Second Class (200–234)");
        scoreAdapter.add("Third Class (150–199)");
        scoreAdapter.add("Select your own score (150-300)");
        scoreSpinner.setAdapter(scoreAdapter);
        scoreSpinner.setOnItemSelectedListener(this);


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

    private void calculateScore(View view)
    {
        int tempACL = 0;

        int tempMUFMin = 0;
        int tempMUFSec = 0;
        int tempMTCMin = 0;
        int tempMTCSec = 0;
        SpannableStringBuilder results = new SpannableStringBuilder();

        tempACL = retrieveValue((EditText) view.findViewById(R.id.ACL_input));
        tempMUFMin = retrieveValue((EditText) view.findViewById(R.id.MUF_minutes_text_input));
        tempMUFSec = retrieveValue((EditText) view.findViewById(R.id.MUF_seconds_text_input));
        tempMTCMin = retrieveValue((EditText) view.findViewById(R.id.MTC_minutes_text_input));
        tempMTCSec = retrieveValue((EditText) view.findViewById(R.id.MTC_seconds_text_input));


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.getActivity());
        if (tempMUFSec > 59 || tempMTCSec > 59) {
            results.append( "Please enter a valid time for seconds (<60)");
        } else {
            CFT cft= new CFT(tempACL, tempMTCMin,tempMTCSec,tempMUFMin, tempMUFSec,gender, ageGroupPos, elevation);
            results.append(cft.getWhatIfResults(scoreClass, desiredScore,agegroup));
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
            case R.id.score_spinner:
                scoreClass = pos; //0=first, 1=second, 2-third, 3-user defined
                if (scoreClass == 0)
                    desiredScore = 235;
                else if (scoreClass == 1)
                    desiredScore = 200;
                else if (scoreClass == 2)
                    desiredScore = 150;
                else if (scoreClass == 3)
                    setUserDefinedScore();
                else {
                    desiredScore = Integer.parseInt(scoreSpinner.getItemAtPosition(4).toString());
                    setScoreClass(desiredScore);
                }
                break;



        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    private void setUserDefinedScore() {
        LayoutInflater alertInflater = LayoutInflater.from(this.getActivity());
        final View inflator = alertInflater.inflate(R.layout.userdefinedscorelayout, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.getActivity());
        alertDialogBuilder.setView(inflator);

        final EditText editTextScore = (EditText) inflator.findViewById(R.id.score_text_input);
        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                final String temp = Integer.toString(desiredScore);
                desiredScore = retrieveValue(editTextScore);

                if ((desiredScore < 150) || (desiredScore > 300)) {
                    Toast.makeText(getActivity(), "Please enter a score between 150 and 300!", Toast.LENGTH_LONG).show();
                    setUserDefinedScore();
                } else {
                    setScoreClass(desiredScore);
                    if (scoreSpinner.getCount() == 5)
                        scoreAdapter.remove(temp);
                    scoreAdapter.add(Integer.toString(desiredScore));
                    scoreSpinner.setSelection(5);
                    scoreAdapter.notifyDataSetChanged();
                }

            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        alertDialog.show();
    }

    private void setScoreClass(int score) {
        if (score >= 235)
            scoreClass = 0;
        else if (score >= 200)
            scoreClass = 1;
        else scoreClass = 2;
    }






}
