package it.units.sdm.quoridor.model.movemanagement.actions;

import it.units.sdm.quoridor.model.*;

public class PawnMover implements Action<AbstractTile> {
  @Override
  public void execute(AbstractGame game, AbstractTile target) {
      AbstractPawn playingPawn = game.getPlayingPawn();
      AbstractTile startingTile = playingPawn.getCurrentTile();
      playingPawn.move(target);
      target.setOccupiedBy(playingPawn);
      startingTile.setFree();
  }
}