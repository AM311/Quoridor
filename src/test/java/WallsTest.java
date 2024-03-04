import it.units.sdm.quoridor.model.Game;
import it.units.sdm.quoridor.model.GameBoard;
import it.units.sdm.quoridor.model.Pawn;
import it.units.sdm.quoridor.model.Player;
import it.units.sdm.quoridor.utils.Direction;
import it.units.sdm.quoridor.utils.WallOrientation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.awt.*;
import java.util.List;

public class WallsTest {

    GameBoard gameBoard = new GameBoard();

    GameBoard.Tile tile1 = gameBoard.getGameState()[0][4];
    Pawn pawn1 = new Pawn(tile1, Color.black);
    Player player1 = new Player("Bob", 10, pawn1);
    GameBoard.Tile tile2 = gameBoard.getGameState()[8][4];
    Pawn pawn2 = new Pawn(tile2, Color.red);
    Player player2 = new Player("Alice", 10, pawn2);

    List<Player> players = List.of(player1, player2);
    Game game = new Game(players, gameBoard);

}
