package cos420.robotrally.models;

import cos420.robotrally.helpers.GameBoardData;

public class GameBoard{

    //this is the underlying 2d array
    private Tile[][] gameBoard;
    private int currentRow;
    private int currentColumn;
    private final int size;

    /**
     * Constructor
     * @param gameBoardData GameBoardData object with all of the data for the game board
     */
    public GameBoard(GameBoardData gameBoardData)
    {
        this.size = gameBoardData.size;
        this.makeBoard(gameBoardData);
    }

    /**
     * This is the function to copy in a given level layout
     * @param levelLayout this is the pre-programmed level lay out
     */
    private void importBoard(Tile[][] levelLayout){
        gameBoard = levelLayout;
    }

    /**
     * Makes and sets up the 2d array for the game board
     * @param gameBoardData GameBoardData object with all of the data for the game board
     */
    private void makeBoard(GameBoardData gameBoardData){
        gameBoard = new Tile[size][size];

        //this loop will initialize each vertex of the array
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                gameBoard[x][y] = new Tile();
            }
        }

        // Set Starting and Goal tiles
        gameBoard[gameBoardData.startRow][gameBoardData.startColumn].setOccupied(true);
        gameBoard[gameBoardData.goalRow][gameBoardData.goalColumn].setGoalTile(true);

        // Set Obstacles
        for (Obstacle o : gameBoardData.obstacles)
        {
            gameBoard[o.getRow()][o.getColumn()].setObstacleType(o.getObstacleType());
        }

        // Set Collectables
        for (Collectable c : gameBoardData.collectables)
        {
            gameBoard[c.getRow()][c.getColumn()].setHasCollectable(true);
        }
    }

    /**
     * This is the move up function written by Christian, adapted into the gameBoard class
     * @return a boolean value that represents whether the action completed or not
     * @throws Exception if the roomba is somehow being set to where it already is.
     */
    public boolean moveUp() throws Exception {
        // test whether space above Roomba is blocked or out of bounds
        // if Roomba at top of grid, fail, or if space is blocked by obstacle, fail
        if (currentRow == 0 || gameBoard[currentRow-1][currentColumn].isObstacle()) {
            return false;
        }
        // else move Roomba up
        else {
            //set the current tile to roomba is not there
            gameBoard[currentRow][currentColumn].setOccupied(false);
            //change the current row that the roomba is in
            currentRow -= 1;
            //set the tile at that position to have the roomba
            gameBoard[currentRow][currentColumn].setOccupied(true);
        }
        // return true if command was valid
        return true;
    }

    /**
     * This is the move left function written by Christian adapted into the gameBoard class
     * @return a boolean value that represents whether the action completed or not
     * @throws Exception if the roomba is somehow being set to where it already is
     */
    public boolean moveLeft() throws Exception {
        // test whether space to the left of Roomba is blocked or out of bounds
        // if Roomba at left side of grid, fail, or if space is blocked by obstacle, fail
        if (currentColumn == 0 || gameBoard[currentRow][currentColumn-1].isObstacle()) {
            return false;
        }
        // else move Roomba left
        else {
            //set the current tile to roomba is not there
            gameBoard[currentRow][currentColumn].setOccupied(false);
            //change the current column that the roomba is in
            currentColumn -= 1;
            //set the tile at that position to have the roomba
            gameBoard[currentRow][currentColumn].setOccupied(true);
        }
        // return true if command was valid
        return true;
    }

    /**
     * This is the move down function written by Christian adapted into the gameBoard class
     * @return a boolean value that represents whether the action completed or not
     * @throws Exception if the roomba is somehow being set to where it already is
     */
    public boolean moveDown() throws Exception {
        // test whether space below Roomba is blocked or out of bounds
        // if Roomba at bottom of grid, fail or if space is blocked by obstacle, fail
        if (currentRow == size-1 || gameBoard[currentRow+1][currentColumn].isObstacle()) {
            return false;
        }
        // else move Roomba down
        else {
            //set the current tile to roomba is not there
            gameBoard[currentRow][currentColumn].setOccupied(false);
            //change the current row that the roomba is in
            currentRow -= 1;
            //set the tile at that position to have the roomba
            gameBoard[currentRow][currentColumn].setOccupied(true);
        }
        // return true if command was valid
        return true;
    }

    /**
     * This is the move right function written by Christian adapted into the gameBoard class
     * @return a boolean value that represents whether the action completed or not
     * @throws Exception if the roomba is somehow being set to where it already is
     */
    public boolean moveRight() throws Exception {
        // test whether space to the right of Roomba is blocked or out of bounds
        // if Roomba at right side of grid, fail or if space is blocked by obstacle, fail
        if (currentColumn == size-1 || gameBoard[currentRow][currentColumn+1].isObstacle()) {
            return false;
        }
        // else move Roomba right
        else {
            //set the current tile to roomba is not there
            gameBoard[currentRow][currentColumn].setOccupied(false);
            //change the current column that the roomba is in
            currentColumn += 1;
            //set the tile at that position to have the roomba
            gameBoard[currentRow][currentColumn].setOccupied(true);
        }
        // return true if command was valid
        return true;
    }

    public boolean destReached() {
        return gameBoard[currentRow][currentColumn].isGoalTile();
    }
}