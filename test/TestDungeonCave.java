import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import dungeonmodel.coordinates.CaveCoordinates;
import dungeonmodel.coordinates.Coordinates;
import dungeonmodel.dungeon.Cave;
import dungeonmodel.dungeon.DungeonCave;
import dungeonmodel.monster.Otyugh;
import dungeonmodel.treasure.Diamond;
import dungeonmodel.treasure.GemQuality;
import dungeonmodel.treasure.Ruby;
import dungeonmodel.treasure.Treasure;
import dungeonmodel.weapon.CrookedArrow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Class to test expected functionality of the DungeonCave class.
 */
public class TestDungeonCave {

  /**
   * Test expected exception with null coordinates.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testNullCoordinates() {
    new DungeonCave(null);
  }

  /**
   * Test creating empty cave with no treasure and no adjacents.
   */
  @Test
  public void testEmptyCaveCreation() {
    Cave emptyCave = new DungeonCave(new CaveCoordinates(0, 0));
    assertTrue(emptyCave.getAllAdjacent().isEmpty());
    assertTrue(emptyCave.viewTreasure().isEmpty());
    assertFalse(emptyCave.hasArrow());
    assertFalse(emptyCave.hasMonster());
    assertNull(emptyCave.removeArrow());
  }

  /**
   * Test adding treasure to a cave.
   */
  @Test
  public void testAddingTreasure() {
    List<Treasure> expectedTreasure = new ArrayList<>();
    Treasure ruby1 = new Ruby(GemQuality.AVERAGE);
    Treasure diamond1 = new Diamond(GemQuality.POOR);

    Cave someCave = new DungeonCave(new CaveCoordinates(0, 0));
    someCave.addTreasure(ruby1);
    expectedTreasure.add(ruby1);
    assertEquals(someCave.viewTreasure(), expectedTreasure);
    someCave.addTreasure(diamond1);
    expectedTreasure.add(diamond1);
    assertEquals(someCave.viewTreasure(), expectedTreasure);
  }

  /**
   * Test adding arrow to cave.
   */
  @Test
  public void testAddingArrow() {
    Cave someCave = new DungeonCave(new CaveCoordinates(0, 0));
    someCave.addArrow(new CrookedArrow());
    assertTrue(someCave.hasArrow());
  }

  /**
   * Test adding a monster to a cave.
   */
  @Test
  public void testAddingMonster() {
    Cave someCave = new DungeonCave(new CaveCoordinates(0, 0));
    someCave.addMonster(new Otyugh(someCave.getCoordinates()));
    assertTrue(someCave.hasMonster());
  }

  /**
   * Test expected exception when adding two monsters to same cave.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testAddingTwoMonsters() {
    Cave someCave = new DungeonCave(new CaveCoordinates(0, 0));
    someCave.addMonster(new Otyugh(someCave.getCoordinates()));
    assertTrue(someCave.hasMonster());
    someCave.addMonster(new Otyugh(someCave.getCoordinates()));
  }

  /**
   * Test expected exception when adding two arrows to same cave.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testAddingTwoArrows() {
    Cave someCave = new DungeonCave(new CaveCoordinates(0, 0));
    someCave.addArrow(new CrookedArrow());
    assertTrue(someCave.hasArrow());
    someCave.addArrow(new CrookedArrow());
  }

  /**
   * Test expected exception when adding null monsters.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testAddingNullMonsters() {
    Cave someCave = new DungeonCave(new CaveCoordinates(0, 0));
    someCave.addMonster(null);
  }

  /**
   * Test expected exception when adding null arrows.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testAddingNullArrows() {
    Cave someCave = new DungeonCave(new CaveCoordinates(0, 0));
    someCave.addArrow(null);
  }

  /**
   * Test removing arrow from a cave.
   */
  @Test
  public void testRemoveArrow() {
    Cave someCave = new DungeonCave(new CaveCoordinates(0, 0));
    someCave.addArrow(new CrookedArrow());
    assertTrue(someCave.hasArrow());
    assertEquals(new CrookedArrow(), someCave.removeArrow());
    assertFalse(someCave.hasArrow());
    assertNull(someCave.removeArrow());
  }

  /**
   * Test removing treasure from a cave.
   */
  @Test
  public void testRemovingTreasure() {
    List<Treasure> expectedTreasure = new ArrayList<>();
    Treasure ruby1 = new Ruby(GemQuality.AVERAGE);
    Treasure diamond1 = new Diamond(GemQuality.POOR);

    Cave someCave = new DungeonCave(new CaveCoordinates(0, 0));
    someCave.addTreasure(ruby1);
    expectedTreasure.add(ruby1);
    assertEquals(someCave.viewTreasure(), expectedTreasure);
    someCave.addTreasure(diamond1);
    expectedTreasure.add(diamond1);
    assertEquals(someCave.viewTreasure(), expectedTreasure);
    assertEquals(someCave.removeTreasure(), expectedTreasure);
    assertTrue(someCave.viewTreasure().isEmpty());
    assertTrue(someCave.removeTreasure().isEmpty());
  }

  /**
   * Test expected exception when adding null treasure.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testAddingNullTreasure() {
    Cave someCave = new DungeonCave(new CaveCoordinates(0, 0));
    someCave.addTreasure(null);
  }

  /**
   * Test adding invalid/over the bound coordinates for max row and column.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testAddInvalidAdjacentLargeIndex() {
    Cave someCave = new DungeonCave(new CaveCoordinates(0, 0));
    someCave.setAdjacent(new CaveCoordinates(5, 5), 5, 5);
  }

  /**
   * Test expected exception adding adjacent which is two cells away.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testAddingInvalidAdjacentTooFarAway() {
    Cave someCave = new DungeonCave(new CaveCoordinates(0, 0));
    someCave.setAdjacent(new CaveCoordinates(2, 0), 5, 5);
  }

  /**
   * Test expected exception adding adjacent of same coordinates.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testAddingInvalidAdjacentSameCoords() {
    Cave someCave = new DungeonCave(new CaveCoordinates(0, 0));
    someCave.setAdjacent(new CaveCoordinates(0, 0), 5, 5);
  }

  /**
   * Test adding non wrapping adjacent elements.
   */
  @Test
  public void testAddingNonWrappingAdjacent() {
    Cave someCave = new DungeonCave(new CaveCoordinates(2, 2));
    Coordinates coord1 = new CaveCoordinates(1, 2);
    Coordinates coord2 = new CaveCoordinates(2, 1);
    Coordinates coord3 = new CaveCoordinates(3, 2);
    Coordinates coord4 = new CaveCoordinates(2, 3);
    List<Coordinates> expected = new ArrayList<>();
    someCave.setAdjacent(coord1, 5, 5);
    expected.add(coord1);
    assertTrue(someCave.getAllAdjacent().containsAll(expected)
            && expected.containsAll(someCave.getAllAdjacent()));
    someCave.setAdjacent(coord2, 5, 5);
    expected.add(coord2);
    assertTrue(someCave.getAllAdjacent().containsAll(expected)
            && expected.containsAll(someCave.getAllAdjacent()));
    someCave.setAdjacent(coord3, 5, 5);
    expected.add(coord3);
    assertTrue(someCave.getAllAdjacent().containsAll(expected)
            && expected.containsAll(someCave.getAllAdjacent()));
    someCave.setAdjacent(coord4, 5, 5);
    expected.add(coord4);
    assertTrue(someCave.getAllAdjacent().containsAll(expected)
            && expected.containsAll(someCave.getAllAdjacent()));
  }

  /**
   * Test adding wrapping adjacent elements.
   */
  @Test
  public void testAddingWrappingAdjacent() {
    Cave cave1 = new DungeonCave(new CaveCoordinates(0, 0));
    Coordinates wrapping1 = new CaveCoordinates(4, 0);
    cave1.setAdjacent(wrapping1, 5, 5);
    assertTrue(cave1.getAllAdjacent().contains(wrapping1));
    Coordinates wrapping2 = new CaveCoordinates(0, 4);
    cave1.setAdjacent(wrapping2, 5, 5);
    assertTrue(cave1.getAllAdjacent().contains(wrapping2));
    Cave cave2 = new DungeonCave(new CaveCoordinates(0, 4));
    Coordinates wrapping3 = new CaveCoordinates(0, 0);
    cave2.setAdjacent(wrapping3, 5, 5);
    assertTrue(cave2.getAllAdjacent().contains(wrapping3));
    Coordinates wrapping4 = new CaveCoordinates(4, 4);
    cave2.setAdjacent(wrapping4, 5, 5);
    assertTrue(cave2.getAllAdjacent().contains(wrapping4));
  }

  /**
   * Test gettting coordinates from created cave.
   */
  @Test
  public void testGetCoordinates() {
    Cave cave1 = new DungeonCave(new CaveCoordinates(0, 0));
    assertEquals(0, cave1.getCoordinates().getXCoordinates());
    assertEquals(0, cave1.getCoordinates().getYCoordinates());

    Cave cave2 = new DungeonCave(new CaveCoordinates(3, 2));
    assertEquals(3, cave2.getCoordinates().getXCoordinates());
    assertEquals(2, cave2.getCoordinates().getYCoordinates());

    Cave cave3 = new DungeonCave(new CaveCoordinates(6, 6));
    assertEquals(6, cave3.getCoordinates().getXCoordinates());
    assertEquals(6, cave3.getCoordinates().getYCoordinates());

    Cave cave4 = new DungeonCave(new CaveCoordinates(9, 9));
    assertEquals(9, cave4.getCoordinates().getXCoordinates());
    assertEquals(9, cave4.getCoordinates().getYCoordinates());

    Cave cave5 = new DungeonCave(new CaveCoordinates(4, 1));
    assertEquals(4, cave5.getCoordinates().getXCoordinates());
    assertEquals(1, cave5.getCoordinates().getYCoordinates());
  }

  /**
   * Test expected exception when adding adjacent with negative row.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testSettingAdjacentWithNegativeRowCount() {
    Cave someCave = new DungeonCave(new CaveCoordinates(2, 2));
    Coordinates coord1 = new CaveCoordinates(1, 2);
    someCave.setAdjacent(coord1, -1, 5);
  }

  /**
   * Test expected exception when adding adjacent with negative column.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testSettingAdjacentWithNegativeColumnCount() {
    Cave someCave = new DungeonCave(new CaveCoordinates(2, 2));
    Coordinates coord1 = new CaveCoordinates(1, 2);
    someCave.setAdjacent(coord1, 5, -3);
  }

  /**
   * Test expected exception when adding null adjacent.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testSettingNullCoordinateAdjacent() {
    Cave someCave = new DungeonCave(new CaveCoordinates(2, 2));
    someCave.setAdjacent(null, 3, 3);
  }

  /**
   * Test creating a tunnel (2 adjacent caves.)
   */
  @Test
  public void testTunnelCreation() {
    Cave someCave = new DungeonCave(new CaveCoordinates(2, 2));
    someCave.setAdjacent(new CaveCoordinates(1, 2), 3, 3);
    someCave.setAdjacent(new CaveCoordinates(2, 1), 3, 3);
    assertTrue(someCave.isTunnel());
    assertEquals(2, someCave.getAllAdjacent().size());
  }

  /**
   * Test exception when treasure is tried to be added to tunnel.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testAddingTreasureToTunnel() {
    Cave someCave = new DungeonCave(new CaveCoordinates(2, 2));
    someCave.setAdjacent(new CaveCoordinates(1, 2), 3, 3);
    someCave.setAdjacent(new CaveCoordinates(2, 1), 3, 3);
    assertTrue(someCave.isTunnel());
    assertEquals(2, someCave.getAllAdjacent().size());
    someCave.addTreasure(new Diamond(GemQuality.POOR));
  }

  /**
   * Test that reset cave works as expected.
   */
  @Test
  public void testResetCave() {
    Cave someCave = new DungeonCave(new CaveCoordinates(2, 2));
    someCave.setAdjacent(new CaveCoordinates(1, 2), 4, 4);
    someCave.setAdjacent(new CaveCoordinates(2, 1), 4, 4);
    someCave.setAdjacent(new CaveCoordinates(2, 3), 4 , 4);
    assertEquals(3, someCave.getAllAdjacent().size());
    someCave.addTreasure(new Diamond(GemQuality.POOR));
    someCave.addArrow(new CrookedArrow());
    someCave.addMonster(new Otyugh(someCave.getCoordinates()));
    assertTrue(someCave.hasArrow());
    assertTrue(someCave.hasMonster());
    assertFalse(someCave.viewTreasure().isEmpty());

    someCave.resetCave();
    assertFalse(someCave.hasArrow());
    assertFalse(someCave.hasMonster());
    assertTrue(someCave.viewTreasure().isEmpty());
  }
}
