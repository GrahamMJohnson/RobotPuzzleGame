package cos420.robotrally.services;

import android.util.Log;

import cos420.robotrally.commands.ICommand;
import cos420.robotrally.commands.special.*;
import cos420.robotrally.enumerations.SpecialCommandType;
import cos420.robotrally.models.GameBoard;

/**
 * Map from enum to ICommand Type
 */
public class SpecialCommandService {

    /**
     * Static method that returns a new instance of desired special command.
     * @param commandType The type of command to create
     * @param gameBoard The instance of gameBoard this command will execute on.
     * @return An instance of the created command
     */
    public static ICommand getInstanceOfSpecialCommand(SpecialCommandType commandType, GameBoard gameBoard)
    {
        switch (commandType)
        {
            case D3: return new Down3(gameBoard);
            case R3: return new Right3(gameBoard);
            case L2: return new Left2(gameBoard);
            default: Log.e("Special Command Creation", "Invalid Special Command Type");
        }
        throw new RuntimeException("SpecialCommandType: " + commandType + " is not mapped to anything");
    }

}
