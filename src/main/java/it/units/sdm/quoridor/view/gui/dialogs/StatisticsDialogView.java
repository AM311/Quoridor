package it.units.sdm.quoridor.view.gui.dialogs;

import it.units.sdm.quoridor.controller.StatisticsCounter;
import it.units.sdm.quoridor.model.abstracts.AbstractPawn;
import it.units.sdm.quoridor.utils.GUIConstants;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class StatisticsDialogView implements DialogView {
  private final StatisticsCounter statistics;
  private final List<AbstractPawn> pawns;
  private final int numberOfPlayers;
  private final JFrame mainFrame;
  private final JDialog dialog;

  public StatisticsDialogView(StatisticsCounter statistics, List<AbstractPawn> pawns, int numberOfPlayers, JFrame mainFrame) {
    this.statistics = statistics;
    this.pawns = pawns;
    this.numberOfPlayers = numberOfPlayers;
    this.mainFrame = mainFrame;
    this.dialog = new JDialog(mainFrame, true);
  }

  @Override
  public void displayDialog() {
    dialog.setUndecorated(true);
    dialog.setSize(700, numberOfPlayers == 4 ? 750 : 600);
    dialog.setLocationRelativeTo(mainFrame);
    dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

    JPanel panel = createDialogContent();
    dialog.add(panel);
    dialog.setVisible(true);
  }

  private JPanel createDialogContent() {
    JPanel panel = new JPanel(new BorderLayout());
    panel.setBorder(GUIConstants.POPUP_BORDER);
    panel.setBackground(GUIConstants.SCREEN_BACKGROUND);

    JLabel messageLabel = getStatisticsMessageLabel();
    panel.add(messageLabel, BorderLayout.NORTH);

    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
    buttonPanel.setBackground(GUIConstants.SCREEN_BACKGROUND);
    buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 30, 30));

    JButton closeButton = new JButton("CLOSE");
    closeButton.setFont(GUIConstants.BUTTON_FONT);
    closeButton.setPreferredSize(new Dimension(GUIConstants.BUTTON_WIDTH, GUIConstants.BUTTON_HEIGHT));
    closeButton.addActionListener(e -> dialog.dispose());

    buttonPanel.add(closeButton);
    panel.add(buttonPanel, BorderLayout.CENTER);

    return panel;
  }

  private JLabel getStatisticsMessageLabel() {
    JLabel messageLabel = new JLabel(
            "<html><br>" + getLastGameStatistics() + "<br><br><br>" + getTotalStatistics() + "</html>",
            SwingConstants.CENTER
    );
    messageLabel.setForeground(GUIConstants.TEXT_COLOR);
    messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
    messageLabel.setFont(GUIConstants.NORMAL_FONT);
    messageLabel.setBorder(BorderFactory.createEmptyBorder(30, 30, 0, 30));
    return messageLabel;
  }

  private String getLastGameStatistics() {
    StringBuilder lastGameStatistics = new StringBuilder();
    lastGameStatistics.append("============= LAST GAME STATISTICS ============= <br><br>");
    lastGameStatistics.append("<table style='width:100%; text-align:right;'>");

    lastGameStatistics.append("<tr>")
            .append("<th style='padding:6px;'>PLAYER</th>")
            .append("<th style='padding:6px;'>MOVES MADE</th>")
            .append("<th style='padding:6px;'>WALLS PLACED</th>")
            .append("</tr>");

    for (AbstractPawn pawn : pawns) {
      String playerColor = pawn.getPawnAppearance().color().toString();
      String playerIdentifier = pawn.getPawnAppearance().toString();

      lastGameStatistics.append("<tr>")
              .append(GUIConstants.STATISTICS_STYLE).append(playerColor).append("</td>")
              .append(GUIConstants.STATISTICS_STYLE).append(statistics.getGameMoves(playerIdentifier)).append("</td>")
              .append(GUIConstants.STATISTICS_STYLE).append(statistics.getGameWalls(playerIdentifier)).append("</td>")
              .append("</tr>");
    }

    lastGameStatistics.append("</table>");
    return lastGameStatistics.toString();
  }

  private String getTotalStatistics() {
    StringBuilder totalStatisticsString = new StringBuilder();
    totalStatisticsString.append("============= TOTAL GAME STATISTICS ============= <br><br>");
    totalStatisticsString.append("<table style='width:100%; text-align:right;'>");

    totalStatisticsString.append("<tr>")
            .append(GUIConstants.STATISTICS_STYLE).append("PLAYER</th>")
            .append(GUIConstants.STATISTICS_STYLE).append("WINS</th>")
            .append(GUIConstants.STATISTICS_STYLE).append("WIN RATE</th>")
            .append(GUIConstants.STATISTICS_STYLE).append("TOTAL MOVES</th>")
            .append(GUIConstants.STATISTICS_STYLE).append("TOTAL WALLS</th>")
            .append("</tr>");

    for (AbstractPawn pawn : pawns) {
      String playerColor = pawn.getPawnAppearance().color().toString();
      String playerIdentifier = pawn.getPawnAppearance().toString();

      totalStatisticsString.append("<tr>")
              .append(GUIConstants.STATISTICS_STYLE).append(playerColor).append("</th>")
              .append(GUIConstants.STATISTICS_STYLE).append(statistics.getTotalWins(playerIdentifier)).append("</th>")
              .append(GUIConstants.STATISTICS_STYLE).append(statistics.getWinRate(playerIdentifier)).append("</th>")
              .append(GUIConstants.STATISTICS_STYLE).append(statistics.getTotalMoves(playerIdentifier)).append("</th>")
              .append(GUIConstants.STATISTICS_STYLE).append(statistics.getTotalWalls(playerIdentifier)).append("</th>")
              .append("</tr>");
    }

    totalStatisticsString.append("</table>");
    return totalStatisticsString.toString();
  }
}
