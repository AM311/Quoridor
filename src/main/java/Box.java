public class Box {
  private boolean taken;

  public Box(boolean taken) {
    this.taken = taken;
  }

  public boolean isTaken() {
    return taken;
  }

  public void setTaken(boolean taken) {
    this.taken = taken;
  }
}
