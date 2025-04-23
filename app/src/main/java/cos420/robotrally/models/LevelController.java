package cos420.robotrally.models;

import android.util.Log;

import java.util.List;

import cos420.robotrally.MainActivity;
import cos420.robotrally.adaptersAndItems.MoveItem;
import cos420.robotrally.commands.*;
import cos420.robotrally.enumerations.EAfterExecuteCondition;
import cos420.robotrally.enumerations.ListName;
import cos420.robotrally.levels.LevelData;

// TODO javadoc for the class itself
public class LevelController {
    /** attribute for the game board for this level */
    private final GameBoard gameBoard;

    /** attribute for the user input of commands */
    private final CommandList commandScript;

    /** attribute to store the a sub-routine */
    private final CommandList subroutineA;

    /** attribute to store the a sub-routine */
    private final CommandList subroutineB;

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
        subroutineA = new CommandList();
        subroutineB = new CommandList();

        attempts = 0;
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
     * @param list The list to add command to
     */
    public void addUpCommand(ListName list)
    {
        switch(list) {
            case MAIN: commandScript.addCommand(new Up(gameBoard)); break;
            case A: subroutineA.addCommand(new Up(gameBoard)); break;
            case B: subroutineB.addCommand(new Up(gameBoard)); break;
        }
    }

    /**
     * Method to add a Down command
     * @param list The list to add command to
     */
    public void addDownCommand(ListName list)
    {
        switch(list) {
            case MAIN: commandScript.addCommand(new Down(gameBoard)); break;
            case A: subroutineA.addCommand(new Down(gameBoard)); break;
            case B: subroutineB.addCommand(new Down(gameBoard)); break;
        }
    }

    /**
     * Method to add a Left command
     * @param list The list to add command to
     */
    public void addLeftCommand(ListName list)
    {
        switch(list) {
            case MAIN: commandScript.addCommand(new Left(gameBoard)); break;
            case A: subroutineA.addCommand(new Left(gameBoard)); break;
            case B: subroutineB.addCommand(new Left(gameBoard)); break;
        }
    }

    /**
     * Method to add a Right command
     * @param list The list to add command to
     */
    public void addRightCommand(ListName list)
    {
        switch(list) {
            case MAIN: commandScript.addCommand(new Right(gameBoard)); break;
            case A: subroutineA.addCommand(new Right(gameBoard)); break;
            case B: subroutineB.addCommand(new Right(gameBoard)); break;
        }
    }

    /**
     * Method to add an A command to script
     * @param list The list to add command to
     * @return Whether or not the subroutine was able to be added
     */
    public boolean addSubroutineA(ListName list)
    {
        switch(list) {
            case MAIN: commandScript.addSubroutine(subroutineA); return true;
            case A: ; // Can't add to itself
            case B: ; // A can only be added to main to prevent loops
        }
        return false;
    }

    /**
     * Method to add an B command to script
     * @param list The list to add command to
     * @return Whether or not the subroutine was able to be added
     */
    public boolean addSubroutineB(ListName list)
    {
        switch(list) {
            case MAIN: commandScript.addSubroutine(subroutineB); return true;
            case A: subroutineA.addSubroutine(subroutineB); return true;
            case B: ; // Can't add to itself
        }
        return false;
    }

    /**
     * Method to remove command from end of script
     * @param list The list to remove command from
     * @return int value of how many commands were deleted
     * @throws Exception is the script is empty
     */
    public int remove(ListName list) throws Exception {
        switch(list) {
            case MAIN: return commandScript.remove();
            case A: return subroutineA.remove();
            case B: return subroutineB.remove();
            default: return 0;
        }
    }

    /**
     * Passing up what command is selected(to get to UI)
     * @param list The list to get selected from
     * @return selected command
     */
    public int getSelected(ListName list) {
        switch(list) {
            case MAIN: return commandScript.getSelect();
            case A: return subroutineA.getSelect();
            case B: return subroutineB.getSelect();
            default: return 0;
        }
    }

    /**
     * Setter for what command is selected
     * @param s The index of selected command
     * @param list The list to set selected for
     */
    public void setSelected(int s, ListName list) {
        switch(list) {
            case MAIN: commandScript.setSelect(s); break;
            case A: subroutineA.setSelect(s); break;
            case B: subroutineB.setSelect(s); break;
        }
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
     * This runs execution step by step, STOPPING IF WE CRASH, running the highlight function at the same time<br><br>
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

                // have the highlight switch before the grid
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    Log.d(LOG_TAG, e.getMessage() != null ? e.getMessage() : "Error sleeping");
                }

                //Move grid
                activity.runOnUiThread(() -> callback.onStepMove(finalI));

                try {
                    Thread.sleep(450);
                } catch (InterruptedException e) {
                    Log.d(LOG_TAG, e.getMessage() != null ? e.getMessage() : "Error sleeping");
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
