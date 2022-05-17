package dungeonmodel.treasure;

import java.util.Objects;

// Package private class to have common code between multiple gems types.
class AbstractGem implements Treasure {
  private final GemQuality quality;
  private final double value;
  private final GemTypes type;

  // Creates an AbstractGem object with the given parameters
  AbstractGem(GemTypes type, GemQuality quality, double multiplier)
          throws IllegalArgumentException {
    if (type == null || quality == null) {
      throw new IllegalArgumentException("Type/quality cannot be null");
    }

    if (multiplier <= 0) {
      throw new IllegalArgumentException("Multiplier must be positive");
    }
    this.quality = quality;
    this.value = quality.getValue() * multiplier;
    this.type = type;
  }

  @Override
  public GemTypes getTreasureType() {
    return this.type;
  }

  @Override
  public double getTreasureValue() {
    return this.value;
  }

  @Override
  public GemQuality getTreasureQuality() {
    return this.quality;
  }

  @Override
  public String toString() {
    return String.format("%s, Value: %d, Quality: %s", this.type.toString(),
            (int) this.value, this.quality.toString());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AbstractGem that = (AbstractGem) o;
    return Double.compare(that.value, value) == 0
            && quality == that.quality
            && type.equals(that.type);
  }

  @Override
  public int hashCode() {
    return Objects.hash(quality, value, type);
  }
}
