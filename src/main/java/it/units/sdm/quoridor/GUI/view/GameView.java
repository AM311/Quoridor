package it.units.sdm.quoridor.GUI.view;

import it.units.sdm.quoridor.GUI.controller.GameActionHandler;
import it.units.sdm.quoridor.GUI.controller.GameController;
import it.units.sdm.quoridor.GUI.view.managers.DialogManager;
import it.units.sdm.quoridor.GUI.view.managers.PanelsManager;
import it.units.sdm.quoridor.cli.StatisticsCounter;
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
  public void onWallPlaced(Position position, WallOrientation orientation, int playerIndex, int remainingWalls) {
    panelsManager.updateWallVisualization(position, orientation);
    panelsManager.updateWallLabel(playerIndex, remainingWalls);
    onTurnFinished();
  }

  @Override
  public void onTurnFinished() {
    panelsManager.removeCurrentActionPanel(actionHandler.getPlayingPawnIndex());
    actionHandler.changeRound();
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
  public void onInvalidAction(String message) {
    dialogManager.displayNotificationDialog(message, true);
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
  public void onGameFinished(StatisticsCounter statistics) {
    dialogManager.displayGameFinishedDialog(statistics);
  }
}