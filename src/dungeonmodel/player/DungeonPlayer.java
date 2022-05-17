package dungeonmodel.player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import dungeonmodel.coordinates.Coordinates;
import dungeonmodel.dungeon.Dungeon;
import dungeonmodel.dungeon.MonsterSmell;
import dungeonmodel.treasure.Treasure;
import dungeonmodel.weapon.Arrow;
import dungeonmodel.weapon.CrookedArrow;

/**
 * Class representing a player who can traverse a dungeon and pick up treasure on the way.
 */
public class DungeonPlayer implements Player {

  private static final int STARTING_ARROWS = 3;

  private final String name;
  private List<Treasure> currentTreasure;
  private List<Arrow> crookedArrows;
  private final Dungeon currentDungeon;
  private Coordinates currentCoordinates;
  private boolean isAlive;
  private boolean hasWon;

  /**
   * Creates a player with the provided name.
   *
   * @param name name of th player
   * @throws IllegalArgumentException if provided name is null or empty
   */
  public DungeonPlayer(String name, Dungeon dungeon) throws IllegalArgumentException {
    if (name == null || name.trim().equals("")) {
      throw new IllegalArgumentException("Player name cannot be blank or null");
    }
    this.name = name;
    this.currentCoordinates = null;
    this.currentTreasure = new ArrayList<>();
    this.crookedArrows = new ArrayList<>();

    for (int count = 0; count < STARTING_ARROWS; count++) {
      this.crookedArrows.add(new CrookedArrow());
    }
    this.isAlive = true;
    if (dungeon == null) {
      throw new IllegalArgumentException("Dungeon cannot be null");
    }
    this.currentDungeon = dungeon;
    this.currentCoordinates = dungeon.getStartCave();
  }

  @Override
  public String getName() {
    return this.name;
  }


  @Override
  public Coordinates getCurrentCoordinates() throws IllegalArgumentException {
    return this.currentCoordinates;
  }

  @Override
  public boolean moveTo(Coordinates coordinates) throws IllegalArgumentException {
    if (coordinates == null) {
      throw new IllegalArgumentException("Coordinates can't be null!");
    }
    this.throwExceptionIfPlayerDead();
    if (this.currentDungeon.getAdjacent(this.currentCoordinates).contains(coordinates)) {
      this.currentCoordinates = coordinates;
      if (currentDungeon.hasMonster(this.currentCoordinates)) {
        if (currentDungeon.monsterArrowCount(this.currentCoordinates) == 0) {
          this.isAlive = false;
        } else {
          if (new Random().nextInt(2) == 1) {
            this.isAlive = false;
          }
        }
      }

      if (this.isAlive && this.currentCoordinates.equals(this.currentDungeon.getEndCave())) {
        this.hasWon = true;
      }

      return true;
    }
    return false;
  }

  @Override
  public List<Coordinates> getPossibleMoves() throws IllegalArgumentException {
    return this.currentDungeon.getAdjacent(this.currentCoordinates);
  }

  @Override
  public List<Treasure> getCurrentTreasure() {
    return new ArrayList<>(this.currentTreasure);
  }

  @Override
  public boolean pickUpTreasure() throws IllegalArgumentException {
    this.throwExceptionIfPlayerDead();
    if (!this.currentDungeon.viewTreasureInCave(this.currentCoordinates).isEmpty()) {
      this.currentTreasure.addAll(this.currentDungeon.pickupTreasureInCave(
              this.currentCoordinates));
      return true;
    }
    return false;
  }

  @Override
  public List<Treasure> viewTreasureAtCurrentLocation() throws IllegalArgumentException {
    return this.currentDungeon.viewTreasureInCave(this.currentCoordinates);
  }

  @Override
  public boolean currentLocationHasArrow() throws IllegalArgumentException {
    return this.currentDungeon.hasArrow(this.currentCoordinates);
  }

  @Override
  public boolean pickUpArrow() throws IllegalArgumentException {
    this.throwExceptionIfPlayerDead();
    Arrow arrow = this.currentDungeon.pickUpArrow(this.currentCoordinates);
    if (arrow != null) {
      this.crookedArrows.add(arrow);
      return true;
    }
    return false;
  }

  @Override
  public int getArrowCount() {
    return this.crookedArrows.size();
  }

  @Override
  public Coordinates shootArrow(int distance, Coordinates direction)
          throws IllegalArgumentException {
    if (direction == null) {
      throw new IllegalArgumentException("Direction can't be null!");
    }
    this.throwExceptionIfPlayerDead();
    if (this.crookedArrows.size() == 0) {
      throw new IllegalArgumentException("Player has no arrows to shoot");
    }
    Arrow someArrow = this.crookedArrows.get(0);
    someArrow.shoot(distance, direction, this.currentCoordinates, this.currentDungeon);
    this.crookedArrows.remove(0);
    return someArrow.getCurrentLocation();
  }

  @Override
  public void resetPlayer() {
    this.currentTreasure = new ArrayList<>();
    this.crookedArrows = new ArrayList<>();

    for (int count = 0; count < STARTING_ARROWS; count++) {
      this.crookedArrows.add(new CrookedArrow());
    }
    this.isAlive = true;
    this.hasWon = false;
    this.currentCoordinates = this.currentDungeon.getStartCave();
    this.currentDungeon.resetDungeon();
  }

  @Override
  public boolean isPlayerAlive() {
    return this.isAlive;
  }

  @Override
  public MonsterSmell getSmellInCurrentCave() {
    return this.currentDungeon.getSmell(this.currentCoordinates);
  }

  @Override
  public boolean hasPlayerWon() {
    return this.hasWon;
  }

  @Override
  public Coordinates getNorth() {
    return this.currentDungeon.getNorth(this.currentCoordinates);
  }

  @Override
  public Coordinates getSouth() {
    return this.currentDungeon.getSouth(this.currentCoordinates);
  }

  @Override
  public Coordinates getEast() {
    return this.currentDungeon.getEast(this.currentCoordinates);
  }

  @Override
  public Coordinates getWest() {
    return this.currentDungeon.getWest(this.currentCoordinates);
  }

  // Private helper method to throw an exception if player is dead
  private void throwExceptionIfPlayerDead() throws IllegalArgumentException {
    if (!this.isAlive) {
      throw new IllegalArgumentException("Player is dead, cannot perform current operation");
    }
  }

}
