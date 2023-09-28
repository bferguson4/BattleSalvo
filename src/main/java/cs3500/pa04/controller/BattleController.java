package cs3500.pa04.controller;

import static cs3500.pa04.view.SceneLoader.displayWarning;

import cs3500.pa04.model.AiPlayer;
import cs3500.pa04.model.Coord;
import cs3500.pa04.model.ManualPlayer;
import cs3500.pa04.model.Peg;
import cs3500.pa04.model.Player;
import cs3500.pa04.model.ShipType;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Represents the main controller for the BattleSalvo game
 */
public class BattleController extends InterfaceController {

  private final ManualPlayer player1;
  private final Player player2;
  private final List<List<Peg>> board1;
  private final List<List<Peg>> board2;
  private Stage stage;
  @FXML
  private TextField widthInput;
  @FXML
  private TextField heightInput;
  @FXML
  private TextField carrierInput;
  @FXML
  private TextField battleInput;
  @FXML
  private TextField destroyerInput;
  @FXML
  private TextField subInput;
  @FXML
  private Button submitFleet;
  @FXML
  private Button submitDimen;
  @FXML
  private Button shoot;
  @FXML
  private HBox yourBox;
  @FXML
  private HBox enemyBox;
  @FXML
  private TextField xInput;
  @FXML
  private TextField yInput;
  @FXML
  private Text shotsCounter;


  /**
   * Initializes the BattleModel and the BattleView from two Players and boards
   */
  public BattleController(Stage stage) {
    super(stage);
    this.board1 = new ArrayList<>();
    this.board2 = new ArrayList<>();
    this.player2 = new AiPlayer();
    this.player1 = new ManualPlayer(board1, board2);
    this.stage = stage;
  }


  /**
   * Runs the BattleSalvo game
   */
  @Override
  public void run() {
    setScene("dimensions.fxml");
    bindSubmit(submitDimen, widthInput, heightInput);
    submitDimen.setOnAction(event -> {
      try {
        int width = Integer.parseInt(widthInput.getText());
        int height = Integer.parseInt(heightInput.getText());
        if (height < 6 || height > 15 || width < 6 || width > 15) {
          displayWarning(stage, "Insufficient dimensions");
          throw new NumberFormatException();
        }
        enterFleet(height, width);
      } catch (NumberFormatException e) {
        run();
      }
    });
  }

  /**
   * Enters the fleet of ships for the game with the given height and width
   *
   * @param height The given height
   * @param width The given width
   */
  private void enterFleet(int height, int width) {
    int maxShips = Math.min(height, width);
    setScene("fleet.fxml");
    bindSubmit(submitFleet, subInput, carrierInput, destroyerInput, battleInput);
    submitFleet.setOnAction(event -> {
      LinkedHashMap<ShipType, Integer> specs = getShipSpecs(maxShips);
      player1.setup(height, width, specs);
      player2.setup(height, width, specs);
      runSalvo();
    });
  }


  private void runSalvo() {
    setScene("battle.fxml");
    displayBoards();
    bindSubmit(shoot, xInput, yInput);
    List<Coord> shots = new ArrayList<>();
    int numShots = player1.takeShots().size();
    List<Coord> player2Shots = player2.takeShots();
    if (numShots == 0 || player2Shots.isEmpty()) {
      xInput.setDisable(true);
      yInput.setDisable(true);
      shotsCounter.setText("Good Game!");
      return;
    }
    shotsCounter.setText("Shots Left: " + numShots);
    shoot.setOnAction(event -> {
      if (shots.size() < numShots) {
        if (takeShot(shots) == null) {
          runSalvo();
        } else {
          shots.add(takeShot(shots));
          shotsCounter.setText("Shots left: " + (numShots - shots.size()));
          xInput.clear();
          yInput.clear();
        }
      } if (shots.size() == numShots) {
        List<Coord> shotsThatHitPlayer1 = player1.reportDamage(player2Shots);
        List<Coord> shotsThatHitPlayer2 = player2.reportDamage(shots);
        player1.setPreviousShots(shots);
        player1.successfulHits(shotsThatHitPlayer2);
        player2.successfulHits(shotsThatHitPlayer1);
        displayBoards();
        runSalvo();
      }
    });
  }

  private Coord takeShot(List<Coord> shots) {
    List<Coord> availableShots = getRemainingShots();
    try {
      int shotX = Integer.parseInt(xInput.getText());
      int shotY = Integer.parseInt(yInput.getText());
      Coord shot = new Coord(shotX, shotY);
      if (!availableShots.contains(shot) || shots.contains(shot)) {
        displayWarning(stage, "Shot was not avaiable");
        throw new NumberFormatException();
      }
      return shot;
    } catch (NumberFormatException e) {
      return null;
    }
  }

  private List<Coord> getRemainingShots() {
    List<Coord> remainingCoords = new ArrayList<>();
    for (int r = 0; r < board2.size(); r++) {
      for (int c = 0; c < board2.get(r).size(); c++) {
        Peg p = board2.get(r).get(c);
        if (p.equals(Peg.EMPTY)) {
          remainingCoords.add(new Coord(c, r));
        }
      }
    }
    return remainingCoords;
  }


  private void displayBoards() {
    yourBox.getChildren().clear();
    enemyBox.getChildren().clear();
    GridPane yourBoard = displayBoard(board1);
    GridPane player2Board = displayBoard(board2);
    yourBox.getChildren().add(yourBoard);
    enemyBox.getChildren().add(player2Board);
  }

  @FXML
  private static GridPane displayBoard(List<List<Peg>> board) {
    GridPane boardPane = new GridPane();
    int gridSize = 350;
    boardPane.setPrefSize(gridSize, gridSize);
    int recSize = Math.min(gridSize / board.size(), gridSize / board.get(0).size());
    for (int r = 0; r < board.size(); r++) {
      boardPane.add(new Text(String.valueOf(r)), 0, r + 1);
      for (int c = 0; c < board.get(r).size(); c++) {
        if (r == 0) {
          Text colIndex = new Text(String.valueOf(c));
          GridPane.setHalignment(colIndex, HPos.CENTER);
          boardPane.add(colIndex, c + 1, 0);
        }
        Peg peg = board.get(r).get(c);
        Rectangle square = new Rectangle(recSize, recSize);
        square.setStyle("-fx-stroke: gray; -fx-stroke-width: 1;");
        square.setFill(peg.getColor());
        boardPane.add(square, c + 1, r + 1);
      }
    }

    return boardPane;
  }

  /**
   * Gets the specifications for the ship fleet
   *
   * @param maxShips The maximum number of ships in the ship fleet
   * @return The specifications of the fleet
   */
  private LinkedHashMap<ShipType, Integer> getShipSpecs(int maxShips) {
    try {
      LinkedHashMap<ShipType, Integer> specs = new LinkedHashMap<>();
      specs.put(ShipType.CARRIER, Integer.parseInt(carrierInput.getText()));
      specs.put(ShipType.DESTROYER, Integer.parseInt(destroyerInput.getText()));
      specs.put(ShipType.BATTLESHIP, Integer.parseInt(battleInput.getText()));
      specs.put(ShipType.SUBMARINE, Integer.parseInt(subInput.getText()));
      if (checkInvalidFleet(specs.values().stream().toList(), maxShips)) {
        displayWarning(stage, "Insufficient fleet");
        throw new NumberFormatException();
      }
      return specs;
    } catch (NumberFormatException e) {
      return getShipSpecs(maxShips);
    }
  }

  /**
   * Cheeks if the fleet specifications are invalid
   *
   * @param specsValues The amount of each ship
   * @param maxShips The maximum total ships
   * @return If the fleet is invalid
   */
  private boolean checkInvalidFleet(List<Integer> specsValues, int maxShips) {
    int sum = 0;
    for (int i : specsValues) {
      sum += i;
      if (i <= 0) {
        return true;
      }
    }
    return sum > maxShips;
  }



}
