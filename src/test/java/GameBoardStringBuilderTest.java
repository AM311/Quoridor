import it.units.sdm.quoridor.exceptions.BuilderException;
import it.units.sdm.quoridor.exceptions.InvalidParameterException;
import it.units.sdm.quoridor.model.AbstractGame;
import it.units.sdm.quoridor.model.AbstractGameBoard;
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
    AbstractGameBoard gameBoard = game.getGameBoard();
    String gameBoardString = gameBoard.toString();
    Assertions.assertNotNull(gameBoardString);
  }

  @Test
  void gameBoardStringIsNotEmpty() throws InvalidParameterException, BuilderException {
    AbstractGame game = buildGame();
    AbstractGameBoard gameBoard = game.getGameBoard();
    String gameBoardString = gameBoard.toString();
    Assertions.assertFalse(gameBoardString.isEmpty());
  }
}
