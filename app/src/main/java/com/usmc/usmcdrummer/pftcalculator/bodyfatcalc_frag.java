package com.usmc.usmcdrummer.pftcalculator;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import static android.view.View.GONE;

public class bodyfatcalc_frag extends Fragment {

    boolean gender = true;

    public bodyfatcalc_frag() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_bodyfatcalc_frag, container, false);
        final EditText heightInput = view.findViewById(R.id.height_input);

        MainActivity profileGetter = (MainActivity)getActivity();
        String userProfile = profileGetter.getUserProfile();
        String userGender = userProfile.substring(0,1);//0 male 1 female

        RadioButton maleRadioButton = view.findViewById(R.id.radio_male);
        RadioButton femaleRadioButton = view.findViewById(R.id.radio_female);
        if(userGender.equals("0"))
            maleRadioButton.setChecked(true);
        else {
            femaleRadioButton.setChecked(true);
            gender = false;
            TextView tv = view.findViewById(R.id.abs_text);
            LinearLayout lay = view.findViewById(R.id.hips_layout);
            EditText abInput = view.findViewById(R.id.abs_input);
            tv.setText("Waist: ");
            lay.setVisibility(View.VISIBLE);
            abInput.setNextFocusDownId(R.id.hip_input);
        }
        final RadioGroup radioGroup = view.findViewById(R.id.radio_gender);

        radioGroup.setOnCheckedChangeListener(
                new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        TextView tv = view.findViewById(R.id.abs_text);
                        LinearLayout lay = view.findViewById(R.id.hips_layout);
                        EditText abInput = view.findViewById(R.id.abs_input);

                        switch (checkedId) {
                            case R.id.radio_male: //Male
                                gender = true;
                                tv.setText("Abdomen: ");
                                lay.setVisibility(GONE);
                                abInput.setNextFocusDownId(R.id.weight_input);
                                break;
                            case R.id.radio_female: //Female
                                gender = false;
                                tv.setText("Waist: ");
                                lay.setVisibility(View.VISIBLE);
                                abInput.setNextFocusDownId(R.id.hip_input);
                                break;
                            default:
                                break;
                        }
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

    private void calculateScore(View view){
        double tempHeight;
        double tempNeck;
        double tempAbs;
        double tempHips;
        tempHeight = retrieveValueDouble((EditText) view.findViewById(R.id.height_input));
        tempNeck = retrieveValueDouble((EditText) view.findViewById(R.id.neck_input));
        tempAbs = retrieveValueDouble((EditText) view.findViewById(R.id.abs_input));
        tempHips= retrieveValueDouble((EditText) view.findViewById(R.id.hip_input));

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.getActivity());
        String results;

        BodyFat BF = new BodyFat(tempHeight,tempNeck,tempAbs,tempHips,0,gender);
        results = BF.getResults();

        alertDialogBuilder.setMessage(results);
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    private double retrieveValueDouble(EditText edit) {
        double value = 0;
        String userInput = "";
        userInput = edit.getText().toString();
        try {
            value = Double.parseDouble(userInput);
        } catch (NumberFormatException ex) {
            value = 0;
        }
        return value;
    }



}
