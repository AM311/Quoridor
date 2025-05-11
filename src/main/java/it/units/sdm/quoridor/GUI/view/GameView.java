package it.units.sdm.quoridor.GUI.view;

import it.units.sdm.quoridor.GUI.controller.GameActionHandler;
import it.units.sdm.quoridor.GUI.controller.GameController;
import it.units.sdm.quoridor.GUI.view.managers.DialogManager;
import it.units.sdm.quoridor.GUI.view.managers.PanelsManager;
import it.units.sdm.quoridor.exceptions.BuilderException;
import it.units.sdm.quoridor.exceptions.InvalidParameterException;
import it.units.sdm.quoridor.model.AbstractGame;
import it.units.sdm.quoridor.model.builder.BuilderDirector;
import it.units.sdm.quoridor.model.builder.StdQuoridorBuilder;
import it.units.sdm.quoridor.utils.Position;
import it.units.sdm.quoridor.utils.WallOrientation;

import javax.swing.*;

public class GameView implements GameEventListener {
  private final GameActionHandler actionHandler;
  private PanelsManager panelsManager;
  private DialogManager dialogManager;

  public GameView(GameActionHandler actionHandler) {
    this.actionHandler = actionHandler;

    if (actionHandler instanceof GameController) {
      ((GameController) actionHandler).setEventListener(this);
    }
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      try {
        BuilderDirector builderDirector = new BuilderDirector(new StdQuoridorBuilder(2));
        AbstractGame game = builderDirector.makeGame();
        GameController gameController = new GameController(game);
        GameView gameView = new GameView(gameController);
        gameView.displayGUI();
      } catch (BuilderException | InvalidParameterException e) {
        JOptionPane.showMessageDialog(null,
                "Error starting game: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
      }
    });
  }

  public void displayGUI() {
    JFrame mainFrame = new JFrame("Quoridor");
    mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);

    dialogManager = new DialogManager(mainFrame, (GameController) actionHandler);
    panelsManager = new PanelsManager((GameController) actionHandler, dialogManager);

    JPanel rootPanel = panelsManager.createRootPanel();
    mainFrame.setContentPane(rootPanel);
    mainFrame.setVisible(true);

    dialogManager.displayHelpDialog();
    displayNotification("Player " + (actionHandler.getPlayingPawnIndex() + 1) + "'s turn", false);
  }

  @Override
  public void onWallPlaced(int playerIndex, int remainingWalls) {
    panelsManager.updateWallLabel(playerIndex, remainingWalls);
  }

  @Override
  public void onTurnComplete() {
    panelsManager.removeCurrentActionPanel(actionHandler.getPlayingPawnIndex());
    if (actionHandler instanceof GameController) {
      ((GameController) actionHandler).changeRound();
    }
    panelsManager.updatePlayerPanel(actionHandler.getPlayingPawnIndex());
    panelsManager.displayActionsPanelForPlayingPlayer(actionHandler.getPlayingPawnIndex());
    displayNotification("Player " + (actionHandler.getPlayingPawnIndex() + 1) + "'s turn", false);
  }

  @Override
  public void displayNotification(String message, boolean isError) {
    dialogManager.displayNotificationDialog(message, isError);
  }

  @Override
  public void highlightValidMoves() {
    panelsManager.highlightValidMoves();
  }

  @Override
  public void onPawnMoved(Position oldPosition, Position newPosition, int playerIndex) {
    panelsManager.updatePawnPosition(oldPosition, newPosition, playerIndex);
  }

  @Override
  public void updateWallVisualization(Position position, WallOrientation orientation) {
    panelsManager.updateWallVisualization(position, orientation);
  }

  @Override
  public void clearHighlights() {
    panelsManager.clearHighlights();
  }

  @Override
  public void displayWallDirectionButtons(int playerIndex) {
    panelsManager.displayWallDirectionButtons(playerIndex);
  }

  @Override
  public void showGameFinishedDialog() {
    dialogManager.displayGameFinishedDialog();
  }
}