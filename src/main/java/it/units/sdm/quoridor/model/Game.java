package it.units.sdm.quoridor.model;

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

    public void placeWall(boolean orientation, GameBoard.Tile startingTile) {

    }


    public boolean checkWallPosition(boolean orientation, GameBoard.Tile startingTile) {
        if (orientation) {
            if (startingTile.getRow() == 8 || startingTile.getColumn() == 8) {
                return false;
            }

            GameBoard.Tile tileBelowStartingTile = gameBoard.getGameState()[startingTile.getRow() + 1][startingTile.getColumn()];
            GameBoard.Tile tileRightToStartingTile = gameBoard.getGameState()[startingTile.getRow()][startingTile.getColumn() + 1];

            if (startingTile.getRightLink() == GameBoard.LinkState.WALL && tileBelowStartingTile.getRightLink() == GameBoard.LinkState.WALL) {
                return false;
            }
            if (startingTile.getLowerLink() == GameBoard.LinkState.WALL || tileRightToStartingTile.getLowerLink() == GameBoard.LinkState.WALL) {
                return false;
            }
        } else {
            if (startingTile.getRow() == 0 || startingTile.getColumn() == 0) {
                return false;
            }

            GameBoard.Tile tileAboveStartingTile = gameBoard.getGameState()[startingTile.getRow() - 1][startingTile.getColumn()];
            GameBoard.Tile tileLeftToStartingTile = gameBoard.getGameState()[startingTile.getRow()][startingTile.getColumn() - 1];

            if (startingTile.getUpperLink() == GameBoard.LinkState.WALL && tileLeftToStartingTile.getUpperLink() == GameBoard.LinkState.WALL) {
                return false;
            }
            if (startingTile.getLeftLink() == GameBoard.LinkState.WALL || tileAboveStartingTile.getLeftLink() == GameBoard.LinkState.WALL) {
                return false;
            }
        }
        return true;
    }
}
