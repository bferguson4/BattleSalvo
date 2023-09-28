package cs3500.pa04.model;

import javafx.scene.paint.Color;

/**
 * Represents a Peg on the board of a BattleSalvo game
 */
public enum Peg {
  SHIP("S", Color.BLACK), MISS("M", Color.LIGHTBLUE),
  HIT("H", Color.RED), EMPTY("0", Color.BLUE);

  private final String pegLetter;
  private final Color color;

  /**
   * Sets the letter for the peg
   *
   * @param letter A given letter
   */
  private Peg(String letter, Color color) {
    pegLetter = letter;
    this.color = color;
  }

  /**
   * Overrides toString to return the letter for representing a Peg
   *
   * @return The letter
   */
  @Override
  public String toString() {
    return pegLetter;
  }

  public Color getColor() {
    return color;
  }
}
