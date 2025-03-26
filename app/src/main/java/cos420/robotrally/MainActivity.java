package cos420.robotrally;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cos420.robotrally.adaptersAndItems.MoveAdapter;
import cos420.robotrally.adaptersAndItems.MoveItem;
import cos420.robotrally.levels.LevelData;
import cos420.robotrally.models.LevelController;
import cos420.robotrally.services.LevelMapper;

public class MainActivity extends AppCompatActivity {
    List<MoveItem> moveList;
    MoveAdapter adapter;
    LevelController levelController;

    /**
     * 0 indexed list of the data for level layout<br>
     * Level 1 can be accessed by levels.get(0)
     */
    List<LevelData> levels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         //Moves view set up
        RecyclerView recyclerView = findViewById(R.id.move_viewer);
        moveList = new ArrayList<>();
        //Set LayoutManager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //Set Adapter
        adapter = new MoveAdapter(moveList);
        recyclerView.setAdapter(adapter);
        //Set ItemTouchHelper
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        // list of data for all the levels
        levels = LevelMapper.mapLevelDataFromFile(this);

        // TODO make level dynamic
        levelController = new LevelController(levels.get(0));
        setupGUIButtons(levelController);
    }

    /**
     * Adds new moves to the back-end, and adds them to the UI if no errors occur.
     *
     * @implNote  Present here rather than the level controller because findViewById
     * was difficult to access within the controller.
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
        //Delete
        findViewById(R.id.delete_button).setOnClickListener(v -> {

            try {
                levelC.remove();
                removeMoveFromUI();
            } catch (Exception e) {
                Log.d("Button Actions", "Attempted to remove item from empty list");
            }
        });
    }

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
        adapter.notifyDataSetChanged();//notify adapter of change
    }

    /**
     * Remove the last move item from the list
     */
    private void removeMoveFromUI () {
        moveList.remove(moveList.size() - 1); //Remove Last Item
        adapter.notifyDataSetChanged(); //notify adapter of change
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END, 0) {
        @Override //drag and drop moves
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();
            Collections.swap(moveList, fromPosition, toPosition);
            recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);
            //make changes to backend
            levelController.switchMove(fromPosition, toPosition);
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        }
    };

    private void showRobotHit(){
        //TODO: Test to see if this works
        //Still figuring out how to make the dialog show up.
        Dialog robotHitDialog = new Dialog(this);
        robotHitDialog.setContentView(R.layout.robot_hit_dialog);
    }

    private void addLevelToUI(int levelNumber){
        switch(levelNumber) {
            case 1:
            case 2:
            case 3:
                break;
            default: throw new InvalidParameterException(levelNumber + " is not a valid level number");
        }

        //create a new instance of the layout
        LayoutInflater levelButtonView = getLayoutInflater();
        View level = levelButtonView.inflate(R.layout.level_select_level_tile, null);

        //edit the text of the layout to have the level number
        androidx.appcompat.widget.AppCompatButton levelButton = findViewById(R.id.levelDynamic);
        levelButton.setText(levelNumber);

        //edit the stats/level rating
        //TODO: Figure out what to display (stars, efficiency rating, etc) and how to display it
        ImageView levelImage = findViewById(R.id.levelRatingDynamic);

        //add the edited level to the grid view
        GridView dynamicGrid = findViewById(R.id.dynamicLevelLayout);
        dynamicGrid.addView(level);
    }

    // TODO clear button listeners when done with level
}