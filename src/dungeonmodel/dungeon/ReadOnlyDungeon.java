package dungeonmodel.dungeon;

import java.util.List;

import dungeonmodel.coordinates.Coordinates;
import dungeonmodel.treasure.Treasure;

/**
 * Representation of a Dungeon with methods to only retrieve data from the dungeon.
 * Does not contain any mutation operations.
 */
public interface ReadOnlyDungeon {

  /**
   * Get the dimensions of the cave.
   *
   * @return a list with first element number of rows, and second element being number of columns.
   */
  List<Integer> getDimensions();

  /**
   * View the treasure in a given coordinate pair without picking it up.
   *
   * @param coordinates coordinates of cave to be viewed
   * @return list of treasure present in that cave
   */
  List<Treasure> viewTreasureInCave(Coordinates coordinates);

  /**
   * Return the start/entrance of this dungeon.
   *
   * @return start location coordinates
   */
  Coordinates getStartCave();

  /**
   * Return the end location of this dungeon.
   *
   * @return end location coordinates
   */
  Coordinates getEndCave();

  /**
   * Get all adjacent paths from a given coordinate/location.
   *
   * @param coordinates coordinates to be checked
   * @return list of all adjacent paths (north, south, east, west)
   */
  List<Coordinates> getAdjacent(Coordinates coordinates);

  /**
   * Check if a given location has an arrow.
   *
   * @param coordinates coordinates to be checked
   * @return true if arrow present, false otherwise
   */
  boolean hasArrow(Coordinates coordinates);

  /**
   * Check if a given location has a monster.
   *
   * @param coordinates coordinates to be checked
   * @return true if monster present, false otherwise
   */
  boolean hasMonster(Coordinates coordinates);

  /**
   * Return smell of any nearby monsters from a given coordinate.
   *
   * @param coordinates coordinate to check smell in
   * @return Monster Smell corresponding to given location.
   */
  MonsterSmell getSmell(Coordinates coordinates);

  /**
   * Check how many arrows a monster at a location has been hit by.
   *
   * @param coordinates coordinates of monster location
   * @return count of arrows monster has been hit by
   */
  int monsterArrowCount(Coordinates coordinates);

  /**
   * Returns coordinates to the north of the given coordinates, if one exists.
   *
   * @param coordinates coordinates for which north coordinates has to be fetched
   * @return the coordinates north of the given coordinates, or null if none exist
   */
  Coordinates getNorth(Coordinates coordinates);

  /**
   * Returns coordinates to the south of the given coordinates, if one exists.
   *
   * @param coordinates coordinates for which south coordinates has to be fetched
   * @return the coordinates south of the given coordinates, or null if none exist
   */
  Coordinates getSouth(Coordinates coordinates);

  /**
   * Returns coordinates to the east of the given coordinates, if one exists.
   *
   * @param coordinates coordinates for which east coordinates has to be fetched
   * @return the coordinates east of the given coordinates, or null if none exist
   */
  Coordinates getEast(Coordinates coordinates);

  /**
   * Returns coordinates to the west of the given coordinates, if one exists.
   *
   * @param coordinates coordinates for which west coordinates has to be fetched
   * @return the coordinates west of the given coordinates, or null if none exist
   */
  Coordinates getWest(Coordinates coordinates);

}
