package dungeonmodel.monster;

import dungeonmodel.coordinates.Coordinates;
import dungeonmodel.weapon.Arrow;

/**
 * Class which represents an Otyugh, a monster which resides in a dungeon.
 * It takes two hits to kill one, and if an Otyugh is injured, any player has a chance to escape.
 */
public class Otyugh implements Monster {

  private final Coordinates location;
  private int arrowsHit;

  /**
   * Public constructor to create an Otyugh.
   *
   * @param location location where the Otyugh resides
   * @throws IllegalArgumentException if given location is invalid/null
   */
  public Otyugh(Coordinates location) throws IllegalArgumentException {

    if (location == null) {
      throw new IllegalArgumentException("Location cannot be null");
    }
    this.arrowsHit = 0;
    this.location = location;
  }

  @Override
  public boolean isAlive() {
    return ((this.arrowsHit < 2));
  }

  @Override
  public int countOfArrowsHit() {
    return this.arrowsHit;
  }

  @Override
  public void arrowStrike(Arrow arrow) {
    if (arrow == null) {
      throw new IllegalArgumentException("Arrow cannot be null");
    }
    if (arrow.getCurrentLocation().equals(this.location)) {
      this.arrowsHit += 1;
    }
  }

  @Override
  public Coordinates getLocation() {
    return this.location;
  }

}
