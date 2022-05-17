import java.io.InputStreamReader;
import java.util.Scanner;

import controller.DungeonGameController;
import dungeonmodel.dungeon.Dungeon;
import dungeonmodel.dungeon.DungeonImpl;
import dungeonmodel.player.DungeonPlayer;
import dungeonmodel.player.Player;
import view.ConsoleView;
import view.GameView;
import view.SwingView;

/**
 * Driver class to show functionality of dungeon model.
 */
public class DriverMain {
  /**
   * Main function which runs the sample run.
   *
   * @param args optional arguments
   */
  public static void main(String[] args) {
    if (args[0].equals("--gui")) {
      SwingView gameGUI = new SwingView();
      gameGUI.setVisible(true);
    }
    else if (args[0].equals("--text")) {
      Readable inputStream = new InputStreamReader(System.in);
      Appendable output = System.out;
      boolean invalid = true;
      Scanner scanner = new Scanner(System.in);
      System.out.println("Enter Player Name");
      String name = scanner.next();
      int rows = 0;
      int columns = 0;
      int interconnectivity = 0;
      boolean wrapping = false;
      int percentage = 0;
      int monsters = 0;
      while (invalid) {
        try {
          System.out.println("Enter number of rows");
          rows = Integer.parseInt(scanner.next());
          System.out.println("Enter number of columns");
          columns = Integer.parseInt(scanner.next());
          System.out.println("Enter degree of interconnectivity");
          interconnectivity = Integer.parseInt(scanner.next());
          System.out.println("Enter yes if you want dungeon to be wrapping, "
                  + "any other input if not");
          wrapping = scanner.next().equalsIgnoreCase("yes");
          System.out.println("Enter percentage of treasure and arrows");
          percentage = Integer.parseInt(scanner.next());
          System.out.println("Enter number of monsters");
          monsters = Integer.parseInt(scanner.next());
          invalid = false;
        }
        catch (NumberFormatException nEx) {
          System.out.println("Invalid integer input, try again");
        }
      }
      Dungeon dungeon = new DungeonImpl(rows, columns, interconnectivity,
              wrapping, percentage, monsters);
      Player player = new DungeonPlayer(name, dungeon);
      GameView view = new ConsoleView(inputStream, output, player);
      new DungeonGameController(player, view).playGame();
    }
    else {
      System.out.println("Invalid argument given to jar, should be either --gui or --text.");
      System.exit(0);
    }
  }
}
