package cs3500.pa04.model;

public class PotentialShips {

  public Coord coord;
  public Direction direction;
  public boolean isNext;

  PotentialShips(Coord coord, Direction direction,  boolean isNext) {
    this.coord = coord;
    this.direction = direction;
    this.isNext = isNext;
  }

}
