package cos420.robotrally.commands;

import cos420.robotrally.enumerations.LocationInSubroutine;
import cos420.robotrally.models.GameBoard;

/**
 * Command to move left
 */
public class Left implements ICommand {

    /** reference to the game board of the level this command is initialized from */
    private final GameBoard gameBoard;

    /** Stores where a command is in a subroutine */
    private LocationInSubroutine locationInSubroutine;

    /**
     * Constructor
     * @param gameBoard the game board tied to the level creating this command
     */
    public Left(GameBoard gameBoard)
    {
        this.gameBoard = gameBoard;
        locationInSubroutine = null;
    }

    /**
     * execute the command
     * @return boolean value for if execution was successful or not
     */
    @Override
    public boolean execute()
    {
        return gameBoard.moveLeft();
    }

    /**
     * Method to set if command is in subroutine
     * @param location The location in subroutine
     */
    @Override
    public void setLocationInSubroutine(LocationInSubroutine location) {
        locationInSubroutine = location;
    }

    /**
     * Method to get commands location in subroutine
     * @return LocationInSubroutine representing where a command is in subroutine
     *<br> - it can return null which means command isn't in a subroutine
     */
    @Override
    public LocationInSubroutine getLocationInSubroutine() {
        return locationInSubroutine;
    }

    /**
     * Get the gameBoard method for making copy of this command
     * @return the gameBoard element tied to this command
     */
    @Override
    public GameBoard getGameBoard() {
        return gameBoard;
    }
}
