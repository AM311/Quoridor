package testDoubles;

import it.units.sdm.quoridor.controller.parser.QuoridorParser;
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
  protected AbstractGame game;
  protected boolean isPawnMoved;
  protected boolean isWallPlaced;
  protected boolean isGameQuit;
  protected boolean isGameEnded;
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
  protected boolean endGameHasToBeHandled;
  protected boolean gameHasToActuallyRestart;
  protected boolean isGameActuallyRestarted;

  public StubStandardCLIQuoridorGameEngine(BufferedReader reader, QuoridorParser parser, AbstractQuoridorBuilder builder) {
    this.reader = reader;
    this.parser = parser;
    this.builder = builder;
  }

  protected void createGame() throws BuilderException {
    BuilderDirector builderDirector = new BuilderDirector(builder);
    this.game = builderDirector.makeGame();
  }

  public void runGame() throws BuilderException, InvalidParameterException {
    createGame();

    if(isGameRestarted && gameHasToActuallyRestart){
      isGameActuallyRestarted = true;
      return;
    }

    while (!game.isGameFinished()) {
      if (isLoopStoppedImmediately) {
        break;
      }

      if (pawn0HasToWin && loopCounter == 0) {
        Position destinationTilePosition = new Position(8, 5);
        AbstractTile destinationTile = game.getGameBoard().getTile(destinationTilePosition);
        game.getPlayingPawn().move(destinationTile);
      }
      if (pawn1HasToWin && loopCounter == 1) {
        Position destinationTilePosition = new Position(0, 5);
        AbstractTile destinationTile = game.getGameBoard().getTile(destinationTilePosition);
        game.getPlayingPawn().move(destinationTile);
      }

      executeRound();

      loopCounter++;
      if (isLoopStoppedAfterOneRound && loopCounter == 1) {
        break;
      }
      if (!game.isGameFinished()) {
        game.changeRound();
      }
    }
    if(endGameHasToBeHandled) {
      handleEndGame();
    }
  }

  protected void executeRound() throws BuilderException {
    isRoundCompleted = false;

    boolean commandExecuted;
    do {
      try {
        String command = askCommand();
        commandExecuted = performCommand(command);
        isCommandExecuted = commandExecuted;

        if (isHelpAsked || isGameRestarted || isGameQuit) {
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

  protected boolean performCommand(String command) throws ParserException, InvalidParameterException, InvalidActionException, BuilderException {
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
      case HELP -> {
        isHelpAsked = true;
        yield false;
      }
      case QUIT -> {
        quitGame();
        yield true;
      }
      case RESTART -> {
        restartGame();
        yield true;
      }
    };
  }

  protected void handleEndGame() throws InvalidParameterException {
    isGameEnded = true;
    try {
      String command = askCommand();
      parser.parse(command);

      switch (parser.getCommandType().orElseThrow()) {
        case QUIT -> handleQuitGame();
        case RESTART -> handleRestartGame();
      }
    } catch (IOException e) {
      System.err.println("Error reading input: " + e.getMessage());
      quitGame();
    } catch (ParserException | BuilderException e) {
      handleQuitGame();
    }
  }

  protected void handleRestartGame() throws BuilderException, InvalidParameterException {
    restartGame();
  }

  protected void restartGame() throws BuilderException, InvalidParameterException {
    isGameRestarted = true;
    runGame();
  }

  protected void handleQuitGame() {
    quitGame();
  }

  protected void quitGame(){
    isGameQuit = true;
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

  public AbstractGame getGame() {
    return game;
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

  public void setEndGameHasToBeHandled(boolean endGameHasToBeHandled) {
    this.endGameHasToBeHandled = endGameHasToBeHandled;
  }

  public void setGameHasToActuallyRestart(boolean gameHasToActuallyRestart) {
    this.gameHasToActuallyRestart = gameHasToActuallyRestart;
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

  public boolean isRoundCompleted() {
    return isRoundCompleted;
  }

  public boolean isGameActuallyRestarted(){
    return isGameActuallyRestarted;
  }
}

