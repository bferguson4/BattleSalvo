package cs3500.pa04.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents an AI player for the BattleSalvo game
 */
public class AiPlayer extends AbstractPlayer {


  private List<Coord> hits = new ArrayList<>();
  private SalvoDatabase sd = new SalvoDatabase();
  private List<PotentialShips> potential = new ArrayList<>();

  /**
   * Initializes the AI player with a given random
   *
   * @param rand The given Random
   */
  public AiPlayer(Random rand) {
    super(rand);
  }

  public AiPlayer() {
    super(new Random());
  }

  /**
   * Initializes the fields with a given name, random, and boards
   *
   * @param rand The given Random
   * @param board The given board for this player's board
   * @param shootingBoard The given board to be the board for the opponent
   */
  public AiPlayer(Random rand,
                        List<List<Peg>> board, List<List<Peg>> shootingBoard) {
    super(rand, board, shootingBoard);

  }

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
    this.hits.addAll(hits);
    return hits;
  }

  private static void updateBoard(boolean wasHit, int x, int y, List<List<Peg>> board) {
    List<Peg> row = board.get(y);
    if (wasHit) {
      row.set(x, Peg.HIT);
    } else {
      row.set(x, Peg.MISS);
    }
  }

  /**
   * Returns this AI player's shots on the opponent's board. The number of shots returned should
   * equal the number of ships on this player's board that have not sunk.
   *
   * @return the locations of shots on the opponent's board
   */
  @Override
  public List<Coord> takeShots() {
    List<Coord> shots = new ArrayList<>();
    Ship.removeIfSunk(ships);
    List<Coord> availableShots = getRemainingShots();
    int totalAvailableShots = availableShots.size();
    int numShots =  Math.min(totalAvailableShots, ships.size());
    takeTacticalShots(availableShots, numShots, shots);
    int remainingShots = shots.size();
    for (int i = 0; i < numShots - remainingShots; i++) {
      shots.add(sd.getShot(availableShots));
    }
    previousShots = shots;
    return shots;
  }

  /**
   * Takes shots in a strategic way
   *
   * @param availableShots The available shots to take
   * @param numShots The number of allowed shots
   * @param shots The list of shots
   */
  private void takeTacticalShots(List<Coord> availableShots,
                                 int numShots, List<Coord> shots) {
    for (Coord shot : previousShots) {
      if (shootingBoard.get(shot.getY()).get(shot.getX()).equals(Peg.HIT)) {
        Coord nextV = new Coord(shot.getX(), shot.getY() + 1);
        Coord nextH = new Coord(shot.getX() + 1, shot.getY());
        Coord left = new Coord(shot.getX() - 1, shot.getY());
        Coord top = new Coord(shot.getX(), shot.getY() - 1);
        List<PotentialShips> shipsToRemove = new ArrayList<>();
        for (PotentialShips ship : potential) {
          if (ship.coord.equals(shot)) {
            shipsToRemove.add(ship);
            if (ship.direction.equals(Direction.VERTICAL)) {
              if (ship.isNext) {
                while (availableShots.contains(nextV) && shots.size() < maxShipSize) {
                  shots.add(nextV);
                  availableShots.remove(nextV);
                  nextV = new Coord(shot.getX(), shot.getY() + 1);
                }
              } else {
                while (availableShots.contains(top) && shots.size() < maxShipSize) {
                  shots.add(top);
                  availableShots.remove(top);
                  top = new Coord(shot.getX(), shot.getY() - 1);
                }
              }
            } else {
              if (ship.isNext) {
                while (availableShots.contains(nextH) && shots.size() < maxShipSize) {
                  shots.add(nextH);
                  availableShots.remove(nextH);
                  nextH = new Coord(shot.getX() + 1, shot.getY());
                }
              } else {
                while (availableShots.contains(left) && shots.size() < maxShipSize) {
                  shots.add(left);
                  availableShots.remove(left);
                  left = new Coord(shot.getX() - 1, shot.getY());
                }
              }
            }
          }
        }
        potential.removeAll(shipsToRemove);
        if (availableShots.contains(nextH) && shots.size() < numShots) {
          shots.add(nextH);
          availableShots.remove(nextH);
          potential.add(new PotentialShips(nextH, Direction.HORIZONTAL, true));
        }
        if (availableShots.contains(nextV) && shots.size() < numShots) {
          shots.add(nextV);
          availableShots.remove(nextV);
          potential.add(new PotentialShips(nextV, Direction.VERTICAL, true));
        }
        if (availableShots.contains(left) && shots.size() < numShots) {
          shots.add(left);
          availableShots.remove(left);
          potential.add(new PotentialShips(left, Direction.HORIZONTAL, false));
        }
        if (availableShots.contains(top) && shots.size() < numShots) {
          shots.add(top);
          availableShots.remove(top);
          potential.add(new PotentialShips(top, Direction.VERTICAL, false));
        }
      }
    }
  }


  /**
   * Gets the shots that remaining for this salvo round
   *
   * @return The list of remaining Coords
   */
  private List<Coord> getRemainingShots() {
    List<Coord> remainingCoords = new ArrayList<>();
    for (int r = 0; r < shootingBoard.size(); r++) {
      for (int c = 0; c < shootingBoard.get(r).size(); c++) {
        Peg p = shootingBoard.get(r).get(c);
        if (p.equals(Peg.EMPTY)) {
          remainingCoords.add(new Coord(c, r));
        }
      }
    }
    return remainingCoords;
  }

  public void endGame(GameResult result, String reason) {
    sd.writeHits(hits);
  }
}
