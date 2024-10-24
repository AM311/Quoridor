import it.units.sdm.quoridor.exceptions.BuilderException;
import it.units.sdm.quoridor.exceptions.InvalidParameterException;
import it.units.sdm.quoridor.model.*;
import it.units.sdm.quoridor.model.builder.BuilderDirector;
import it.units.sdm.quoridor.model.builder.StdQuoridorBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GameTest {
  private static AbstractGame buildGame() throws InvalidParameterException, BuilderException {
    BuilderDirector builderDirector = new BuilderDirector(new StdQuoridorBuilder(2));
    return builderDirector.makeGame();
  }
  @Test
  void changeRoundOnceTest() throws InvalidParameterException, BuilderException {
    AbstractGame game = buildGame();

    game.changeRound();
    Assertions.assertEquals(game.getPawns().getLast(), game.getPlayingPawn());
  }

  @Test
  void changeRoundTwiceTest() throws InvalidParameterException, BuilderException {
    AbstractGame game = buildGame();

    game.changeRound();
    game.changeRound();
    Assertions.assertEquals(game.getPawns().getFirst(), game.getPlayingPawn());
  }
}
