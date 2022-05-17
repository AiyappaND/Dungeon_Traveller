package view;

import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.JTextArea;

import dungeonmodel.player.ReadOnlyPlayer;
import dungeonmodel.treasure.Treasure;

// Package private class to display current player and game state to the user
class InfoPanel extends JPanel {
  private final ReadOnlyPlayer player;
  private final JTextArea infoText;

  // Constructor, takes player as input
  InfoPanel(ReadOnlyPlayer player) {
    this.player = player;
    this.setBackground(Color.WHITE);
    this.infoText = new JTextArea();
    infoText.setEditable(false);
    add(infoText);
    updateInfo();
  }

  // Package private method to update the information panel
  void updateInfo() {
    infoText.setText(textField());
  }

  // Display state in a text field
  private String textField() {
    StringBuilder result = new StringBuilder();
    String playerName = player.getName();
    int playerArrows = player.getArrowCount();
    StringBuilder currentTreasure = new StringBuilder();
    StringBuilder locationTreasure = new StringBuilder();
    for (Treasure treasure: player.getCurrentTreasure()) {
      currentTreasure.append(treasure.getTreasureType())
              .append(" : ").append(treasure.getTreasureQuality()).append(" ");
    }

    for (Treasure treasure: player.viewTreasureAtCurrentLocation()) {
      locationTreasure.append(treasure.getTreasureType())
              .append(" : ").append(treasure.getTreasureQuality()).append(" ");
    }

    String arrowFound;

    if (player.currentLocationHasArrow()) {
      arrowFound = "Current location has 1 Arrow";
    }
    else {
      arrowFound = "Current location has no arrows";
    }

    result.append(String.format("Player Name: %s", playerName));
    result.append("\n");
    result.append(String.format("Player Location: %s",
            player.getCurrentCoordinates().toString()));
    result.append("\n");
    result.append(String.format("Player Arrow Count: %s", playerArrows));
    result.append("\n");
    result.append(String.format("Can move to: %s", player.getPossibleMoves().toString()));
    result.append("\n");
    result.append(String.format("Player Treasure: %s", currentTreasure));
    result.append("\n");
    result.append(String.format("Treasure At Location: %s", locationTreasure));
    result.append("\n");
    result.append(arrowFound);
    result.append("\n");
    result.append(String.format("Smell of monsters nearby: %s",
            player.getSmellInCurrentCave().toString()));
    result.append("\n");
    return result.toString();
  }

}
