package cs3500.pa04.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Adapts a Ship to be mapped to the JSON messages
 */
public class ShipAdapter extends Ship {

  private final Coord coord;
  private final int length;
  private final Direction direction;

  /**
   * Constructor that takes in a Coord, length, and direction of the ship
   *
   * @param coord : the starting Coordinate of the ship
   * @param length : the length of the ship
   * @param direction : the direction in which the ship is facing
   */
  @JsonCreator
  public ShipAdapter(@JsonProperty("coord") Coord coord, @JsonProperty("length") int length,
                     @JsonProperty("direction") Direction direction) {

    this.coord = coord;
    this.length = length;
    this.direction = direction;
  }

  /**
   * Creates a new ShipAdapter with all the corresponding parameters given the location in which a
   * Ship lies
   *
   * @param location : the list of coordinates where the ship lies
   * @return a ShipAdapter that represents the given Ship Coords
   */
  public static ShipAdapter locationToShipAdapter(List<Coord> location) {
    Coord start = location.get(0);
    int length = location.size();
    Direction direction = null;
    if (checkDirection(location, true)) {
      direction = Direction.HORIZONTAL;
    } else if (checkDirection(location, false)) {
      direction = Direction.VERTICAL;
    }
    if (direction == null) {
      throw new IllegalArgumentException("Invalid location");
    }
    return new ShipAdapter(start, length, direction);
  }

  /**
   * Checks to see which direction the Ship is facing in
   *
   * @param location : the location of the ship
   * @param isCheckingHorizontal : check horizontal or vertical direction
   * @return whether the ship is facing the way of the given direction
   */
  private static boolean checkDirection(List<Coord> location, boolean isCheckingHorizontal) {
    int index  = 0;
    boolean isDirection = true;
    while (index < location.size() - 1 && isDirection) {
      Coord atIndex = location.get(index);
      Coord next = location.get(index + 1);
      if (isCheckingHorizontal) {
        isDirection = next.getX() == atIndex.getX() + 1;
      } else {
        isDirection = next.getY() == atIndex.getY() + 1;
      }
      index++;
    }
    return isDirection;
  }

  /**
   * Gets the direction of the ship
   *
   * @return direction
   */
  public Direction getDirection() {
    return direction;
  }

  /**
   * Gets the length of the ship
   *
   * @return length
   */
  public int getLength() {
    return length;
  }

  /**
   * Gets the starting Coord of the ship
   *
   * @return coord
   */
  public Coord getCoord() {
    return coord;
  }

}
