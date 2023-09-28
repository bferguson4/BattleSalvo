package cs3500.pa04.view;

/**
 * An interface that represents a view
 */
public interface View {

  /**
   * Writes a given message onto the view
   *
   * @param message The given message
   */
  public void write(String message);


  /**
   * Gets the next item from a view
   *
   * @return The next item as a String
   */
  public String getNext();

  /**
   * Skips a line from the view's input
   */
  public void skipLine();
}
