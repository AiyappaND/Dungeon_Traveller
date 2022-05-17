import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import dungeonmodel.coordinates.CaveCoordinates;
import dungeonmodel.coordinates.Coordinates;
import dungeonmodel.dungeon.Dungeon;
import dungeonmodel.dungeon.DungeonImpl;
import dungeonmodel.player.DungeonPlayer;
import dungeonmodel.player.Player;
import dungeonmodel.treasure.Treasure;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Class to test expected functionality of a Player.
 */
public class TestDungeonPlayer {

  /**
   * Test expected exception when player name is null.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testNullName() {
    Dungeon dungeon1 = new DungeonImpl(5, 5, 0, false, 50, 1);
    new DungeonPlayer(null, dungeon1);
  }

  /**
   * Test expected exception when player name is empty.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testEmptyName() {
    Dungeon dungeon1 = new DungeonImpl(5, 5, 0, false, 50, 1);
    new DungeonPlayer("  ", dungeon1);
  }

  /**
   * Test expected exception when player tries to enter null dungeon.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testEnteringNullDungeon() {
    new DungeonPlayer("Player", null);
  }

  /**
   * Test player name is as provided.
   */
  @Test
  public void testPlayerName() {
    Dungeon dungeon1 = new DungeonImpl(5, 5, 0, false, 50, 1);
    Player player = new DungeonPlayer("Player 1", dungeon1);
    assertEquals(player.getName(), "Player 1");
  }

  /**
   * Test that moving to same coordinate as current works as expected.
   */
  @Test
  public void testMovingToSameCoordinate() {
    Dungeon dungeon1 = new DungeonImpl(5, 5, 0, false, 50, 1);
    Player player = new DungeonPlayer("Player 1", dungeon1);
    boolean moved = player.moveTo(dungeon1.getStartCave());
    assertFalse(moved);
    assertEquals(player.getCurrentCoordinates(), dungeon1.getStartCave());
  }

  /**
   * Test that player always begins at dungeon start location.
   */
  @Test
  public void testPlayerBeginsAtStart() {
    Dungeon dungeon1 = new DungeonImpl(5, 5, 0, false, 50, 1);
    Player player = new DungeonPlayer("Player 1", dungeon1);
    assertEquals(player.getCurrentCoordinates(), dungeon1.getStartCave());
  }

  /**
   * Test moving to invalid coordinates works as expected.
   */
  @Test
  public void testMovingToWrongCoordinate() {
    Dungeon dungeon1 = new DungeonImpl(5, 5, 0, false, 50, 1);
    Player player = new DungeonPlayer("Player 1", dungeon1);
    Coordinates wrong = new CaveCoordinates(dungeon1.getStartCave().getXCoordinates() + 2,
            dungeon1.getStartCave().getYCoordinates());
    boolean moved = player.moveTo(wrong);
    assertFalse(moved);
    assertEquals(player.getCurrentCoordinates(), dungeon1.getStartCave());
  }

  /**
   * Test that moving to valid coordinates changes player's current location.
   */
  @Test
  public void testMoveToRightCoordinates() {
    Dungeon dungeon1 = new DungeonImpl(5, 5, 0, false, 50, 1);
    Player player = new DungeonPlayer("Player 1", dungeon1);
    boolean moved = player.moveTo(dungeon1.getAdjacent(dungeon1.getStartCave()).get(0));
    assertTrue(moved);
    assertEquals(player.getCurrentCoordinates(),
            dungeon1.getAdjacent(dungeon1.getStartCave()).get(0));
  }

  /**
   * Test that view and pickup treasure works as expected.
   */
  @Test
  public void testViewAndPickupTreasure() {
    Dungeon dungeon1 = new DungeonImpl(5, 5, 0, false, 100, 1);
    Player player = new DungeonPlayer("Player 1", dungeon1);
    boolean treasureFound = false;
    while (!treasureFound) {
      if (player.viewTreasureAtCurrentLocation().isEmpty()) {
        player.moveTo(player.getPossibleMoves().get(
                new Random().nextInt(player.getPossibleMoves().size())));
      } else {
        treasureFound = true;
      }
    }
    List<Treasure> treasurePresent = player.viewTreasureAtCurrentLocation();
    assertEquals(treasurePresent, dungeon1.viewTreasureInCave(player.getCurrentCoordinates()));
    boolean treasurePickedUp = player.pickUpTreasure();
    assertTrue(treasurePickedUp);
    assertEquals(treasurePresent, player.getCurrentTreasure());
    assertTrue(dungeon1.viewTreasureInCave(player.getCurrentCoordinates()).isEmpty());
  }

  /**
   * Test moving to every cave in the dungeon is possible from start.
   */
  @Test
  public void testMoveToAllCoordinates() {
    Dungeon dungeon1 = new DungeonImpl(5, 5, 0, false, 100, 1);
    Player player = new DungeonPlayer("Player 1", dungeon1);
    List<Coordinates> allCoordinates = new ArrayList<>();
    allCoordinates.add(player.getCurrentCoordinates());
    while (allCoordinates.size() != 25) {
      Coordinates next = player.getPossibleMoves().get(
              new Random().nextInt(player.getPossibleMoves().size()));
      if (dungeon1.hasMonster(next)) {
        player.shootArrow(1, next);
        player.shootArrow(1, next);
      }
      player.moveTo(next);
      if (!allCoordinates.contains(player.getCurrentCoordinates())) {
        allCoordinates.add(player.getCurrentCoordinates());
      }
    }
    for (int row = 0; row < 5; row++) {
      for (int column = 0; column < 5; column++) {
        assertTrue(allCoordinates.contains(new CaveCoordinates(row, column)));
      }
    }
  }

  /**
   * Test that moving to end location is possible from start, and that player dies if he moves into
   * a cave with an Otyugh that is not shot.
   */
  @Test
  public void testMoveToEndAndDie() {
    Dungeon dungeon1 = new DungeonImpl(5, 5, 0, true, 100, 1);
    Player player = new DungeonPlayer("Player 1", dungeon1);
    boolean visitedEnd = false;
    while (!visitedEnd) {
      player.moveTo(player.getPossibleMoves().get(
              new Random().nextInt(player.getPossibleMoves().size())));
      if (player.getCurrentCoordinates().equals(dungeon1.getEndCave())) {
        visitedEnd = true;
      }
    }
    assertEquals(player.getCurrentCoordinates(), dungeon1.getEndCave());
    assertFalse(player.isPlayerAlive());

  }

  /**
   * Test picking up all treasure and arrows in the dungeon.
   */
  @Test
  public void testPickupAllTreasureAndAllArrows() {
    Dungeon dungeon1 = new DungeonImpl(5, 5, 0, false, 100, 1);
    Player player = new DungeonPlayer("Player 1", dungeon1);
    List<Treasure> allTreasurePresent = new ArrayList<>();
    int arrowCount = 0;
    for (int row = 0; row < 5; row++) {
      for (int column = 0; column < 5; column++) {
        allTreasurePresent.addAll(dungeon1.viewTreasureInCave(new CaveCoordinates(row, column)));
        if (dungeon1.hasArrow(new CaveCoordinates(row, column))) {
          arrowCount += 1;
        }
      }
    }
    player.pickUpTreasure();
    player.pickUpArrow();
    List<Coordinates> allCoordinates = new ArrayList<>();
    allCoordinates.add(player.getCurrentCoordinates());
    while (allCoordinates.size() != 25) {
      Coordinates nextMove = player.getPossibleMoves().get(
              new Random().nextInt(player.getPossibleMoves().size()));
      if (dungeon1.hasMonster(nextMove)) {
        player.shootArrow(1, nextMove);
        player.shootArrow(1, nextMove);
      }
      player.moveTo(nextMove);
      player.pickUpTreasure();
      player.pickUpArrow();
      if (!allCoordinates.contains(player.getCurrentCoordinates())) {
        allCoordinates.add(player.getCurrentCoordinates());
      }
    }
    for (int row = 0; row < 5; row++) {
      for (int column = 0; column < 5; column++) {
        assertTrue(allCoordinates.contains(new CaveCoordinates(row, column)));
        assertTrue(dungeon1.viewTreasureInCave(new CaveCoordinates(row, column)).isEmpty());
      }
    }
    assertTrue(allTreasurePresent.containsAll(player.getCurrentTreasure()));
    assertTrue(player.getCurrentTreasure().containsAll(allTreasurePresent));
    assertEquals(player.getArrowCount(), arrowCount + 1);
  }

  /**
   * Test that reset player works as expected.
   */
  @Test
  public void testResetPlayer() {
    Dungeon dungeon1 = new DungeonImpl(5, 5, 0, true, 100, 1);
    Player player = new DungeonPlayer("Player 1", dungeon1);
    boolean visitedEnd = false;
    while (!visitedEnd) {
      player.pickUpArrow();
      player.pickUpTreasure();
      player.moveTo(player.getPossibleMoves().get(
              new Random().nextInt(player.getPossibleMoves().size())));
      if (player.getCurrentCoordinates().equals(dungeon1.getEndCave())) {
        visitedEnd = true;
      }
    }
    assertEquals(player.getCurrentCoordinates(), dungeon1.getEndCave());
    assertFalse(player.isPlayerAlive());
    assertFalse(player.getCurrentTreasure().isEmpty());
    assertTrue(player.getArrowCount() > 3);

    player.resetPlayer();
    assertEquals(player.getCurrentCoordinates(), dungeon1.getStartCave());
    assertTrue(player.isPlayerAlive());
    assertTrue(player.getCurrentTreasure().isEmpty());
    assertEquals(3, player.getArrowCount());
    assertFalse(player.hasPlayerWon());
  }

}
