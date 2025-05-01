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
  private PanelManager panelManager;

  public GameGUI(int numberOfPlayers, GameController controller) {
    this.numberOfPlayers = numberOfPlayers;
    this.controller = controller;
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      int numPlayers = 4;
      try {
        BuilderDirector builderDirector = new BuilderDirector(new StdQuoridorBuilder(numPlayers));
        AbstractGame game = builderDirector.makeGame();

        GameController controller = new GameController(game);

        GameGUI gameGUI = new GameGUI(numPlayers, controller);
        gameGUI.showGUI();
      } catch (InvalidParameterException | BuilderException e) {
        System.out.println(e.getMessage());
      }
    });
  }


  public void showGUI() {
    JFrame mainFrame = new JFrame("Quoridor");
    mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);

    DialogManager dialogManager = new DialogManager(mainFrame, numberOfPlayers, controller);

    panelManager = new PanelManager(controller, numberOfPlayers, dialogManager);

    JPanel rootPanel = panelManager.createRootPanel();
    mainFrame.setContentPane(rootPanel);

    controller.connectComponents(this, panelManager.getGameBoardPanel(), dialogManager);

    mainFrame.setVisible(true);

    dialogManager.showHelpDialog();
  }

  public void onWallPlaced(int playerIndex, int remainingWalls) {
    panelManager.updateWallLabel(playerIndex, remainingWalls);
  }

  public void onTurnComplete() {
    panelManager.removeCurrentActionPanel(controller.getPlayingPawnIndex());
    controller.changeRound();
    panelManager.showActionButtonsForPlayer(controller.getPlayingPawnIndex());
  }
}