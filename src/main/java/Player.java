public class Player {
  private final String name;
  private int numberOfWalls;
  private Pawn pawn;

  public Player(String name, int numberOfWalls, Pawn pawn) {
    this.name = name;
    this.numberOfWalls = numberOfWalls;
    this.pawn = pawn;
  }

  public void useWall() {
    numberOfWalls--;
  }

  public String getName() {
    return name;
  }

  public int getNumberOfWalls() {
    return numberOfWalls;
  }

  public Pawn getPawn() {
    return pawn;
  }
}
