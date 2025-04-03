package cos420.robotrally.models;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * This is the robotRallySave class
 */
public class RobotRallySave {

    ///--------------
    /// These are the global variables for the RobotRallySave class
    ///--------------

    //These are the shared prefs
    private final SharedPreferences sharedPreferences;

    //JSON Object and the key to use to save that JSON Object in shared prefs
    JSONObject currentJSON;
    String sharedKey;

    //this is the name that we are going to use in the JSON name param
    String jsonMoveSequence = "move_sequence";
    String jsonNumAttempts = "num_attempts";
    String jsonEfficiencyScore = "efficiency_score";
    String jsonSquaresTraveled = "total_squares_traveled";
    String jsonCurMoveDiff = "current_move_difference";
    String jsonBestMoveDiff = "best_move_difference";
    String jsonPercentCollectibles = "percent_collected";


    ///--------------
    /// This is the constructor for the save function
    ///--------------

    /**
     * This is the default constructor for a robotRallySave object
     * It should create shared preferences for the game.
     * @param context this is the context that you are in
     */
    public RobotRallySave(Context context, int levelNumber) {
        //this is the key for the shared prefs that stores the JSON string
        sharedKey = "level" + levelNumber;

        //this is getting the shared preferences
        sharedPreferences = context.getSharedPreferences("RobotRallySave",
                Context.MODE_PRIVATE);

        //this is getting the JSON string from the shared preferences
        String jsonString = sharedPreferences.getString(sharedKey, "{}");

        //it will then use the key to try to construct a JSON Object
        try{
            currentJSON = new JSONObject(jsonString);
        }
        //if bad JSON string, it will create a new JSON file
        catch(JSONException e){
            e.printStackTrace();
            currentJSON = new JSONObject();
        }
    }

    ///--------------
    /// These methods are for testing purposes
    ///--------------

    /**
     * This is the method to reset all of the values for a level save to their defaults
     * @throws Exception JSONException if the JSON Object has any issues
     */
    public void ClearSave() throws Exception {

        //edit all of the values of the passed in JSON object
        currentJSON.put(jsonMoveSequence, "");
        currentJSON.put(jsonNumAttempts, -1);
        currentJSON.put(jsonEfficiencyScore, -1);
        currentJSON.put(jsonSquaresTraveled, -1);
        currentJSON.put(jsonCurMoveDiff, -1);
        currentJSON.put(jsonBestMoveDiff, -1);
        currentJSON.put(jsonPercentCollectibles, -1);
    }

    ///--------------
    /// These are all of the setters for all of the different values that need to be saved.
    ///--------------

    /**
     * This is the method for saving the move sequence
     * @param commandList the string for the list of commands
     */
    public void SetMoveSequence(String commandList){
        //this is to catch passing in a null string (it was giving me an error for this)
        if(commandList == null){
            return;
        }
        try {
            //try putting the string into the json object
            currentJSON.put(jsonMoveSequence, commandList);
        }
        //if it fails, log the exception, and do nothing else
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * This is the method for saving the number of attempts
     * @param numOfAttempts the int representing how many attempts the user has made
     */
    public void SetNumAttempts(int numOfAttempts){
        try {
            //try putting the int into the json object
            currentJSON.put(jsonNumAttempts, numOfAttempts);
        }
        //if it fails, log the exception, and do nothing else
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * This is the method for saving the efficiency score
     * @param efficiencyScore the int representing the user's efficiency score
     */
    public void SetEfficiencyScore(int efficiencyScore){
        try {
            //try putting the int into the json object
            currentJSON.put(jsonEfficiencyScore, efficiencyScore);
        }
        //if it fails, log the exception, and do nothing else
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * This is the method for saving the number of squares that the user traveled
     * @param totalSquaresTraveled the int representing the user's number of squares traveled
     */
    public void SetTotalSquaresTraveled(int totalSquaresTraveled){
        try {
            //try putting the int into the json object
            currentJSON.put(jsonSquaresTraveled, totalSquaresTraveled);
        }
        //if it fails, log the exception, and do nothing else
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * This is the method for saving the difference between the current and optimal moves
     * @param currentMoveDifference the int representing the difference between current and optimal
     */
    public void SetCurrentMoveDifference(int currentMoveDifference) {
        try {
            //try putting the int into the json object
            currentJSON.put(jsonCurMoveDiff, currentMoveDifference);
        }
        //if it fails, log the exception, and do nothing else
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * This is the method for saving the difference between the best and optimal moves
     * @param bestMoveDifference the int representing the difference between best and optimal
     */
    public void SetBestMoveDifference(int bestMoveDifference){
        try {
            //try putting the int into the JSON object
            currentJSON.put(jsonBestMoveDiff, bestMoveDifference);
        }
        //if it fails, log the exception, and do nothing else
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * This is the method for saving the percentage of collectibles that the user has collected
     * @param percentCollectiblesCollected the int representing the percentage
     */
    public void SetCollectiblesCollected(int percentCollectiblesCollected){
        try {
            //try putting the int into the json object
            currentJSON.put(jsonPercentCollectibles, percentCollectiblesCollected);
        }
        //if it fails, log the exception, and do nothing else
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    ///--------------
    /// These are all of the getters for all of the different values that need to be saved.
    ///--------------

    /**
     * This is the method for getting the saved move sequence
     * @return the string that represents the current move sequence
     */
    public String GetMoveSequence(){
        try {
            //try to get the string from the json object
            return currentJSON.getString(jsonMoveSequence);
        }
        //if it fails, log the exception, and return an empty string
        catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * This is the method for getting the saved move attempts
     * @return the int that represents the amount of moves the robot took
     */
    public int GetMoveAttempts() {
        try {
            //try to get the int from the json object
            return currentJSON.getInt(jsonNumAttempts);
        }
        //if it fails, log the exception, and return a default value of -1
        catch (JSONException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * This is the method for getting the saved efficiency score
     * @return the int that represents the efficiency score of the best attempt
     */
    public int GetEfficiencyScore(){
        try {
            //try to get the int from the json object
            return currentJSON.getInt(jsonEfficiencyScore);
        }
        //if it fails, log the exception, and return a default value of -1
        catch (JSONException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * This is the method for getting the saved squares traveled
     * @return the int that represents the number of squares traveled on the best attempt
     */
    public int GetTotalSquaresTraveled(){
        try {
            //try to get the int from the json object
            return currentJSON.getInt(jsonSquaresTraveled);
        }
        //if it fails, log the exception, and return a default value of -1
        catch (JSONException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * This is the method for getting the saved difference between the current and optimal move count
     * @return the int representing the difference between the current and optimal
     */
    public int GetCurrentMoveDifference(){
        try {
            //try to get the int from the json object
            return currentJSON.getInt(jsonCurMoveDiff);
        }
        //if it fails, log the exception, and return a default value of -1
        catch (JSONException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * This is the method to save the difference between the best and optimal move counts
     * @return the int representing the difference between the best and optimal
     */
    public int GetBestMoveDifference(){
        try {
            //try to get the int from the json object
            return currentJSON.getInt(jsonBestMoveDiff);
        }
        //if it fails, log the exception, and return a default value of -1
        catch (JSONException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * This is the method to save the percentage of collectibles collected
     * @return the int representing the percentage of collectibles the user collected
     */
    public int GetPercentageCollectiblesCollected(){
        try {
            //try to get the int from the json object
            return currentJSON.getInt(jsonPercentCollectibles);
        }
        //if it fails, log the exception, and return a default value of -1
        catch (JSONException e) {
            e.printStackTrace();
            return -1;
        }
    }

    ///--------------
    /// This is the actual save function that you need to run
    ///--------------

    /**
     * This is the method for saving all of the sata contained within the json
     * This should be put after any data for the save is updated
     */
    public void saveLevelData(){
        //get the editor for the preferences
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //put the json string into the shared prefs key for this level
        editor.putString(sharedKey, currentJSON.toString());

        //apply the changes to the shared preferences
        editor.apply();
    }

}
