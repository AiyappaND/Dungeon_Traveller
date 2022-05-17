package dungeonmodel.treasure;

/**
 * Represents a Sapphire.
 * Sapphires are common, so they are worth only the base price of all gems.
 */
public class Sapphire extends AbstractGem {
  private static final int SAPPHIRE_MULTIPLIER = 1;

  /**
   * Creates a sapphire with the specified quality.
   *
   * @param quality quality of the sapphire
   * @throws IllegalArgumentException if quality is null
   */
  public Sapphire(GemQuality quality) throws IllegalArgumentException {
    super(GemTypes.SAPPHIRE, quality, SAPPHIRE_MULTIPLIER);
  }
}
