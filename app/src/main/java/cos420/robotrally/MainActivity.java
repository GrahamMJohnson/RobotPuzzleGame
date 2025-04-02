package cos420.robotrally;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import cos420.robotrally.adaptersAndItems.GridAdapter;
import cos420.robotrally.adaptersAndItems.GridItem;
import cos420.robotrally.adaptersAndItems.LevelAdapter;
import cos420.robotrally.adaptersAndItems.LevelItem;
import cos420.robotrally.adaptersAndItems.MoveAdapter;
import cos420.robotrally.adaptersAndItems.MoveItem;
import cos420.robotrally.levels.LevelData;
import cos420.robotrally.models.Collectable;
import cos420.robotrally.models.LevelController;
import cos420.robotrally.models.Obstacle;
import cos420.robotrally.services.LevelMapper;

public class MainActivity extends AppCompatActivity implements LevelAdapter.LevelSelectListener {
    /** A list of levels, for use by the adapter */
    ArrayList<LevelItem> levelDisplayList;
    /** Adapter to allow for dynamic list elements in level select */
    LevelAdapter levelAdapter;

    List<MoveItem> moveList;
    MoveAdapter moveAdapter;
    LevelController levelController;

    GridView gridTile;
    ArrayList<GridItem> gridList;
    GridAdapter gridAdapter;

    /**
     * 0 indexed list of the data for level layout<br>
     * Level 1 can be accessed by levels.get(0)
     */
    List<LevelData> levels;

    private int selectedLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // list of data for all the levels
        levels = LevelMapper.mapLevelDataFromFile(this);
        openLevelSelect();
    }

    /// --------------
    /// LEVEL SELECT
    /// --------------

    /**
     * Generates and displays the level selection menu to the user.
     */
    private void openLevelSelect() {
        setContentView(R.layout.level_select_dynamic);
        levelDisplayList = new ArrayList<>();
        // for each level
        for (int i = 0; i < levels.size(); i++) {
            // TODO Calc percentage
            double percentage = .5;
            // Create LevelItem
            levelDisplayList.add(new LevelItem(i, percentage));
        }
        levelAdapter = new LevelAdapter(this, levelDisplayList, this);
        GridView levelLayout = findViewById(R.id.dynamicLevelLayout);
        levelLayout.setAdapter(levelAdapter);
    }

    @Override
    public void onLevelSelectClick(int levelID) {
        if (levelID < 0 || levelID >= levels.size()) {
            throw new IndexOutOfBoundsException("Level " + levelID + " does not exist.");
        }
        openSelectedLevel(levelID);
    }

    /// --------------
    /// PLAYABLE UI
    /// --------------
    /// SETUP

    /**
     * Changes the game screen to the main activity and sets up the level
     * @param levelID   to load the level for
     */
    private void openSelectedLevel(int levelID) {
        setContentView(R.layout.activity_main);

        // Moves view set up
        RecyclerView recyclerView = findViewById(R.id.move_viewer);
        moveList = new ArrayList<>();

        // Set LayoutManager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Set Adapter
        moveAdapter = new MoveAdapter(moveList);
        recyclerView.setAdapter(moveAdapter);

        //GridTile  view set up
        gridTile = findViewById(R.id.grid);
        gridList = new ArrayList<>();

        setupGrid(levels.get(levelID));
        //Set up text at top of screen
        TextView tv = findViewById(R.id.level_text);
        tv.setText(String.valueOf(levelID + 1));

        //Set adapter
        gridAdapter = new GridAdapter(this, gridList);
        gridTile.setAdapter(gridAdapter);

        levelController = new LevelController(levels.get(levelID));
        setupGUIButtons(levelController);
    }

    /**
     * Adds new moves to the back-end, and adds them to the UI if no errors occur.
     * Also allows for deleting moves from the back-end list & GUI and beginning run execution.
     *
     * @param levelC    The level controller responsible for the current stage.
     */
    private void setupGUIButtons(LevelController levelC) {
        // UP
        findViewById(R.id.up_button).setOnClickListener(v -> {
            levelC.addUpCommand();
            addMoveToUI("UP");
        });

        // DOWN
        findViewById(R.id.down_button).setOnClickListener(v -> {
            levelC.addDownCommand();
            addMoveToUI("DOWN");
        });

        // LEFT
        findViewById(R.id.left_button).setOnClickListener(v -> {
            levelC.addLeftCommand();
            addMoveToUI("LEFT");
        });
        // RIGHT
        findViewById(R.id.right_button).setOnClickListener(v -> {
            levelC.addRightCommand();
            addMoveToUI("RIGHT");
        });
        // A
        findViewById(R.id.button_a).setOnClickListener(v -> {
            levelC.addACommand();
            addMoveToUI("A");
        });
        // B
        findViewById(R.id.button_b).setOnClickListener(v -> {
            levelC.addBCommand();
            addMoveToUI("B");
        });
        // DELETE
        findViewById(R.id.delete_button).setOnClickListener(v -> {
            try {
                levelC.remove();
                removeMoveFromUI();
            } catch (Exception e) {
                Log.d("Button Pressed", "Attempted to remove item from empty list");
            }
        });
        //Reset
        findViewById(R.id.reset_button).setOnClickListener(v -> {
            //TODO method to reset grid
            //TODO method to reset moves
        });
        // START
        findViewById(R.id.start_button).setOnClickListener(v -> {
            boolean victory = levelC.executeScript();
            if (victory) { // reached destination
                showWinScreen();
            } else {
                // TODO crash screen
            }
        });
        // BACK
        findViewById(R.id.back_button).setOnClickListener(v -> {
            clearGameListeners();
            openLevelSelect();
        });
    }

    /**
     * Method that displays the win pop up to the player
     */
    private void showWinScreen()
    {
        LayoutInflater inflater = getLayoutInflater();
        View customView = inflater.inflate(R.layout.win_dialog, null);

        AlertDialog winScreen = new AlertDialog.Builder(this)
                .setView(customView)
                .setCancelable(false)
                .create();

        // setup button actions
        customView.findViewById(R.id.last_level_button).setOnClickListener(v -> {
            Log.v("Win Dialogue", "Previous level button pressed");
            //TODO go to previous level method
        });

        customView.findViewById(R.id.RetryButton).setOnClickListener(v -> {
            Log.v("Win Dialogue", "Retry level button pressed");
            //TODO call method to reset the level
            winScreen.dismiss();
        });

        customView.findViewById(R.id.next_level_button).setOnClickListener(v -> {
            Log.v("Win Dialogue", "Next level button pressed");
            //TODO go to next level method
        });

        winScreen.show();
    }

    /**
     * Sets up the game board grid
     * @param l the current level
     */
    private void setupGrid(LevelData l) {
        int size = l.gameBoardData.getSize(); //size of game board
        ColorDrawable gray = new ColorDrawable(Color.parseColor("#D3D3D3"));
        ColorDrawable black = new ColorDrawable(Color.parseColor("#000000"));

        for (int i = 0; i < size; i++) { // Row
            for (int j = 0; j < size; j++) { //Column
                if (i == l.gameBoardData.getStartRow() && j == l.gameBoardData.getStartColumn()) { //Check if start tile
                    gridList.add(new GridItem("R", gray));
                }else if (i == l.gameBoardData.getGoalRow() && j == l.gameBoardData.getGoalColumn()) { //Check if Goal tile
                    gridList.add(new GridItem("D", gray));
                }else if(isObstacle(l.gameBoardData.getObstacles(), i, j)) {
                    gridList.add(new GridItem("", black));
                }else if(isCollectable(l.gameBoardData.getCollectables(), i, j)) {
                    gridList.add(new GridItem("C", gray));
                } else {// Blank Tile
                    gridList.add(new GridItem("", gray));
                }
            }
        }
    }

    /**
     * checks if the tile is a Obstacle
     * @param list  of obstacles
     * @param row   of the tile
     * @param col   of the tile
     * @return      true if obstacle, false otherwise
     */
    private boolean isObstacle(List<Obstacle> list, int row, int col) {
        for (int i = 0; i < list.size(); i++) {
            int r = list.get(i).getRow();
            int c = list.get(i).getColumn();
            if(r == row && c == col) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the tile is a Collectable
     * @param list  of collectables
     * @param row   of the tile
     * @param col   of the tile
     * @return      true if a collectable, false otherwise
     */
    private boolean isCollectable(List<Collectable> list, int row, int col) {
        for (int i = 0; i < list.size(); i++) {
            int r = list.get(i).getRow();
            int c = list.get(i).getColumn();
            if(r == row && c == col) {
                return true;
            }
        }
        return false;
    }

    /// IN-GAME CHANGES

    /**
     * Adds a new move to the list shown to the user.
     *
     * @param moveText The designator of the move; must be "UP", "DOWN", LEFT", "RIGHT", "A", or "B"
     */
    private void addMoveToUI (String moveText) {
        // Using a switch w/ fallthrough rather than if-statement in order to make future work
        // easier. Each type of move will need to access a different image.
        switch (moveText) {
            case "UP":
            case "DOWN":
            case "LEFT":
            case "RIGHT":
            case "A":
            case "B":
                break;
            default: throw new InvalidParameterException(moveText + " is not a valid move.");
        }

        moveList.add(new MoveItem(moveText));//add item
        moveAdapter.notifyDataSetChanged();//notify adapter of change
    }

    /**
     * Remove the last move item from the list
     */
    private void removeMoveFromUI () {
        moveList.remove(moveList.size() - 1); //Remove Last Item
        moveAdapter.notifyDataSetChanged(); //notify adapter of change
    }

    /**
     * Show the collision screen
     */
    private void showCollisionScreen() {
        //TODO: Figure out where to place this into the main activity to get it to execute

        //this is creating the view to be referenced
        LayoutInflater collisionInflator = getLayoutInflater();
        View collisionView = collisionInflator.inflate(R.layout.robot_hit_dialog, null);

        //this is putting the alert onto the screen with the created view
        AlertDialog collisionScreen = new AlertDialog.Builder(this)
                .setView(collisionView)
                .setCancelable(false)
                .create();

        //this is the button listener to close the dialog
        collisionView.findViewById(R.id.crash_dialog_retry).setOnClickListener(v -> {
            collisionScreen.dismiss();
        });

        collisionScreen.show();
    }

    /// CLEANUP

    /**
     * Clears all listeners from buttons in the playable interface.
     */
    private void clearGameListeners() {
        findViewById(R.id.up_button).setOnClickListener(null);
        findViewById(R.id.down_button).setOnClickListener(null);
        findViewById(R.id.left_button).setOnClickListener(null);
        findViewById(R.id.right_button).setOnClickListener(null);
        findViewById(R.id.button_a).setOnClickListener(null);
        findViewById(R.id.button_b).setOnClickListener(null);
        findViewById(R.id.delete_button).setOnClickListener(null);
        findViewById(R.id.start_button).setOnClickListener(null);
        findViewById(R.id.back_button).setOnClickListener(null);
    }

    /// END PLAYABLE UI METHODS
}