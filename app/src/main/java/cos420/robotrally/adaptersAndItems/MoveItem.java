package cos420.robotrally.adaptersAndItems;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;

import androidx.annotation.ColorInt;

/**
 * Model for adding data to a Move
 */
public class MoveItem {
    // TODO javadocs in here are lackluster, but they'll have to be rewritten when the moves are
    //  switched for images. Improving them isn't high priority.
    private String text;
    private Drawable color;

    private Drawable buttonColor;

    /**
     * Constructor
     * @param text move text
     * @param color background color
     */
    public MoveItem(String text, Drawable color, Drawable buttonColor) {
        this.text = text;
        this.color = color;
        this.buttonColor = buttonColor;
    }

    /**
     * Getter
     * @return
     */
    public String getText() {
        return text;
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
}
