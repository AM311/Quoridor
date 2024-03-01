package it.units.sdm.quoridor.model;


import it.units.sdm.quoridor.utils.WallOrientation;

import static it.units.sdm.quoridor.model.GameBoard.LinkState.WALL;
import static it.units.sdm.quoridor.utils.Direction.*;
import static it.units.sdm.quoridor.utils.WallOrientation.HORIZONTAL;
import static it.units.sdm.quoridor.utils.WallOrientation.VERTICAL;

public class Player {
    private final String name;
    private final Pawn pawn;
    private int numberOfWalls;

    //todo add checks on walls number
    //todo add wall number decrease
    //todo add behavior in placeWall if checkWallPosition returns false

    public Player(String name, int numberOfWalls, Pawn pawn) {
        this.name = name;
        this.numberOfWalls = numberOfWalls;
        this.pawn = pawn;
    }

    public void placeWall(GameBoard gameBoard, WallOrientation orientation, GameBoard.Tile startingTile) {
        if (checkWallPosition(gameBoard, orientation, startingTile)) {
            setWallLinks(gameBoard, orientation, startingTile);
        }
    }

    private void setWallLinks(GameBoard gameBoard, WallOrientation orientation, GameBoard.Tile startingTile) {
        if (orientation == HORIZONTAL) {
            setWallLinksForHorizontalWall(gameBoard, startingTile);
        }
        if (orientation == VERTICAL) {
            setWallLinkForVerticalWall(gameBoard, startingTile);
        }
    }

    private void setWallLinksForHorizontalWall(GameBoard gameBoard, GameBoard.Tile startingTile) {
        GameBoard.Tile tileBelowStartingTile = gameBoard.getGameState()[startingTile.getRow() + 1][startingTile.getColumn()];
        GameBoard.Tile tileRightToStartingTile = gameBoard.getGameState()[startingTile.getRow()][startingTile.getColumn() + 1];
        GameBoard.Tile tileLowRightDiagToStartingTile = gameBoard.getGameState()[startingTile.getRow() + 1][startingTile.getColumn() + 1];

        startingTile.setLink(DOWN, WALL);
        tileRightToStartingTile.setLink(DOWN, WALL);
        tileBelowStartingTile.setLink(UP, WALL);
        tileLowRightDiagToStartingTile.setLink(UP, WALL);
    }

    private void setWallLinkForVerticalWall(GameBoard gameBoard, GameBoard.Tile startingTile) {
        GameBoard.Tile tileAboveStartingTile = gameBoard.getGameState()[startingTile.getRow() - 1][startingTile.getColumn()];
        GameBoard.Tile tileLeftToStartingTile = gameBoard.getGameState()[startingTile.getRow()][startingTile.getColumn() - 1];
        GameBoard.Tile tileUpLeftDiagToStartingTile = gameBoard.getGameState()[startingTile.getRow() - 1][startingTile.getColumn() - 1];

        startingTile.setLink(LEFT, WALL);
        tileAboveStartingTile.setLink(LEFT, WALL);
        tileLeftToStartingTile.setLink(RIGHT, WALL);
        tileUpLeftDiagToStartingTile.setLink(RIGHT, WALL);
    }


    public boolean checkWallPosition(GameBoard gameBoard, WallOrientation orientation, GameBoard.Tile startingTile) {
        if (orientation == HORIZONTAL) {
            return checkHorizontalWallPosition(gameBoard, startingTile);
        }
        if (orientation == VERTICAL) {
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

        if (startingTile.getLink(RIGHT) == WALL && tileBelowStartingTile.getLink(RIGHT) == WALL) {
            return false;
        }
        if (startingTile.getLink(DOWN) == WALL || tileRightToStartingTile.getLink(DOWN) == WALL) {
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

        if (startingTile.getLink(UP) == WALL && tileLeftToStartingTile.getLink(UP) == WALL) {
            return false;
        }
        if (startingTile.getLink(LEFT) == WALL || tileAboveStartingTile.getLink(LEFT) == WALL) {
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
