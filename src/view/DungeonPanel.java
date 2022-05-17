package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import dungeonmodel.coordinates.CaveCoordinates;
import dungeonmodel.coordinates.Coordinates;
import dungeonmodel.dungeon.MonsterSmell;
import dungeonmodel.dungeon.ReadOnlyDungeon;
import dungeonmodel.player.ReadOnlyPlayer;
import dungeonmodel.treasure.GemTypes;
import dungeonmodel.treasure.Treasure;

// Panel where all the dungeon caves are rendered to the screen
// Package private as it is used internally by the swing view.
class DungeonPanel extends JPanel {
  private final ReadOnlyPlayer player;
  private final ReadOnlyDungeon dungeon;
  private final Set<Coordinates> discoveredCells;
  private final List<Coordinates> arrowLandedCells;
  static final int CAVE_PIXELS = 64;

  // Package private constructor of the class
  DungeonPanel(ReadOnlyPlayer player, ReadOnlyDungeon dungeon) {
    this.player = player;
    this.dungeon = dungeon;
    this.setBackground(Color.LIGHT_GRAY);
    this.setPreferredSize(new Dimension(CAVE_PIXELS * dungeon.getDimensions().get(1),
            CAVE_PIXELS * dungeon.getDimensions().get(0)));
    this.discoveredCells = new HashSet<>();
    this.discoveredCells.add(player.getCurrentCoordinates());
    this.arrowLandedCells = new ArrayList<>();
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    this.paintCaves(g);
    this.paintSmells(g);
    this.paintArrowLanded(g);
    this.paintPlayer(g);
    this.paintArrowInCave(g);
    this.paintTreasure(g);
    this.paintMonster(g);
  }

  // Method to draw smells in a cave
  private void paintSmells(Graphics g) {
    Graphics2D g2d = (Graphics2D) g;
    for (Coordinates coordinate:
         this.discoveredCells) {
      if (dungeon.getSmell(coordinate).equals(MonsterSmell.STRONG)) {
        g2d.drawImage(this.getStrongSmellImage(),
                coordinate.getYCoordinates() * CAVE_PIXELS,
                coordinate.getXCoordinates() * CAVE_PIXELS, this);
      }
      else if (dungeon.getSmell(coordinate).equals(MonsterSmell.WEAK)) {
        g2d.drawImage(this.getWeakSmellImage(),
                coordinate.getYCoordinates() * CAVE_PIXELS,
                coordinate.getXCoordinates() * CAVE_PIXELS, this);
      }
    }
  }

  // Method to draw cave
  private void paintCaves(Graphics g) {
    int totalRows = this.dungeon.getDimensions().get(0);
    int totalColumns = this.dungeon.getDimensions().get(1);
    for (int row = 0; row < totalRows; row ++) {
      for (int col = 0; col < totalColumns; col ++) {
        if (this.discoveredCells.contains(new CaveCoordinates(row, col))) {
          this.paintDiscoveredCave(new CaveCoordinates(row, col), g);
        }
        else {
          this.paintUndiscoveredCell(new CaveCoordinates(row, col), g);
        }
      }
    }
  }


  // Cheat method to make all caves visible
  void makeAllCavesVisible() {
    int totalRows = this.dungeon.getDimensions().get(0);
    int totalColumns = this.dungeon.getDimensions().get(1);
    for (int row = 0; row < totalRows; row ++) {
      for (int col = 0; col < totalColumns; col ++) {
        this.discoveredCells.add(new CaveCoordinates(row, col));
      }
    }
  }

  // Method to paint caves already visited by player
  private void paintDiscoveredCave(Coordinates coordinates, Graphics g) {
    Graphics2D g2d = (Graphics2D) g;

    g2d.drawImage(this.getCaveImage(this.getDirectionsNames(coordinates)),
            coordinates.getYCoordinates() * CAVE_PIXELS,
            coordinates.getXCoordinates() * CAVE_PIXELS, this);

  }

  // Method to paint caves not visited by player
  private void paintUndiscoveredCell(Coordinates coordinates, Graphics g) {
    Graphics2D g2d = (Graphics2D) g;
    g2d.drawImage(getBlankImage(),
            coordinates.getYCoordinates() * CAVE_PIXELS,
            coordinates.getXCoordinates() * CAVE_PIXELS, this);
  }

  // Method to build name of the image for a certain cave and its directions
  private String getDirectionsNames(Coordinates coordinates) {
    StringBuilder res = new StringBuilder();

    if (dungeon.getEast(coordinates) != null) {
      res.append("E");
    }
    if (dungeon.getNorth(coordinates) != null) {
      res.append("N");
    }
    if (dungeon.getSouth(coordinates) != null) {
      res.append("S");
    }
    if (dungeon.getWest(coordinates) != null) {
      res.append("W");
    }

    return res.toString();
  }

  // Package private method to update the player's current location
  void updatePlayerLocation() {
    this.discoveredCells.add(this.player.getCurrentCoordinates());
    this.arrowLandedCells.removeIf(element ->
            (element.equals(this.player.getCurrentCoordinates())));
  }


  // Method to paint where the arrow has landed after firing
  private void paintArrowLanded(Graphics g) {
    Set<Coordinates> landed = new HashSet<>(this.arrowLandedCells);
    Graphics2D g2d = (Graphics2D) g;

    for (Coordinates coordinate:
         landed) {
      int occurrence = Collections.frequency(this.arrowLandedCells, coordinate);

      if (occurrence >= 2) {
        g2d.drawImage(getMultipleLandedImage(),
                coordinate.getYCoordinates() * CAVE_PIXELS,
                coordinate.getXCoordinates() * CAVE_PIXELS, this);
      }
      else {
        g2d.drawImage(getSingleLandedImage(),
                coordinate.getYCoordinates() * CAVE_PIXELS,
                coordinate.getXCoordinates() * CAVE_PIXELS, this);
      }

    }

  }

  // Package private method to add a location to the list of arrows landed
  void addArrowLandedLocation(Coordinates coordinates) {
    this.arrowLandedCells.add(coordinates);
  }

  // Method to paint a player's icon in a cave
  private void paintPlayer(Graphics g) {
    Graphics2D g2d = (Graphics2D) g;
    g2d.drawImage(this.getPlayerImage(),
            (player.getCurrentCoordinates().getYCoordinates() * CAVE_PIXELS) + 20,
            (player.getCurrentCoordinates().getXCoordinates() * CAVE_PIXELS) + 20, this);
  }

  // Method to paint the arrow in a cave
  private void  paintArrowInCave(Graphics g) {
    Graphics2D g2d = (Graphics2D) g;
    for (Coordinates coordinate:
         this.discoveredCells) {
      if (dungeon.hasArrow(coordinate)) {
        g2d.drawImage(this.getArrowImage(),
                (coordinate.getYCoordinates() * CAVE_PIXELS) + 25,
                (coordinate.getXCoordinates() * CAVE_PIXELS) + 30, this);
      }
    }
  }

  // Method to paint the treasure in a cave
  private void paintTreasure(Graphics g) {
    Graphics2D g2d = (Graphics2D) g;
    for (Coordinates coordinates : this.discoveredCells) {
      if (!dungeon.viewTreasureInCave(coordinates).isEmpty()) {
        boolean sapphirePresent = false;
        boolean rubyPresent = false;
        boolean diamondPresent = false;
        for (Treasure treasure:
             this.dungeon.viewTreasureInCave(coordinates)) {
          if (treasure.getTreasureType().equals(GemTypes.RUBY)) {
            rubyPresent = true;
          }
          if (treasure.getTreasureType().equals(GemTypes.SAPPHIRE)) {
            sapphirePresent = true;
          }
          if (treasure.getTreasureType().equals(GemTypes.DIAMOND)) {
            diamondPresent = true;
          }

          if (rubyPresent) {
            g2d.drawImage(this.getRubyImage(),
                    (coordinates.getYCoordinates() * CAVE_PIXELS) + 20,
                    (coordinates.getXCoordinates() * CAVE_PIXELS) + 40, this);
          }
          if (diamondPresent) {
            g2d.drawImage(this.getDiamondImage(),
                    (coordinates.getYCoordinates() * CAVE_PIXELS) + 30,
                    (coordinates.getXCoordinates() * CAVE_PIXELS) + 40, this);
          }
          if (sapphirePresent) {
            g2d.drawImage(this.getSapphireImage(),
                    (coordinates.getYCoordinates() * CAVE_PIXELS) + 40,
                    (coordinates.getXCoordinates() * CAVE_PIXELS) + 40, this);
          }

        }
      }
    }
  }

  // Method to paint the monster in a cave
  private void paintMonster(Graphics g) {
    Graphics2D g2d = (Graphics2D) g;
    for (Coordinates coordinates : this.discoveredCells) {
      if (this.dungeon.hasMonster(coordinates)) {
        g2d.drawImage(this.getOtyughImage(),
                coordinates.getYCoordinates() * CAVE_PIXELS + 15,
                coordinates.getXCoordinates() * CAVE_PIXELS + 20, this);
      }
    }
  }

  // Method to import images from the resources folder
  private Image importImage(String imageLocation) {
    try {
      return ImageIO.read(Objects.requireNonNull(
              this.getClass().getResource(imageLocation)));
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  // Method to get sapphire icon
  private Image getSapphireImage() {
    return Objects.requireNonNull(this.importImage("/sapphire.png"))
                    .getScaledInstance(10, 10, Image.SCALE_FAST);
  }

  // Method to get ruby icon
  private Image getRubyImage() {
    return Objects.requireNonNull(this.importImage("/ruby.png"))
                    .getScaledInstance(10, 10, Image.SCALE_FAST);
  }

  // Method to get diamond icon
  private Image getDiamondImage() {
    return Objects.requireNonNull(this.importImage("/diamond.png"))
                    .getScaledInstance(10, 10, Image.SCALE_FAST);
  }

  // Method to get Otyugh icon
  private Image getOtyughImage() {
    return Objects.requireNonNull(this.importImage("/otyugh.png"))
                    .getScaledInstance(Objects.requireNonNull(
                    this.importImage("/otyugh.png"))
                                    .getWidth(this) / 2,
                    Objects.requireNonNull(this.importImage("/otyugh.png"))
                                    .getHeight(this) / 2, Image.SCALE_FAST);
  }

  // Method to get Strong smell icon
  private Image getStrongSmellImage() {
    return this.importImage("/stench02.png");
  }

  // Method to get Weak smell icon
  private Image getWeakSmellImage() {
    return this.importImage("/stench01.png");
  }

  // Method to get arrow icon
  private Image getArrowImage() {
    return Objects.requireNonNull(this.importImage("/arrow-white.png"))
                    .getScaledInstance(Objects.requireNonNull(
                                    this.importImage("/arrow-white.png"))
                                            .getWidth(this) / 2,
                    Objects.requireNonNull(this.importImage("/arrow-white.png"))
                                    .getHeight(this) / 2, Image.SCALE_FAST);
  }

  // Method to get location with one arrow landed icon
  private Image getSingleLandedImage() {
    return Objects.requireNonNull(this.importImage("/singleArrow.png"))
                    .getScaledInstance(CAVE_PIXELS, CAVE_PIXELS, Image.SCALE_FAST);
  }

  // Method to get location with multiple arrow landed icon
  private Image getMultipleLandedImage() {
    return Objects.requireNonNull(this.importImage("/multipleArrow.png"))
                    .getScaledInstance(CAVE_PIXELS, CAVE_PIXELS, Image.SCALE_FAST);
  }

  // Method to get cave image icon
  private Image getCaveImage(String type) {
    return this.importImage(String.format("/%s.png", type));
  }

  // Method to get blank/undiscovered cave icon
  private Image getBlankImage() {
    return this.importImage("/blank.png");
  }

  // Method to get player icon
  private Image getPlayerImage() {
    return this.importImage("/archer.png");
  }

}
