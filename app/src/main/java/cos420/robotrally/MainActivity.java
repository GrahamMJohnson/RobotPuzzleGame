package cos420.robotrally;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.security.InvalidParameterException;

import cos420.robotrally.levels.TestLevelData;
import cos420.robotrally.models.LevelController;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TODO make level dynamic
        LevelController levelController = new LevelController(new TestLevelData());
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

        // Setup the element to be added
        // TODO change this to proper class and image
        TextView newMove = new TextView(this);
        newMove.setText(moveText);

        // Add the move to the scrollable list
        LinearLayout putMoveHere = findViewById(R.id.move_viewer);
        putMoveHere.addView(newMove);
    }

    // TODO clear button listeners when done with level
}