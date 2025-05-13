package cos420.robotrally.models;

import android.util.Log;

import java.util.List;

import cos420.robotrally.MainActivity;
import cos420.robotrally.adaptersAndItems.MoveItem;
import cos420.robotrally.commands.*;
import cos420.robotrally.enumerations.EAfterExecuteCondition;
import cos420.robotrally.enumerations.ListName;
import cos420.robotrally.enumerations.SubroutineType;
import cos420.robotrally.levels.LevelData;

/**
 * Class that contains the lists of moves, and handles execution for a level
 */
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

    /** String used for the tag for log messages in this class */
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
        subroutineA = new CommandList(level.maxMovesA);
        subroutineB = new CommandList(level.maxMovesB);

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
     * Method to clear all commands out of all lists
     */
    public void resetLists()
    {
        commandScript.clear();
        subroutineA.clear();
        subroutineB.clear();
    }


    /**
     * Method to add an Up command to script
     * @param list The list to add command to
     * @return boolean if adding the command was successful
     */
    public boolean addUpCommand(ListName list)
    {
        switch(list) {
            case MAIN: return commandScript.addCommand(new Up(gameBoard));
            case A: return subroutineA.addCommand(new Up(gameBoard));
            case B: return subroutineB.addCommand(new Up(gameBoard));
        }
        return false;
    }

    /**
     * Method to add a Down command
     * @param list The list to add command to
     * @return boolean if adding the command was successful
     */
    public boolean addDownCommand(ListName list)
    {
        switch(list) {
            case MAIN: return commandScript.addCommand(new Down(gameBoard));
            case A: return subroutineA.addCommand(new Down(gameBoard));
            case B: return subroutineB.addCommand(new Down(gameBoard));
        }
        return false;
    }

    /**
     * Method to add a Left command
     * @param list The list to add command to
     * @return boolean if adding the command was successful
     */
    public boolean addLeftCommand(ListName list)
    {
        switch(list) {
            case MAIN: return commandScript.addCommand(new Left(gameBoard));
            case A: return subroutineA.addCommand(new Left(gameBoard));
            case B: return subroutineB.addCommand(new Left(gameBoard));
        }
        return false;
    }

    /**
     * Method to add a Right command
     * @param list The list to add command to
     * @return boolean if adding the command was successful
     */
    public boolean addRightCommand(ListName list)
    {
        switch(list) {
            case MAIN: return commandScript.addCommand(new Right(gameBoard));
            case A: return subroutineA.addCommand(new Right(gameBoard));
            case B: return subroutineB.addCommand(new Right(gameBoard));
        }
        return false;
    }

    /**
     * Method to add an A command to script
     * @param list The list to add command to
     * @return Whether or not the subroutine was able to be added
     */
    public boolean addSubroutineA(ListName list)
    {
        switch(list) {
            case MAIN: return commandScript.addCommand(new A(gameBoard));
            case A: // Can't add to itself
            case B: // A can only be added to main to prevent loops
        }
        return false;
    }

    /**
     * Method to add B command to script
     * @param list The list to add command to
     * @return Whether or not the subroutine was able to be added
     */
    public boolean addSubroutineB(ListName list)
    {
        switch(list) {
            case MAIN: return commandScript.addCommand(new B(gameBoard));
            case A: return subroutineA.addCommand(new B(gameBoard));
            case B: // Can't add to itself
        }
        return false;
    }

    /**
     * Method to remove command from end of script
     * @param list The list to remove command from
     * @throws Exception if the script is empty
     */
    public void remove(ListName list) throws Exception {
        switch(list) {
            case MAIN: commandScript.remove(); break;
            case A: subroutineA.remove(); break;
            case B: subroutineB.remove(); break;
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
     * Getter for the gameBoard object
     * @return the game board
     */
    public GameBoard getGameBoard() {
        return gameBoard;
    }

    /**
     * Method to get the end status after execution is complete
     * @return the end status of execution for handling in MainActivity
     */
    public EAfterExecuteCondition getEndStatus() {
        if (commandScript.didWeCrash()) {
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

        expandSubroutines();

        attempts++;
        new Thread(() -> {
            for (int i = 0; i < moveList.size(); i++) {
                int finalI = i;
                int collectiblesCollected;

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
                activity.runOnUiThread(callback::onStepMove);

                try {
                    Thread.sleep(450);
                } catch (InterruptedException e) {
                    Log.d(LOG_TAG, e.getMessage() != null ? e.getMessage() : "Error sleeping");
                }

                if (commandScript.didWeCrash()) {
                    break;
                }
            }


            EAfterExecuteCondition result = getEndStatus();
            activity.runOnUiThread(() -> {
                callback.onExecutionEnd(result);
                collapseSubroutines();
            });
        }).start();
    }

    /**
     * Gets the number of collectibles collected from the game board
     * Through this, you can get the number in main
     * @return int for number of collectibles that the user collected
     */
    public int getCollectiblesCollected(){
        return gameBoard.collectiblesCollected;
    }

    /**
     * Resets the number of collectibles collected for each run.
     */
    public void resetCollectiblesCollected(){
        gameBoard.collectiblesCollected = 0;
    }

    /**
     * Method to get the max moves for Subroutines A and B
     * @return int array -> index 0 has max moves for A, index 1 has max moves for B
     */
    public int[] getSubroutineMaxMoves() {
        return new int[]{levelData.maxMovesA, levelData.maxMovesB};
    }

    /**
     * Method to expand A/B command into full subroutine on execution
     */
    private void expandSubroutines() {
        // Variables to be used later
        int removeIndex;

        // Loop through each command in script searching for subroutine placeholders
        for (int i = 0; i < commandScript.size(); i++) {
            // Get the command out of list
            ICommand c = commandScript.get(i);

            // Set variables if command is a subroutine placeholder
            CommandList subroutineToAdd;
            SubroutineType subroutineType;
            if (c instanceof A) {
                subroutineToAdd = subroutineA;
                subroutineType = SubroutineType.A;
            }
            else if (c instanceof B) {
                subroutineToAdd = subroutineB;
                subroutineType = c.getSubroutine();
            }
            else {
                // If command was not an A/B placeholder, we can continue to next command in list
                continue;
            }

            // Select the correct command
            if (commandScript.getSelect() != i)
                commandScript.setSelect(i);

            // Store index of placeholder command to be removed
            removeIndex = i;

            // Add the commands from subroutine into to main script
            commandScript.addSubroutine(subroutineToAdd, subroutineType);
            // Store the current select, so we don't have to check the newly added commands

            // Remove the placeholder command
            if (removeIndex != commandScript.getSelect())
                commandScript.setSelect(removeIndex);
            try {
                commandScript.remove();
            } catch (Exception ignored) {
                // We should never get here as remove is only called if it found an A command
            }

            // Subtract 1 from i so we check the same index again, as this could now be a B command if we just expanded an A
            i--;
        }
    }

    /**
     * Method to collapse subroutine into A/B command when execution is complete
     */
    private void collapseSubroutines() {
        // Loop through list looking for commands that were added as part of subroutine
        for (int i = 0; i < commandScript.size(); i++) {
            // Get command from list
            ICommand c = commandScript.get(i);
            ICommand subroutineCommand;
            int commandsInExpandedSubroutine;

            SubroutineType subType = c.getSubroutine();

            // If command is not part of subroutine, continue on to next command
            if (subType == null) {
                continue;
            }

            // Set variables depending on which subroutine command is a part of
            switch (c.getSubroutine()) {
                case AB:
                    // an AB means the command is part of subroutine B, but is nested in A, so we want to collapse it as if it is A
                case A:
                    subroutineCommand = new A(gameBoard);
                    commandsInExpandedSubroutine = subroutineA.size() + subroutineA.containsSubroutineB() * (subroutineB.size() - 1);
                    break;
                case B:
                    subroutineCommand = new B(gameBoard);
                    commandsInExpandedSubroutine = subroutineB.size();
                    break;
                default: continue;
            }

            // Calculate the number of Commands in expanded Subroutine

            // Get index to insert placeholder command into list after subroutine commands, then select and add
            int insertIndex = i + commandsInExpandedSubroutine - 1;
            if (insertIndex != commandScript.getSelect())
                commandScript.setSelect(insertIndex);
            commandScript.addCommand(subroutineCommand);

            // Remove commands from the script based off of how many commands are in subroutine
            for (int j = 0; j < commandsInExpandedSubroutine; j++) {

                // Set select to be the first command of subroutine
                if (i != commandScript.getSelect())
                    commandScript.setSelect(i);
                try {
                    commandScript.remove();
                } catch (Exception ignored) {
                    // We should never get here as remove is only called if it found an A command
                }
            }
        }
    }

    public boolean getWeCrashed() {
        return commandScript.didWeCrash();
    }
}
