package dungeonmodel.treasure;

/**
 * Determines the quality of a gem and its base value.
 * Higher quality gem has more value.
 */
public enum GemQuality {
  POOR(50),
  AVERAGE(100),
  HIGH(200);

  private final int value;

  /**
   * Creates the specified gem quality and sets a base value.
   *
   * @param value base value of the quality
   */
  GemQuality(int value) {
    this.value = value;
  }

  /**
   * Returns the base value of the gem depending on its quality.
   *
   * @return the base value of the gem quality
   */
  public int getValue() {
    return this.value;
  }
}
