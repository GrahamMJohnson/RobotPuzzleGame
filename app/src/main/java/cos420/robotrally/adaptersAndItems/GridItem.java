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
     * The angle the image should be rotated.
     * This is ignored if gif.
     */
    private int rotation;
    /**
     * So the adapter knows if it's a gif
     */
    private boolean isGif;

    /** The row of the item (needed to convert obstacles from gif to static) */
    private int row;
    /** The col of the item (needed to convert obstacles from gif to static) */
    private int col;

    /**
     * Constructor
     * @param image
     * @param rotation
     * @param isGif
     * @param row   (only useful for obstacles)
     * @param col   (only useful for obstacles)
     */
    public GridItem(int image, int rotation, boolean isGif, int row, int col) {
        this.image = image;
        this.rotation = rotation;
        this.isGif = isGif;
        this.row = row;
        this.col = col;
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

    /**
     * Return the number of degrees the image should be rotated
     * @return
     */
    public int getRotation() { return rotation; }

    /**
     * @return row of griditem
     */
    public int getRow() {return row;}

    /**
     * @return col of griditem
     */
    public int getCol() {return col;}
}
