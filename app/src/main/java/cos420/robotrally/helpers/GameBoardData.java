package cos420.robotrally.helpers;

import java.util.List;

import cos420.robotrally.models.Collectable;
import cos420.robotrally.models.Obstacle;

// TODO javadoc for the class itself
public class GameBoardData {
    /** size of the game board */
    public int size;
    /** starting column for the roomba */
    public int startRow;
    /** starting column for the roomba */
    public int startColumn;
    /** row of the goal tile */
    public int goalRow;
    /** column of the goal tile */
    public int goalColumn;
    /** What angle the goal should be rotated by */
    public int goalRotation;
    /** List of obstacles for the level */
    public List<Obstacle> obstacles;
    /** List of collectables for the level */
    public List<Collectable> collectables;

    // TODO javadoc (or remove)
    public GameBoardData() {
    }

    // TODO javadoc
    public GameBoardData(int size, int startRow, int startColumn, int goalRow, int goalColumn, int goalRotation, List<Obstacle> obstacles, List<Collectable> collectables) {
        this.size = size;
        this.startRow = startRow;
        this.startColumn = startColumn;
        this.goalRow = goalRow;
        this.goalColumn = goalColumn;
        this.goalRotation = goalRotation;
        this.obstacles = obstacles;
        this.collectables = collectables;
    }

    // TODO javadoc
    public GameBoardData(int size, int startRow, int startColumn, int goalRow, int goalColumn, int goalRotation) {
        this.size = size;
        this.startRow = startRow;
        this.startColumn = startColumn;
        this.goalRow = goalRow;
        this.goalColumn = goalColumn;
        this.goalRotation = goalRotation;
    }

    // TODO javadoc
    public int getSize() {
        return size;
    }
    // TODO javadoc
    public int getStartRow() {
        return startRow;
    }

    // TODO javadoc
    public int getStartColumn() {
        return startColumn;
    }

    // TODO javadoc
    public int getGoalRow() {
        return goalRow;
    }

    // TODO javadoc
    public int getGoalColumn() {
        return goalColumn;
    }

    /**
     * @return the angle the goal should be rotated by
     */
    public int getGoalRotation() {return goalRotation;}

    // TODO javadoc
    public List<Obstacle> getObstacles() {
        return obstacles;
    }

    // TODO javadoc
    public List<Collectable> getCollectables() {
        return collectables;
    }
}
