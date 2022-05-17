package dungeonmodel.monster;

import dungeonmodel.coordinates.Coordinates;
import dungeonmodel.weapon.Arrow;

/**
 * Interface to represent a monster that resides in some in-game location.
 * Provides methods to check health of monster and to attack monster.
 */
public interface Monster {
  /**
   * Check if monster is alive.
   *
   * @return true if monster is alive, false if not
   */
  boolean isAlive();

  /**
   * Check the number of arrows monster has been hit by.
   *
   * @return number of arrows monster has been hit by
   */
  int countOfArrowsHit();

  /**
   * Attack this monster with an arrow.
   *
   * @param arrow arrow that hits the monster
   */
  void arrowStrike(Arrow arrow);

  /**
   * Get the location of this monster.
   *
   * @return coordinates of monster's location
   */
  Coordinates getLocation();

}
