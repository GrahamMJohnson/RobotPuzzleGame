package cos420.robotrally.services;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import cos420.robotrally.enumerations.ObstacleType;
import cos420.robotrally.enumerations.SpecialCommandType;
import cos420.robotrally.helpers.GameBoardData;
import cos420.robotrally.helpers.SpecialCommandData;
import cos420.robotrally.levels.LevelData;
import cos420.robotrally.models.Collectable;
import cos420.robotrally.models.Obstacle;

public class LevelMapper {
    
    private static final String LOG_TAG = "Level Mapper";

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
                        String[] rowArray = reader.readLine().split(" ");
                        String[] columnArray = reader.readLine().split(" ");

                        // Get value for type row and column
                        ObstacleType type = ObstacleType.getTypeFromString(typeArray[typeArray.length - 1]);
                        int row = Integer.parseInt(rowArray[rowArray.length - 1]);
                        int column = Integer.parseInt(columnArray[columnArray.length - 1]);

                        // Create obstacle object and add it to list of obstacles
                        level.gameBoardData.obstacles.add(new Obstacle(type, row, column));

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
                // Checking if we are reading in a special command
                else if (isSpecialCommand(line))
                {
                    // Using try catch because integer parsing could fail
                    try {
                        Log.v(LOG_TAG, "Start mapping special command");

                        // Split lines into arrays
                        String[] typeArray = reader.readLine().trim().split(" ");
                        String[] paramsArray = reader.readLine().trim().split(":")[1].trim().split(" ");

                        // Get value from last index of array
                        SpecialCommandType type = SpecialCommandType.getTypeFromString(typeArray[typeArray.length - 1]);
                        int[] params = new int[paramsArray.length];
                        for (int i = 0; i < paramsArray.length; i++)
                        {
                            params[i] = Integer.parseInt(paramsArray[i]);
                        }

                        // Create SpecialCommandData object and add it to the level
                        SpecialCommandData specialCommand = new SpecialCommandData(type, params);
                        // first special command is command A
                        // second special command is command B
                        if (level.commandAData == null)
                        {
                            level.commandAData = specialCommand;
                        }
                        else
                        {
                            level.commandBData = specialCommand;
                        }

                        Log.v(LOG_TAG, "End mapping special command");
                    } catch (IOException e) {
                        Log.e(LOG_TAG, e.getMessage() != null ? e.getMessage() : "Error parsing special command data");
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

                        // Getting the wanted values
                        int size = Integer.parseInt(sizeArray[sizeArray.length - 1]);
                        int startRow = Integer.parseInt(startRowArray[startRowArray.length - 1]);
                        int startColumn = Integer.parseInt(startColumnArray[startColumnArray.length - 1]);
                        int goalRow = Integer.parseInt(goalRowArray[goalRowArray.length - 1]);
                        int goalColumn = Integer.parseInt(goalColumnArray[goalColumnArray.length - 1]);

                        // Add values to gameBoardData object of level
                        level.gameBoardData.size = size;
                        level.gameBoardData.startRow = startRow;
                        level.gameBoardData.startColumn = startColumn;
                        level.gameBoardData.goalRow = goalRow;
                        level.gameBoardData.goalColumn = goalColumn;

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
     * @param line the line of file to check
     * @return true -> line is a comment<br>
     *         false -> line is data
     */
    private static boolean isCommentOrEmpty(String line)
    {
        return line.startsWith("//") || line.isEmpty();
    }

    private static boolean isNewLevel(String line)
    {
        return line.startsWith("-----------------");
    }

    private static boolean isObstacle(String line)
    {
        return line.startsWith("obstacle");
    }

    private static boolean isCollectable(String line)
    {
        return line.startsWith("collectable");
    }

    private static boolean isSpecialCommand(String line)
    {
        return line.startsWith("special");
    }
}
