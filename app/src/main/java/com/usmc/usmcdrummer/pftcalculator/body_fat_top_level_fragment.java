package com.usmc.usmcdrummer.pftcalculator;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import static android.view.View.GONE;


public class body_fat_top_level_fragment extends Fragment {

    boolean gender=true;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_body_fat_top_level, container, false);
        final EditText heightInput = view.findViewById(R.id.height_input);

        RadioButton maleRadioButton = view.findViewById(R.id.radio_male);
        maleRadioButton.setChecked(true);

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
                                calculateMinMax(view, heightInput.getText());
                                break;
                            case R.id.radio_female: //Female
                                gender = false;
                                tv.setText("Waist: ");
                                lay.setVisibility(View.VISIBLE);
                                abInput.setNextFocusDownId(R.id.hip_input);
                                calculateMinMax(view, heightInput.getText());
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


        heightInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                calculateMinMax(view, s);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });




        return view;
    }

    private void calculateMinMax(View view, CharSequence s){
        TextView minWeight = view.findViewById(R.id.min_weight_profile);
        TextView maxWeight = view.findViewById(R.id.max_weight_profile);
        try {
            double tempHeight = Double.parseDouble(s.toString());
            //todo fix the gender problem
            if ((tempHeight >= 56) && (tempHeight <= 82)) {
                BodyFat bf = new BodyFat();
                minWeight.setText(Integer.toString(bf.getWeightMin((int)Math.round(tempHeight))));
                maxWeight.setText(Integer.toString(bf.getWeightMax((int)Math.round(tempHeight), gender)));
            }
            else
            {
                minWeight.setText("");
                maxWeight.setText("");
            }
        }
        catch(NumberFormatException e){
            minWeight.setText("");
            maxWeight.setText("");
        }
    }

    private void calculateScore(View view){
        Double tempHeight;
        double tempNeck;
        double tempAbs;
        double tempHips;
        int tempWeight;
        tempHeight = retrieveValueDouble((EditText) view.findViewById(R.id.height_input));
        tempNeck = retrieveValueDouble((EditText) view.findViewById(R.id.neck_input));
        tempAbs = retrieveValueDouble((EditText) view.findViewById(R.id.abs_input));
        tempHips= retrieveValueDouble((EditText) view.findViewById(R.id.hip_input));
        tempWeight= retrieveValue((EditText) view.findViewById(R.id.weight_input));

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.getActivity());
        String results;

        BodyFat BF = new BodyFat(tempHeight,tempNeck,tempAbs,tempHips,tempWeight,gender);
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
