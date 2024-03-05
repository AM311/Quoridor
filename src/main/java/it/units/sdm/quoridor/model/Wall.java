package it.units.sdm.quoridor.model;

import it.units.sdm.quoridor.model.GameBoard.Tile;
import it.units.sdm.quoridor.utils.WallOrientation;

public record Wall(WallOrientation orientation, Tile startingTile) {
}
