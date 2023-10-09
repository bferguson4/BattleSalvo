package cs3500.pa04.controller;

import cs3500.pa04.view.SceneLoader;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Represents an abstracted controller for interfaces
 */
public abstract class InterfaceController implements Controller {

  private Stage stage;

  public InterfaceController(Stage stage) {
    this.stage = stage;
  }

  /**
   * Sets the scene of the controller
   *
   * @param scene The given scene
   */
  public void setScene(String scene) {
    SceneLoader sceneLoader = new SceneLoader(this, scene);
    stage.setScene(sceneLoader.load());
  }

  /**
   * Binds the submission of a text fields related to a control
   *
   * @param submit The given control
   * @param texts The text fields
   */
  public void bindSubmit(Control submit, TextField... texts) {
    submit.disableProperty().bind(bindsubmitHelp(0, texts));
  }

  /**
   * Helps recursively bind the submissino of a control
   *
   * @param index The index of the text field
   * @param texts The given text fields
   * @return The BooleanBinding related to the text field
   */
  private BooleanBinding bindsubmitHelp(int index, TextField... texts) {
    if (index == texts.length - 1) {
      return Bindings.isEmpty(texts[index].textProperty());
    }
    return Bindings.or(Bindings.isEmpty(texts[index].textProperty()),
        bindsubmitHelp(index + 1, texts));
  }
}