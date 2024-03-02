package it.units.sdm.quoridor.model;


import it.units.sdm.quoridor.model.GameBoard.Tile;
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
    //todo check if a wall blocks completely the opponent

    public Player(String name, int numberOfWalls, Pawn pawn) {
        this.name = name;
        this.numberOfWalls = numberOfWalls;
        this.pawn = pawn;
    }

    public void placeWall(GameBoard gameBoard, WallOrientation orientation, Tile startingTile) {
        if (checkWallPosition(gameBoard, orientation, startingTile)) {
            setWallLinks(gameBoard, orientation, startingTile);
        }
    }

    private void setWallLinks(GameBoard gameBoard, WallOrientation orientation, Tile startingTile) {
        switch(orientation) {
			case HORIZONTAL -> setWallLinksForHorizontalWall(gameBoard, startingTile);
			case VERTICAL ->  setWallLinkForVerticalWall(gameBoard, startingTile);
		}
    }

    private void setWallLinksForHorizontalWall(GameBoard gameBoard, Tile startingTile) {
        Tile tileBelowStartingTile = gameBoard.getGameState()[startingTile.getRow() + 1][startingTile.getColumn()];
        Tile tileRightToStartingTile = gameBoard.getGameState()[startingTile.getRow()][startingTile.getColumn() + 1];
        Tile tileLowRightDiagToStartingTile = gameBoard.getGameState()[startingTile.getRow() + 1][startingTile.getColumn() + 1];

        startingTile.setLink(DOWN, WALL);
        tileRightToStartingTile.setLink(DOWN, WALL);
        tileBelowStartingTile.setLink(UP, WALL);
        tileLowRightDiagToStartingTile.setLink(UP, WALL);
    }

    private void setWallLinkForVerticalWall(GameBoard gameBoard, Tile startingTile) {
        Tile tileAboveStartingTile = gameBoard.getGameState()[startingTile.getRow() - 1][startingTile.getColumn()];
        Tile tileLeftToStartingTile = gameBoard.getGameState()[startingTile.getRow()][startingTile.getColumn() - 1];
        Tile tileUpLeftDiagToStartingTile = gameBoard.getGameState()[startingTile.getRow() - 1][startingTile.getColumn() - 1];

        startingTile.setLink(LEFT, WALL);
        tileAboveStartingTile.setLink(LEFT, WALL);
        tileLeftToStartingTile.setLink(RIGHT, WALL);
        tileUpLeftDiagToStartingTile.setLink(RIGHT, WALL);
    }


    public boolean checkWallPosition(GameBoard gameBoard, WallOrientation orientation, Tile startingTile) {
        return switch (orientation) {
			case HORIZONTAL -> checkHorizontalWallPosition(gameBoard, startingTile);
			case VERTICAL -> checkVerticalWallPosition(gameBoard, startingTile);
		};
    }

    private boolean checkHorizontalWallPosition(GameBoard gameBoard, Tile startingTile) {
        if (gameBoard.isInLastRow(startingTile) || gameBoard.isInLastColumn(startingTile)) {
            return false;
        }
        //todo make the following part more explicit: why are these tiles retrieved? Which checks are made?
        Tile tileBelowStartingTile = gameBoard.getGameState()[startingTile.getRow() + 1][startingTile.getColumn()];
        Tile tileRightToStartingTile = gameBoard.getGameState()[startingTile.getRow()][startingTile.getColumn() + 1];

        if (startingTile.getLink(RIGHT) == WALL && tileBelowStartingTile.getLink(RIGHT) == WALL) {
            return false;
        }
        if (startingTile.getLink(DOWN) == WALL || tileRightToStartingTile.getLink(DOWN) == WALL) {
            return false;
        }
        return true;
    }

    private boolean checkVerticalWallPosition(GameBoard gameBoard, Tile startingTile) {
        if (gameBoard.isInFirstRow(startingTile) || gameBoard.isInFirstColumn(startingTile)) {
            return false;
        }
        //todo make the following part more explicit: why are these tiles retrieved? Which checks are made?
        Tile tileAboveStartingTile = gameBoard.getGameState()[startingTile.getRow() - 1][startingTile.getColumn()];
        Tile tileLeftToStartingTile = gameBoard.getGameState()[startingTile.getRow()][startingTile.getColumn() - 1];

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
