import org.junit.Test;

import dungeonmodel.treasure.Diamond;
import dungeonmodel.treasure.GemQuality;
import dungeonmodel.treasure.GemTypes;
import dungeonmodel.treasure.Ruby;
import dungeonmodel.treasure.Sapphire;
import dungeonmodel.treasure.Treasure;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Class to test functionality of treasure/gem class.
 */
public class TestGems {

  // helper method to create a diamond of given quality
  protected Treasure diamond(GemQuality gemQuality) throws IllegalArgumentException {
    return new Diamond(gemQuality);
  }

  // helper method to create a ruby of given quality
  protected Treasure ruby(GemQuality gemQuality) throws IllegalArgumentException {
    return new Ruby(gemQuality);
  }

  // helper method to create a sapphire of given quality
  protected Treasure sapphire(GemQuality gemQuality) throws IllegalArgumentException {
    return new Sapphire(gemQuality);
  }

  /**
   * Test expected exception with null diamond quality.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testNullDiamond() {
    diamond(null);
  }

  /**
   * Test expected exception with null ruby quality.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testNullRuby() {
    ruby(null);
  }

  /**
   * Test expected exception with null sapphire quality.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testNullSapphire() {
    sapphire(null);
  }

  /**
   * Test that a diamond is created as expected.
   */
  @Test
  public void testDiamondData() {
    Treasure averageDiamond = diamond(GemQuality.AVERAGE);
    Treasure poorDiamond = diamond(GemQuality.POOR);
    Treasure highDiamond = diamond(GemQuality.HIGH);

    assertEquals(400, highDiamond.getTreasureValue(), 0.001);
    assertEquals(100, poorDiamond.getTreasureValue(), 0.001);
    assertEquals(200, averageDiamond.getTreasureValue(), 0.001);

    assertEquals(GemQuality.POOR, poorDiamond.getTreasureQuality());
    assertEquals(GemQuality.AVERAGE, averageDiamond.getTreasureQuality());
    assertEquals(GemQuality.HIGH, highDiamond.getTreasureQuality());

    assertEquals(GemTypes.DIAMOND, highDiamond.getTreasureType());
    assertEquals(GemTypes.DIAMOND, poorDiamond.getTreasureType());
    assertEquals(GemTypes.DIAMOND, averageDiamond.getTreasureType());

    assertEquals("DIAMOND, Value: 100, Quality: POOR", poorDiamond.toString());
    assertEquals("DIAMOND, Value: 200, Quality: AVERAGE", averageDiamond.toString());
    assertEquals("DIAMOND, Value: 400, Quality: HIGH", highDiamond.toString());
  }

  /**
   * Test that a ruby is created as expected.
   */
  @Test
  public void testRubyData() {
    Treasure averageRuby = ruby(GemQuality.AVERAGE);
    Treasure poorRuby = ruby(GemQuality.POOR);
    Treasure highRuby = ruby(GemQuality.HIGH);

    assertEquals(300, highRuby.getTreasureValue(), 0.001);
    assertEquals(75, poorRuby.getTreasureValue(), 0.001);
    assertEquals(150, averageRuby.getTreasureValue(), 0.001);

    assertEquals(GemQuality.POOR, poorRuby.getTreasureQuality());
    assertEquals(GemQuality.AVERAGE, averageRuby.getTreasureQuality());
    assertEquals(GemQuality.HIGH, highRuby.getTreasureQuality());

    assertEquals(GemTypes.RUBY, highRuby.getTreasureType());
    assertEquals(GemTypes.RUBY, poorRuby.getTreasureType());
    assertEquals(GemTypes.RUBY, averageRuby.getTreasureType());

    assertEquals("RUBY, Value: 75, Quality: POOR", poorRuby.toString());
    assertEquals("RUBY, Value: 150, Quality: AVERAGE", averageRuby.toString());
    assertEquals("RUBY, Value: 300, Quality: HIGH", highRuby.toString());
  }

  /**
   * Test that a sapphire is created as expected.
   */
  @Test
  public void testSapphireData() {
    Treasure averageSapphire = sapphire(GemQuality.AVERAGE);
    Treasure poorSapphire = sapphire(GemQuality.POOR);
    Treasure highSapphire = sapphire(GemQuality.HIGH);

    assertEquals(200, highSapphire.getTreasureValue(), 0.001);
    assertEquals(50, poorSapphire.getTreasureValue(), 0.001);
    assertEquals(100, averageSapphire.getTreasureValue(), 0.001);

    assertEquals(GemQuality.POOR, poorSapphire.getTreasureQuality());
    assertEquals(GemQuality.AVERAGE, averageSapphire.getTreasureQuality());
    assertEquals(GemQuality.HIGH, highSapphire.getTreasureQuality());

    assertEquals(GemTypes.SAPPHIRE, highSapphire.getTreasureType());
    assertEquals(GemTypes.SAPPHIRE, poorSapphire.getTreasureType());
    assertEquals(GemTypes.SAPPHIRE, averageSapphire.getTreasureType());

    assertEquals("SAPPHIRE, Value: 50, Quality: POOR", poorSapphire.toString());
    assertEquals("SAPPHIRE, Value: 100, Quality: AVERAGE", averageSapphire.toString());
    assertEquals("SAPPHIRE, Value: 200, Quality: HIGH", highSapphire.toString());
  }

  /**
   * Test overridden equality and hashcode methods.
   */
  @Test
  public void testEquality() {
    Treasure averageDiamond = diamond(GemQuality.AVERAGE);
    Treasure poorDiamond = diamond(GemQuality.POOR);
    Treasure highDiamond = diamond(GemQuality.HIGH);

    Treasure averageRuby = ruby(GemQuality.AVERAGE);
    Treasure poorRuby = ruby(GemQuality.POOR);
    Treasure highRuby = ruby(GemQuality.HIGH);

    Treasure averageSapphire = sapphire(GemQuality.AVERAGE);
    Treasure poorSapphire = sapphire(GemQuality.POOR);
    Treasure highSapphire = sapphire(GemQuality.HIGH);

    assertEquals(averageDiamond, averageDiamond);
    assertEquals(poorDiamond, poorDiamond);
    assertEquals(highDiamond, highDiamond);

    assertEquals(averageRuby, averageRuby);
    assertEquals(poorRuby, poorRuby);
    assertEquals(highRuby, highRuby);

    assertEquals(averageSapphire, averageSapphire);
    assertEquals(poorSapphire, poorSapphire);
    assertEquals(highSapphire, highSapphire);

    assertNotEquals(averageDiamond, averageRuby);
    assertNotEquals(averageSapphire, averageRuby);

    Treasure averageDiamond2 = diamond(GemQuality.AVERAGE);
    Treasure poorRuby2 = ruby(GemQuality.POOR);
    Treasure highSapphire2 = sapphire(GemQuality.HIGH);

    assertEquals(averageDiamond, averageDiamond2);
    assertEquals(poorRuby, poorRuby2);
    assertEquals(highSapphire, highSapphire2);
  }

  /**
   * Test overridden equality and hashcode methods.
   */
  @Test
  public void testHashCode() {
    Treasure averageDiamond = diamond(GemQuality.AVERAGE);
    Treasure poorDiamond = diamond(GemQuality.POOR);
    Treasure highDiamond = diamond(GemQuality.HIGH);

    Treasure averageRuby = ruby(GemQuality.AVERAGE);
    Treasure poorRuby = ruby(GemQuality.POOR);
    Treasure highRuby = ruby(GemQuality.HIGH);

    Treasure averageSapphire = sapphire(GemQuality.AVERAGE);
    Treasure poorSapphire = sapphire(GemQuality.POOR);
    Treasure highSapphire = sapphire(GemQuality.HIGH);

    assertEquals(averageDiamond.hashCode(), averageDiamond.hashCode());
    assertEquals(poorDiamond.hashCode(), poorDiamond.hashCode());
    assertEquals(highDiamond.hashCode(), highDiamond.hashCode());

    assertEquals(averageRuby.hashCode(), averageRuby.hashCode());
    assertEquals(poorRuby.hashCode(), poorRuby.hashCode());
    assertEquals(highRuby.hashCode(), highRuby.hashCode());

    assertEquals(averageSapphire.hashCode(), averageSapphire.hashCode());
    assertEquals(poorSapphire.hashCode(), poorSapphire.hashCode());
    assertEquals(highSapphire.hashCode(), highSapphire.hashCode());

    Treasure averageDiamond2 = diamond(GemQuality.AVERAGE);
    Treasure poorRuby2 = ruby(GemQuality.POOR);
    Treasure highSapphire2 = sapphire(GemQuality.HIGH);

    assertEquals(averageDiamond.hashCode(), averageDiamond2.hashCode());
    assertEquals(poorRuby.hashCode(), poorRuby2.hashCode());
    assertEquals(highSapphire.hashCode(), highSapphire2.hashCode());
  }

}
