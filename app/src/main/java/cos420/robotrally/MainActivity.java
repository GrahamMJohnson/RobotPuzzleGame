package cos420.robotrally;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cos420.robotrally.adaptersAndItems.AttemptAdapter;
import cos420.robotrally.adaptersAndItems.AttemptItem;
import cos420.robotrally.adaptersAndItems.GridAdapter;
import cos420.robotrally.adaptersAndItems.GridItem;
import cos420.robotrally.adaptersAndItems.LevelAdapter;
import cos420.robotrally.adaptersAndItems.LevelItem;
import cos420.robotrally.adaptersAndItems.MoveAdapter;
import cos420.robotrally.adaptersAndItems.MoveItem;
import cos420.robotrally.enumerations.EAfterExecuteCondition;
import cos420.robotrally.enumerations.ListName;
import cos420.robotrally.levels.LevelData;
import cos420.robotrally.models.Collectable;
import cos420.robotrally.models.GameBoard;
import cos420.robotrally.models.LevelController;
import cos420.robotrally.models.Obstacle;
import cos420.robotrally.models.StatManager;
import cos420.robotrally.services.LevelMapper;
import cos420.robotrally.models.ExecutionCallback;

/**
 * Class that handles all UI related stuff
 */
public class MainActivity extends AppCompatActivity implements LevelAdapter.LevelSelectListener, MoveAdapter.MoveListener, AttemptAdapter.AttemptListener, ExecutionCallback{

    /** The move list that is currently active */
    private List<MoveItem> activeMoveList;
    /** The move list for the main list */
    private List<MoveItem> moveListMain;
    /** The move list for the A subroutine */
    private List<MoveItem> moveListA;
    /** The move list for the B subroutine */
    private List<MoveItem> moveListB;
    /** The list of ghost elements for the movement trail  */
    private List<View> ghostList;
    /** The media player responsible for playing sounds */
    private MediaPlayer mediaPlayer;
    /** The move adapter that is currently active */
    private MoveAdapter activeMoveAdapter;
    /** The move adapter for the main list */
    private MoveAdapter moveAdapterMain;
    /** The move adapter for the A subroutine */
    private MoveAdapter moveAdapterA;
    /** The move adapter for the B subroutine */
    private MoveAdapter moveAdapterB;
    /** The recycler view that is currently active */
    private RecyclerView activeRecyclerView;
    /** The recycler view for the main list */
    private RecyclerView recyclerViewMain;
    /** The recycler view for the A subroutine */
    private RecyclerView recyclerViewA;
    /** The recycler view for the B subroutine */
    private RecyclerView recyclerViewB;
    /** Reference to the subroutine edit view */
    private View subroutineEditView;
    /** Tracks if use is editing subroutine or not */
    private boolean editingSubroutine;
    /** Reference to the settings menu from the game play screen */
    AlertDialog settingsScreenAttempts;
    /** The index of the currently selected command in the UI */
    private int curSelectUI;
    /** Reference to the grid view */
    private GridView gridView;
    /** Reference to the grid adapter */
    private GridAdapter gridAdapter;
    /** Reference to the grid list */
    private ArrayList<GridItem> gridList;
    /** Independent tracking of where we are in execution, in the UI */
    private int executingMoveUI;
    /** The animator to make the cursor blink */
    private ObjectAnimator animator;
    /**Creates the stat manager for the level*/
    private StatManager saveFunction;
    /** Reference to the Level Controller for the level */
    private LevelController levelController;
    /** Used to make sure changes don't get saved if a run has not been completed */
    private boolean changesMade;
    /** 0 indexed list of the data for level layout. Level 1 can be accessed by levels.get(0) */
    private List<LevelData> levels;
    /** The ID for the selected level */
    private int selectedLevelID;
    /** Boolean to track if animation is active or not */
    private boolean animationIsOff;
    /** Attribute to track if subroutines are being edited */
    private ListName activeListName = ListName.MAIN;
    /** The image to display on collectable tiles */
    private int collectableImage;
    /** The image to display on destination tile */
    private int destinationImage;
    /** The image to display for the roomba */
    private int roombaImage;
    /** Attribute to track if images are gifs or not */
    private boolean isGif;
    private ImageView moving;
    private FrameLayout root;
    private ImageView lastImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // list of data for all the levels
        levels = LevelMapper.mapLevelDataFromFile(this);

        //Sets animations to "OFF" by default
        animationIsOff = true;
        setGifs();

        openLevelSelect();

        root = findViewById(android.R.id.content);
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

        findViewById(R.id.settingsButton).setOnClickListener(v -> showSettingsMenu());

        // for each level
        for (int i = 0; i < levels.size(); i++) {
            double percentage;
            saveFunction = new StatManager(this, i);
            percentage = saveFunction.GetEfficiencyScore();
            // Create LevelItem
            levelDisplayList.add(new LevelItem(i, percentage));
        }
        levelAdapter = new LevelAdapter(this, levelDisplayList, this);
        GridView levelLayout = findViewById(R.id.dynamicLevelLayout);
        levelLayout.setAdapter(levelAdapter);

        findViewById(R.id.clearSaveButton).setOnClickListener(v -> {
            for(int i = 0; i < levels.size(); i++){
                saveFunction = new StatManager(this, i);
                saveFunction.resetSave();
            }
            openLevelSelect();
        });
    }

    /**
     * Method that handles level being selected
     * @param levelID to try to open
     */
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

        // Set up al of the lists
        moveListMain = new ArrayList<>();
        moveListA = new ArrayList<>();
        moveListB = new ArrayList<>();
        ghostList = new ArrayList<>();

        // Moves view set up
        recyclerViewMain = findViewById(R.id.move_viewer);

        // Set LayoutManager
        recyclerViewMain.setLayoutManager(new LinearLayoutManager(this));

        // Set Adapters
        moveAdapterMain = new MoveAdapter(moveListMain, this, ListName.MAIN);
        moveAdapterA = new MoveAdapter(moveListA, this, ListName.A);
        moveAdapterB = new MoveAdapter(moveListB, this, ListName.B);

        // Connect adapter to recycler
        recyclerViewMain.setAdapter(moveAdapterMain);

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
        setupGUIButtons(levelController, levels.get(levelID));

        setActiveList(ListName.MAIN);

        //this starts an instance of a stat manager class based on the level number
        saveFunction = new StatManager(this, levelID);
        changesMade = false;
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
     */ // TODO don't delete destination or obstacles
    @Override
    public void onStepMove() {
        GameBoard g = levelController.getGameBoard();

        boolean weCrashed = levelController.getWeCrashed();

        int previousIndex = (g.getPreviousRow() * g.getSize()) + g.getPreviousColumn();
        int currentIndex = (g.getCurrentRow() * g.getSize()) + g.getCurrentColumn();

        GridItem current = gridList.get(currentIndex);

        if(current.getImage() == collectableImage || current.getImage() == destinationImage) {
            current.setImage(R.drawable.empty);
        }
        if (!weCrashed) {
            animateTileMove(previousIndex, currentIndex);
        }else {
            if (!g.isOutOfBounds()) { //is not out of bounds
                animateCrash(previousIndex, currentIndex);
            }
        }
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

        //Remove the last temporary view for the animation
        root.removeView(moving);

        //Set next tile to Invisible
        lastImage = toView.findViewById(R.id.tile_view);
        lastImage.setVisibility(INVISIBLE);

        //Create new temporary view
        moving = new ImageView(this);
        moving.setImageDrawable(((ImageView) fromView.findViewById(R.id.tile_view)).getDrawable());

        //Layout for temporary view
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(fromView.getWidth(), fromView.getHeight());
        params.leftMargin = fromPlace[0];
        params.topMargin = fromPlace[1];
        root.addView(moving, params);

        //Set previous
        gridList.set(from, gridList.get(to));
        gridAdapter.notifyDataSetChanged();

        //Play movement sound
        playRoombaMoveSound();

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
                //Set the image of the last tile to Visible
                ImageView fromViewImage = fromView.findViewById(R.id.tile_view);
                fromViewImage.setVisibility(VISIBLE);

                //Set current
                gridList.set(to, pre);
                gridAdapter.notifyDataSetChanged();
            }
        });

        animator.start();
    }

    /**
     * Animates the roomba crashing
     * @param from the valid tile
     * @param to the tile being crashed into
     */
    private void animateCrash(int from, int to) {
        //Views of tiles
        View fromView = gridView.getChildAt(from);
        View toView = gridView.getChildAt(to);

        //Coordinates
        int[] fromPlace = new int[2];
        int[] toPlace = new int[2];
        fromView.getLocationOnScreen(fromPlace);
        toView.getLocationOnScreen(toPlace);

        //Movement line
        float halfX = (toPlace[0] - fromPlace[0]) * 0.5f;
        float halfY = (toPlace[1] - fromPlace[1]) * 0.5f;

        //Remove the last temporary view for the animation
        root.removeView(moving);

        //Add temporary image to root
        moving = new ImageView(this);
        moving.setImageDrawable(((ImageView) fromView.findViewById(R.id.tile_view)).getDrawable());
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(fromView.getWidth(), fromView.getHeight());
        params.leftMargin = fromPlace[0];
        params.topMargin = fromPlace[1];
        root.addView(moving, params);

        //Make the original tile invisible
        ImageView fromImage = fromView.findViewById(R.id.tile_view);
        fromImage.setVisibility(INVISIBLE);

        //Animator
        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
        animator.setDuration(400);

        //animate movement
        animator.addUpdateListener(animation -> {
            //Check where movement progress, if halfway turn around
            float progress = (float) animation.getAnimatedValue();
            float moveProgress = progress <= 0.5f ? (progress * 2) : (1 - progress) * 2;

            //Set movement
            float x = fromPlace[0] + halfX * moveProgress;
            float y = fromPlace[1] + halfY * moveProgress;
            moving.setTranslationX(x - fromPlace[0]);
            moving.setTranslationY(y - fromPlace[1]);
        });

        //End of animation
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                //Cleanup
                fromImage.setVisibility(VISIBLE);
            }
        });

        //Start animator
        animator.start();
    }

    /**
     * Adds animations to desired button.
     * Scaling button on press.
     * @param view this view
     */
    private void animateButton(View view) {
        ScaleAnimation scaleAnimation = new ScaleAnimation(
                1f, 0.9f, // Start and end values for the X axis scaling
                1f, 0.9f, // Start and end values for the Y axis scaling
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(100);
        scaleAnimation.setRepeatCount(1);
        scaleAnimation.setRepeatMode(Animation.REVERSE);
        view.startAnimation(scaleAnimation);
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
        params.leftMargin = (int) (x + (tileW / 2.0) - (ghostSize / 2.0));
        params.topMargin = (int) (y + (tileH / 2.0) - (ghostSize / 2.0));

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
        root.addView(ghost, root.indexOfChild(moving));

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
        saveFunction.currentNumberCollectibles = levelController.getCollectiblesCollected();
        handleExecutionEnd(result);
    }

    /**
     * Last piece of execution run, used to show the correct screen based on execution result
     * @param result end result determined by the execution script in level controller
     */
    private void handleExecutionEnd(EAfterExecuteCondition result){
        // Resetting color of last highlighted move.
        MoveItem m = moveListMain.get(executingMoveUI);
        m.clearHighlight();
        moveAdapterMain.notifyItemChanged(executingMoveUI);

        if (result == EAfterExecuteCondition.DEST_REACHED) {
            saveFunction.saveCurrentMoveSequence(true);
            saveFunction.checkBest(true);
            showWinScreen();
        } else if (result == EAfterExecuteCondition.CRASHED) {
            saveFunction.saveCurrentMoveSequence(false);
            saveFunction.checkBest(false);
            playRoombaCrashSound();
            showCollisionScreen();
        } else if (result == EAfterExecuteCondition.GOT_LOST) {
            saveFunction.saveCurrentMoveSequence(false);
            saveFunction.checkBest(false);
            showLostScreen();
        } else {
            Log.d("End Condition Error", "Someone added a new end condition and " +
                    "forgot to add it to the end-screen handler.");
        }
        collapseSubroutinesInUI();
    }

    /**
     * Highlights the currently executing move, graying the previous one anc yellow-ing the current one
     * @param index of execution in list
     */
    public void highlightExecutionStep(int index) {
        if (index < 0 || index >= moveListMain.size()) return;

        // Reset previous highlight
        if (executingMoveUI >= 0 && executingMoveUI < moveListMain.size() && executingMoveUI != index) {
            MoveItem prevMove = moveListMain.get(executingMoveUI);
            prevMove.clearHighlight();
            moveAdapterMain.notifyItemChanged(executingMoveUI);
        }

        MoveItem currentMove = moveListMain.get(index);
        ColorDrawable yellow = new ColorDrawable(Color.parseColor("#FFF176")); // or any highlight color
        currentMove.setButtonColor(yellow);

        // Keep the executing command > 1 spot above bottom of view until its the last command
        // This was you can see what is coming next
        int scrollIndex = index + 1;
        if (index == 0) {
            scrollIndex = index; // Scroll to top for first command
        }

        recyclerViewMain.scrollToPosition(scrollIndex);

        moveAdapterMain.notifyItemChanged(index);

        executingMoveUI = index;
    }

    /**
     * Resets the grid of the UI game board
     */
    public void resetGrid() {
        //Removes the temporary view over grid
        root.removeView(moving);

        //Sets the last tile back to Visible
        if (lastImage != null) {
            lastImage.setVisibility(VISIBLE);
        }

        //Grid clear and setup
        gridList.clear();
        setupGrid(levels.get(selectedLevelID));
        gridAdapter.notifyDataSetChanged();
    }

    /**
     * Method that handles when a move item in the move list is clicked
     * @param position The position of the move item
     * @param list The list the move item is in
     */
    @Override
    public void onMoveClick(int position, ListName list) {
        if (activeListName == list) {
            //set which command is selected
            levelController.setSelected(position, list);
            blinkUI();
        }
        // editing sub commands and clicking on A or B (whichever isn't currently active
        else if (list != ListName.MAIN) {
            // Make sure cursor ends up on clicked move. If this move is already selected,
            //  selecting it again would cause the last move to become selected
            if (position != levelController.getSelected(list))
                levelController.setSelected(position, list);
            setActiveList(list);
        }
    }

    /**
     * Method that creates/shows the blinking cursor
     */
    public void blinkUI() {

        //update UI of selected command
        int select = levelController.getSelected(activeListName);

        //Remove blink from previous command
        if (animator != null && animator.isRunning()) {
            animator.cancel();
            //remove background from previous selected
            if (curSelectUI >= 0 && curSelectUI < activeMoveList.size() && curSelectUI != select) {
                MoveItem m = activeMoveList.get(curSelectUI);
                ColorDrawable gray = new ColorDrawable(Color.parseColor("#D3D3D3"));
                m.setColor(gray);
                activeMoveList.set(curSelectUI, m);
                activeMoveAdapter.notifyItemChanged(curSelectUI);
            }
        }

        //Set UI to blink on selected command
        RecyclerView.ViewHolder v = activeRecyclerView.findViewHolderForAdapterPosition(select);
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
    private void setupGUIButtons(LevelController levelC, LevelData curLevel) {
        // UP
        findViewById(R.id.up_button).setOnClickListener(v -> {
            animateButton(v);
            boolean moveAdded = levelC.addUpCommand(activeListName);
            if (moveAdded)
                addMoveToUI("UP");
        });

        // DOWN
        findViewById(R.id.down_button).setOnClickListener(v -> {
            animateButton(v);
            boolean moveAdded = levelC.addDownCommand(activeListName);
            if (moveAdded)
                addMoveToUI("DOWN");
        });

        // LEFT
        findViewById(R.id.left_button).setOnClickListener(v -> {
            animateButton(v);
            boolean moveAdded = levelC.addLeftCommand(activeListName);
            if (moveAdded)
                addMoveToUI("LEFT");
        });

        // RIGHT
        findViewById(R.id.right_button).setOnClickListener(v -> {
            animateButton(v);
            boolean moveAdded = levelC.addRightCommand(activeListName);
            if (moveAdded)
                addMoveToUI("RIGHT");
        });

        // A
        findViewById(R.id.button_a).setOnClickListener(v -> {
            animateButton(v);
            boolean moveAdded = levelC.addSubroutineA(activeListName);
            if (moveAdded)
                addMoveToUI("A");
        });

        // B
        findViewById(R.id.button_b).setOnClickListener(v -> {
            animateButton(v);
            boolean moveAdded = levelC.addSubroutineB(activeListName);
            if (moveAdded)
                addMoveToUI("B");
        });

        // DELETE
        findViewById(R.id.delete_button).setOnClickListener(v -> {
            animateButton(v);
            try {
                int before = levelC.getSelected(activeListName); //The command that was deleted
                levelC.remove(activeListName);
                removeMoveFromUI(before);
            } catch (Exception e) {
                Log.d("Button Pressed", "Attempted to remove item from empty list");
            }
        });

        // Edit Subroutines
        findViewById(R.id.subroutine_button).setOnClickListener(v -> {
            animateButton(v);
            if (!editingSubroutine) {
                editingSubroutine = true;
                findViewById(R.id.start_button).setClickable(false);
                showSubroutineEditScreen();
                // Edit subroutine A the first time, otherwise the last subroutine being edited
                setActiveList(ListName.A);
                int lastIndex = activeMoveList.size() - 1;
                if (lastIndex > 0) {
                    levelController.setSelected(lastIndex, activeListName);
                }
                activeRecyclerView.post(this::blinkUI);
            } else {
                editingSubroutine = false;
                ViewGroup rootLayout = findViewById(android.R.id.content);
                setActiveList(ListName.MAIN);
                rootLayout.removeView(subroutineEditView);
                findViewById(R.id.start_button).setClickable(true);
            }
        });

        // START
        findViewById(R.id.start_button).setOnClickListener(v -> {
            animateButton(v);
            levelController.resetCollectiblesCollected();
            setButtonsClickable(false);
            animator.end();
            recyclerViewMain.smoothScrollToPosition(0);

            // Save number of moves in list
            saveFunction.currentNumMoves = moveListMain.size();

            // Expand subroutines and have subroutine letter appended to moves for saving
            expandSubroutinesWithSubroutineLetter();

            //these are the edits that need to be made to the save function each time it is run
            saveFunction.currentMoveSequence = saveFunction.MoveSequenceConverter(moveListMain);
            saveFunction.currentNumAttempts += 1;
            changesMade = true;

            // Remove appended subroutine letters
            removeAppendedLetters();

            recyclerViewMain.post(() -> levelC.executeScript(moveListMain, this, this));
        });

        // BACK
        findViewById(R.id.back_button).setOnClickListener(v -> {
            animateButton(v);
            clearGameListeners();
            openLevelSelect();
            editingSubroutine = false;
        });

        // gameBoard settings/info
        findViewById(R.id.settings_info_button).setOnClickListener(v -> {
            animateButton(v);
            if(changesMade) {
                saveFunction.saveCurrent();
            }
            showSettingsInfoMenu(curLevel);
        });
    }

    /**
     * Sets up the game board grid
     * @param l the current level
     */
    private void setupGrid(LevelData l) {
        int size = l.gameBoardData.getSize(); //size of game board
        //Set column number for grid
        gridView.setNumColumns(size);

        for (int i = 0; i < size; i++) { // Row
            for (int j = 0; j < size; j++) { //Column
                if (i == l.gameBoardData.getStartRow() && j == l.gameBoardData.getStartColumn()) { //Check if start tile
                    gridList.add(new GridItem(roombaImage, 0, isGif, i, j));

                } else if (i == l.gameBoardData.getGoalRow() && j == l.gameBoardData.getGoalColumn()) { //Check if Goal tile
                    gridList.add(new GridItem(destinationImage, l.gameBoardData.getGoalRotation(), isGif, i, j));

                } else if(isObstacle(l.gameBoardData.getObstacles(), i, j) != -1) {
                    int obsIndex = isObstacle(l.gameBoardData.getObstacles(), i, j);
                    int obsRotation = getObstacleSpin(l.gameBoardData.getObstacles(), obsIndex, isGif);
                    int obsImg = getObstacleImg(l.gameBoardData.getObstacles(), obsIndex, isGif);
                    gridList.add(new GridItem(obsImg, obsRotation, isGif, i, j));

                } else if(isCollectable(l.gameBoardData.getCollectables(), i, j)) {
                    // get random rotation
                    int ranRotate = (int) ((Math.random() * (4))*90);
                    gridList.add(new GridItem(collectableImage, ranRotate, isGif, i, j));

                } else {// Blank Tile
                    gridList.add(new GridItem(R.drawable.empty, 0, false, i, j));
                }
            }
        }
    }

    /**
     * Changes the grid based on if animations are on or off
     * @param l the level data
     */
    private void changeGridForGif(LevelData l) {
        for (int i = 0; i < gridList.size(); i++) {
            int image = gridList.get(i).getImage();
            int rotation = 0;
            int r = gridList.get(i).getRow();
            int c = gridList.get(i).getCol();

            if (image == R.drawable.collectible_dirt || image == R.drawable.dust) { //is a collectable
                if (!isGif) { rotation = (int) ((Math.random() * (4))*90); }
                gridList.set(i, new GridItem(collectableImage, rotation, isGif, r, c));

            } else if (image == R.drawable.destination || image == R.drawable.destination_gif) { //Destination
                if (!isGif) { rotation = l.gameBoardData.getGoalRotation(); }
                gridList.set(i, new GridItem(destinationImage, rotation, isGif, r, c));

            } else if (image == R.drawable.roomba || image == R.drawable.roomba_gif) { // roomba
                gridList.set(i, new GridItem(roombaImage, 0, isGif, r, c));

            } else if (image != R.drawable.empty) {
                // is a obstacle; cannot reasonably use 'if' to detect specifically obstacle due to number of walls

                int obsIndex = 0;
                if (!isGif) { // we don't need to spend the processing time if if's a gif, since they all same
                    // find the index in obstacle list
                    obsIndex = isObstacle(l.gameBoardData.getObstacles(), r, c);
                }
                int obsImg = getObstacleImg(l.gameBoardData.getObstacles(), obsIndex, isGif);
                rotation = getObstacleSpin(l.gameBoardData.getObstacles(), obsIndex, isGif);
                // get the data to display
                gridList.set(i, new GridItem(obsImg, rotation, isGif, r, c));
            }
        }
    }

    /**
     * @param list  of obstacles
     * @param index of obstacle in list
     * @param isGif whether or not gifs are active
     * @return  the image that should be displayed for this obstacle
     */
    private int getObstacleImg(List<Obstacle> list, int index, boolean isGif) {
        if (isGif) {
            return R.drawable.cat_gif;
        } else {
            String obstacleString = list.get(index).getObstacleType();
            return this.getResources().getIdentifier(obstacleString, "drawable", this.getPackageName());
        }
    }

    /**
     * @param list  of obstacles
     * @param index of obstacle in list
     * @param isGif whether or not gifs are active
     * @return  the rotation the obstacle's image should have
     */
    private int getObstacleSpin(List<Obstacle> list, int index, boolean isGif) {
        if (isGif) { return 0; } // gifs should not be rotated
        return list.get(index).getRotation();
    }

    /**
     * Checks if the tile is a Obstacle and determines what index it bears in the obstacle list
     * @param list  of obstacles
     * @param row   of the tile
     * @param col   of the tile
     * @return      the index if obstacle, -1 otherwise
     */
    private int isObstacle(List<Obstacle> list, int row, int col) {
        for (int i = 0; i < list.size(); i++) {
            int r = list.get(i).getRow();
            int c = list.get(i).getColumn();
            if(r == row && c == col) {
                return i;
            }
        }
        return -1;
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

        //android was getting confused when only using one gray drawable to setup move buttons.
        ColorDrawable color1 = new ColorDrawable(Color.parseColor("#D3D3D3"));
        ColorDrawable backgroundColor;
        // Using a switch w/ fallthrough rather than if-statement in order to make future work
        // easier. Each type of move will need to access a different image.
        switch (moveText) {
            case "UP":
            case "DOWN":
            case "LEFT":
            case "RIGHT":
                backgroundColor = new ColorDrawable(Color.parseColor("#D3D3D3"));
                break;
            case "A":
                backgroundColor = new ColorDrawable(getResources().getColor(R.color.indian_red, null));
                break;
            case "B":
                backgroundColor = new ColorDrawable(getResources().getColor(R.color.steel_blue, null));
                break;
            default: throw new InvalidParameterException(moveText + " is not a valid move.");
        }

        int s = levelController.getSelected(activeListName);


        activeMoveList.add(s, new MoveItem(moveText, color1, backgroundColor));//add item
        activeMoveAdapter.notifyItemInserted(s);

        activeRecyclerView.scrollToPosition(s);
        activeRecyclerView.post(this::blinkUI);//Delays adding animation until after view holder is set
    }

    /**
     * Method to add moves of a subroutine to UI
     * @param subroutine The text to display
     * @param location Where in the list it is being added
     */
    private void addSubroutineToUI(String subroutine, int location) {
        List<MoveItem> subsequenceToAdd;
        ColorDrawable color1 = new ColorDrawable(getResources().getColor(R.color.light_grey, null));
        ColorDrawable color2;
        switch(subroutine){
            case "A":
                color2 = new ColorDrawable(getResources().getColor(R.color.indian_red, null));
                subsequenceToAdd = moveListA;
                break;
            case "B":
                color2 = new ColorDrawable(getResources().getColor(R.color.steel_blue, null));
                subsequenceToAdd = moveListB;
                break;
            case "AB":
            case "SAB":
                color2 = new ColorDrawable(getResources().getColor(R.color.mediumPurple, null));
                subsequenceToAdd = moveListB;
                break;
            default: throw new InvalidParameterException(subroutine + " is not a valid move.");
        }

        int s = location;
        int start = s;
        boolean first = true;
        // Add all moves from designated subsequence
        for (var move : subsequenceToAdd) {
            var text = move.getText();
            // Mark the start of the subroutine
            if (first) {
                first = false;
                switch(subroutine) {
                    case "A": text = "SA" + text; break;
                    case "B": text = "SB" + text; break;
                    case "AB": text = "ASB" + text; break;
                    case "SAB": text = "SASB" + text; break;
                }
            }
            else {
                if (subroutine.equals("SAB"))
                    text = "AB" + text;
                else
                    text = subroutine + text;
            }
            // Add move to list
            MoveItem m = new MoveItem(text, color1, color2);
            m.setSubroutineType(subroutine);
            moveListMain.add(s, m); //add item
            s++;
        }

        moveAdapterMain.notifyItemRangeInserted(start, subsequenceToAdd.size());
    }

    /**
     * Method to expand the A/B command into the full subroutine
     * Also appends the letter of the subroutine to the beginning of the move for saving purposes
     */
    private void expandSubroutinesWithSubroutineLetter() {
        for (int i = 0; i < moveListMain.size(); i++) {
            MoveItem m = moveListMain.get(i);
            String text = m.getText();
            if (!(text.equals("A") ||
                    text.equals("B") ||
                    text.equals("AB") ||
                    text.equals("SAB"))) {
                continue;
            }

            // remove the a command
            moveListMain.remove(i);
            moveAdapterMain.notifyItemRemoved(i);

            // add the commands from a to main script
            addSubroutineToUI(text, i);

            // Check the new item at at same index, as this could be a nested subroutine
            i--;
        }
        levelController.setSelected(0, ListName.MAIN);
    }

    /**
     *  Method to remove A/B from beginning of move commands
     */
    private void removeAppendedLetters() {
        for (MoveItem m : moveListMain) {
            String text = m.getText();
            if (text.startsWith("SASB"))
                m.setText(text.substring(4));
            else if (text.startsWith("ASB"))
                m.setText(text.substring(3));
            else if (text.startsWith("AB") || text.startsWith("SA") || text.startsWith("SB"))
                m.setText(text.substring(2));
            else if (text.startsWith("A") || text.startsWith("B"))
                m.setText(text.substring(1));
        }
    }


    /**
     * Method to collapse subroutine back into A/B command when execution is complete
     */
    private void collapseSubroutinesInUI() {
        for (int i = 0; i < moveListMain.size(); i++) {
            MoveItem m = moveListMain.get(i);

            String subroutine = m.getSubroutineType();

            if (subroutine == null) {
                continue;
            }

            MoveItem newMoveItem;
            int commandsToRemove;

            ColorDrawable color1 = new ColorDrawable(getResources().getColor(R.color.light_grey, null));
            ColorDrawable color2;

            switch (subroutine) {
                case "AB":
                case "A":
                    commandsToRemove = moveListA.size() + getNumberOfBCommandsInA() * (moveListB.size() - 1);
                    color2 = new ColorDrawable(getResources().getColor(R.color.indian_red, null));
                    newMoveItem = new MoveItem("A", color1, color2);
                    break;
                case "B":
                    commandsToRemove = moveListB.size();
                    color2 = new ColorDrawable(getResources().getColor(R.color.steel_blue, null));
                    newMoveItem = new MoveItem("B", color1, color2);
                    break;
                default: continue;
            }

            // Remove commands from the script based off of how many commands are in subroutine
            for (int j = 0; j < commandsToRemove; j++) {
                moveListMain.remove(i);
            }

            moveAdapterMain.notifyItemRangeRemoved(i, commandsToRemove);

            // Add placeholder subroutine command back to script
            moveListMain.add(i, newMoveItem);
            moveAdapterMain.notifyItemInserted(i);
        }
    }

    /**
     * Method that restarts the blinking cursor after execution
     */
    private void restartBlinkAfterRun() {
        int lastIndex = moveListMain.size() - 1;
        levelController.setSelected(lastIndex, ListName.MAIN);
        blinkUI();
        moveAdapterMain.notifyItemChanged(lastIndex);
    }

    /**
     * Remove the last move item from the list
     * @param before the command that got deleted
     */
    private void removeMoveFromUI (int before) {
        activeMoveList.remove(before);
        activeMoveAdapter.notifyItemRemoved(before); //notify adapter of change
        blinkUI();
    }

    /**
     * Method that can be used to enable/disable all buttons
     * @param value boolean value True enables buttons, False disables them
     */
    private void setButtonsClickable(boolean value) {
        findViewById(R.id.up_button).setClickable(value);
        findViewById(R.id.down_button).setClickable(value);
        findViewById(R.id.left_button).setClickable(value);
        findViewById(R.id.right_button).setClickable(value);
        findViewById(R.id.button_a).setClickable(value);
        findViewById(R.id.button_b).setClickable(value);
        findViewById(R.id.start_button).setClickable(value);
        findViewById(R.id.subroutine_button).setClickable(value);
        findViewById(R.id.delete_button).setClickable(value);
        findViewById(R.id.back_button).setClickable(value);
        findViewById(R.id.settings_info_button).setClickable(value);
    }

    /**
     * Method to change which list is being edited
     * @param listToEdit ListName enum for the list to edit
     */
    private void setActiveList(ListName listToEdit) {

        if (activeMoveList != null && !activeMoveList.isEmpty() &&
                animator != null && animator.isRunning()){
            animator.end();
        }

        activeListName = listToEdit;
        switch(listToEdit) {
            case MAIN:
                activeMoveList = moveListMain;
                activeMoveAdapter = moveAdapterMain;
                activeRecyclerView = recyclerViewMain;
                blinkUI();
                break;
            case A:
                activeMoveList = moveListA;
                activeMoveAdapter = moveAdapterA;
                activeRecyclerView = recyclerViewA;
                activeRecyclerView.post(this::blinkUI);
                break;
            case B:
                activeMoveList = moveListB;
                activeMoveAdapter = moveAdapterB;
                activeRecyclerView = recyclerViewB;
                activeRecyclerView.post(this::blinkUI);
                break;
        }
    }

    /**
     * Method to get the number of B commands in the A subroutine. Used for collapsing subroutines after run
     * @return The number of B commands in A
     */
    private int getNumberOfBCommandsInA() {
        int count = 0;
        for (MoveItem m : moveListA) {
            if (m.getText().equals("B"))
                count++;
        }
        return count;
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
            if(changesMade) {
                saveFunction.saveCurrent();
            }
            Log.v("Win Dialogue", "Previous level button pressed");
            mainMenuButton();
            winScreen.dismiss();
        });

        customView.findViewById(R.id.RetryButton).setOnClickListener(v -> {
            if(changesMade) {
                saveFunction.saveCurrent();
            }
            Log.v("Win Dialogue", "Retry level button pressed");
            retry();
            winScreen.dismiss();
            restartBlinkAfterRun();
        });

        customView.findViewById(R.id.next_level_button).setOnClickListener(v -> {
            if(changesMade) {
                saveFunction.saveCurrent();
            }
            Log.v("Win Dialogue", "Next level button pressed");
            clearGameListeners();
            winScreen.dismiss();
            openSelectedLevel(++selectedLevelID);
        });

        //This sets the text in the best run move count text view to the saved stat
        TextView bestRunMoveCount = customView.findViewById(R.id.Best_Run_Move_Count);
        bestRunMoveCount.setText(String.valueOf(saveFunction.GetBestNumMoves()));

        //This sets the text in the best collectibles count text view to the saved stat
        TextView bestCollectiblesCount = customView.findViewById(R.id.Best_Run_Collectibles_Count);
        bestCollectiblesCount.setText(String.valueOf(saveFunction.getBestNumberCollectibles()));

        //This sets the text in the current move count text view to the saved stat
        TextView currentMoveCount = customView.findViewById(R.id.This_Run_Move_Count);
        currentMoveCount.setText(String.valueOf(saveFunction.GetCurrentNumMoves()));

        //This sets the text in the current collectibles count text view to the saved stat
        TextView currentCollectiblesCount = customView.findViewById(R.id.This_Run_Collectibles_Count);
        currentCollectiblesCount.setText(String.valueOf(saveFunction.getCurrentNumberCollectibles()));

        //This sets the text in the goal moves count text view to the saved stat
        TextView goalMovesCount = customView.findViewById(R.id.Goal_Move_Count);
        goalMovesCount.setText(String.valueOf(saveFunction.idealNumMoves));

        //This sets the text in the goal collectibles number text view to the saved stat
        TextView goalCollectiblesNumber = customView.findViewById(R.id.Goal_Collectibles_Count);
        goalCollectiblesNumber.setText(String.valueOf(saveFunction.numberPossibleCollectibles));

        // Only show next button if there is another level
        customView.findViewById(R.id.next_level_button).setVisibility(selectedLevelID < levels.size() - 1 ? VISIBLE : INVISIBLE);

        ProgressBar winProgressBar = customView.findViewById(R.id.winProgressBar);
        winProgressBar.setProgress((int) (saveFunction.GetEfficiencyScore() * 100));

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

        //This sets the text in the best run move count text view to the saved stat
        TextView bestRunMoveCount = collisionView.findViewById(R.id.Hit_Best_Run_Move_Count);
        bestRunMoveCount.setText(String.valueOf(saveFunction.GetBestNumMoves()));

        //This sets the text in the best collectibles count text view to the saved stat
        TextView bestCollectiblesCount = collisionView.findViewById(R.id.Hit_Best_Run_Collectibles_Count);
        bestCollectiblesCount.setText(String.valueOf(saveFunction.getBestNumberCollectibles()));

        //This sets the text in the current move count text view to the saved stat
        TextView currentMoveCount = collisionView.findViewById(R.id.Hit_This_Run_Move_Count);
        currentMoveCount.setText(String.valueOf(saveFunction.GetCurrentNumMoves()));

        //This sets the text in the current collectibles count text view to the saved stat
        TextView currentCollectiblesCount = collisionView.findViewById(R.id.Hit_This_Run_Collectibles_Count);
        currentCollectiblesCount.setText(String.valueOf(saveFunction.getCurrentNumberCollectibles()));

        //This sets the text in the goal moves count text view to the saved stat
        TextView goalMovesCount = collisionView.findViewById(R.id.Hit_Goal_Move_Count);
        goalMovesCount.setText(String.valueOf(saveFunction.idealNumMoves));

        //This sets the text in the goal collectibles number text view to the saved stat
        TextView goalCollectiblesNumber = collisionView.findViewById(R.id.Hit_Goal_Collectibles_Count);
        goalCollectiblesNumber.setText(String.valueOf(saveFunction.numberPossibleCollectibles));

        //this is the button listener to close the dialog
        collisionView.findViewById(R.id.CrashDialogRetryButton).setOnClickListener(v -> {
            retry();
            collisionScreen.dismiss();
            restartBlinkAfterRun();
        });

        //this is the button listener to bring the dialog back to the main menu
        collisionView.findViewById(R.id.CrashDialogMenuButton).setOnClickListener(v -> {
            mainMenuButton();
            collisionScreen.dismiss();
        });

        ProgressBar collisionProgressBar = collisionView.findViewById(R.id.collisionProgressBar);
        collisionProgressBar.setProgress((int) (saveFunction.GetEfficiencyScore() * 100));

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

        //This sets the text in the best run move count text view to the saved stat
        TextView bestRunMoveCount = lostView.findViewById(R.id.Lost_Best_Run_Move_Count);
        bestRunMoveCount.setText(String.valueOf(saveFunction.GetBestNumMoves()));

        //This sets the text in the best collectibles count text view to the saved stat
        TextView bestCollectiblesCount = lostView.findViewById(R.id.Lost_Best_Run_Collectibles_Count);
        bestCollectiblesCount.setText(String.valueOf(saveFunction.getBestNumberCollectibles()));

        //This sets the text in the current move count text view to the saved stat
        TextView currentMoveCount = lostView.findViewById(R.id.Lost_This_Run_Move_Count);
        currentMoveCount.setText(String.valueOf(saveFunction.GetCurrentNumMoves()));

        //This sets the text in the current collectibles count text view to the saved stat
        TextView currentCollectiblesCount = lostView.findViewById(R.id.Lost_This_Run_Collectibles_Count);
        currentCollectiblesCount.setText(String.valueOf(saveFunction.getCurrentNumberCollectibles()));

        //This sets the text in the goal moves count text view to the saved stat
        TextView goalMovesCount = lostView.findViewById(R.id.Lost_Goal_Move_Count);
        goalMovesCount.setText(String.valueOf(saveFunction.idealNumMoves));

        //This sets the text in the goal collectibles number text view to the saved stat
        TextView goalCollectiblesNumber = lostView.findViewById(R.id.Lost_Goal_Collectibles_Count);
        goalCollectiblesNumber.setText(String.valueOf(saveFunction.numberPossibleCollectibles));

        // This is the button listener to close the dialog
        lostView.findViewById(R.id.LostDialogRetryButton).setOnClickListener(v -> {
            retry();
            lostScreen.dismiss();
            restartBlinkAfterRun();
        });

        lostView.findViewById(R.id.LostDialogMenuButton).setOnClickListener(v -> {
            mainMenuButton();
            lostScreen.dismiss();
        });

        ProgressBar lostProgressBar = lostView.findViewById(R.id.lostProgressBar);
        lostProgressBar.setProgress((int) (saveFunction.GetEfficiencyScore() * 100));

        // Show the dialog
        lostScreen.show();
    }

    /**
     * Shows the Subroutine Edit Screen
     */
    private void showSubroutineEditScreen() {
        ViewGroup rootLayout = findViewById(android.R.id.content);
        LayoutInflater inflater = LayoutInflater.from(this);
        subroutineEditView = inflater.inflate(R.layout.subroutine_edit_overlay, rootLayout, false);

        View grid = findViewById(R.id.grid);
        int height = grid.getHeight();
        int[] location = new int[2];
        grid.getLocationOnScreen(location);
        int topLocation = location[1];
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                height
        );
        params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;
        params.setMargins(2, topLocation, 2, 0); // Margin from bottom-right
        subroutineEditView.setLayoutParams(params);

        // Set up recyclers
        recyclerViewA = subroutineEditView.findViewById(R.id.subroutine_a_viewer);
        recyclerViewB = subroutineEditView.findViewById(R.id.subroutine_b_viewer);

        // Set LayoutManagers
        recyclerViewA.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewB.setLayoutManager(new LinearLayoutManager(this));

        // Connect adapters to recyclers
        recyclerViewA.setAdapter(moveAdapterA);
        recyclerViewB.setAdapter(moveAdapterB);

        subroutineEditView.findViewById(R.id.empty_a_click_area).setOnClickListener(v -> {
            int select = moveListA.size() - 1;
            if (select > 0)
                levelController.setSelected(select, ListName.A);
            setActiveList(ListName.A);
        });

        subroutineEditView.findViewById(R.id.empty_b_click_area).setOnClickListener(v -> {
            int select = moveListB.size() - 1;
            if (select > 0)
                levelController.setSelected(select, ListName.B);
            setActiveList(ListName.B);
        });

        int[] maxMoves = levelController.getSubroutineMaxMoves();

        TextView subAMaxMoves = subroutineEditView.findViewById(R.id.subroutine_a_max_moves);
        String maxMovesA = "Max Moves: " + maxMoves[0];
        subAMaxMoves.setText(maxMovesA);

        TextView subBMaxMoves = subroutineEditView.findViewById(R.id.subroutine_b_max_moves);
        String maxMovesB = "Max Moves: " + maxMoves[1];
        subBMaxMoves.setText(maxMovesB);

        // Add it to root
        rootLayout.addView(subroutineEditView);

        // Handle button press inside the floating panel
        subroutineEditView.findViewById(R.id.close_button).setOnClickListener(v -> {
            editingSubroutine = false;
            setActiveList(ListName.MAIN);
            rootLayout.removeView(subroutineEditView);
            findViewById(R.id.start_button).setClickable(true);
        });
    }

    /**
     * Show the settings/info screen for the gameBoard instance of the settings menu
     */
    private void showSettingsInfoMenu(LevelData curLevel){
        //Create the view to be referenced
        LayoutInflater settingsInflater = getLayoutInflater();
        View settingsView = settingsInflater.inflate(R.layout.settings_info, null);

        settingsScreenAttempts = new AlertDialog.Builder(this)
                .setView(settingsView)
                .setCancelable(false)
                .create();

        settingsView.findViewById(R.id.settingsCloseButton).setOnClickListener(v -> settingsScreenAttempts.dismiss());

        //Checkbox for animations
        CheckBox check = settingsView.findViewById(R.id.checkAnimations);

        //Load current state of checkbox
        check.setChecked(animationIsOff);

        //Check box listener
        check.setOnCheckedChangeListener((buttonView, isChecked) -> {
            //Animation turns off if true, turns on if false
            animationIsOff = isChecked;
            setGifs();
            changeGridForGif(curLevel);
            gridAdapter.notifyDataSetChanged();
        });

        //Set up attempts
        RecyclerView attemptView = settingsView.findViewById(R.id.attempts_recycler);
        attemptView.setLayoutManager(new LinearLayoutManager(this));
        ArrayList<AttemptItem> attemptList = new ArrayList<>();
        AttemptAdapter attemptAdapter = new AttemptAdapter(attemptList, this, this);
        attemptView.setAdapter(attemptAdapter);

        //Determines the number of attempts to show
        for(int x = 1; x <= 15; x++) {
            // Get number of moves
            int numMoves = saveFunction.getPastMoveCount(x);
            // Check if attempt exists
            if (numMoves == -1)
                continue;
            // Create and attempt to list
            attemptList.add(new AttemptItem(x, "Number of moves: " + numMoves,
                    saveFunction.getPastMoveSequenceSuccess(x)));
        }
        attemptAdapter.notifyItemRangeInserted(0, attemptList.size());

        settingsScreenAttempts.show();
    }

    /**
     * Show the settings menu, (FOR LEVEL SELECT MENU)
     */
    private void showSettingsMenu(){
        //Create the view to be referenced
        LayoutInflater settingsInflater = getLayoutInflater();
        View settingsView = settingsInflater.inflate(R.layout.settings_screen, null);

        AlertDialog settingsScreen = new AlertDialog.Builder(this)
                .setView(settingsView)
                .setCancelable(false)
                .create();

        settingsView.findViewById(R.id.settingsCloseButton).setOnClickListener(v -> settingsScreen.dismiss());

        //Checkbox for animations
        CheckBox check = settingsView.findViewById(R.id.checkAnimations);

        //Load current state of checkbox
        check.setChecked(animationIsOff);

        //Check box listener
        check.setOnCheckedChangeListener((buttonView, isChecked) -> {
            //Animation turns off if true, turns on if false
            animationIsOff = isChecked;
            setGifs();
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
     * Sets the Gifs or images if animation is on or off
     */
    public void setGifs() {
        if (animationIsOff) {//animation is off
            collectableImage = R.drawable.collectible_dirt;
            destinationImage = R.drawable.destination;
            roombaImage = R.drawable.roomba;
            isGif = false;
        } else { //animation is on
            collectableImage  = R.drawable.dust;
            destinationImage = R.drawable.destination_gif;
            roombaImage = R.drawable.roomba_gif;
            isGif = true;
        }
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
        saveDebug.findViewById(R.id.save_debug_close).setOnClickListener(v -> saveDebugScreen.dismiss());

        //This is setting the move sequence text to the current move sequence save value
        TextView moveSequence = saveDebug.findViewById(R.id.save_content_moveSequence);
        moveSequence.setText(String.valueOf(saveFunction.GetBestMoveSequence()));

        //This is setting the number of attempts text to the current number of attempts save value
        TextView numberOfAttempts = saveDebug.findViewById(R.id.save_content_numberOfAttempts);
        numberOfAttempts.setText(String.valueOf(saveFunction.GetCurrentNumAttempts()));

        //This is setting the efficiency score text to the current efficiency score save value
        TextView efficiencyScore = saveDebug.findViewById(R.id.save_content_efficiencyScore);
        efficiencyScore.setText(String.valueOf(saveFunction.GetEfficiencyScore()));

        //This is setting the total squares traveled text to the current total squares traveled save value
        TextView totalSquaresTraveled = saveDebug.findViewById(R.id.save_content_totalSquaresTraveled);
        totalSquaresTraveled.setText(String.valueOf(saveFunction.GetBestNumMoves()));

        //This is setting the current move difference text to the current move difference save value
        TextView currentMoveDifference = saveDebug.findViewById(R.id.save_content_currentMoveDifference);
        currentMoveDifference.setText(String.valueOf(saveFunction.GetCurrentNumMoves() - saveFunction.idealNumMoves));

        //This is setting the best move difference text to the best move difference save value
        TextView bestMoveDifference = saveDebug.findViewById(R.id.save_content_bestMoveDifference);
        bestMoveDifference.setText(String.valueOf(saveFunction.GetBestNumMoves() - saveFunction.idealNumMoves));

        //This is setting the percentage collectibles collected text to the current percentage collectibles collected save value
        TextView percentageCollectiblesCollected = saveDebug.findViewById(R.id.save_content_percentageCollectiblesCollected);
        percentageCollectiblesCollected.setText(String.valueOf(saveFunction.getBestCollectiblesPercentage()));

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

    /**
     * What to do when an attempt is clicked
     * @param position the position of the clicked attempt
     */
    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onClick(int position) {
        // Get saved move list and convert to array
        String moveListString = saveFunction.getPastMoveSequence(position + 1);
        List<String> moveList = new ArrayList<>(Arrays.asList(moveListString.split(" ")));

        // Clear commands out of all lists
        levelController.resetLists();
        moveListMain.clear();
        moveListA.clear();
        moveListB.clear();

        //TODO rework everything below here with new save format

        // Get subroutine B
        getSubroutineBFromList(moveList);

        // Get subroutine A
        getSubroutineAFromList(moveList);

        // Update main move list;
        convertToMoveList(moveListMain, moveList);

        // Update backend lists
        updateBackendLists();
        settingsScreenAttempts.dismiss();

        // Alert Adapters of changes
        moveAdapterMain.notifyDataSetChanged();
        moveAdapterA.notifyDataSetChanged();
        moveAdapterB.notifyDataSetChanged();

        setActiveList(ListName.MAIN);
        recyclerViewMain.post(this::blinkUI);
    }

    /**
     * Get subroutine B from moveList
     * @param moveList The list of strings of moves
     */
    private void getSubroutineBFromList(List<String> moveList) {
        List<String> subroutineB = new ArrayList<>();

        for (int i = 0; i < moveList.size(); i++) {
            String text = moveList.get(i);
            // If move is not part of B, go on to next move
            if (!text.contains("SB")) {
                continue;
            }

            // Add move to subroutine B list
            subroutineB.add(text);
            i++;

            // Loop through list ending when a new B starts or when move is not part of B
            while (i < moveList.size()) {
                text = moveList.get(i);

                if (text.contains("S") || !text.contains("B"))
                    break;
                subroutineB.add(text);

                i++;
            }


            // Convert from Strings to MoveItems and add to moveListB;
            convertToMoveList(moveListB, subroutineB);

            // Remove instances of subroutine B from moveList
            removeInstancesOfSubroutine(moveList, "B");

            break;
        }
    }

    private void getSubroutineAFromList(List<String> moveList) {
        List<String> subroutineA = new ArrayList<>();

        for (int i = 0; i < moveList.size(); i++) {
            String text = moveList.get(i);
            // If move is not part of B, go on to next move
            if (!text.contains("SA")) {
                continue;
            }

            // Add move to subroutine A list
            subroutineA.add(text);
            i++;

            // Loop through list ending when a new A starts or when move is not part of A
            while (i < moveList.size()) {
                text = moveList.get(i);

                if (text.contains("SA") || !text.contains("A"))
                    break;
                subroutineA.add(text);

                i++;
            }


            // Convert from Strings to MoveItems and add to moveListA;
            convertToMoveList(moveListA, subroutineA);

            // Remove instances of subroutine A from moveList
            removeInstancesOfSubroutine(moveList, "A");

            break;
        }
    }

    private void convertToMoveList(List<MoveItem> moveList, List<String> stringList) {
        ColorDrawable gray1 = new ColorDrawable(getResources().getColor(R.color.light_grey, null));
        ColorDrawable gray2 = new ColorDrawable(getResources().getColor(R.color.light_grey, null));
        ColorDrawable red = new ColorDrawable(getResources().getColor(R.color.indian_red, null));
        ColorDrawable blue = new ColorDrawable(getResources().getColor(R.color.steel_blue, null));

        for (String text : stringList) {
            MoveItem move;
            if (text.contains("UP"))
                move = new MoveItem("UP", gray1, gray2);
            else if (text.contains("DOWN"))
                move = new MoveItem("DOWN", gray1, gray2);
            else if (text.contains("LEFT"))
                move = new MoveItem("LEFT", gray1, gray2);
            else if (text.contains("RIGHT"))
                move = new MoveItem("RIGHT", gray1, gray2);
            else if (text.contains("B"))
                move = new MoveItem("B", gray1, blue);
            else if (text.contains("A"))
                move = new MoveItem("A", gray1, red);
            else
                continue;

            moveList.add(move);
        }
    }

    private void removeInstancesOfSubroutine(List<String> stringList, String list) {
        int numCommands;

        if (list.equals("A"))
            numCommands = moveListA.size();
        else if (list.equals("B"))
            numCommands = moveListB.size();
        else
            return;

        for (int i = 0; i < stringList.size(); i++) {
            String move = stringList.get(i);
            if (move.contains("S" + list)) {
                stringList.add(i, move.substring(0, move.indexOf(list) - 1) + list);
                i++;
                for (int j = 0; j < numCommands; j++)
                    stringList.remove(i);
                i--;
            }
        }
    }

    private void updateBackendLists() {
        for (MoveItem m : moveListMain) {
            addCommandToBackend(m.getText(), ListName.MAIN);
        }

        for (MoveItem m : moveListA) {
            addCommandToBackend(m.getText(), ListName.A);
        }

        for (MoveItem m : moveListB) {
            addCommandToBackend(m.getText(), ListName.B);
        }
    }

    private void addCommandToBackend(String text, ListName listName) {
        switch(text.toLowerCase()) {
            case "up": levelController.addUpCommand(listName); break;
            case "down": levelController.addDownCommand(listName); break;
            case "left": levelController.addLeftCommand(listName); break;
            case "right": levelController.addRightCommand(listName); break;
            case "a": levelController.addSubroutineA(listName); break;
            case "b": levelController.addSubroutineB(listName); break;
        }
    }

    /// END PLAYABLE UI METHODS

    public void playRoombaMoveSound() {
        mediaPlayer = MediaPlayer.create(this, R.raw.roombamove);
        mediaPlayer.start();
    }

    public void playRoombaCrashSound() {
        mediaPlayer = MediaPlayer.create(this, R.raw.roombacrash);
        mediaPlayer.start();
    }
}