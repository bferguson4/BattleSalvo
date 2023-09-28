package cs3500.pa04.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a coordinate on a board
 */
public class Coord {

  private final int coordX;
  private final int coordY;

  /**
   * Initializes the x and y coordinates from a given x and y
   *
   * @param x The given x
   * @param y The given y
   * */
  @JsonCreator
  public Coord(@JsonProperty("x") int x, @JsonProperty("y") int y) {
    this.coordX = x;
    this.coordY = y;
  }

  /**
   * Overrides equals to compare Coords by field
   *
   * @param o The given object
   * @return Whether the object equals this Coord
   */
  @Override
  public boolean equals(Object o) {
    if (o instanceof Coord c) {
      return c.coordX == coordX && c.coordY == coordY;
    }
    return false;
  }

  @Override
  public int hashCode() {
    return this.coordX + this.coordY;
  }

  /**
   * Returns the x coordinate from this Coord
   *
   * @return The x coordinate
   */
  public int getX() {
    return coordX;
  }

  /**
   * Returns the y coordinate from this Coord
   *
   * @return The y coordinate
   */
  public int getY() {
    return coordY;
  }

  public String toString() {
    return coordX + ", " + coordY;
  }

  public static Coord stringToCoord(String str) {
    String[] coordinates = str.split(", ");
    int x = Integer.parseInt(coordinates[0]);
    int y = Integer.parseInt(coordinates[1]);
    return new Coord(x, y);
  }

}
