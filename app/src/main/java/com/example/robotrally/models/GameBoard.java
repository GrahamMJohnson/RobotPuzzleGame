package com.example.robotrally.models;

public class GameBoard{

    //this is the underlying 2d array
    private Tile[][] gameBoard;
    private int currentRow;
    private int currentColumn;
    private int size;

    /**
     * Made to initialize the gameBoard object.
     * @param givenSize is the number of tiles that you want in each row or column
     */
    public GameBoard(int givenSize) {
        size = givenSize;
        MakeBoard(size);
    }

    /**
     * This is the function to copy in a given level layout
     * @param levelLayout this is the pre-programmed level lay out
     */
    private void ImportBoard(Tile[][] levelLayout){
        gameBoard = levelLayout;
    }

    /**
     * This method makes a blank board with the given size.
     * @param givenSize is the number of tiles that you want in each row or column
     */
    private void MakeBoard(int givenSize){
        size = givenSize;

        //this loop will initialize each vertex of the array
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                assert gameBoard != null;
                gameBoard[x][y] = new Tile(false, false, false);
            }
        }
        return;
    }

    /**
     * This is the move up function written by Christian, adapted into the gameBoard class
     * @return a boolean value that represents whether the action completed or not
     * @throws Exception if the roomba is somehow being set to where it already is.
     */
    public boolean MoveUp() throws Exception {
        // test whether space above Roomba is blocked or out of bounds
        // if Roomba at top of grid, fail, or if space is blocked by obstacle, fail
        if (currentRow == 0 || !gameBoard[currentRow-1][currentColumn].CanMoveTo()) {
            return false;
        }
        // else move Roomba up
        else {
            //set the current tile to roomba is not there
            gameBoard[currentRow][currentColumn].SetIsRoombaHere(false);
            //change the current row that the roomba is in
            currentRow -= 1;
            //set the tile at that position to have the roomba
            gameBoard[currentRow][currentColumn].SetIsRoombaHere(true);
        }
        // return true if command was valid
        return true;
    }

    /**
     * This is the move left function written by Christian adapted into the gameBoard class
     * @return a boolean value that represents whether the action completed or not
     * @throws Exception if the roomba is somehow being set to where it already is
     */
    public boolean MoveLeft() throws Exception {
        // test whether space to the left of Roomba is blocked or out of bounds
        // if Roomba at left side of grid, fail, or if space is blocked by obstacle, fail
        if (currentColumn == 0 || !gameBoard[currentRow][currentColumn-1].CanMoveTo()) {
            return false;
        }
        // else move Roomba left
        else {
            //set the current tile to roomba is not there
            gameBoard[currentRow][currentColumn].SetIsRoombaHere(false);
            //change the current column that the roomba is in
            currentColumn -= 1;
            //set the tile at that position to have the roomba
            gameBoard[currentRow][currentColumn].SetIsRoombaHere(true);
        }
        // return true if command was valid
        return true;
    }

    /**
     * This is the move down function written by Christian adapted into the gameBoard class
     * @return a boolean value that represents whether the action completed or not
     * @throws Exception if the roomba is somehow being set to where it already is
     */
    boolean MoveDown() throws Exception {
        // test whether space below Roomba is blocked or out of bounds
        // if Roomba at bottom of grid, fail or if space is blocked by obstacle, fail
        if (currentRow == size-1 || !gameBoard[currentRow+1][currentColumn].CanMoveTo()) {
            return false;
        }
        // else move Roomba down
        else {
            //set the current tile to roomba is not there
            gameBoard[currentRow][currentColumn].SetIsRoombaHere(false);
            //change the current row that the roomba is in
            currentRow -= 1;
            //set the tile at that position to have the roomba
            gameBoard[currentRow][currentColumn].SetIsRoombaHere(true);
        }
        // return true if command was valid
        return true;
    }

    /**
     * This is the move right function written by Christian adapted into the gameBoard class
     * @return a boolean value that represents whether the action completed or not
     * @throws Exception if the roomba is somehow being set to where it already is
     */
    boolean MoveRight() throws Exception {
        // test whether space to the right of Roomba is blocked or out of bounds
        // if Roomba at right side of grid, fail or if space is blocked by obstacle, fail
        if (currentColumn == size-1 || !gameBoard[currentRow][currentColumn+1].CanMoveTo()) {
            return false;
        }
        // else move Roomba right
        else {
            //set the current tile to roomba is not there
            gameBoard[currentRow][currentColumn].SetIsRoombaHere(false);
            //change the current column that the roomba is in
            currentColumn += 1;
            //set the tile at that position to have the roomba
            gameBoard[currentRow][currentColumn].SetIsRoombaHere(true);
        }
        // return true if command was valid
        return true;
    }
}