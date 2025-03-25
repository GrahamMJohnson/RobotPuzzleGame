package cos420.robotrally.models;

import cos420.robotrally.commands.*;
import cos420.robotrally.levels.LevelDataTemplate;
import cos420.robotrally.services.SpecialCommandCreationService;

public class LevelController {
    /** attribute for the game board for this level */
    private final GameBoard gameBoard;

    /** attribute for the user input of commands */
    private final CommandList commandScript;

    /** attribute that is a reference to the stored level data */
    private final LevelDataTemplate levelData;

    /** attribute to store number of attempts on level */
    private int attempts;

    /**
     * Constructor that takes info for Level from Level class to create the Level Controller
     * @param level the object that stores the data for the desired level
     */
    public LevelController(LevelDataTemplate level)
    {
        levelData = level;
        gameBoard = new GameBoard(level.getGameBoardData());
        commandScript = new CommandList();
        attempts = 0;
    }

    /**
     * Method to add an Up command to script
     */
    public void addUpCommand() { commandScript.addCommand(new Up(gameBoard)); }

    /**
     * Method to add a Down command to script
     */
    public void addDownCommand()
    {
        commandScript.addCommand(new Down(gameBoard));
    }

    /**
     * Method to add a Left command to script
     */
    public void addLeftCommand()
    {
        commandScript.addCommand(new Left(gameBoard));
    }

    /**
     * Method to add a Right command to script
     */
    public void addRightCommand()
    {
        commandScript.addCommand(new Right(gameBoard));
    }

    /**
     * Method to add an A command to script
     */
    public void addACommand()
    {
        ICommand command = SpecialCommandCreationService.getInstanceOfSpecialCommand(
                commandScript,
                levelData.getSpecialCommandAData().getCommandType(),
                levelData.getSpecialCommandAData().getCommandParams());
        if (command != null) {
            commandScript.addCommand(command);
        }
    }

    /**
     * Method to add an A command to script
     */
    public void addBCommand()
    {
        ICommand command = SpecialCommandCreationService.getInstanceOfSpecialCommand(
                commandScript,
                levelData.getSpecialCommandBData().getCommandType(),
                levelData.getSpecialCommandBData().getCommandParams());
        if (command != null)
        {
            commandScript.addCommand(command);
        }
    }

    /**
     * Method to remove last command from script
     * @return the removed command
     */
    public void remove() { commandScript.removeLastCommand();}

    /**
     * method to execute the script
     */
    public void executeScript()
    {
        attempts++;
        commandScript.execute();
    }

    /**
     * Switches a command from position "from" to position "to"
     */
    public void switchMove(int from, int to) { commandScript.switchMove(from, to);}
}
