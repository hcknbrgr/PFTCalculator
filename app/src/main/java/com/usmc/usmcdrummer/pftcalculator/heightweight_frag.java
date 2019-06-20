package com.usmc.usmcdrummer.pftcalculator;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

public class heightweight_frag extends Fragment implements AdapterView.OnItemSelectedListener {

    boolean gender=true;
    String agegroup = "";
    int ageGroupPos = 0;
    boolean heightValid = false;
    View rootView;
    int PFTCFTClassPos = 0;


    public heightweight_frag() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_height_weight_frag, container, false);
        rootView = view;
        final EditText heightInput = view.findViewById(R.id.height_input);

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

        Spinner PFTSpinner = view.findViewById(R.id.pftcft_scores);
        PFTSpinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> PFTAdapter = ArrayAdapter.createFromResource(this.getActivity(),
                R.array.pftcft_array, android.R.layout.simple_spinner_item);
        PFTAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        PFTSpinner.setAdapter(PFTAdapter);

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
                                calculateMinMax(view, heightInput.getText());
                                break;
                            case R.id.radio_female: //Female
                                gender = false;
                                calculateMinMax(view, heightInput.getText());
                                break;
                            default:
                                break;
                        }
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {

        EditText heightInput = rootView.findViewById(R.id.height_input);

        switch (parent.getId()) {
            case R.id.age_spinner:
                agegroup = parent.getItemAtPosition(pos).toString();
                ageGroupPos = pos;
                calculateMinMax(rootView,heightInput.getText());
                break;
            case R.id.pftcft_scores:
                PFTCFTClassPos = pos;
                calculateMinMax(rootView, heightInput.getText());
                break;
        }
    }
    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    private void calculateMinMax(View view, CharSequence s){
        TextView minWeight = view.findViewById(R.id.min_weight_profile);
        TextView maxWeight = view.findViewById(R.id.max_weight_profile);
        TextView maxBodyFat = view.findViewById(R.id.max_bf_allowed);

        try {
            double tempHeight = Double.parseDouble(s.toString());
            if ((tempHeight >= 56) && (tempHeight <= 82)) {
                BodyFat bf = new BodyFat();
                minWeight.setText(Integer.toString(bf.getWeightMin((int)Math.round(tempHeight))));
                maxWeight.setText(Integer.toString(bf.getWeightMax((int)Math.round(tempHeight), gender)));

                if(PFTCFTClassPos==0)
                    maxBodyFat.setText(Integer.toString(bf.getBodyFatMax(ageGroupPos,gender)));
                else if(PFTCFTClassPos==1)
                    maxBodyFat.setText(Integer.toString(bf.getBodyFatMax(ageGroupPos,gender)+1));
                else
                    maxBodyFat.setText("N/A");

                heightValid = true;
            }
            else
            {
                heightValid = false;
                minWeight.setText("");
                maxWeight.setText("");
                maxBodyFat.setText("");
            }
        }
        catch(NumberFormatException e){
            heightValid = false;
            minWeight.setText("");
            maxWeight.setText("");
            maxBodyFat.setText("");
        }
    }
}
