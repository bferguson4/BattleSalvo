module cs3500.pa04 {
  requires javafx.controls;
  requires javafx.fxml;

  requires org.controlsfx.controls;
  requires com.fasterxml.jackson.core;
  requires com.fasterxml.jackson.databind;

  opens cs3500.pa04 to javafx.fxml;
  exports cs3500.pa04;
  exports cs3500.pa04.controller;
  exports cs3500.pa04.model;
  exports cs3500.pa04.view;
  opens cs3500.pa04.controller to javafx.fxml;
}