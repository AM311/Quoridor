package it.units.sdm.quoridor.movemanager;

import it.units.sdm.quoridor.exceptions.InvalidActionException;
import it.units.sdm.quoridor.exceptions.InvalidParameterException;
import it.units.sdm.quoridor.exceptions.NumberOfWallsBelowZeroException;
import it.units.sdm.quoridor.exceptions.OutOfGameBoardException;
import it.units.sdm.quoridor.model.GameBoard;
import it.units.sdm.quoridor.model.Pawn;
import it.units.sdm.quoridor.model.Wall;

import static it.units.sdm.quoridor.model.GameBoard.LinkState.WALL;
import static it.units.sdm.quoridor.utils.directions.StraightDirection.*;

public class WallPlacer implements Action<Wall> {
	@Override
	public void execute(GameBoard gameBoard, Pawn pawn, Wall target) throws InvalidActionException {
		pawn.decrementNumberOfWalls();
		setWallLinks(gameBoard, target);
	}

	private void setWallLinks(GameBoard gameBoard, Wall wall) throws InvalidActionException {
		switch (wall.orientation()) {
			case HORIZONTAL -> setWallLinksForHorizontalWall(gameBoard, wall.startingTile());
			case VERTICAL -> setWallLinkForVerticalWall(gameBoard, wall.startingTile());
		}
	}

	private void setWallLinksForHorizontalWall(GameBoard gameBoard, GameBoard.Tile startingTile) throws InvalidActionException {
		try {
			GameBoard.Tile tileBelowStartingTile = gameBoard.getAdjacentTile(startingTile, DOWN);
			GameBoard.Tile tileRightToStartingTile = gameBoard.getAdjacentTile(startingTile, RIGHT);
			GameBoard.Tile tileLowRightDiagToStartingTile = gameBoard.getAdjacentTile(gameBoard.getAdjacentTile(startingTile, RIGHT), DOWN);

			startingTile.setLink(DOWN, WALL);
			tileRightToStartingTile.setLink(DOWN, WALL);
			tileBelowStartingTile.setLink(UP, WALL);
			tileLowRightDiagToStartingTile.setLink(UP, WALL);
		} catch (OutOfGameBoardException e) {
			throw new InvalidActionException();
		}
	}

	private void setWallLinkForVerticalWall(GameBoard gameBoard, GameBoard.Tile startingTile) throws InvalidActionException {
		try {
			GameBoard.Tile tileAboveStartingTile = gameBoard.getAdjacentTile(startingTile, UP);
			GameBoard.Tile tileLeftToStartingTile = gameBoard.getAdjacentTile(startingTile, LEFT);
			GameBoard.Tile tileUpLeftDiagToStartingTile = gameBoard.getAdjacentTile(gameBoard.getAdjacentTile(startingTile, LEFT), UP);

			startingTile.setLink(LEFT, WALL);
			tileAboveStartingTile.setLink(LEFT, WALL);
			tileLeftToStartingTile.setLink(RIGHT, WALL);
			tileUpLeftDiagToStartingTile.setLink(RIGHT, WALL);
		} catch (OutOfGameBoardException e) {
			throw new InvalidActionException();
		}
	}
}
