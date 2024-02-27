import java.util.List;

public class Game {
  private final List<Player> players;
  private final GameBoard gameBoard;

  public Game(List<Player> players, GameBoard gameBoard) {
    this.players = players;
    this.gameBoard = gameBoard;
  }

  public List<Player> getPlayers() {
    return players;
  }

  public GameBoard getGameBoard() {
    return gameBoard;
  }
}
