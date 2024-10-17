import it.units.sdm.quoridor.exceptions.BuilderException;
import it.units.sdm.quoridor.exceptions.InvalidActionException;
import it.units.sdm.quoridor.exceptions.InvalidParameterException;
import it.units.sdm.quoridor.model.*;
import it.units.sdm.quoridor.model.builder.BuilderDirector;
import it.units.sdm.quoridor.model.builder.StdQuoridorBuilder;
import it.units.sdm.quoridor.utils.Position;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class MovePawnTest {

  private static AbstractGame mockGameBuilder() throws InvalidParameterException, BuilderException {
    BuilderDirector builderDirector = new BuilderDirector(new StdQuoridorBuilder(2));
    return builderDirector.makeGame();
  }

  @ParameterizedTest
  @CsvSource({"7, 1, 7, 2", "4, 3, 5, 3", "3, 1, 2, 1", "5, 6, 5, 5"})
  void movePawnVerticallyAndHorizontally(int startingRow, int startingColumn, int destinationRow, int destinationColumn) throws InvalidActionException, InvalidParameterException, BuilderException {

    AbstractGame game = mockGameBuilder();

    Position startingPosition = new Position(startingRow, startingColumn);
    Position destinationPosition = new Position(destinationRow, destinationColumn);

    AbstractTile startingTile = game.getGameBoard().getTile(startingPosition);
    AbstractTile destinationTile = game.getGameBoard().getTile(destinationPosition);

    game.getPlayingPawn().move(startingTile);
    game.movePlayingPawn(destinationPosition);

    Assertions.assertTrue(startingTile.isOccupiedBy().isEmpty()
            && destinationTile.isOccupiedBy().isPresent()
            && game.getPlayingPawn().getCurrentTile().equals(destinationTile));
  }


  @Test
  void movePawnDiagonalUpRight() throws InvalidActionException, InvalidParameterException, BuilderException {
    AbstractGame game = mockGameBuilder();

    Position startingPosition = new Position(1, 1);
    Position destinationPosition = new Position(0, 2);
    Position opponentPosition = new Position(0, 1);

    AbstractTile startingTile = game.getGameBoard().getTile(startingPosition);
    AbstractTile destinationTile = game.getGameBoard().getTile(destinationPosition);
    AbstractTile opponentTile = game.getGameBoard().getTile(opponentPosition);

    game.getPlayingPawn().move(startingTile);

    game.changeRound();
    game.getPlayingPawn().move(opponentTile);
    game.getGameBoard().getTile(opponentPosition).setOccupiedBy(game.getPlayingPawn());
    game.changeRound();

    game.movePlayingPawn(destinationPosition);

    Assertions.assertTrue(startingTile.isOccupiedBy().isEmpty()
            && destinationTile.isOccupiedBy().isPresent()
            && game.getPlayingPawn().getCurrentTile().equals(destinationTile));
  }


  @Test
  void movePawnDiagonalUpLeft() throws InvalidActionException, InvalidParameterException, BuilderException {
    AbstractGame game = mockGameBuilder();

    Position startingPosition = new Position(1, 1);
    Position destinationPosition = new Position(0, 0);
    Position opponentPosition = new Position(0, 1);

    AbstractTile startingTile = game.getGameBoard().getTile(startingPosition);
    AbstractTile destinationTile = game.getGameBoard().getTile(destinationPosition);

    game.getPlayingPawn().move(startingTile);

    game.changeRound();
    game.getPlayingPawn().move(game.getGameBoard().getTile(opponentPosition));
    game.getGameBoard().getTile(opponentPosition).setOccupiedBy(game.getPlayingPawn());
    game.changeRound();

    game.movePlayingPawn(destinationPosition);
    Assertions.assertTrue(startingTile.isOccupiedBy().isEmpty()
            && destinationTile.isOccupiedBy().isPresent()
            && game.getPlayingPawn().getCurrentTile().equals(destinationTile));
  }


  @Test
  void movePawnDiagonalDownRight() throws InvalidActionException, InvalidParameterException, BuilderException {
    AbstractGame game = mockGameBuilder();

    Position startingPosition = new Position(4, 7);
    Position destinationPosition = new Position(5, 8);
    Position opponentPosition = new Position(4, 8);

    AbstractTile startingTile = game.getGameBoard().getTile(startingPosition);
    AbstractTile destinationTile = game.getGameBoard().getTile(destinationPosition);

    game.getPlayingPawn().move(startingTile);

    game.changeRound();
    game.getPlayingPawn().move(game.getGameBoard().getTile(opponentPosition));
    game.getGameBoard().getTile(opponentPosition).setOccupiedBy(game.getPlayingPawn());
    game.changeRound();

    game.movePlayingPawn(destinationPosition);
    Assertions.assertTrue(startingTile.isOccupiedBy().isEmpty()
            && destinationTile.isOccupiedBy().isPresent()
            && game.getPlayingPawn().getCurrentTile().equals(destinationTile));
  }


  @Test
  void movePawnDiagonalDownLeft() throws InvalidActionException, InvalidParameterException, BuilderException {
    AbstractGame game = mockGameBuilder();

    Position startingPosition = new Position(4, 1);
    Position destinationPosition = new Position(5, 0);
    Position opponentPosition = new Position(4, 0);

    AbstractTile startingTile = game.getGameBoard().getTile(startingPosition);
    AbstractTile destinationTile = game.getGameBoard().getTile(destinationPosition);

    game.getPlayingPawn().move(startingTile);

    game.changeRound();
    game.getPlayingPawn().move(game.getGameBoard().getTile(opponentPosition));
    game.getGameBoard().getTile(opponentPosition).setOccupiedBy(game.getPlayingPawn());
    game.changeRound();

    game.movePlayingPawn(destinationPosition);
    Assertions.assertTrue(startingTile.isOccupiedBy().isEmpty()
            && destinationTile.isOccupiedBy().isPresent()
            && game.getPlayingPawn().getCurrentTile().equals(destinationTile));
  }

}