package controller;

import dungeonmodel.coordinates.Coordinates;

import dungeonmodel.player.Player;
import view.GameView;


/**
 * Implementation of the Controller for a Dungeon Navigation game.
 * Has the view and player as fields to handle actions on both.
 */
public class DungeonGameController implements GameController {

  private final Player player;
  private final GameView view;


  /**
   * Constructor of the Game Controller.
   * @param player player of the game
   * @param view view of the game
   * @throws IllegalArgumentException if either player/view is null.
   */
  public DungeonGameController(Player player, GameView view) throws IllegalArgumentException {
    if (player == null || view == null) {
      throw new IllegalArgumentException("Player and view can't be null");
    }
    this.player = player;
    this.view = view;

  }

  @Override
  public void playGame() throws IllegalArgumentException {
    this.view.startView(this);
  }

  @Override
  public void pickupArrow() {
    boolean result = this.player.pickUpArrow();

    if (result) {
      this.view.displayMessage("Arrow picked up successfully\n");
    }
    else {
      this.view.displayMessage("No arrow to pick up\n");
    }
    this.view.refresh();

  }

  @Override
  public void pickupTreasure() {
    boolean result = this.player.pickUpTreasure();

    if (result) {
      this.view.displayMessage("Treasure picked up successfully\n");
    }
    else {
      this.view.displayMessage("No treasure to pick up\n");
    }
    this.view.refresh();
  }

  @Override
  public void movePlayer(Coordinates direction) throws IllegalArgumentException {
    if (direction == null) {
      throw new IllegalArgumentException("Direction cannot be null!");
    }
    try {
      this.player.moveTo(direction);
      if (!this.player.isPlayerAlive()) {
        this.view.displayMessage("Player has been eaten by the Otyugh! Game Over!");
      }
    }
    catch (IllegalArgumentException iEx) {
      this.view.displayMessage(String.format("Move failed: %s \n", iEx.getMessage()));
    }
    this.view.refresh();
  }

  @Override
  public Coordinates shootArrow(Coordinates direction, int distance)
          throws IllegalArgumentException {
    if (direction == null) {
      throw new IllegalArgumentException("Direction cannot be null!");
    }
    try {
      Coordinates landed = this.player.shootArrow(distance, direction);
      this.view.displayMessage(String.format("Arrow has landed in %s", landed.toString()));
      this.view.refresh();
      return landed;
    }
    catch (IllegalArgumentException iEx) {
      this.view.displayMessage(String.format("Could not shoot: %s \n", iEx.getMessage()));
      this.view.refresh();
      return null;
    }
  }

  @Override
  public void resetPlayerAndDungeon() {
    this.player.resetPlayer();
    this.view.refresh();
  }
}
