package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.swing.JFrame;
import javax.swing.JSplitPane;
import javax.swing.JScrollPane;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JMenuBar;
import javax.swing.BoxLayout;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;

import controller.DungeonGameController;
import controller.GameController;
import dungeonmodel.coordinates.CaveCoordinates;
import dungeonmodel.coordinates.Coordinates;
import dungeonmodel.dungeon.Dungeon;
import dungeonmodel.dungeon.DungeonImpl;
import dungeonmodel.dungeon.ReadOnlyDungeon;
import dungeonmodel.player.DungeonPlayer;
import dungeonmodel.player.Player;
import dungeonmodel.player.ReadOnlyPlayer;

/**
 * Implementation of the GameView as a Swing UI.
 * Contains methods to display state to the user, pass commands to the controller and listeners
 * for various actions such as mouse clicks and keyboard types.
 */
public class SwingView extends JFrame implements GameView, MouseListener,
        ActionListener, KeyListener {

  private boolean sPressed;
  private GameController controller;
  private ReadOnlyPlayer player;
  private ReadOnlyDungeon dungeon;
  private JSplitPane splitPane;
  private JScrollPane dungeonScrollPanel;
  private DungeonPanel dungeonPanel;
  private InfoPanel infoPanel;
  private boolean wonMessagePrinted;
  private static final int DIVIDER_LOCATION = 400;
  private static final int START_WIDTH = 400;
  private static final int START_HEIGHT = 100;
  private static final int FINAL_HEIGHT = 640;
  private static final int FINAL_WIDTH = 640;
  private static final int INFO_SCROLL_HEIGHT = 200;
  private static final int DUNGEON_SCROLL_HEIGHT = 400;

  /**
   * Public constructor of the view.
   * Starts an empty view, which gets populated when the player starts a game.
   */
  public SwingView() {
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    createStartGameMenu();
    this.setSize(START_WIDTH, START_HEIGHT);
    this.setResizable(true);
    this.setTitle("Dungeon Game");
    this.setFocusable(true);
    this.setBackground(Color.LIGHT_GRAY);
    this.sPressed = false;
  }

  // Creates the start game menu
  // https://docs.oracle.com/javase/tutorial/uiswing/components/menu.html
  private void createStartGameMenu() {
    JMenu options = new JMenu("Options");
    JMenu startGame = new JMenu("Start Game");
    JMenuItem quitGame = new JMenuItem("Exit");
    JMenuItem help = new JMenuItem("Help");
    JMenuItem cheat = new JMenuItem("Cheat");
    options.add(help);
    options.add(quitGame);
    options.add(cheat);
    JMenuItem newConfiguration = new JMenuItem("New Settings");
    startGame.add(newConfiguration);
    JMenuItem sameConfiguration = new JMenuItem("Same Settings");
    startGame.add(sameConfiguration);
    JMenuBar menuBar = new JMenuBar();
    this.setJMenuBar(menuBar);
    menuBar.add(options);
    menuBar.add(startGame);

    quitGame.addActionListener(this);
    help.addActionListener(this);
    cheat.addActionListener(this);
    newConfiguration.addActionListener(this);
    sameConfiguration.addActionListener(this);
  }

  // Helper method to initialize model and controller based on user input and passes
  // control to the controller
  private void initializeAndStartGame(Player player, Dungeon dungeon) {
    this.player = player;
    this.dungeon = dungeon;
    GameController controller = new DungeonGameController(player, this);
    controller.playGame();
  }


  @Override
  public void startView(GameController controller) throws IllegalArgumentException {
    if (player == null || dungeon == null || controller == null) {
      throw new IllegalArgumentException("Game has not been initialized yet");
    }
    getContentPane().removeAll();
    this.controller = controller;
    this.dungeonPanel = new DungeonPanel(player, dungeon);
    this.infoPanel = new InfoPanel(player);
    this.splitPane = new JSplitPane();
    JScrollPane infoScrollPane = new JScrollPane(infoPanel);
    infoScrollPane.setPreferredSize(new Dimension(FINAL_WIDTH, INFO_SCROLL_HEIGHT));
    this.dungeonScrollPanel = new JScrollPane(dungeonPanel);

    dungeonScrollPanel.setPreferredSize(new Dimension(FINAL_WIDTH, DUNGEON_SCROLL_HEIGHT));
    setPreferredSize(new Dimension(FINAL_WIDTH, FINAL_HEIGHT));
    getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
    getContentPane().add(splitPane);

    splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
    splitPane.setDividerLocation(DIVIDER_LOCATION);
    splitPane.setTopComponent(dungeonScrollPanel);
    splitPane.setBottomComponent(infoScrollPane);
    dungeonScrollPanel.addMouseListener(this);
    this.addKeyListener(this);
    pack();
  }

  @Override
  public void displayMessage(String message) {
    if (message == null) {
      throw new IllegalArgumentException("Message cannot be null");
    }
    JOptionPane.showMessageDialog(null, message, "Info",
            JOptionPane.INFORMATION_MESSAGE);
  }

  @Override
  public void refresh() {
    this.infoPanel.updateInfo();
    this.dungeonPanel.updatePlayerLocation();
    this.dungeonPanel.repaint();
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    double xCoordinates = (double) e.getY() / DungeonPanel.CAVE_PIXELS;
    double yCoordinates = (double) e.getX() / DungeonPanel.CAVE_PIXELS;
    Coordinates moveTo = new CaveCoordinates((int)Math.floor(xCoordinates),
            (int)Math.floor(yCoordinates));
    controller.movePlayer(moveTo);
    this.refresh();
    if (this.player.hasPlayerWon() && !this.wonMessagePrinted) {
      this.printWonMessage();
    }
  }

  // Prints a message to let the user know if they have won the game
  private void printWonMessage() {
    this.displayInfoPane("Congratulations, you have reached the end and won the game! "
            + "You can continue exploring if you wish.", "Congratulations!");
    this.wonMessagePrinted = true;
  }

  @Override
  public void mousePressed(MouseEvent e) {
    // do nothing
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    // do nothing
  }

  @Override
  public void mouseEntered(MouseEvent e) {
    // do nothing
  }

  @Override
  public void mouseExited(MouseEvent e) {
    // do nothing
  }

  // Helper method to create a information panel
  private void displayInfoPane(String content, String title) {
    JOptionPane.showMessageDialog(null, content, title,
            JOptionPane.INFORMATION_MESSAGE);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    String text = ((JMenuItem) e.getSource()).getText();
    switch (text) {
      case "New Settings":
        boolean validSettings = false;
        while (!validSettings) {
          Map<String, String> settings = getGameSettings();
          if (settings.get("cancelled").equals("True")) {
            validSettings = true;
          }
          else {
            validSettings = validateGameSettings(settings);
          }
        }
        break;
      case "Same Settings":
        if (player == null || dungeon == null) {
          String infoMessage = "You need to have started a game using Options "
                  + "-> Start Game -> New Settings in order to reuse them!";
          displayInfoPane(infoMessage, "Error");
        }
        else {
          controller.resetPlayerAndDungeon();
          dungeonPanel = new DungeonPanel(player, dungeon);
          dungeonScrollPanel = new JScrollPane(dungeonPanel);
          splitPane.setDividerLocation(DIVIDER_LOCATION);
          splitPane.setTopComponent(dungeonScrollPanel);
          this.wonMessagePrinted = false;
          dungeonPanel.addMouseListener(this);
          pack();
          displayInfoPane("Game has been reset!", "Info");
        }
        break;
      case "Help":
        String infoMessage = "To Start New Game: Options -> Start Game -> New Settings\n"
                + "To Restart: Options -> Start Game -> Same Settings\n"
                + "To Move: Press Arrow Keys\n"
                + "To Shoot: Press S and Arrow Key of Direction together\n"
                + "To Pickup Treasure: Press T\nTo Pickup Arrow: Press A";
        displayInfoPane(infoMessage, "Help");
        break;
      case "Exit":
        System.exit(0);
        break;
      case "Cheat":
        if (player == null || dungeon == null) {
          String message = "You need to have started a game using Options "
                  + "-> Start Game -> New Settings in order to use cheat!";
          displayInfoPane(message, "Error");
        }
        else {
          String message = "All caves will be visible now! Enjoy!";
          displayInfoPane(message, "Info");
          this.dungeonPanel.makeAllCavesVisible();
          this.refresh();
        }
        break;
      default:
        // do nothing
        break;
    }
  }

  // Popup to get game configuration for a new game
  // https://stackoverflow.com/questions/3002787/simple-popup-java-form-with-at-least-two-fields
  private Map<String, String> getGameSettings() {
    Map<String, String> result = new HashMap<>();

    String[] items = {"No", "Yes"};
    JComboBox<String> combo = new JComboBox<>(items);
    JTextField rows = new JTextField("5");
    JTextField columns = new JTextField("5");
    JTextField playerName = new JTextField("Name");
    JTextField treasureArrowPercent = new JTextField("50");
    JTextField numOfMonsters = new JTextField("1");
    JTextField interconnectivity = new JTextField("0");
    JPanel panel = new JPanel(new GridLayout(0, 1));
    panel.add(new JLabel("Player Name"));
    panel.add(playerName);
    panel.add(new JLabel("Wrapping Dungeon?"));
    panel.add(combo);
    panel.add(new JLabel("Number of Rows"));
    panel.add(rows);
    panel.add(new JLabel("Number of Columns"));
    panel.add(columns);
    panel.add(new JLabel("Interconnectivity"));
    panel.add(interconnectivity);
    panel.add(new JLabel("Percentage of Treasure/Arrows"));
    panel.add(treasureArrowPercent);
    panel.add(new JLabel("Number of Otyughs"));
    panel.add(numOfMonsters);
    int buttonPress = JOptionPane.showConfirmDialog(null, panel, "Settings",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
    if (buttonPress == JOptionPane.OK_OPTION) {
      result.put("name", playerName.getText());
      result.put("wrapping", Objects.requireNonNull(combo.getSelectedItem()).toString());
      result.put("rows", rows.getText());
      result.put("columns", columns.getText());
      result.put("treasureArrowPercent", treasureArrowPercent.getText());
      result.put("numMonsters", numOfMonsters.getText());
      result.put("cancelled", "False");
      result.put("interconnectivity", interconnectivity.getText());
    } else {
      result.put("cancelled", "True");
    }

    return result;
  }

  // Validates user input for type (integer/string etc)
  private boolean validateGameSettings(Map<String, String> settings) {
    int rows;
    int columns;
    int numOfMonsters;
    int treasureArrowPercent;
    int interconnectivity;
    boolean isWrapping;

    try {
      rows = Integer.parseInt(settings.get("rows"));
      columns = Integer.parseInt(settings.get("columns"));
    }
    catch (NumberFormatException nEx) {
      displayInfoPane("Rows and columns need to be valid integers", "Error");
      return false;
    }

    try {
      numOfMonsters = Integer.parseInt(settings.get("numMonsters"));
    }
    catch (NumberFormatException nEx) {
      displayInfoPane("Number of monsters needs to be a valid integer", "Error");
      return false;
    }

    try {
      treasureArrowPercent = Integer.parseInt(settings.get("treasureArrowPercent"));
    }
    catch (NumberFormatException nEx) {
      displayInfoPane("Treasure and Arrow percentage needs to be a valid integer",
              "Error");
      return false;
    }

    try {
      interconnectivity = Integer.parseInt(settings.get("interconnectivity"));
    }
    catch (NumberFormatException nEx) {
      displayInfoPane("Interconnectivity needs to be a valid integer",
              "Error");
      return false;
    }

    isWrapping = settings.get("wrapping").equals("Yes");

    try {
      Dungeon testDungeon = new DungeonImpl(rows, columns, interconnectivity, isWrapping,
              treasureArrowPercent, numOfMonsters);
      Player testPlayer = new DungeonPlayer(settings.get("name"), testDungeon);
      initializeAndStartGame(testPlayer, testDungeon);
      return true;
    }
    catch (IllegalArgumentException iAx) {
      displayInfoPane(iAx.getMessage(), "Error");
      return false;
    }
  }

  // Helper method to get shoot distance
  private String getShootDistance() {
    JLabel label = new JLabel("Enter distance");
    JTextField distance = new JTextField("");
    JPanel panel = new JPanel(new GridLayout(0, 1));
    panel.add(label);
    panel.add(distance);
    int buttonPress = JOptionPane.showConfirmDialog(null, panel, "Distance",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
    if (buttonPress == JOptionPane.OK_OPTION) {
      return distance.getText();
    }
    return null;
  }

  @Override
  public void keyTyped(KeyEvent e) {
    if (player != null) {
      if (e.getKeyChar() == 's') {
        this.sPressed = true;
      }
      if (e.getKeyChar() == 't') {
        try {
          this.controller.pickupTreasure();
        }
        catch (IllegalArgumentException iEx) {
          this.displayInfoPane(iEx.getMessage(), "Error");
        }
      }
      if (e.getKeyChar() == 'a') {
        try {
          this.controller.pickupArrow();
        }
        catch (IllegalArgumentException iEx) {
          this.displayInfoPane(iEx.getMessage(), "Error");
        }
      }
    }
  }

  @Override
  public void keyPressed(KeyEvent e) {
    // do nothing
  }

  @Override
  public void keyReleased(KeyEvent e) {
    if (player != null) {
      if (e.getKeyChar() == 's') {
        this.sPressed = false;
      }
      // Arrow Key Left - Move West
      if (e.getKeyCode() == 37) {
        if (!this.sPressed) {
          if ((this.player.getWest() != null)) {
            this.controller.movePlayer(player.getWest());
          }
        }
        else {
          this.handleShoot(this.player.getWest());
        }
      }
      // Arrow Key Up - Move North
      if (e.getKeyCode() == 38) {
        if (!this.sPressed) {
          if ((this.player.getNorth() != null)) {
            this.controller.movePlayer(player.getNorth());
          }
        }
        else {
          this.handleShoot(this.player.getNorth());
        }
      }
      // Arrow Key Right - Move East
      if (e.getKeyCode() == 39) {
        if (!this.sPressed) {
          if ((this.player.getEast() != null)) {
            this.controller.movePlayer(player.getEast());
          }
        }
        else {
          this.handleShoot(this.player.getEast());
        }
      }
      // Arrow Key Down - Move South
      if (e.getKeyCode() == 40) {
        if (!this.sPressed) {
          if ((this.player.getSouth() != null)) {
            this.controller.movePlayer(player.getSouth());
          }
        }
        else {
          this.handleShoot(this.player.getSouth());
        }
      }
      if (this.player.hasPlayerWon() && !this.wonMessagePrinted) {
        this.printWonMessage();
      }
    }
  }

  // Handles the shoot input
  private void handleShoot(Coordinates direction) {
    this.sPressed = false;
    if ((direction != null)) {
      boolean valid = false;
      while (!valid) {
        String distance = this.getShootDistance();
        if (distance != null) {
          try {
            int distanceInt = Integer.parseInt(distance);
            Coordinates landed = this.controller.shootArrow(direction, distanceInt);
            valid = true;
            if (landed != null) {
              this.dungeonPanel.addArrowLandedLocation(landed);
              this.refresh();
            }
          }
          catch (NumberFormatException nEx) {
            this.displayInfoPane("Distance must be a valid Integer!", "Error");
          }
        }
        else {
          valid = true;
        }
      }
    }
    else {
      this.displayInfoPane("Can't shoot in that direction!", "Info");
    }
  }
}
