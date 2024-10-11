package it.units.sdm.quoridor.model.builder;

import it.units.sdm.quoridor.exceptions.BuilderException;
import it.units.sdm.quoridor.model.AbstractGame;

public class BuilderDirector {
  AbstractQuoridorBuilder builder;

  public BuilderDirector(AbstractQuoridorBuilder builder) {
    this.builder = builder;
  }

  public void changeBuilder(AbstractQuoridorBuilder builder) {
    this.builder = builder;
  }

  public AbstractGame makeGame() throws BuilderException {
    try {
      return builder.setIGameBoard().setIPawnList().setActionManager().setPlaceWallActionController().setMovePawnActionController().buildGame();
    } catch (RuntimeException e) {
      throw new BuilderException("Exception while building Game: " + e.getMessage());
    }
  }
}
