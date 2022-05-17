# Dungeon Traversal Game
## About/Overview

This project represents a game which allows a player to enter and move around a dungeon which can be represented in a two dimensional form. A dungeon has an end cave and start cave, a player starts at the start cave and can travel till they are alive/reach the end cave. A player is said to have won the game if they reach the end cave alive.

Dungeons have treasures/gems in them in a specified percentage of caves, that the player can pick up.

A dungeon also has monsters in it which will kill the player if the monster is uninjured. A monster is always present in the end cave, but can also have additional monsters depending on the dungeon's configuration. To combat a monster, a player can pick up arrows in the dungeon, which can be shot at monsters. These arrows are present with the same frequency of treasures.

There is also a GUI implemented using Swing present, which uses the above model and a controller which a player can use to play the game till the player has won the game or is killed by a monster.

We will go into the exhaustive list of all features/operations in later sections of this README.

## List of features

A player can be created with the following attributes:
- name

A dungeon can be created with the following attributes:
- A number of rows
- A number of columns
- Degree of interconnectivity (in how many unique paths can a player travel from one point to another)
- Wrapping/non-wrapping: Choice to allow paths from last to first rows/columns and vice versa.
- Percentage of treasure and arrows in caves.
- Number of monsters in the dungeon.

Each point in a dungeon represents a cave or a tunnel. A tunnel is a point which has exactly one entry and one exit. Treasures cannot exist in a tunnel, and players can't enter or end in a tunnel. 

A player can enter a dungeon at the start location, and choose to move to any of the adjacent/connected locations. The distance between the start and end cave is at least 5 moves. A player always starts with three arrows with them.

Treasures are of three types, Rubies, Sapphires and Diamonds. Each of these has a quality associated with them (Low, Average, High) that determines their value. A cave can have any random assortment of these. 

Each location may also have a monster present in it. A monster can be killed if shot by two arrows. A monster which is injured, i.e has been shot by one arrow has a 50% chance of killing the player. A player entering a cave with uninjured monsters will always be killed.

A player can shoot crooked arrows, by specifying a direction and distance to shoot. An arrow shot by a player can curve in a tunnel and come out of the other exit. An arrow hits a monster if it is shot such that it lands exactly in the monster's location. 

A player can detect a nearby monster by the smell of the monster. If a monster is present in an immediately adjacent location, it gives off a strong smell. If one is present two locations away, it emits a weak smell. However, if there are multiple monsters two locations away, they emit strong smell. 

Using these features, a player may try to reach the end location and win the game.

A controller has been implemented to help the player navigate the dungeon, detailed instructions on how to use the controller are provided in the following sections.

## How to run

The jar file can be run with the following command:

Command, in the res/ folder:
```
 java –jar DungeonModelController.jar --<type>
```

`<type>` can be `gui` for UI game or `text` for text based game.

This will ask the user for further inputs which are detailed in the following section.

## How to use the program

Once the .jar file is run, the controller can be navigated by using the following commands/inputs:
- Text Based:
    ```
    Move to an adjacent location:
    m , followed by the option of available locations (1, 2, 3 etc)
    
    Pickup treasure
    t

    Pickup arrow
    a

    Shoot arrow
    s, followed by the option of available locations (1, 2, 3 etc), followed by distance (positive integer)

    Quit game
    q
    ```
- GUI Based
    - Select `Start Game` on the screen to get a menu for configuration of the game.
    - Enter configuration and start the game
    - Use arrow keys or mouse clicks to move around the caves
    - Use key `a` to pickup arrow and key `t` to pickup treasure. Popups after each operation display status of the move, i.e if the operation was succesful or not.
    - Use arrow key while holding down `s` key to shoot an arrow in a certain direction. A pop up will ask for distance, after the shoot move, a message will be displayed with the landing location of the arrow.
    - Strong smell in a cave is represented by a bright green haze, weak by a slightly lesser bright haze.
    - You can restart the game at any point by selecting `Start Game` again, followed by an option to select if it is a new game or same game/settings.
    - There is a helpful `Cheat` option to make all caves visible, in case you want to see them.
    - At any time, to see instructions on how to run the game, use the `Help` option given. This will display a list of all moves/operations of the game.

## Description of examples

`res\screenshots` folder contains screenshots of all the operations and info panels in the game. 

## Design/Model Changes

Compared to the first version of the UML/Design document, the final implementation has a few significant changes. They are listed below: 
- ReadOnly versions of models were created for the view to access.
- Treasure types were represented as Enums instead of Strings
- Controller was refactored to enable common operations between both GUI based and Text based views.

## Citations
- None

## Limitations

- Rendering of dungeon caves can lag slightly if Cheat option is used on large dungeons to make all caves visible.

## Assumptions

- The movement of the player is supplied in the form of coordinates, and not directions. Since current coordinates are always available and adjacent caves can be viewed from current location, these can be used to make a decision.
- Controller is strictly option driven for most operations (cannot choose one that is unavailable in the displayed list of available options)

