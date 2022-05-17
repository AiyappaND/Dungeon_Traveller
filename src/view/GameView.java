package view;

import controller.GameController;

/**
 * Interface which represents a view for a dungeon game.
 * Contains methods to start a view, display some message on demand to the user and to refresh.
 */
public interface GameView {

  /**
   * Starts the view.
   * This makes the view ready to take some input after displaying the initial state of the game.
   *
   * @param controller controller which the view will interact with
   */
  void startView(GameController controller);

  /**
   * Display a message on demand to the user.
   *
   * @param message message to be displayed
   */
  void displayMessage(String message);

  /**
   * Refresh the view, showing an updated game state.
   */
  void refresh();

}
