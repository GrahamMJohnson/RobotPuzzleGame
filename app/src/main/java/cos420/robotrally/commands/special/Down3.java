package cos420.robotrally.commands.special;

import cos420.robotrally.commands.ICommand;
import cos420.robotrally.models.GameBoard;

/**
 * Special command to move the Roomba down 3 times
 */
public class Down3 implements ICommand {

    /** reference to the game board of the level this command is initialized from */
    private final GameBoard gameBoard;

    /**
     * Constructor
     * @param gameBoard the game board tied to the level creating this command
     */
    public Down3(GameBoard gameBoard)
    {
        this.gameBoard = gameBoard;
    }

    /**
     * Execute this command
     * @return True or False if execution was successful
     */
    @Override
    public boolean execute() {
        // Call moveDown method 3 times
        for (int i = 0; i < 3; i++)
        {
            boolean success = gameBoard.moveDown();
            if(!success) {
                return false;
            }
        }
        // All commands successfully executed
        return true;
    }
}
