package controller;

import dungeonmodel.coordinates.Coordinates;

/**
 * Represents a controller for the Dungeon Model.
 * Handles player's in-game choices and conveys result to the view in some shape and form.
 */
public interface GameController {

  /**
   * Symbolic method called to signify the controller getting signalled that a game is in progress.
   * Hands over control to the controller.
   */
  void playGame();

  /**
   * Method which acts as a pickup command for an arrow.
   */
  void pickupArrow();

  /**
   * Method which acts as a pickup command for treasure.
   */
  void pickupTreasure();

  /**
   * Moves a player in a certain direction.
   * @param direction direction to move player in
   */
  void movePlayer(Coordinates direction);

  /**
   * Shoot arrow for a certain distance in a certain direction.
   * @param direction direction arrow has to be shot in
   * @param distance distance arrow has to be shot for
   * @return coordinates where the arrow has landed, for the view to display in some shape/form
   */
  Coordinates shootArrow(Coordinates direction, int distance);

  /**
   * Reset the game by resetting player and dungeon.
   */
  void resetPlayerAndDungeon();
}
