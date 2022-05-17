package dungeonmodel.coordinates;

/**
 * Represents a set of coordinates that can exist in a dungeon in (x, y) format.
 * x represents the row, starting from the top and y represents the column, starting from the
 * left.
 */
public interface Coordinates {
  /**
   * Get the x value of a coordinate.
   *
   * @return x value of coordinate
   */
  int getXCoordinates();

  /**
   * Get the y value of a coordinate.
   *
   * @return y value of coordinate
   */
  int getYCoordinates();
}
