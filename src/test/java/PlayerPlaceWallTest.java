import it.units.sdm.quoridor.model.GameBoard;
import it.units.sdm.quoridor.model.Pawn;
import it.units.sdm.quoridor.model.Player;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.awt.*;

import static it.units.sdm.quoridor.model.GameBoard.LinkState.*;
import static it.units.sdm.quoridor.utils.Direction.*;
import static it.units.sdm.quoridor.utils.WallOrientation.HORIZONTAL;
import static it.units.sdm.quoridor.utils.WallOrientation.VERTICAL;

public class PlayerPlaceWallTest {
    GameBoard gameBoard = new GameBoard();
    GameBoard.Tile tile = gameBoard.getGameState()[0][4];
    Pawn pawn = new Pawn(tile, Color.black);
    Player player = new Player("Bob", 10, pawn);


    @ParameterizedTest
    @CsvSource({"5, 2", "4, 3", "3, 1", "5, 6"})
    void wallOnLowerLinkAfterHorizontalWallPlacement_startingTile_innerTiles(int row, int column) {
        GameBoard.Tile startingTile = gameBoard.getGameState()[row][column];
        player.placeWall(gameBoard, HORIZONTAL, startingTile);

        GameBoard.LinkState[] expected = new GameBoard.LinkState[]{FREE, FREE, FREE, WALL};
        GameBoard.LinkState[] actual = new GameBoard.LinkState[]
                {
                        gameBoard.getGameState()[row][column].getLink(LEFT),
                        gameBoard.getGameState()[row][column].getLink(RIGHT),
                        gameBoard.getGameState()[row][column].getLink(UP),
                        gameBoard.getGameState()[row][column].getLink(DOWN)
                };

        Assertions.assertArrayEquals(expected, actual);
    }

    @ParameterizedTest
    @CsvSource({"5, 2", "4, 3", "3, 1", "5, 6"})
    void wallOnUpperLinkAfterHorizontalWallPlacement_tileBelowStartingTile_innerTiles(int row, int column) {
        GameBoard.Tile startingTile = gameBoard.getGameState()[row][column];
        player.placeWall(gameBoard, HORIZONTAL, startingTile);

        GameBoard.LinkState[] expected = new GameBoard.LinkState[]{FREE, FREE, WALL, FREE};
        GameBoard.LinkState[] actual = new GameBoard.LinkState[]
                {
                        gameBoard.getGameState()[row + 1][column].getLink(LEFT),
                        gameBoard.getGameState()[row + 1][column].getLink(RIGHT),
                        gameBoard.getGameState()[row + 1][column].getLink(UP),
                        gameBoard.getGameState()[row + 1][column].getLink(DOWN)
                };

        Assertions.assertArrayEquals(expected, actual);
    }

    @ParameterizedTest
    @CsvSource({"5, 2", "4, 3", "3, 1", "5, 6"})
    void wallOnLowerLinkAfterHorizontalWallPlacement_tileRightToStartingTile_innerTiles(int row, int column) {
        GameBoard.Tile startingTile = gameBoard.getGameState()[row][column];
        player.placeWall(gameBoard, HORIZONTAL, startingTile);

        GameBoard.LinkState[] expected = new GameBoard.LinkState[]{FREE, FREE, FREE, WALL};
        GameBoard.LinkState[] actual = new GameBoard.LinkState[]
                {
                        gameBoard.getGameState()[row][column + 1].getLink(LEFT),
                        gameBoard.getGameState()[row][column + 1].getLink(RIGHT),
                        gameBoard.getGameState()[row][column + 1].getLink(UP),
                        gameBoard.getGameState()[row][column + 1].getLink(DOWN)
                };

        Assertions.assertArrayEquals(expected, actual);
    }

    @ParameterizedTest
    @CsvSource({"5, 2", "4, 3", "3, 1", "5, 6"})
    void wallOnUpperLinkAfterHorizontalWallPlacement_tileLowRightDiagToStartingTile_innerTiles(int row, int column) {
        GameBoard.Tile startingTile = gameBoard.getGameState()[row][column];
        player.placeWall(gameBoard, HORIZONTAL, startingTile);

        GameBoard.LinkState[] expected = new GameBoard.LinkState[]{FREE, FREE, WALL, FREE};
        GameBoard.LinkState[] actual = new GameBoard.LinkState[]
                {
                        gameBoard.getGameState()[row + 1][column + 1].getLink(LEFT),
                        gameBoard.getGameState()[row + 1][column + 1].getLink(RIGHT),
                        gameBoard.getGameState()[row + 1][column + 1].getLink(UP),
                        gameBoard.getGameState()[row + 1][column + 1].getLink(DOWN)
                };

        Assertions.assertArrayEquals(expected, actual);
    }

    @Test
    void wallOnLowerLinkAfterHorizontalWallPlacement_startingTile_upperLeftCorner() {
        GameBoard.Tile startingTile = gameBoard.getGameState()[0][0];
        player.placeWall(gameBoard, HORIZONTAL, startingTile);

        GameBoard.LinkState[] expected = new GameBoard.LinkState[]{EDGE, FREE, EDGE, WALL};
        GameBoard.LinkState[] actual = new GameBoard.LinkState[]
                {
                        gameBoard.getGameState()[0][0].getLink(LEFT),
                        gameBoard.getGameState()[0][0].getLink(RIGHT),
                        gameBoard.getGameState()[0][0].getLink(UP),
                        gameBoard.getGameState()[0][0].getLink(DOWN)
                };

        Assertions.assertArrayEquals(expected, actual);
    }

    @ParameterizedTest
    @CsvSource({"4, 3", "3, 3", "5, 2", "5, 5"})
    void wallOnLeftLinkAfterVerticalWallPlacement_startingTile_innerTiles(int row, int column) {
        GameBoard.Tile startingTile = gameBoard.getGameState()[row][column];
        player.placeWall(gameBoard, VERTICAL, startingTile);

        GameBoard.LinkState[] expected = new GameBoard.LinkState[]{WALL, FREE, FREE, FREE};
        GameBoard.LinkState[] actual = new GameBoard.LinkState[]
                {
                        gameBoard.getGameState()[row][column].getLink(LEFT),
                        gameBoard.getGameState()[row][column].getLink(RIGHT),
                        gameBoard.getGameState()[row][column].getLink(UP),
                        gameBoard.getGameState()[row][column].getLink(DOWN)
                };

        Assertions.assertArrayEquals(expected, actual);
    }

    @ParameterizedTest
    @CsvSource({"4, 3", "3, 3", "5, 2", "5, 5"})
    void wallOnLeftLinkAfterVerticalWallPlacement_tileAboveStartingTile_innerTiles(int row, int column) {
        GameBoard.Tile startingTile = gameBoard.getGameState()[row][column];
        player.placeWall(gameBoard, VERTICAL, startingTile);

        GameBoard.LinkState[] expected = new GameBoard.LinkState[]{WALL, FREE, FREE, FREE};
        GameBoard.LinkState[] actual = new GameBoard.LinkState[]
                {
                        gameBoard.getGameState()[row - 1][column].getLink(LEFT),
                        gameBoard.getGameState()[row - 1][column].getLink(RIGHT),
                        gameBoard.getGameState()[row - 1][column].getLink(UP),
                        gameBoard.getGameState()[row - 1][column].getLink(DOWN)
                };

        Assertions.assertArrayEquals(expected, actual);
    }

    @ParameterizedTest
    @CsvSource({"4, 3", "3, 3", "5, 2", "5, 5"})
    void wallOnRightLinkAfterVerticalWallPlacement_tileLeftToStartingTile_innerTiles(int row, int column) {
        GameBoard.Tile startingTile = gameBoard.getGameState()[row][column];
        player.placeWall(gameBoard, VERTICAL, startingTile);

        GameBoard.LinkState[] expected = new GameBoard.LinkState[]{FREE, WALL, FREE, FREE};
        GameBoard.LinkState[] actual = new GameBoard.LinkState[]
                {
                        gameBoard.getGameState()[row][column - 1].getLink(LEFT),
                        gameBoard.getGameState()[row][column - 1].getLink(RIGHT),
                        gameBoard.getGameState()[row][column - 1].getLink(UP),
                        gameBoard.getGameState()[row][column - 1].getLink(DOWN)
                };

        Assertions.assertArrayEquals(expected, actual);
    }

    @ParameterizedTest
    @CsvSource({"4, 3", "3, 3", "5, 2", "5, 5"})
    void wallOnRightLinkAfterVerticalWallPlacement_tileUpLeftDiagToStartingTile_innerTiles(int row, int column) {
        GameBoard.Tile startingTile = gameBoard.getGameState()[row][column];
        player.placeWall(gameBoard, VERTICAL, startingTile);

        GameBoard.LinkState[] expected = new GameBoard.LinkState[]{FREE, WALL, FREE, FREE};
        GameBoard.LinkState[] actual = new GameBoard.LinkState[]
                {
                        gameBoard.getGameState()[row - 1][column - 1].getLink(LEFT),
                        gameBoard.getGameState()[row - 1][column - 1].getLink(RIGHT),
                        gameBoard.getGameState()[row - 1][column - 1].getLink(UP),
                        gameBoard.getGameState()[row - 1][column - 1].getLink(DOWN)
                };

        Assertions.assertArrayEquals(expected, actual);
    }

    @Test
    void wallOnLeftLinkAfterVerticalWallPlacement_startingTile_lowerRightCorner() {
        GameBoard.Tile startingTile = gameBoard.getGameState()[gameBoard.getSideLength() - 1][gameBoard.getSideLength() - 1];
        player.placeWall(gameBoard, VERTICAL, startingTile);

        GameBoard.LinkState[] expected = new GameBoard.LinkState[]{WALL, EDGE, FREE, EDGE};
        GameBoard.LinkState[] actual = new GameBoard.LinkState[]
                {
                        gameBoard.getGameState()[gameBoard.getSideLength() - 1][gameBoard.getSideLength() - 1].getLink(LEFT),
                        gameBoard.getGameState()[gameBoard.getSideLength() - 1][gameBoard.getSideLength() - 1].getLink(RIGHT),
                        gameBoard.getGameState()[gameBoard.getSideLength() - 1][gameBoard.getSideLength() - 1].getLink(UP),
                        gameBoard.getGameState()[gameBoard.getSideLength() - 1][gameBoard.getSideLength() - 1].getLink(DOWN)
                };

        Assertions.assertArrayEquals(expected, actual);
    }

    @ParameterizedTest
    @CsvSource({"3, 3", "6, 2", "0, 0"})
    void horizontalWallIsAllowed(int row, int column) {
        GameBoard.Tile startingTile = gameBoard.getGameState()[row][column];

        Assertions.assertTrue(player.checkWallPosition(gameBoard, HORIZONTAL, startingTile));
    }

    @ParameterizedTest
    @CsvSource({"8, 0", "4, 8", "8, 8", "0, 8"})
    void horizontalWallIsNotAllowed(int row, int column) {
        GameBoard.Tile startingTile = gameBoard.getGameState()[row][column];

        Assertions.assertFalse(player.checkWallPosition(gameBoard, HORIZONTAL, startingTile));
    }

    @ParameterizedTest
    @CsvSource({"0, 0", "3, 3", "6, 4"})
    void horizontalWallCrossingVerticalWallIsNotAllowed(int row, int column) {
        GameBoard.Tile startingTile = gameBoard.getGameState()[row][column];
        GameBoard.Tile belowStartingTile = gameBoard.getGameState()[startingTile.getRow() + 1][startingTile.getColumn()];

        startingTile.setLink(RIGHT, WALL);
        belowStartingTile.setLink(RIGHT, WALL);


        Assertions.assertFalse(player.checkWallPosition(gameBoard, HORIZONTAL, startingTile));
    }

    @ParameterizedTest
    @CsvSource({"0, 0", "3, 4", "7, 2"})
    void horizontalWallAboveVerticalIsAllowed(int row, int column) {
        GameBoard.Tile startingTile = gameBoard.getGameState()[row][column];
        GameBoard.Tile belowStartingTile = gameBoard.getGameState()[startingTile.getRow() + 1][startingTile.getColumn()];

        belowStartingTile.setLink(RIGHT, WALL);

        Assertions.assertTrue(player.checkWallPosition(gameBoard, HORIZONTAL, startingTile));
    }

    @ParameterizedTest
    @CsvSource({"6, 1", "2, 5", "3, 2"})
    void horizontalWallsOverlappingIsNotAllowedFirstCase(int row, int column) {
        GameBoard.Tile startingTile = gameBoard.getGameState()[row][column];

        startingTile.setLink(DOWN, WALL);

        Assertions.assertFalse(player.checkWallPosition(gameBoard, HORIZONTAL, startingTile));
    }

    @ParameterizedTest
    @CsvSource({"2, 3", "1, 4", "4, 7"})
    void horizontalWallsOverlappingIsNotAllowedSecondCase(int row, int column) {
        GameBoard.Tile startingTile = gameBoard.getGameState()[row][column];
        GameBoard.Tile tileRightToStartingTile = gameBoard.getGameState()[startingTile.getRow()][startingTile.getColumn() + 1];

        tileRightToStartingTile.setLink(DOWN, WALL);

        Assertions.assertFalse(player.checkWallPosition(gameBoard, HORIZONTAL, startingTile));
    }

    @ParameterizedTest
    @CsvSource({"1, 6", "4, 4", "7, 7"})
    void horizontalWallsNearEachOtherIsAllowed(int row, int column) {
        GameBoard.Tile startingTile = gameBoard.getGameState()[row][column];
        GameBoard.Tile tileLeftToStartingTile = gameBoard.getGameState()[startingTile.getRow()][startingTile.getColumn() - 1];

        tileLeftToStartingTile.setLink(DOWN, WALL);

        Assertions.assertTrue(player.checkWallPosition(gameBoard, HORIZONTAL, startingTile));
    }

    @ParameterizedTest
    @CsvSource({"0, 0", "4, 0", "0, 7"})
    void verticalWallIsNotAllowed(int row, int column) {
        GameBoard.Tile startingTile = gameBoard.getGameState()[row][column];

        Assertions.assertFalse(player.checkWallPosition(gameBoard, VERTICAL, startingTile));
    }

    @ParameterizedTest
    @CsvSource({"8, 8", "4, 4", "1, 8"})
    void verticalWallIsAllowed(int row, int column) {
        GameBoard.Tile startingTile = gameBoard.getGameState()[row][column];

        Assertions.assertTrue(player.checkWallPosition(gameBoard, VERTICAL, startingTile));
    }

    @ParameterizedTest
    @CsvSource({"3, 4", "5, 7", "1, 7"})
    void verticalWallCrossingHorizontalWallIsNotAllowed(int row, int column) {
        GameBoard.Tile startingTile = gameBoard.getGameState()[row][column];
        GameBoard.Tile tileLeftToStartingTile = gameBoard.getGameState()[startingTile.getRow()][startingTile.getColumn() - 1];

        startingTile.setLink(UP, WALL);
        tileLeftToStartingTile.setLink(UP, WALL);

        Assertions.assertFalse(player.checkWallPosition(gameBoard, VERTICAL, startingTile));
    }

    @ParameterizedTest
    @CsvSource({"3, 4", "3, 3", "2, 4"})
    void verticalWallRightToHorizontalWallIsAllowed(int row, int column) {
        GameBoard.Tile startingTile = gameBoard.getGameState()[row][column];
        GameBoard.Tile tileLeftToStartingTile = gameBoard.getGameState()[startingTile.getRow()][startingTile.getColumn() - 1];

        tileLeftToStartingTile.setLink(UP, WALL);

        Assertions.assertTrue(player.checkWallPosition(gameBoard, VERTICAL, startingTile));
    }

    @ParameterizedTest
    @CsvSource({"1, 4", "7, 2", "4, 6"})
    void verticalWallsOverlappingIsNotAllowedFirstCase(int row, int column) {
        GameBoard.Tile startingTile = gameBoard.getGameState()[row][column];

        startingTile.setLink(LEFT, WALL);

        Assertions.assertFalse(player.checkWallPosition(gameBoard, VERTICAL, startingTile));
    }

    @ParameterizedTest
    @CsvSource({"5, 4", "5, 1", "6, 3"})
    void verticalWallsOverlappingIsNotAllowedSecondCase(int row, int column) {
        GameBoard.Tile startingTile = gameBoard.getGameState()[row][column];
        GameBoard.Tile tileAboveStartingTile = gameBoard.getGameState()[startingTile.getRow() - 1][startingTile.getColumn()];

        tileAboveStartingTile.setLink(LEFT, WALL);

        Assertions.assertFalse(player.checkWallPosition(gameBoard, VERTICAL, startingTile));
    }

    @ParameterizedTest
    @CsvSource({"6, 2", "3, 5", "5, 1"})
    void verticalWallsNearEachOtherIsAllowed(int row, int column) {
        GameBoard.Tile startingTile = gameBoard.getGameState()[row][column];
        GameBoard.Tile tileBelowStartingTile = gameBoard.getGameState()[startingTile.getRow() + 1][startingTile.getColumn()];

        tileBelowStartingTile.setLink(DOWN, WALL);

        Assertions.assertTrue(player.checkWallPosition(gameBoard, VERTICAL, startingTile));
    }


}

