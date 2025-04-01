package cos420.robotrally.adaptersAndItems;

import android.health.connect.datatypes.units.Percentage;

/**
 * LevelItems are items to be shown in the level select menu.
 */
public class LevelItem {

    /**
     * The id of the level.
     */
    final private int ID;
    /** Percentage of the bar that should be filled */
    private double percentage;

    /**
     * Constructor for LevelItems
     * @param id        of the level. 0-based.
     * @param percent   of the completion bar that should be filled.<br>
     *                 1 = 100%, 0 = 0%, .5 = 50%, etc.
     */
    public LevelItem(int id, double percent) {
        this.ID = id;
        this.percentage = percent;
    }

    /// GETTERS

    /**
     * @return  the 0-based ID of the level.
     */
    public int getID() { return ID; }

    /**
     * @return  The string that should be displayed to the user.
     */
    public String getDisplayName() { return String.valueOf(ID + 1); }
    /**
     * @return  the percentage of the completion bar that should be filled
     */
    public double getPercent() { return percentage; }

    /// SETTERS

    /**
     * Update the percentage the bar should display.
     * @param percent   the bar should now be
     */
    public void setPercent(double percent) { this.percentage = percent; }
}
