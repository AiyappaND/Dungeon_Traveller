import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import dungeonmodel.coordinates.CaveCoordinates;
import dungeonmodel.coordinates.Coordinates;
import dungeonmodel.dungeon.Dungeon;
import dungeonmodel.dungeon.DungeonImpl;
import dungeonmodel.dungeon.MonsterSmell;
import dungeonmodel.monster.Otyugh;
import dungeonmodel.player.DungeonPlayer;
import dungeonmodel.player.Player;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Test class to check that all interactions between a player and monster work as expected.
 */
public class TestPlayerAndMonsterInteraction {

  /**
   * Test that a player dies and that a dead player cannot perform further actions.
   */
  @Test
  public void testDyingPlayerCannotPerformActions() {
    for (int dungeonCount = 0; dungeonCount < 100; dungeonCount++) {
      int randomRowCount = new Random().nextInt(15) + 5;
      int randomColCount = new Random().nextInt(15) + 5;
      Dungeon dungeon = new DungeonImpl(randomRowCount, randomColCount,
              0, false, 50, 1);
      Player dyingPlayer = new DungeonPlayer("Test", dungeon);
      while (!dyingPlayer.getCurrentCoordinates().equals(dungeon.getEndCave())) {
        dyingPlayer.moveTo(dyingPlayer.getPossibleMoves().get(
                new Random().nextInt(dyingPlayer.getPossibleMoves().size())));
      }
      assertFalse(dyingPlayer.isPlayerAlive());
      try {
        dyingPlayer.moveTo(dyingPlayer.getPossibleMoves().get(
                new Random().nextInt(dyingPlayer.getPossibleMoves().size())));
        fail();
      }
      catch (IllegalArgumentException ilEx) {
        // do nothing, expected exception
      }
      try {
        dyingPlayer.pickUpTreasure();
        fail();
      }
      catch (IllegalArgumentException ilEx) {
        // do nothing, expected exception
      }
      try {
        dyingPlayer.pickUpArrow();
        fail();
      }
      catch (IllegalArgumentException ilEx) {
        // do nothing, expected exception
      }
      try {
        dyingPlayer.shootArrow(1, dyingPlayer.getPossibleMoves().get(
                new Random().nextInt(dyingPlayer.getPossibleMoves().size())));
        fail();
      }
      catch (IllegalArgumentException ilEx) {
        // do nothing, expected exception
      }

      assertFalse(dyingPlayer.isPlayerAlive());
      assertFalse(dyingPlayer.hasPlayerWon());
    }
  }

  /**
   * Test that player can smell a strong smell when there is one monster close by.
   */
  @Test
  public void testStrongSmellWithOneMonster() {
    for (int dungeonCount = 0; dungeonCount < 1000; dungeonCount++) {
      int randomRowCount = new Random().nextInt(15) + 5;
      int randomColCount = new Random().nextInt(15) + 5;
      Dungeon dungeon = new DungeonImpl(randomRowCount, randomColCount,
              0, false, 50, 1);
      Player dyingPlayer = new DungeonPlayer("Test", dungeon);
      while (!dyingPlayer.getCurrentCoordinates().equals(dungeon.getEndCave())) {
        Coordinates next = dyingPlayer.getPossibleMoves().get(
                new Random().nextInt(dyingPlayer.getPossibleMoves().size()));
        if (next.equals(dungeon.getEndCave())) {
          assertEquals(MonsterSmell.STRONG, dyingPlayer.getSmellInCurrentCave());
        }
        dyingPlayer.moveTo(next);
      }
    }
  }

  /**
   * Test that player can smell a weak smell with a monster far away.
   */
  @Test
  public void testWeakSmellWithOneMonster() {
    for (int dungeonCount = 0; dungeonCount < 1000; dungeonCount++) {
      int randomRowCount = new Random().nextInt(15) + 5;
      int randomColCount = new Random().nextInt(15) + 5;
      Dungeon dungeon = new DungeonImpl(randomRowCount, randomColCount,
              0, false, 50, 1);
      Player dyingPlayer = new DungeonPlayer("Test", dungeon);
      while (!dyingPlayer.getCurrentCoordinates().equals(dungeon.getEndCave())) {
        Coordinates next = dyingPlayer.getPossibleMoves().get(
                new Random().nextInt(dyingPlayer.getPossibleMoves().size()));
        if (dungeon.getAdjacent(next).contains(dungeon.getEndCave())) {
          assertEquals(MonsterSmell.WEAK, dyingPlayer.getSmellInCurrentCave());
        }
        dyingPlayer.moveTo(next);
      }
    }
  }

  /**
   * Test that a player can smell all smells as expected.
   */
  @Test
  public void testMultipleSmellsWithMultipleMonsters() {
    int strongSmell = 0;
    int weakSmell = 0;
    int noSmell = 0;
    for (int dungeonCount = 0; dungeonCount < 5000; dungeonCount++) {
      int randomRowCount = new Random().nextInt(15) + 5;
      int randomColCount = new Random().nextInt(15) + 5;
      Dungeon dungeon = new DungeonImpl(randomRowCount, randomColCount,
              0, false, 100,
              randomColCount + randomRowCount);
      Player dyingPlayer = new DungeonPlayer("Test", dungeon);
      while (dyingPlayer.isPlayerAlive()
              && !dyingPlayer.getCurrentCoordinates().equals(dungeon.getEndCave())) {
        Coordinates next = dyingPlayer.getPossibleMoves().get(
                new Random().nextInt(dyingPlayer.getPossibleMoves().size()));
        if (dungeon.hasMonster(next)) {
          assertEquals(dyingPlayer.getSmellInCurrentCave(), MonsterSmell.STRONG);
          strongSmell += 1;
          if (dyingPlayer.getArrowCount() > 0) {
            dyingPlayer.shootArrow(1, next);
          }
          if (dyingPlayer.getArrowCount() > 0) {
            dyingPlayer.shootArrow(1, next);
          }
        }
        else {
          List<Coordinates> oneStepAdjacent = dyingPlayer.getPossibleMoves();
          Set<Coordinates> twoStepAdjacent = new HashSet<>();
          boolean foundOneStepAdjacentMonster = false;
          for (Coordinates coords: oneStepAdjacent) {
            if (dungeon.hasMonster(coords)) {
              assertEquals(dyingPlayer.getSmellInCurrentCave(), MonsterSmell.STRONG);
              strongSmell += 1;
              foundOneStepAdjacentMonster = true;
            }
            twoStepAdjacent.addAll(dungeon.getAdjacent(coords));
          }
          if (dungeon.hasMonster(dyingPlayer.getCurrentCoordinates())) {
            assertEquals(dyingPlayer.getSmellInCurrentCave(), MonsterSmell.STRONG);
            strongSmell += 1;
            foundOneStepAdjacentMonster = true;
          }
          if (!foundOneStepAdjacentMonster) {
            int monsterCount = 0;
            for (Coordinates coords: twoStepAdjacent) {
              if (dungeon.hasMonster(coords)) {
                monsterCount += 1;
              }
            }
            if (monsterCount == 1) {
              assertEquals(dyingPlayer.getSmellInCurrentCave(), MonsterSmell.WEAK);
              weakSmell += 1;
            }
            else if (monsterCount == 0) {
              assertEquals(dyingPlayer.getSmellInCurrentCave(), MonsterSmell.NONE);
              noSmell += 1;
            }
            else {
              assertEquals(dyingPlayer.getSmellInCurrentCave(), MonsterSmell.STRONG);
              strongSmell += 1;
            }
          }
        }
        if (dungeon.hasArrow(dyingPlayer.getCurrentCoordinates())) {
          assertTrue(dyingPlayer.pickUpArrow());
        }
        dyingPlayer.moveTo(next);
      }
    }
    assertTrue(strongSmell > 0);
    assertTrue(weakSmell > 0);
    assertTrue(noSmell > 0);
  }

  /**
   * Test that player always dies when meeting uninjured monster.
   */
  @Test
  public void testNoSurvivalChancesAndArrowMisses() {
    for (int dungeonCount = 0; dungeonCount < 5000; dungeonCount++) {
      int randomRowCount = new Random().nextInt(15) + 5;
      int randomColCount = new Random().nextInt(15) + 5;
      Dungeon dungeon = new DungeonImpl(randomRowCount, randomColCount,
              0, false, 100,
              1);
      Player dyingPlayer = new DungeonPlayer("Test", dungeon);

      while (!dyingPlayer.getCurrentCoordinates().equals(dungeon.getEndCave())) {
        Coordinates next = dyingPlayer.getPossibleMoves().get(
                new Random().nextInt(dyingPlayer.getPossibleMoves().size()));

        if (dungeon.hasMonster(next)) {
          dyingPlayer.shootArrow(2, next);
          dyingPlayer.shootArrow(2, next);
          assertTrue(dungeon.hasMonster(next));
        }
        dyingPlayer.moveTo(next);
      }
      assertEquals(dyingPlayer.getCurrentCoordinates(), dungeon.getEndCave());
      assertFalse(dyingPlayer.isPlayerAlive());
    }
  }

  /**
   * Test that player has a 50% chance of survival with an injured monster.
   */
  @Test
  public void testFiftyPercentSurvivalChancesAndPlayerWin() {
    int aliveCount = 0;
    int wonCount = 0;
    for (int dungeonCount = 0; dungeonCount < 5000; dungeonCount++) {
      int randomRowCount = new Random().nextInt(15) + 5;
      int randomColCount = new Random().nextInt(15) + 5;
      Dungeon dungeon = new DungeonImpl(randomRowCount, randomColCount,
              0, false, 100,
              1);
      Player dyingPlayer = new DungeonPlayer("Test", dungeon);
      while (!dyingPlayer.getCurrentCoordinates().equals(dungeon.getEndCave())) {
        Coordinates next = dyingPlayer.getPossibleMoves().get(
                new Random().nextInt(dyingPlayer.getPossibleMoves().size()));
        if (next.equals(dungeon.getEndCave())) {
          dyingPlayer.shootArrow(1, next);
        }
        dyingPlayer.moveTo(next);
      }
      assertEquals(dyingPlayer.getCurrentCoordinates(), dungeon.getEndCave());
      if (dyingPlayer.isPlayerAlive()) {
        aliveCount += 1;
        assertTrue(dyingPlayer.hasPlayerWon());
      }
      if (dyingPlayer.hasPlayerWon()) {
        wonCount += 1;
      }
    }
    assertTrue(aliveCount > 2300 && aliveCount < 2700);
    assertEquals(aliveCount, wonCount);
  }

  /**
   * Test that arrow shot straight north travels as expected in a non wrapping Dungeon.
   */
  @Test
  public void testArrowMovesStraightInNonWrappingNorth() {
    int shotCount = 0;
    for (int dungeonCount = 0; dungeonCount < 1000; dungeonCount ++) {
      Dungeon dungeon = new DungeonImpl(5, 5, 16, false, 100, 1);
      Player player = new DungeonPlayer("Test", dungeon);
      assertTrue(player.pickUpArrow());
      assertEquals(4, player.getArrowCount());
      int x = player.getCurrentCoordinates().getXCoordinates();
      int y = player.getCurrentCoordinates().getYCoordinates();
      if (x - 1 >= 0) {
        Coordinates arrowLandedShootingNorth = player.shootArrow(x,
                new CaveCoordinates(x - 1, y));
        shotCount += 1;
        assertEquals(new CaveCoordinates(0, player.getCurrentCoordinates().getYCoordinates()),
              arrowLandedShootingNorth);
      }
    }
    assertTrue(shotCount > 500);
  }

  /**
   * Test that arrow shot straight south travels as expected in a non wrapping Dungeon.
   */
  @Test
  public void testArrowMovesStraightInNonWrappingSouth() {
    int shotCount = 0;
    for (int dungeonCount = 0; dungeonCount < 1000; dungeonCount ++) {
      Dungeon dungeon = new DungeonImpl(5, 5, 16, false, 100, 1);
      Player player = new DungeonPlayer("Test", dungeon);
      assertTrue(player.pickUpArrow());
      assertEquals(4, player.getArrowCount());
      int x = player.getCurrentCoordinates().getXCoordinates();
      int y = player.getCurrentCoordinates().getYCoordinates();
      if (x + 1 <= 4) {
        Coordinates arrowLandedShootingNorth = player.shootArrow(4 - x,
                new CaveCoordinates(x + 1, y));
        shotCount += 1;
        assertEquals(new CaveCoordinates(4, player.getCurrentCoordinates().getYCoordinates()),
                arrowLandedShootingNorth);
      }
    }
    assertTrue(shotCount > 500);
  }

  /**
   * Test that arrow shot straight west travels as expected in a non wrapping Dungeon.
   */
  @Test
  public void testArrowMovesStraightInNonWrappingWest() {
    int shotCount = 0;
    for (int dungeonCount = 0; dungeonCount < 1000; dungeonCount ++) {
      Dungeon dungeon = new DungeonImpl(5, 5, 16, false, 100, 1);
      Player player = new DungeonPlayer("Test", dungeon);
      assertTrue(player.pickUpArrow());
      assertEquals(4, player.getArrowCount());
      int x = player.getCurrentCoordinates().getXCoordinates();
      int y = player.getCurrentCoordinates().getYCoordinates();
      if (y - 1 >= 0) {
        Coordinates arrowLandedShootingWest = player.shootArrow(y,
                new CaveCoordinates(x, y - 1));
        shotCount += 1;
        assertEquals(new CaveCoordinates(player.getCurrentCoordinates().getXCoordinates(), 0),
                arrowLandedShootingWest);
      }
    }
    assertTrue(shotCount > 500);
  }

  /**
   * Test that arrow shot straight east travels as expected in a non wrapping Dungeon.
   */
  @Test
  public void testArrowMovesStraightInNonWrappingEast() {
    int shotCount = 0;
    for (int dungeonCount = 0; dungeonCount < 1000; dungeonCount ++) {
      Dungeon dungeon = new DungeonImpl(5, 5, 16, false, 100, 1);

      Player player = new DungeonPlayer("Test", dungeon);
      assertTrue(player.pickUpArrow());
      assertEquals(4, player.getArrowCount());
      int x = player.getCurrentCoordinates().getXCoordinates();
      int y = player.getCurrentCoordinates().getYCoordinates();
      if (y + 1 <= 4) {
        Coordinates arrowLandedShootingWest = player.shootArrow(4 - y,
                new CaveCoordinates(x, y + 1));
        shotCount += 1;
        assertEquals(new CaveCoordinates(player.getCurrentCoordinates().getXCoordinates(), 4),
                arrowLandedShootingWest);
      }
    }
    assertTrue(shotCount > 500);
  }

  /**
   * Test that arrow shot straight north travels as expected in a wrapping Dungeon.
   */
  @Test
  public void testArrowMovesStraightInWrappingNorth() {
    for (int dungeonCount = 0; dungeonCount < 1000; dungeonCount ++) {
      Dungeon dungeon = new DungeonImpl(10, 10, 101, true, 100, 1);

      Player player = new DungeonPlayer("Test", dungeon);
      assertTrue(player.pickUpArrow());
      assertEquals(4, player.getArrowCount());
      int x = player.getCurrentCoordinates().getXCoordinates();
      int y = player.getCurrentCoordinates().getYCoordinates();
      Coordinates arrowLandedShootingNorth;
      if (x - 1 < 0) {
        arrowLandedShootingNorth = player.shootArrow(10,
                new CaveCoordinates(9, y));
      }
      else {
        arrowLandedShootingNorth = player.shootArrow(10,
                new CaveCoordinates(x - 1, y));
      }
      assertEquals(player.getCurrentCoordinates(),
              arrowLandedShootingNorth);
    }
  }

  /**
   * Test that arrow shot straight south travels as expected in a wrapping Dungeon.
   */
  @Test
  public void testArrowMovesStraightInWrappingSouth() {
    for (int dungeonCount = 0; dungeonCount < 1000; dungeonCount ++) {
      Dungeon dungeon = new DungeonImpl(10, 10, 101, true, 100, 1);

      Player player = new DungeonPlayer("Test", dungeon);
      assertTrue(player.pickUpArrow());
      assertEquals(4, player.getArrowCount());
      int x = player.getCurrentCoordinates().getXCoordinates();
      int y = player.getCurrentCoordinates().getYCoordinates();
      Coordinates arrowLandedShootingSouth;
      if (x + 1 > 9) {
        arrowLandedShootingSouth = player.shootArrow(10,
                new CaveCoordinates(0, y));
      }
      else {
        arrowLandedShootingSouth = player.shootArrow(10,
                new CaveCoordinates(x + 1, y));
      }
      assertEquals(player.getCurrentCoordinates(),
              arrowLandedShootingSouth);
    }
  }

  /**
   * Test that arrow shot straight west travels as expected in a wrapping Dungeon.
   */
  @Test
  public void testArrowMovesStraightInWrappingWest() {
    for (int dungeonCount = 0; dungeonCount < 1000; dungeonCount ++) {
      Dungeon dungeon = new DungeonImpl(10, 10, 101, true, 100, 1);

      Player player = new DungeonPlayer("Test", dungeon);
      assertTrue(player.pickUpArrow());
      assertEquals(4, player.getArrowCount());
      int x = player.getCurrentCoordinates().getXCoordinates();
      int y = player.getCurrentCoordinates().getYCoordinates();
      Coordinates arrowLandedWest;
      if (y - 1 < 0) {
        arrowLandedWest = player.shootArrow(10,
                new CaveCoordinates(x, 9));
      }
      else {
        arrowLandedWest = player.shootArrow(10,
                new CaveCoordinates(x, y - 1));
      }
      assertEquals(player.getCurrentCoordinates(),
              arrowLandedWest);
    }
  }

  /**
   * Test that arrow shot straight east travels as expected in a wrapping Dungeon.
   */
  @Test
  public void testArrowMovesStraightInWrappingEast() {
    for (int dungeonCount = 0; dungeonCount < 1000; dungeonCount ++) {
      Dungeon dungeon = new DungeonImpl(10, 10, 101, true, 100, 1);

      Player player = new DungeonPlayer("Test", dungeon);
      assertTrue(player.pickUpArrow());
      assertEquals(4, player.getArrowCount());
      int x = player.getCurrentCoordinates().getXCoordinates();
      int y = player.getCurrentCoordinates().getYCoordinates();
      Coordinates arrowEast;
      if (y + 1 > 9) {
        arrowEast = player.shootArrow(10,
                new CaveCoordinates(x, 0));
      }
      else {
        arrowEast = player.shootArrow(10,
                new CaveCoordinates(x, y + 1));
      }
      assertEquals(player.getCurrentCoordinates(),
              arrowEast);
    }
  }

  /**
   * Test that arrow curves east when a tunnel has an opening only in that direction.
   */
  @Test
  public void testArrowCurvesEastInTunnel() {
    int arrowCount = 0;
    while (arrowCount < 1000) {
      Dungeon dungeon = new DungeonImpl(5, 5, 16, false, 100, 1);

      Player player = new DungeonPlayer("Test", dungeon);
      if (player.getCurrentCoordinates().getYCoordinates() == 0
              && player.getCurrentCoordinates().getXCoordinates() != 0) {
        Coordinates arrowLocation = player.shootArrow(5,
                new CaveCoordinates(player.getCurrentCoordinates().getXCoordinates() - 1,
                        player.getCurrentCoordinates().getYCoordinates()));
        arrowCount += 1;
        assertEquals(new CaveCoordinates(0, 5
                - player.getCurrentCoordinates().getXCoordinates()), arrowLocation);
      }
    }
  }

  /**
   * Test that arrow curves west when a tunnel has an opening only in that direction.
   */
  @Test
  public void testArrowCurvesWestInTunnel() {
    int arrowCount = 0;
    while (arrowCount < 1000) {
      Dungeon dungeon = new DungeonImpl(5, 5, 16, false, 100, 1);

      Player player = new DungeonPlayer("Test", dungeon);
      if (player.getCurrentCoordinates().getYCoordinates() == 4
              && player.getCurrentCoordinates().getXCoordinates() != 0) {
        Coordinates arrowLocation = player.shootArrow(5,
                new CaveCoordinates(player.getCurrentCoordinates().getXCoordinates() - 1,
                        player.getCurrentCoordinates().getYCoordinates()));
        arrowCount += 1;
        assertEquals(new CaveCoordinates(0, 4
                - (5 - player.getCurrentCoordinates().getXCoordinates())), arrowLocation);
      }
    }
  }

  /**
   * Test that arrow curves north when a tunnel has an opening only in that direction.
   */
  @Test
  public void testArrowCurvesNorthInTunnel() {
    int arrowCount = 0;
    while (arrowCount < 1000) {
      Dungeon dungeon = new DungeonImpl(5, 5, 16, false, 100, 1);

      Player player = new DungeonPlayer("Test", dungeon);
      if (player.getCurrentCoordinates().getXCoordinates() == 4
              && player.getCurrentCoordinates().getYCoordinates() != 4) {
        Coordinates arrowLocation = player.shootArrow(5,
                new CaveCoordinates(player.getCurrentCoordinates().getXCoordinates(),
                        player.getCurrentCoordinates().getYCoordinates() + 1));
        arrowCount += 1;
        assertEquals(new CaveCoordinates(4
                - (5 - (4 - player.getCurrentCoordinates().getYCoordinates())), 4),
                arrowLocation);
      }
    }
  }

  /**
   * Test that arrow curves south when a tunnel has an opening only in that direction.
   */
  @Test
  public void testArrowCurvesSouthInTunnel() {
    int arrowCount = 0;
    while (arrowCount < 1000) {
      Dungeon dungeon = new DungeonImpl(5, 5, 16, false, 100, 1);

      Player player = new DungeonPlayer("Test", dungeon);
      if (player.getCurrentCoordinates().getXCoordinates() == 0
              && player.getCurrentCoordinates().getYCoordinates() != 4) {
        Coordinates arrowLocation = player.shootArrow(5,
                new CaveCoordinates(player.getCurrentCoordinates().getXCoordinates(),
                        player.getCurrentCoordinates().getYCoordinates() + 1));
        arrowCount += 1;
        assertEquals(new CaveCoordinates(5
                - (4 - player.getCurrentCoordinates().getYCoordinates()), 4), arrowLocation);
      }
    }
  }

  /**
   * Test expected exception when shooting is attempted with no arrows.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testShootingWhenNoArrows() {
    Dungeon dungeon = new DungeonImpl(5, 5, 16, false, 100, 1);

    Player player = new DungeonPlayer("Test", dungeon);
    player.shootArrow(1, player.getPossibleMoves().get(0));
    player.shootArrow(1, player.getPossibleMoves().get(0));
    player.shootArrow(1, player.getPossibleMoves().get(0));
    player.shootArrow(1, player.getPossibleMoves().get(0));
  }

  /**
   * Test expected exception when shooting is attempted with negative distance.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testShootingNegativeDistance() {
    Dungeon dungeon = new DungeonImpl(5, 5, 16, false, 100, 1);

    Player player = new DungeonPlayer("Test", dungeon);
    player.shootArrow(-1, player.getPossibleMoves().get(0));
  }

  /**
   * Test expected exception when shooting is attempted with invalid direction.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testShootingInInvalidDirection() {
    Dungeon dungeon = new DungeonImpl(5, 5, 16, false, 100, 1);

    Player player = new DungeonPlayer("Test", dungeon);
    player.shootArrow(1, dungeon.getEndCave());
  }

  /**
   * Test expected exception when monster is placed in null coordinates.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testPlacingOtyughInNull() {
    new Otyugh(null);
  }
}
