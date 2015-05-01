package com.projects.android.protomoldpuzzle;

import android.util.Log;

/**
 * Created by jrslagle on 2/25/15.
 */
public class Block {
    private String name; // "green" "purple"
    private String code; // initial, orientation, location "GT6001
    private int volume;
    private int permutations;
    private char[][][] shape;
    private static final String TAG = "Block";

    public Block (Block original) {
        this.name = original.name;
        this.code = original.code;
        this.volume = original.volume;
        this.permutations = original.permutations;
        this.shape = original.shape.clone();
    }

    public Block (String color) {
        name = color;
//        String code; // initial, orientation, location "GT6201"
//        shape = new char[1][1][1];
        if(color.equals("green")) {
            // bounding volume = 4*4*3 = 48
            volume = 13;
            permutations = 48;
            code = "GT0000";
            shape = new char[][][]
                    {{{'G','G','G'},
                      {'G','_','_'},
                      {'_','_','_'},
                      {'_','_','_'}},

                        {{'_','_','_'},
                         {'G','_','_'},
                         {'G','_','_'},
                         {'G','G','G'}},

                            {{'_','_','_'},
                             {'G','_','_'},
                             {'_','_','_'},
                             {'_','_','_'}},

                                {{'G','G','_'},
                                 {'G','_','_'},
                                 {'_','_','_'},
                                 {'_','_','_'}}};

        } else if (color.equals("purple")) {
            // bounding volume = 4*3*2 = 24
            volume = 8;
            permutations = 144;
            code = "PT0000";
            shape = new char[][][]
                    {{{'_','_'},
                      {'_','_'},
                      {'P','P'}},

                        {{'_','_'},
                         {'_','_'},
                         {'P','_'}},

                            {{'_','_'},
                             {'_','_'},
                             {'P','_'}},

                                {{'_','P'},
                                 {'_','P'},
                                 {'P','P'}}};

        } else if (color.equals("orange")) {
            // bounding volume = 3*3*2 = 18
            volume = 9;
            permutations = 288;
            code = "OT0000";
            shape = new char[][][]
                    {{{'O','_'},
                      {'_','_'},
                      {'_','_'}},

                        {{'O','_'},
                         {'O','O'},
                         {'_','_'}},

                            {{'O','O'},
                             {'O','O'},
                             {'_','O'}}};

        } else if (color.equals("red")) {
            // bounding volume = 3*3*2 = 18
            volume = 8;
            permutations = 288;
            code = "RT0000";
            shape = new char[][][]
                    {{{'_','_'},
                      {'_','_'},
                      {'_','R'}},

                        {{'_','R'},
                         {'_','_'},
                         {'R','R'}},

                            {{'_','R'},
                             {'_','R'},
                             {'R','R'}}};

        } else if (color.equals("yellow")) {
            // bounding volume = 3*3*2 = 18
            volume = 6;
            permutations = 288;
            code = "YT0000";
            shape = new char[][][]
                    {{{'_','Y'},
                      {'_','_'},
                      {'_','_'}},

                        {{'_','Y'},
                         {'Y','Y'},
                         {'Y','_'}},

                           {{'_','_'},
                            {'_','_'},
                            {'Y','_'}}};

        } else if (color.equals("black")) {
            // bounding volume = 4*2*2 = 16
            volume = 6;
            permutations = 216;
            code = "KT0000";
            shape = new char[][][]
                    {{{'K','_'},
                      {'_','_'}},

                        {{'K','_'},
                         {'K','_'}},

                            {{'K','K'},
                             {'_','_'}},

                                {{'K','_'},
                                 {'_','_'}}};

        } else if (color.equals("clear")) {
            // bounding volume = 2*3*2 = 12
            volume = 5;
            permutations = 432;
            code = "CT0000";
            shape = new char[][][]
                    {{{'C','C'},
                      {'C','_'},
                      {'C','_'}},

                        {{'_','_'},
                         {'_','_'},
                         {'C','_'}}};

        } else if (color.equals("white")) {
            // bounding volume = 2*2*2 = 8
            volume = 5;
            permutations = 648;
            code = "WT0000";
            shape = new char[][][]
                    {{{'W','_'},
                      {'W','_'}},

                        {{'W','W'},
                         {'_','W'}}};

        } else if (color.equals("blue")) {
            // bounding volume = 2*2*2 = 8
            volume = 5;
            permutations = 648;
            code = "BT0000";
            shape = new char[][][]
                    {{{'B','_'},
                      {'B','B'}},

                        {{'B','_'},
                         {'_','_'}}};

        } else {
            // error, color name is unknown.
            code = " ";
            Log.e(TAG, "Cannot initialize " + color + "block, " +
                    "color is unknown.");
        }
    }

    // x = left to right;
    // y = front to back;
    // z = top to bottom;

    public void pitchBlock() {
        // pitches block 90 degrees toward the viewer
        // like a plane steering upward.

        // renaming block code
        char[] codeArray = code.toCharArray();
        if(codeArray[1] == 'T') {
            codeArray[1] = 'F';
        } else if(codeArray[1] == 'F') {
            codeArray[1] = 'B';
        } else if(codeArray[1] == 'B') {
            codeArray[1] = 'K';
            codeArray[2] = invertClockAngle(codeArray[2]);
        } else if(codeArray[1] == 'K') {
            codeArray[1] = 'T';
            codeArray[2] = invertClockAngle(codeArray[2]);
        } else if(codeArray[1] == 'L') {
            codeArray[2] = rotateClockAngle(codeArray[2]);
        } else if(codeArray[1] == 'R') {
            codeArray[2] = reverseClockAngle(codeArray[2]);
        } else {
            Log.e(TAG, "Cannot pitch block, " + codeArray + " is an " +
                    "invalid code code.");
        }
        code = String.valueOf(codeArray);

        // turned[x][z][-y] = shape[x][y][z]

        // creates an array with the same dimensions as shape, but pitched
        char[][][] turned = new char[shape.length][shape[0][0]
                .length][shape[0].length];

        // translates the block from the reference of the original block
        for (int z = 0; z < shape[0][0].length; z++) {
            for (int y = 0; y < shape[0].length; y++) {
                for (int x = 0; x < shape.length; x++) {
                    turned[x][shape[0][0].length - z - 1][y] = shape[x][y][z];
                }
            }
        }
        shape = turned;
    }

    public void yawBlock() {
        // turns block 90 degrees clockwise from a top down reference.

        // renaming block code
        char[] codeArray = code.toCharArray();
        if(codeArray[1] == 'T') {
            codeArray[2] = rotateClockAngle(codeArray[2]);
        } else if(codeArray[1] == 'F') {
            codeArray[1] = 'L';
        } else if(codeArray[1] == 'B') {
            codeArray[2] = reverseClockAngle(codeArray[2]);
        } else if(codeArray[1] == 'K') {
            codeArray[1] = 'R';
        } else if(codeArray[1] == 'L') {
            codeArray[1] = 'K';
        } else if(codeArray[1] == 'R') {
            codeArray[1] = 'F';
        } else {
            Log.e(TAG, "Cannot yaw block, " + codeArray + " is an " +
                    "invalid code code.");
        }
        code = String.valueOf(codeArray);

        // turned[y][-x][z] = shape[x][y][z]

        // creates an array with the same dimensions as shape, but pitched
        char[][][] turned = new char[shape[0].length][shape.length][shape[0][0]
                .length];

        // translates the block from the reference of the original block
        for (int z = 0; z < shape[0][0].length; z++) {
            for (int y = 0; y < shape[0].length; y++) {
                for (int x = 0; x < shape.length; x++) {
                    turned[shape[0].length - y - 1][x][z] = shape[x][y][z];
                }
            }
        }
        shape = turned;
    }

    public void rollBlock() {
        // turns block 90 degrees clockwise from a front facing reference.

        // renaming block code
        char[] codeArray = code.toCharArray();
        if(codeArray[1] == 'T')  {
            codeArray[1] = 'R';
            codeArray[2] = rotateClockAngle(codeArray[2]);
        } else if(codeArray[1] == 'F') {
            codeArray[2] = rotateClockAngle(codeArray[2]);
        } else if(codeArray[1] == 'B') {
            codeArray[1] = 'L';
            codeArray[2] = rotateClockAngle(codeArray[2]);
        } else if(codeArray[1] == 'K') {
            codeArray[2] = reverseClockAngle(codeArray[2]);
        } else if(codeArray[1] == 'L') {
            codeArray[1] = 'T';
            codeArray[2] = rotateClockAngle(codeArray[2]);
        } else if(codeArray[1] == 'R') {
            codeArray[1] = 'B';
            codeArray[2] = rotateClockAngle(codeArray[2]);
        } else {
            Log.e(TAG, "Cannot roll block, " + codeArray + " is an " +
                    "invalid code code.");
        }
        code = String.valueOf(codeArray);

        // turned[z][y][-x] = shape[x][y][z]

        // creates an array with the same dimensions as shape, but pitched
        char[][][] turned = new char[shape[0][0].length][shape[0]
                .length][shape.length];

        // translates the block from the reference of the original block
        for (int z = 0; z < shape[0][0].length; z++) {
            for (int y = 0; y < shape[0].length; y++) {
                for (int x = 0; x < shape.length; x++) {
                    turned[shape[0][0].length - z - 1][y][x] = shape[x][y][z];
                }
            }
        }
        shape = turned;
    }

    private char invertClockAngle(char angle) {
        if(angle == '0') angle = '6';
        else if(angle == '3') angle = '9';
        else if(angle == '6') angle = '0';
        else if(angle == '9') angle = '3';
        else {
            Log.e(TAG, "Cannot turn block, " + angle + " is an " +
                    "invalid angle code.");
        }
        return angle;
    }

    private char rotateClockAngle(char angle) {
        if(angle == '0') angle = '3';
        else if(angle == '3') angle = '6';
        else if(angle == '6') angle = '9';
        else if(angle == '9') angle = '0';
        else {
            Log.e(TAG, "Cannot turn block, " + angle + " is an " +
                    "invalid angle code.");
        }
        return angle;
    }

    private char reverseClockAngle(char angle) {
        if(angle == '0') angle = '9';
        else if(angle == '3') angle = '0';
        else if(angle == '6') angle = '3';
        else if(angle == '9') angle = '6';
        else {
            Log.e(TAG, "Cannot turn block, " + angle + " is an " +
                    "invalid angle code.");
        }
        return angle;
    }

    public String getName() {return name;}

    public int getVolume() {return volume;}

    public int getPermutations() {return permutations;}

    public String getCode() {return code;}

    public void setCode(String newCode) {code = newCode;}

    public char[][][] getShape() {return shape;}

    @Override
    public String toString() {
        String output = "Block " + code + ":\n";

        for (int y = 0; y < shape[0].length; y++) {
            for (int z = 0; z < shape[0][0].length; z++) {
                output = output.concat("[");
                for (int x = 0; x < shape.length; x++) {
                    output = output.concat(String.valueOf(shape[x][y][z]));
                }
                output = output.concat("]\t");
            }
            output = output.concat("\n");
        }
        output = output.concat("\n");
        return output;
    }
}
