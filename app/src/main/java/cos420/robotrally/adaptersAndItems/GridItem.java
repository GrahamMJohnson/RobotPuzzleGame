package cos420.robotrally.adaptersAndItems;

import android.graphics.drawable.Drawable;

/**
 * model for adding data to a grid tile
 */
public class GridItem {
    // TODO javadoc
    private String text;
    // TODO javadoc
    private Drawable color;

    // TODO javadoc could be improved
    /**
     * Constructor
     * @param text
     */
    public GridItem(String text, Drawable color) {
        this.text = text;
        this.color = color;
    }

    // TODO javadoc could be improved
    /**
     * Getter
     * @return
     */
    public String getText() {
        return text;
    }

    // TODO javadoc
    public Drawable getColor() { return color;}
}
