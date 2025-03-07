package cos420.robotrally.models;

public class Collectable {
    /** Row index of the collectable */
    private final int row;

    /** column index of the collectable */
    private final int column;

    /**
     * Create a new Collectable
     * @param row the row the collectable is in
     * @param column the column the collectable is in
     */
    public Collectable(int row, int column) {
        this.row = row;
        this.column = column;
    }

    /**
     * get the row of the collectable
     * @return int -> the row of the collectable
     */
    public int getRow() {
        return row;
    }

    /**
     * get the column of the collectable
     * @return int -> the column of the collectable
     */
    public int getColumn() {
        return column;
    }
}
