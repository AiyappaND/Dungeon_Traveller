import org.junit.Test;

import dungeonmodel.coordinates.CaveCoordinates;
import dungeonmodel.coordinates.Coordinates;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Class to test expected functionality of a coordinate set.
 */
public class TestCoordinates {

  /**
   * Helper method which returns a Coordinate object with the given row and column value.
   *
   * @param x row of coordinate
   * @param y column of coordinate
   * @return CaveCoordinate with given value
   */
  protected Coordinates xycoord(int x, int y) {
    return new CaveCoordinates(x, y);
  }

  /**
   * Test expected exception with negative row.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testNegativeX() {
    xycoord(-1, 5);
  }

  /**
   * Test expected exception with negative column.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testNegativeY() {
    xycoord(1, -5);
  }

  /**
   * Test expected coordinate creation.
   */
  @Test
  public void testCoordinateCreation() {
    Coordinates xy1 = xycoord(0, 0);
    Coordinates xy2 = xycoord(3, 5);
    Coordinates xy3 = xycoord(4, 7);

    assertEquals("(0, 0)", xy1.toString());
    assertEquals("(3, 5)", xy2.toString());
    assertEquals("(4, 7)", xy3.toString());

    assertEquals(0, xy1.getXCoordinates());
    assertEquals(0, xy1.getYCoordinates());

    assertEquals(3, xy2.getXCoordinates());
    assertEquals(5, xy2.getYCoordinates());

    assertEquals(4, xy3.getXCoordinates());
    assertEquals(7, xy3.getYCoordinates());
  }

  /**
   * Test overridden equality and hashcode methods.
   */
  @Test
  public void testEqualityAndHashcode() {
    Coordinates xy1 = xycoord(0, 0);
    Coordinates xy2 = xycoord(3, 5);
    Coordinates xy3 = xycoord(4, 7);

    Coordinates xy4 = xycoord(4, 7);
    Coordinates xy5 = xycoord(0, 0);

    assertEquals(xy1, xy1);
    assertEquals(xy1.hashCode(), xy1.hashCode());

    assertNotEquals(xy2, xy1);

    assertEquals(xy3, xy4);
    assertEquals(xy3.hashCode(), xy4.hashCode());

    assertEquals(xy5, xy1);
    assertEquals(xy1.hashCode(), xy5.hashCode());
  }
}
