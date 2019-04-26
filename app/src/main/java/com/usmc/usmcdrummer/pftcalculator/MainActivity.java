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

todo 2.2.0 - Test out autoswitch to next field for minutes to seconds
todo 2.2.0 - see if the bottom nav can not pop up during text input
todo 2.2.0 - update gradle
todo 2.2.0 - release

TODO 3.0.0 - Add CFT Whatif

TODO 4.0.0 - Add access to charts -- TableLayout?
 */

package com.usmc.usmcdrummer.pftcalculator;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Retrieve a reference to the BottomNavigationView and listen for click events.
        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.getMenu().findItem(R.id.action_PFT).setTitle("PFT");
        bottomNav.getMenu().findItem(R.id.action_BF).setTitle("Body Fat");
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_host, new pft_top_level_fragment())
                .commit();

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



}