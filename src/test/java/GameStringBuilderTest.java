import it.units.sdm.quoridor.exceptions.BuilderException;
import it.units.sdm.quoridor.exceptions.InvalidParameterException;
import it.units.sdm.quoridor.model.AbstractGame;
import it.units.sdm.quoridor.model.builder.BuilderDirector;
import it.units.sdm.quoridor.model.builder.StandardQuoridorBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GameStringBuilderTest {
  private static AbstractGame buildGame(int nOfPlayers) throws InvalidParameterException, BuilderException {
    BuilderDirector builderDirector = new BuilderDirector(new StandardQuoridorBuilder(nOfPlayers));
    return builderDirector.makeGame();
  }

    @Test
    void createGameStringIsNotNull () throws InvalidParameterException, BuilderException {
      AbstractGame game = buildGame(2);
      String gameString = game.toString();
      Assertions.assertNotNull(gameString);
      assertNotNull(gameString, "La stringa non deve essere null");
    }


    @Test
    void createGameStringIsNotEmpty () throws InvalidParameterException, BuilderException {
      AbstractGame game = buildGame(2);
      String gameString = game.toString();
      assertFalse(gameString.isBlank(), "La stringa non deve essere vuota");
    }
  }


