package cs3500.pa04.model;

/**
 * Represents different types of ships in a BattleSalvo game
 */
public enum ShipType {
  CARRIER(6), BATTLESHIP(5), DESTROYER(4),
  SUBMARINE(3);

  private final int size;

  /**
   * Intializes a ShipType with a given size
   *
   * @param size The given size
   */
  private ShipType(int size) {
    this.size = size;
  }

  /**
   * Returns the size of a ShipType
   *
   * @return The size
   */
  public int getSize() {
    return size;
  }
}
