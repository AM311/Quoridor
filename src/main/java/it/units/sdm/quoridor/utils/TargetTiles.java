package it.units.sdm.quoridor.utils;

import it.units.sdm.quoridor.model.AbstractTile;

import java.util.Collection;

public record TargetTiles(AbstractTile startingTile, Collection<AbstractTile> destinationTiles) {
}
