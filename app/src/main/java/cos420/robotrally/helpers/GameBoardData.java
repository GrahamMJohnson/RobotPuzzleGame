package cos420.robotrally.helpers;

import java.util.List;

import cos420.robotrally.models.Collectable;
import cos420.robotrally.models.Obstacle;

public class GameBoardData {
    /** size of the game board */
    protected int size;
    /** starting column for the roomba */
    protected int startRow;
    /** starting column for the roomba */
    protected int startColumn;
    /** row of the goal tile */
    protected int goalRow;
    /** column of the goal tile */
    protected int goalColumn;
    /** List of obstacles for the level */
    protected List<Obstacle> obstacles;
    /** List of collectables for the level */
    protected List<Collectable> collectables;

    /**
     * Constructor
     * @param size The size of game board
     * @param startRow The starting row
     * @param startColumn The starting column
     * @param goalRow The goal row
     * @param goalColumn The goal column
     * @param obstacles List of obstacles
     * @param collectables List of collectables
     */
    public GameBoardData(int size, int startRow, int startColumn, int goalRow, int goalColumn,
                            List<Obstacle> obstacles, List<Collectable> collectables) {
        this.size = size;
        this.startRow = startRow;
        this.startColumn = startColumn;
        this.goalRow = goalRow;
        this.goalColumn = goalColumn;
        this.obstacles = obstacles;
        this.collectables = collectables;
    }

    // Getter methods
    /**
     * Getter method for the size attribute
     * @return the size of the level
     */
    public int getSize() {
        return size;
    }

    /**
     * Gets the starting row
     * @return the starting row of the level
     */
    public int getStartRow() {
        return startRow;
    }

    /**
     * Gets the starting column
     * @return the starting column of the level
     */
    public int getStartColumn() {
        return startColumn;
    }

    /**
     * Gets the goal row
     * @return the goal row of the level
     */
    public int getGoalRow() {
        return goalRow;
    }

    /**
     * Gets the goal column
     * @return the goal column of the level
     */
    public int getGoalColumn() {
        return goalColumn;
    }

    /**
     * Gets the obstacles in the level
     * @return List of obstacles
     */
    public List<Obstacle> getObstacles() {
        return obstacles;
    }

    /**
     * Gets the collectables in the level
     * @return List of collectables
     */
    public List<Collectable> getCollectables() {
        return collectables;
    }
}
