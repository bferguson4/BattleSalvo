package cs3500.pa04.view;

import java.io.IOException;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Represents a simple Whack-a-Mole GUI view.
 */
public class SceneLoader {

  FXMLLoader loader;

  /**
   * Represents the constructor for SceneLoader
   *
   * @param controller controller of the object
   * @param file       given file name
   */
  public SceneLoader(Object controller, String file) {
    // look up and store the layout
    this.loader = new FXMLLoader();
    this.loader.setLocation(getClass().getClassLoader().getResource(file));
    this.loader.setController(controller);
  }

  /**
   * Loads scene
   *
   * @return the layout
   */
  public Scene load() {
    try {
      return this.loader.load();
    } catch (IOException exc) {
      throw new IllegalStateException("Unable to load layout.");
    }
  }

  /**
   * Displays a warning message about incorrect inputs
   */
  public static void displayWarning(Stage stage, String message) {
    Popup warning = new Popup();
    Label text = new Label(message);
    text.setFont(new Font(15));
    text.setStyle("-fx-background-color: red");
    warning.getContent().add(text);
    warning.show(stage);
    Timeline showTime = new Timeline(new KeyFrame(Duration.seconds(2),
        event -> warning.hide()));
    showTime.setCycleCount(1);
    showTime.play();
  }

}