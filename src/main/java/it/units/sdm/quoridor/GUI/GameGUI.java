package it.units.sdm.quoridor.GUI;

import it.units.sdm.quoridor.exceptions.BuilderException;
import it.units.sdm.quoridor.exceptions.InvalidParameterException;
import it.units.sdm.quoridor.model.AbstractGame;
import it.units.sdm.quoridor.model.builder.BuilderDirector;
import it.units.sdm.quoridor.model.builder.StdQuoridorBuilder;

import javax.swing.*;

public class GameGUI {
  private final int numberOfPlayers;
  private final GameController controller;
  private PanelsManager panelsManager;
  private DialogManager dialogManager;

  public GameGUI(int numberOfPlayers, GameController controller) {
    this.numberOfPlayers = numberOfPlayers;
    this.controller = controller;
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      try {
        BuilderDirector builderDirector = new BuilderDirector(new StdQuoridorBuilder(2));
        AbstractGame game = builderDirector.makeGame();
        GameController gameController = new GameController(game);
        GameGUI gameGUI = new GameGUI(2, gameController);
        gameGUI.showGUI();
      } catch (BuilderException | InvalidParameterException e) {
        JOptionPane.showMessageDialog(null,
                "Error starting game: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
      }
    });
  }

  public void showGUI() {
    JFrame mainFrame = new JFrame("Quoridor");
    mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);

    dialogManager = new DialogManager(mainFrame, controller);
    panelsManager = new PanelsManager(controller, numberOfPlayers, dialogManager, mainFrame);

    JPanel rootPanel = panelsManager.createRootPanel();
    mainFrame.setContentPane(rootPanel);

    controller.connectComponents(this, panelsManager.getGameBoardPanel(), dialogManager);

    mainFrame.setVisible(true);

    dialogManager.showHelpDialog();
    dialogManager.showNotificationDialog("Player " + (controller.getPlayingPawnIndex() + 1) + "'s turn", controller.getPlayingPawnIndex(), 1500);
  }

  public void onWallPlaced(int playerIndex, int remainingWalls) {
    panelsManager.updateWallLabel(playerIndex, remainingWalls);
  }

  public void onTurnComplete() {
    panelsManager.removeCurrentActionPanel(controller.getPlayingPawnIndex());
    controller.changeRound();
    panelsManager.updatePlayerPanel(controller.getPlayingPawnIndex());
    panelsManager.showActionButtonsForPlayer(controller.getPlayingPawnIndex());
    dialogManager.showNotificationDialog("Player " + (controller.getPlayingPawnIndex() + 1) + "'s turn", controller.getPlayingPawnIndex(), 1500);

  }
}