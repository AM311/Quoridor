import java.awt.*;

public class Pawn {
  private int currentRow;
  private int currentColumn;
  private final int startingRow;
  private final int startingColumn;
  private final Color color;

  public Pawn(int currentRow, int currentColumn, int startingRow, int startingColumn, Color color) {
    this.currentRow = currentRow;
    this.currentColumn = currentColumn;
    this.startingRow = startingRow;
    this.startingColumn = startingColumn;
    this.color = color;
  }

  public void setCurrentRow(int currentRow) {
    this.currentRow = currentRow;
  }

  public void setCurrentColumn(int currentColumn) {
    this.currentColumn = currentColumn;
  }

  public int getCurrentRow() {
    return currentRow;
  }

  public int getCurrentColumn() {
    return currentColumn;
  }

  public int getStartingRow() {
    return startingRow;
  }

  public int getStartingColumn() {
    return startingColumn;
  }

  public Color getColor() {
    return color;
  }
}
