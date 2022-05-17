package view;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;

import controller.GameController;
import dungeonmodel.coordinates.Coordinates;
import dungeonmodel.player.ReadOnlyPlayer;

/**
 * Console based view for the dungeon traversal game.
 * Reads user input from console, sends to the controller and updates itself when required.
 */
public class ConsoleView implements GameView {

  private final Appendable out;
  private final Scanner scan;
  private boolean hasQuit;
  private final ReadOnlyPlayer player;

  /**
   * Public constructor of the console view class.
   * Contains methods to render player and dungeon state to the screen.
   *
   * @param in readable from which input is received
   * @param out appendable to which output is sent
   * @param player player who's playing the game
   * @throws IllegalArgumentException if any of the given parameters are null
   */
  public ConsoleView(Readable in, Appendable out, ReadOnlyPlayer player)
          throws IllegalArgumentException {
    if (in == null || out == null) {
      throw new IllegalArgumentException("Readable and Appendable can't be null");
    }

    if (player == null) {
      throw new IllegalArgumentException("Player cannot be null");
    }

    this.out = out;
    this.scan = new Scanner(in);
    this.hasQuit = false;
    this.player = player;
  }

  @Override
  public void startView(GameController controller) throws IllegalArgumentException {
    if (controller == null) {
      throw new IllegalArgumentException("Controller cannot be null");
    }
    this.refresh();
    try {
      while (!this.hasQuit && player.isPlayerAlive() && !player.hasPlayerWon()) {
        String choice = this.getChoice();
        switch (choice) {
          case "a":
            controller.pickupArrow();
            break;
          case "t":
            controller.pickupTreasure();
            break;
          case "m":
            Coordinates moveDirection = this.getMoveDirection(player);
            controller.movePlayer(moveDirection);
            break;
          case "s":
            Coordinates direction = this.getArrowDirection(player);
            int distance = this.getArrowDistance();
            controller.shootArrow(direction, distance);
            break;
          default:
            this.renderToOut("Player has quit the game\n");
            break;
        }
      }
      if (player.hasPlayerWon()) {
        this.renderToOut("Player has won the game\n");
      }
      if (!player.isPlayerAlive()) {
        this.renderToOut("Player is dead\n");
      }
      this.renderPlayerState(player);
    } catch (IOException ioe) {
      throw new IllegalStateException("Append failed", ioe);
    } catch (NoSuchElementException nse) {
      // do nothing, input stream has ended
    }
  }

  @Override
  public void displayMessage(String message) {
    if (message == null) {
      throw new IllegalArgumentException("Message can't be null");
    }
    try {
      this.renderToOut(message);
    }
    catch (IOException ioe) {
      throw new IllegalStateException("Append failed", ioe);
    }
  }

  @Override
  public void refresh() {
    try {
      this.displayPlayerAndCaveState(player);
    }
    catch (IOException ioe) {
      throw new IllegalStateException("Append failed", ioe);
    }
  }

  // Helper method to get the direction input for moving player
  private Coordinates getMoveDirection(ReadOnlyPlayer player) throws IOException {
    this.renderToOut("Choose cave to move to from the following options\n");
    this.renderAllMoves(player);
    Coordinates moveDirection = null;
    boolean validChoice = false;
    while (!validChoice) {
      try {
        int moveOption = Integer.parseInt(this.scan.next());
        if (moveOption > 0 && moveOption <= player.getPossibleMoves().size()) {
          validChoice = true;
          moveDirection = player.getPossibleMoves().get(moveOption - 1);
        } else {
          this.renderToOut("Not a valid choice, try again. \n");
        }
      } catch (NumberFormatException nEx) {
        this.renderToOut("Not a valid integer, try again. \n");
      }
    }
    return moveDirection;
  }

  // Helper method to get direction to which arrow will be shot
  private Coordinates getArrowDirection(ReadOnlyPlayer player) throws IOException {
    this.renderToOut("Choose direction to shoot from the following options\n");
    this.renderAllMoves(player);
    boolean validDirectionOption = false;
    int shootChoice = 0;
    while (!validDirectionOption) {
      try {
        shootChoice = Integer.parseInt(this.scan.next());
        if (shootChoice > 0 && shootChoice <= player.getPossibleMoves().size()) {
          validDirectionOption = true;
        } else {
          this.renderToOut("Not a valid choice, try again. \n");
        }
      } catch (NumberFormatException nEx) {
        this.renderToOut("Not a valid integer, try again. \n");
      }
    }
    return player.getPossibleMoves().get(shootChoice - 1);
  }

  // Helper method to get distance to which arrow will be shot
  private int getArrowDistance() throws IOException {
    int distance = -1;
    boolean validDistance = false;
    this.renderToOut("Enter distance to shoot (1/2/3.. etc) \n");
    while (!validDistance) {
      try {
        distance = Integer.parseInt(this.scan.next());
        validDistance = true;
      } catch (NumberFormatException nEx) {
        this.renderToOut("Not a valid integer, try again. \n");
      }
    }
    return distance;
  }

  // Helper method to get choice for next operation/process
  private String getChoice() throws IOException {
    boolean validInput = false;
    String returnValue = null;

    this.renderToOut("Following choices are available.\n");
    this.renderToOut("a: Pickup arrow\n");
    this.renderToOut("t: Pickup treasure\n");
    this.renderToOut("m: Move to another location\n");
    this.renderToOut("s: Shoot arrow\n");
    this.renderToOut("q: Quit game\n");
    this.renderToOut("Enter your choice\n");


    while (!validInput) {
      returnValue = this.scan.next();
      if (returnValue.equalsIgnoreCase("q")) {
        this.hasQuit = true;
        validInput = true;
      } else if (!(returnValue.equalsIgnoreCase("a")
              || returnValue.equalsIgnoreCase("t")
              || returnValue.equalsIgnoreCase("m")
              || returnValue.equalsIgnoreCase("s"))) {
        this.renderToOut("Invalid input, please enter one of the available choices\n");
      } else {
        validInput = true;
      }
    }
    return returnValue;
  }

  // Helper method to render a cave's description
  private void renderCaveDirectionsAndItems(ReadOnlyPlayer player) throws IOException {
    this.renderToOut("Available paths: \n");
    this.renderAllMoves(player);
    this.renderToOut("\n");
    this.renderToOut("Treasure in this cave: \n");
    this.renderToOut(player.viewTreasureAtCurrentLocation().toString());
    this.renderToOut("\n");
    this.renderToOut("Arrow present at this location: ");
    this.renderToOut(String.valueOf(player.currentLocationHasArrow()));
    this.renderToOut("\n");
  }

  // Helper method to render/display a player's state
  private void renderPlayerState(ReadOnlyPlayer player) throws IOException {
    this.renderToOut("Player currently at: ");
    this.renderToOut(player.getCurrentCoordinates().toString());
    this.renderToOut("\n");
    this.renderToOut("Treasure held by player: \n");
    this.renderToOut(player.getCurrentTreasure().toString());
    this.renderToOut("\n");
    this.renderToOut("Number of arrows with player: ");
    this.renderToOut(String.valueOf(player.getArrowCount()));
    this.renderToOut("\n");
  }

  // Helper method to render smell based on nearby monsters
  private void renderSmell(ReadOnlyPlayer player) throws IOException {
    switch (player.getSmellInCurrentCave().toString()) {
      case "STRONG":
        this.renderToOut("There is a strong smell of nearby monsters\n");
        break;
      case "WEAK":
        this.renderToOut("There is a weak smell of nearby monsters\n");
        break;
      default:
        this.renderToOut("Cannot smell anything currently\n");
    }
  }

  // Helper method to append info to the external display
  private void renderToOut(String outString) throws IOException {
    this.out.append(outString);
  }

  // Helper method to render all nearby adjacent locations
  private void renderAllMoves(ReadOnlyPlayer player) throws IOException {
    for (int i = 1; i <= player.getPossibleMoves().size(); i++) {
      this.renderToOut(String.valueOf(i));
      this.renderToOut(": ");
      this.renderToOut(player.getPossibleMoves().get(i - 1).toString());
      this.renderToOut("\n");
    }
  }

  // Helper method to display a player state and cave description
  private void displayPlayerAndCaveState(ReadOnlyPlayer player) throws IOException {
    this.renderPlayerState(player);
    this.renderCaveDirectionsAndItems(player);
    this.renderSmell(player);
  }

}
