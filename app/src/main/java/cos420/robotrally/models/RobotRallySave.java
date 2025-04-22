package cos420.robotrally.models;

import android.content.Context;
import android.content.SharedPreferences;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * The RobotRallySave class allows for parameters to be passed into JSON objects -> (strings passed in
 * as keys to be able to reference specific pieces of data). These JSON objects are then turned into
 * strings and stored in sharedPreferences (similar to JSON objects, but persistent [keys to
 * reference specific pieces of data])
 *
 * Handles all of the saving and persistence in the RobotRally project.
 */
public class RobotRallySave {

    ///--------------
    /// Global Variables
    ///--------------

    /**
     * This is the main instance of SharedPreferences. This will be referenced to save JSON objects
     * to, and retrieve JSON objects from the SharedPreferences functionality this project uses.
     */
    private final SharedPreferences sharedPreferences;

    /**
     * Current JSON object being saved to and retrieved from. Each level has its own JSON file to
     * hold all of the data that needs to be persistent, so each level will reference a different
     * currentJSON object.
     */
    JSONObject currentJSON;

    /**
     * The reference key used to save <code>currentJSON</code> in <code>sharedPreferences</code>.
     */
    String sharedKey;

    /**
     * This is the name that we are going to use in the JSON name param. Essentially a key to
     * retrieve data. They are saved to avoid typos inputting keys into save/retrieval calls.
     * The variable names are telling of what each one is used for in retrieving/saving when
     * inputted into JSON object editing/reading calls.
     */
    String jsonMoveSequence = "move_sequence";
    String jsonNumAttempts = "num_attempts";
    String jsonEfficiencyScore = "efficiency_score";
    String jsonSquaresTraveled = "total_squares_traveled";
    String jsonCurMoveDiff = "current_move_difference";
    String jsonBestMoveDiff = "best_move_difference";
    String jsonPercentCollectibles = "percent_collected";
    String jsonSavedMoveSequence = "saved_move_sequence";
    String jsonBeginningMoveSequences = "number_saved_move_sequences";
    String jsonEndMoveSequences = "start_point";
    String jsonNumberMoveSequences = "number_saved_moves";



    ///--------------
    /// Constructors
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
            currentJSON.put(jsonBeginningMoveSequences, 0);
        }
        //if bad JSON string, it will create a new JSON file
        catch(JSONException e){
            e.printStackTrace();
            currentJSON = new JSONObject();
        }
    }

    ///--------------
    /// Debug/Testing
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
    /// Class Setters
    ///--------------


    /**
     * This is the method to save level attempts
     * @param moveSequence the string representation of the move sequence that you want to save.
     */
    public void saveLevelAttempt(String moveSequence) throws Exception{
        //Finds the current key that is next to be saved to based on how many have already been saved
        String currentKey = jsonSavedMoveSequence + (currentJSON.getInt(jsonBeginningMoveSequences));
        //Saves the desired move sequence to the correct slot
        currentJSON.put(currentKey, moveSequence);

        //If there are the full amount of move sequences saved, iterate the start point by one each time you save
        if(currentJSON.getInt(jsonNumberMoveSequences) > 14){
            currentJSON.put(jsonBeginningMoveSequences, ((currentJSON.getInt(jsonBeginningMoveSequences) + 1) % 15));
        }
        //Add one to the end index of the move sequence save
        currentJSON.put(jsonEndMoveSequences, ((currentJSON.getInt(jsonEndMoveSequences) + 1) % 15));

        //Add one more to the current number of move sequences being saved
        currentJSON.put(jsonNumberMoveSequences, (currentJSON.getInt(jsonNumberMoveSequences) + 1));
    }

    /**
     * This is the method for saving the move sequence into the JSON Object
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
     * This is the method for saving the number of attempts into the JSON Object
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
     * This is the method for saving the efficiency score into the JSON object
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
     * This is the method for saving the number of squares that the user traveled into the JSON object
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
     * This is the method for saving the difference between the current and optimal moves into the JSON object
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
     * This is the method for saving the difference between the best and optimal moves into the JSON object
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
     * This is the method for saving the percentage of collectibles that the user has collected into the JSON object
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
    /// Class Getters
    ///--------------

    /**
     * This is the method for getting the saved move sequence from the JSON object
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
     * This is the method for getting the saved move attempts from the JSON object
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
     * This is the method for getting the saved efficiency score from the JSON object
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
     * This is the method for getting the saved squares traveled from the JSON object
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
     * This is the method for getting the saved difference between the current and optimal move count from the JSON object
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
     * This is the method to save the difference between the best and optimal move counts from the JSON object.
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
     * This is the method to save the percentage of collectibles collected from the JSON object
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

    /**
     * This is the method to get the move sequences from memory
     * @param attemptNumber the how many attempts have gone by since that one was saved
     */
    public String getPastMoveSequence(int attemptNumber){
        try{
            //Find the key for the attempt x times ago
            String currentKey = jsonMoveSequence + (currentJSON.getInt(jsonEndMoveSequences) - attemptNumber);
            //return that attempt
            return currentJSON.getString(currentKey);
        }
        //if it fails to retrieve the data, return N/A if no data is found
        catch (JSONException e) {
            e.printStackTrace();
            return "N/A";
        }
    }

    ///--------------
    /// Write to Memory
    ///--------------

    /**
     * This is the method for saving all of the data contained within the JSON (edited by this class)
     * This should be put after ANY DATA is added to the save using the methods in this class.
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
