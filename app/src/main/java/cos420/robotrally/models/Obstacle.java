package cos420.robotrally.models;

import cos420.robotrally.enumerations.ObstacleType;

// TODO javadoc for the class itself
public class Obstacle {
    /** Type of obstacle */
    private final ObstacleType obstacleType;

    /** Row index of the obstacle */
    private final int row;

    /** column index of the obstacle */
    private final int column;

    /**
     * Create a new Obstacle
     * @param obstacleType the type of obstacle to create
     * @param row the row the obstacle is in
     * @param column the column the obstacle is in
     */
    public Obstacle(ObstacleType obstacleType, int row, int column) {
        this.obstacleType = obstacleType;
        this.row = row;
        this.column = column;
    }

    /**
     * get the obstacle type
     * @return ObstacleType -> the type of the obstacle
     */
    public ObstacleType getObstacleType() {
        return obstacleType;
    }

    /**
     * get the row of the obstacle
     * @return int -> the row of the obstacle
     */
    public int getRow() {
        return row;
    }

    /**
     * get the column of the obstacle
     * @return int -> the column of the obstacle
     */
    public int getColumn() {
        return column;
    }
}
