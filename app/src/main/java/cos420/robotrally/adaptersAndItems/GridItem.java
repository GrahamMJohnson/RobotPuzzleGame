package cos420.robotrally.adaptersAndItems;

import android.graphics.drawable.Drawable;

/**
 * model for adding data to a grid tile
 */
public class GridItem {
    private String text;
    private Drawable color;

    /**
     * Constructor
     * @param text
     */
    public GridItem(String text, Drawable color) {
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
}
