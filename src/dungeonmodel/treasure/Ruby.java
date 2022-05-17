package dungeonmodel.treasure;

/**
 * Represents a Ruby.
 * Diamonds are moderately rare, so they are worth 1.5 times the base price of all gems.
 */
public class Ruby extends AbstractGem {
  private static final double RUBY_MULTIPLIER = 1.5;

  /**
   * Creates a ruby with the specified quality.
   *
   * @param quality quality of the ruby
   * @throws IllegalArgumentException if quality is null
   */
  public Ruby(GemQuality quality) throws IllegalArgumentException {
    super(GemTypes.RUBY, quality, RUBY_MULTIPLIER);
  }
}
