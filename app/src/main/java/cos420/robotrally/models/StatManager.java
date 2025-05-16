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

    ///This is the number of moves that is ideal for the level
    public int idealNumMoves;

    ///This is the number of moves that the user used in their best attempt
    private int bestNumMoves;

    ///This is the number of moves that the user used in their most recent attempt
    public int currentNumMoves;

    ///This is the efficiency score of the user's best attempt
    private double efficiencyScore;

    ///This is the number of collectibles possible for the current level
    public int numberPossibleCollectibles;

    ///This is the number of collectibles that the user collected on their current attempt
    public int currentNumberCollectibles;

    ///This is the number of collectibles that the user collected on their best attempt
    private int bestNumberCollectibles;

    ///This is the percentage of collectibles that the user collected
    private double bestCollectiblesPercentage;

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

        //This is updating all of the variables in this class with the save data
        currentMoveSequence = currentSave.GetCurrentMoveSequence();
        bestMoveSequence = currentSave.GetBestMoveSequence();
        currentNumAttempts = currentSave.GetNumAttempts();
        bestNumMoves = currentSave.GetBestNumMoves();
        currentNumMoves = currentSave.GetCurrentNumMoves();
        efficiencyScore = currentSave.GetEfficiencyScore();
        currentNumberCollectibles = currentSave.GetCurrentNumCollectibles();
        bestNumberCollectibles = currentSave.GetBestNumCollectibles();
        bestCollectiblesPercentage = currentSave.GetBestCollectiblesPercentage();

        if(levelID + 1 == 1){
            numberPossibleCollectibles = 2;
            idealNumMoves = 5;
        }
        else if(levelID + 1 == 2){
            numberPossibleCollectibles = 3;
            idealNumMoves = 5;
        }
        else if(levelID + 1 == 3){
            numberPossibleCollectibles = 3;
            idealNumMoves = 4;
        }
        else if(levelID + 1 == 4){
            numberPossibleCollectibles = 3;
            idealNumMoves = 9;
        }
        else if(levelID + 1 == 5){
            numberPossibleCollectibles = 4;
            idealNumMoves = 10;
        }
        else if(levelID + 1 == 6){
            numberPossibleCollectibles = 5;
            idealNumMoves = 10;
        }
    }

    /**
     * This is the method to check if the data saved on the last attempt was the best attempt
     */
    public void checkBest(boolean levelCompleted){
        //If this attempt is the best attempt
        if(currentNumMoves < bestNumMoves && levelCompleted){
            //Update all of the "best" variables with the current variables
            bestMoveSequence = currentMoveSequence;
            bestNumMoves = currentNumMoves;
            efficiencyScore = (double) idealNumMoves / (double) currentNumMoves;
            bestNumberCollectibles = currentNumberCollectibles;
            if(numberPossibleCollectibles > 0) {
                bestCollectiblesPercentage = (double) currentNumberCollectibles / (double) numberPossibleCollectibles;
            }
        }
    }

    /**
     * This is the method to be run if you quit out of the level or quit out of the app to save
     * all of the updated data to persistent storage.
     */
    public void saveCurrent(){

        //Saves the current move sequence
        currentSave.SetCurrentMoveSequence(currentMoveSequence);

        //Saves the best move sequence
        currentSave.SetBestMoveSequence(bestMoveSequence);

        //Saves the number of attempts
        currentSave.SetNumAttempts(currentNumAttempts);

        //Saves the current number of moves
        currentSave.SetCurrentNumMoves(currentNumMoves);

        //Saves the best number of moves
        currentSave.SetBestNumMoves(bestNumMoves);

        //Saves the efficiency score
        currentSave.SetEfficiencyScore(efficiencyScore);

        //Saves the current number of collectibles
        currentSave.SetCurrentNumCollectibles(currentNumberCollectibles);

        //Saves the best number of collectibles
        currentSave.SetBestNumCollectibles(bestNumberCollectibles);

        //Saves the best collectibles percentage
        currentSave.SetBestCollectiblesPercentage(bestCollectiblesPercentage);

        //Saves the updated data to the shared preferences
        currentSave.saveLevelData();
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

    /**
     * Calculates the number of moves based on the move sequence
     * @return number of moves
     */
    public int calcNumMoves(){
        String thisMoveSequence = currentMoveSequence;
        String[] moves = thisMoveSequence.split(" ");
        int numMoves = 0;

        for(int i = 0; i < moves.length; i++){
            numMoves += 1;
        }

        return numMoves;
    }

    /**
     * Gets current move sequence
     * @return move sequence string
     */
    public String GetCurrentMoveSequence(){
        return currentMoveSequence;
    }

    /**
     * Gets best move sequence
     * @return move sequence string
     */
    public String GetBestMoveSequence(){
        return bestMoveSequence;
    }


    /**
     * Gets number of attempts
     * @return number of attempts int
     */
    public int GetCurrentNumAttempts(){
        return currentNumAttempts;
    }

    /**
     * Gets best number of moves
     * @return best number of moves int
     */
    public int GetBestNumMoves(){
        return bestNumMoves;
    }

    /**
     * Gets current number of moves
     * @return current number of moves int
     */
    public int GetCurrentNumMoves(){
        return currentNumMoves;
    }

    /**
     * Gets efficiency score
     * @return efficiency score double
     */
    public double GetEfficiencyScore(){
        return efficiencyScore;
    }

    /**
     * Gets the current number of collectibles
     * @return the current number of collectibles collected
     */
    public int getCurrentNumberCollectibles(){
        return currentNumberCollectibles;
    }

    /**
     * Gets the best number of collectibles
     * @return the best number of collectibles collected
     */
    public int getBestNumberCollectibles(){
        return bestNumberCollectibles;
    }

    /**
     * Gets the best collectibles percentage recorded
     * @return best collectibles percentage
     */
    public double getBestCollectiblesPercentage(){
        return bestCollectiblesPercentage;
    }

    /**
     * Gives engineer functionality to reset to default values
     */
    public void resetSave(){
        try{
            currentSave.ClearSave();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        currentMoveSequence = "";
        bestMoveSequence = "";
        currentNumAttempts = 0;
        bestNumMoves = 1000;
        currentNumMoves = 1000;
        efficiencyScore = 0;
        currentNumberCollectibles = 0;
        bestCollectiblesPercentage = 0;
    }

    /**
     * Saves the most recent attempt to memory
     * @param win whether the attempt was successful or not
     */
    public void saveCurrentMoveSequence(boolean win){
        try {
            currentSave.saveLevelAttempt(currentMoveSequence, currentNumMoves, win);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * The method to get past move sequences from memory
     * @param attemptNumber how many attempts ago you want to get (last move -> 1, 2 moves ago -> 2, etc.)
     * @return a string representing that move sequence
     */
    public String getPastMoveSequence(int attemptNumber){
        return currentSave.getPastMoveSequence(attemptNumber);
    }

    /**
     * Method to get the number of moves in an attempt
     * @param attemptNumber how many attempts ago you want to get
     * @return int - how many moves there were
     */
    public int getPastMoveCount(int attemptNumber) {
        return currentSave.getPastNumMoves(attemptNumber);
    }

    public boolean getPastMoveSequenceSuccess(int attemptNumber){
        return currentSave.getPastMoveSequenceSuccess(attemptNumber);
    }

    public int getEndPoint(){
        return currentSave.getEndPoint();
    }

    public int getSavedMSNumber(){
        return currentSave.getSavedMSNumber();
    }
}
