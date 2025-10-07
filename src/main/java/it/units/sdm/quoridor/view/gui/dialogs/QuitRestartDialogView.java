package it.units.sdm.quoridor.view.gui.dialogs;

import it.units.sdm.quoridor.controller.engine.gui.GUIQuoridorGameEngine;
import it.units.sdm.quoridor.controller.server.Logger;
import it.units.sdm.quoridor.exceptions.BuilderException;
import it.units.sdm.quoridor.utils.GUIConstants;

import javax.swing.*;
import java.awt.*;

public class QuitRestartDialogView implements DialogView {
  private final GUIQuoridorGameEngine gameEngine;
  private final JFrame mainFrame;
  private final JDialog dialog;

  public QuitRestartDialogView(GUIQuoridorGameEngine gameEngine, JFrame mainFrame) {
    this.gameEngine = gameEngine;
    this.mainFrame = mainFrame;
    this.dialog = new JDialog(mainFrame, true);
  }

  @Override
  public void displayDialog() {
    dialog.setUndecorated(true);
    dialog.setSize(400, 150);
    dialog.setLocationRelativeTo(mainFrame);
    dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

    JPanel panel = createDialog();

    dialog.add(panel);
    dialog.setVisible(true);

    dialog.toFront();
    dialog.requestFocus();
  }

  private JPanel createDialog() {
    JPanel panel = new JPanel(new BorderLayout());
    panel.setBorder(GUIConstants.POPUP_BORDER);
    panel.setBackground(GUIConstants.SCREEN_BACKGROUND);

    JLabel messageLabel = getMessageLabel();
    panel.add(messageLabel, BorderLayout.NORTH);

    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
    buttonPanel.setBackground(GUIConstants.SCREEN_BACKGROUND);
    buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 30, 30));

    JButton restartButton = getRestartButton();
    JButton exitButton = getExitButton();

    buttonPanel.add(restartButton);
    buttonPanel.add(exitButton);
    panel.add(buttonPanel, BorderLayout.CENTER);

    return panel;
  }

  private JButton getExitButton() {
    JButton exitButton = new JButton("EXIT");

    exitButton.setFont(GUIConstants.BUTTON_FONT);
    exitButton.setPreferredSize(new Dimension(GUIConstants.BUTTON_WIDTH, GUIConstants.BUTTON_HEIGHT));
    exitButton.addActionListener(e -> {
      dialog.dispose();
      gameEngine.handleQuitGame();
    });
    return exitButton;
  }

  private JButton getRestartButton() {
    JButton restartButton = new JButton("RESTART");
    restartButton.setFont(GUIConstants.BUTTON_FONT);
    restartButton.setPreferredSize(new Dimension(GUIConstants.BUTTON_WIDTH, GUIConstants.BUTTON_HEIGHT));
    restartButton.addActionListener(e -> {
      try {
        dialog.dispose();
        gameEngine.handleRestartGame();
      } catch (BuilderException ex) {
        Logger.printLog(System.err, "Exception while restarting game: " + ex.getMessage());
        gameEngine.handleQuitGame();
      }
    });
    return restartButton;
  }

  private JLabel getMessageLabel() {
    JLabel messageLabel = new JLabel("<html>Choose how to continue<br></html>", SwingConstants.CENTER);
    messageLabel.setForeground(GUIConstants.TEXT_COLOR);

    messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
    messageLabel.setFont(GUIConstants.NORMAL_FONT);
    messageLabel.setBorder(BorderFactory.createEmptyBorder(30, 30, 0, 30));
    return messageLabel;
  }
}