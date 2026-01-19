package it.units.sdm.quoridor.model;

import it.units.sdm.quoridor.model.abstracts.AbstractTile;
import it.units.sdm.quoridor.utils.WallOrientation;

public record Wall(WallOrientation orientation, AbstractTile startingTile) {
}
