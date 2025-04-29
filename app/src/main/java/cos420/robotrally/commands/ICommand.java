package cos420.robotrally.commands;

import cos420.robotrally.enumerations.SubroutineType;
import cos420.robotrally.models.GameBoard;

/**
 * Interface for commands to interact with the game board
 */
public interface ICommand {

    /**
     * Executes the command.
     * @return true if the command was successful. False indicates the roomba crashed.
     */
    boolean execute();

    void setSubroutine(SubroutineType location);

    SubroutineType getSubroutine();

    public GameBoard getGameBoard();
}
