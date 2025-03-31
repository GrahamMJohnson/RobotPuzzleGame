package cos420.robotrally.models;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * This is the robotRallySave class
 */
public class RobotRallySave {

    //Initializes the file writer
    FileWriter save_file;

    //initializes collectibles percentage variable for if/else statement to avoid div by 0
    float collectiblesPercentage;

    //These are the values to change to represent the number of moves for the special moves
    int specialMoveCount1 = 5;
    int specialMoveCount2 = 10;

    //This is initializing the string that is going to return all of the data contained within the
    //save
    String[] fileRead;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor prefEditor;

    /**
     * This is the default constructor for a robotRallySave object
     * It should create shared preferences for the game.
     * @param context this is the context that you are in
     */
    public RobotRallySave(Context context) {
        sharedPreferences = context.getSharedPreferences("RobotRallySave",
                Context.MODE_PRIVATE);
    }

    /**
     * This is the method used to clear the save file for testing purposes
     */
    public void ClearSave() {
        prefEditor = sharedPreferences.edit();
        prefEditor.putString("moveSequence", "");
        prefEditor.putFloat("numberOfAttempts", 0);
        prefEditor.putFloat("efficiencyScore", 0);
        prefEditor.putInt("totalSquaresTraveled", 0);
        prefEditor.putString("currentMoveDifference", "");
        prefEditor.putString("bestMoveDifference", "");
        prefEditor.putString("percentCollectiblesCollected", "");
        prefEditor.apply();
    }

    /**
     * This is the call for writing a save in a file
     * @param moveSequence the current sequence of moves that is the best
     * @param numberOfAttempts the number of attempts the user has taken at the level
     * @param optimalMoveCount is the number of moves for the level that is optimal
     * @param currentMoveCount is the number of moves that the current attempt has
     * @param bestMoveCount is the number of moves that the best attempt had
     * @param tilesCollected is the number of tiles that have been collected by the user
     * @param tilesPossibleToCollect is the number of tiles that have collectibles on them
     */
    public void WriteAllToSave(char[] moveSequence, float numberOfAttempts, int optimalMoveCount,
                               int currentMoveCount, int bestMoveCount, int tilesCollected,
                               int tilesPossibleToCollect) {

        //Initializes the string for building the move sequence save
        String moveSequenceString = "";

        //Initializes the writing tools that we will be using
        prefEditor = sharedPreferences.edit();

        //Writes all of the move sequence data to the shared preference
        for(int i: moveSequence) {
            moveSequenceString += moveSequence[i];
        }
        prefEditor.putString("moveSequence", moveSequenceString);

        //Writes the number of attempts to the shared preference
        prefEditor.putFloat("numberOfAttempts", numberOfAttempts);

        //Calculates and writes the efficiency score to the shared preferences
        float efficiencyScore = (numberOfAttempts / optimalMoveCount) * 1000;
        prefEditor.putFloat("efficiencyScore", efficiencyScore);

        //calculates the total squares moved and writes that to the shared preferences
        //In this case, s is special move one (5 moves), x is special move 2 (10 moves)
        int totalSquaresTraveled = 0;
        for(int i : moveSequence) {
            if(moveSequence[i] == 's') {
                totalSquaresTraveled += specialMoveCount1;
            }
            else if(moveSequence[i] == 'x') {
                totalSquaresTraveled += specialMoveCount2;
            }
            else {
                totalSquaresTraveled += 1;
            }
        }
        prefEditor.putInt("totalSquaresTraveled", totalSquaresTraveled);

        //Calculates the difference in move count between the current and the optimal
        //and writes that to the shared preferences
        int currentDifference = currentMoveCount - optimalMoveCount;
        prefEditor.putInt("currentMoveDifference", currentDifference);


        //Calculates the difference in move count between the current and the optimal
        //and writes that to the shared preferences
        int bestDifference = bestMoveCount - optimalMoveCount;
        prefEditor.putInt("bestMoveDifference", bestDifference);

        //Calculates the percentage of collectibles collected and writes that to the shared prefs
        //If/Else is to avoid div by 0 if there are no possible collection tiles
        if(tilesPossibleToCollect != 0) {
            collectiblesPercentage = ((float)tilesCollected/ (float)tilesPossibleToCollect) * 100;
        }
        else {
            collectiblesPercentage = 100;
        }
        prefEditor.putFloat("percentageCollectiblesCollected", collectiblesPercentage);
        prefEditor.apply();
    }


    //These are all of the setters for all of the different values that need to be saved.
    public void SetMoveSequence(String MoveSequence){
        prefEditor.putString("moveSequence", MoveSequence);
        prefEditor.apply();
    }

    public void SetNumAttempts(int numOfAttempts){
        prefEditor.putInt("numberOfAttempts", numOfAttempts);
        prefEditor.apply();
    }

    public void SetEfficiencyScore(float EfficiencyScore){
        prefEditor.putFloat("efficiencyScore", EfficiencyScore);
        prefEditor.apply();
    }

    public void SetTotalSquaresTraveled(int TotalSquaresTraveled){
        prefEditor.putInt("totalSquaresTraveled", TotalSquaresTraveled);
        prefEditor.apply();
    }

    public void SetCurrentMoveDifference(int currentMoveDifference){
        prefEditor.putInt("currentMoveDifference", currentMoveDifference);
        prefEditor.apply();
    }

    public void SetBestMoveDifference(int bestMoveDifference){
        prefEditor.putInt("bestMoveDifference", bestMoveDifference);
        prefEditor.apply();
    }

    public void SetCollectiblesCollected(float percentCollectiblesCollected){
        prefEditor.putFloat("percentCollectiblesCollected", percentCollectiblesCollected);
        prefEditor.apply();
    }


    //These are all of the getters for all of the saved values
    public String GetMoveSequence(){
        return sharedPreferences.getString("moveSequence", "");
    }

    public int GetMoveAttempts(){
        return sharedPreferences.getInt("numberOfAttempts", 0);
    }

    public float GetEfficiencyScore(){
        return sharedPreferences.getFloat("efficiencyScore", 100);
    }

    public int GetTotalSquaresTraveled(){
        return sharedPreferences.getInt("totalSquaresTraveled", 0);
    }

    public int GetCurrentMoveDifference(){
        return sharedPreferences.getInt("currentMoveDifference", 0);
    }

    public int GetBestMoveDifference(){
        return sharedPreferences.getInt("bestMoveDifference", 0);
    }

    public int GetPercentageCollectiblesCollected(){
        return sharedPreferences.getInt("percentageCollectiblesCollected", 100);
    }
}
