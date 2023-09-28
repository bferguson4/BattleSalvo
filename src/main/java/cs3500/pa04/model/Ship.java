package cs3500.pa04.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a ship in the BattleSalvo game
 */
public class Ship {

  private final List<Coord> location;
  private boolean isSunk;

  /**
   * Initializes with a List of Coord location
   *
   * @param location The given location
   */
  public Ship(List<Coord> location) {
    this.location = location;
    isSunk = false;
  }

  public Ship() {
    location = new ArrayList<>();
    isSunk = false;
  }


  /**
   * Adds this ship to a board of Pegs
   *
   * @param board The given board of Pegs
   */
  public void addShip(List<List<Peg>> board) {
    for (Coord c : location) {
      int x = c.getX();
      int y = c.getY();
      List<Peg> row = board.get(y);
      row.set(x, Peg.SHIP);
    }
  }

  /**
   * Returns if a given shot hit this ship
   * Sets isSunk to true if all the Coords have been hit
   *
   * @param c The given Coord shot
   * @return Whether the ship was hit
   */
  public boolean isHit(Coord c) {
    if (location.contains(c)) {
      location.remove(c);
      if (location.size() == 0) {
        isSunk = true;
      }
      return true;
    }
    return false;
  }

  /**
   * Removes any ships from a list of Ship if the ship isSunk
   *
   * @param ships A given list of ships
   */
  public static void removeIfSunk(List<Ship> ships) {
    ships.removeIf(ship -> ship.isSunk);
  }

  /**
   * Overrides equals to compare Ships by fields
   *
   * @param o A given object
   * @return If this Ship equals a given object
   */
  public boolean equals(Object o) {
    if (o instanceof Ship s) {
      return location.equals(s.location) && isSunk == s.isSunk;
    }
    return false;
  }

  public String toString() {
    return "Location" + location;
  }

}
