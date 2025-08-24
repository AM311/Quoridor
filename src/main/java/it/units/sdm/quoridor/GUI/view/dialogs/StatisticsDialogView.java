package it.units.sdm.quoridor.GUI.view.dialogs;

import it.units.sdm.quoridor.cli.StatisticsCounter;
import it.units.sdm.quoridor.cli.engine.GUIQuoridorGameEngine;
import it.units.sdm.quoridor.model.AbstractPawn;
import it.units.sdm.quoridor.utils.GUIConstants;

import javax.swing.*;
import java.awt.*;

public class StatisticsDialogView implements DialogView {
  private final GUIQuoridorGameEngine gameEngine;
  private final JFrame mainFrame;
  private final JDialog dialog;

  public StatisticsDialogView(GUIQuoridorGameEngine gameEngine, JFrame mainFrame) {
    this.gameEngine = gameEngine;
    this.mainFrame = mainFrame;
    this.dialog = new JDialog(mainFrame, true);
  }

  @Override
  public void displayDialog() {
    dialog.setUndecorated(true);
    dialog.setSize(600,  gameEngine.getNumberOfPlayers() == 4 ? 500 : 400);
    dialog.setLocationRelativeTo(mainFrame);
    dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

    JPanel panel = createDialog();

    dialog.add(panel);
    dialog.setVisible(true);
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

    JButton closeButton = new JButton("CLOSE");
    closeButton.setFont(GUIConstants.BUTTON_FONT);
    closeButton.setPreferredSize(new Dimension(GUIConstants.BUTTON_WIDTH, GUIConstants.BUTTON_HEIGHT));
    closeButton.addActionListener(e -> {
      dialog.dispose();
    });

    buttonPanel.add(closeButton);
    panel.add(buttonPanel, BorderLayout.CENTER);

    return panel;
  }

  private JLabel getMessageLabel() {
    JLabel messageLabel = new JLabel("<html><br>" + getStatistics() + "</html>", SwingConstants.CENTER);
    messageLabel.setForeground(GUIConstants.TEXT_COLOR);

    messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
    messageLabel.setFont(GUIConstants.NORMAL_FONT);
    messageLabel.setBorder(BorderFactory.createEmptyBorder(30, 30, 0, 30));
    return messageLabel;
  }

  private String getStatistics() {
    StatisticsCounter statistics = gameEngine.getStatisticsCounter();
    StringBuilder statisticsString = new StringBuilder();
    statisticsString.append("============= GAME STATISTICS ============= <br><br><br>");

    statisticsString.append("<table style='width:100%; text-align:right;'>");

    statisticsString.append("<tr>")
            .append("<th style='padding:6px;'>PLAYER</th>")
            .append("<th style='padding:6px;'>WINS</th>")
            .append("<th style='padding:6px;'>WIN RATE</th>")
            .append("<th style='padding:6px;'>TOTAL MOVES</th>")
            .append("<th style='padding:6px;'>TOTAL WALLS</th>")
            .append("</tr>");

    for (AbstractPawn pawn : gameEngine.getPawns()) {
      String playerColor = pawn.getPawnAppearance().color().toString();
      String playerIdentifier = pawn.getPawnAppearance().toString();

      statisticsString.append("<tr>")
              .append("<th style='padding:6px;'>").append(playerColor).append("</td>")
              .append("<th style='padding:6px;'>").append(statistics.getTotalWins(playerIdentifier)).append("</td>")
              .append("<th style='padding:6px;'>").append(statistics.getWinRate(playerIdentifier)).append("</td>")
              .append("<th style='padding:6px;'>").append(statistics.getTotalMoves(playerIdentifier)).append("</td>")
              .append("<th style='padding:6px;'>").append(statistics.getTotalWalls(playerIdentifier)).append("</td>")
              .append("</tr>");
    }

    statisticsString.append("</table>");
    return statisticsString.toString();
  }
}