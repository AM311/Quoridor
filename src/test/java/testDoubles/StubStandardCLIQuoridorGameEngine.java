package testDoubles;

import it.units.sdm.quoridor.cli.parser.QuoridorParser;
import it.units.sdm.quoridor.exceptions.BuilderException;
import it.units.sdm.quoridor.exceptions.InvalidActionException;
import it.units.sdm.quoridor.exceptions.InvalidParameterException;
import it.units.sdm.quoridor.exceptions.ParserException;
import it.units.sdm.quoridor.model.AbstractGame;
import it.units.sdm.quoridor.model.builder.AbstractQuoridorBuilder;
import it.units.sdm.quoridor.model.builder.BuilderDirector;
import it.units.sdm.quoridor.utils.Position;

import java.io.IOException;
import java.io.Reader;
import java.util.Optional;

public class StubStandardCLIQuoridorGameEngine {

  private final Reader reader;
  private final QuoridorParser parser;
  private final AbstractQuoridorBuilder builder;
  private boolean isPawnMoved;
  private boolean isWallPlaced;
  private boolean isGameQuit;
  private AbstractGame currentGame;
  private boolean isLoopStoppedImmediately;
  private boolean isLoopStoppedAfterOneTurn;
  private int loopCounter = 0;

  public StubStandardCLIQuoridorGameEngine(Reader reader, QuoridorParser parser, AbstractQuoridorBuilder builder) {
    this.reader = reader;
    this.parser = parser;
    this.builder = builder;
  }

  public void startGame() {
    AbstractGame game = createGame();
    currentGame = game;
    System.out.println(game);

    while (!game.isGameFinished()) {
      game.changeRound();
      if (isLoopStoppedAfterOneTurn && loopCounter == 1) {
        break;
      }
      executeRound(game);
      if(isLoopStoppedImmediately) {
        break;
      }
      loopCounter++;
    }
    endGame();
  }

  private void endGame(){
  }

  private AbstractGame createGame() {
    BuilderDirector builderDirector = new BuilderDirector(builder);
    try {
      return builderDirector.makeGame();
    } catch (BuilderException e) {
      throw new RuntimeException(e);
    }
  }

  private void executeRound(AbstractGame game) {
    String command = askCommand();
    performCommand(command, game);
  }

  private String askCommand() {
    String command;
    try {
      command = String.valueOf((char) reader.read());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return command;
  }

  private void performCommand(String command, AbstractGame game) {
    boolean commandExecuted = false;
    do {
      try {
        parser.acceptAndParse(command);
        Optional<Position> targetPosition = parser.getActionPosition();
        switch (parser.getCommandType().orElseThrow()) {
          case MOVE -> {
            isPawnMoved = true;
            game.movePlayingPawn(targetPosition.orElse(null));
          }
          case WALL -> {
            isWallPlaced = true;
            game.placeWall(targetPosition.orElse(null), parser.getWallOrientation().orElse(null));
          }
          case QUIT -> {
            isGameQuit = true;
            endGame();
          }
        }
        commandExecuted = true;

      } catch (ParserException | InvalidParameterException | InvalidActionException e) {
        break;
      }
    } while (!commandExecuted);
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

  public void setLoopStoppedImmediately(boolean loopStoppedImmediately) {
    isLoopStoppedImmediately = loopStoppedImmediately;
  }

  public void setLoopStoppedAfterOneTurn(boolean loopStoppedAfterOneTurn) {
    isLoopStoppedAfterOneTurn = loopStoppedAfterOneTurn;
  }
}

