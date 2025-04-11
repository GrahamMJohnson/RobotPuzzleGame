package cos420.robotrally.models;

import android.util.Log;

import java.util.List;

import cos420.robotrally.MainActivity;
import cos420.robotrally.adaptersAndItems.MoveItem;
import cos420.robotrally.commands.*;
import cos420.robotrally.enumerations.EAfterExecuteCondition;
import cos420.robotrally.levels.LevelData;
import cos420.robotrally.services.SpecialCommandService;

// TODO javadoc for the class itself
public class LevelController {
    /** attribute for the game board for this level */
    private final GameBoard gameBoard;

    /** attribute for the user input of commands */
    private final CommandList commandScript;

    /** attribute that is a reference to the stored level data */
    private final LevelData levelData;


    /** attribute to store number of attempts on level */
    private int attempts;

    // TODO javadoc
    private static final String LOG_TAG = "Level Controller";


    /**
     * Constructor that takes info for Level from Level class to create the Level Controller
     * @param level the object that stores the data for the desired level
     */
    public LevelController(LevelData level)
    {
        levelData = level;
        gameBoard = new GameBoard(level.gameBoardData);
        commandScript = new CommandList();
        attempts = 0;
    }

    /**
     * Method that resets the level's Roomba position and clears the command linked list
     */
    public void resetLevel()
    {
        gameBoard.resetGrid(levelData.gameBoardData);
        commandScript.clearList();
    }

    /**
     * Method to retry the level
     */
    public void retryLevel()
    {
        gameBoard.resetGrid(levelData.gameBoardData);
    }

    /**
     * Method to add an Up command to script
     */
    public void addUpCommand()
    {
        commandScript.addCommand(new Up(gameBoard));
        Log.v(LOG_TAG, "Added up command");
    }

    /**
     * Method to add a Down command to script
     */
    public void addDownCommand()
    {
        commandScript.addCommand(new Down(gameBoard));
        Log.v(LOG_TAG, "Added down command");
    }

    /**
     * Method to add a Left command to script
     */
    public void addLeftCommand()
    {
        commandScript.addCommand(new Left(gameBoard));
        Log.v(LOG_TAG, "Added left command");
    }

    /**
     * Method to add a Right command to script
     */
    public void addRightCommand()
    {
        commandScript.addCommand(new Right(gameBoard));
        Log.v(LOG_TAG, "Added right command");
    }

    /**
     * Method to add an A command to script
     */
    public void addACommand() {
        try {
            ICommand command = SpecialCommandService.getInstanceOfSpecialCommand(levelData.commandAType, gameBoard);
            commandScript.addCommand(command);
            Log.v(LOG_TAG, "Added special command A");
        }
        catch (RuntimeException e) {
            Log.v("Adding Special Command", e.getMessage() != null ? e.getMessage() : "Error creating special command");
        }
    }

    /**
     * Method to add an B command to script
     */
    public void addBCommand() {
        try {
            ICommand command = SpecialCommandService.getInstanceOfSpecialCommand(levelData.commandBType, gameBoard);
            commandScript.addCommand(command);
            Log.v(LOG_TAG, "Added special command B");
        }
        catch (RuntimeException e) {
            Log.v("Adding Special Command", e.getMessage() != null ? e.getMessage() : "Error creating special command");
        }
    }

    /**
     * Method to remove command from end of script
     * @throws Exception is the script is empty
     */
    public void remove() throws Exception {
        commandScript.remove();
        Log.v(LOG_TAG, "Deleted command");
    }

    /**
     * Passing up what command is selected(to get to UI)
     * @return selected command
     */
    public int getSelected() {
        return commandScript.getSelect();
    }

    /**
     * Setter for what command is selected
     */
    public void setSelected(int s) {
        commandScript.setSelect(s);
    }

    /**
     * Getter
     * @return the game board
     */
    public GameBoard getGameBoard() {
        return gameBoard;
    }

    /**
     * @return the end status of execution for handling in MainActivity
     */
    public EAfterExecuteCondition getEndStatus() {
        if (!commandScript.getDidWeDriveSafe()) {
            // Roomba crashed
            return EAfterExecuteCondition.CRASHED;
        }
        if (gameBoard.destReached()) {
            // Roomba arrived at destination successfully
            return EAfterExecuteCondition.DEST_REACHED;
        }
        // Roomba neither crashed nor arrived at destination
        return EAfterExecuteCondition.GOT_LOST;
    }

    /**
     * This runs execution step by step, STOPPING IF WE CRASH, running the highlight function at the same time
     *
     * After execution, it determines the end status then calls something in the callback to do end of execution checking
     *
     * @param moveList reference to the moveList in MainActivity
     * @param callback reference to the callback interface in MainActivity
     * @param activity reference to MainActivity so we can manage the UI thread
     */
    public void executeScript(List<MoveItem> moveList, ExecutionCallback callback, MainActivity activity) {
        attempts++;
        new Thread(() -> {
            for (int i = 0; i < moveList.size(); i++) {
                int finalI = i;

                // Highlight current command
                activity.runOnUiThread(() -> callback.onStepHighlight(finalI));

                commandScript.executeCommand(finalI);

                //Move grid
                activity.runOnUiThread(() -> callback.onStepMove(finalI));

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (!commandScript.getDidWeDriveSafe()) {
                    break;
                }
            }


            EAfterExecuteCondition result = getEndStatus();
            activity.runOnUiThread(() -> callback.onExecutionEnd(result));
        }).start();
    }

}
