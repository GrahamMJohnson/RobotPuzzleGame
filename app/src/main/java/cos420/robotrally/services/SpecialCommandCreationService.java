package cos420.robotrally.services;

import android.util.Log;

import cos420.robotrally.commands.ICommand;
import cos420.robotrally.commands.Repeat;
import cos420.robotrally.enumerations.SpecialCommandType;
import cos420.robotrally.models.CommandList;


public class SpecialCommandCreationService {

    /**
     * Static method that returns a new instance of desired special command.
     * @param script The script object for the level
     * @param commandType The type of command to create
     * @param parameters The array of parameters to be used in the constructor
     * @return An instance of the created command
     */
    public static ICommand getInstanceOfSpecialCommand(CommandList script, SpecialCommandType commandType, int[] parameters)
    {
        switch (commandType)
        {
            case REPEAT: return new Repeat(script, parameters[0], parameters[1]);
            case OTHER: return null; //TODO: do something here
            default: Log.e("Special Command Creation", "Invalid Special Command Type");
        };
        return null;
    }

}
