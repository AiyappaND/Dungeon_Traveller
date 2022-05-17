package dungeonmodel.weapon;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import dungeonmodel.coordinates.CaveCoordinates;
import dungeonmodel.coordinates.Coordinates;
import dungeonmodel.dungeon.Dungeon;

/**
 * Class to represent a crooked arrow.
 * This can be fired by a player travelling through a dungeon, and curves through tunnels.
 */
public class CrookedArrow implements Arrow {

  private Coordinates currentCoordinates;

  public CrookedArrow() {
    this.currentCoordinates = null;
  }

  @Override
  public void shoot(int distance, Coordinates direction,
                    Coordinates source, Dungeon dungeon)
          throws IllegalArgumentException {
    if (distance <= 0) {
      throw new IllegalArgumentException("Distance cannot be negative");
    }

    if (direction == null || source == null) {
      throw new IllegalArgumentException("Direction and source cannot be null");
    }

    if (dungeon == null) {
      throw new IllegalArgumentException("Dungeon cannot be null");
    }

    if (!dungeon.getAdjacent(source).contains(direction)) {
      throw new IllegalArgumentException("Cannot shoot in the given direction, no path available");
    }

    this.currentCoordinates = source;
    Coordinates next = direction;
    List<Integer> offset = this.findOffset(this.currentCoordinates, next, dungeon);

    int movedCount = 0;

    for (int i = 0; i < distance; i++) {
      if (dungeon.getAdjacent(this.currentCoordinates).contains(next)) {
        if (dungeon.getAdjacent(next).size() == 2) {
          List<Coordinates> adjacent = dungeon.getAdjacent(next);
          adjacent.remove(this.currentCoordinates);
          Coordinates temp = adjacent.get(0);
          offset = this.findOffset(next, temp, dungeon);
          this.currentCoordinates = next;
          next = temp;
        } else {
          this.currentCoordinates = next;
          int newX = next.getXCoordinates() + offset.get(0);
          int newY = next.getYCoordinates() + offset.get(1);

          if (newX < 0) {
            newX += dungeon.getDimensions().get(0);
          } else if (newX >= dungeon.getDimensions().get(0)) {
            newX -= dungeon.getDimensions().get(0);
          }

          if (newY < 0) {
            newY += dungeon.getDimensions().get(1);
          } else if (newY >= dungeon.getDimensions().get(1)) {
            newY -= dungeon.getDimensions().get(1);
          }

          next = new CaveCoordinates(newX, newY);
        }
        movedCount += 1;
      } else {
        break;
      }
    }

    if (movedCount == distance) {
      dungeon.arrowStrike(this);
    }

  }

  @Override
  public Coordinates getCurrentLocation() {
    return this.currentCoordinates;
  }

  // Helper method to find an offset/direction between some source and target
  private List<Integer> findOffset(Coordinates source, Coordinates target, Dungeon dungeon) {
    int targetX = target.getXCoordinates() - source.getXCoordinates();
    int targetY = target.getYCoordinates() - source.getYCoordinates();

    if (targetX < -1) {
      targetX += dungeon.getDimensions().get(0);
    }
    if (targetX > 1) {
      targetX -= dungeon.getDimensions().get(0);
    }

    if (targetY < -1) {
      targetY += dungeon.getDimensions().get(1);
    }
    if (targetY > 1) {
      targetY -= dungeon.getDimensions().get(1);
    }

    List<Integer> returnValue = new ArrayList<>();
    returnValue.add(targetX);
    returnValue.add(targetY);

    return returnValue;

  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CrookedArrow that = (CrookedArrow) o;
    return Objects.equals(currentCoordinates, that.currentCoordinates);
  }

  @Override
  public int hashCode() {
    return Objects.hash(currentCoordinates);
  }
}
