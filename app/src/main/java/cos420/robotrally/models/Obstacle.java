package cos420.robotrally.models;

// TODO javadoc for the class itself
public class Obstacle {
    /** Type/image name of obstacle */
    private final String obstacleType;

    /** What angle the obstacle image should be rotated at */
    private final int rotation;

    /** Row index of the obstacle */
    private final int row;

    /** column index of the obstacle */
    private final int column;

    /**
     * Create a new Obstacle
     * @param obstacleType the type of obstacle to create
     * @param rotation  for the image to be rotated at
     * @param row the row the obstacle is in
     * @param column the column the obstacle is in
     */
    public Obstacle(String obstacleType, int rotation, int row, int column) {
        this.obstacleType = obstacleType;
        this.rotation = rotation;
        this.row = row;
        this.column = column;
    }

    /**
     * get the obstacle type
     * @return ObstacleType -> the type of the obstacle
     */
    public String getObstacleType() {
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

    /**
     * @return the degrees the obstacle should be rotated by
     */
    public int getRotation() {return rotation;}
}
