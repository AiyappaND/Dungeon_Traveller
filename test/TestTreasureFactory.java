import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import dungeonmodel.treasure.Diamond;
import dungeonmodel.treasure.GemQuality;
import dungeonmodel.treasure.Ruby;
import dungeonmodel.treasure.Sapphire;
import dungeonmodel.treasure.Treasure;
import dungeonmodel.treasure.TreasureFactory;

import static org.junit.Assert.assertTrue;

/**
 * Class to test functionality of treasure factory.
 */
public class TestTreasureFactory {

  /**
   * Test that treasure factory returns all possible gems/treasures.
   */
  @Test
  public void testTreasureFactory() {
    List<Treasure> expectedList = new ArrayList<>();
    expectedList.add(new Diamond(GemQuality.POOR));
    expectedList.add(new Ruby(GemQuality.POOR));
    expectedList.add(new Sapphire(GemQuality.POOR));
    expectedList.add(new Diamond(GemQuality.AVERAGE));
    expectedList.add(new Ruby(GemQuality.AVERAGE));
    expectedList.add(new Sapphire(GemQuality.AVERAGE));
    expectedList.add(new Diamond(GemQuality.HIGH));
    expectedList.add(new Ruby(GemQuality.HIGH));
    expectedList.add(new Sapphire(GemQuality.HIGH));

    assertTrue(expectedList.containsAll(TreasureFactory.getAllPossibleTreasure()));
    assertTrue(TreasureFactory.getAllPossibleTreasure().containsAll(expectedList));
  }
}
