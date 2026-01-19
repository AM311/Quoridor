package it.units.sdm.quoridor.utils;

import it.units.sdm.quoridor.model.abstracts.AbstractTile;

import java.util.Collection;

public record TargetTiles(AbstractTile startingTile, Collection<AbstractTile> destinationTiles) {
}
