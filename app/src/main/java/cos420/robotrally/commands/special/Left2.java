package cos420.robotrally.commands.special;

import cos420.robotrally.commands.ICommand;
import cos420.robotrally.models.GameBoard;

/**
 * Special Command to move left twice
 */
public class Left2 implements ICommand {

    /** reference to the game board of the level this command is initialized from */
    private final GameBoard gameBoard;

    /**
     * Constructor
     * @param gameBoard the game board tied to the level creating this command
     */
    public Left2(GameBoard gameBoard)
    {
        this.gameBoard = gameBoard;
    }

    @Override
    public boolean execute() {
        // call moveDown method 3 times
        for (int i = 0; i < 2; i++)
        {
            boolean success = gameBoard.moveLeft();
            if(!success) {
                return false;
            }
        }
        // All commands successfully executed
        return true;
    }
}
