package cs3500.pa04.controller;

import cs3500.pa04.model.Player;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class ShotHandler implements EventHandler<ActionEvent> {

  private int shotsTaken;

  public ShotHandler() {
    this.shotsTaken = 0;
  }

  /**
   * Invoked when a specific event of the type for which this handler is
   * registered happens.
   *
   * @param event the event which occurred
   */
  @Override
  public void handle(ActionEvent event) {
    shotsTaken++;
  }

  public int getShotsTaken() {
    return shotsTaken;
  }
}
