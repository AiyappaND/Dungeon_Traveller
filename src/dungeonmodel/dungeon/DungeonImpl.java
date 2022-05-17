package dungeonmodel.dungeon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import dungeonmodel.coordinates.CaveCoordinates;
import dungeonmodel.coordinates.Coordinates;
import dungeonmodel.monster.Otyugh;
import dungeonmodel.treasure.Treasure;
import dungeonmodel.treasure.TreasureFactory;
import dungeonmodel.weapon.Arrow;
import dungeonmodel.weapon.CrookedArrow;

/**
 * Class representing a dungeon, with a two-dimensional grid of caves in it.
 */
public class DungeonImpl implements Dungeon {

  private final int rows;
  private final int columns;
  private final Cave[][] allCaves;
  private final Cave startCave;
  private final Cave endCave;
  private final int treasureAndArrowPercent;
  private final int numberOfMonsters;

  /**
   * Constructor to initialize a dungeon.
   *
   * @param rows                    number of rows
   * @param columns                 number of columns
   * @param interconnectivity       the degree of interconnectivity
   * @param isWrapping              true if the dungeon should be wrapping, false otherwise
   * @param treasureAndArrowPercent percentage of caves which will have treasure and arrows
   *                                present in them
   * @param numberOfMonsters        number of monsters to be added in the dungeon
   * @throws IllegalArgumentException if any of the given arguments are invalid
   */
  public DungeonImpl(int rows, int columns, int interconnectivity, boolean isWrapping,
                     int treasureAndArrowPercent, int numberOfMonsters)
          throws IllegalArgumentException {
    if (rows <= 0 || columns <= 0) {
      throw new IllegalArgumentException("Rows/columns cannot be zero/negative");
    }

    if (interconnectivity < 0) {
      throw new IllegalArgumentException("Interconnectivity cannot be negative");
    }

    if (treasureAndArrowPercent <= 0) {
      throw new IllegalArgumentException("Treasure percentage cannot be zero/negative");
    }

    if (treasureAndArrowPercent > 100) {
      throw new IllegalArgumentException("Treasure percentage cannot be more than hundred");
    }

    if (numberOfMonsters <= 0) {
      throw new IllegalArgumentException("Number of monsters must always be positive");
    }

    this.allCaves = new DungeonCave[rows][columns];
    this.rows = rows;
    this.columns = columns;

    Set<Set<Coordinates>> allPossibleEdges = new HashSet<>(this.getNonWrappingEdges());

    for (int row = 0; row < rows; row++) {
      for (int column = 0; column < columns; column++) {
        this.allCaves[row][column] = new DungeonCave(new CaveCoordinates(row, column));
      }
    }

    if (isWrapping) {
      allPossibleEdges.addAll(this.getWrappingEdges());
    }

    this.useKruskalGenerateEdges(interconnectivity, allPossibleEdges);
    this.numberOfMonsters = numberOfMonsters;
    this.treasureAndArrowPercent = treasureAndArrowPercent;
    this.addTreasureToGivenPercentage(TreasureFactory.getAllPossibleTreasure());
    this.addArrowsToGivenPercentage();
    List<Cave> startAndEnd = this.startAndEndCave();
    this.startCave = startAndEnd.get(0);
    this.endCave = startAndEnd.get(1);
    this.addMonsters();
  }

  // Iterates through all caves and adds arrows to a given percentage of them
  private void addArrowsToGivenPercentage() {
    List<Cave> caves = this.getAllCaves();
    Collections.shuffle(caves);
    int numCavesWithArrows = (int) ((this.treasureAndArrowPercent / 100.0) * caves.size());
    List<Cave> cavesToAddArrows = caves.subList(0, numCavesWithArrows);
    for (Cave cave :
            cavesToAddArrows) {
      cave.addArrow(new CrookedArrow());
    }
  }

  // Iterates through all caves and adds treasure to a given percentage of them
  private void addTreasureToGivenPercentage(List<Treasure> allPossibleTreasure) {
    List<Cave> cavesNotTunnels = this.getAllCavesNotTunnels();
    Collections.shuffle(cavesNotTunnels);
    Random randomSelector = new Random();
    int cavesWithTreasure = (int) ((this.treasureAndArrowPercent / 100.0) * cavesNotTunnels.size());
    List<Cave> cavesToAddTreasure = cavesNotTunnels.subList(0, cavesWithTreasure);
    for (Cave cave :
            cavesToAddTreasure) {
      Collections.shuffle(allPossibleTreasure);
      int upperBound = randomSelector.nextInt(allPossibleTreasure.size());
      if (upperBound < 2) {
        upperBound += 1;
      }
      List<Treasure> sublist = allPossibleTreasure.subList(0, upperBound);
      for (Treasure treasure :
              sublist) {
        cave.addTreasure(treasure);
      }
    }
  }

  // Adds monsters to a specified number of caves
  private void addMonsters() throws IllegalArgumentException {
    List<Cave> cavesNotTunnels = this.getAllCavesNotTunnels();
    Collections.shuffle(cavesNotTunnels);

    if (cavesNotTunnels.size() - 1 < this.numberOfMonsters) {
      throw new IllegalArgumentException("Number of monsters exceeds number of available caves");
    }
    this.endCave.addMonster(new Otyugh(this.endCave.getCoordinates()));
    cavesNotTunnels.remove(this.endCave);
    cavesNotTunnels.remove(this.startCave);
    List<Cave> cavesToAddMonsters = cavesNotTunnels.subList(0, this.numberOfMonsters - 1);

    for (Cave cave : cavesToAddMonsters) {
      cave.addMonster(new Otyugh(cave.getCoordinates()));
    }

  }

  //https://stackoverflow.com/questions/8379785/
  // how-does-a-breadth-first-search-work-when-looking-for-shortest-path
  private List<Cave> startAndEndCave() throws IllegalArgumentException {

    List<Cave> startList = this.getAllCavesNotTunnels();

    Cave finalEndCave = null;
    Cave finalStartCave = null;

    Collections.shuffle(startList);
    boolean found = false;
    while (!startList.isEmpty() && !found) {
      Cave potentialStart = startList.remove(0);
      List<Cave> listOfCaves = this.getAllCavesNotTunnels();
      listOfCaves.remove(potentialStart);
      Collections.shuffle(listOfCaves);
      for (Cave potentialEndCave : listOfCaves) {
        Set<Cave> visited = new HashSet<>();
        List<Cave> visitedOrder = new ArrayList<>();
        Map<Cave, Integer> distanceMap = new HashMap<>();

        distanceMap.put(potentialStart, 0);
        visitedOrder.add(potentialStart);
        while (!visitedOrder.isEmpty() && !found) {
          Cave currentCave = visitedOrder.remove(0);
          visited.add(currentCave);
          int currentDistance = distanceMap.get(currentCave);
          List<Coordinates> adjacency = currentCave.getAllAdjacent();
          for (Coordinates coordinate : adjacency) {
            Cave adjacentCave =
                    this.allCaves[coordinate.getXCoordinates()][coordinate.getYCoordinates()];
            if (!visited.contains(adjacentCave)) {
              if (!distanceMap.containsKey(adjacentCave) && !visitedOrder.contains(adjacentCave)) {
                distanceMap.put(adjacentCave, currentDistance + 1);
                visitedOrder.add(adjacentCave);
              }
              if (adjacentCave.equals(potentialEndCave)) {
                if (distanceMap.get(adjacentCave) >= 5) {
                  finalEndCave = potentialEndCave;
                  finalStartCave = potentialStart;
                  found = true;
                }
              }
            }
          }
        }
      }
    }
    if (finalEndCave == null) {
      throw new IllegalArgumentException("No paths of minimum distance 5 found");
    }
    return new ArrayList<>((Arrays.asList(finalStartCave, finalEndCave)));
  }

  // Returns a list of caves that are not tunnels
  private List<Cave> getAllCavesNotTunnels() {
    List<Cave> startList = new ArrayList<>();
    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < columns; col++) {
        if (!this.allCaves[row][col].isTunnel()) {
          startList.add(this.allCaves[row][col]);
        }
      }
    }
    return startList;
  }

  // Returns a list of all caves and tunnels
  private List<Cave> getAllCaves() {
    List<Cave> startList = new ArrayList<>();
    for (int row = 0; row < rows; row++) {
      startList.addAll(Arrays.asList(this.allCaves[row]).subList(0, columns));
    }
    return startList;
  }


  // Generates a set of paths between the caves with the given degree of interconnectivity.
  private void useKruskalGenerateEdges(int interconnectivity,
                                       Set<Set<Coordinates>> allPossibleEdges) {
    List<Set<Cave>> kruskalTrackerSet = new ArrayList<>();
    Set<Set<Coordinates>> discardedEdges = new HashSet<>();

    for (Set<Coordinates> edge : allPossibleEdges) {
      Coordinates c1 = (Coordinates) edge.toArray()[0];
      Coordinates c2 = (Coordinates) edge.toArray()[1];
      Cave cave1 = this.allCaves[c1.getXCoordinates()][c1.getYCoordinates()];
      Cave cave2 = this.allCaves[c2.getXCoordinates()][c2.getYCoordinates()];

      Set<Cave> caveSet1 = null;
      Set<Cave> caveSet2 = null;

      for (Set<Cave> caveSet : kruskalTrackerSet) {
        if (caveSet.contains(cave1)) {
          caveSet1 = caveSet;
        }
        if (caveSet.contains(cave2)) {
          caveSet2 = caveSet;
        }
      }
      if (caveSet1 == null && caveSet2 == null) {
        cave1.setAdjacent(cave2.getCoordinates(), this.rows, this.columns);
        cave2.setAdjacent(cave1.getCoordinates(), this.rows, this.columns);
        kruskalTrackerSet.add(new HashSet<>(Arrays.asList(cave1, cave2)));
      } else if (caveSet1 == null) {
        caveSet2.add(cave1);
        cave1.setAdjacent(cave2.getCoordinates(), this.rows, this.columns);
        cave2.setAdjacent(cave1.getCoordinates(), this.rows, this.columns);
      } else if (caveSet2 == null) {
        caveSet1.add(cave2);
        cave1.setAdjacent(cave2.getCoordinates(), this.rows, this.columns);
        cave2.setAdjacent(cave1.getCoordinates(), this.rows, this.columns);
      } else if (caveSet1.equals(caveSet2)) {
        discardedEdges.add(edge);
      } else {
        caveSet1.addAll(caveSet2);
        kruskalTrackerSet.remove(caveSet2);
        cave1.setAdjacent(cave2.getCoordinates(), this.rows, this.columns);
        cave2.setAdjacent(cave1.getCoordinates(), this.rows, this.columns);
      }
    }

    if (interconnectivity > discardedEdges.size()) {
      throw new IllegalArgumentException("Degree of interconnectivity "
              + "not possible with current dimensions");
    }

    for (int size = 0; size < interconnectivity; size++) {
      Random random = new Random();
      int randomIndex = random.nextInt(discardedEdges.size());
      Set<Coordinates> edge = (Set<Coordinates>) discardedEdges.toArray()[randomIndex];
      Coordinates c1 = (Coordinates) edge.toArray()[0];
      Coordinates c2 = (Coordinates) edge.toArray()[1];
      Cave cave1 = this.allCaves[c1.getXCoordinates()][c1.getYCoordinates()];
      Cave cave2 = this.allCaves[c2.getXCoordinates()][c2.getYCoordinates()];
      cave1.setAdjacent(cave2.getCoordinates(), this.rows, this.columns);
      cave2.setAdjacent(cave1.getCoordinates(), this.rows, this.columns);
      discardedEdges.remove(edge);
    }
  }

  // Return all possible wrapping edges.
  private Set<Set<Coordinates>> getWrappingEdges() {
    Set<Set<Coordinates>> allWrappingEdges = new HashSet<>();
    for (int column = 0; column < this.columns; column++) {
      Set<Coordinates> wrappingEdge = new HashSet<>();
      wrappingEdge.add(new CaveCoordinates(0, column));
      wrappingEdge.add(new CaveCoordinates(this.rows - 1, column));
      allWrappingEdges.add(wrappingEdge);
    }
    for (int row = 0; row < this.rows; row++) {
      Set<Coordinates> wrappingEdge = new HashSet<>();
      wrappingEdge.add(new CaveCoordinates(row, 0));
      wrappingEdge.add(new CaveCoordinates(row, this.columns - 1));
      allWrappingEdges.add(wrappingEdge);
    }
    return allWrappingEdges;
  }

  // Return all possible non wrapping edges
  private Set<Set<Coordinates>> getNonWrappingEdges() {
    Set<Set<Coordinates>> nonWrappingEdges = new HashSet<>();
    for (int row = 0; row < this.rows; row++) {
      for (int column = 0; column < this.columns; column++) {
        int adjRow = row + 1;
        int adjColumn = column + 1;
        if (adjRow < this.rows) {
          Set<Coordinates> edge = new HashSet<>();
          edge.add(new CaveCoordinates(row, column));
          edge.add(new CaveCoordinates(adjRow, column));
          nonWrappingEdges.add(edge);
        }
        if (adjColumn < this.columns) {
          Set<Coordinates> edge = new HashSet<>();
          edge.add(new CaveCoordinates(row, column));
          edge.add(new CaveCoordinates(row, adjColumn));
          nonWrappingEdges.add(edge);
        }
      }
    }
    return nonWrappingEdges;
  }

  @Override
  public List<Integer> getDimensions() {
    List<Integer> dimensions = new ArrayList<>();
    dimensions.add(this.rows);
    dimensions.add(this.columns);

    return dimensions;
  }

  @Override
  public List<Treasure> viewTreasureInCave(Coordinates coordinates) {
    if (coordinates == null) {
      throw new IllegalArgumentException("Coordinates cant be null");
    }
    return this.allCaves[coordinates.getXCoordinates()]
            [coordinates.getYCoordinates()].viewTreasure();
  }

  @Override
  public List<Treasure> pickupTreasureInCave(Coordinates coordinates) {
    if (coordinates == null) {
      throw new IllegalArgumentException("Coordinates cant be null");
    }
    return this.allCaves[coordinates.getXCoordinates()]
            [coordinates.getYCoordinates()].removeTreasure();
  }

  @Override
  public Coordinates getStartCave() {
    return new CaveCoordinates(this.startCave.getCoordinates().getXCoordinates(),
            this.startCave.getCoordinates().getYCoordinates());
  }

  @Override
  public Coordinates getEndCave() {
    return new CaveCoordinates(this.endCave.getCoordinates().getXCoordinates(),
            this.endCave.getCoordinates().getYCoordinates());
  }

  @Override
  public List<Coordinates> getAdjacent(Coordinates coordinates) {
    if (coordinates == null) {
      throw new IllegalArgumentException("Coordinates cant be null");
    }
    return this.allCaves[coordinates.getXCoordinates()]
            [coordinates.getYCoordinates()].getAllAdjacent();
  }

  @Override
  public boolean hasArrow(Coordinates coordinates) {
    if (coordinates == null) {
      throw new IllegalArgumentException("Coordinates cant be null");
    }
    return this.allCaves[coordinates.getXCoordinates()][coordinates.getYCoordinates()].hasArrow();
  }

  @Override
  public boolean hasMonster(Coordinates coordinates) {
    if (coordinates == null) {
      throw new IllegalArgumentException("Coordinates cant be null");
    }
    return this.allCaves[coordinates.getXCoordinates()][coordinates.getYCoordinates()].hasMonster();
  }

  @Override
  public Arrow pickUpArrow(Coordinates coordinates) {
    if (coordinates == null) {
      throw new IllegalArgumentException("Coordinates cant be null");
    }
    return this.allCaves[coordinates.getXCoordinates()]
            [coordinates.getYCoordinates()].removeArrow();
  }

  @Override
  public MonsterSmell getSmell(Coordinates coordinates) throws IllegalArgumentException {

    if (coordinates == null) {
      throw new IllegalArgumentException("Coordinates cant be null");
    }

    int monsterCount = 0;
    if (this.allCaves[coordinates.getXCoordinates()]
            [coordinates.getYCoordinates()].isMonsterAlive()) {
      return MonsterSmell.STRONG;
    }

    Set<Coordinates> nextAdjacents = new HashSet<>();
    for (Coordinates adjacents :
            this.getAdjacent(coordinates)) {
      if (this.allCaves[adjacents.getXCoordinates()]
              [adjacents.getYCoordinates()].isMonsterAlive()) {
        return MonsterSmell.STRONG;
      }
      nextAdjacents.add(adjacents);
    }
    for (Coordinates coords :
            nextAdjacents) {
      for (Coordinates adjCoords : this.allCaves[coords.getXCoordinates()]
              [coords.getYCoordinates()].getAllAdjacent()) {
        if (this.allCaves[adjCoords.getXCoordinates()]
                [adjCoords.getYCoordinates()].isMonsterAlive()) {
          monsterCount += 1;
        }
      }
    }
    if (monsterCount > 1) {
      return MonsterSmell.STRONG;
    } else if (monsterCount == 1) {
      return MonsterSmell.WEAK;
    }
    return MonsterSmell.NONE;
  }

  @Override
  public void arrowStrike(Arrow arrow) {
    if (arrow == null) {
      throw new IllegalArgumentException("Arrow cannot be null");
    }
    int xArrow = arrow.getCurrentLocation().getXCoordinates();
    int yArrow = arrow.getCurrentLocation().getYCoordinates();

    this.allCaves[xArrow][yArrow].hitMonster(arrow);

  }

  @Override
  public int monsterArrowCount(Coordinates coordinates) {
    if (coordinates == null) {
      throw new IllegalArgumentException("Coordinates cant be null");
    }
    return this.allCaves[coordinates.getXCoordinates()]
            [coordinates.getYCoordinates()].monsterArrowCount();
  }

  @Override
  public Coordinates getNorth(Coordinates coordinates) throws IllegalArgumentException {
    if (coordinates == null) {
      throw new IllegalArgumentException("Coordinates cant be null");
    }
    return this.allCaves[coordinates.getXCoordinates()][coordinates.getYCoordinates()].getNorth();
  }

  @Override
  public Coordinates getSouth(Coordinates coordinates) throws IllegalArgumentException {
    if (coordinates == null) {
      throw new IllegalArgumentException("Coordinates cant be null");
    }
    return this.allCaves[coordinates.getXCoordinates()][coordinates.getYCoordinates()].getSouth();
  }

  @Override
  public Coordinates getEast(Coordinates coordinates) throws IllegalArgumentException {
    if (coordinates == null) {
      throw new IllegalArgumentException("Coordinates cant be null");
    }
    return this.allCaves[coordinates.getXCoordinates()][coordinates.getYCoordinates()].getEast();
  }

  @Override
  public Coordinates getWest(Coordinates coordinates) throws IllegalArgumentException {
    if (coordinates == null) {
      throw new IllegalArgumentException("Coordinates cant be null");
    }
    return this.allCaves[coordinates.getXCoordinates()][coordinates.getYCoordinates()].getWest();
  }

  @Override
  public void resetDungeon() {
    for (int row = 0; row < rows; row++) {
      for (int column = 0; column < columns; column++) {
        this.allCaves[row][column].resetCave();
      }
    }
    this.addTreasureToGivenPercentage(TreasureFactory.getAllPossibleTreasure());
    this.addArrowsToGivenPercentage();
    this.addMonsters();
  }

  @Override
  public String toString() {
    StringBuilder result = new StringBuilder();
    for (int row = 0; row < rows; row++) {
      for (int column = 0; column < columns; column++) {
        result.append("[").append(this.allCaves[row][column].toString()).append("]\n");
      }
    }
    return result.toString();
  }

}
