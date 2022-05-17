package dungeonmodel.treasure;

/**
 * Represents a treasure that can be found in a dungeon.
 * These can be picked up by the travelers of the dungeon.
 */
public interface Treasure {

  /**
   * Get the name of the treasure.
   *
   * @return type of the treasure
   */
  GemTypes getTreasureType();

  /**
   * Get the value of the treasure.
   *
   * @return value of the treasure.
   */
  double getTreasureValue();

  /**
   * Get the quality of the treasure.
   *
   * @return quality of the treasure.
   */
  GemQuality getTreasureQuality();
}
