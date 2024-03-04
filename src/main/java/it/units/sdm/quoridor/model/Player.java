package it.units.sdm.quoridor.model;

import it.units.sdm.quoridor.model.GameBoard.Tile;
import it.units.sdm.quoridor.utils.WallOrientation;

import static it.units.sdm.quoridor.model.GameBoard.LinkState.WALL;
import static it.units.sdm.quoridor.utils.Direction.*;

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

	public boolean checkWallPosition(GameBoard gameBoard, WallOrientation orientation, Tile startingTile) {
		return switch (orientation) {
			case HORIZONTAL -> checkHorizontalWallPosition(gameBoard, startingTile);
			case VERTICAL -> checkVerticalWallPosition(gameBoard, startingTile);
		};
	}

	private void setWallLinks(GameBoard gameBoard, WallOrientation orientation, Tile startingTile) {
		switch (orientation) {
			case HORIZONTAL -> setWallLinksForHorizontalWall(gameBoard, startingTile);
			case VERTICAL -> setWallLinkForVerticalWall(gameBoard, startingTile);
		}
	}

	private boolean checkHorizontalWallPosition(GameBoard gameBoard, Tile startingTile) {
		if (gameBoard.isInLastRow(startingTile) || gameBoard.isInLastColumn(startingTile)) {
			return false;
		}
		//todo extract method "checkCrossingWalls"?
		Tile tileBelowStartingTile = gameBoard.getLowerTile(startingTile);
		Tile tileRightToStartingTile = gameBoard.getRightTile(startingTile);

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
		//todo extract method "checkCrossingWalls"?
		Tile tileAboveStartingTile = gameBoard.getUpperTile(startingTile);
		Tile tileLeftToStartingTile = gameBoard.getLeftTile(startingTile);

		if (startingTile.getLink(UP) == WALL && tileLeftToStartingTile.getLink(UP) == WALL) {
			return false;
		}
		if (startingTile.getLink(LEFT) == WALL || tileAboveStartingTile.getLink(LEFT) == WALL) {
			return false;
		}
		return true;
	}

	private void setWallLinksForHorizontalWall(GameBoard gameBoard, Tile startingTile) {
		Tile tileBelowStartingTile = gameBoard.getLowerTile(startingTile);
		Tile tileRightToStartingTile = gameBoard.getRightTile(startingTile);
		Tile tileLowRightDiagToStartingTile = gameBoard.getLowerTile(gameBoard.getRightTile(startingTile));

		startingTile.setLink(DOWN, WALL);
		tileRightToStartingTile.setLink(DOWN, WALL);
		tileBelowStartingTile.setLink(UP, WALL);
		tileLowRightDiagToStartingTile.setLink(UP, WALL);
	}

	private void setWallLinkForVerticalWall(GameBoard gameBoard, Tile startingTile) {
		Tile tileAboveStartingTile = gameBoard.getUpperTile(startingTile);
		Tile tileLeftToStartingTile = gameBoard.getLeftTile(startingTile);
		Tile tileUpLeftDiagToStartingTile = gameBoard.getUpperTile(gameBoard.getLeftTile(startingTile));

		startingTile.setLink(LEFT, WALL);
		tileAboveStartingTile.setLink(LEFT, WALL);
		tileLeftToStartingTile.setLink(RIGHT, WALL);
		tileUpLeftDiagToStartingTile.setLink(RIGHT, WALL);
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
