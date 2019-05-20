package com.usmc.usmcdrummer.pftcalculator;

public class BodyFat {
    boolean gender;
    double height;
    double neck;
    double abs;
    double hips;
    int weight;
    double CValue;
    String results;
    boolean goodResults=false;

    public BodyFat(){};

    public BodyFat (double Height, double Neck, double Abs, double Hips, int Weight, boolean Gender){
        gender = Gender;
        height = Height;
        neck = Neck;
        abs = Abs;
        hips = Hips;
        weight = Weight;
        CValue = getCValue();
        results = getBodyFat();
    }

    public String getResults() {

        String resultsString = "";
        if(goodResults) {

            resultsString = "RESULTS" +  "\nGender: " + genderString() + "\n";
            resultsString += "\nBody Fat: ";
            if (results.equals("0"))
                resultsString +="Body fat does not fall on chart!";
            else
                resultsString += results;
            resultsString += "\nCircumference Value: " + Double.toString(CValue);

            resultsString += "\n\nHeight:  " + Double.toString(height);
            resultsString += "\nNeck: " + Double.toString(neck);
            if (gender)
                resultsString += "\nAbdomen: " + Double.toString(abs);
            else
                resultsString += "\nWaist: " + Double.toString(abs);

            if (!gender)
                resultsString += "\nHips: " + Double.toString(hips);
            resultsString += "\nWeight: " + Integer.toString(weight);
            resultsString += "\nMinimum weight: " + Integer.toString(getWeightMin());
            resultsString += "\nMaximum weight: " + Integer.toString(getWeightMax());

            goodResults=false;
        }
        else
            resultsString += results;
        return resultsString;
    }



    public String genderString() {
        if (gender)
            return "Male";
        else
            return "Female";
    }

    private String getBodyFat()
    {
        double Circumference = CValue;
        double Height = height;//sets a temp height for alteration only in this function
        String fat;
        if (gender){
                int[][] maleMatrix = {
                        {9, 9, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},//13.5
                        {11, 11, 10, 10, 10, 10, 9, 9, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},//14
                        {12, 12, 12, 11, 11, 11, 11, 10, 10, 10, 10, 9, 9, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},//14.5
                        {13, 13, 13, 13, 12, 12, 12, 12, 11, 11, 11, 11, 10, 10, 10, 10, 10, 9, 9, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},//15
                        {15, 14, 14, 14, 14, 13, 13, 13, 13, 12, 12, 12, 12, 11, 11, 11, 11, 11, 10, 10, 10, 10, 9, 9, 9, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},//15.5
                        {16, 16, 15, 15, 15, 15, 14, 14, 14, 14, 13, 13, 13, 13, 12, 12, 12, 12, 12, 11, 11, 11, 11, 10, 10, 10, 10, 10, 9, 9, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},//16
                        {17, 17, 16, 16, 16, 16, 15, 15, 15, 15, 14, 14, 14, 14, 14, 13, 13, 13, 13, 12, 12, 12, 12, 12, 11, 11, 11, 11, 11, 10, 10, 10, 10, 10, 9, 9, 0, 0, 0, 0},//16.5
                        {18, 18, 18, 17, 17, 17, 17, 16, 16, 16, 16, 15, 15, 15, 15, 14, 14, 14, 14, 14, 13, 13, 13, 13, 13, 12, 12, 12, 12, 11, 11, 11, 11, 11, 10, 10, 10, 10, 10, 9},//17
                        {19, 19, 19, 18, 18, 18, 18, 17, 17, 17, 17, 16, 16, 16, 16, 16, 15, 15, 15, 15, 14, 14, 14, 14, 14, 13, 13, 13, 13, 13, 12, 12, 12, 12, 12, 11, 11, 11, 11, 11},//17.5
                        {20, 20, 20, 19, 19, 19, 19, 18, 18, 18, 18, 18, 17, 17, 17, 17, 16, 16, 16, 16, 15, 15, 15, 15, 15, 14, 14, 14, 14, 14, 13, 13, 13, 13, 13, 12, 12, 12, 12, 12},//18
                        {21, 21, 21, 20, 20, 20, 20, 19, 19, 19, 19, 19, 18, 18, 18, 18, 17, 17, 17, 17, 17, 16, 16, 16, 16, 15, 15, 15, 15, 15, 14, 14, 14, 14, 14, 13, 13, 13, 13, 13},//18.5
                        {22, 22, 22, 21, 21, 21, 21, 20, 20, 20, 20, 20, 19, 19, 19, 19, 18, 18, 18, 18, 18, 17, 17, 17, 17, 16, 16, 16, 16, 16, 15, 15, 15, 15, 15, 14, 14, 14, 14, 14},//19
                        {23, 23, 23, 22, 22, 22, 22, 21, 21, 21, 21, 21, 20, 20, 20, 20, 19, 19, 19, 19, 18, 18, 18, 18, 18, 17, 17, 17, 17, 17, 16, 16, 16, 16, 16, 15, 15, 15, 15, 15},//19.5
                        {24, 24, 24, 23, 23, 23, 23, 22, 22, 22, 22, 21, 21, 21, 21, 21, 20, 20, 20, 20, 19, 19, 19, 19, 19, 18, 18, 18, 18, 18, 17, 17, 17, 17, 17, 16, 16, 16, 16, 16},//20
                        {25, 25, 25, 24, 24, 24, 24, 23, 23, 23, 23, 22, 22, 22, 22, 21, 21, 21, 21, 21, 20, 20, 20, 20, 19, 19, 19, 19, 19, 18, 18, 18, 18, 18, 17, 17, 17, 17, 17, 16},//20.5
                        {26, 26, 25, 25, 25, 25, 24, 24, 24, 24, 24, 23, 23, 23, 23, 22, 22, 22, 22, 21, 21, 21, 21, 21, 20, 20, 20, 20, 20, 19, 19, 19, 19, 19, 18, 18, 18, 18, 18, 17},//21
                        {27, 27, 26, 26, 26, 26, 25, 25, 25, 25, 24, 24, 24, 24, 23, 23, 23, 23, 23, 22, 22, 22, 22, 21, 21, 21, 21, 21, 20, 20, 20, 20, 20, 19, 19, 19, 19, 19, 18, 18},//21.5
                        {28, 27, 27, 27, 27, 26, 26, 26, 26, 25, 25, 25, 25, 25, 24, 24, 24, 24, 23, 23, 23, 23, 23, 22, 22, 22, 22, 22, 21, 21, 21, 21, 20, 20, 20, 20, 20, 20, 19, 19},//22
                        {29, 28, 28, 28, 28, 27, 27, 27, 27, 26, 26, 26, 26, 25, 25, 25, 25, 24, 24, 24, 24, 24, 23, 23, 23, 23, 23, 22, 22, 22, 22, 22, 21, 21, 21, 21, 21, 20, 20, 20},//22.5
                        {29, 29, 29, 29, 28, 28, 28, 28, 27, 27, 27, 27, 26, 26, 26, 26, 26, 25, 25, 25, 25, 24, 24, 24, 24, 24, 23, 23, 23, 23, 23, 22, 22, 22, 22, 22, 21, 21, 21, 21},//23
                        {30, 30, 30, 29, 29, 29, 29, 28, 28, 28, 28, 27, 27, 27, 27, 27, 26, 26, 26, 26, 25, 25, 25, 25, 25, 24, 24, 24, 24, 24, 23, 23, 23, 23, 23, 22, 22, 22, 22, 22},//23.5
                        {31, 31, 30, 30, 30, 30, 29, 29, 29, 29, 28, 28, 28, 28, 28, 27, 27, 27, 27, 26, 26, 26, 26, 26, 25, 25, 25, 25, 25, 24, 24, 24, 24, 24, 23, 23, 23, 23, 23, 22},//24
                        {32, 31, 31, 31, 31, 30, 30, 30, 30, 29, 29, 29, 29, 29, 28, 28, 28, 28, 27, 27, 27, 27, 27, 26, 26, 26, 26, 26, 25, 25, 25, 25, 25, 24, 24, 24, 24, 24, 23, 23},//24.5
                        {32, 32, 32, 32, 31, 31, 31, 31, 30, 30, 30, 30, 30, 29, 29, 29, 29, 28, 28, 28, 28, 28, 27, 27, 27, 27, 26, 26, 26, 26, 26, 25, 25, 25, 25, 25, 24, 24, 24, 24},//25
                        {33, 33, 33, 32, 32, 32, 32, 31, 31, 31, 31, 31, 30, 30, 30, 30, 29, 29, 29, 29, 29, 28, 28, 28, 28, 27, 27, 27, 27, 27, 26, 26, 26, 26, 26, 25, 25, 25, 25, 25},//25.5
                        {34, 34, 33, 33, 33, 33, 32, 32, 32, 32, 31, 31, 31, 31, 31, 30, 30, 30, 30, 29, 29, 29, 29, 29, 28, 28, 28, 28, 28, 27, 27, 27, 27, 27, 26, 26, 26, 26, 26, 25},//26
                        {35, 34, 34, 34, 34, 33, 33, 33, 33, 32, 32, 32, 32, 32, 31, 31, 31, 31, 30, 30, 30, 30, 30, 29, 29, 29, 29, 28, 28, 28, 28, 28, 27, 27, 27, 27, 27, 26, 26, 26},//26.5
                        {35, 35, 35, 35, 34, 34, 34, 34, 33, 33, 33, 33, 32, 32, 32, 32, 32, 31, 31, 31, 31, 30, 30, 30, 30, 30, 29, 29, 29, 29, 29, 28, 28, 28, 28, 28, 27, 27, 27, 27},//27
                        {36, 36, 36, 35, 35, 35, 35, 34, 34, 34, 34, 33, 33, 33, 33, 32, 32, 32, 32, 32, 31, 31, 31, 31, 30, 30, 30, 30, 30, 29, 29, 29, 29, 29, 28, 28, 28, 28, 28, 27},//27.5
                        {37, 36, 36, 36, 36, 35, 35, 35, 35, 34, 34, 34, 34, 34, 33, 33, 33, 33, 32, 32, 32, 32, 32, 31, 31, 31, 31, 31, 30, 30, 30, 30, 29, 29, 29, 29, 29, 29, 28, 28},//28
                        {0, 0, 37, 37, 36, 36, 36, 36, 35, 35, 35, 35, 34, 34, 34, 34, 34, 33, 33, 33, 33, 32, 32, 32, 32, 32, 31, 31, 31, 31, 31, 30, 30, 30, 30, 30, 29, 29, 29, 29},//28.5
                        {0, 0, 0, 0, 37, 37, 37, 36, 36, 36, 36, 35, 35, 35, 35, 34, 34, 34, 34, 34, 33, 33, 33, 33, 32, 32, 32, 32, 32, 31, 31, 31, 31, 31, 30, 30, 30, 30, 30, 29},//29
                        {0, 0, 0, 0, 0, 0, 0, 37, 37, 36, 36, 36, 36, 36, 35, 35, 35, 35, 34, 34, 34, 34, 34, 33, 33, 33, 33, 32, 32, 32, 32, 32, 31, 31, 31, 31, 31, 30, 30, 30},//29.5
                        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 37, 37, 36, 36, 36, 36, 35, 35, 35, 35, 35, 34, 34, 34, 34, 34, 33, 33, 33, 33, 32, 32, 32, 32, 32, 31, 31, 31, 31, 31},//30
                        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 37, 37, 37, 36, 36, 36, 36, 35, 35, 35, 35, 35, 34, 34, 34, 34, 34, 33, 33, 33, 33, 32, 32, 32, 32, 32, 32, 31},//30.5
                        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 37, 37, 36, 36, 36, 36, 36, 35, 35, 35, 35, 35, 34, 34, 34, 34, 33, 33, 33, 33, 33, 33, 32, 32, 32},//31
                        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 37, 37, 36, 36, 36, 36, 36, 35, 35, 35, 35, 35, 34, 34, 34, 34, 33, 33, 33, 33, 33, 33},//31.5
                        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 37, 37, 37, 36, 36, 36, 36, 36, 35, 35, 35, 35, 34, 34, 34, 34, 34, 33, 33, 33},//32
                        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 37, 37, 36, 36, 36, 36, 36, 35, 35, 35, 35, 35, 34, 34, 34, 34, 34},//32.5
                        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 37, 37, 36, 36, 36, 36, 36, 35, 35, 35, 35, 35, 34, 34},//33
                        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 37, 37, 36, 36, 36, 36, 36, 35, 35, 35, 35},//33.5
                        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 37, 37, 37, 36, 36, 36, 36, 36, 35},//34
                        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 37, 37, 37, 36, 36, 36},//34.5
                        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 37, 37, 36}//35
                         };
                Height = (Height - 60) * 2;
            Circumference = (Circumference - 13.5) * 2;

            if (Circumference > 43)
                fat = "Circumference value too large for table.  Circumference value: " + Double.toString(Circumference);

            else if (Height < 0)
                fat = "Too short for table, minimum height is 60''.";
            else if (Height > 39)
                fat = "Too tall for table. maximum height is 79.5''.";
            else if ((Height%2 !=0)&&(Height%2 !=1))
                fat = "Please enter a height in half inch increments.";
            else if (Circumference < 0)
                fat = "Circumference value does not fall on table. [Circumference = Abs - Neck]";
            else if ((Height >= 0) && (Circumference >= 0)) {
                fat = Integer.toString(maleMatrix[(int) Circumference][(int) Height]);
                goodResults = true;
            }
            else
                fat = Integer.toString(102); // I don't know
            }
            else{
                int[][] femaleMatrix = {
                    {19, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},//45
                    {20,20,19, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},//45.5
                    {21,20,20,20,19, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},//46
                    {21,21,21,20,20,20,19,19, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},//46.5
                    {22,22,22,21,21,20,20,20,19,19, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},//47
                    {23,23,22,22,22,21,21,21,20,20,19,19, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},//47.5
                    {24,23,23,23,22,22,22,21,21,21,20,20,20,19, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},//48
                    {24,24,24,23,23,23,22,22,22,21,21,21,20,20,20,19, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},//48.5
                    {25,25,24,24,24,23,23,23,22,22,22,21,21,21,20,20,20,19,19, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},//49/
                    {26,26,25,25,24,24,24,23,23,23,22,22,22,21,21,21,20,20,20,19,19, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},//49.5
                    {27,26,26,26,25,25,24,24,24,23,23,23,22,22,22,21,21,21,21,20,20,20,19, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},//50
                    {27,27,27,26,26,26,25,25,25,24,24,23,23,23,23,22,22,22,21,21,21,20,20,20,19,19, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},//50.5
                    {28,28,27,27,27,26,26,26,25,25,25,24,24,24,23,23,23,22,22,22,21,21,21,20,20,20,19,19, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},//51
                    {29,28,28,28,27,27,27,26,26,26,25,25,25,24,24,24,23,23,23,22,22,22,21,21,21,20,20,20,20,19, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},//51.5
                    {29,29,29,28,28,28,27,27,27,26,26,26,25,25,25,24,24,24,23,23,23,22,22,22,21,21,21,21,20,20,20,19,19, 0, 0, 0, 0, 0, 0, 0},//52
                    {30,30,29,29,29,28,28,28,27,27,27,26,26,26,25,25,25,24,24,24,23,23,23,22,22,22,22,21,21,21,20,20,20,19,19, 0, 0, 0, 0, 0},//52.5
                    {31,30,30,30,29,29,29,28,28,28,27,27,27,26,26,26,25,25,25,24,24,24,23,23,23,22,22,22,22,21,21,21,20,20,20,20,19,19, 0, 0},//53
                    {31,31,31,30,30,30,29,29,29,28,28,28,27,27,27,26,26,26,25,25,25,24,24,24,23,23,23,23,22,22,22,21,21,21,21,20,20,20,19,19},//53.5
                    {32,32,31,31,31,30,30,30,29,29,29,28,28,28,27,27,27,26,26,26,25,25,25,24,24,24,24,23,23,23,22,22,22,21,21,21,21,20,20,20},//54
                    {33,32,32,32,31,31,31,30,30,30,29,29,29,28,28,28,27,27,27,26,26,26,25,25,25,24,24,24,24,23,23,23,22,22,22,22,21,21,21,20},//54.5
                    {33,33,33,32,32,32,31,31,31,30,30,30,29,29,29,28,28,28,27,27,27,26,26,26,25,25,25,25,24,24,24,23,23,23,22,22,22,22,21,21},//55
                    {34,34,33,33,33,32,32,32,31,31,31,30,30,30,29,29,29,28,28,28,27,27,27,26,26,26,25,25,25,25,24,24,24,23,23,23,23,22,22,22},//55.5
                    {35,34,34,34,33,33,33,32,32,31,31,31,30,30,30,30,29,29,29,28,28,28,27,27,27,26,26,26,25,25,25,25,24,24,24,23,23,23,23,22},//56
                    {35,35,35,34,34,34,33,33,32,32,32,31,31,31,30,30,30,29,29,29,29,28,28,28,27,27,27,26,26,26,26,25,25,25,24,24,24,24,23,23},//56.5
                    {36,36,35,35,34,34,34,33,33,33,32,32,32,31,31,31,30,30,30,29,29,29,29,28,28,28,27,27,27,26,26,26,26,25,25,25,24,24,24,24},//57
                    {37,36,36,35,35,35,34,34,34,33,37,36,36,35,35,35,34,34,34,33,30,29,29,29,29,28,28,28,27,27,27,26,26,26,26,25,25,25,25,24},//57.5
                    {37,37,36,36,36,35,35,35,34,34,34,33,33,33,32,32,32,31,31,31,30,30,30,29,29,29,29,28,28,28,27,27,27,27,26,26,26,25,25,25},//58
                    {38,37,37,37,36,36,36,35,35,35,34,34,34,33,33,33,32,32,32,31,31,31,30,30,30,29,29,29,29,28,28,28,27,27,27,27,26,26,26,25},//58.5
                    {38,38,38,37,37,37,36,36,36,35,35,35,34,34,34,33,33,33,32,32,32,31,31,31,30,30,30,29,29,29,29,28,28,28,27,27,27,27,26,26},//59
                    {39,39,38,38,38,37,37,36,36,36,35,35,35,34,34,34,33,33,33,33,32,32,32,31,31,31,30,30,30,29,29,29,29,28,28,28,27,27,27,27},//59.5
                    {40,39,39,38,38,38,37,37,37,36,36,36,35,35,35,34,34,34,33,33,33,32,32,32,32,31,31,31,30,30,30,30,29,29,29,28,28,28,28,27},//60
                    {40,40,39,39,39,38,38,38,37,37,37,36,36,36,35,35,35,34,34,34,33,33,33,32,32,32,32,31,31,31,30,30,30,30,29,29,29,28,28,28},//60.5
                    {41,40,40,40,39,39,39,38,38,38,37,37,37,36,36,36,35,35,35,34,34,34,33,33,33,32,32,32,32,31,31,31,30,30,30,30,29,29,29,28},//61
                    {41,41,41,40,40,40,39,39,38,38,38,37,37,37,36,36,36,36,35,35,35,34,34,34,33,33,33,32,32,32,32,31,31,31,30,30,30,30,29,29},//61.5
                    {42,42,41,41,40,40,40,39,39,39,38,38,38,37,37,37,36,36,36,35,35,35,35,34,34,34,33,33,33,32,32,32,32,31,31,31,30,30,30,30},//62
                    {42,42,42,41,41,41,40,40,40,39,39,39,38,38,38,37,37,37,36,36,36,35,35,35,34,34,34,34,33,33,33,32,32,32,32,31,31,31,30,30},//62.5
                    {43,43,42,42,42,41,41,41,40,40,40,39,39,39,38,38,38,37,37,37,36,36,36,35,35,35,34,34,34,34,33,33,33,32,32,32,32,31,31,31},//63
                    {44,43,43,42,42,42,41,41,41,40,40,40,39,39,39,38,38,38,37,37,37,37,36,36,36,35,35,35,34,34,34,34,33,33,33,32,32,32,32,31},//63.5
                    {44,44,43,43,43,42,42,42,41,41,41,40,40,40,39,39,39,38,38,38,37,37,37,36,36,36,36,35,35,35,34,34,34,34,33,33,33,32,32,32},//64
                    {45,44,44,44,43,43,43,42,42,42,41,41,41,40,40,40,39,39,39,38,38,38,37,37,37,36,36,36,36,35,35,35,34,34,34,33,33,33,33,32},//64.5
                    {45,45,45,44,44,43,43,43,42,42,42,41,41,41,40,40,40,39,39,39,38,38,38,38,37,37,37,36,36,36,35,35,35,35,34,34,34,33,33,33},//65
                    {46,45,45,45,44,44,44,43,43,43,42,42,42,41,41,41,40,40,40,39,39,39,38,38,38,37,37,37,37,36,37,37,37,36,36,36,35,35,35,35},//65.5
                    { 0, 0,47,46,46,46,45,45,45,44,44,44,43,43,43,42,42,42,41,41,41,40,40,40,39,39,39,39,38,38,38,37,37,37,36,36,36,36,35,35},//67
                    { 0, 0, 0,47,46,46,46,45,45,45,44,44,44,43,43,43,42,42,42,41,41,41,41,40,40,40,39,39,39,38,38,38,38,37,37,37,36,36,36,36},//67.5
                    { 0, 0, 0, 0,47,47,46,46,46,45,45,45,44,44,44,43,43,43,42,42,42,41,41,41,40,40,40,40,39,39,39,38,38,38,38,37,37,37,36,36},//68
                    { 0, 0, 0, 0, 0, 0,47,46,46,46,45,45,45,44,44,44,43,43,43,43,42,42,42,41,41,41,40,40,40,39,39,39,39,38,38,38,37,37,37,37},//68.5
                    { 0, 0, 0, 0, 0, 0, 0,47,47,46,46,46,45,45,45,44,44,44,43,43,43,42,42,42,41,41,41,41,40,40,40,39,39,39,39,38,38,38,37,37},//69
                    { 0, 0, 0, 0, 0, 0, 0, 0, 0,47,46,46,46,45,45,45,44,44,44,44,43,43,43,42,42,42,41,41,41,41,40,40,40,39,39,39,39,38,38,38},//69.5
                    { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,47,47,46,46,46,45,45,45,44,44,44,43,43,43,43,42,42,42,41,41,41,40,40,40,40,39,39,39,38,38},//70
                    { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,47,46,46,46,46,45,45,45,44,44,44,43,43,43,42,42,42,42,41,41,41,40,40,40,40,39,39,39},//70.5
                    { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,47,47,46,46,46,45,45,45,44,44,44,44,43,43,43,42,42,42,41,41,41,41,40,40,40,39,39},//71
                    { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,47,47,46,46,46,45,45,45,44,44,44,43,43,43,43,42,42,42,41,41,41,41,40,40,40},//71.5
                    { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,47,47,46,46,46,45,45,45,45,44,44,44,43,43,43,42,42,42,42,41,41,41,40,40},//72
                    { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,47,47,46,46,46,45,45,45,44,44,44,44,43,43,43,42,42,42,42,41,41,41},//72.5
                    { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,47,46,46,46,45,45,45,45,44,44,44,43,43,43,43,42,42,42,41,41},//73
                    { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,47,47,46,46,46,45,45,45,44,44,44,44,43,43,43,42,42,42,42},//73.5
                    { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,47,46,46,46,46,45,45,45,44,44,44,44,43,43,43,42,42},//74
                    { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,47,47,46,46,46,45,45,45,45,44,44,44,43,43,43,43},//74.5
                    { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,47,46,46,46,46,45,45,45,44,44,44,44,43,43},//75
                    { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,47,47,46,46,46,46,45,45,45,44,44,44,44},//75.5
                    { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,47,47,46,46,46,45,45,45,45,44,44},//76
                    { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,47,46,46,46,46,45,45,45,44},//76.5
                    { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,47,47,46,46,46,45,45,45},//77
                    { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,47,47,46,46,46,45},//77.5
                    { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,47,47,46,46,46},//78
                    { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,47,47,46},//78.5
                    { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,47}//79
            };
                Height = (Height - 58)*2;
                Circumference = (Circumference - 45)*2;
                if (Circumference > 29)
                    fat = "Circumference value too large for table.  Circumference value = " + Double.toString(Circumference);
                else if (Height < 0)
                    fat = "Too short for table. Minimum height is 58''";
                else if (Height > 42)
                    fat = "Too tall for table, Maximum height is 77.5''";
                else if ((Height%2 !=0)&&(Height%2 !=1))
                    fat = "Please enter a height in half inch increments.";
                else if (Circumference < 0 )
                    fat = "Circumference value does not fall on table [Circumference value = Waist + hips - neck]";
                else if(Circumference >=0) {
                    goodResults = true;
                    fat = Integer.toString(femaleMatrix[(int) Circumference][(int)Height]);
                }
                else
                    fat = Integer.toString(102); // I don't know
        }

        return fat;

    }

    private int getWeightMin()
    {
        int[] Weight = {85,88,91,94,97,100,104,107,110,114,117,121,125,128,132,136,140,144,148,152,156,160,164,168,173,177,182};
        return Weight[(int)Math.ceil(height)-56];
    }

    private int getWeightMax()
    {
        int standard;
        int[] maleWeight = {122,127,131,136,141,145,150,155,160,165,170,175,180,186,191,197,202,208,214,220,225,231,237,244,250,256,263};
        int[] femaleWeight = {115,120,124,129,133,137,142,146,151,156,161,166,171,176,181,186,191,197,202,208,213,219,225,230,236,242,248};

        if(gender)
                standard = maleWeight[(int)Math.ceil(height)-56];
        else
                standard = femaleWeight[(int)Math.ceil(height)-56];

        return standard;
    }

    public int getWeightMin(int newHeight)
    {
        int[] Weight = {85,88,91,94,97,100,104,107,110,114,117,121,125,128,132,136,140,144,148,152,156,160,164,168,173,177,182};
        return Weight[newHeight-56];
    }

    public int getWeightMax(int newHeight, boolean newGender)
    {
        int standard;
        int[] maleWeight = {122,127,131,136,141,145,150,155,160,165,170,175,180,186,191,197,202,208,214,220,225,231,237,244,250,256,263};
        int[] femaleWeight = {115,120,124,129,133,137,142,146,151,156,161,166,171,176,181,186,191,197,202,208,213,219,225,230,236,242,248};

        if(newGender)
            standard = maleWeight[newHeight-56];
        else
            standard = femaleWeight[newHeight-56];

        return standard;
    }


    private double getCValue()
    {
        double value = 0;
        if (gender)
            value = abs - neck;
        else
            value =  abs + hips - neck;
        return value;
    }
}