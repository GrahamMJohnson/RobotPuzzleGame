package cos420.robotrally.commands;

import cos420.robotrally.models.GameBoard;

// TODO javadoc for the class itself
public class Down implements ICommand {

    /** reference to the game board of the level this command is initialized from */
    private final GameBoard gameBoard;

    /**
     * Constructor
     * @param gameBoard the game board tied to the level creating this command
     */
    public Down(GameBoard gameBoard)
    {
        this.gameBoard = gameBoard;
    }

    @Override
    public boolean execute()
    {
        try {
            return gameBoard.moveDown();
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            return false;
        }
    }
}
