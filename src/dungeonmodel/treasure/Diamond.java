package dungeonmodel.treasure;


/**
 * Represents a Diamond.
 * Diamonds are the rarest gems, so they are worth twice the base price of all gems.
 */
public class Diamond extends AbstractGem {
  private static final int DIAMOND_MULTIPLIER = 2;

  /**
   * Creates a diamond with the specified quality.
   *
   * @param quality quality of the diamond
   * @throws IllegalArgumentException if quality is null
   */
  public Diamond(GemQuality quality) throws IllegalArgumentException {
    super(GemTypes.DIAMOND, quality, DIAMOND_MULTIPLIER);
  }
}
