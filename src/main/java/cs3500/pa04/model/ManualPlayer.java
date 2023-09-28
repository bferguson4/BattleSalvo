package cs3500.pa04.model;

import java.util.ArrayList;
import java.util.List;
import javafx.stage.Stage;

/**
 * Represents a manual player of the BattleSalvo game
 */
public class ManualPlayer extends AbstractPlayer {

  private Stage stage;


  /**
   * Initializes the manual player with a given name, random, and boards
   *
   * @param rand The given random
   * @param board The given board
   * @param shootingBoard The given board for the opponent
   */
  public ManualPlayer(List<List<Peg>> board, List<List<Peg>> shootingBoard) {
    super(board, shootingBoard);
  }

  /**
   * Returns this manual player's shots on the opponent's board. The number of shots returned should
   * equal the number of ships on this player's board that have not sunk.
   *
   * @return the locations of shots on the opponent's board
   */
  @Override
  public List<Coord> takeShots() {
    Ship.removeIfSunk(ships);
    List<Coord> availableShots = getRemainingShots();
    int totalAvailableShots = availableShots.size();
    int numShots =  Math.min(totalAvailableShots, ships.size());
    List<Coord> shots = new ArrayList<>();
    for (int i = 0; i < numShots; i++) {
      shots.add(new Coord(0, 0));
    }
    return shots;
  }

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

  public void setPreviousShots(List<Coord> shots) {
    previousShots = shots;
  }


}
