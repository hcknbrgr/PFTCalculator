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
**** 1.1.0 - Add Bottom red background again
**** 1.1.0 - Change What-If to match original
**** 1.1.0 - TEST AND RELEASE I already changed Gradle for version number


**** 1.2.0 - Make tabs to change between calculator and what-if
**** 1.2.0 - Update Gradle Info
**** 1.2.0 - Test and Release

**** 1.2.1 - Add how to button in whatif calculator
**** 1.2.1 - Update Gradle
**** 1.2.1 - RELEASE

**** 1.3.0 - Add an option to get any score? -- add other to Spinner, create popup to insert user generated score
**** 1.3.0 - Fix 0 for event but still passing bug in whatif (mandatory pass)
**** 1.3.0 - Add obtained score in whatif, if goal is 3rd or 2nd class and obtained is higher than requested
**** 1.3.0 - Update Gradle
**** 1.3.0 - RELEASE

**** 2.0.0 - Make a bottom menu bar to switch activities to include CFT and coming soon Body Fat
**** 2.0.0 - Update the APP Title in the Manifest
**** 2.0.0 - Add CFT Calculator
**** 2.0.0 - Update Screen Shots
**** 2.0.0 - Update Gradle
**** 2.0.0 - RELEASE!

**** 2.1.0 - Add Body Fat Calculator
**** 2.1.0 - Update Gradle
**** 2.1.0 - Release

**** 2.2.0 - Test out autoswitch to next field for minutes to seconds
**** 2.2.0 - update gradle
**** 2.2.0 - release

**** 3.0.0 - Add CFT Whatif
**** 3.0.0 - Update focus on Body fat calc for Male
**** 3.0.0 - Update results message for PFT what-if to include gender and age group
**** 3.0.0 - Fix CFT Elevation scores.  It alters the actual time when entered in elevation to factor in for scores, make temp
**** 3.0.0 - Update Gradle
**** 3.0.0 - Update screen shots
**** 3.0.0 - Release

**** 3.1.0 - Height and Weight minmax update
**** 3.1.0 - Have PFT or CFT autoload based on time of the year?

**** 3.2.0 - Make user profile

**** 3.3.0 - Shift Height and weight into 2 tabs.  One for ht weight and body fat allowed. one for actual calculator
**** 3.3.0 - Fixed Glitch in 51+ that would cause app to crash when entered max pullups score
**** 3.3.0 - Fix Run Time display in PFT WHATIF

TODO - 3.4.0 - Create PFT Calc plank update
             - Create PFT What-if plank update

TODO - 3.4.1 - Add about page

TODO - 3.4.2 - Show max and min reps next to each items - maybe as hint texts? I think making it a small button will be best for now.

TODO - 3.5.0 - Refactor CFT tables if possible

TODO - 4.0.0 - Make it so that the activities don't restart -- pause activity when activating another one?

TODO - 5.0.0 - Add access to charts -- TableLayout?
             - come up with some different layout options, 4th Nav at bottom?

TODO - 6.0.0 - Add tracking log

TODO - X.X.X - Update what-if and body fat calculators to include "Done" button on keyboard?

*/

package com.usmc.usmcdrummer.pftcalculator;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity {

    private static final String FILE_NAME = "user_profile.txt";
    private String userProfile = "00"; //digit 1: Gender (0 male, 1 female), digit 2: Age group position
    private String tempProfile = "00";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        load();
        Log.e("USER PROFILE LOADED: ", userProfile);
        Toolbar myToolbar = findViewById(R.id.toolbar);

        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("");

        // Retrieve a reference to the BottomNavigationView and listen for click events.
        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.getMenu().findItem(R.id.action_PFT).setTitle("PFT");
        bottomNav.getMenu().findItem(R.id.action_BF).setTitle("Ht/Wt");
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        Calendar today = Calendar.getInstance();
        if(today.get(Calendar.MONTH)<=5)
            getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_host, new pft_top_level_fragment())
                .commit();
        else
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_host, new cft_top_level_fragment())
                    .commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.userprofile, menu);
        return true;
    }

    public String getUserProfile(){
        return userProfile;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.menuProfileItem) {

            LayoutInflater inflater = this.getLayoutInflater();
            final View view = inflater.inflate(R.layout.profile_adb, null);
            String userGender = userProfile.substring(0,1);//0 male 1 female
            String userAge = userProfile.substring(1);//position of spinner


            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setView(view);
            Spinner ageSpinner = view.findViewById(R.id.profileSpinner);
            ageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    char temp = tempProfile.charAt(0);
                    tempProfile = temp + Integer.toString(position);
                    Log.i("current profile: ", userProfile);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.age_array, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            ageSpinner.setAdapter(adapter);
            ageSpinner.setSelection(Integer.parseInt(userAge));

            RadioButton maleRadioButton = view.findViewById(R.id.profile_male);
            RadioButton femaleRadioButton = view.findViewById(R.id.profile_female);


            if(userGender.equals("0"))
                maleRadioButton.setChecked(true);
            else
                femaleRadioButton.setChecked(true);


            final RadioGroup radioGroup = view.findViewById(R.id.profile_gender);

            radioGroup.setOnCheckedChangeListener(
                    new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            char temp = tempProfile.charAt(1);
                            switch (checkedId) {
                                case R.id.profile_male: //Male
                                    tempProfile = "0" + temp;
                                    break;
                                case R.id.profile_female: //Female
                                    tempProfile = "1" + temp;
                                    break;
                                default:
                                    break;
                            }

                        }
                    });

            Log.i("User profile: ", userProfile);
            alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    save(view);
                    recreate();
                }
            });
            alertDialogBuilder.setNegativeButton("Cancel", null);
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Depending on the clicked item, change the displayed TopLevelFragment.
                Fragment fragment = null;
                switch (item.getItemId()) {
                    case R.id.action_PFT:
                        fragment = new pft_top_level_fragment();
                        break;

                    case R.id.action_CFT:
                        fragment = new cft_top_level_fragment();
                        break;

                    case R.id.action_BF:
                        fragment = new body_fat_top_level_fragment();
                        break;

                }
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_host, fragment)
                        .commit();
                return true;

         }

    };


    public void save(View v) {

        FileOutputStream fos = null;

        try {
            fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
            fos.write(tempProfile.getBytes());
            Toast.makeText(this, "Profile saved", Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void load() {
        FileInputStream fis = null;

        try {
            fis = openFileInput(FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;

            while ((text = br.readLine()) != null) {
                sb.append(text);
            }

            userProfile=sb.toString();
            tempProfile = userProfile;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}