package testDoubles;

import it.units.sdm.quoridor.cli.parser.QuoridorParser;
import it.units.sdm.quoridor.exceptions.BuilderException;
import it.units.sdm.quoridor.exceptions.InvalidActionException;
import it.units.sdm.quoridor.exceptions.InvalidParameterException;
import it.units.sdm.quoridor.exceptions.ParserException;
import it.units.sdm.quoridor.model.AbstractGame;
import it.units.sdm.quoridor.model.AbstractTile;
import it.units.sdm.quoridor.model.builder.AbstractQuoridorBuilder;
import it.units.sdm.quoridor.model.builder.BuilderDirector;
import it.units.sdm.quoridor.utils.Position;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Optional;

public class StubStandardCLIQuoridorGameEngine {

  protected final BufferedReader reader;
  protected final QuoridorParser parser;
  protected final AbstractQuoridorBuilder builder;
  protected boolean isPawnMoved;
  protected boolean isWallPlaced;
  protected boolean isGameQuit;
  protected boolean isGameEnded;
  protected AbstractGame currentGame;
  protected boolean isLoopStoppedAfterOneRound;
  protected int loopCounter = 0;
  protected boolean isInvalidParameterExceptionCaught;
  protected boolean isParserExceptionCaught;
  protected boolean isInvalidActionExceptionCaught;
  protected boolean pawn0HasToWin;
  protected boolean pawn1HasToWin;
  protected boolean isHelpAsked;
  protected boolean isCommandExecuted;
  protected boolean isRoundCompleted;
  protected boolean isLoopStoppedImmediately;
  protected boolean isGameRestarted;

  public StubStandardCLIQuoridorGameEngine(BufferedReader reader, QuoridorParser parser, AbstractQuoridorBuilder builder) {
    this.reader = reader;
    this.parser = parser;
    this.builder = builder;
  }

  public void runGame() throws InvalidParameterException, BuilderException {
    AbstractGame game = createGame();
    currentGame = game;

    while (!game.isGameFinished()) {
      if (isLoopStoppedImmediately) {
        break;
      }

      if (pawn0HasToWin && loopCounter == 0) {
        Position destinationTilePosition = new Position(8, 5);
        AbstractTile destinationTile = currentGame.getGameBoard().getTile(destinationTilePosition);
        currentGame.getPlayingPawn().move(destinationTile);
      }
      if (pawn1HasToWin && loopCounter == 1) {
        Position destinationTilePosition = new Position(0, 5);
        AbstractTile destinationTile = currentGame.getGameBoard().getTile(destinationTilePosition);
        currentGame.getPlayingPawn().move(destinationTile);
      }

      executeRound(game);

      loopCounter++;
      if (isLoopStoppedAfterOneRound && loopCounter == 1) {
        break;
      }

      if (!game.isGameFinished()) {
        game.changeRound();
      }

    }
    isGameEnded = true;
  }

  protected AbstractGame createGame() throws BuilderException {
    BuilderDirector builderDirector = new BuilderDirector(builder);
    return builderDirector.makeGame();
  }

  protected void executeRound(AbstractGame game) {
    isRoundCompleted = false;

    String command;
    boolean commandExecuted;
    do {
      try {
        command = askCommand();
        commandExecuted = performCommand(command, game);
        isCommandExecuted = commandExecuted;

        if (isHelpAsked) {
          break;
        }
      } catch (IOException e) {
        return;
      } catch (ParserException e) {
        isParserExceptionCaught = true;
        return;
      } catch (InvalidParameterException e) {
        isInvalidParameterExceptionCaught = true;
        return;
      } catch (InvalidActionException e) {
        isInvalidActionExceptionCaught = true;
        return;
      }
    } while (!commandExecuted);

    isRoundCompleted = true;
  }

  protected String askCommand() throws IOException {
    return String.valueOf(reader.readLine());
  }

  protected boolean performCommand(String command, AbstractGame game) throws ParserException, InvalidParameterException, InvalidActionException {
    parser.parse(command);
    Optional<Position> targetPosition = parser.getActionPosition();
    return switch (parser.getCommandType().orElseThrow()) {
      case MOVE -> {
        isPawnMoved = true;
        game.movePlayingPawn(targetPosition.orElse(null));
        yield true;
      }
      case WALL -> {
        isWallPlaced = true;
        game.placeWall(targetPosition.orElse(null), parser.getWallOrientation().orElse(null));
        yield true;
      }
      case QUIT -> {
        handleEndGame();
        yield true;
      }
      case HELP -> {
        isHelpAsked = true;
        yield false;
      }
      case RESTART -> {
        handleRestartGame();
        yield true;
      }
    };
  }

  protected void handleEndGame() {
    isGameQuit = true;
    isGameEnded = true;
  }

  protected void handleRestartGame() {
    isGameRestarted = true;
    isGameEnded = true;
  }

  public boolean isPawnMoved() {
    return isPawnMoved;
  }

  public boolean isWallPlaced() {
    return isWallPlaced;
  }

  public boolean isGameQuit() {
    return isGameQuit;
  }

  public AbstractGame getCurrentGame() {
    return currentGame;
  }

  public void setLoopStoppedAfterOneRound(boolean loopStoppedAfterOneRound) {
    isLoopStoppedAfterOneRound = loopStoppedAfterOneRound;
  }

  public boolean isInvalidParameterExceptionCaught() {
    return isInvalidParameterExceptionCaught;
  }

  public boolean isParserExceptionCaught() {
    return isParserExceptionCaught;
  }

  public boolean isInvalidActionExceptionCaught() {
    return isInvalidActionExceptionCaught;
  }

  public void setPawn0HasToWin(boolean pawn0HasToWin) {
    this.pawn0HasToWin = pawn0HasToWin;
  }

  public void setPawn1HasToWin(boolean pawn1HasToWin) {
    this.pawn1HasToWin = pawn1HasToWin;
  }

  public boolean isGameEnded() {
    return isGameEnded;
  }

  public boolean isHelpAsked() {
    return isHelpAsked;
  }

  public boolean isCommandExecuted() {
    return isCommandExecuted;
  }

  public boolean isGameRestarted() {
    return isGameRestarted;
  }

  public void setLoopStoppedImmediately(boolean loopStoppedImmediately) {
    isLoopStoppedImmediately = loopStoppedImmediately;
  }

  public boolean isRoundCompleted() {
    return isRoundCompleted;
  }
}

