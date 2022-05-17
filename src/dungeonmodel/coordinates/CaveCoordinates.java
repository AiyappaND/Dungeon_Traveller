package dungeonmodel.coordinates;

import java.util.Objects;

/**
 * Class representing coordinates of a cave that is present in a dungeon.
 * Contains x: row and y:column attributes.
 */
public class CaveCoordinates implements Coordinates {
  private final int x;
  private final int y;

  /**
   * Constructor to create a CaveCoordinate object.
   *
   * @param x row value of the coordinate
   * @param y column value of the coordinate
   * @throws IllegalArgumentException if either x/y are negative
   */
  public CaveCoordinates(int x, int y) throws IllegalArgumentException {

    if (x < 0 || y < 0) {
      throw new IllegalArgumentException("Coordinate values cannot be negative");
    }

    this.x = x;
    this.y = y;
  }

  @Override
  public int getXCoordinates() {
    return this.x;
  }

  @Override
  public int getYCoordinates() {
    return this.y;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CaveCoordinates that = (CaveCoordinates) o;
    return (x == that.x && y == that.y);
  }

  @Override
  public int hashCode() {
    return Objects.hash(x, y);
  }

  @Override
  public String toString() {
    return String.format("(%d, %d)", this.x, this.y);
  }
}
