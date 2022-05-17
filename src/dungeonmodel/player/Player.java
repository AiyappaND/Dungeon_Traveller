package dungeonmodel.player;

import dungeonmodel.coordinates.Coordinates;

/**
 * Represents a player that can travel through a dungeon.
 * Can enter a dungeon at the starting location and travel wherever he wants inside it.
 * Can also pick up any treasure he finds inside it.
 */
public interface Player extends ReadOnlyPlayer {

  /**
   * Try to move player to a set of coordinates/new location in the dungeon.
   *
   * @param coordinates coordinates to be moved to
   * @return true if player has successfully moved, false if not
   */
  boolean moveTo(Coordinates coordinates);

  /**
   * Pick up treasure at the current location.
   *
   * @return true if any treasure was picked up, false if not
   */
  boolean pickUpTreasure();

  /**
   * Try to pick up arrow at current location.
   *
   * @return true if arrow was successfully picked up, false if not
   */
  boolean pickUpArrow();

  /**
   * Shoot an arrow in the given direction and distance.
   *
   * @param distance  distance to shoot the arrow to
   * @param direction direction of nearby cave
   * @return location where the arrow has landed
   */
  Coordinates shootArrow(int distance, Coordinates direction);

  /**
   * Resets the current player.
   * Removes all treasure/items picked up and changes settings to default settings.
   */
  void resetPlayer();
}
