package cos420.robotrally.models;

import cos420.robotrally.enumerations.ObstacleType;

public class Tile {
    /** Boolean to store if this is a goal tile */
    private boolean isGoalTile;

    /** Boolean to track if there is an obstacle on this tile */
    private boolean isObstacle;

    /** Boolean to track if the roomba currently occupies this tile */
    private boolean isOccupied;

    /** Boolean to track if tile has collectable */
    private boolean hasCollectable;

    /**
     * An enum to store what is on the tile
     */
    private ObstacleType obstacleType;

    //TODO: Can we get buy with constructor that has no parameters since GameBoard updates tiles after creating array?
    /**
     * Constructor for Tile class
     */
    public Tile() {
        this(null, false, false, false);
    }

    /**
     * Constructor for a tile that the roomba currently occupies
     * @param obstacleType The type of obstacle on this tile (null if not an obstacle
     * @param isGoalTile Is this tile a goal state?
     * @param hasCollectable Does this tile have a collectable?
     * @param isOccupied Is the roomba on this tile?
     */
    public Tile(ObstacleType obstacleType, boolean isGoalTile, boolean hasCollectable, boolean isOccupied) {
        isObstacle = obstacleType != null;
        this.obstacleType = obstacleType;
        this.isGoalTile = isGoalTile;
        this.hasCollectable = hasCollectable;
        this.isOccupied = isOccupied;
    }

    // Getter and Setters

    /**
     * Check if tile is a goal state
     * @return true -> is goal state <br>
     *         false -> is not goal state
     */
    public boolean isGoalTile() {
        return isGoalTile;
    }

    /**
     * Set isGoalTile to new Value
     * @param isGoalTile boolean -> new value
     */
    public void setGoalTile(boolean isGoalTile) {
        this.isGoalTile = isGoalTile;
    }

    /**
     * Check if tile is an obstacle
     * @return true -> has obstacle <br>
     *         false -> does not have obstacle
     */
    public boolean isObstacle() {
        return isObstacle;
    }

    /**
     * Getter method for isOccupied
     * @return true -> roomba is on tile <br>
     *         false -> roomba is not on tile
     */
    public boolean isOccupied() {
        return isOccupied;
    }

    /**
     * Set whether or not the roomba is on this tile
     * @param isOccupied the new value to store in isOccupied
     */
    public void setOccupied(boolean isOccupied) {
        this.isOccupied = isOccupied;
    }

    /**
     * Method to check if tile has collectable
     * @return true -> has collectable <br>
     *         false -> no collectable
     */
    public boolean hasCollectable() {
        return hasCollectable;
    }

    /**
     * Method to collect any collectable on tile
     */
    public void setHasCollectable(boolean hasCollectable) {
        this.hasCollectable = hasCollectable;
    }

    /**
     * Method to get the type of obstacle on a tile
     * @return The type of obstacle on tile
     */
    public ObstacleType getObstacleType() {
        return obstacleType;
    }

    /**
     * Method to set the obstacle type. This also updates isObstacle attribute
     * @param obstacleType ObstacleType enum to set the obstacle type for the tile<br>
     *                     set to null if tile is not an obstacle
     */
    public void setObstacleType(ObstacleType obstacleType) {
        this.obstacleType = obstacleType;
        isObstacle = obstacleType != null;
    }

}