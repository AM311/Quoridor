package it.units.sdm.quoridor.model;

import it.units.sdm.quoridor.utils.Direction;
import it.units.sdm.quoridor.utils.WallOrientation;

public class Player {
    private final String name;
    private int numberOfWalls;
    private final Pawn pawn;

    //todo aggiungere controlli su numero di muri

    public Player(String name, int numberOfWalls, Pawn pawn) {
        this.name = name;
        this.numberOfWalls = numberOfWalls;
        this.pawn = pawn;
    }

    public void placeWall(GameBoard gameBoard, WallOrientation orientation, GameBoard.Tile startingTile) {

    }


    public boolean checkWallPosition(GameBoard gameBoard, WallOrientation orientation, GameBoard.Tile startingTile) {
        if (orientation == WallOrientation.HORIZONTAL) {
            return checkHorizontalWallPosition(gameBoard, startingTile);
        }
        if (orientation == WallOrientation.VERTICAL) {
            return checkVerticalWallPosition(gameBoard, startingTile);
        }
        return true;
    }

    private boolean checkHorizontalWallPosition(GameBoard gameBoard, GameBoard.Tile startingTile) {
        if (startingTile.getRow() == gameBoard.getSideLength() - 1 || startingTile.getColumn() == gameBoard.getSideLength() - 1) {
            return false;
        }
        GameBoard.Tile tileBelowStartingTile = gameBoard.getGameState()[startingTile.getRow() + 1][startingTile.getColumn()];
        GameBoard.Tile tileRightToStartingTile = gameBoard.getGameState()[startingTile.getRow()][startingTile.getColumn() + 1];

        if (startingTile.getLink(Direction.RIGHT) == GameBoard.LinkState.WALL && tileBelowStartingTile.getLink(Direction.RIGHT) == GameBoard.LinkState.WALL) {
            return false;
        }
        if (startingTile.getLink(Direction.DOWN) == GameBoard.LinkState.WALL || tileRightToStartingTile.getLink(Direction.DOWN) == GameBoard.LinkState.WALL) {
            return false;
        }
        return true;
    }

    private boolean checkVerticalWallPosition(GameBoard gameBoard, GameBoard.Tile startingTile) {
        if (startingTile.getRow() == 0 || startingTile.getColumn() == 0) {
            return false;
        }

        GameBoard.Tile tileAboveStartingTile = gameBoard.getGameState()[startingTile.getRow() - 1][startingTile.getColumn()];
        GameBoard.Tile tileLeftToStartingTile = gameBoard.getGameState()[startingTile.getRow()][startingTile.getColumn() - 1];

        if (startingTile.getLink(Direction.UP) == GameBoard.LinkState.WALL && tileLeftToStartingTile.getLink(Direction.UP) == GameBoard.LinkState.WALL) {
            return false;
        }
        if (startingTile.getLink(Direction.LEFT) == GameBoard.LinkState.WALL || tileAboveStartingTile.getLink(Direction.LEFT) == GameBoard.LinkState.WALL) {
            return false;
        }
        return true;
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
