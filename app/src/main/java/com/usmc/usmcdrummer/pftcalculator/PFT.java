package com.usmc.usmcdrummer.pftcalculator;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;

public class PFT {

    private int PullupsScore = 0;
    private int PushupsScore = 0;
    private int CrunchesScore = 0;
    private int RunScore = 0;
    private int RowScore = 0;
    private boolean Gender = true; //true for male -- false for female
    private boolean Elevation = false; //true for if adjustment is required and checkbox is checked

    private int ageGroup = 0;

    private int TotalScore = 0;
    private int PFTClass = 0; //0 for fail

    private int Pullups = 0;
    private int Pushups = 0;

    private int Crunches = 0;
    private int RunTimeMin = 0;
    private int RunTimeSec = 0;
    private int RowTimeMin = 0;
    private int RowTimeSec = 0;
    private boolean runningSelected = false;
    private boolean pullupsSelected = false;

    private boolean runRequired = false;
    private boolean rowRequired = false;
    private boolean pullRequired = false;
    private boolean pushrequired = false;
    private boolean crunchRequired = false;



    public PFT() //Generic constructor
    {

    }

    public PFT(int pullups, boolean pullSelected, int crunches, int runTimeMin, int runTimeSec, boolean RunningSelected, boolean gender, int age_group, boolean elevation) {
        runningSelected = RunningSelected;
        pullupsSelected = pullSelected;
        if(!runningSelected){
            RowTimeMin=runTimeMin;
            RowTimeSec=runTimeSec;
            RunTimeMin = 0;
            RunTimeSec = 0;
        }
        else {
            RunTimeMin = runTimeMin;
            RunTimeSec = runTimeSec;
            RowTimeMin = 0;
            RowTimeSec = 0;
        }

        if(pullSelected) {
            Pullups = pullups;
            Pushups = 0;
        }
        else{
            Pushups=pullups;
            Pullups=0;
        }
        Crunches=crunches;

        Gender = gender;
        ageGroup = age_group;
        Elevation = elevation;
        makeScores();
    }

    public SpannableStringBuilder getWhatIfResults(int desiredClass, int desScore)
    {
        int whatIfTotal = TotalScore;  //Sets the running total to the score they have from the events they entered
        int startLength;
        SpannableStringBuilder resultsString = new SpannableStringBuilder();

        int desiredScore = desScore;
        int remainingScores = scoresRemaining();  //Sets the amount of scores remaining to calculate scores for (the amount of scores they didn't enter)
        if (remainingScores == 0){ //If they entered all the scores
            resultsString.append("All Scores Entered. Score: " + Integer.toString(TotalScore));
            return resultsString;
        }
        if (remainingScores == -1){ //If they failed an event returns -1
            resultsString.append("You failed an event!");
            return resultsString;
        }
        int neededScore = desiredScore - TotalScore; //subtracts the score obtained from entered events from the desired score
        int scorePerEvent = neededScore / remainingScores + ((neededScore % remainingScores == 0) ? 0 : 1); //Calculates the scores needed per event


        if(scorePerEvent > 100){
            resultsString.append("You cannot obtain that class!");
            return resultsString;
        }

        if(pushrequired) {
            if(scorePerEvent>70 && remainingScores==1) {
                resultsString.append( "Only pushups needed, more than 70 points required");
                return resultsString;
            }
            if ((neededScore - 70)/(remainingScores-1)>100) {
                resultsString.append("Pushups needed, need too many points afterwards");
                return resultsString;
            }

        }

        resultsString.append("\nDesired Class: " + Integer.toString(desiredClass+1) + //Returns the desired class 1, 2, or 3
                "\nScore required: " + Integer.toString(desiredScore)+ "\n");  //Returns the score required of that class

        if(pullRequired) {
            int[] pullstuff;
            pullstuff = getPullIndex(scorePerEvent); //returns required pullups to achieve scorePerEventScore and the score
            neededScore -= pullstuff[1];
            remainingScores--;
            if(remainingScores>0)
                scorePerEvent=neededScore / remainingScores + ((neededScore % remainingScores == 0) ? 0 : 1);
            String temp = "\nPullups Required: " + Integer.toString(pullstuff[0]) +
                    " Score: " + pullstuff[1];
            startLength = resultsString.length();
            resultsString.append(temp);
            resultsString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), startLength, resultsString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            whatIfTotal += pullstuff[1];
        }
        else if(pushrequired) {
            int[] pushstuff;
            pushstuff = getPushIndex(scorePerEvent);
            neededScore -= pushstuff[1];
            remainingScores--;
            if(remainingScores>0)
                scorePerEvent=neededScore / remainingScores + ((neededScore % remainingScores == 0) ? 0 : 1);
            String temp = "\nPushups Required: " + Integer.toString(pushstuff[0]) +
                    " Score: " + pushstuff[1];
            startLength = resultsString.length();
            resultsString.append(temp);
            resultsString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), startLength, resultsString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            whatIfTotal += pushstuff[1];
        }
        else {
            if(PushupsScore==0)
                resultsString.append("\nPullups: ")
                        .append(Integer.toString(Pullups))
                        .append(" Score: ")
                        .append(Integer.toString(PullupsScore));
            else
                resultsString.append("\nPushups: ")
                        .append(Integer.toString(Pushups))
                        .append(" Score: ")
                        .append(Integer.toString(PushupsScore));
        }

        if(crunchRequired){
            int[] crunchstuff;
            crunchstuff = getCrunchIndex(scorePerEvent);
            neededScore -= crunchstuff[1];
            remainingScores--;
            if(remainingScores>0)
                scorePerEvent=neededScore / remainingScores + ((neededScore % remainingScores == 0) ? 0 : 1);
            startLength = resultsString.length();
            resultsString.append("\nCrunches Required: ")
                    .append(Integer.toString(crunchstuff[0]))
                    .append(" Score: ")
                    .append(Integer.toString(crunchstuff[1]));
            resultsString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), startLength, resultsString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            whatIfTotal += crunchstuff[1];
        }
        else
            resultsString.append( "\nCrunches: " + Crunches + " Score: " + CrunchesScore);

        if(runRequired){
            int[]runstuff;
            runstuff = getRunIndex(scorePerEvent);
            neededScore -= runstuff[2];
            remainingScores--;
            startLength=resultsString.length();
            resultsString.append("\nRun Faster Than: " + Integer.toString(runstuff[0]) +
                    ":" + String.format("%02d",runstuff[1]) +
                    " Score: " + runstuff[2]);
            resultsString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), startLength, resultsString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            whatIfTotal += runstuff[2];
        }
        else if(rowRequired){
            int[]rowstuff;
            rowstuff = getRowIndex(scorePerEvent);
            neededScore -= rowstuff[2];
            remainingScores--;
            startLength = resultsString.length();
            resultsString.append("\nRow Faster Than: " + Integer.toString(rowstuff[0]) +
                    ":" + String.format("%02d",rowstuff[1]) +
                    "\nScore: " + rowstuff[2]);
            resultsString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), startLength, resultsString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            whatIfTotal += rowstuff[2];
        }
        else{
            if(RunScore == 0)
                resultsString.append( "\nRow Time: " + RowTimeMin + ":" + String.format("%02d",RowTimeSec) + " Score: " + RowScore);
            else
                resultsString.append("\nRun Time: " + RunTimeMin + ":" + String.format("%02d",RunTimeSec) + " Score: " + RunScore);

        }
        resultsString.append("\n\nTotal Score: " + Integer.toString(whatIfTotal));
        resultsString.append("\nClass Obtained: " + Integer.toString(getClassObtained(whatIfTotal)));

        return resultsString;
    }

    private int getClassObtained(int score)
    {
        if(score>=235)
            return 1;
        else if(score>=200)
            return 2;
        else return 3;

    }
    private int[] getPullIndex(int score){//todo go to all get*index and delete 0. remove the -1 from the index += tableindex

        //returns 0: repetitions required
        //        1: score given for reps completed
        int[] PullTable;
        int minreps = 0;
        int index = 0;
        if(Gender){//MALE TABLE
            switch( ageGroup ) {
                case 0: //***17-20
                    //PullTable = new int[]{40, 40, 44, 48, 51, 55, 59, 63, 66, 70, 74, 78, 81, 85, 89, 93, 96, 100};
                    PullTable = new int[]{40, 44, 48, 51, 55, 59, 63, 66, 70, 74, 78, 81, 85, 89, 93, 96, 100};
                    minreps = 4;
                    break;
                case 1:
                    PullTable = new int[]{40, 43, 47, 50, 53, 57, 60, 63, 67, 70, 73, 77, 80, 83, 87, 90, 93, 97, 100};
                    minreps = 5;
                    break;
                case 2:
                    PullTable = new int[]{40, 43, 47, 50, 53, 57, 60, 63, 67, 70, 73, 77, 80, 83, 87, 90, 93, 97, 100};
                    minreps = 5;
                    break;
                case 3:
                    PullTable = new int[]{40, 43, 47, 50, 53, 57, 60, 63, 67, 70, 73, 77, 80, 83, 87, 90, 93, 97, 100};
                    minreps = 5;
                    break;
                case 4:
                    PullTable = new int[]{40, 44, 48, 51, 55, 59, 63, 66, 70, 74, 78, 81, 85, 89, 93, 96, 100};
                    minreps = 5;
                    break;
                case 5:
                    PullTable = new int[]{40, 44, 48, 52, 56, 60, 64, 68, 72, 76, 80, 84, 88, 92, 96, 100};
                    minreps = 5;
                    break;
                case 6:
                    PullTable = new int[]{40, 44, 48, 52, 56, 60, 64, 68, 72, 76, 80, 84, 88, 92, 96, 100};
                    minreps = 4;
                    break;
                case 7:
                    PullTable = new int[]{40, 44, 48, 52, 56, 60, 64, 68, 72, 76, 80, 84, 88, 92, 96};
                    minreps = 3;
                    break;
                default:
                    PullTable = new int[0];
                    break;
            }
        }
        else{//FEMALE TABLES
            switch (ageGroup){
                case 0:
                    PullTable = new int[] {60,67,73,80,87,93,100};
                    minreps = 1;
                    break;
                case 1:
                    PullTable = new int[] {60,65,70,75,80,85,90,95,100};
                    minreps=3;
                    break;
                case 2:
                    PullTable = new int[] {60,65,70,75,80,85,90,95,100};
                    minreps = 4;
                    break;
                case 3:
                    PullTable = new int[] {60,65,70,75,80,85,90,95,100};
                    minreps = 3;
                    break;
                case 4:
                    PullTable = new int[] {60, 66,71,77,83,89,94,100};
                    minreps = 3;
                    break;
                case 5:
                    PullTable = new int[] {60,67, 73, 80, 87, 93, 100};
                    minreps = 2;
                    break;
                case 6:
                    PullTable = new int[] {60,70, 80, 90,100};
                    minreps = 2;
                    break;
                case 7:
                    PullTable = new int[] {60,80,100};
                    minreps = 2;
                    break;
                default:
                    PullTable = new int[0];
                    break;
            }
        }
        while (PullTable[index]<score)
            index++;
        int tableIndex = index;
        index += minreps;
        int[] returnIndex = new int[]{index, PullTable[tableIndex]};
        return returnIndex;
    }

    private int[] getPushIndex(int score){

        //returns 0: repetitions required
        //        1: score given for reps completed
        int[] PushTable;
        int minreps = 0;
        int index = 0;
        if(Gender){
            switch( ageGroup ) {
                case 0: //***17-20
                    PushTable = new int[]{40,41,42,42,43,44,45,45,46,47,48,48,49,50,51,51,52,53,54,54,55,56,57,57,58,59,60,60,61,62,63,63,64,65,66,66,67,68,69,69,70};
                    minreps = 42;
                    break;
                case 1:
                    PushTable = new int[] {40,41,41,42,43,43,44,44,45,46,46,47,48,48,49,50,50,51,51,52,53,53,54,55,55,56,57,57,58,59,59,60,60,61,62,62,63,64,64,65,66,66,67,67,68,69,69,70};
                    minreps = 40;
                    break;
                case 2:
                    PushTable = new int[] {40,41,41,42,43,43,44,45,45,46,47,47,48,49,49,50,51,51,52,53,53,54,55,55,56,57,57,58,59,59,60,61,61,62,63,63,64,65,65,66,67,67,68,69,69,70};
                    minreps = 39;
                    break;
                case 3:
                    PushTable = new int[] {40,41,41,42,43,43,44,45,45,46,47,48,48,49,50,50,51,52,52,53,54,54,55,56,56,57,58,58,59,60,60,61,62,63,63,64,65,65,66,67,67,68,69,69,70};
                    minreps = 36;
                    break;
                case 4:
                    PushTable = new int[] {40,41,41,42,43,44,44,45,46,46,47,48,49,49,50,51,51,52,53,54,54,55,56,56,57,58,59,59,60,61,61,62,63,64,64,65,66,66,67,68,69,69,70};
                    minreps = 34;
                    break;
                case 5:
                    PushTable = new int[] {40,41,41,42,43,44,44,45,46,46,47,48,49,49,50,51,51,52,53,54,54,55,56,56,57,58,59,59,60,61,61,62,63,64,64,65,66,66,67,68,69,69,70};
                    minreps = 30;
                    break;
                case 6:
                    PushTable = new int[] {40,41,41,42,43,43,44,45,46,46,47,48,48,49,50,50,51,52,53,53,54,55,55,56,57,57,58,59,60,60,61,62,62,63,64,64,65,66,67,67,68,69,69,70};
                    minreps = 25;
                    break;
                case 7:
                    PushTable = new int[] {40,41,41,42,43,43,44,45,45,46,47,48,48,49,50,50,51,52,52,53,54,54,55,56,56,57,58,58,59,60,60,61,62,63,63,64,65,65,66,67,67,68,69,69,70};
                    minreps = 20;
                    break;
                default:
                    PushTable = new int[0];
                    break;
            }
        }
        else{
            minreps = 1;
            switch (ageGroup){
                case 0: //***17-20
                    PushTable = new int[] {40,41,43,44,45,47,48,49,50,52,53,54,56,57,58,60,61,62,63,65,66,67,69,70};
                    minreps = 19;
                    break;
                case 1:
                    PushTable = new int[] {40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70};
                    minreps = 18;
                    break;
                case 2:
                    PushTable = new int[] {40,41,42,43,44,45,46,47,48,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,63,64,65,66,67,68,69,70};
                    minreps = 18;
                    break;
                case 3:
                    PushTable = new int[] {40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70};
                    minreps = 16;
                    break;
                case 4:
                    PushTable = new int[] {40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70};
                    minreps = 14;
                    break;
                case 5:
                    PushTable = new int[] {40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70};
                    minreps = 12;
                    break;
                case 6:
                    PushTable = new int[] {40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70};
                    minreps = 11;
                    break;
                case 7:
                    PushTable = new int[] {40,41,42,43,44,45,46,48,49,50,51,52,53,54,55,56,57,58,59,60,61,63,64,65,66,67,68,69,70};
                    minreps = 10;
                    break;
                default:
                    PushTable = new int[0];
                    break;
            }
        }
        if(score>70)
            score=70;
        while (PushTable[index]<score)
            index++;
        int tableIndex = index;
        index += minreps;
        int[] returnIndex = new int[]{index, PushTable[tableIndex]};
        return returnIndex;
    }

    private int[] getCrunchIndex(int score){

        //returns 0: repetitions required
        //        1: score given for reps completed
        int[] CrunchTable;
        int minreps = 0;
        int index = 0;
        if(Gender){
            switch( ageGroup ) {
                case 0: //***17-20
                    CrunchTable = new int[]{40,42,43,45,47,49,50,52,54,55,57,59,61,62,64,66,67,69,71,73,74,76,78,79,81,83,85,86,88,90,91,93,95,97,98,100};
                    minreps = 70;
                    break;
                case 1:
                    CrunchTable = new int[] {40,42,43,45,46,48,49,51,52,54,55,57,58,60,61,63,64,66,67,69,70,72,73,75,76,78,79,81,82,84,85,87,88,90,91,93,94,96,97,99,100};
                    minreps = 70;
                    break;
                case 2:
                    CrunchTable = new int[] {40,41,43,44,45,47,48,49,51,52,53,55,56,57,59,60,61,63,64,65,67,68,69,71,72,73,75,76,77,79,80,81,83,84,85,87,88,89,91,92,93,95,96,97,99,100};
                    minreps = 70;
                    break;
                case 3:
                    CrunchTable = new int[] {40,41,43,44,45,47,48,49,51,52,53,55,56,57,59,60,61,63,64,65,67,68,69,71,72,73,75,76,77,79,80,81,83,84,85,87,88,89,91,92,93,95,96,97,99,100};
                    minreps = 70;
                    break;
                case 4:
                    CrunchTable = new int[] {40,42,43,45,46,48,49,51,52,54,55,57,58,60,61,63,64,66,67,69,70,72,73,75,76,78,79,81,82,84,85,87,88,90,91,93,94,96,97,99,100};
                    minreps = 70;
                    break;
                case 5:
                    CrunchTable = new int[] {40,42,43,45,46,48,49,51,52,54,55,57,58,60,61,63,64,66,67,69,70,72,73,75,76,78,79,81,82,84,85,87,88,90,91,93,94,96,97,99,100};
                    minreps = 65;
                    break;
                case 6:
                    CrunchTable = new int[] {40,41,42,44,45,46,47,48,50,51,52,53,54,56,57,58,59,60,62,63,64,65,66,68,69,70,71,72,74,75,76,77,78,80,81,82,83,84,86,87,88,89,90,92,93,94,95,96,98,99,100};
                    minreps = 50;
                    break;
                case 7:
                    CrunchTable = new int[] {40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100};
                    minreps = 40;
                    break;
                default:
                    CrunchTable = new int[0];
                    break;
            }
        }
        else{
            minreps = 1;
            switch (ageGroup){
                case 0:
                    CrunchTable = new int[] {40,41,42,44,45,46,47,48,50,51,52,53,54,56,57,58,59,60,62,63,64,65,66,68,69,70,71,72,74,75,76,77,78,80,81,82,83,84,86,87,88,89,90,92,93,94,95,96,98,99,100};
                    minreps = 50;
                    break;
                case 1:
                    CrunchTable = new int[] {40,41,42,44,45,46,47,48,50,51,52,53,54,56,57,58,59,60,62,63,64,65,66,68,69,70,71,72,74,75,76,77,78,80,81,82,83,84,86,87,88,89,90,92,93,94,95,96,98,99,100};
                    minreps = 55;
                    break;
                case 2:
                    CrunchTable = new int[] {40,41,42,44,45,46,47,48,50,51,52,53,54,56,57,58,59,60,62,63,64,65,66,68,69,70,71,72,74,75,76,77,78,80,81,82,83,84,86,87,88,89,90,92,93,94,95,96,98,99,100};
                    minreps = 60;
                    break;
                case 3:
                    CrunchTable = new int[] {40,41,43,44,45,47,48,49,51,52,53,55,56,57,59,60,61,63,64,65,67,68,69,71,72,73,75,76,77,79,80,81,83,84,85,87,88,89,91,92,93,95,96,97,99,100};
                    minreps = 60;
                    break;
                case 4:
                    CrunchTable = new int[] {40,41,43,44,45,47,48,49,51,52,53,55,56,57,59,60,61,63,64,65,67,68,69,71,72,73,75,76,77,79,80,81,83,84,85,87,88,89,91,92,93,95,96,97,99,100};
                    minreps = 60;
                    break;
                case 5:
                    CrunchTable = new int[] {40,41,43,44,45,47,48,49,51,52,53,55,56,57,59,60,61,63,64,65,67,68,69,71,72,73,75,76,77,79,80,81,83,84,85,87,88,89,91,92,93,95,96,97,99,100};
                    minreps = 55;
                    break;
                case 6:
                    CrunchTable = new int[] {40,41,42,44,45,46,47,48,50,51,52,53,54,56,57,58,59,60,62,63,64,65,66,68,69,70,71,72,74,75,76,77,78,80,81,82,83,84,86,87,88,89,90,92,93,94,95,96,98,99,100};
                    minreps = 50;
                    break;
                case 7:
                    CrunchTable = new int[] {40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100};
                    minreps = 40;
                    break;
                default:
                    CrunchTable = new int[0];
                    break;
            }
        }
        while (CrunchTable[index]<score)
            index++;
        int tableIndex = index;
        index += minreps;
        int[] returnIndex = new int[]{index, CrunchTable[tableIndex]};
        return returnIndex;
    }

    private int[] getRunIndex(int score){

        //returns 0: needed minutes
        //returns 1: needed sceonds
        //returns 2: score given for reps completed
        //RunTable.length - index gives amount of 10 second intervals needed.

        int[] RunTable;
        int minMin = 0;
        int minSec = 0;
        int index = 0;
        if(Gender){
            switch( ageGroup ) {
                case 0: //***17-20
                    RunTable = new int[] {40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100};
                    minMin = 18;
                    minSec = 0;
                    break;
                case 1:
                    RunTable = new int[] {40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100};
                    minMin = 18;
                    minSec = 0;
                    break;
                case 2:
                    RunTable = new int[] {40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100};
                    minMin = 18;
                    minSec = 0;
                    break;
                case 3:
                    RunTable = new int[] {40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100};
                    minMin = 18;
                    minSec = 0;
                    break;
                case 4:
                    RunTable = new int[] {40,41,42,43,44,45,46,47,48,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,93,94,95,96,97,98,99,100};
                    minMin = 18;
                    minSec = 0;
                    break;
                case 5:
                    RunTable = new int[] {40,41,42,43,44,45,46,46,47,48,49,50,51,52,53,54,55,56,57,58,58,59,60,61,62,63,64,65,66,67,68,69,70,70,71,72,73,74,75,76,77,78,79,80,81,82,82,83,84,85,86,87,88,89,90,91,92,93,94,94,95,96,97,98,99,100};
                    minMin = 18;
                    minSec = 30;
                    break;
                case 6:
                    RunTable = new int[] {40,41,42,43,44,45,45,46,47,48,49,50,51,52,53,54,55,55,56,57,58,59,60,61,62,63,64,65,65,66,67,68,69,70,71,72,73,74,75,75,76,77,78,79,80,81,82,83,84,85,85,86,87,88,89,90,91,92,93,94,95,95,96,97,98,99,100};
                    minMin = 19;
                    minSec = 0;
                    break;
                case 7:
                    RunTable = new int[] {40,41,41,42,43,44,44,45,46,47,47,48,49,50,50,51,52,53,53,54,55,56,56,57,58,59,59,60,61,61,62,63,64,64,65,66,67,67,68,69,70,70,71,72,73,73,74,75,76,76,77,78,79,79,80,81,81,82,83,84,84,85,86,87,87,88,89,90,90,91,92,93,93,94,95,96,96,97,98,99,99,100};
                    minMin = 19;
                    minSec = 30;
                    break;
                default:
                    RunTable = new int[0];
                    break;
            }
        }
        else{

            switch (ageGroup){
                case 0:
                    RunTable = new int[] {40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100};
                    minMin = 21;
                    minSec = 0;
                    break;
                case 1:
                    RunTable = new int[] {40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100};
                    minMin = 21;
                    minSec = 0;
                    break;
                case 2:
                    RunTable = new int[] {40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100};
                    minMin = 21;
                    minSec = 0;
                    break;
                case 3:
                    RunTable = new int[] {40,41,42,43,44,45,46,47,48,49,50,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,90,91,92,93,94,95,96,97,98,99,100};
                    minMin = 21;
                    minSec = 0;
                    break;
                case 4:
                    RunTable = new int[] {40,41,42,43,44,45,46,46,47,48,49,50,51,52,53,54,55,56,57,58,58,59,60,61,62,63,64,65,66,67,68,69,70,70,71,72,73,74,75,76,77,78,79,80,81,82,82,83,84,85,86,87,88,89,90,91,92,93,94,94,95,96,97,98,99,100};
                    minMin = 21;
                    minSec = 0;
                    break;
                case 5:
                    RunTable = new int[] {41,42,43,44,45,45,46,47,48,49,50,51,52,53,54,55,55,56,57,58,59,60,61,62,63,64,65,65,66,67,68,69,70,71,72,73,74,75,75,76,77,78,79,8,81,82,83,84,85,85,86,87,88,89,90,91,92,93,94,95,95,96,97,98,99,100};
                    minMin = 21;
                    minSec = 30;
                    break;
                case 6:
                    RunTable = new int[] {40,41,42,43,43,44,45,46,47,48,49,50,50,51,52,53,54,55,56,57,57,58,59,60,61,62,63,63,64,65,66,67,68,69,70,70,71,72,73,74,75,76,77,77,78,79,80,81,82,83,83,84,85,86,87,88,89,90,90,91,92,93,94,95,96,97,97,98,99,100};
                    minMin = 22;
                    minSec = 0;
                    break;
                case 7:
                    RunTable = new int[] {40,41,41,42,43,44,44,45,46,47,47,48,49,50,50,51,52,53,53,54,55,56,56,57,58,59,59,60,61,61,62,63,64,64,65,66,67,67,68,69,70,70,71,72,73,73,74,75,76,76,77,78,79,79,80,81,81,82,83,84,84,85,86,87,87,88,89,90,90,91,92,93,93,94,95,96,96,97,98,99,99,100};
                    minMin = 22;
                    minSec = 30;
                    break;
                default:
                    RunTable = new int[0];
                    break;
            }
        }
        while (RunTable[index]<score)
            index++;
        int tableIndex = index;
        int temp = 10*(RunTable.length-1 - index); // number of seconds added to min time
        if (!Elevation)
            temp += 90;
        int neededMin = temp/60 + minMin;
        int neededSec = temp%60 + minSec;
        if (minSec >= 60){
            neededMin++;
            neededSec-=60;
        }
        int[] returnIndex = new int[]{neededMin, neededSec, RunTable[tableIndex]};
        return returnIndex;
    }

    private int[] getRowIndex(int score){

        //returns 0: needed minutes
        //returns 1: needed sceonds
        //returns 2: score given for reps completed
        //RunTable.length - index gives amount of 10 second intervals needed.

        int[] RowTable;
        int minMin = 0;
        int minSec = 0;
        int index = 0;
        if(Gender){
            switch( ageGroup ) {
                case 0: //***17-20
                    RowTable = new int[] {40,41,42,43,44,45,45,46,47,48,49,50,51,52,53,54,55,55,56,57,58,59,60,61,62,63,64,65,65,66,67,68,69,70,71,72,73,74,75,75,76,77,78,79,80,81,82,83,84,85,85,86,87,88,89,90,91,92,93,94,95,95,96,97,98,99,100};
                    minMin = 18;
                    minSec = 0;
                    break;
                case 1:
                    RowTable = new int[] {40,41,42,43,44,44,45,46,47,48,49,50,51,52,53,53,54,55,56,57,58,59,60,61,61,62,63,64,65,66,67,68,69,70,70,71,72,73,74,75,76,77,78,79,79,80,81,82,83,84,85,86,87,87,88,89,90,91,92,93,94,95,96,96,97,98,99,100};
                    minMin = 18;
                    minSec = 15;
                    break;
                case 2:
                    RowTable = new int[]{40,41,42,43,44,44,45,46,47,48,49,50,51,51,52,53,54,55,56,57,58,59,59,60,61,62,63,64,65,66,66,67,68,69,70,71,72,73,74,74,75,76,77,78,79,80,81,81,82,83,84,85,86,87,88,89,89,90,91,92,93,94,95,96,96,97,98,99,100};
                    minMin = 18;
                    minSec = 30;
                    break;
                case 3:
                    RowTable = new int[] {40,41,42,43,43,44,45,46,47,48,49,50,50,51,52,53,54,55,56,57,57,58,59,60,61,62,63,63,64,65,66,67,68,69,70,70,71,72,73,74,75,76,77,77,78,79,80,81,82,83,83,84,85,86,87,88,89,90,90,91,92,93,94,95,96,97,97,98,99,100};
                    minMin = 18;
                    minSec = 45;
                    break;
                case 4:
                    RowTable = new int[] {40,41,42,43,43,44,45,46,47,48,49,49,50,51,52,53,54,55,55,56,57,58,59,60,61,61,62,63,64,65,66,67,67,68,69,70,71,72,73,73,74,75,76,77,78,79,79,80,81,82,83,84,85,85,86,87,88,89,90,91,91,92,93,94,95,96,97,97,98,99,100};
                    minMin = 19;
                    minSec = 0;
                    break;
                case 5:
                    RowTable = new int[] {40,41,42,43,43,44,45,46,47,48,48,49,50,51,52,53,54,54,55,56,57,58,59,59,60,61,62,63,64,65,65,66,67,68,69,70,70,71,72,73,74,75,75,76,77,78,79,80,81,81,82,83,84,85,86,86,87,88,89,90,91,92,92,93,94,95,96,97,97,98,99,100};
                    minMin = 19;
                    minSec = 15;
                    break;
                case 6:
                    RowTable = new int[] {40,41,42,43,43,44,45,46,47,48,48,49,50,51,52,53,53,54,55,56,57,58,58,59,60,61,62,63,63,64,65,66,67,68,68,69,70,71,72,73,73,74,75,76,77,78,78,79,80,81,82,83,83,84,85,86,87,88,88,89,90,91,92,93,93,94,95,96,97,98,98,99,100};
                    minMin = 19;
                    minSec = 35;
                    break;
                case 7:
                    RowTable = new int[] {40,41,42,43,43,44,45,46,47,48,48,49,50,51,52,53,53,54,55,56,57,58,58,59,60,61,62,63,63,64,65,66,67,68,68,69,70,71,72,73,73,74,75,76,77,78,78,79,80,81,82,83,83,84,85,86,87,88,88,89,90,91,92,93,93,94,95,96,97,98,98,99,100};
                    minMin = 20;
                    minSec = 0;
                    break;
                default:
                    RowTable = new int[0];
                    break;
            }
        }
        else{

            switch (ageGroup){
                case 0:
                    RowTable = new int[] {40,41,42,43,44,45,45,46,47,48,49,50,51,52,53,54,55,55,56,57,58,59,60,61,62,63,64,65,65,66,67,68,69,70,71,72,73,74,75,75,76,77,78,79,80,81,82,83,84,85,85,86,87,88,89,90,91,92,93,94,95,95,96,97,98,99,100};
                    minMin = 21;
                    minSec = 0;
                    break;
                case 1:
                    RowTable = new int[] {40,41,42,43,44,44,45,46,47,48,49,50,51,52,53,53,54,55,56,57,58,59,60,61,61,62,63,64,65,66,67,68,69,70,70,71,72,73,74,75,76,77,78,79,79,80,81,82,83,84,85,86,87,87,88,89,90,91,92,93,94,95,96,96,97,98,99,100};
                    minMin = 21;
                    minSec = 15;
                    break;
                case 2:
                    RowTable = new int[]{40,41,42,43,44,44,45,46,47,48,49,50,51,51,52,53,54,55,56,57,58,59,59,60,61,62,63,64,65,66,66,67,68,69,70,71,72,73,74,74,75,76,77,78,79,80,81,81,82,83,84,85,86,87,88,89,89,90,91,92,93,94,95,96,96,97,98,99,100};
                    minMin = 21;
                    minSec = 30;
                    break;
                case 3:
                    RowTable = new int[] {40,41,42,43,43,44,45,46,47,48,49,50,50,51,52,53,54,55,56,57,57,58,59,60,61,62,63,63,64,65,66,67,68,69,70,70,71,72,73,74,75,76,77,77,78,79,80,81,82,83,83,84,85,86,87,88,89,90,90,91,92,93,94,95,96,97,97,98,99,100};
                    minMin = 21;
                    minSec = 45;
                    break;
                case 4:
                    RowTable = new int[] {40,41,42,43,43,44,45,46,47,48,49,49,50,51,52,53,54,55,55,56,57,58,59,60,61,61,62,63,64,65,66,67,67,68,69,70,71,72,73,73,74,75,76,77,78,79,79,80,81,82,83,84,85,85,86,87,88,89,90,91,91,92,93,94,95,96,97,97,98,99,100};
                    minMin = 22;
                    minSec = 0;
                    break;
                case 5:
                    RowTable = new int[] {40,41,42,43,43,44,45,46,47,48,48,49,50,51,52,53,54,54,55,56,57,58,59,59,60,61,62,63,64,65,65,66,67,68,69,70,70,71,72,73,74,75,75,76,77,78,79,80,81,81,82,83,84,85,86,86,87,88,89,90,91,92,92,93,94,95,96,97,97,98,99,100};
                    minMin = 22;
                    minSec = 15;
                    break;
                case 6:
                    RowTable = new int[] {40,41,42,43,43,44,45,46,47,48,48,49,50,51,52,53,53,54,55,56,57,58,58,59,60,61,62,63,63,64,65,66,67,68,68,69,70,71,72,73,73,74,75,76,77,78,78,79,80,81,82,83,83,84,85,86,87,88,88,89,90,91,92,93,93,94,95,96,97,98,98,99,100};
                    minMin = 22;
                    minSec = 35;
                    break;
                case 7:
                    RowTable = new int[] {40,41,42,43,43,44,45,46,47,48,48,49,50,51,52,53,53,54,55,56,57,58,58,59,60,61,62,63,63,64,65,66,67,68,68,69,70,71,72,73,73,74,75,76,77,78,78,79,80,81,82,83,83,84,85,86,87,88,88,89,90,91,92,93,93,94,95,96,97,98,98,99,100};
                    minMin = 23;
                    minSec = 0;
                    break;
                default:
                    RowTable = new int[0];
                    break;
            }
        }
        while (RowTable[index]<score)
            index++;
        int tableIndex = index;
        int temp = 10*(RowTable.length-1 - index); // number of seconds added to min time
        if (!Elevation)
            temp += 40;
        int neededMin = temp/60 + minMin;
        int neededSec = temp%60 + minSec;
        if (minSec >= 60){
            neededMin++;
            neededSec-=60;
        }
        int[] returnIndex = new int[]{neededMin, neededSec, RowTable[tableIndex]};
        return returnIndex;
    }


    private int scoresRemaining(){
        int total = 0;
        if(runningSelected && (RunScore == 0) && (RunTimeMin != 0 ))
            return -1;
        else if (!runningSelected && (RowScore == 0) &&  RowTimeMin !=0)
            return -1;
        if(pullupsSelected && PullupsScore ==0 && Pullups !=0)
            return -1;
        else if (!pullupsSelected && PushupsScore ==0 && Pushups !=0)
            return -1;
        if(Crunches!=0 && CrunchesScore==0)
            return -1;

        if(!runningSelected){
            if(RowScore==0) {
                total++;
                rowRequired = true;
            }
        }
        else
        if(RunScore==0) {
            total++;
            runRequired = true;
        }


        if(pullupsSelected) {
            if(PullupsScore==0) {
                total++;
                pullRequired = true;
            }
        }
        else
        if(PushupsScore==0) {
            total++;
            pushrequired = true;
        }

        if(CrunchesScore==0) {
            total++;
            crunchRequired = true;
        }

        return total;

    }



    private int classScore(int desiredClass){
        if(desiredClass==0)
            return 235;
        else if(desiredClass==1)
            return 200;
        else return 150;


    }
    public String getResults(String agegroup) {
        String resultsString ="RESULTS" +
                "\n\nAge Group: " + agegroup + "\nGender: " + genderString() + "\n";

        if(Pushups != 0)
            resultsString += "\nPushups: " + Pushups + "   Score: " + PushupsScore;
        else
            resultsString += "\nPullups: " + Pullups + "   Score: " + PullupsScore;


        resultsString +=
                "\nCrunches: " + Crunches + "   Score: " + CrunchesScore;

        if(!runningSelected)
            resultsString += "\nRow Time: " + RowTimeMin + ":" + String.format("%02d",RowTimeSec) + "   Score: " + RowScore;
        else
            resultsString += "\nRun Time: " + RunTimeMin + ":" + String.format("%02d",RunTimeSec) + "   Score: " + RunScore;

        resultsString += "\n\nTotal Score: " + getTotalScore() + "\nClass: " + getPFTClassString()  +
                "\n" + elevationString();



        return resultsString;
    }

    public int getPullups() { return this.Pullups; }

    public int getPushups(){ return this.Pushups;}

    public int getCrunches() {
        return this.Crunches;
    }

    public int getRunTimeMin() {
        return this.RunTimeMin;
    }

    public int getRunTimeSec() {
        return this.RunTimeSec;
    }

    public int getPullupsScore() {
        return this.PullupsScore;
    }

    public int getPushupsScore() {
        return this.PushupsScore;
    }

    public int getCrunchesScore() {
        return this.CrunchesScore;
    }

    public int getRunScore() {
        return this.RunScore;
    }

    public int getTotalScore() {
        return this.TotalScore;
    }

    public int getPFTClass() {
        return this.PFTClass;
    }

    public String elevationString(){
        if(Elevation)
            return "";
        else
            return "Adjustment for elevation has been factored in this result.";
    }

    public String getPFTClassString(){
        String temp;
        switch(PFTClass) {
            case 0:
                temp = "You failed!";
                break;
            case 1:
                temp = "First Class";
                break;
            case 2:
                temp = "Second Class";
                break;
            case 3:
                temp ="Third Class";
                break;
            default:
                temp = "Error";
                break;

        }
        return temp;
    }

    public String genderString() {
        if (Gender)
            return "Male";
        else
            return "Female";
    }


    private int getIndex(int amount,int min,int max,int length) {
        int index = 0;
        if(amount < min){ index = 0;	}
        else if(amount > max){index=length-1;}
        else{index=amount-min+1;}
        return index;
    }

    private int getRunIndex(int runMin,int runSec,int minMin,int minSec,int length,boolean elevation)
/*
	index = 0
	subtract mininimum runmin from the runners minutes
	subtract minimum run seconds from the runners seconds, if <0 run sec = runsec+60 and runmin-1
	then multiply remaining minutes by 6 to add to index
	divide seconds by 10 and add 1 to index
	mod10 !0 =, add 1 to index
	if the index is greater than the length, then they fail
*/
    {
        int index = 0;
        if(runMin == 0) {return index;}
        else
        {
            if(!elevation)//add if for rowsselected
            {
                minMin = minMin+1;
                minSec = minSec+30;
                if(minSec == 60)
                {
                    minMin = minMin +1;
                    minSec = 0;
                }
            }
            runMin = runMin - minMin;
            runSec = runSec - minSec;
            if (runSec < 0)
            {
                runSec = runSec + 60;
                runMin = runMin -1;
            }

            if (runMin < 0 || (runMin == 0 && runSec == 0))
            {

                return length-1;
            }
            else
            {
                index = runMin*6 + (int)(Math.floor(runSec/10));
                if(runSec%10 != 0){
                    index=index+1;
                }
                if(index>=length) {
                    return 0;
                }
                index=Math.abs(index-length+1);
                return index;


            }
        }

    }

    private int getRowIndex(int rowMin,int rowSec,int minMin,int minSec,int length,boolean elevation)
    {
        int index = 0;
        if(rowMin == 0) {return index;}
        else
        {
            if(!elevation)//add if for rowsselected
            {
                minSec = minSec+40;
                if(minSec == 60)
                {
                    minMin = minMin +1;
                    minSec = 0;
                }
            }
            rowMin = rowMin - minMin;
            rowSec = rowSec - minSec;
            if (rowSec < 0)
            {
                rowSec = rowSec + 60;
                rowMin = rowMin -1;
            }

            if (rowMin < 0 || (rowMin == 0 && rowSec == 0))
            {

                return length-1;
            }
            else
            {
                index = rowMin*12 + (int)(Math.floor(rowSec/5));
                if(rowSec%5 != 0){
                    index=index+1;
                }
                if(index>=length) {
                    return 0;
                }
                index=Math.abs(index-length+1);
                return index;
            }
        }

    }


    private void makeScores()
    {
        int[] PullTable;
        int[] PushTable;
        int[] CrunchTable;
        int[] RunTable;
        int[] RowTable;



        if(Gender){//male tables
            switch( ageGroup ){
                case 0:  //****************17-20*************************
                    PullTable = new int[]{0,40,44,48,51,55,59,63,66,70,74,78,81,85,89,93,96,100};
                    PushTable = new int[]{0,40,41,42,42,43,44,45,45,46,47,48,48,49,50,51,51,52,53,54,54,55,56,57,57,58,59,60,60,61,62,63,63,64,65,66,66,67,68,69,69,70};
                    CrunchTable = new int[]{0,40,42,43,45,47,49,50,52,54,55,57,59,61,62,64,66,67,69,71,73,74,76,78,79,81,83,85,86,88,90,91,93,95,97,98,100};
                    RunTable = new int[] {0,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100};
                    RowTable = new int[] {0,40,41,42,43,44,45,45,46,47,48,49,50,51,52,53,54,55,55,56,57,58,59,60,61,62,63,64,65,65,66,67,68,69,70,71,72,73,74,75,75,76,77,78,79,80,81,82,83,84,85,85,86,87,88,89,90,91,92,93,94,95,95,96,97,98,99,100};
                    //CALCULATE PULLUPS Score
                    PullupsScore=PullTable[getIndex(Pullups,4,20,PullTable.length)];

                    //CALCULATE PUSHUPS Score
                    if(Pushups>0){
                        PullupsScore=0;
                        PushupsScore=PushTable[getIndex(Pushups,42,81,PushTable.length)];
                    }
                    else PushupsScore=0;

                    //CALCULATE CRUNCHES Score
                    CrunchesScore = CrunchTable[getIndex(Crunches,70,104,CrunchTable.length)];

                    //CALCULATE RUN Score
                    if(runningSelected)
                        RunScore = RunTable[getRunIndex(RunTimeMin, RunTimeSec, 18,0, RunTable.length, Elevation)];
                    else
                        RowScore = RowTable[getRowIndex(RowTimeMin, RowTimeSec, 18,0, RowTable.length, Elevation)];

                    break;

                case 1: //21-25
                    PullTable = new int[] {0,40,43,47,50,53,57,60,63,67,70,73,77,80,83,87,90,93,97,100};
                    PushTable = new int[] {0,40,41,41,42,43,43,44,44,45,46,46,47,48,48,49,50,50,51,51,52,53,53,54,55,55,56,57,57,58,59,59,60,60,61,62,62,63,64,64,65,66,66,67,67,68,69,69,70};
                    CrunchTable = new int[] {0,40,42,43,45,46,48,49,51,52,54,55,57,58,60,61,63,64,66,67,69,70,72,73,75,76,78,79,81,82,84,85,87,88,90,91,93,94,96,97,99,100};
                    RunTable = new int[] {0,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100};
                    RowTable = new int[] {0,40,41,42,43,44,44,45,46,47,48,49,50,51,52,53,53,54,55,56,57,58,59,60,61,61,62,63,64,65,66,67,68,69,70,70,71,72,73,74,75,76,77,78,79,79,80,81,82,83,84,85,86,87,87,88,89,90,91,92,93,94,95,96,96,97,98,99,100};

                    //CALCULATE PULLUPS Score
                    PullupsScore=PullTable[getIndex(Pullups,5,23,PullTable.length)];

                    //CALCULATE PUSHUPS Score
                    if(Pushups>0)
                    {
                        PullupsScore=0;
                        PushupsScore=PushTable[getIndex(Pushups,40,86,PushTable.length)];
                    }
                    else PushupsScore=0;

                    //CALCULATE CRUNCHES Score
                    CrunchesScore = CrunchTable[getIndex(Crunches,70,109,CrunchTable.length)];

                    //CALCULATE RUN Score
                    if(runningSelected)
                        RunScore = RunTable[getRunIndex(RunTimeMin, RunTimeSec, 18,0,RunTable.length, Elevation)];
                    else
                        RowScore = RowTable[getRowIndex(RowTimeMin, RowTimeSec, 18,15, RowTable.length, Elevation)];
                    break;

                case 2: //26-30
                    PullTable = new int[] {0,40,43,47,50,53,57,60,63,67,70,73,77,80,83,87,90,93,97,100};
                    PushTable = new int[] {0,40,41,41,42,43,43,44,45,45,46,47,47,48,49,49,50,51,51,52,53,53,54,55,55,56,57,57,58,59,59,60,61,61,62,63,63,64,65,65,66,67,67,68,69,69,70};
                    CrunchTable = new int[] {0,40,41,43,44,45,47,48,49,51,52,53,55,56,57,59,60,61,63,64,65,67,68,69,71,72,73,75,76,77,79,80,81,83,84,85,87,88,89,91,92,93,95,96,97,99,100};
                    RunTable = new int[] {0, 40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100};
                    RowTable = new int[]{0,40,41,42,43,44,44,45,46,47,48,49,50,51,51,52,53,54,55,56,57,58,59,59,60,61,62,63,64,65,66,66,67,68,69,70,71,72,73,74,74,75,76,77,78,79,80,81,81,82,83,84,85,86,87,88,89,89,90,91,92,93,94,95,96,96,97,98,99,100};

                    //CALCULATE PULLUPS SCORE
                    PullupsScore=PullTable[getIndex(Pullups,5,23,PullTable.length)];

                    //CALCULATE PUSHUPS SCORE
                    if(Pushups>0)
                    {
                        PullupsScore=0;
                        PushupsScore=PushTable[getIndex(Pushups,39,83,PushTable.length)];
                    }
                    else PushupsScore=0;

                    //CALCULATE CRUNCHES
                    CrunchesScore = CrunchTable[getIndex(Crunches,70,115,CrunchTable.length)];

                    //CALCULATE RUN TIME
                    if(runningSelected)
                        RunScore = RunTable[getRunIndex(RunTimeMin, RunTimeSec, 18,0,RunTable.length, Elevation)];
                    else
                        RowScore = RowTable[getRowIndex(RowTimeMin, RowTimeSec, 18,30, RowTable.length, Elevation)];
                    break;

                case 3: //"31-35":

                    PullTable = new int[] {0,40,43,47,50,53,57,60,63,67,70,73,77,80,83,87,90,93,97,100};
                    PushTable = new int[] {0,40,41,41,42,43,43,44,45,45,46,47,48,48,49,50,50,51,52,52,53,54,54,55,56,56,57,58,58,59,60,60,61,62,63,63,64,65,65,66,67,67,68,69,69,70};
                    CrunchTable = new int[] {0,40,41,43,44,45,47,48,49,51,52,53,55,56,57,59,60,61,63,64,65,67,68,69,71,72,73,75,76,77,79,80,81,83,84,85,87,88,89,91,92,93,95,96,97,99,100};
                    RunTable = new int[] {0,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100};
                    RowTable = new int[] {0,40,41,42,43,43,44,45,46,47,48,49,50,50,51,52,53,54,55,56,57,57,58,59,60,61,62,63,63,64,65,66,67,68,69,70,70,71,72,73,74,75,76,77,77,78,79,80,81,82,83,83,84,85,86,87,88,89,90,90,91,92,93,94,95,96,97,97,98,99,100};

                    //calculate pullups
                    PullupsScore=PullTable[getIndex(Pullups,5,23,PullTable.length)];

                    //CALCULATE PUSHUPS SCORE
                    if(Pushups>0)
                    {
                        PullupsScore=0;
                        PushupsScore=PushTable[getIndex(Pushups,36,79,PushTable.length)];
                    }
                    else PushupsScore=0;

                    //CALCULATE CRUNCHES
                    CrunchesScore = CrunchTable[getIndex(Crunches,70,115,CrunchTable.length)];

                    //calculate run
                    if(runningSelected)
                        RunScore = RunTable[getRunIndex(RunTimeMin, RunTimeSec, 18,0,RunTable.length, Elevation)];
                    else
                        RowScore = RowTable[getRowIndex(RowTimeMin, RowTimeSec, 18,45, RowTable.length, Elevation)];
                    break;
                case 4: //"36-40":
                    PullTable = new int[] {0,40,44,48,51,55,59,63,66,70,74,78,81,85,89,93,96,100};
                    PushTable = new int[] {0,40,41,41,42,43,44,44,45,46,46,47,48,49,49,50,51,51,52,53,54,54,55,56,56,57,58,59,59,60,61,61,62,63,64,64,65,66,66,67,68,69,69,70};
                    CrunchTable = new int[] {0,40,42,43,45,46,48,49,51,52,54,55,57,58,60,61,63,64,66,67,69,70,72,73,75,76,78,79,81,82,84,85,87,88,90,91,93,94,96,97,99,100};
                    RunTable = new int[] {0,40,41,42,43,44,45,46,47,48,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,93,94,95,96,97,98,99,100};
                    RowTable = new int[] {0,40,41,42,43,43,44,45,46,47,48,49,49,50,51,52,53,54,55,55,56,57,58,59,60,61,61,62,63,64,65,66,67,67,68,69,70,71,72,73,73,74,75,76,77,78,79,79,80,81,82,83,84,85,85,86,87,88,89,90,91,91,92,93,94,95,96,97,97,98,99,100};

                    //calculate pullups
                    PullupsScore=PullTable[getIndex(Pullups,5,21,PullTable.length)];


                    //CALCULATE PUSHUPS SCORE
                    if(Pushups>0)
                    {
                        PullupsScore=0;
                        PushupsScore=PushTable[getIndex(Pushups,34,75,PushTable.length)];
                    }
                    else PushupsScore=0;

                    //CALCULATE CRUNCHES
                    CrunchesScore = CrunchTable[getIndex(Crunches,70,109,CrunchTable.length)];

                    //calculate run
                    if(runningSelected)
                        RunScore = RunTable[getRunIndex(RunTimeMin, RunTimeSec, 18,0,RunTable.length, Elevation)];
                    else
                        RowScore = RowTable[getRowIndex(RowTimeMin, RowTimeSec, 19,0, RowTable.length, Elevation)];

                    break;


                case 5: //"41-45":
                    //FILL ARRAYS
                    PullTable = new int[] {0,40,44,48,52,56,60,64,68,72,76,80,84,88,92,96,100};
                    PushTable = new int[] {0,40,41,41,42,43,44,44,45,46,46,47,48,49,49,50,51,51,52,53,54,54,55,56,56,57,58,59,59,60,61,61,62,63,64,64,65,66,66,67,68,69,69,70};
                    CrunchTable = new int[] {0,40,42,43,45,46,48,49,51,52,54,55,57,58,60,61,63,64,66,67,69,70,72,73,75,76,78,79,81,82,84,85,87,88,90,91,93,94,96,97,99,100};
                    RunTable = new int[] {0,40,41,42,43,44,45,46,46,47,48,49,50,51,52,53,54,55,56,57,58,58,59,60,61,62,63,64,65,66,67,68,69,70,70,71,72,73,74,75,76,77,78,79,80,81,82,82,83,84,85,86,87,88,89,90,91,92,93,94,94,95,96,97,98,99,100};
                    RowTable = new int[] {0,40,41,42,43,43,44,45,46,47,48,48,49,50,51,52,53,54,54,55,56,57,58,59,59,60,61,62,63,64,65,65,66,67,68,69,70,70,71,72,73,74,75,75,76,77,78,79,80,81,81,82,83,84,85,86,86,87,88,89,90,91,92,92,93,94,95,96,97,97,98,99,100};

                    //calculate pullups
                    PullupsScore=PullTable[getIndex(Pullups,5,20,PullTable.length)];


                    //CALCULATE PUSHUPS SCORE
                    if(Pushups>0)
                    {
                        PullupsScore=0;
                        PushupsScore=PushTable[getIndex(Pushups,30,71,PushTable.length)];
                    }
                    else PushupsScore=0;

                    //CALCULATE CRUNCHES
                    CrunchesScore = CrunchTable[getIndex(Crunches,65,104,CrunchTable.length)];

                    //calculate run
                    if(runningSelected)
                        RunScore = RunTable[getRunIndex(RunTimeMin, RunTimeSec, 18,30,RunTable.length, Elevation)];
                    else
                        RowScore = RowTable[getRowIndex(RowTimeMin, RowTimeSec, 19,15, RowTable.length, Elevation)];

                    break;

                case 6:// 46-50
                    //FILL ARRAYS
                    PullTable = new int[] {0,40,44,48,52,56,60,64,68,72,76,80,84,88,92,96,100};
                    PushTable = new int[] {0,40,41,41,42,43,43,44,45,46,46,47,48,48,49,50,50,51,52,53,53,54,55,55,56,57,57,58,59,60,60,61,62,62,63,64,64,65,66,67,67,68,69,69,70};
                    CrunchTable = new int[] {0,40,41,42,44,45,46,47,48,50,51,52,53,54,56,57,58,59,60,62,63,64,65,66,68,69,70,71,72,74,75,76,77,78,80,81,82,83,84,86,87,88,89,90,92,93,94,95,96,98,99,100};
                    RunTable = new int[] {0,40,41,42,43,44,45,45,46,47,48,49,50,51,52,53,54,55,55,56,57,58,59,60,61,62,63,64,65,65,66,67,68,69,70,71,72,73,74,75,75,76,77,78,79,80,81,82,83,84,85,85,86,87,88,89,90,91,92,93,94,95,95,96,97,98,99,100};
                    RowTable = new int[] {0,40,41,42,43,43,44,45,46,47,48,48,49,50,51,52,53,53,54,55,56,57,58,58,59,60,61,62,63,63,64,65,66,67,68,68,69,70,71,72,73,73,74,75,76,77,78,78,79,80,81,82,83,83,84,85,86,87,88,88,89,90,91,92,93,93,94,95,96,97,98,98,99,100};
                    //CALCULATE PULLUPS Score
                    PullupsScore=PullTable[getIndex(Pullups,4,19,PullTable.length)];

                    //CALCULATE PUSHUPS Score
                    if(Pushups>0)
                    {
                        PullupsScore=0;
                        PushupsScore=PushTable[getIndex(Pushups,25,67,PushTable.length)];
                    }
                    else PushupsScore=0;

                    //CALCULATE CRUNCHES Score
                    CrunchesScore = CrunchTable[getIndex(Crunches,50,99,CrunchTable.length)];

                    //CALCULATE RUN Score
                    if(runningSelected)
                        RunScore = RunTable[getRunIndex(RunTimeMin, RunTimeSec, 19,0, RunTable.length, Elevation)];
                    else
                        RowScore = RowTable[getRowIndex(RowTimeMin, RowTimeSec, 19,35, RowTable.length, Elevation)];
                    break;

                case 7: //"51+":
                    //FILL ARRAYS
                    PullTable = new int[] {0,40,44,48,52,56,60,64,68,72,76,80,84,88,92,96};
                    PushTable = new int[] {0,40,41,41,42,43,43,44,45,45,46,47,48,48,49,50,50,51,52,52,53,54,54,55,56,56,57,58,58,59,60,60,61,62,63,63,64,65,65,66,67,67,68,69,69,70};
                    CrunchTable = new int[] {0,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100};
                    RunTable = new int[] {0,40,41,41,42,43,44,44,45,46,47,47,48,49,50,50,51,52,53,53,54,55,56,56,57,58,59,59,60,61,61,62,63,64,64,65,66,67,67,68,69,70,70,71,72,73,73,74,75,76,76,77,78,79,79,80,81,81,82,83,84,84,85,86,87,87,88,89,90,90,91,92,93,93,94,95,96,96,97,98,99,99,100};
                    RowTable = new int[] {0,40,41,42,43,43,44,45,46,47,48,48,49,50,51,52,53,53,54,55,56,57,58,58,59,60,61,62,63,63,64,65,66,67,68,68,69,70,71,72,73,73,74,75,76,77,78,78,79,80,81,82,83,83,84,85,86,87,88,88,89,90,91,92,93,93,94,95,96,97,98,98,99,100};
                    //CALCULATE PULLUPS Score
                    PullupsScore=PullTable[getIndex(Pullups,3,18,PullTable.length)];

                    //CALCULATE PUSHUPS Score
                    if(Pushups>0)
                    {
                        PullupsScore=0;
                        PushupsScore=PushTable[getIndex(Pushups,20,63,PushTable.length)];
                    }
                    else PushupsScore=0;

                    //CALCULATE CRUNCHES Score
                    CrunchesScore = CrunchTable[getIndex(Crunches,40,99,CrunchTable.length)];

                    //CALCULATE RUN Score
                    if(runningSelected)
                        RunScore = RunTable[getRunIndex(RunTimeMin, RunTimeSec, 19,30, RunTable.length, Elevation)];
                    else
                        RowScore = RowTable[getRowIndex(RowTimeMin, RowTimeSec, 20,0, RowTable.length, Elevation)];
                    break;
            }}

        else{//female tables
            switch( ageGroup ){
                case 0: //"17-20":  //****************17-20*************************
                    PullTable = new int[] {0,60,67,73,80,87,93,100};
                    PushTable = new int[] {0,40,41,43,44,45,47,48,49,50,52,53,54,56,57,58,60,61,62,63,65,66,67,69,70};
                    CrunchTable = new int[] {0,40,41,42,44,45,46,47,48,50,51,52,53,54,56,57,58,59,60,62,63,64,65,66,68,69,70,71,72,74,75,76,77,78,80,81,82,83,84,86,87,88,89,90,92,93,94,95,96,98,99,100};
                    RunTable = new int[] {0,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100};
                    RowTable = new int[] {0,40,41,42,43,44,45,45,46,47,48,49,50,51,52,53,54,55,55,56,57,58,59,60,61,62,63,64,65,65,66,67,68,69,70,71,72,73,74,75,75,76,77,78,79,80,81,82,83,84,85,85,86,87,88,89,90,91,92,93,94,95,95,96,97,98,99,100};
                    //CALCULATE PULLUPS Score
                    PullupsScore=PullTable[getIndex(Pullups,1,7,PullTable.length)];

                    //CALCULATE PUSHUPS Score
                    if(Pushups>0)
                    {
                        PullupsScore=0;
                        PushupsScore=PushTable[getIndex(Pushups,19,42,PushTable.length)];
                    }
                    else PushupsScore=0;

                    //CALCULATE CRUNCHES Score
                    CrunchesScore = CrunchTable[getIndex(Crunches,50,100,CrunchTable.length)];

                    //CALCULATE RUN Score
                    if(runningSelected)
                        RunScore = RunTable[getRunIndex(RunTimeMin, RunTimeSec, 21,0, RunTable.length, Elevation)];
                    else
                        RowScore = RowTable[getRowIndex(RowTimeMin, RowTimeSec, 21,0, RowTable.length, Elevation)];


                    break;
                case 1: //"21-25":
                    //FILL ARRAYS
                    PullTable = new int[] {0,60,65,70,75,80,85,90,95,100};
                    PushTable = new int[] {0,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70};
                    CrunchTable = new int[] {0,40,41,42,44,45,46,47,48,50,51,52,53,54,56,57,58,59,60,62,63,64,65,66,68,69,70,71,72,74,75,76,77,78,80,81,82,83,84,86,87,88,89,90,92,93,94,95,96,98,99,100};
                    RunTable = new int[] {0,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100};
                    RowTable = new int[] {0,40,41,42,43,44,44,45,46,47,48,49,50,51,52,53,53,54,55,56,57,58,59,60,61,61,62,63,64,65,66,67,68,69,70,70,71,72,73,74,75,76,77,78,79,79,80,81,82,83,84,85,86,87,87,88,89,90,91,92,93,94,95,96,96,97,98,99,100};
                    //CALCULATE PULLUPS Score
                    PullupsScore=PullTable[getIndex(Pullups,3,11,PullTable.length)];

                    //CALCULATE PUSHUPS Score
                    if(Pushups>0)
                    {
                        PullupsScore=0;
                        PushupsScore=PushTable[getIndex(Pushups,18,48,PushTable.length)];
                    }
                    else PushupsScore=0;

                    //CALCULATE CRUNCHES Score
                    CrunchesScore = CrunchTable[getIndex(Crunches,55,105,CrunchTable.length)];

                    //CALCULATE RUN Score
                    if(runningSelected)
                        RunScore = RunTable[getRunIndex(RunTimeMin, RunTimeSec, 21,0, RunTable.length, Elevation)];
                    else
                        RowScore = RowTable[getRowIndex(RowTimeMin, RowTimeSec, 21,15, RowTable.length, Elevation)];
                    break;

                case 2: //"26-30":
                    //FILL ARRAYS
                    PullTable = new int[] {0,60,65,70,75,80,85,90,95,100};
                    PushTable = new int[] {0,40,41,42,43,44,45,46,47,48,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,63,64,65,66,67,68,69,70};
                    CrunchTable = new int[] {0,40,41,42,44,45,46,47,48,50,51,52,53,54,56,57,58,59,60,62,63,64,65,66,68,69,70,71,72,74,75,76,77,78,80,81,82,83,84,86,87,88,89,90,92,93,94,95,96,98,99,100};
                    RunTable = new int[] {0,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100};
                    RowTable = new int[]{0,40,41,42,43,44,44,45,46,47,48,49,50,51,51,52,53,54,55,56,57,58,59,59,60,61,62,63,64,65,66,66,67,68,69,70,71,72,73,74,74,75,76,77,78,79,80,81,81,82,83,84,85,86,87,88,89,89,90,91,92,93,94,95,96,96,97,98,99,100};
                    //CALCULATE PULLUPS Score
                    PullupsScore=PullTable[getIndex(Pullups,4,12,PullTable.length)];

                    //CALCULATE PUSHUPS Score
                    if(Pushups>0)
                    {
                        PullupsScore=0;
                        PushupsScore=PushTable[getIndex(Pushups,18,50,PushTable.length)];
                    }
                    else PushupsScore=0;

                    //CALCULATE CRUNCHES Score
                    CrunchesScore = CrunchTable[getIndex(Crunches,60,110,CrunchTable.length)];

                    //CALCULATE RUN Score
                    if(runningSelected)
                        RunScore = RunTable[getRunIndex(RunTimeMin, RunTimeSec, 21,0, RunTable.length, Elevation)];
                    else
                        RowScore = RowTable[getRowIndex(RowTimeMin, RowTimeSec, 21,30, RowTable.length, Elevation)];

                    break;

                case 3: //"31-35":
                    //FILL ARRAYS
                    PullTable = new int[] {0,60,65,70,75,80,85,90,95,100};
                    PushTable = new int[] {0,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70};
                    CrunchTable = new int[] {0,40,41,43,44,45,47,48,49,51,52,53,55,56,57,59,60,61,63,64,65,67,68,69,71,72,73,75,76,77,79,80,81,83,84,85,87,88,89,91,92,93,95,96,97,99,100};
                    RunTable = new int[] {0,40,41,42,43,44,45,46,47,48,49,50,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,90,91,92,93,94,95,96,97,98,99,100};
                    RowTable = new int[] {0,40,41,42,43,43,44,45,46,47,48,49,50,50,51,52,53,54,55,56,57,57,58,59,60,61,62,63,63,64,65,66,67,68,69,70,70,71,72,73,74,75,76,77,77,78,79,80,81,82,83,83,84,85,86,87,88,89,90,90,91,92,93,94,95,96,97,97,98,99,100};

                    //CALCULATE PULLUPS Score
                    PullupsScore=PullTable[getIndex(Pullups,3,11,PullTable.length)];

                    //CALCULATE PUSHUPS Score
                    if(Pushups>0)
                    {
                        PullupsScore=0;
                        PushupsScore=PushTable[getIndex(Pushups,16,46,PushTable.length)];
                    }
                    else PushupsScore=0;

                    //CALCULATE CRUNCHES Score
                    CrunchesScore = CrunchTable[getIndex(Crunches,60,105,CrunchTable.length)];

                    //CALCULATE RUN Score
                    if(runningSelected)
                        RunScore = RunTable[getRunIndex(RunTimeMin, RunTimeSec, 21,0, RunTable.length, Elevation)];
                    else
                        RowScore = RowTable[getRowIndex(RowTimeMin, RowTimeSec, 21,45, RowTable.length, Elevation)];
                    break;

                case 4: //"36-40":
                    //FILL ARRAYS
                    PullTable = new int[] {0,60, 66,71,77,83,89,94,100};
                    PushTable = new int[] {0,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70};
                    CrunchTable = new int[] {0,40,41,43,44,45,47,48,49,51,52,53,55,56,57,59,60,61,63,64,65,67,68,69,71,72,73,75,76,77,79,80,81,83,84,85,87,88,89,91,92,93,95,96,97,99,100};
                    RunTable = new int[] {0,40,41,42,43,44,45,46,46,47,48,49,50,51,52,53,54,55,56,57,58,58,59,60,61,62,63,64,65,66,67,68,69,70,70,71,72,73,74,75,76,77,78,79,80,81,82,82,83,84,85,86,87,88,89,90,91,92,93,94,94,95,96,97,98,99,100};
                    RowTable = new int[] {0,40,41,42,43,43,44,45,46,47,48,49,49,50,51,52,53,54,55,55,56,57,58,59,60,61,61,62,63,64,65,66,67,67,68,69,70,71,72,73,73,74,75,76,77,78,79,79,80,81,82,83,84,85,85,86,87,88,89,90,91,91,92,93,94,95,96,97,97,98,99,100};

                    //CALCULATE PULLUPS Score
                    PullupsScore=PullTable[getIndex(Pullups,3,10,PullTable.length)];

                    //CALCULATE PUSHUPS Score
                    if(Pushups>0)
                    {
                        PullupsScore=0;
                        PushupsScore=PushTable[getIndex(Pushups,14,43,PushTable.length)];
                    }
                    else PushupsScore=0;

                    //CALCULATE CRUNCHES Score
                    CrunchesScore = CrunchTable[getIndex(Crunches,60,105,CrunchTable.length)];

                    //CALCULATE RUN Score
                    if(runningSelected)
                        RunScore = RunTable[getRunIndex(RunTimeMin, RunTimeSec, 21,0, RunTable.length, Elevation)];
                    else
                        RowScore = RowTable[getRowIndex(RowTimeMin, RowTimeSec, 22,0, RowTable.length, Elevation)];
                    break;
                case 5: //"41-45":
                    //FILL ARRAYS
                    PullTable = new int[] {0,60,67, 73, 80, 87, 93, 100};
                    PushTable = new int[] {0,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70};
                    CrunchTable = new int[] {0,40,41,43,44,45,47,48,49,51,52,53,55,56,57,59,60,61,63,64,65,67,68,69,71,72,73,75,76,77,79,80,81,83,84,85,87,88,89,91,92,93,95,96,97,99,100};
                    RunTable = new int[] {0,41,42,43,44,45,45,46,47,48,49,50,51,52,53,54,55,55,56,57,58,59,60,61,62,63,64,65,65,66,67,68,69,70,71,72,73,74,75,75,76,77,78,79,8,81,82,83,84,85,85,86,87,88,89,90,91,92,93,94,95,95,96,97,98,99,100};
                    RowTable = new int[] {0,40,41,42,43,43,44,45,46,47,48,48,49,50,51,52,53,54,54,55,56,57,58,59,59,60,61,62,63,64,65,65,66,67,68,69,70,70,71,72,73,74,75,75,76,77,78,79,80,81,81,82,83,84,85,86,86,87,88,89,90,91,92,92,93,94,95,96,97,97,98,99,100};

                    //CALCULATE PULLUPS Score
                    PullupsScore=PullTable[getIndex(Pullups,2,8,PullTable.length)];

                    //CALCULATE PUSHUPS Score
                    if(Pushups>0)
                    {
                        PullupsScore=0;
                        PushupsScore=PushTable[getIndex(Pushups,12,41,PushTable.length)];
                    }
                    else PushupsScore=0;

                    //CALCULATE CRUNCHES Score
                    CrunchesScore = CrunchTable[getIndex(Crunches,55,100,CrunchTable.length)];

                    //CALCULATE RUN Score
                    if(runningSelected)
                        RunScore = RunTable[getRunIndex(RunTimeMin, RunTimeSec, 21,30, RunTable.length, Elevation)];
                    else
                        RowScore = RowTable[getRowIndex(RowTimeMin, RowTimeSec, 22,15, RowTable.length, Elevation)];
                    break;

                case 6: //"46-50":
                    //FILL ARRAYS
                    PullTable = new int[] {0,60,70, 80, 90,100};
                    PushTable = new int[] {0,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70};
                    CrunchTable = new int[] {0,40,41,42,44,45,46,47,48,50,51,52,53,54,56,57,58,59,60,62,63,64,65,66,68,69,70,71,72,74,75,76,77,78,80,81,82,83,84,86,87,88,89,90,92,93,94,95,96,98,99,100};
                    RunTable = new int[] {0,40,41,42,43,43,44,45,46,47,48,49,50,50,51,52,53,54,55,56,57,57,58,59,60,61,62,63,63,64,65,66,67,68,69,70,70,71,72,73,74,75,76,77,77,78,79,80,81,82,83,83,84,85,86,87,88,89,90,90,91,92,93,94,95,96,97,97,98,99,100};
                    RowTable = new int[] {0,40,41,42,43,43,44,45,46,47,48,48,49,50,51,52,53,53,54,55,56,57,58,58,59,60,61,62,63,63,64,65,66,67,68,68,69,70,71,72,73,73,74,75,76,77,78,78,79,80,81,82,83,83,84,85,86,87,88,88,89,90,91,92,93,93,94,95,96,97,98,98,99,100};

                    //CALCULATE PULLUPS Score
                    PullupsScore=PullTable[getIndex(Pullups,2,6,PullTable.length)];

                    //CALCULATE PUSHUPS Score
                    if(Pushups>0)
                    {
                        PullupsScore=0;
                        PushupsScore=PushTable[getIndex(Pushups,11,40,PushTable.length)];
                    }
                    else PushupsScore=0;

                    //CALCULATE CRUNCHES Score
                    CrunchesScore = CrunchTable[getIndex(Crunches,50,100,CrunchTable.length)];

                    //CALCULATE RUN Score
                    if(runningSelected)
                        RunScore = RunTable[getRunIndex(RunTimeMin, RunTimeSec, 22,0, RunTable.length, Elevation)];
                    else
                        RowScore = RowTable[getRowIndex(RowTimeMin, RowTimeSec, 22,35, RowTable.length, Elevation)];
                    break;

                case 7: //"51+":
                    //FILL ARRAYS
                    PullTable = new int[] {0,60,80,100};
                    PushTable = new int[] {0,40,41,42,43,44,45,46,48,49,50,51,52,53,54,55,56,57,58,59,60,61,63,64,65,66,67,68,69,70};
                    CrunchTable = new int[] {0,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100};
                    RunTable = new int[] {0,40,41,41,42,43,44,44,45,46,47,47,48,49,50,50,51,52,53,53,54,55,56,56,57,58,59,59,60,61,61,62,63,64,64,65,66,67,67,68,69,70,70,71,72,73,73,74,75,76,76,77,78,79,79,80,81,81,82,83,84,84,85,86,87,87,88,89,90,90,91,92,93,93,94,95,96,96,97,98,99,99,100};
                    RowTable = new int[] {0,40,41,42,43,43,44,45,46,47,48,48,49,50,51,52,53,53,54,55,56,57,58,58,59,60,61,62,63,63,64,65,66,67,68,68,69,70,71,72,73,73,74,75,76,77,78,78,79,80,81,82,83,83,84,85,86,87,88,88,89,90,91,92,93,93,94,95,96,97,98,98,99,100};
                    //CALCULATE PULLUPS Score
                    PullupsScore=PullTable[getIndex(Pullups,2,4,PullTable.length)];

                    //CALCULATE PUSHUPS Score
                    if(Pushups>0)
                    {
                        PullupsScore=0;
                        PushupsScore=PushTable[getIndex(Pushups,10,38,PushTable.length)];
                    }
                    else PushupsScore=0;

                    //CALCULATE CRUNCHES Score
                    CrunchesScore = CrunchTable[getIndex(Crunches,40,100,CrunchTable.length)];

                    //CALCULATE RUN Score
                    if(runningSelected)
                        RunScore = RunTable[getRunIndex(RunTimeMin, RunTimeSec, 22,30, RunTable.length, Elevation)];
                    else
                        RowScore = RowTable[getRowIndex(RowTimeMin, RowTimeSec, 23,0, RowTable.length, Elevation)];
                    break;
            }
        }

        TotalScore = PullupsScore+PushupsScore+CrunchesScore+RunScore+RowScore;

        if (TotalScore < 150)
            PFTClass = 0;
        else if(TotalScore < 200)
            PFTClass = 3;
        else if(TotalScore < 235)
            PFTClass = 2;
        else
            PFTClass = 1;

        if((PullupsScore == 0 && PushupsScore==0)||CrunchesScore==0||(RunScore==0&&RowScore==0))
            PFTClass = 0;
    }
}



