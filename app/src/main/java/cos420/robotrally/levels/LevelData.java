package cos420.robotrally.levels;

import cos420.robotrally.enumerations.SpecialCommandType;
import cos420.robotrally.helpers.GameBoardData;
import cos420.robotrally.helpers.SpecialCommandData;

/**
 * The class that stores all data for the backend of a level
 */
public class LevelData {
    /** Data for the game board */
    public GameBoardData gameBoardData;

    /** Type for special command A*/
    public SpecialCommandType commandAType;

    /** Type for special command B*/
    public SpecialCommandType commandBType;
}
