package it.units.sdm.quoridor.GUI.dialogs;

import it.units.sdm.quoridor.GUI.GUIConstants;

import javax.swing.*;
import java.awt.*;

public class NotificationDialogComponent implements DialogComponent {
  private final String message;

  public NotificationDialogComponent(String message) {
    this.message = message;
  }

  @Override
  public JPanel createDialog() {
    JPanel panel = new JPanel(new BorderLayout());
    panel.setBorder(GUIConstants.POPUP_BORDER);
    panel.setBackground(GUIConstants.POPUP_BACKGROUND);

    JLabel messageLabel = new JLabel(message, SwingConstants.CENTER);
    messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
    messageLabel.setFont(GUIConstants.SMALL_FONT);
    messageLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    panel.add(messageLabel, BorderLayout.CENTER);
    return panel;
  }
}
