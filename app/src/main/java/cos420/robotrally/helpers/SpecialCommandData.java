package cos420.robotrally.helpers;

import cos420.robotrally.enumerations.SpecialCommandType;

public class SpecialCommandData {
    /** The type of command B */
    protected SpecialCommandType commandType;
    /** Parameters for command A's constructor. Do not have a commandScript as that gets passed in by default in the LevelController */
    protected Object[] commandParams;

    /**
     * Constructor
     * @param commandType the type of the command
     * @param commandParams The parameters for the commands constructor
     */
    public SpecialCommandData(SpecialCommandType commandType, Object[] commandParams) {
        this.commandType = commandType;
        this.commandParams = commandParams;
    }

    /**
     * Gets the type of the command
     * @return Command Type Enum of command
     */
    public SpecialCommandType getCommandType() {
        return commandType;
    }

    /**
     * Gets the parameters for the constructor of command
     * @return Array of parameters for command's constructor
     */
    public Object[] getCommandParams() {
        return commandParams;
    }
}
