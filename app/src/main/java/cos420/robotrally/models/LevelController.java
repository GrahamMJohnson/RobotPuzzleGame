package cos420.robotrally.models;

import android.util.Log;

import cos420.robotrally.commands.*;
import cos420.robotrally.enumerations.EAfterExecuteCondition;
import cos420.robotrally.levels.LevelData;
import cos420.robotrally.services.SpecialCommandCreationService;

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
        this.gameBoard.resetGrid(this.levelData.gameBoardData);
        commandScript.clearList();
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
    public void addACommand()
    {
        ICommand command = SpecialCommandCreationService.getInstanceOfSpecialCommand(
                commandScript,
                levelData.commandAData.commandType,
                levelData.commandAData.commandParams);
        if (command != null)
        {
            commandScript.addCommand(command);
        }
        Log.v(LOG_TAG, "Added special command A");
    }

    /**
     * Method to add an B command to script
     */
    public void addBCommand()
    {
        ICommand command = SpecialCommandCreationService.getInstanceOfSpecialCommand(
                commandScript,
                levelData.commandBData.commandType,
                levelData.commandBData.commandParams);
        if (command != null)
        {
            commandScript.addCommand(command);
        }
        Log.v(LOG_TAG, "Added special command B");
    }

    /**
     * Method to remove command from end of script
     * @throws Exception is the script is empty
     */
    public void remove() throws Exception {
        commandScript.removeLastCommand();
        Log.v(LOG_TAG, "Deleted command");
    }

    /**
     * method to execute the script
     * @return The end condition of the roomba, in enum form.
     */
    public EAfterExecuteCondition executeScript()
    {
        attempts++;
        Log.v(LOG_TAG, "Executing script");
        boolean didWeDriveSafe = commandScript.execute();
        if (!didWeDriveSafe) {
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
     * Switches a command from position "from" to position "to"
     */
    public void switchMove(int from, int to) { commandScript.switchMove(from, to);}
}
