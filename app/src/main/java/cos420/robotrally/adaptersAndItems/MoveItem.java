package cos420.robotrally.adaptersAndItems;

/**
 * Model for adding data to a Move
 */
public class MoveItem {
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
