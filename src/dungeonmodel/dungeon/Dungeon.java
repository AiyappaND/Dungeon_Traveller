package dungeonmodel.dungeon;

import java.util.List;

import dungeonmodel.coordinates.Coordinates;
import dungeonmodel.treasure.Treasure;
import dungeonmodel.weapon.Arrow;

/**
 * Represents a dungeon.
 * A dungeon is a set of caves connected to each other. Each such cave can have treasure present.
 * A player can move through these, picking up and viewing treasure.
 * A dungeon has a start and end cave. Players always start at the start cave.
 */
public interface Dungeon extends ReadOnlyDungeon {

  /**
   * Pickup/remove all treasure present in a given coordinate pair/cave.
   *
   * @param coordinates coordinates of the cave
   * @return list of treasure present in the cave
   */
  List<Treasure> pickupTreasureInCave(Coordinates coordinates);

  /**
   * Pickup arrow from a location.
   *
   * @param coordinates coordinates of the location
   * @return Arrow present at the location
   */
  Arrow pickUpArrow(Coordinates coordinates);

  /**
   * Strike a monster with an arrow.
   *
   * @param arrow which strikes monster
   */
  void arrowStrike(Arrow arrow);


  /**
   * Resets the dungeon, keeping adjacency between caves as the same and having the same attributes.
   */
  void resetDungeon();

}
