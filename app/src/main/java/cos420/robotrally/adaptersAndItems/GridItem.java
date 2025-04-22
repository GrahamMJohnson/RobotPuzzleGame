package cos420.robotrally.adaptersAndItems;

/**
 * model for adding data to a grid tile
 */
public class GridItem {
    /**
     * The image
     */
    private int image;
    /**
     * So the adapter knows if it's a gif
     */
    private boolean isGif;
    /**
     * Constructor
     * @param image
     */
    public GridItem(int image, boolean isGif) {
        this.image = image;
        this.isGif = isGif;
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

    /**
     * Gets whether the image is a gif or not
     * @return
     */
    public boolean isGif() {
        return isGif;
    }
}
