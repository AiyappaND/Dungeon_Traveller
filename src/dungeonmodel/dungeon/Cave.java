package dungeonmodel.dungeon;

import java.util.List;

import dungeonmodel.coordinates.Coordinates;
import dungeonmodel.monster.Monster;
import dungeonmodel.treasure.Treasure;
import dungeonmodel.weapon.Arrow;

/**
 * Represents a standalone location inside a dungeon, and can be used to go to other caves.
 * Has a set of coordinates representing which row and column it belongs to.
 * May also contain treasure.
 */
public interface Cave {

  /**
   * View the treasure inside this cave.
   *
   * @return list of treasure present in this cave
   */
  List<Treasure> viewTreasure();

  /**
   * Checks if this is a tunnel or not.
   *
   * @return true if contains only two connecting paths, false otherwise
   */
  boolean isTunnel();

  /**
   * Get all adjacent caves that can be reached from here.
   *
   * @return list of adjacent coordinates
   */
  List<Coordinates> getAllAdjacent();

  /**
   * Get this cave's coordinates.
   *
   * @return th coordinates of this cave
   */
  Coordinates getCoordinates();

  /**
   * Set a coordinate pair as adjacent to this cave/create a path to a coordinate.
   *
   * @param coordinates  to create path to
   * @param totalRows    total rows in the dungeon (used to calculate wrapping edges)
   * @param totalColumns total columns in the dungeon (used to calculate wrapping edges)
   */
  void setAdjacent(Coordinates coordinates, int totalRows, int totalColumns);

  /**
   * Add some treasure to the cave.
   *
   * @param treasure treasure to be added
   */
  void addTreasure(Treasure treasure);

  /**
   * Remove all treasure from the cave.
   *
   * @return list of treasures present in the cave
   */
  List<Treasure> removeTreasure();

  /**
   * Check if the current cave has a monster in it.
   *
   * @return true if monster present/false if not
   */
  boolean hasMonster();

  /**
   * Check if the current cave has an arrow in it.
   *
   * @return true if arrow present/false if not
   */
  boolean hasArrow();

  /**
   * Remove/pickup arrow from current location.
   *
   * @return arrow present in this cave (null if none)
   */
  Arrow removeArrow();

  /**
   * Add an arrow to current cave.
   *
   * @param arrow arrow to be added
   */
  void addArrow(Arrow arrow);

  /**
   * Add a monster to current cave.
   *
   * @param monster monster to be added
   */
  void addMonster(Monster monster);

  /**
   * Check if monster is alive in cave.
   *
   * @return true if monster is alive/false if not
   */
  boolean isMonsterAlive();

  /**
   * Check how many arrows the monster in this cave has been hit by.
   *
   * @return number of arrows monster has been hit by
   */
  int monsterArrowCount();

  /**
   * Hit a monster in the cave with an arrow.
   *
   * @param arrow Arrow which is striking the monster.
   */
  void hitMonster(Arrow arrow);

  /**
   * Resets the cave, removing all items/entities inside the cave. Adjacency is unchanged.
   */
  void resetCave();

  /**
   * Get cave north of this cave, if it exists.
   *
   * @return cave to the north if an adjacent cave exists, null otherwise
   */
  Coordinates getNorth();

  /**
   * Get cave south of this cave, if it exists.
   *
   * @return cave to the south if an adjacent cave exists, null otherwise
   */
  Coordinates getSouth();

  /**
   * Get cave east of this cave, if it exists.
   *
   * @return cave to the east if an adjacent cave exists, null otherwise
   */
  Coordinates getEast();

  /**
   * Get cave west of this cave, if it exists.
   *
   * @return cave to the west if an adjacent cave exists, null otherwise
   */
  Coordinates getWest();

}
