package cos420.robotrally.helpers;

import cos420.robotrally.enumerations.SpecialCommandType;

// TODO javadoc for the class itself
public class SpecialCommandData {
    /** Special command command */
    public SpecialCommandType commandType;
    /** Parameters for special constructor. Do not have a commandScript as that gets passed in by default in the LevelController */
    public int[] commandParams;

    // TODO javadoc
    public SpecialCommandData(SpecialCommandType commandType, int[] commandParams) {
        this.commandType = commandType;
        this.commandParams = commandParams;
    }
}
