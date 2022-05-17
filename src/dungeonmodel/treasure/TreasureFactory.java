package dungeonmodel.treasure;

import java.util.ArrayList;
import java.util.List;

/**
 * Class which returns a list of all possible treasures.
 */
public class TreasureFactory {

  /**
   * Creates and returns a list of all possible treasures.
   *
   * @return list of all possible treasures
   */
  public static List<Treasure> getAllPossibleTreasure() {
    List<Treasure> treasureList = new ArrayList<>();
    treasureList.add(new Diamond(GemQuality.POOR));
    treasureList.add(new Ruby(GemQuality.POOR));
    treasureList.add(new Sapphire(GemQuality.POOR));
    treasureList.add(new Diamond(GemQuality.AVERAGE));
    treasureList.add(new Ruby(GemQuality.AVERAGE));
    treasureList.add(new Sapphire(GemQuality.AVERAGE));
    treasureList.add(new Diamond(GemQuality.HIGH));
    treasureList.add(new Ruby(GemQuality.HIGH));
    treasureList.add(new Sapphire(GemQuality.HIGH));
    return treasureList;
  }

}
