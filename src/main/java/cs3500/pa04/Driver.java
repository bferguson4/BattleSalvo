package cs3500.pa04;

import cs3500.pa04.controller.BattleController;
import cs3500.pa04.controller.StartController;
import cs3500.pa04.model.AiPlayer;
import cs3500.pa04.model.Peg;
import cs3500.pa04.model.SalvoDatabase;
import cs3500.pa04.view.SceneLoader;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * This is the main driver of this project.
 */
public class Driver extends Application {

  /**
   * Starts the application for BattleSalvo
   *
   * @param stage the primary stage for this application, onto which
   * the application scene can be set.
   * Applications may create other stages, if needed, but they will not be
   * primary stages.
   */
  public void start(Stage stage) {
    StartController sc = new StartController(stage);
    SceneLoader loader = new SceneLoader(sc, "splash.fxml");
    stage.setScene(loader.load());
    sc.run();
    stage.show();
  }


  /**
   * Runs a game of BattleSalvo
   *
   * @param args - no command line args required
   */
  public static void main(String[] args) {
    launch();
  }
}