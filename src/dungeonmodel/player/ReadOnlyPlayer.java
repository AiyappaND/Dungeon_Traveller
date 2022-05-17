package dungeonmodel.player;

import java.util.List;

import dungeonmodel.coordinates.Coordinates;
import dungeonmodel.dungeon.MonsterSmell;
import dungeonmodel.treasure.Treasure;

/**
 * Representation of a Player with methods to only retrieve data of the player.
 * Does not contain any mutation operations.
 */
public interface ReadOnlyPlayer {
  /**
   * Get the name of the player.
   *
   * @return player name
   */

  String getName();

  /**
   * Get the current coordinates of the player.
   *
   * @return current coordinates/location of the player
   */

  Coordinates getCurrentCoordinates();

  /**
   * Get all adjacent/possible paths for the player from current location.
   *
   * @return list of all adjacent coordinates
   */
  List<Coordinates> getPossibleMoves();

  /**
   * Return the list of current treasure player is holding.
   *
   * @return list of player's treasure
   */
  List<Treasure> getCurrentTreasure();

  /**
   * View all the treasure at the current cave/location.
   *
   * @return list of all treasure present at the current location
   */
  List<Treasure> viewTreasureAtCurrentLocation();

  /**
   * Check if current location where player is present, has an arrow.
   *
   * @return true if arrow present, false if not
   */
  boolean currentLocationHasArrow();

  /**
   * Returns number of arrows present with player.
   *
   * @return number of arrows with player.
   */
  int getArrowCount();

  /**
   * Check if player is alive.
   *
   * @return true if player is alive, false if not
   */
  boolean isPlayerAlive();

  /**
   * Check smell in current player location.
   *
   * @return MonsterSmell representing proximity of nearby monsters
   */
  MonsterSmell getSmellInCurrentCave();

  /**
   * Check if player has won the game.
   *
   * @return true if player has won, false otherwise
   */
  boolean hasPlayerWon();

  /**
   * Get coordinates to the north of the player, if it is reachable.
   *
   * @return reachable coordinates to the north of the player, null if none exist
   */
  Coordinates getNorth();

  /**
   * Get coordinates to the south of the player, if it is reachable.
   *
   * @return reachable coordinates to the south of the player, null if none exist
   */
  Coordinates getSouth();

  /**
   * Get coordinates to the east of the player, if it is reachable.
   *
   * @return reachable coordinates to the east of the player, null if none exist
   */
  Coordinates getEast();

  /**
   * Get coordinates to the west of the player, if it is reachable.
   *
   * @return reachable coordinates to the west of the player, null if none exist
   */
  Coordinates getWest();
}
