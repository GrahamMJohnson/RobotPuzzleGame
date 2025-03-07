package cos420.robotrally.levels;

import java.util.List;

import cos420.robotrally.models.Collectable;
import cos420.robotrally.models.Obstacle;
import cos420.robotrally.helpers.GameBoardData;
import cos420.robotrally.helpers.SpecialCommandData;

/**
 * Abstract class used as a template for creating level classes.
 */
public abstract class LevelDataTemplate {

    /** stores the data for the game board */
    protected GameBoardData gameBoardData;
    /** stores the data for special command A */
    protected SpecialCommandData specialCommandAData;
    /** stores the data for special command B */
    protected SpecialCommandData specialCommandBData;

    // Abstract methods
    /**
     * Method to create the game board data for the level
     * @return a GameBoardData object with all data for game board
     */
    protected abstract GameBoardData createGameBoardData();

    /**
     * Method to to create the data for special command A
     * @return a SpecialCommandData object with command A's info
     */
    protected abstract SpecialCommandData createSpecialCommandAData();

    /**
     * Method to to create the data for special command B
     * @return a SpecialCommandData object with command B's info
     */
    protected abstract SpecialCommandData createSpecialCommandBData();

    /**
     * Method to get a list of obstacles
     * @return A list of type Obstacle
     */
    protected abstract List<Obstacle> createObstacles();

    /**
     * Method to get a list of collectables
     * @return A list of type Collectable
     */
    protected abstract List<Collectable> createCollectables();

    // Getter methods
    /**
     * Gets the game board data
     * @return GameBoardData object with game board data
     */
    public GameBoardData getGameBoardData() {
        return gameBoardData;
    }

    /**
     * Gets the data for command A
     * @return SpecialCommandData object with info for command A
     */
    public SpecialCommandData getSpecialCommandAData() {
        return specialCommandAData;
    }

    /**
     * Gets the data for command B
     * @return SpecialCommandData object with info for command B
     */
    public SpecialCommandData getSpecialCommandBData() {
        return specialCommandBData;
    }
}