package cos420.robotrally.commands;

/**
 * Interface for commands to interact with the game board
 */
public interface ICommand {

    /**
     * Executes the command.
     * @return true if the command was successful. False indicates the roomba crashed.
     */
    boolean execute();
}
