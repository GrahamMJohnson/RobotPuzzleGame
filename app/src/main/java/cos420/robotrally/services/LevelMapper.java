package cos420.robotrally.services;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import cos420.robotrally.helpers.GameBoardData;
import cos420.robotrally.levels.LevelData;
import cos420.robotrally.models.Collectable;
import cos420.robotrally.models.Obstacle;

/**
 * Class that maps in LevelData from text file into list of LevelData classes
 */
public class LevelMapper {

    /** Variable that holds tag for logs for this class */
    private static final String LOG_TAG = "Level Mapper";

    /**
     * Method to map in Level Data
     * @param context Reference to the context that is calling this method
     * @return List of LevelData objects that hold data for all levels
     */
    public static List<LevelData> mapLevelDataFromFile(Context context)
    {
        AssetManager assetManager = context.getAssets();
        List<LevelData> levelList = new ArrayList<>();

        try (InputStream is = assetManager.open("LevelData.txt");
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            String line;
            LevelData level = new LevelData();

            Log.v(LOG_TAG, "Start mapping levels");

            while ((line = reader.readLine()) != null) {
                // remove leading or trailing white space
                line = line.toLowerCase().trim();

                // Check if line is a comment
                if (isCommentOrEmpty(line))
                {
                    Log.v(LOG_TAG, "Ignoring commented line");
                }
                //Check for starting new level
                else if (isNewLevel(line))
                {
                    Log.v(LOG_TAG, "Start setting up new level");

                    level = new LevelData();
                    levelList.add(level);
                    level.gameBoardData = new GameBoardData();
                    level.gameBoardData.obstacles = new ArrayList<>();
                    level.gameBoardData.collectables = new ArrayList<>();

                    Log.v(LOG_TAG,"End setting up new level");
                }
                // Checking if we are reading in an obstacle
                else if (isObstacle(line))
                {
                    // Using try catch because integer parsing could fail
                    try {
                        Log.v(LOG_TAG, "Start mapping obstacle");

                        // Get data for type row and column
                        String[] typeArray = reader.readLine().split(" ");
                        String[] rotationArray = reader.readLine().split(" ");
                        String[] rowArray = reader.readLine().split(" ");
                        String[] columnArray = reader.readLine().split(" ");

                        // Get value for type, rotation, row, and column
                        String type = typeArray[typeArray.length - 1];
                        int rotation = Integer.parseInt(rotationArray[rotationArray.length - 1]);
                        int row = Integer.parseInt(rowArray[rowArray.length - 1]);
                        int column = Integer.parseInt(columnArray[columnArray.length - 1]);

                        // Create obstacle object and add it to list of obstacles
                        level.gameBoardData.obstacles.add(new Obstacle(type, rotation, row, column));

                        Log.v(LOG_TAG, "End mapping obstacle");
                    }
                    catch (Exception e)
                    {
                        Log.e(LOG_TAG, e.getMessage() != null ? e.getMessage() : "Error parsing obstacle data");
                    }
                }
                // Checking if we are reading in a collectable
                else if (isCollectable(line))
                {
                    // Using try catch because integer parsing could fail
                    try {
                        Log.v(LOG_TAG, "Start mapping collectable");

                        // Get data for type row and column
                        String[] rowArray = reader.readLine().split(" ");
                        String[] columnArray = reader.readLine().split(" ");

                        // Get value for type row and column
                        int row = Integer.parseInt(rowArray[rowArray.length - 1]);
                        int column = Integer.parseInt(columnArray[columnArray.length - 1]);

                        // Create collectable object and add it to list of obstacles
                        level.gameBoardData.collectables.add(new Collectable(row, column));

                        Log.v(LOG_TAG, "End mapping collectable");
                    }
                    catch (Exception e)
                    {
                        Log.e(LOG_TAG, e.getMessage() != null ? e.getMessage() : "Error parsing collectable data");
                    }
                }
                // Reading in general game board data
                else
                {
                    // Using try catch because integer parsing could fail
                    try
                    {
                        Log.v(LOG_TAG, "Start mapping general data");

                        // Splitting the lines into arrays
                        String[] sizeArray = reader.readLine().trim().split(" ");
                        String[] startRowArray = reader.readLine().trim().split(" ");
                        String[] startColumnArray = reader.readLine().trim().split(" ");
                        String[] goalRowArray = reader.readLine().trim().split(" ");
                        String[] goalColumnArray = reader.readLine().trim().split(" ");
                        String[] goalRotationArray = reader.readLine().trim().split(" ");
                        String[] maxMovesAArray = reader.readLine().trim().split(" ");
                        String[] maxMovesBArray = reader.readLine().trim().split(" ");

                        // Getting the wanted values
                        int size = Integer.parseInt(sizeArray[sizeArray.length - 1]);
                        int startRow = Integer.parseInt(startRowArray[startRowArray.length - 1]);
                        int startColumn = Integer.parseInt(startColumnArray[startColumnArray.length - 1]);
                        int goalRow = Integer.parseInt(goalRowArray[goalRowArray.length - 1]);
                        int goalColumn = Integer.parseInt(goalColumnArray[goalColumnArray.length - 1]);
                        int goalRotation = Integer.parseInt(goalRotationArray[goalRotationArray.length - 1]);
                        int maxMovesA = Integer.parseInt(maxMovesAArray[maxMovesAArray.length - 1]);
                        int maxMovesB = Integer.parseInt(maxMovesBArray[maxMovesBArray.length - 1]);

                        // Add values to gameBoardData object of level
                        level.gameBoardData.size = size;
                        level.gameBoardData.startRow = startRow;
                        level.gameBoardData.startColumn = startColumn;
                        level.gameBoardData.goalRow = goalRow;
                        level.gameBoardData.goalColumn = goalColumn;
                        level.gameBoardData.goalRotation = goalRotation;
                        level.maxMovesA = maxMovesA;
                        level.maxMovesB = maxMovesB;

                        Log.d("Rotation", String.valueOf(goalRotation));

                        Log.v(LOG_TAG, "End mapping general data");
                    }
                    catch (NumberFormatException e)
                    {
                        Log.e(LOG_TAG, e.getMessage() != null ? e.getMessage() : "Error Parsing input value");
                    }
                }
            }

            Log.v(LOG_TAG, "End mapping levels");
        } catch (Exception e) {
            Log.e("Mapping Level Data", e.getMessage() != null ? e.getMessage() : "Error mapping level data");
        }

        return levelList;
    }

    /**
     * Checks if a line of the data file is a comment or blank
     * @param line The line from the text file to check
     * @return true -> line is a comment<br>
     *         false -> line is data
     */
    private static boolean isCommentOrEmpty(String line)
    {
        return line.startsWith("//") || line.isEmpty();
    }

    /**
     * Checks if line is start of new level
     * @param line The line from the text file to check
     * @return True or False
     */
    private static boolean isNewLevel(String line)
    {
        return line.startsWith("-----------------");
    }

    /**
     * Checks if the line is declaring an Obstacle
     * @param line The line from the text file to check
     * @return True or False
     */
    private static boolean isObstacle(String line)
    {
        return line.startsWith("obstacle");
    }

    /**
     * Checks if line is declaring a Collectable
     * @param line The line from the text file to check
     * @return True or False
     */
    private static boolean isCollectable(String line)
    {
        return line.startsWith("collectable");
    }
}
