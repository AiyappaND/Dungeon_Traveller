import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import dungeonmodel.coordinates.CaveCoordinates;
import dungeonmodel.coordinates.Coordinates;
import dungeonmodel.dungeon.Dungeon;
import dungeonmodel.dungeon.DungeonImpl;
import dungeonmodel.treasure.Treasure;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Class to test expected functionality of a Dungeon.
 */
public class TestDungeonImpl {

  /**
   * Test expected exception when creating dungeon with negative row count.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testDungeonWithNegativeRows() {
    new DungeonImpl(-1, 5, 0, true, 20, 1);
  }

  /**
   * Test expected exception when creating dungeon with negative column count.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testDungeonWithNegativeColumns() {
    new DungeonImpl(5, -5, 0, true, 20, 1);
  }

  /**
   * Test expected exception when creating dungeon with negative interconnectivity.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testDungeonWithNegativeInterconnectivity() {
    new DungeonImpl(5, 5, -2, true, 20, 1);
  }

  /**
   * Test expected exception when creating dungeon with negative treasure percentage.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testDungeonWithNegativeTreasure() {
    new DungeonImpl(5, 5, 2, true, -5, 1);
  }

  /**
   * Test expected exception when creating dungeon with zero row count.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testDungeonWithZeroRows() {
    new DungeonImpl(0, 5, 0, true, 20, 1);
  }

  /**
   * Test expected exception when creating dungeon with zero column count.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testDungeonWithZeroColumns() {
    new DungeonImpl(5, 0, 0, true, 20, 1);
  }

  /**
   * Test expected exception when creating dungeon with zero treasure percentage.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testDungeonWithZeroTreasure() {
    new DungeonImpl(5, 5, 2, true, 0, 1);
  }

  /**
   * Test expected exception when creating dungeon with treasure percentage greater than hundred.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testDungeonWithTreasureMoreThanHundred() {
    new DungeonImpl(5, 5, 2, true, 101, 1);
  }

  /**
   * Test expected exception when interconnectivity is more than achievable for the dimensions.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testVeryHighInterconnectivity() {
    new DungeonImpl(5, 5, 30, true, 50, 1);
  }

  /**
   * Test expected exception when no path can exist with minimum length 5.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testMinPathAlwaysLesserThan5() {
    new DungeonImpl(2, 2, 0, false, 50, 1);
  }

  /**
   * Test expected exception when zero number of monsters are given.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testZeroMonsters() {
    new DungeonImpl(5, 5, 0, false, 50, 0);
  }

  /**
   * Test expected exception when negative number of monsters are given.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testNegativeMonsters() {
    new DungeonImpl(5, 5, 0, false, 50, -1);
  }

  /**
   * Test expected exception when number of given monsters are more than number of caves.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testMoreMonstersThanCaves() {
    new DungeonImpl(5, 5, 0, false, 50, 23);
  }

  /**
   * Test that dungeon has wrapping edges when required.
   */
  @Test
  public void testHasWrappingEdges() {
    for (int dungeonCount = 0; dungeonCount < 1000; dungeonCount++) {
      int randomRowCount = new Random().nextInt(15) + 5;
      int randomColCount = new Random().nextInt(15) + 5;
      Dungeon dungeon = new DungeonImpl(randomRowCount, randomColCount,
              0, true, 50, 1);
      int wrappingCount = 0;
      for (int column = 0; column < randomColCount; column++) {
        List<Coordinates> adjacent = dungeon.getAdjacent(new CaveCoordinates(0, column));
        for (Coordinates coo :
                adjacent) {
          if (coo.getXCoordinates() == randomRowCount - 1) {
            wrappingCount += 1;
          }
        }
      }

      for (int row = 0; row < randomRowCount; row++) {
        List<Coordinates> adjacent = dungeon.getAdjacent(new CaveCoordinates(row, 0));
        for (Coordinates coo :
                adjacent) {
          if (coo.getYCoordinates() == randomColCount - 1) {
            wrappingCount += 1;
          }
        }
      }
      assertTrue(wrappingCount > 0);
    }
  }

  /**
   * Test that dungeon has no wrapping edges when not required.
   */
  @Test
  public void testHasNoWrappingEdges() {
    for (int dungeonCount = 0; dungeonCount < 1000; dungeonCount++) {
      int randomRowCount = new Random().nextInt(15) + 5;
      int randomColCount = new Random().nextInt(15) + 5;
      Dungeon dungeon = new DungeonImpl(randomRowCount, randomColCount,
              0, false, 50, 1);
      int wrappingCount = 0;
      for (int column = 0; column < randomColCount; column++) {
        List<Coordinates> adjacent = dungeon.getAdjacent(new CaveCoordinates(0, column));
        for (Coordinates coo :
                adjacent) {
          if (coo.getXCoordinates() == randomRowCount - 1) {
            wrappingCount += 1;
          }
        }
      }

      for (int row = 0; row < randomRowCount; row++) {
        List<Coordinates> adjacent = dungeon.getAdjacent(new CaveCoordinates(row, 0));
        for (Coordinates coo :
                adjacent) {
          if (coo.getYCoordinates() == randomColCount - 1) {
            wrappingCount += 1;
          }
        }
      }
      assertEquals(0, wrappingCount);
    }
  }

  /**
   * Test dungeon always has start and end and that it is never a tunnel.
   */
  @Test
  public void testHasStartAndEnd() {
    for (int dungeonCount = 0; dungeonCount < 500; dungeonCount++) {
      int randomRowCount = new Random().nextInt(15) + 5;
      int randomColCount = new Random().nextInt(15) + 5;
      Dungeon dungeon = new DungeonImpl(randomRowCount, randomColCount,
              0, false, 50, 1);
      assertNotNull(dungeon.getStartCave());
      assertNotNull(dungeon.getEndCave());
      assertNotEquals(dungeon.getEndCave(), dungeon.getStartCave());
      assertNotEquals(2, dungeon.getAdjacent(dungeon.getStartCave()).size());
      assertNotEquals(2, dungeon.getAdjacent(dungeon.getEndCave()).size());
    }

    for (int dungeonCount = 0; dungeonCount < 500; dungeonCount++) {
      int randomRowCount = new Random().nextInt(15) + 5;
      int randomColCount = new Random().nextInt(15) + 5;
      Dungeon dungeon = new DungeonImpl(randomRowCount, randomColCount,
              2, true, 50, 1);
      assertNotNull(dungeon.getStartCave());
      assertNotNull(dungeon.getEndCave());
      assertNotEquals(dungeon.getEndCave(), dungeon.getStartCave());
    }
  }

  /**
   * Test that caves exist for all cells in the dungeon with given size.
   */
  @Test
  public void allCavesArePresent() {
    for (int dungeonCount = 0; dungeonCount < 500; dungeonCount++) {
      int randomRowCount = new Random().nextInt(15) + 5;
      int randomColCount = new Random().nextInt(15) + 5;
      Dungeon dungeon = new DungeonImpl(randomRowCount, randomColCount,
              0, false, 50, 1);
      Set<Coordinates> allCoordinates = new HashSet<>();
      for (int row = 0; row < randomRowCount; row++) {
        for (int column = 0; column < randomColCount; column++) {
          List<Coordinates> adj = dungeon.getAdjacent(new CaveCoordinates(row, column));
          allCoordinates.addAll(adj);
        }
      }
      assertEquals(randomColCount * randomRowCount, allCoordinates.size());
      for (int row = 0; row < randomRowCount; row++) {
        for (int column = 0; column < randomColCount; column++) {
          assertTrue(allCoordinates.contains(new CaveCoordinates(row, column)));
        }
      }
    }

    for (int dungeonCount = 0; dungeonCount < 500; dungeonCount++) {
      int randomRowCount = new Random().nextInt(15) + 5;
      int randomColCount = new Random().nextInt(15) + 5;
      Dungeon dungeon = new DungeonImpl(randomRowCount, randomColCount,
              0, true, 50, 1);
      Set<Coordinates> allCoordinates = new HashSet<>();
      for (int row = 0; row < randomRowCount; row++) {
        for (int column = 0; column < randomColCount; column++) {
          List<Coordinates> adj = dungeon.getAdjacent(new CaveCoordinates(row, column));
          allCoordinates.addAll(adj);
        }
      }
      assertEquals(randomColCount * randomRowCount, allCoordinates.size());
      for (int row = 0; row < randomRowCount; row++) {
        for (int column = 0; column < randomColCount; column++) {
          assertTrue(allCoordinates.contains(new CaveCoordinates(row, column)));
        }
      }
    }
  }

  /**
   * Test that all locations are connected to all other locations using BFS.
   */
  @Test
  public void testAllCavesAreConnectedToAllCaves() {
    for (int dungenCount = 0; dungenCount < 500; dungenCount++) {
      int randomRowCount = new Random().nextInt(15) + 5;
      int randomColCount = new Random().nextInt(15) + 5;
      Dungeon dungeon = new DungeonImpl(randomRowCount, randomColCount,
              0, false, 50, 1);
      Coordinates startCave = dungeon.getStartCave();

      // Breadth First Search
      List<Coordinates> toBeVisited = new ArrayList<>();
      List<Coordinates> alreadyVisited = new ArrayList<>();
      List<Coordinates> canBeReached = new ArrayList<>();
      toBeVisited.add(startCave);
      while (!toBeVisited.isEmpty()) {
        Coordinates current = toBeVisited.remove(0);
        alreadyVisited.add(current);
        if (!canBeReached.contains(current)) {
          canBeReached.add(current);
        }
        List<Coordinates> adjacents = dungeon.getAdjacent(current);
        for (Coordinates coo : adjacents) {
          if (!alreadyVisited.contains(coo)) {
            toBeVisited.add(coo);
          }
          if (!canBeReached.contains(coo)) {
            canBeReached.add(coo);
          }
        }
      }
      assertEquals(randomColCount * randomRowCount, canBeReached.size());
      for (int row = 0; row < randomRowCount; row++) {
        for (int column = 0; column < randomColCount; column++) {
          assertTrue(canBeReached.contains(new CaveCoordinates(row, column)));
        }
      }
    }

    for (int dungenCount = 0; dungenCount < 500; dungenCount++) {
      int randomRowCount = new Random().nextInt(15) + 5;
      int randomColCount = new Random().nextInt(15) + 5;
      Dungeon dungeon = new DungeonImpl(randomRowCount, randomColCount,
              0, true, 50, 1);
      Coordinates startCave = dungeon.getStartCave();

      // Breadth First Search
      List<Coordinates> toBeVisited = new ArrayList<>();
      List<Coordinates> alreadyVisited = new ArrayList<>();
      List<Coordinates> canBeReached = new ArrayList<>();
      toBeVisited.add(startCave);
      while (!toBeVisited.isEmpty()) {
        Coordinates current = toBeVisited.remove(0);
        alreadyVisited.add(current);
        if (!canBeReached.contains(current)) {
          canBeReached.add(current);
        }
        List<Coordinates> adjacents = dungeon.getAdjacent(current);
        for (Coordinates coo : adjacents) {
          if (!alreadyVisited.contains(coo)) {
            toBeVisited.add(coo);
          }
          if (!canBeReached.contains(coo)) {
            canBeReached.add(coo);
          }
        }
      }
      assertEquals(randomColCount * randomRowCount, canBeReached.size());
      for (int row = 0; row < randomRowCount; row++) {
        for (int column = 0; column < randomColCount; column++) {
          assertTrue(canBeReached.contains(new CaveCoordinates(row, column)));
        }
      }
    }
  }

  /**
   * Test that end cave is connected to start cave using BFS.
   */
  @Test
  public void testEndCavesConnectedToStartCave() {
    for (int dungenCount = 0; dungenCount < 500; dungenCount++) {
      int randomRowCount = new Random().nextInt(15) + 5;
      int randomColCount = new Random().nextInt(15) + 5;
      Dungeon dungeon = new DungeonImpl(randomRowCount, randomColCount,
              0, false, 50, 1);
      Coordinates startCave = dungeon.getStartCave();

      // Breadth First Search
      List<Coordinates> toBeVisited = new ArrayList<>();
      List<Coordinates> alreadyVisited = new ArrayList<>();
      List<Coordinates> canBeReached = new ArrayList<>();
      toBeVisited.add(startCave);
      boolean found = false;
      while (!toBeVisited.isEmpty() && !found) {
        Coordinates current = toBeVisited.remove(0);
        alreadyVisited.add(current);
        if (!canBeReached.contains(current)) {
          canBeReached.add(current);
        }
        List<Coordinates> adjacents = dungeon.getAdjacent(current);
        for (Coordinates coo : adjacents) {
          if (!alreadyVisited.contains(coo)) {
            toBeVisited.add(coo);
          }
          if (!canBeReached.contains(coo)) {
            if (dungeon.getEndCave().equals(coo)) {
              found = true;
            }
            canBeReached.add(coo);
          }
        }
      }
      assertTrue(found);
    }

    for (int dungenCount = 0; dungenCount < 500; dungenCount++) {
      int randomRowCount = new Random().nextInt(15) + 5;
      int randomColCount = new Random().nextInt(15) + 5;
      Dungeon dungeon = new DungeonImpl(randomRowCount, randomColCount,
              2, true, 50, 1);
      Coordinates startCave = dungeon.getStartCave();

      // Breadth First Search
      List<Coordinates> toBeVisited = new ArrayList<>();
      List<Coordinates> alreadyVisited = new ArrayList<>();
      List<Coordinates> canBeReached = new ArrayList<>();
      toBeVisited.add(startCave);
      boolean found = false;
      while (!toBeVisited.isEmpty() && !found) {
        Coordinates current = toBeVisited.remove(0);
        alreadyVisited.add(current);
        if (!canBeReached.contains(current)) {
          canBeReached.add(current);
        }
        List<Coordinates> adjacents = dungeon.getAdjacent(current);
        for (Coordinates coo : adjacents) {
          if (!alreadyVisited.contains(coo)) {
            toBeVisited.add(coo);
          }
          if (!canBeReached.contains(coo)) {
            if (dungeon.getEndCave().equals(coo)) {
              found = true;
            }
            canBeReached.add(coo);
          }
        }
      }
      assertTrue(found);
    }
  }

  /**
   * Test that start and end distance is at least 5 using BFS.
   */
  @Test
  public void testStartToEndPathMinimum5Length() {

    for (int dungenCount = 0; dungenCount < 500; dungenCount++) {
      int randomRowCount = new Random().nextInt(15) + 5;
      int randomColCount = new Random().nextInt(15) + 5;
      Dungeon dungeon = new DungeonImpl(randomRowCount, randomColCount,
              0, false, 50, 1);
      Coordinates startCave = dungeon.getStartCave();

      // Breadth First Search
      List<Coordinates> toBeVisited = new ArrayList<>();
      List<Coordinates> alreadyVisited = new ArrayList<>();
      List<Coordinates> canBeReached = new ArrayList<>();
      Map<Coordinates, Integer> distanceDict = new HashMap<>();
      toBeVisited.add(startCave);
      distanceDict.put(startCave, 0);
      int pathDistance = 0;
      boolean found = false;
      while (!toBeVisited.isEmpty() && !found) {
        Coordinates current = toBeVisited.remove(0);
        int currentDistance = distanceDict.get(current);
        alreadyVisited.add(current);
        if (!canBeReached.contains(current)) {
          canBeReached.add(current);
        }
        List<Coordinates> adjacents = dungeon.getAdjacent(current);
        for (Coordinates coo : adjacents) {
          if (!alreadyVisited.contains(coo)) {
            toBeVisited.add(coo);
          }
          if (!canBeReached.contains(coo)) {
            canBeReached.add(coo);
            if (!distanceDict.containsKey(coo)) {
              distanceDict.put(coo, currentDistance + 1);
            }
            if (dungeon.getEndCave().equals(coo)) {
              found = true;
              pathDistance = distanceDict.get(coo);
            }
          }
        }
      }
      assertTrue(found);
      assertTrue(pathDistance >= 5);
    }

    for (int dungenCount = 0; dungenCount < 500; dungenCount++) {
      int randomRowCount = new Random().nextInt(15) + 5;
      int randomColCount = new Random().nextInt(15) + 5;
      Dungeon dungeon = new DungeonImpl(randomRowCount, randomColCount,
              3, true, 50, 1);
      Coordinates startCave = dungeon.getStartCave();

      // Breadth First Search
      List<Coordinates> toBeVisited = new ArrayList<>();
      List<Coordinates> alreadyVisited = new ArrayList<>();
      List<Coordinates> canBeReached = new ArrayList<>();
      Map<Coordinates, Integer> distanceDict = new HashMap<>();
      toBeVisited.add(startCave);
      distanceDict.put(startCave, 0);
      int pathDistance = 0;
      boolean found = false;
      while (!toBeVisited.isEmpty() && !found) {
        Coordinates current = toBeVisited.remove(0);
        int currentDistance = distanceDict.get(current);
        alreadyVisited.add(current);
        if (!canBeReached.contains(current)) {
          canBeReached.add(current);
        }
        List<Coordinates> adjacents = dungeon.getAdjacent(current);
        for (Coordinates coo : adjacents) {
          if (!alreadyVisited.contains(coo)) {
            toBeVisited.add(coo);
          }
          if (!canBeReached.contains(coo)) {
            canBeReached.add(coo);
            if (!distanceDict.containsKey(coo)) {
              distanceDict.put(coo, currentDistance + 1);
            }
            if (dungeon.getEndCave().equals(coo)) {
              found = true;
              pathDistance = distanceDict.get(coo);
            }
          }
        }
      }
      assertTrue(found);
      assertTrue(pathDistance >= 5);
    }
  }

  /**
   * Test that all caves contain treasure and arrows if percentage is 100.
   */
  @Test
  public void containsFullTreasureAndArrows() {
    for (int dungeonCount = 0; dungeonCount < 500; dungeonCount++) {
      int randomRowCount = new Random().nextInt(15) + 5;
      int randomColCount = new Random().nextInt(15) + 5;
      Dungeon dungeon = new DungeonImpl(randomRowCount, randomColCount,
              3, true, 100, 1);
      for (int row = 0; row < randomRowCount; row++) {
        for (int col = 0; col < randomColCount; col++) {
          if (dungeon.getAdjacent(new CaveCoordinates(row, col)).size() != 2) {
            List<Treasure> treasureList = dungeon.viewTreasureInCave(new CaveCoordinates(row, col));
            assertFalse(treasureList.isEmpty());
          }
          assertTrue(dungeon.hasArrow(new CaveCoordinates(row, col)));
          assertNotNull(dungeon.pickUpArrow(new CaveCoordinates(row, col)));
        }
      }
    }
  }

  /**
   * Test that caves have almost the same percentage of treasure and arrows as expected.
   * Exact percentage is hard to achieve as number of caves may not always be exactly divisible.
   */
  @Test
  public void containsGivenTreasureAndArrows() {
    for (int dungeonCount = 0; dungeonCount < 5000; dungeonCount++) {
      double treasureCounter = 0;
      double arrowCounter = 0;
      double caveCounter = 0;
      double caveAndTunnelCounter = 0;
      int randomRowCount = new Random().nextInt(15) + 5;
      int randomColCount = new Random().nextInt(15) + 5;
      int randomPercent = new Random().nextInt(100) + 1;
      Dungeon dungeon = new DungeonImpl(randomRowCount, randomColCount,
              3, false, randomPercent, 1);
      for (int row = 0; row < randomRowCount; row++) {
        for (int col = 0; col < randomColCount; col++) {
          caveAndTunnelCounter += 1;
          if (dungeon.getAdjacent(new CaveCoordinates(row, col)).size() != 2) {
            caveCounter += 1;
            List<Treasure> treasureList = dungeon.viewTreasureInCave(new CaveCoordinates(row, col));
            if (!treasureList.isEmpty()) {
              treasureCounter += 1;
            }
          }
          if (dungeon.hasArrow(new CaveCoordinates(row, col))) {
            assertNotNull(dungeon.pickUpArrow(new CaveCoordinates(row, col)));
            assertFalse(dungeon.hasArrow(new CaveCoordinates(row, col)));
            arrowCounter += 1;
          }
        }
      }
      double foundTreasurePercent = (treasureCounter / caveCounter) * 100;
      assertEquals(randomPercent, foundTreasurePercent, 10.0);
      double foundArrowPercent = (arrowCounter / caveAndTunnelCounter) * 100;
      assertEquals(randomPercent, foundArrowPercent, 5.0);
    }
  }

  /**
   * Test that expected number of monsters are always present and that there is always a monster
   * in the end cave and that it is never in a tunnel.
   */
  @Test
  public void testMonsterCount() {
    for (int dungeonCount = 0; dungeonCount < 5000; dungeonCount++) {
      int monsterCounter = 0;
      int randomRowCount = new Random().nextInt(15) + 5;
      int randomColCount = new Random().nextInt(15) + 5;
      int randomPercent = new Random().nextInt(100) + 1;
      int randomMonsters = new Random().nextInt(5) + 1;
      Dungeon dungeon = new DungeonImpl(randomRowCount, randomColCount,
              3, false, randomPercent, randomMonsters);
      for (int row = 0; row < randomRowCount; row++) {
        for (int col = 0; col < randomColCount; col++) {
          if (dungeon.getAdjacent(new CaveCoordinates(row, col)).size() != 2) {
            if (dungeon.hasMonster(new CaveCoordinates(row, col))) {
              monsterCounter += 1;
            }
          }
          else {
            assertFalse(dungeon.hasMonster(new CaveCoordinates(row, col)));
          }
        }
      }
      assertEquals(randomMonsters, monsterCounter);
      assertTrue(dungeon.hasMonster(dungeon.getEndCave()));
      assertFalse(dungeon.hasMonster(dungeon.getStartCave()));
    }

    for (int dungeonCount = 0; dungeonCount < 5000; dungeonCount++) {
      int monsterCounter = 0;
      int randomRowCount = new Random().nextInt(15) + 5;
      int randomColCount = new Random().nextInt(15) + 5;
      int randomPercent = new Random().nextInt(100) + 1;
      int randomMonsters = new Random().nextInt(5) + 1;
      Dungeon dungeon = new DungeonImpl(randomRowCount, randomColCount,
              3, true, randomPercent, randomMonsters);
      for (int row = 0; row < randomRowCount; row++) {
        for (int col = 0; col < randomColCount; col++) {
          if (dungeon.getAdjacent(new CaveCoordinates(row, col)).size() != 2) {
            if (dungeon.hasMonster(new CaveCoordinates(row, col))) {
              monsterCounter += 1;
            }
          }
        }
      }
      assertEquals(randomMonsters, monsterCounter);
      assertTrue(dungeon.hasMonster(dungeon.getEndCave()));
    }
  }

  /**
   * Test that dimensions of dungeon are as provided.
   */
  @Test
  public void testDimensions() {
    for (int dungeonCount = 0; dungeonCount < 500; dungeonCount++) {
      Integer randomRowCount = new Random().nextInt(15) + 5;
      Integer randomColCount = new Random().nextInt(15) + 5;
      Dungeon dungeon = new DungeonImpl(randomRowCount, randomColCount,
              3, true, 100, 1);
      assertEquals(randomRowCount, dungeon.getDimensions().get(0));
      assertEquals(randomColCount, dungeon.getDimensions().get(1));
    }
  }

  /**
   * Test that viewing and removing treasure works as expected.
   */
  @Test
  public void testViewAndRemoveTreasure() {
    for (int dungeonCount = 0; dungeonCount < 5000; dungeonCount++) {
      int randomRowCount = new Random().nextInt(15) + 5;
      int randomColCount = new Random().nextInt(15) + 5;
      int randomPercent = new Random().nextInt(100) + 1;
      Dungeon dungeon = new DungeonImpl(randomRowCount, randomColCount,
              3, false, randomPercent, 1);
      for (int row = 0; row < randomRowCount; row++) {
        for (int col = 0; col < randomColCount; col++) {
          if (dungeon.getAdjacent(new CaveCoordinates(row, col)).size() != 2) {
            List<Treasure> treasureList = dungeon.viewTreasureInCave(new CaveCoordinates(row, col));
            if (!treasureList.isEmpty()) {
              assertEquals(treasureList, dungeon.pickupTreasureInCave(
                      new CaveCoordinates(row, col)));
              assertTrue(dungeon.viewTreasureInCave(new CaveCoordinates(row, col)).isEmpty());
            }
          }
        }
      }
    }
  }

  /**
   * Test that reset dungeon works as expected.S
   */
  @Test
  public void testResetDungeon() {
    for (int dungeonCount = 0; dungeonCount < 5000; dungeonCount++) {
      double treasureCounter = 0;
      double arrowCounter = 0;
      double caveCounter = 0;
      double caveAndTunnelCounter = 0;
      int randomRowCount = new Random().nextInt(15) + 5;
      int randomColCount = new Random().nextInt(15) + 5;
      int randomPercent = new Random().nextInt(100) + 1;
      int randomMonsters = new Random().nextInt(5) + 1;
      Dungeon dungeon = new DungeonImpl(randomRowCount, randomColCount,
              3, false, randomPercent, randomMonsters);
      Coordinates start1 = dungeon.getStartCave();
      Coordinates end1 = dungeon.getEndCave();
      for (int row = 0; row < randomRowCount; row++) {
        for (int col = 0; col < randomColCount; col++) {
          caveAndTunnelCounter += 1;
          if (dungeon.getAdjacent(new CaveCoordinates(row, col)).size() != 2) {
            caveCounter += 1;
            List<Treasure> treasureList = dungeon.pickupTreasureInCave(
                    new CaveCoordinates(row, col));
            if (!treasureList.isEmpty()) {
              treasureCounter += 1;
            }
          }
          if (dungeon.hasArrow(new CaveCoordinates(row, col))) {
            assertNotNull(dungeon.pickUpArrow(new CaveCoordinates(row, col)));
            assertFalse(dungeon.hasArrow(new CaveCoordinates(row, col)));
            arrowCounter += 1;
          }
        }
      }
      double foundTreasurePercent = (treasureCounter / caveCounter) * 100;
      assertEquals(randomPercent, foundTreasurePercent, 10.0);
      double foundArrowPercent = (arrowCounter / caveAndTunnelCounter) * 100;
      assertEquals(randomPercent, foundArrowPercent, 5.0);

      dungeon.resetDungeon();
      double treasureCounter2 = 0;
      double arrowCounter2 = 0;
      double caveCounter2 = 0;
      double caveAndTunnelCounter2 = 0;

      for (int row = 0; row < randomRowCount; row++) {
        for (int col = 0; col < randomColCount; col++) {
          caveAndTunnelCounter2 += 1;
          if (dungeon.getAdjacent(new CaveCoordinates(row, col)).size() != 2) {
            caveCounter2 += 1;
            List<Treasure> treasureList = dungeon.pickupTreasureInCave(
                    new CaveCoordinates(row, col));
            if (!treasureList.isEmpty()) {
              treasureCounter2 += 1;
            }
          }
          if (dungeon.hasArrow(new CaveCoordinates(row, col))) {
            assertNotNull(dungeon.pickUpArrow(new CaveCoordinates(row, col)));
            assertFalse(dungeon.hasArrow(new CaveCoordinates(row, col)));
            arrowCounter2 += 1;
          }
        }
      }

      assertEquals(treasureCounter, treasureCounter2, 0.001);
      assertEquals(arrowCounter, arrowCounter2, 0.001);
      assertEquals(caveCounter, caveCounter2, 0.001);
      assertEquals(caveAndTunnelCounter, caveAndTunnelCounter2, 0.001);
      assertEquals(start1, dungeon.getStartCave());
      assertEquals(end1, dungeon.getEndCave());

    }
  }

}
