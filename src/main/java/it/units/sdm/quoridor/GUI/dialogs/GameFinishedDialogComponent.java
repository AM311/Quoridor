package it.units.sdm.quoridor.GUI.dialogs;

import it.units.sdm.quoridor.GUI.GUIConstants;
import it.units.sdm.quoridor.GUI.managers.GameGUIManager;
import it.units.sdm.quoridor.GUI.GameGUI;
import it.units.sdm.quoridor.exceptions.BuilderException;
import it.units.sdm.quoridor.exceptions.InvalidParameterException;
import it.units.sdm.quoridor.model.builder.BuilderDirector;
import it.units.sdm.quoridor.model.builder.StdQuoridorBuilder;

import javax.swing.*;
import java.awt.*;

public class GameFinishedDialogComponent implements DialogComponent {
  private final GameGUIManager gameManager;
  private final JFrame mainFrame;
  private final JDialog gameFinishedDialog;

  public GameFinishedDialogComponent(GameGUIManager gameManager, JFrame mainFrame, JDialog gameFinishedDialog) {
    this.gameManager = gameManager;
    this.mainFrame = mainFrame;
    this.gameFinishedDialog = gameFinishedDialog;
  }

  @Override
  public JPanel createDialog() {
    JPanel panel = new JPanel(new BorderLayout());
    panel.setBorder(GUIConstants.POPUP_BORDER);
    panel.setBackground(GUIConstants.WIN_SCREEN_BACKGROUND);

    JLabel messageLabel = getMessageLabel();
    panel.add(messageLabel, BorderLayout.NORTH);

    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
    buttonPanel.setBackground(GUIConstants.WIN_SCREEN_BACKGROUND);
    buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 30, 30));

    JButton restartButton = new JButton("RESTART");
    restartButton.setFont(GUIConstants.BUTTON_FONT);
    restartButton.setPreferredSize(new Dimension(GUIConstants.BUTTON_WIDTH, GUIConstants.BUTTON_HEIGHT));
    restartButton.addActionListener(e -> {
      gameFinishedDialog.dispose();
      restartGame();
    });


    JButton exitButton = new JButton("EXIT");

    exitButton.setFont(GUIConstants.BUTTON_FONT);
    exitButton.setPreferredSize(new Dimension(GUIConstants.BUTTON_WIDTH, GUIConstants.BUTTON_HEIGHT));
    exitButton.addActionListener(e -> System.exit(0));

    buttonPanel.add(restartButton);
    buttonPanel.add(exitButton);
    panel.add(buttonPanel, BorderLayout.CENTER);
    return panel;
  }

  private JLabel getMessageLabel() {
    int numberOfPlayers = gameManager.getGame().getPawns().size();
    boolean isGameFinished = gameManager.isGameFinished();
    JLabel messageLabel = getMessageLabel(numberOfPlayers, isGameFinished);

    messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
    messageLabel.setFont(GUIConstants.NORMAL_FONT);
    messageLabel.setBorder(BorderFactory.createEmptyBorder(30, 30, 0, 30));
    return messageLabel;
  }

  private JLabel getMessageLabel(int numberOfPlayers, boolean isGameFinished) {
    int playingPawnIndex = gameManager.getPlayingPawnIndex();

    JLabel messageLabel;
    if (numberOfPlayers == 2) {
      messageLabel = new JLabel("<html>Player " + (isGameFinished ? (playingPawnIndex + 1) : (2 - playingPawnIndex)) + " WINS!<br><br></html>", SwingConstants.CENTER);
    } else {
      messageLabel = new JLabel("<html>Player " + (isGameFinished ? (playingPawnIndex + 1) + " WINS!</html>" : (playingPawnIndex + 1) + " QUIT!</html>") + "<br><br></html>", SwingConstants.CENTER);
    }
    return messageLabel;
  }

  private void restartGame() {
    if (mainFrame != null) {
      mainFrame.dispose();
    }

    try {
      BuilderDirector builderDirector = new BuilderDirector(new StdQuoridorBuilder(gameManager.getGame().getPawns().size()));
      var game = builderDirector.makeGame();
      GameGUIManager newgameManager = new GameGUIManager(game);
      GameGUI gameGUI = new GameGUI(gameManager.getGame().getPawns().size(), newgameManager);
      gameGUI.showGUI();
    } catch (InvalidParameterException | BuilderException e) {
      JOptionPane.showMessageDialog(mainFrame,
              e.getMessage(),
              "Error",
              JOptionPane.ERROR_MESSAGE);
    }
  }

}
