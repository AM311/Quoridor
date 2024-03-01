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

public class PlayerPlaceWallTest {
    GameBoard gameBoard = new GameBoard();
    GameBoard.Tile tile = gameBoard.getGameState()[0][4];
    Pawn pawn = new Pawn(tile, Color.black);
    Player player = new Player("Bob", 10, pawn);

    @ParameterizedTest
    @CsvSource({"3, 3", "6, 2", "0, 0"})
    void horizontalWallIsAllowed(int row, int column) {
        GameBoard.Tile startingTile = gameBoard.getGameState()[row][column];

        Assertions.assertTrue(player.checkWallPosition(gameBoard, WallOrientation.HORIZONTAL, startingTile));
    }

    @ParameterizedTest
    @CsvSource({"8, 0", "4, 8", "8, 8", "0, 8"})
    void horizontalWallIsNotAllowed(int row, int column) {
        GameBoard.Tile startingTile = gameBoard.getGameState()[row][column];

        Assertions.assertFalse(player.checkWallPosition(gameBoard, WallOrientation.HORIZONTAL, startingTile));
    }

    @ParameterizedTest
    @CsvSource({"0, 0", "3, 3", "6, 4"})
    void horizontalWallCrossingVerticalWallIsNotAllowed(int row, int column) {
        GameBoard.Tile startingTile = gameBoard.getGameState()[row][column];
        GameBoard.Tile belowStartingTile = gameBoard.getGameState()[startingTile.getRow() + 1][startingTile.getColumn()];

        startingTile.setLink(Direction.RIGHT, GameBoard.LinkState.WALL);
        belowStartingTile.setLink(Direction.RIGHT, GameBoard.LinkState.WALL);


        Assertions.assertFalse(player.checkWallPosition(gameBoard, WallOrientation.HORIZONTAL, startingTile));
    }

    @ParameterizedTest
    @CsvSource({"0, 0", "3, 4", "7, 2"})
    void horizontalWallAboveVerticalIsAllowed(int row, int column) {
        GameBoard.Tile startingTile = gameBoard.getGameState()[row][column];
        GameBoard.Tile belowStartingTile = gameBoard.getGameState()[startingTile.getRow() + 1][startingTile.getColumn()];

        belowStartingTile.setLink(Direction.RIGHT, GameBoard.LinkState.WALL);

        Assertions.assertTrue(player.checkWallPosition(gameBoard, WallOrientation.HORIZONTAL, startingTile));
    }

    @ParameterizedTest
    @CsvSource({"6, 1", "2, 5", "3, 2"})
    void horizontalWallsOverlappingIsNotAllowedFirstCase(int row, int column) {
        GameBoard.Tile startingTile = gameBoard.getGameState()[row][column];

        startingTile.setLink(Direction.DOWN, GameBoard.LinkState.WALL);

        Assertions.assertFalse(player.checkWallPosition(gameBoard, WallOrientation.HORIZONTAL, startingTile));
    }

    @ParameterizedTest
    @CsvSource({"2, 3", "1, 4", "4, 7"})
    void horizontalWallsOverlappingIsNotAllowedSecondCase(int row, int column) {
        GameBoard.Tile startingTile = gameBoard.getGameState()[row][column];
        GameBoard.Tile tileRightToStartingTile = gameBoard.getGameState()[startingTile.getRow()][startingTile.getColumn() + 1];

        tileRightToStartingTile.setLink(Direction.DOWN, GameBoard.LinkState.WALL);

        Assertions.assertFalse(player.checkWallPosition(gameBoard, WallOrientation.HORIZONTAL, startingTile));
    }

    @ParameterizedTest
    @CsvSource({"1, 6", "4, 4", "7, 7"})
    void horizontalWallsNearEachOtherIsAllowed(int row, int column) {
        GameBoard.Tile startingTile = gameBoard.getGameState()[row][column];
        GameBoard.Tile tileLeftToStartingTile = gameBoard.getGameState()[startingTile.getRow()][startingTile.getColumn() - 1];

        tileLeftToStartingTile.setLink(Direction.DOWN, GameBoard.LinkState.WALL);

        Assertions.assertTrue(player.checkWallPosition(gameBoard, WallOrientation.HORIZONTAL, startingTile));
    }

    @ParameterizedTest
    @CsvSource({"0, 0", "4, 0", "0, 7"})
    void verticalWallIsNotAllowed(int row, int column) {
        GameBoard.Tile startingTile = gameBoard.getGameState()[row][column];

        Assertions.assertFalse(player.checkWallPosition(gameBoard, WallOrientation.VERTICAL, startingTile));
    }

    @ParameterizedTest
    @CsvSource({"8, 8", "4, 4", "1, 8"})
    void verticalWallIsAllowed(int row, int column) {
        GameBoard.Tile startingTile = gameBoard.getGameState()[row][column];

        Assertions.assertTrue(player.checkWallPosition(gameBoard, WallOrientation.VERTICAL, startingTile));
    }

    @ParameterizedTest
    @CsvSource({"3, 4", "5, 7", "1, 7"})
    void verticalWallCrossingHorizontalWallIsNotAllowed(int row, int column) {
        GameBoard.Tile startingTile = gameBoard.getGameState()[row][column];
        GameBoard.Tile tileLeftToStartingTile = gameBoard.getGameState()[startingTile.getRow()][startingTile.getColumn() - 1];

        startingTile.setLink(Direction.UP, GameBoard.LinkState.WALL);
        tileLeftToStartingTile.setLink(Direction.UP, GameBoard.LinkState.WALL);

        Assertions.assertFalse(player.checkWallPosition(gameBoard, WallOrientation.VERTICAL, startingTile));
    }

    @ParameterizedTest
    @CsvSource({"3, 4", "3, 3", "2, 4"})
    void verticalWallRightToHorizontalWallIsAllowed(int row, int column) {
        GameBoard.Tile startingTile = gameBoard.getGameState()[row][column];
        GameBoard.Tile tileLeftToStartingTile = gameBoard.getGameState()[startingTile.getRow()][startingTile.getColumn() - 1];

        tileLeftToStartingTile.setLink(Direction.UP, GameBoard.LinkState.WALL);

        Assertions.assertTrue(player.checkWallPosition(gameBoard, WallOrientation.VERTICAL, startingTile));
    }

    @ParameterizedTest
    @CsvSource({"1, 4", "7, 2", "4, 6"})
    void verticalWallsOverlappingIsNotAllowedFirstCase(int row, int column) {
        GameBoard.Tile startingTile = gameBoard.getGameState()[row][column];

        startingTile.setLink(Direction.LEFT, GameBoard.LinkState.WALL);

        Assertions.assertFalse(player.checkWallPosition(gameBoard, WallOrientation.VERTICAL, startingTile));
    }

    @ParameterizedTest
    @CsvSource({"5, 4", "5, 1", "6, 3"})
    void verticalWallsOverlappingIsNotAllowedSecondCase(int row, int column) {
        GameBoard.Tile startingTile = gameBoard.getGameState()[row][column];
        GameBoard.Tile tileAboveStartingTile = gameBoard.getGameState()[startingTile.getRow() - 1][startingTile.getColumn()];

        tileAboveStartingTile.setLink(Direction.LEFT, GameBoard.LinkState.WALL);

        Assertions.assertFalse(player.checkWallPosition(gameBoard, WallOrientation.VERTICAL, startingTile));
    }

    @ParameterizedTest
    @CsvSource({"6, 2", "3, 5", "5, 1"})
    void verticalWallsNearEachOtherIsAllowed(int row, int column) {
        GameBoard.Tile startingTile = gameBoard.getGameState()[row][column];
        GameBoard.Tile tileBelowStartingTile = gameBoard.getGameState()[startingTile.getRow() + 1][startingTile.getColumn()];

        tileBelowStartingTile.setLink(Direction.DOWN, GameBoard.LinkState.WALL);

        Assertions.assertTrue(player.checkWallPosition(gameBoard, WallOrientation.VERTICAL, startingTile));
    }


}

