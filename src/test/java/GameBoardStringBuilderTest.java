import it.units.sdm.quoridor.exceptions.BuilderException;
import it.units.sdm.quoridor.exceptions.InvalidParameterException;
import it.units.sdm.quoridor.model.abstracts.AbstractGame;
import it.units.sdm.quoridor.model.builder.BuilderDirector;
import it.units.sdm.quoridor.model.builder.StandardQuoridorBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GameBoardStringBuilderTest {
  private static AbstractGame buildGame() throws InvalidParameterException, BuilderException {
    BuilderDirector builderDirector = new BuilderDirector(new StandardQuoridorBuilder(2));
    return builderDirector.makeGame();
  }

  @Test
  void gameBoardStringIsNotNull() throws InvalidParameterException, BuilderException {
    AbstractGame game = buildGame();
    String gameBoardString = game.getGameBoard().toString();
    Assertions.assertNotNull(gameBoardString);
  }

  @Test
  void gameBoardStringIsNotBlank() throws InvalidParameterException, BuilderException {
    AbstractGame game = buildGame();
    String gameBoardString = game.getGameBoard().toString();
    Assertions.assertFalse(gameBoardString.isBlank());
  }
}
