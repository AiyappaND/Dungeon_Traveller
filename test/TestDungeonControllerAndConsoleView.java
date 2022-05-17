import org.junit.Test;

import java.io.StringReader;
import java.util.List;
import java.util.Random;

import controller.DungeonGameController;
import controller.GameController;
import dungeonmodel.coordinates.Coordinates;
import dungeonmodel.dungeon.Dungeon;
import dungeonmodel.dungeon.DungeonImpl;
import dungeonmodel.dungeon.MonsterSmell;
import dungeonmodel.player.DungeonPlayer;
import dungeonmodel.player.Player;
import dungeonmodel.treasure.Treasure;
import view.ConsoleView;
import view.GameView;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test class to test functionality of a DungeonGame Controller.
 */
public class TestDungeonControllerAndConsoleView {

  /**
   * Test expected exception with a failing appendable.
   */
  @Test(expected = IllegalStateException.class)
  public void testFailingAppendable() {
    // Testing when something goes wrong with the Appendable
    // Here we are passing it a mocked Appendable that always fails
    Dungeon dungeon = new DungeonImpl(5, 5, 5, false, 50, 1);
    Player player = new DungeonPlayer("Test", dungeon);
    StringReader input = new StringReader("2 2 1 1 3 3 1 2 1 3 2 3 2 1 3 1 3 2");
    Appendable gameLog = new FailingAppendable();
    GameView consoleView = new ConsoleView(input, gameLog, player);
    GameController c = new DungeonGameController(player, consoleView);
    c.playGame();
  }

  /**
   * Test that quit option works and that game is ended.
   */
  @Test
  public void testEndWhenQuit() {
    Dungeon dungeon = new DungeonImpl(5, 5, 5, false, 50, 1);
    Player player = new DungeonPlayer("Test", dungeon);
    StringReader input = new StringReader("q m 1");
    Appendable gameLog = new StringBuilder();
    GameView consoleView = new ConsoleView(input, gameLog, player);
    GameController c = new DungeonGameController(player, consoleView);
    c.playGame();
    assertTrue(gameLog.toString().contains("Player has quit the game"));
    assertEquals(player.getCurrentCoordinates(), dungeon.getStartCave());
  }

  /**
   * Test that moving a player works as expected.
   */
  @Test
  public void testMovePlayer() {
    Dungeon dungeon = new DungeonImpl(5, 5, 5, false, 50, 1);
    Player player = new DungeonPlayer("Test", dungeon);
    Coordinates playerCoord = dungeon.getAdjacent(dungeon.getStartCave()).get(0);
    StringReader input = new StringReader("23476 m 5 1");
    Appendable gameLog = new StringBuilder();
    GameView consoleView = new ConsoleView(input, gameLog, player);
    GameController c = new DungeonGameController(player, consoleView);
    c.playGame();
    assertEquals(playerCoord, player.getCurrentCoordinates());
    assertTrue(gameLog.toString().contains("Not a valid choice, try again."));
  }

  /**
   * Test that picking up a treasure works as expected.
   */
  @Test
  public void testPickUpTreasure() {
    Dungeon dungeon = new DungeonImpl(5, 5, 5, false, 100, 1);
    Player player = new DungeonPlayer("Test", dungeon);
    assertFalse(dungeon.viewTreasureInCave(dungeon.getStartCave()).isEmpty());
    List<Treasure> treasureList = dungeon.viewTreasureInCave(dungeon.getStartCave());
    StringReader input = new StringReader("ooq 43 t");
    Appendable gameLog = new StringBuilder();
    GameView consoleView = new ConsoleView(input, gameLog, player);
    GameController c = new DungeonGameController(player, consoleView);
    c.playGame();
    assertEquals(treasureList, player.getCurrentTreasure());
    assertTrue(dungeon.viewTreasureInCave(dungeon.getStartCave()).isEmpty());
    assertTrue(gameLog.toString().contains(
            "Invalid input, please enter one of the available choices"));
  }

  /**
   * Test that picking up an arrow works as expected.
   */
  @Test
  public void testPickUpArrow() {
    Dungeon dungeon = new DungeonImpl(5, 5, 5, false, 100, 1);
    Player player = new DungeonPlayer("Test", dungeon);
    assertTrue(dungeon.hasArrow(dungeon.getStartCave()));
    StringReader input = new StringReader("23472938 a");
    Appendable gameLog = new StringBuilder();
    GameView consoleView = new ConsoleView(input, gameLog, player);
    GameController c = new DungeonGameController(player, consoleView);
    c.playGame();
    assertEquals(4, player.getArrowCount());
    assertFalse(dungeon.hasArrow(dungeon.getStartCave()));
    assertTrue(gameLog.toString().contains(
            "Invalid input, please enter one of the available choices"));
  }

  /**
   * Test that shooting an arrow works as expected.
   */
  @Test
  public void testShootArrow() {
    Dungeon dungeon = new DungeonImpl(5, 5, 5, false, 50, 1);
    Player player = new DungeonPlayer("Test", dungeon);
    StringReader input = new StringReader("s 1 1");
    Appendable gameLog = new StringBuilder();
    GameView consoleView = new ConsoleView(input, gameLog, player);
    GameController c = new DungeonGameController(player, consoleView);
    c.playGame();
    assertEquals(player.getArrowCount(), 2);
    assertTrue(gameLog.toString().contains(String.format("Arrow has landed in %s",
            dungeon.getAdjacent(dungeon.getStartCave()).get(0))));
  }

  /**
   * Test that game ends as expected when player is eaten by a monster.
   */
  @Test
  public void testEndWhenPlayerDies() {
    Dungeon dungeon = new DungeonImpl(5, 5, 16, false, 50, 20);
    Player player = new DungeonPlayer("Test", dungeon);
    int monsterLocation = 0;
    for (Coordinates coords: dungeon.getAdjacent(dungeon.getStartCave())) {
      if (dungeon.hasMonster(coords)) {
        monsterLocation = dungeon.getAdjacent(dungeon.getStartCave()).indexOf(coords) + 1;
      }
    }
    StringReader input = new StringReader(String.format("23476 m a %d", monsterLocation));
    Appendable gameLog = new StringBuilder();
    GameView consoleView = new ConsoleView(input, gameLog, player);
    GameController c = new DungeonGameController(player, consoleView);
    c.playGame();
    assertFalse(player.isPlayerAlive());
    assertTrue(gameLog.toString().contains("Player is dead"));
    assertTrue(gameLog.toString().contains("Not a valid integer, try again."));
  }

  /**
   * Test that info about strong smell is presented as expected.
   */
  @Test
  public void testStrongSmellInCave() {
    Dungeon dungeon = new DungeonImpl(5, 5, 16, false, 50, 20);
    Player player = new DungeonPlayer("Test", dungeon);
    StringReader input = new StringReader("23472938 a");
    Appendable gameLog = new StringBuilder();
    GameView consoleView = new ConsoleView(input, gameLog, player);
    GameController c = new DungeonGameController(player, consoleView);
    c.playGame();
    assertTrue(gameLog.toString().contains("There is a strong smell of nearby monsters"));
  }

  /**
   * Test that info about no smell is presented as expected.
   */
  @Test
  public void testNoSmellInCave() {
    Dungeon dungeon = new DungeonImpl(5, 5, 5, false, 50, 1);
    Player player = new DungeonPlayer("Test", dungeon);
    StringReader input = new StringReader("23472938 a");
    Appendable gameLog = new StringBuilder();
    GameView consoleView = new ConsoleView(input, gameLog, player);
    GameController c = new DungeonGameController(player, consoleView);
    c.playGame();
    assertTrue(gameLog.toString().contains("Cannot smell anything currently"));
  }

  /**
   * Test that info about weak smell is presented as expected.
   */
  @Test
  public void testWeakSmellInCave() {
    Dungeon dungeon = new DungeonImpl(5, 5, 5, false, 50, 1);
    Player player = new DungeonPlayer("Test", dungeon);
    StringBuilder inputStream = new StringBuilder();

    boolean sequencecompleted = false;
    Coordinates current = dungeon.getStartCave();

    while (!sequencecompleted) {
      int nextPick = new Random().nextInt(dungeon.getAdjacent(current).size());
      inputStream.append(String.format(" m %s", nextPick + 1));
      current = dungeon.getAdjacent(current).get(nextPick);
      if (dungeon.getSmell(current).equals(MonsterSmell.WEAK)) {
        sequencecompleted = true;
      }
    }
    StringReader input = new StringReader(inputStream.toString());
    Appendable gameLog = new StringBuilder();
    GameView consoleView = new ConsoleView(input, gameLog, player);
    GameController c = new DungeonGameController(player, consoleView);
    c.playGame();
    assertTrue(gameLog.toString().contains("There is a weak smell of nearby monsters"));
  }

  /**
   * Test expected behaviour for a full run of the game where player wins the game.
   */
  @Test
  public void testPlayerWins() {
    Dungeon dungeon = new DungeonImpl(5, 5, 5, false, 50, 1);
    Player player = new DungeonPlayer("Test", dungeon);
    StringBuilder inputStream = new StringBuilder();

    boolean sequencecompleted = false;
    Coordinates current = dungeon.getStartCave();

    while (!sequencecompleted) {
      int nextPick = new Random().nextInt(dungeon.getAdjacent(current).size());
      if (dungeon.hasMonster(dungeon.getAdjacent(current).get(nextPick))) {
        inputStream.append(String.format(" s %s 1 ", nextPick + 1));
        inputStream.append(String.format(" s %s 1 ", nextPick + 1));
      }
      inputStream.append(String.format(" m %s", nextPick + 1));
      current = dungeon.getAdjacent(current).get(nextPick);
      if (current.equals(dungeon.getEndCave())) {
        sequencecompleted = true;
      }
    }

    StringReader input = new StringReader(inputStream.toString());
    Appendable gameLog = new StringBuilder();
    GameView consoleView = new ConsoleView(input, gameLog, player);
    GameController c = new DungeonGameController(player, consoleView);
    c.playGame();
    assertTrue(gameLog.toString().contains("There is a strong smell of nearby monsters"));
    assertTrue(gameLog.toString().contains("There is a weak smell of nearby monsters"));
    assertTrue(gameLog.toString().contains("Cannot smell anything currently"));
    assertTrue(player.hasPlayerWon());
    assertTrue(player.isPlayerAlive());
    assertTrue(gameLog.toString().contains("Player has won the game"));
    assertFalse(gameLog.toString().contains("Player is dead"));
  }

  /**
   * Test expected error message for invalid firing of arrow.
   */
  @Test
  public void testExpectedArrowShootFailureInvalidDistance() {
    Dungeon dungeon = new DungeonImpl(5, 5, 5, false, 50, 1);
    Player player = new DungeonPlayer("Test", dungeon);
    StringReader input = new StringReader("s 1 -1");
    Appendable gameLog = new StringBuilder();
    GameView consoleView = new ConsoleView(input, gameLog, player);
    GameController c = new DungeonGameController(player, consoleView);
    c.playGame();
    assertEquals(player.getArrowCount(), 3);
    assertTrue(gameLog.toString().contains("Could not shoot"));
  }

}
