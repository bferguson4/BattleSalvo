package cs3500.pa04.controller;

import cs3500.pa04.model.AiPlayer;
import cs3500.pa04.model.ManualPlayer;
import cs3500.pa04.model.Peg;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class StartController implements Controller {

  @FXML
  private Button startButton;
  private Stage stage;

  public StartController(Stage stage) {
    this.stage = stage;
  }

  /**
   * Runs the start of the battle field application
   */
  public void run() {
    startButton.setOnAction(event -> {
      Controller bc = new BattleController(stage);
      bc.run();
    });
  }

}
