package cos420.robotrally.models;

import cos420.robotrally.enumerations.TileType;

public class Tile {

    /** Boolean to track if there is an obstacle on this tile */
    private final boolean isObstacle;

    /** Boolean to store if this is a goal tile */
    private final boolean isGoalTile;

    /**
     * An enum to store what is on the tile
     * (Thinking this could be used for the UI to load the right image?)
     */
    private final TileType tileType;


    /** Boolean to track if tile has collectable */
    private boolean hasCollectable;

    /** Boolean to track if the roomba currently occupies this tile */
    private boolean isOccupied;

    /**
     * Constructor for Tile class
     * @param tileType The type of obstacle on this tile (Can be NONE)
     * @param isGoalTile Is this tile a goal state?
     * @param hasCollectable Does this tile have a collectable?
     */
    public Tile(TileType tileType, boolean isGoalTile, boolean hasCollectable) {
        this(tileType, isGoalTile, hasCollectable, false);
    }

    /**
     * Constructor for a tile that the roomba currently occupies
     * @param tileType The type of obstacle on this tile (Can be NONE)
     * @param isGoalTile Is this tile a goal state?
     * @param hasCollectable Does this tile have a collectable?
     * @param isOccupied Is the roomba on this tile?
     */
    public Tile(TileType tileType, boolean isGoalTile, boolean hasCollectable, boolean isOccupied) {
        isObstacle = tileType != TileType.EMPTY;
        this.tileType = tileType;
        this.isGoalTile = isGoalTile;
        this.hasCollectable = hasCollectable;
        this.isOccupied = isOccupied;
    }

    /**
     * Check if tile can be occupied by the roomba
     * @return true -> roomba can move here <br>
     *         false -> roomba can't move here
     */
    public boolean isObstacle() {
        return !isObstacle;
    }

    /**
     * Check if tile is a goal state
     * @return true -> is goal state <br>
     *         false -> is not goal state
     */
    public boolean isGoalTile() {
        return isGoalTile;
    }

    /**
     * Set whether or not the roomba is on this tile
     * @param newValue the new value to store in isOccupied
     * @throws Exception Throws exception if trying to set value to what it already is or if tile is obstacle
     */
    public void setOccupied(boolean newValue) throws Exception {
        if (newValue == isOccupied)
        {
            throw new Exception("Cannot set to what value already is");
        }
        else if (isObstacle)
        {
            throw new Exception("Cannot set isOccupied because this tile is an obstacle");
        }

        isOccupied = newValue;
    }

    /**
     * Getter method for isOccupied
     * @return true -> roomba is on tile <br>
     *         false -> roomba is not on tile
     */
    public boolean isRoombaHere() {
        return isOccupied;
    }

    /**
     * Method to collect any collectable on tile
     * @throws Exception Throws Exception if method is called on tile that does not have a collectable
     */
    public void collect() throws Exception {
        if (!hasCollectable)
        {
            throw new Exception("There is no item to collect");
        }
        hasCollectable = false;
    }

    /**
     * Method to get the type of obstacle on a tile <br>
     * (Thinking this could be used to tell the UI what to display for the tile?)
     * @return The type of obstacle on tile
     */
    public TileType getTileType() {
        return tileType;
    }

}