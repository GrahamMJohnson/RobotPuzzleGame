package cos420.robotrally.models;

import android.content.Context;

import java.util.List;

import cos420.robotrally.adaptersAndItems.MoveItem;

/**
 * This is the StatManager class. This class is designed to hold all of the variables that need to be
 * tracked for stats to be saved and displayed
 *
 * This class is designed to be referenced and updated by the main class to hold not only the best
 * stats that need to be persistent between sessions, but also stats from each attempt that need
 * to be compared to the best attempt and then decide if the stats should be saved.
 */
public class StatManager {

    ///--------------
    /// Global Variables
    /// **These are the variables to be referenced by other classes**
    ///--------------

    ///This is the move sequence that was attempted in the most recent attempt
    public String currentMoveSequence;

    ///This is the move sequence that was attached to the best attempt at the level
    private String bestMoveSequence;

    ///This is the number of attempts that the user currently has on the level
    public int currentNumAttempts;

    //TODO: Find where ideal number of moves are stored and connect
    ///This is the number of moves that is ideal for the level
    private int idealNumMoves;

    ///This is the number of moves that the user used in their best attempt
    private int bestNumMoves;

    ///This is the number of moves that the user used in their most recent attempt
    public int currentNumMoves;

    ///This is the efficiency score of the user's best attempt
    public int efficiencyScore;

    //TODO: Find where possible number of collectibles are stored and connect
    ///This is the number of collectibles possible for the current level
    private int numberPossibleCollectibles;

    ///This is the number of collectibles that the user collected on their current attempt
    public int currentNumberCollectibles;

    ///This is the percentage of collectibles that the user collected
    public int bestCollectiblesPercentage;

    ///This is the current RobotRallySave instance that is being saved to
    private RobotRallySave currentSave;


    ///--------------
    /// Constructors
    ///--------------

    /**
     * This is the constructor for the StatManager class
     * @param thisContext is the context from the main activity to be passed into the shared prefs
     * @param levelID is the number of the level that you are saving for
     */
    public StatManager(Context thisContext, int levelID){
        //This creates a new save instance with the given context and levelID
        currentSave = new RobotRallySave(thisContext, levelID);

        //This is updating all of the "best" variables in this class with the save data
        bestMoveSequence = currentSave.GetMoveSequence();
        bestNumMoves = currentSave.GetMoveAttempts();
        efficiencyScore = currentSave.GetEfficiencyScore();
        bestCollectiblesPercentage = currentSave.GetPercentageCollectiblesCollected();
    }

    /**
     * This is the method to check if the data saved on the last attempt was the best attempt
     */
    public void checkBest(){
        //If this attempt is the best attempt
        if(currentNumMoves > bestNumMoves){
            //Update all of the "best" variables with the current variables
            bestMoveSequence = currentMoveSequence;
            bestNumMoves = currentNumMoves;
            bestCollectiblesPercentage = currentNumberCollectibles/numberPossibleCollectibles;
            //TODO: Figure out how to calculate efficiency score
        }
    }

    /**
     * This is the method to be run if you quit out of the level or quit out of the app to save
     * all of the updated data to persistent storage.
     */
    public void saveCurrent(){

        //Saves the move sequence
        currentSave.SetMoveSequence(bestMoveSequence);

        //Saves the number of attempts
        currentSave.SetNumAttempts(currentNumAttempts);

        //Saves the efficiency score
        currentSave.SetEfficiencyScore((currentNumMoves/idealNumMoves)*100);

        //Saves the total squares traveled
        currentSave.SetTotalSquaresTraveled(bestNumMoves);

        //Calculates and saves the current move difference
        int currentMoveDifference = currentNumMoves - idealNumMoves;
        currentSave.SetCurrentMoveDifference(currentMoveDifference);

        //Calculates and saves the best move difference
        int bestMoveDifference = bestNumMoves - idealNumMoves;
        currentSave.SetBestMoveDifference(bestMoveDifference);

        //Calculates and saves the percentage collectibles collected
        int percentCollectiblesCollected = currentNumberCollectibles/numberPossibleCollectibles;
        currentSave.SetCollectiblesCollected(percentCollectiblesCollected);
    }

    /**
     * Since the move sequences are typically being stored as lists, this converts it to a string
     * @param moveItemList the list from main activity containing all of the moves
     * @return the list of moves represented as a string
     */
    public String MoveSequenceConverter(List<MoveItem> moveItemList){
        //Initializes the string that contains all of the moves
        String moveSequenceString = "";

        //Goes through the whole list of moves and converts the move list into text
        for(int i = 0; i < moveItemList.size(); i++){
            MoveItem currentMove = moveItemList.get(i);
            moveSequenceString += currentMove.getText() + " ";
        }

        //returns the move sequence as a string
        return moveSequenceString;
    }

}
