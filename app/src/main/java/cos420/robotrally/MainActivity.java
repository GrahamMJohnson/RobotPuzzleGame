package cos420.robotrally;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cos420.robotrally.adaptersAndItems.GridAdapter;
import cos420.robotrally.adaptersAndItems.GridItem;
import cos420.robotrally.adaptersAndItems.LevelAdapter;
import cos420.robotrally.adaptersAndItems.LevelItem;
import cos420.robotrally.adaptersAndItems.MoveAdapter;
import cos420.robotrally.adaptersAndItems.MoveItem;
import cos420.robotrally.enumerations.EAfterExecuteCondition;
import cos420.robotrally.levels.LevelData;
import cos420.robotrally.models.Collectable;
import cos420.robotrally.models.GameBoard;
import cos420.robotrally.models.LevelController;
import cos420.robotrally.models.Obstacle;
import cos420.robotrally.services.LevelMapper;
import cos420.robotrally.models.RobotRallySave;
import cos420.robotrally.models.ExecutionCallback;

// TODO javadoc for the class itself
public class MainActivity extends AppCompatActivity implements LevelAdapter.LevelSelectListener, MoveAdapter.MoveListener, ExecutionCallback{

    // TODO javadoc
    List<MoveItem> moveList;
    // TODO javadoc
    MoveAdapter moveAdapter;
    int curSelectUI;
    GridView gridView;
    GridAdapter gridAdapter;
    ArrayList<GridItem> gridList;

    /**Independent tracking of where we are in execution, in the UI*/
    int executingMoveUI;
    ObjectAnimator animator;
    private final List<View> ghostList = new ArrayList<>();
    RecyclerView recyclerView;

    //TODO javadoc
    RobotRallySave saveFunction;
    LevelController levelController;

    /**
     * 0 indexed list of the data for level layout<br>
     * Level 1 can be accessed by levels.get(0)
     */
    List<LevelData> levels;

    // TODO javadoc
    private int selectedLevelID;

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
        // A list of levels, for use by the adapter
        ArrayList<LevelItem> levelDisplayList;
        // Adapter to allow for dynamic list elements in level select
        LevelAdapter levelAdapter;

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
        selectedLevelID = levelID;
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
        recyclerView = findViewById(R.id.move_viewer);
        moveList = new ArrayList<>();

        // Set LayoutManager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Set Adapter
        moveAdapter = new MoveAdapter(moveList, (MoveAdapter.MoveListener) this);
        recyclerView.setAdapter(moveAdapter);

        //GridTile  view set up
        gridView = findViewById(R.id.grid);
        gridList = new ArrayList<>();

        setupGrid(levels.get(levelID));
        //Set up text at top of screen
        TextView tv = findViewById(R.id.level_text);
        tv.setText(String.valueOf(levelID + 1));

        //Set adapter
        gridAdapter = new GridAdapter(this, gridList);
        gridView.setAdapter(gridAdapter);

        levelController = new LevelController(levels.get(levelID));
        setupGUIButtons(levelController);

        //this starts an instance of a save class based on the level number
        saveFunction = new RobotRallySave(this, levelID);
    }


    /**
     * defining ExecutionCallback Interface functions
     * @param index index of execution
     */
    @Override
    public void onStepHighlight(int index) {
        highlightExecutionStep(index);
    }

    /**
     * Move tiles in the grid UI
     * @param index
     */
    @Override
    public void onStepMove(int index) {
        GameBoard g = levelController.getGameBoard();

        int previousIndex = (g.getPreviousRow() * g.getSize()) + g.getPreviousColumn();

        int currentIndex = (g.getCurrentRow() * g.getSize()) + g.getCurrentColumn();
        GridItem current = gridList.get(currentIndex);

        if(current.getImage() == R.drawable.coin_image || current.getImage() == R.drawable.flag) {
            current.setImage(R.drawable.empty);
        }
        animateTileMove(previousIndex, currentIndex);
    }

    /**
     * Animate the tile moving
     * @param from current position in list
     * @param to future position in list
     */
    public void animateTileMove(int from, int to) {
        GridItem pre = gridList.get(from);
        //Tiles
        View fromView = gridView.getChildAt(from);
        View toView = gridView.getChildAt(to);

        //Coordinate holders
        int[] fromPlace = new int[2];
        int[] toPlace = new int[2];

        //Get coordinates of tiles on the screen
        fromView.getLocationOnScreen(fromPlace);
        toView.getLocationOnScreen(toPlace);

        float x = toPlace[0] - fromPlace[0];
        float y = toPlace[1] - fromPlace[1];

        //Create a temporary view for the animation
        ImageView moving = new ImageView(this);
        moving.setImageDrawable(((ImageView) fromView.findViewById(R.id.tile_view)).getDrawable());

        //Layout for temporary view
        FrameLayout root = findViewById(android.R.id.content);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(fromView.getWidth(), fromView.getHeight());
        params.leftMargin = fromPlace[0];
        params.topMargin = fromPlace[1];
        root.addView(moving, params);

        //Set previous
        gridList.set(from, gridList.get(to));
        gridAdapter.notifyDataSetChanged();

        //Animator for movement
        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
        animator.setDuration(300);
        final long[] lastGhostTime = {0}; //Time when last ghost was created
        final long ghostInterval = 20; //How long until another ghost is created

        animator.addUpdateListener(animation -> {
            float progress = (float) animation.getAnimatedValue();
            float curX = fromPlace[0] + x * progress;
            float curY = fromPlace[1] + y * progress;

            moving.setTranslationX(curX - fromPlace[0]);
            moving.setTranslationY(curY - fromPlace[1]);

            long curTime = System.currentTimeMillis(); //Actual time
            if (curTime - lastGhostTime[0] > ghostInterval) { //Is it time to create another ghost?
                addGhostTrailAt(curX, curY, fromView.getWidth(), fromView.getHeight());
                lastGhostTime[0] = curTime;
            }
        });

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                //Set current
                gridList.set(to, pre);
                gridAdapter.notifyDataSetChanged();
                //Remove temporary view for movement
                root.removeView(moving);
            }
        });

        animator.start();
    }

    /**
     * Adds a new ghost to the trail
     * @param x the current x coordinate
     * @param y the current y coordinate
     * @param tileW width of current tile
     * @param tileH width of current tile
     */
    private void addGhostTrailAt(float x, float y, int tileW, int tileH) {
        //Create view for ghost with parameters
        View ghost = new View(this);
        int ghostSize = (int) (tileW / 1.5);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ghostSize, ghostSize);
        params.leftMargin = (int) (x + (tileW / 2) - (ghostSize / 2));
        params.topMargin = (int) (y + (tileH / 2) - (ghostSize / 2));

        //Create drawable, which is a white circle
        GradientDrawable draw = new GradientDrawable();
        draw.setShape(GradientDrawable.OVAL);
        draw.setColor(Color.parseColor("#44FFFFFF"));

        //Set ghost
        ghost.setLayoutParams(params);
        ghost.setBackground(draw);
        ghost.setAlpha(0f);//to fade in later
        ghostList.add(ghost);

        //Add ghost to root
        FrameLayout root = findViewById(android.R.id.content);
        root.addView(ghost);

        //Animate ghost to fade in over 150 milliseconds
        ghost.animate()
                .alpha(1f)
                .setDuration(150)
                .start();
    }

    /**
     * Clears the ghost trail
     */
    public void clearGhost() {
        FrameLayout root = findViewById(android.R.id.content);

        for (View ghost : ghostList) {
            root.removeView(ghost);
        }

        ghostList.clear();
    }

    /**
     * After execution callback function for getting the right menu up at the end.
     */
    @Override
    public void onExecutionEnd(EAfterExecuteCondition result) {
        handleExecutionEnd(result);
    }

    /**
     * last piece of execution run, used to show the correct screen based on execution result
     *
     * @param result end result determined by the execution script in level controller
     */
    private void handleExecutionEnd(EAfterExecuteCondition result){
        //resetting color or last highlited move.
        MoveItem m = moveList.get(executingMoveUI);
        m.setButtonColor(new ColorDrawable(Color.parseColor("#D3D3D3")));
        moveAdapter.notifyItemChanged(executingMoveUI);

            if (result == EAfterExecuteCondition.DEST_REACHED) {
                showWinScreen();
            } else if (result == EAfterExecuteCondition.CRASHED) {
                showCollisionScreen();
            } else if (result == EAfterExecuteCondition.GOT_LOST) {
                showLostScreen();
            } else {
                Log.d("End Condition Error", "Someone added a new end condition and " +
                                "forgot to add it to the end-screen handler.");
            }
    }
    /**
     * Highilghts the currently executing move, graying the previous one anc yellow-ing the current one
     *
     * @param index of execution in list
     */
    public void highlightExecutionStep(int index) {
        if (index < 0 || index >= moveList.size()) return;

        // Reset previous highlight
        if (executingMoveUI >= 0 && executingMoveUI < moveList.size() && executingMoveUI != index) {
            MoveItem prevMove = moveList.get(executingMoveUI);
            ColorDrawable gray = new ColorDrawable(Color.parseColor("#D3D3D3"));
            prevMove.setButtonColor(gray);
            moveAdapter.notifyItemChanged(executingMoveUI);
        }

        MoveItem currentMove = moveList.get(index);
        ColorDrawable yellow = new ColorDrawable(Color.parseColor("#FFF176")); // or any highlight color
        currentMove.setButtonColor(yellow);

        // Keep the executing command > 1 spot above bottom of view until its the last command
        // This was you can see what is coming next
        int scrollIndex = index + 1;
        if (index == 0) {
            scrollIndex = index; // Scroll to top for first command
        }

        recyclerView.scrollToPosition(scrollIndex);

        moveAdapter.notifyItemChanged(index);

        executingMoveUI = index;
    }

    /**
     * Resets the grid of the UI game board
     */
    public void resetGrid() {
        gridList.clear();
        setupGrid(levels.get(selectedLevelID));
        gridAdapter.notifyDataSetChanged();
    }

    @Override
    public void onMoveClick(int position) {
        //set which command is selected
        levelController.setSelected(position);
        blinkUI();
    }

    public void blinkUI() {
        //update UI of selected command
        int select = levelController.getSelected();

        //Remove blink from previous command
        if (animator != null && animator.isRunning()) {
            animator.cancel();
            //remove background from previous selected
            if (curSelectUI >= 0 && curSelectUI < moveList.size() && curSelectUI != select) {
                MoveItem m = moveList.get(curSelectUI);
                ColorDrawable gray = new ColorDrawable(Color.parseColor("#D3D3D3"));
                m.setColor(gray);
                moveList.set(curSelectUI, m);
                moveAdapter.notifyItemChanged(curSelectUI);
            }
        }

        //Set UI to blink on selected command
        RecyclerView.ViewHolder v = recyclerView.findViewHolderForAdapterPosition(select);
        if (v != null) {
            View blinkV = v.itemView.findViewById(R.id.blink);
            animator = ObjectAnimator.ofInt(blinkV,
                    "backgroundColor",
                    Color.rgb(211, 211, 211),
                    Color.BLACK, Color.rgb(211, 211, 211));
            animator.setDuration(1000);
            animator.setEvaluator(new ArgbEvaluator());
            animator.setRepeatMode(ValueAnimator.REVERSE);
            animator.setRepeatCount(Animation.INFINITE);
            animator.start();

            //Set current selected command = select
            curSelectUI = select;
        }
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
                int before = levelC.getSelected(); //The command that was deleted
                levelC.remove();
                removeMoveFromUI(before);
            } catch (Exception e) {
                Log.d("Button Pressed", "Attempted to remove item from empty list");
            }
        });

        // RESET
        findViewById(R.id.reset_button).setOnClickListener(v -> {
            levelController.resetLevel();
            int moves = moveList.size();
            moveList.clear();
            moveAdapter.notifyItemRangeRemoved(0, moves);
            resetGrid();
            clearGhost();
        });

        // START
        findViewById(R.id.start_button).setOnClickListener(v -> {
            setButtonsClickable(false);
            animator.end();
            recyclerView.smoothScrollToPosition(0);
//            levelC.setSelected(0);
//            recyclerView.scrollToPosition(0);
//            recyclerView.post(this::blinkUI);//Delays adding animation until after view holder is set
            levelC.executeScript(moveList, this, this);
        });

        // BACK
        findViewById(R.id.back_button).setOnClickListener(v -> {

            //This is currently holding placeholder values.
            //When we begin storing level data, we can replace the values here.
            saveFunction.SetMoveSequence("test");
            saveFunction.SetNumAttempts(10);
            saveFunction.SetEfficiencyScore(100);
            saveFunction.SetTotalSquaresTraveled(15);
            saveFunction.SetCurrentMoveDifference(12);
            saveFunction.SetBestMoveDifference(6);
            saveFunction.SetCollectiblesCollected(100);
            saveFunction.saveLevelData();
            //showSaveDebug();

            clearGameListeners();
            openLevelSelect();
        });
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
                    gridList.add(new GridItem(R.drawable.circle_image)); //Placeholder for roomba
                }else if (i == l.gameBoardData.getGoalRow() && j == l.gameBoardData.getGoalColumn()) { //Check if Goal tile
                    gridList.add(new GridItem(R.drawable.flag)); //Placeholder for destination
                }else if(isObstacle(l.gameBoardData.getObstacles(), i, j)) {
                    gridList.add(new GridItem(R.drawable.fire_image)); //Placeholder for obstacle
                }else if(isCollectable(l.gameBoardData.getCollectables(), i, j)) {
                    gridList.add(new GridItem(R.drawable.coin_image)); //Placeholder for collectable
                } else {// Blank Tile
                    gridList.add(new GridItem(R.drawable.empty)); //Placeholder for empty tile
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

        int s = levelController.getSelected();

        //android was getting confused when only using one gray drawable to setup move buttons.
        ColorDrawable gray1 = new ColorDrawable(Color.parseColor("#D3D3D3"));
        ColorDrawable gray2 = new ColorDrawable(Color.parseColor("#D3D3D3"));

        moveList.add(s, new MoveItem(moveText, gray1, gray2));//add item
        moveAdapter.notifyItemInserted(s);
        recyclerView.scrollToPosition(s);

        recyclerView.post(this::blinkUI);//Delays adding animation until after view holder is set
    }

    /**
     * Remove the last move item from the list
     * @param before the command that got deleted
     */
    private void removeMoveFromUI (int before) {
        moveList.remove(before);
        moveAdapter.notifyItemRemoved(before);//notify adapter of change
        blinkUI();
    }

    private void setButtonsClickable(boolean value) {
        findViewById(R.id.up_button).setClickable(value);
        findViewById(R.id.down_button).setClickable(value);
        findViewById(R.id.left_button).setClickable(value);
        findViewById(R.id.right_button).setClickable(value);
        findViewById(R.id.button_a).setClickable(value);
        findViewById(R.id.button_b).setClickable(value);
        findViewById(R.id.start_button).setClickable(value);
        findViewById(R.id.reset_button).setClickable(value);
        findViewById(R.id.delete_button).setClickable(value);
        findViewById(R.id.back_button).setClickable(value);

    }

    /// VICTORY / FAILURE SCREENS

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
            mainMenuButton();
            winScreen.dismiss();
        });

        customView.findViewById(R.id.RetryButton).setOnClickListener(v -> {
            Log.v("Win Dialogue", "Retry level button pressed");
            retry();
            winScreen.dismiss();
        });

        customView.findViewById(R.id.next_level_button).setOnClickListener(v -> {
            Log.v("Win Dialogue", "Next level button pressed");
            clearGameListeners();
            winScreen.dismiss();
            openSelectedLevel(++selectedLevelID);
        });

        // Only show next button if there is another level
        customView.findViewById(R.id.next_level_button).setVisibility(selectedLevelID < levels.size() - 1 ? VISIBLE : INVISIBLE);

        winScreen.show();
    }

    /**
     * Show the collision screen
     */
    private void showCollisionScreen() {
        //this is creating the view to be referenced
        LayoutInflater collisionInflater = getLayoutInflater();
        View collisionView = collisionInflater.inflate(R.layout.robot_hit_dialog, null);

        //this is putting the alert onto the screen with the created view
        AlertDialog collisionScreen = new AlertDialog.Builder(this)
                .setView(collisionView)
                .setCancelable(false)
                .create();

        //this is the button listener to close the dialog
        collisionView.findViewById(R.id.CrashDialogRetryButton).setOnClickListener(v -> {
            retry();
            collisionScreen.dismiss();
        });

        //this is the button listener to bring the dialog back to the main menu
        collisionView.findViewById(R.id.CrashDialogMenuButton).setOnClickListener(v -> {
            mainMenuButton();
            collisionScreen.dismiss();
        });

        collisionScreen.show();
    }

    /**
     * Shows the got lost screen
     */
    private void showLostScreen() {
        // Create the view to be referenced
        LayoutInflater collisionInflater = getLayoutInflater();
        View lostView = collisionInflater.inflate(R.layout.robot_lost_dialog, null);

        // Set up the dialog
        AlertDialog lostScreen = new AlertDialog.Builder(this)
                .setView(lostView)
                .setCancelable(false)
                .create();

        // This is the button listener to close the dialog
        lostView.findViewById(R.id.LostDialogRetryButton).setOnClickListener(v -> {
            retry();
            lostScreen.dismiss();
        });

        lostView.findViewById(R.id.LostDialogMenuButton).setOnClickListener(v -> {
            mainMenuButton();
            lostScreen.dismiss();
        });

        // Show the dialog
        lostScreen.show();
    }

    /**
     * Show the settings screen
     */
    private void showSettingsMenu(){
        //Create the view to be referenced
        LayoutInflater settingsInflater = getLayoutInflater();
        View settingsView = settingsInflater.inflate(R.layout.settings_screen, null);

        AlertDialog settingsScreen = new AlertDialog.Builder(this)
                .setView(settingsView)
                .setCancelable(false)
                .create();

        settingsView.findViewById(R.id.settingsCloseButton).setOnClickListener(v -> {
            settingsScreen.dismiss();
        });

        settingsScreen.show();
    }

    /**
     * Common lines of code for Main Menu Buttons
     */
    private void mainMenuButton() {
        clearGameListeners();
        openLevelSelect();
    }

    /**
     * Common list of commands used by all retry buttons
     */
    private void retry() {
        levelController.retryLevel();
        setButtonsClickable(true);
        animator.start();
        resetGrid();
        clearGhost();
    }

    /**
     * This is the method for changing the values of the save dialog and then showing it
     */
    private void showSaveDebug(){

        //this is creating the view to be referenced
        LayoutInflater saveInflater = getLayoutInflater();
        View saveDebug = saveInflater.inflate(R.layout.save_debug_dialog, null);

        //this is putting the alert onto the screen with the created view
        AlertDialog saveDebugScreen = new AlertDialog.Builder(this)
                .setView(saveDebug)
                .setCancelable(false)
                .create();

        //this is the button listener to close the dialog
        saveDebug.findViewById(R.id.save_debug_close).setOnClickListener(v ->{
            saveDebugScreen.dismiss();
        });

        //This is setting the move sequence text to the current move sequence save value
        TextView moveSequence = (TextView) saveDebug.findViewById(R.id.save_content_moveSequence);
        moveSequence.setText(saveFunction.GetMoveSequence()+"");

        //This is setting the number of attempts text to the current number of attempts save value
        TextView numberOfAttempts = (TextView) saveDebug.findViewById(R.id.save_content_numberOfAttempts);
        numberOfAttempts.setText(saveFunction.GetMoveAttempts()+"");

        //This is setting the efficiency score text to the current efficiency score save value
        TextView efficiencyScore = (TextView) saveDebug.findViewById(R.id.save_content_efficiencyScore);
        efficiencyScore.setText(saveFunction.GetEfficiencyScore()+"");

        //This is setting the total squares traveled text to the current total squares traveled save value
        TextView totalSquaresTraveled = (TextView) saveDebug.findViewById(R.id.save_content_totalSquaresTraveled);
        totalSquaresTraveled.setText(saveFunction.GetTotalSquaresTraveled()+"");

        //This is setting the current move difference text to the current move difference save value
        TextView currentMoveDifference = (TextView) saveDebug.findViewById(R.id.save_content_currentMoveDifference);
        currentMoveDifference.setText(saveFunction.GetCurrentMoveDifference()+"");

        //This is setting the best move difference text to the best move difference save value
        TextView bestMoveDifference = (TextView) saveDebug.findViewById(R.id.save_content_bestMoveDifference);
        bestMoveDifference.setText(saveFunction.GetBestMoveDifference()+"");

        //This is setting the percentage collectibles collected text to the current percentage collectibles collected save value
        TextView percentageCollectiblesCollected = (TextView) saveDebug.findViewById(R.id.save_content_percentageCollectiblesCollected);
        percentageCollectiblesCollected.setText(saveFunction.GetPercentageCollectiblesCollected()+"");

        saveDebugScreen.show();
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