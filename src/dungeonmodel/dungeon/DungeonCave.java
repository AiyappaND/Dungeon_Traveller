package dungeonmodel.dungeon;

import java.util.ArrayList;
import java.util.List;

import dungeonmodel.coordinates.CaveCoordinates;
import dungeonmodel.coordinates.Coordinates;
import dungeonmodel.monster.Monster;
import dungeonmodel.treasure.Treasure;
import dungeonmodel.weapon.Arrow;

/**
 * Class representing a cave in a dungeon. May contain treasure and list of adjacent cave locations.
 */
public class DungeonCave implements Cave {

  private final Coordinates coordinates;
  private List<Treasure> treasure;
  private Arrow crookedArrow;
  private Monster monster;
  private Coordinates south;
  private Coordinates north;
  private Coordinates east;
  private Coordinates west;

  /**
   * Constructor to create a cave.
   *
   * @param coordinates location coordinates of the cave
   * @throws IllegalArgumentException if given coordinates are null
   */
  public DungeonCave(Coordinates coordinates) throws IllegalArgumentException {
    if (coordinates == null) {
      throw new IllegalArgumentException("Coordinates given cannot be null");
    }
    this.coordinates = coordinates;
    this.treasure = new ArrayList<>();
    this.crookedArrow = null;
    this.monster = null;
  }

  @Override
  public List<Treasure> viewTreasure() {
    return new ArrayList<>(this.treasure);
  }

  @Override
  public boolean isTunnel() {
    int adjacencyCount = 0;
    if (this.south == null) {
      adjacencyCount += 1;
    }
    if (this.north == null) {
      adjacencyCount += 1;
    }
    if (this.east == null) {
      adjacencyCount += 1;
    }
    if (this.west == null) {
      adjacencyCount += 1;
    }
    return (adjacencyCount == 2);
  }

  @Override
  public List<Coordinates> getAllAdjacent() {
    List<Coordinates> adjacencyList = new ArrayList<>();
    if (this.south != null) {
      adjacencyList.add(this.south);
    }
    if (this.north != null) {
      adjacencyList.add(this.north);
    }
    if (this.east != null) {
      adjacencyList.add(this.east);
    }
    if (this.west != null) {
      adjacencyList.add(this.west);
    }
    return adjacencyList;
  }

  @Override
  public Coordinates getCoordinates() {
    return new CaveCoordinates(this.coordinates.getXCoordinates(),
            this.coordinates.getYCoordinates());
  }

  @Override
  public void setAdjacent(Coordinates coordinates, int totalRows, int totalColumns)
          throws IllegalArgumentException {

    if (coordinates == null) {
      throw new IllegalArgumentException("Coordinates given cannot be null");
    }

    if (totalColumns <= 0 || totalRows <= 0) {
      throw new IllegalArgumentException("Total rows/columns always have to be positive");
    }

    int xOffSet = this.coordinates.getXCoordinates() - coordinates.getXCoordinates();

    if (xOffSet < -1) {
      xOffSet += totalRows;
    }
    if (xOffSet > 1) {
      xOffSet -= totalRows;
    }

    int yOffSet = this.coordinates.getYCoordinates() - coordinates.getYCoordinates();
    if (yOffSet < -1) {
      yOffSet += totalColumns;
    }
    if (yOffSet > 1) {
      yOffSet -= totalColumns;
    }

    if (xOffSet == -1 && yOffSet == 0) {
      this.south = coordinates;
    } else if (xOffSet == 1 && yOffSet == 0) {
      this.north = coordinates;
    } else if (xOffSet == 0 && yOffSet == -1) {
      this.east = coordinates;
    } else if (xOffSet == 0 && yOffSet == 1) {
      this.west = coordinates;
    } else {
      throw new IllegalArgumentException("Given coordinates cannot be adjacent");
    }
  }

  @Override
  public void addTreasure(Treasure treasure) throws IllegalArgumentException {
    if (treasure == null) {
      throw new IllegalArgumentException("Given treasure cannot be null");
    }
    if (this.isTunnel()) {
      throw new IllegalArgumentException("Treasure cannot be added to tunnels");
    }
    this.treasure.add(treasure);
  }

  @Override
  public List<Treasure> removeTreasure() {
    List<Treasure> returnTreasure = new ArrayList<>(this.treasure);
    this.treasure.clear();
    return returnTreasure;
  }

  @Override
  public boolean hasMonster() {
    return this.monster != null && this.monster.isAlive();
  }

  @Override
  public boolean hasArrow() {
    return this.crookedArrow != null;
  }

  @Override
  public Arrow removeArrow() {
    Arrow returnedArrow = this.crookedArrow;
    this.crookedArrow = null;
    return returnedArrow;
  }

  @Override
  public void addArrow(Arrow arrow) throws IllegalArgumentException {
    if (arrow == null) {
      throw new IllegalArgumentException("Null arrow provided");
    }
    if (this.crookedArrow != null) {
      throw new IllegalArgumentException("Arrow already exists in cave");
    }
    this.crookedArrow = arrow;
  }

  @Override
  public void addMonster(Monster monster) throws IllegalArgumentException {
    if (monster == null) {
      throw new IllegalArgumentException("Null monster provided");
    }
    if (this.monster != null) {
      throw new IllegalArgumentException("Monster already present in cave");
    }
    if (!(monster.getLocation().equals(this.coordinates))) {
      throw new IllegalArgumentException("Monster is in a different location");
    }

    this.monster = monster;
  }

  @Override
  public boolean isMonsterAlive() {
    if (this.monster != null) {
      return this.monster.isAlive();
    }
    return false;
  }

  @Override
  public int monsterArrowCount() {
    if (this.monster != null) {
      return this.monster.countOfArrowsHit();
    }
    return 0;
  }

  @Override
  public void hitMonster(Arrow arrow) {
    if (arrow == null) {
      throw new IllegalArgumentException("Arrow cannot be null");
    }
    if (this.monster != null) {
      this.monster.arrowStrike(arrow);
    }
  }

  @Override
  public void resetCave() {
    this.treasure = new ArrayList<>();
    this.crookedArrow = null;
    this.monster = null;
  }

  @Override
  public Coordinates getNorth() {
    return this.north;
  }

  @Override
  public Coordinates getSouth() {
    return this.south;
  }

  @Override
  public Coordinates getEast() {
    return this.east;
  }

  @Override
  public Coordinates getWest() {
    return this.west;
  }

  @Override
  public String toString() {
    StringBuilder resultBuilder = new StringBuilder();
    resultBuilder.append(String.format("Coordinates: %s\n", this.coordinates));
    if (this.north != null) {
      resultBuilder.append(String.format("North: %s\n", this.north));
    }
    if (this.south != null) {
      resultBuilder.append(String.format("South: %s\n", this.south));
    }
    if (this.east != null) {
      resultBuilder.append(String.format("East: %s\n", this.east));
    }
    if (this.west != null) {
      resultBuilder.append(String.format("West: %s\n", this.west));
    }
    resultBuilder.append("Treasures:\n");
    for (Treasure treasure :
            this.treasure) {
      resultBuilder.append(String.format("%s\n", treasure.toString()));
    }
    return resultBuilder.toString();
  }
}
