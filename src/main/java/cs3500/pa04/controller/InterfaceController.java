package cs3500.pa04.controller;

import cs3500.pa04.view.SceneLoader;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public abstract class InterfaceController implements Controller {

  private Stage stage;

  public InterfaceController(Stage stage) {
    this.stage = stage;
  }

  public void setScene(String scene) {
    SceneLoader sceneLoader = new SceneLoader(this, scene);
    stage.setScene(sceneLoader.load());
  }


  public void bindSubmit(Control submit, TextField... texts) {
    submit.disableProperty().bind(bindsubmitHelp(0, texts));
  }

  private BooleanBinding bindsubmitHelp(int index, TextField... texts) {
    if (index == texts.length - 1) {
      return Bindings.isEmpty(texts[index].textProperty());
    }
    return Bindings.or(Bindings.isEmpty(texts[index].textProperty()),
        bindsubmitHelp(index + 1, texts));
  }
}
