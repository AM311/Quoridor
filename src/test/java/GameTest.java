import it.units.sdm.quoridor.exceptions.BuilderException;
import it.units.sdm.quoridor.exceptions.InvalidParameterException;
import it.units.sdm.quoridor.model.*;
import it.units.sdm.quoridor.model.builder.BuilderDirector;
import it.units.sdm.quoridor.model.builder.StdQuoridorBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GameTest {
  BuilderDirector builderDirector;
  @Test
  void changeRoundOnceTest() throws InvalidParameterException, BuilderException {
    builderDirector = new BuilderDirector(new StdQuoridorBuilder(2));
    AbstractGame game = builderDirector.makeGame();

    game.changeRound();
    Assertions.assertEquals(game.getPawns().getLast(), game.getPlayingPawn());
  }

  @Test
  void changeRoundTwiceTest() throws InvalidParameterException, BuilderException {
    builderDirector = new BuilderDirector(new StdQuoridorBuilder(2));
    AbstractGame game = builderDirector.makeGame();

    game.changeRound();
    game.changeRound();
    Assertions.assertEquals(game.getPawns().getFirst(), game.getPlayingPawn());
  }
}
