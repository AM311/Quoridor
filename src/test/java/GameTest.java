import it.units.sdm.quoridor.model.Game;
import it.units.sdm.quoridor.model.GameBoard;
import it.units.sdm.quoridor.model.Pawn;
import it.units.sdm.quoridor.model.Player;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.ValueSource;

import java.awt.*;
import java.util.List;

public class GameTest {

    GameBoard gameBoard = new GameBoard();

    GameBoard.Tile tile1 = gameBoard.new Tile(0, 4, true);
    Pawn pawn1 = new Pawn(tile1, Color.black);
    Player player1 = new Player("Bob", 10, pawn1);
    GameBoard.Tile tile2 = gameBoard.new Tile(8, 4, true);
    Pawn pawn2 = new Pawn(tile2, Color.red);
    Player player2 = new Player("Alice", 10, pawn2);

    List<Player> players = List.of(player1, player2);
    Game game = new Game(players, gameBoard);


    @Test
    void horizontalWallIn33IsAllowed() {
        GameBoard.Tile startingTile = gameBoard.getGameState()[3][3];

        Assertions.assertTrue(game.checkWallPosition(true, startingTile));
    }

    @Test
    void horizontalWallIn88IsNotAllowed() {
        GameBoard.Tile startingTile = gameBoard.getGameState()[8][8];

        Assertions.assertFalse(game.checkWallPosition(true, startingTile));
    }

    @Test
    void horizontalWallIn70IsAllowed() {
        GameBoard.Tile startingTile = gameBoard.getGameState()[7][0];

        Assertions.assertTrue(game.checkWallPosition(true, startingTile));
    }

    @Test
    void horizontalWallCrossingVerticalWallIsNotAllowed() {
        GameBoard.Tile startingTile = gameBoard.getGameState()[2][4];
        GameBoard.Tile belowStartingTile = gameBoard.getGameState()[startingTile.getRow() + 1][startingTile.getColumn()];

        startingTile.setRightLink(GameBoard.LinkState.WALL);
        belowStartingTile.setRightLink(GameBoard.LinkState.WALL);


        Assertions.assertFalse(game.checkWallPosition(true, startingTile));
    }

    @Test
    void horizontalWallAboveVerticalIsAllowed() {
        GameBoard.Tile startingTile = gameBoard.getGameState()[1][4];
        GameBoard.Tile belowStartingTile = gameBoard.getGameState()[startingTile.getRow() + 1][startingTile.getColumn()];

        belowStartingTile.setRightLink(GameBoard.LinkState.WALL);

        Assertions.assertTrue(game.checkWallPosition(true, startingTile));
    }

    @Test
    void horizontalWallsOverlappingIsNotAllowedFirstCase() {
        GameBoard.Tile startingTile = gameBoard.getGameState()[5][4];

        startingTile.setLowerLink(GameBoard.LinkState.WALL);

        Assertions.assertFalse(game.checkWallPosition(true, startingTile));
    }

    @Test
    void horizontalWallsOverlappingIsNotAllowedSecondCase() { // merge into single parametric test
        GameBoard.Tile startingTile = gameBoard.getGameState()[5][4];
        GameBoard.Tile tileRightToStartingTile = gameBoard.getGameState()[startingTile.getRow()][startingTile.getColumn() + 1];

        tileRightToStartingTile.setLowerLink(GameBoard.LinkState.WALL);

        Assertions.assertFalse(game.checkWallPosition(true, startingTile));
    }

    @Test
    void horizontalWallsNearEachOtherIsAllowed() {
        GameBoard.Tile startingTile = gameBoard.getGameState()[1][2];
        GameBoard.Tile tileLeftToStartingTile = gameBoard.getGameState()[startingTile.getRow()][startingTile.getColumn() - 1];

        tileLeftToStartingTile.setLowerLink(GameBoard.LinkState.WALL);

        Assertions.assertTrue(game.checkWallPosition(true, startingTile));
    }

    @Test
    void verticalWallIn70IsNotAllowed() {
        GameBoard.Tile startingTile = gameBoard.getGameState()[7][0];

        Assertions.assertFalse(game.checkWallPosition(false, startingTile));
    }

    @Test
    void verticalWallIn00IsNotAllowed() {
        GameBoard.Tile startingTile = gameBoard.getGameState()[0][0];

        Assertions.assertFalse(game.checkWallPosition(false, startingTile));
    }

    @Test
    void verticalWallCrossingHorizontalWallIsNotAllowed() {
        GameBoard.Tile startingTile = gameBoard.getGameState()[6][4];
        GameBoard.Tile tileLeftToStartingTile = gameBoard.getGameState()[startingTile.getRow()][startingTile.getColumn() - 1];

        startingTile.setUpperLink(GameBoard.LinkState.WALL);
        tileLeftToStartingTile.setUpperLink(GameBoard.LinkState.WALL);

        Assertions.assertFalse(game.checkWallPosition(false, startingTile));
    }

    @Test
    void verticalWallRightToHorizontalWallIsAllowed() {
        GameBoard.Tile startingTile = gameBoard.getGameState()[6][5];
        GameBoard.Tile tileLeftToStartingTile = gameBoard.getGameState()[startingTile.getRow()][startingTile.getColumn() - 1];

        tileLeftToStartingTile.setUpperLink(GameBoard.LinkState.WALL);

        Assertions.assertTrue(game.checkWallPosition(false, startingTile));
    }

    @Test
    void verticalWallsOverlappingIsNotAllowedFirstCase() {
        GameBoard.Tile startingTile = gameBoard.getGameState()[5][4];

        startingTile.setLeftLink(GameBoard.LinkState.WALL);

        Assertions.assertFalse(game.checkWallPosition(false, startingTile));
    }

    @Test
    void verticalWallsOverlappingIsNotAllowedSecondCase() {
        GameBoard.Tile startingTile = gameBoard.getGameState()[6][3];
        GameBoard.Tile tileAboveStartingTile = gameBoard.getGameState()[startingTile.getRow() - 1][startingTile.getColumn()];

        tileAboveStartingTile.setLeftLink(GameBoard.LinkState.WALL);

        Assertions.assertFalse(game.checkWallPosition(false, startingTile));
    }

    @Test
    void verticalWallsNearEachOtherIsAllowed() {
        GameBoard.Tile startingTile = gameBoard.getGameState()[6][7];
        GameBoard.Tile tileBelowStartingTile = gameBoard.getGameState()[startingTile.getRow() + 1][startingTile.getColumn()];

        tileBelowStartingTile.setLowerLink(GameBoard.LinkState.WALL);

        Assertions.assertTrue(game.checkWallPosition(true, startingTile));
    }



}
