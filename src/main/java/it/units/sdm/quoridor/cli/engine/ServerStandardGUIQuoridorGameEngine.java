package it.units.sdm.quoridor.cli.engine;

import it.units.sdm.quoridor.GUI.view.GameView;
import it.units.sdm.quoridor.cli.StatisticsCounter;
import it.units.sdm.quoridor.cli.parser.QuoridorParser;
import it.units.sdm.quoridor.exceptions.BuilderException;
import it.units.sdm.quoridor.exceptions.InvalidActionException;
import it.units.sdm.quoridor.exceptions.InvalidParameterException;
import it.units.sdm.quoridor.exceptions.ParserException;
import it.units.sdm.quoridor.model.builder.AbstractQuoridorBuilder;
import it.units.sdm.quoridor.model.builder.StdQuoridorBuilder;
import it.units.sdm.quoridor.server.Logger;
import it.units.sdm.quoridor.utils.Position;
import it.units.sdm.quoridor.utils.WallOrientation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

public class ServerStandardGUIQuoridorGameEngine extends StandardGUIQuoridorGameEngine {
  private final BufferedReader socketReader;
  private final BufferedWriter socketWriter;
  private final QuoridorParser parser;
  private final GameView gameView;

  public ServerStandardGUIQuoridorGameEngine(AbstractQuoridorBuilder quoridorBuilder, StatisticsCounter statisticsCounter, BufferedReader socketReader, BufferedWriter socketWriter, QuoridorParser parser) {
    super(quoridorBuilder, statisticsCounter);
    this.socketReader = socketReader;
    this.socketWriter = socketWriter;
    this.parser = parser;
    this.gameView = new GameView(this);
  }

  @Override
  public void runGame() throws BuilderException {
    createGame();
    gameView.displayGUI(false);

    listenSocket();
  }

  //todo SPOSTARE QUI FLAG BOOLEANO, COSÌ NON SERVE FARE OVERRIDE DEI DUE METODI attempt
  @Override
  public void handleTileClick(Position targetPosition) {
    try {
      switch (currentGUIAction) {
        case MOVE -> {
          attemptPawnMove(targetPosition);
          sendCommand(QuoridorParser.CommandType.MOVE, targetPosition, null);
        }
        case PLACE_HORIZONTAL_WALL -> {
          attemptPlaceWall(targetPosition, WallOrientation.HORIZONTAL);
          sendCommand(QuoridorParser.CommandType.WALL, targetPosition, WallOrientation.HORIZONTAL);
        }
        case PLACE_VERTICAL_WALL -> {
          attemptPlaceWall(targetPosition, WallOrientation.VERTICAL);
          sendCommand(QuoridorParser.CommandType.WALL, targetPosition, WallOrientation.VERTICAL);
        }
        case DO_NOTHING -> eventListener.displayNotification("Wait for yor round or choose an action", true);
      }
    }catch (InvalidParameterException | InvalidActionException e){
       eventListener.displayNotification(e.getMessage(), true);
    }
  }

  //todo ===== !!!!! ===== RITORNARE BOOLEANO E SPOSTARE sendCommand SU SWITCH DI handleTileClick, DOPO CHE MOSSA È STATA VALIDATA ED ESEGUITA ===== !!!!! =====

  // TODO capire se lasciare così o fare in altro modo
  @Override
  public void restartGame() {
    try {
      new ServerStandardGUIQuoridorGameEngine(new StdQuoridorBuilder(getNumberOfPlayers()), statisticsCounter, socketReader, socketWriter, parser).runGame();
    } catch (InvalidParameterException | BuilderException e) {
      eventListener.displayNotification(e.getMessage(), true);
    }
  }

  @Override
  protected void attemptPawnMove(Position targetPosition) throws InvalidParameterException, InvalidActionException {

      Position currentPosition = new Position(
              game.getPlayingPawn().getCurrentTile().getRow(),
              game.getPlayingPawn().getCurrentTile().getColumn()
      );
      game.movePlayingPawn(targetPosition);
      Logger.printLog(System.out, "ENTRO IN MOVE");                            //todo TMP
      statisticsCounter.updateGameMoves(String.valueOf(game.getPlayingPawn()));
      eventListener.onPawnMoved(currentPosition, targetPosition, getPlayingPawnIndex());
      if (isGameFinished()) {
        statisticsCounter.updateAllTotalStats(game);
        eventListener.onGameFinished();
      } else {
        eventListener.onRoundFinished(false);
      }
      setCurrentAction(GUIAction.DO_NOTHING);
  }

  //todo CHECK ++ MANCA INOLTRO PER QUIT E RESTART
  private void sendCommand(QuoridorParser.CommandType commandType, Position position, WallOrientation wallOrientation) {
    Logger.printLog(System.out, "ENTRO IN SEND");                            //todo TMP
    try {
      forwardCommand(parser.generateString(commandType, position, wallOrientation));
    } catch (IOException e) {
      System.err.println("Unable to communicate with server: " + e.getMessage());
      //handleQuitGame();               //todo <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    }
  }

  protected void forwardCommand(String command) throws IOException {
    Logger.printLog(System.out, "ENTRO IN FORWARD");                            //todo TMP
    socketWriter.write(command + System.lineSeparator());
    socketWriter.flush();
    // socketReader.readLine();
  }

  @Override
  protected void attemptPlaceWall(Position position, WallOrientation orientation) throws InvalidParameterException, InvalidActionException {
      game.placeWall(position, orientation);
      Logger.printLog(System.out, "ENTRO IN WALL");                            //todo TMP
      statisticsCounter.updateGameWalls(String.valueOf(game.getPlayingPawn()));
      setCurrentAction(GUIAction.DO_NOTHING);
      eventListener.onWallPlaced(position, orientation, game.getPlayingPawnIndex(), game.getPlayingPawn().getNumberOfWalls());
      eventListener.onRoundFinished(false);
  }

  private void listenSocket() {
    do {
      boolean commandExecuted = false;
      String serverMessage = null;


      try {
        //System.w.println("Waiting for another player's round...");
        serverMessage = socketReader.readLine();
      } catch (IOException e) {
        System.err.println("Unable to communicate with server!" + e.getMessage());
        //handleQuitGame();               //todo <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
      }

      Logger.printLog(System.out, "GIOCATORE DI TURNO: " + game.getPlayingPawnIndex());                            //todo TMP
      Logger.printLog(System.out, "RICEVUTO COMANDO: " + serverMessage);                            //todo TMP


      try {
        if (Objects.equals(serverMessage, "PLAY")) {
          //System.w.println("It's your round!\n");

          Logger.printLog(System.out, "ESEGUO PLAY");                            //todo TMP

          gameView.displayCommandsForCurrentPlayer();     //todo DDDDDDDDD
          //todo SINTESI COMANDO DA PARSER
          //commandExecuted = performCommand("w 4,4 h", true);      //todo SPOSTATO DENTRO A CODICE DEI METODINI...
        } else {
          Logger.printLog(System.out, "ESEGUO REMOTO");                            //todo TMP
          commandExecuted = performCommand(serverMessage, false);
        }
      } catch (IOException e) {
        System.err.println("Unable to communicate with server: " + e.getMessage());
        //handleQuitGame();               //todo <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
      } catch (InvalidActionException | InvalidParameterException | ParserException | BuilderException e) {
        System.err.println(e.getMessage());
      }
    } while (!game.isGameFinished());       //todo FORSE È MEGLIO TRUE... ???
  }

  //todo DUPLICATO RISPETTO A CLI...
  protected boolean performCommand(String command, boolean forwardCommand) throws ParserException, InvalidParameterException, InvalidActionException, IOException, BuilderException {
    parser.parse(command);
    Optional<Position> targetPosition = parser.getActionPosition();

    Logger.printLog(System.out, "ENTRO IN PERFORM");                            //todo TMP

    return switch (parser.getCommandType().orElseThrow()) {
      case MOVE -> {
        attemptPawnMove(targetPosition.orElse(null));
        if (forwardCommand) {
          forwardCommand(command);
        }
        yield true;
      }
      case WALL -> {
        attemptPlaceWall(targetPosition.orElse(null), parser.getWallOrientation().orElse(null));
        if (forwardCommand) {
          forwardCommand(command);
        }
        yield true;
      }
      case HELP -> {
        //printWallsConvention();               //todo già fatto da GUI ??? <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
        System.out.println(parser);
        yield false;
      }
      case QUIT -> {
        if (forwardCommand) {
          forwardCommand(command);
        }
        //handleQuitGame();               //todo <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
        yield true;
      }
      case RESTART -> {
        if (forwardCommand) {
          forwardCommand(command);
        }
        restartGame();
        yield true;
      }
    };
  }
}