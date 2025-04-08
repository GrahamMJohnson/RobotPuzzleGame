package cos420.robotrally.adaptersAndItems;

import android.graphics.drawable.Drawable;

/**
 * Model for adding data to a Move
 */
public class MoveItem {
    // TODO javadocs in here are lackluster, but they'll have to be rewritten when the moves are
    //  switched for images. Improving them isn't high priority.
    private String text;
    private Drawable color;

    /**
     * Constructor
     * @param text move text
     * @param color background color
     */
    public MoveItem(String text, Drawable color) {
        this.text = text;
        this.color = color;
    }

    /**
     * Getter
     * @return
     */
    public String getText() {
        return text;
    }

    public Drawable getColor() { return color;}

    public void setColor(Drawable color) { this.color = color;}
}
