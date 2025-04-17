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
import java.io.Reader;
import java.util.Optional;

public class StubStandardCLIQuoridorGameEngine {

  private final BufferedReader reader;
  private final QuoridorParser parser;
  private final AbstractQuoridorBuilder builder;
  private boolean isPawnMoved;
  private boolean isWallPlaced;
  private boolean isGameQuit;
  private boolean isGameEnded;
  private AbstractGame currentGame;
  private boolean isLoopStoppedAfterOneRound;
  private boolean isLoopStoppedAfterTwoRounds;
  private int loopCounter;
  private boolean isInvalidParameterExceptionCaught;
  private boolean isParserExceptionCaught;
  private boolean isInvalidActionExceptionCaught;
  private boolean pawn0HasToWin;
  private boolean pawn1HasToWin;
  private boolean isHelpAsked;
  private boolean isCommandExecuted;

  public StubStandardCLIQuoridorGameEngine(BufferedReader reader, QuoridorParser parser, AbstractQuoridorBuilder builder) {
    this.reader = reader;
    this.parser = parser;
    this.builder = builder;
  }

  public void runGame() throws InvalidParameterException, BuilderException {
    AbstractGame game = createGame();
    currentGame = game;

    while (!game.isGameFinished()) {
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
      if (isLoopStoppedAfterTwoRounds && loopCounter == 1) {
        break;
      }
      executeRound(game);
      if (isLoopStoppedAfterOneRound) {
        break;
      }
      if (!game.isGameFinished()) {
        game.changeRound();
      }

      loopCounter++;
    }
    endGame();
  }

  private void endGame() {
    isGameEnded = true;
  }

  private AbstractGame createGame() throws BuilderException {
    BuilderDirector builderDirector = new BuilderDirector(builder);
    return builderDirector.makeGame();
  }

  private void executeRound(AbstractGame game) {
    performInputCommand(game);
  }

  private void performInputCommand(AbstractGame game) {
    String command;
    boolean commandExecuted;
    do {
      try {
        command = askCommand();
        commandExecuted = performCommand(command, game);
        isCommandExecuted = commandExecuted;
        if(isHelpAsked){
          break;
        }
      } catch (IOException e) {
        break;
      } catch (ParserException e) {
        isParserExceptionCaught = true;
        break;
      } catch (InvalidParameterException e) {
        isInvalidParameterExceptionCaught = true;
        break;
      } catch (InvalidActionException e) {
        isInvalidActionExceptionCaught = true;
        break;
      }
    } while (!commandExecuted);
  }

  private boolean performCommand(String command, AbstractGame game) throws ParserException, InvalidParameterException, InvalidActionException {
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
        isGameQuit = true;
        endGame();
        yield true;
      }
      case HELP -> {
        isHelpAsked = true;
        yield false;
      }
    };
  }

  private String askCommand() throws IOException {
    return String.valueOf(reader.readLine());
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

  public void setLoopStoppedAfterTwoRounds(boolean loopStoppedAfterTwoRounds) {
    isLoopStoppedAfterTwoRounds = loopStoppedAfterTwoRounds;
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
}

