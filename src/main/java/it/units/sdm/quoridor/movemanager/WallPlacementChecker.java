package it.units.sdm.quoridor.movemanager;

import it.units.sdm.quoridor.exceptions.OutOfGameBoardException;
import it.units.sdm.quoridor.model.GameBoard;
import it.units.sdm.quoridor.model.Pawn;
import it.units.sdm.quoridor.model.Wall;

import static it.units.sdm.quoridor.model.GameBoard.LinkState.WALL;
import static it.units.sdm.quoridor.utils.Directions.Direction.*;

public class WallPlacementChecker implements ActionChecker<Wall> {
	public boolean checkAction(GameBoard gameBoard, Pawn playingPawn, Wall target) {
		return switch (target.orientation()) {
			case HORIZONTAL -> checkHorizontalWallPosition(gameBoard, target.startingTile());
			case VERTICAL -> checkVerticalWallPosition(gameBoard, target.startingTile());
		};
	}

	private boolean checkHorizontalWallPosition(GameBoard gameBoard, GameBoard.Tile startingTile)  {
		if (gameBoard.isInLastRow(startingTile) || gameBoard.isInLastColumn(startingTile)) {
			return false;
		}
		//todo extract method "checkCrossingWalls"?
		GameBoard.Tile tileBelowStartingTile = gameBoard.getAdjacentTile(startingTile, DOWN);
		GameBoard.Tile tileRightToStartingTile = gameBoard.getAdjacentTile(startingTile, RIGHT);

		if (startingTile.getLink(RIGHT) == WALL && tileBelowStartingTile.getLink(RIGHT) == WALL) {
			return false;
		}
		if (startingTile.getLink(DOWN) == WALL || tileRightToStartingTile.getLink(DOWN) == WALL) {
			return false;
		}
		return true;
	}

	private boolean checkVerticalWallPosition(GameBoard gameBoard, GameBoard.Tile startingTile) {
		if (gameBoard.isInFirstRow(startingTile) || gameBoard.isInFirstColumn(startingTile)) {
			return false;
		}
		//todo extract method "checkCrossingWalls"?
		GameBoard.Tile tileAboveStartingTile = gameBoard.getAdjacentTile(startingTile, UP);
		GameBoard.Tile tileLeftToStartingTile = gameBoard.getAdjacentTile(startingTile, LEFT);

		if (startingTile.getLink(UP) == WALL && tileLeftToStartingTile.getLink(UP) == WALL) {
			return false;
		}
		if (startingTile.getLink(LEFT) == WALL || tileAboveStartingTile.getLink(LEFT) == WALL) {
			return false;
		}
		return true;
	}

}
