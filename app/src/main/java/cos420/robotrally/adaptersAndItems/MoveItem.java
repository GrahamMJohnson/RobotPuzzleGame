package cos420.robotrally.adaptersAndItems;

/**
 * Model for adding data to a Move
 */
public class MoveItem {
    // TODO javadocs in here are lackluster, but they'll have to be rewritten when the moves are
    //  switched for images. Improving them isn't high priority.
    private String text;

    /**
     * Constructor
     * @param text
     */
    public MoveItem(String text) {
        this.text = text;
    }

    /**
     * Getter
     * @return
     */
    public String getText() {
        return text;
    }

    /**
     * Setter
     * @param text
     */
    public void setText(String text) {
        this.text = text;
    }
}
