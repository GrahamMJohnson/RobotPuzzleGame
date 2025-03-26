package cos420.robotrally.helpers;

import java.util.List;

import cos420.robotrally.models.Collectable;
import cos420.robotrally.models.Obstacle;

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
    /** List of obstacles for the level */
    public List<Obstacle> obstacles;
    /** List of collectables for the level */
    public List<Collectable> collectables;

    public GameBoardData() {
    }

    public GameBoardData(int size, int startRow, int startColumn, int goalRow, int goalColumn, List<Obstacle> obstacles, List<Collectable> collectables) {
        this.size = size;
        this.startRow = startRow;
        this.startColumn = startColumn;
        this.goalRow = goalRow;
        this.goalColumn = goalColumn;
        this.obstacles = obstacles;
        this.collectables = collectables;
    }

    public GameBoardData(int size, int startRow, int startColumn, int goalRow, int goalColumn) {
        this.size = size;
        this.startRow = startRow;
        this.startColumn = startColumn;
        this.goalRow = goalRow;
        this.goalColumn = goalColumn;
    }
}
