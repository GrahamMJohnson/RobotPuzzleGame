package cos420.robotrally.adaptersAndItems;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;

/**
 * Model for adding data to a Move
 */
public class MoveItem {
    // TODO javadocs in here are lackluster, but they'll have to be rewritten when the moves are
    //  switched for images. Improving them isn't high priority.
    private String text;
    private Drawable color;

    private Drawable buttonColor;
    private final Drawable defaultButtonColor;
    private String subroutineType;

    /**
     * Constructor
     * @param text move text
     * @param color background color
     */
    public MoveItem(String text, Drawable color, Drawable buttonColor) {
        this.text = text;
        this.color = color;
        this.buttonColor = buttonColor;
        this.defaultButtonColor = buttonColor;
        this.subroutineType = null;
    }

    /**
     * Getter
     * @return The text value for this move item
     */
    public String getText() {
        return text;
    }

    /**
     * Method to update the text value of move item
     * @param value The new value to store in text
     */
    public void setText(String value) {
        text = value;
    }

    public Drawable getColor() { return color;}

    public int getColorInt() {
        if (buttonColor instanceof ColorDrawable) {
            return ((ColorDrawable) buttonColor).getColor();
        } else {
            throw new IllegalStateException("Drawable is not a ColorDrawable");
        }
    }

    public void setColor(Drawable color) { this.color = color;}

    public void setButtonColor(Drawable buttonColor) {
        this.buttonColor = buttonColor;
    }

    public void clearHighlight() {
        buttonColor = defaultButtonColor;
    }

    /**
     * Method to get the subroutine command is associated to
     * @return String with A or B, null if part of main list
     */
    public String getSubroutineType() {
        return subroutineType;
    }

    /**
     * Method to set the subroutine this command is associated with
     * @param subroutine String, either A or B, for subroutine or AB for subroutine B nested in A
     *                   <br>will be set to null if not a or b
     */
    public void setSubroutineType(String subroutine) {
        switch (subroutine) {
            case "A":
            case "B":
            case "AB":
                this.subroutineType = subroutine;
                break;
            case "SAB":
                this.subroutineType = "AB";
            default: break;
        }
    }

}
