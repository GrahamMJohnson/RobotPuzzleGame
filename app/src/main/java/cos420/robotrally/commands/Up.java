package cos420.robotrally.commands;

import cos420.robotrally.models.GameBoard;

public class Up implements ICommand {

    /** reference to the game board of the level this command is initialized from */
    private final GameBoard gameBoard;

    /**
     * Constructor
     * @param gameBoard the game board tied to the level creating this command
     */
    public Up(GameBoard gameBoard)
    {
        this.gameBoard = gameBoard;
    }

    /**
     * Method to execute the command
     */
    @Override
    public void execute()
    {
        try {
            gameBoard.moveUp();
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
}
