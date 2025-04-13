package cos420.robotrally.adaptersAndItems;

/**
 * model for adding data to a grid tile
 */
public class GridItem {
    private int image;
    /**
     * Constructor
     * @param image
     */
    public GridItem(int image) {
        this.image = image;
    }

    /**
     * Getter for image on tile
     * @return
     */
    public int getImage() {
        return image;
    }

    /**
     * Setter for image on tile
     * @param image
     */
    public void setImage(int image) {
        this.image = image;
    }
}
