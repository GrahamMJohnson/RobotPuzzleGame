package cos420.robotrally.adaptersAndItems;

import cos420.robotrally.R;

public class AttemptItem {
    private int attemptNumber;
    private int numberOfMoves;
    private String success;
    private int successColor;

    public AttemptItem(int attemptNumber, int numberOfMoves, boolean success) {
        this.attemptNumber = attemptNumber;
        this.numberOfMoves = numberOfMoves;

        if (success) {//If success, make it green
            this.success = "Success";
            successColor = R.color.lime_green;
        }else {//if failure, make it red
            this.success = "Failure";
            successColor = R.color.fire_brick;
        }
    }

    public int getAttemptNumber() {
        return attemptNumber;
    }

    public int getNumberOfMoves() {
        return numberOfMoves;
    }

    public String getSuccess() {
        return success;
    }

    public int getSuccessColor() {
        return successColor;
    }
}
