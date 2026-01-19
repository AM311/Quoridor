import it.units.sdm.quoridor.exceptions.BuilderException;
import it.units.sdm.quoridor.exceptions.InvalidParameterException;
import it.units.sdm.quoridor.model.abstracts.AbstractGame;
import it.units.sdm.quoridor.model.builder.BuilderDirector;
import it.units.sdm.quoridor.model.builder.StandardQuoridorBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GameStringBuilderTest {
  private static AbstractGame buildGame() throws InvalidParameterException, BuilderException {
    BuilderDirector builderDirector = new BuilderDirector(new StandardQuoridorBuilder(2));
    return builderDirector.makeGame();
  }

  @Test
  void createGameStringIsNotNull() throws InvalidParameterException, BuilderException {
    AbstractGame game = buildGame();
    String gameString = game.toString();
    Assertions.assertNotNull(gameString);
    assertNotNull(gameString);
  }

  @Test
  void createGameStringIsNotEmpty() throws InvalidParameterException, BuilderException {
    AbstractGame game = buildGame();
    String gameString = game.toString();
    assertFalse(gameString.isBlank());
  }
}