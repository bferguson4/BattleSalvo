package cs3500.pa04.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Represents an abstracted player for the BattleSalvo game
 */
public abstract class AbstractPlayer implements Player {

  protected List<List<Peg>> shootingBoard;
  protected List<List<Peg>> board;
  protected List<Ship> ships;
  protected List<Coord> previousShots;
  protected final Random rand;
  protected int maxShipSize;

  /**
   * Initializes the fields with a given name and random
   *
   * @param rand The given Random
   */
  public AbstractPlayer(Random rand) {
    this.ships = new ArrayList<>();
    this.previousShots = new ArrayList<>();
    this.board = new ArrayList<>();
    this.shootingBoard = new ArrayList<>();
    this.rand = rand;
  }

  /**
   * Initializes the fields with a given name, random, and boards
   *
   * @param rand The given Random
   * @param board The given board for this player's board
   * @param shootingBoard The given board to be the board for the opponent
   */
  public AbstractPlayer(Random rand,
                        List<List<Peg>> board, List<List<Peg>> shootingBoard) {
    this.ships = new ArrayList<>();
    this.previousShots = new ArrayList<>();
    this.rand = rand;
    this.board = board;
    this.shootingBoard = shootingBoard;
  }

  public AbstractPlayer(List<List<Peg>> board, List<List<Peg>> shootingBoard) {
    this.ships = new ArrayList<>();
    this.previousShots = new ArrayList<>();
    this.rand = new Random();
    this.board = board;
    this.shootingBoard = shootingBoard;
  }


  /**
   * Get the player's name.
   *
   * @return the player's name
   */
  @Override
  public String name() {
    return "pa04-365";
  }

  /**
   * Given the specifications for a BattleSalvo board, return a list of ships with their locations
   * on the board.
   *
   * @param height         the height of the board, range: [6, 15] inclusive
   * @param width          the width of the board, range: [6, 15] inclusive
   * @param specs a map of ship type to the number of occurrences each ship should
   *                       appear on the board
   * @return the placements of each ship on the board
   */
  @Override
  public List<Ship> setup(int height, int width, Map<ShipType, Integer> specs) {
    List<Ship> ships = new ArrayList<>();
    List<Coord> allCoords = new ArrayList<>();
    for (int y = 0; y < height; y++) {
      List<Peg> shootingRow = new ArrayList<>();
      List<Peg> row = new ArrayList<>();
      for (int x = 0; x < width; x++) {
        Coord c = new Coord(x, y);
        allCoords.add(c);
        row.add(Peg.EMPTY);
        shootingRow.add(Peg.EMPTY);
      }
      shootingBoard.add(shootingRow);
      board.add(row);
    }
    int max = 0;
    for (ShipType st : specs.keySet()) {
      if (st.getSize() > max) {
        max = st.getSize();
      }
      int numOfShip = specs.get(st);
      for (int i = 0; i < numOfShip; i++) {
        List<List<Coord>> possibleLocations = getPossibleCoords(allCoords, st.getSize());
        if (possibleLocations.size() == 0) {
          return setup(height, width, specs);
        }
        int randomLocation = rand.nextInt(possibleLocations.size());
        List<Coord> shipLocation = possibleLocations.get(randomLocation);
        for (Coord c : shipLocation) {
          allCoords.remove(c);
        }
        Ship s = new Ship(shipLocation);
        ShipAdapter sa = ShipAdapter.locationToShipAdapter(shipLocation);
        this.ships.add(s);
        ships.add(sa);
      }
    }
    maxShipSize = max;
    addShipsToBoard();
    return ships;
  }

  /**
   * Gets the possible locations for a specific ship
   *
   * @param allCoords All the remaining coordinates on the board
   * @param shipSize The size of the ship
   * @return The list of locations for the ship
   */
  private static List<List<Coord>> getPossibleCoords(List<Coord> allCoords, int shipSize) {
    List<List<Coord>> result = new ArrayList<>();
    for (int i = 0; i < allCoords.size(); i++) {
      Coord c = allCoords.get(i);
      List<Coord> possibleVertical =
          getPossibleInDirection(shipSize, c, allCoords, true);
      List<Coord> possibleHorizontal =
          getPossibleInDirection(shipSize, c, allCoords, false);
      if (possibleVertical.size() == shipSize) {
        result.add(possibleVertical);
      }
      if (possibleHorizontal.size() == shipSize) {
        result.add(possibleHorizontal);
      }
    }
    return result;
  }

  /**
   * Gets a possible location of a ship in either vertical or horizontal direction
   *
   * @param shipSize The size of the ship
   * @param start The starting coordinate of the ship
   * @param allCoords All the remaining coordinates
   * @param isVertical Whether the direction is vertical or not
   * @return The possible location of the ship
   */
  private static List<Coord> getPossibleInDirection(int shipSize, Coord start,
                                                    List<Coord> allCoords, boolean isVertical) {
    List<Coord> possible = new ArrayList<>();
    possible.add(start);
    int x = start.getX();
    int y = start.getY();
    for (int s = 1; s < shipSize; s++) {
      Coord next;
      if (isVertical) {
        next = new Coord(x, y + s);
      } else {
        next = new Coord(x + s, y);
      }
      if (allCoords.contains(next)) {
        possible.add(next);
      }
    }
    return possible;
  }

  /**
   * Adds this player's ships to their board
   */
  private void addShipsToBoard() {
    for (Ship s : ships) {
      s.addShip(board);
    }
  }

  /**
   * Returns this player's shots on the opponent's board. The number of shots returned should
   * equal the number of ships on this player's board that have not sunk.
   *
   * @return the locations of shots on the opponent's board
   */
  @Override
  public abstract List<Coord> takeShots();

  /**
   * Given the list of shots the opponent has fired on this player's board, report which
   * shots hit a ship on this player's board.
   *
   * @param opponentShotsOnBoard the opponent's shots on this player's board
   * @return a list of shots that hit this player's board
   */
  @Override
  public List<Coord> reportDamage(List<Coord> opponentShotsOnBoard) {
    List<Coord> hits = new ArrayList<>();
    for (Coord c : opponentShotsOnBoard) {
      int x = c.getX();
      int y = c.getY();
      boolean hit = false;
      for (Ship s : ships) {
        if (s.isHit(c)) {
          hit = true;
          hits.add(c);
          break;
        }
      }
      updateBoard(hit, x, y, board);
    }
    return hits;
  }

  /**
   * Reports to this AI player what shots in their previous volley returned from takeShots()
   * successfully hit an opponent's ship.
   *
   * @param shotsThatHitOpponentShips the list of shots that successfully hit the opponent's ships
   */
  @Override
  public void successfulHits(List<Coord> shotsThatHitOpponentShips) {
    for (Coord c : previousShots) {
      int x = c.getX();
      int y = c.getY();
      updateBoard(shotsThatHitOpponentShips.contains(c), x, y, shootingBoard);
    }
  }

  /**
   * Notifies the player that the game is over.
   * Win, lose, and draw should all be supported
   *
   * @param result if the player has won, lost, or forced a draw
   * @param reason the reason for the game ending
   */
  @Override
  public void endGame(GameResult result, String reason) {
    //Implemented in later PAs
  }

  /**
   * Updates a given coord in the board based on if the coord was hit
   *
   * @param wasHit If the board was hit
   * @param x The x value in the Coord
   * @param y The y value in the Coord
   * @param board The given board
   */
  private static void updateBoard(boolean wasHit, int x, int y, List<List<Peg>> board) {
    List<Peg> row = board.get(y);
    if (wasHit) {
      row.set(x, Peg.HIT);
    } else {
      row.set(x, Peg.MISS);
    }
  }

}
