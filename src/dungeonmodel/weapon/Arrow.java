package dungeonmodel.weapon;

import dungeonmodel.coordinates.Coordinates;
import dungeonmodel.dungeon.Dungeon;

/**
 * Interface to represent an Arrow.
 * An arrow is a weapon that a player can fire.
 */
public interface Arrow {
  /**
   * Shoot this arrow for a certain distance in a certain direction.
   *
   * @param distance  distance arrow has to be shot
   * @param direction direction arrow has to be shot in
   * @param source    source where the arrow is being fired from
   * @param dungeon   dungeon where the arrow is being fired
   */
  void shoot(int distance, Coordinates direction, Coordinates source, Dungeon dungeon);

  /**
   * Get the current location of the arrow.
   *
   * @return location of the arrow
   */
  Coordinates getCurrentLocation();
}
