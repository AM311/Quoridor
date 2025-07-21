package it.units.sdm.quoridor.GUI.view.dialogs;

import it.units.sdm.quoridor.utils.GUIConstants;

import javax.swing.*;
import java.awt.*;

public class NotificationDialogView implements DialogView {
  private final String message;
  private final boolean invalidActionFlag;
  private final JFrame mainFrame;

  public NotificationDialogView(String message, boolean invalidActionFlag, JFrame mainFrame) {
    this.message = "<html><div>" + message + "</div></html>";
    this.invalidActionFlag = invalidActionFlag;
    this.mainFrame = mainFrame;
  }

  @Override
  public void displayDialog() {
    JDialog notificationDialog = new JDialog(mainFrame);
    notificationDialog.setUndecorated(true);
    notificationDialog.setSize(250, 80);

    if (!invalidActionFlag) {
      notificationDialog.setLocation(45, 350);
    } else {
      notificationDialog.setLocation(1210, 350);
    }

    JPanel notificationPanel = createDialog();

    notificationDialog.add(notificationPanel);
    notificationDialog.setVisible(true);

    if (invalidActionFlag) {
      Timer timer = new Timer(3000, e -> notificationDialog.dispose());
      timer.setRepeats(false);
      timer.start();
    }
  }

  private JPanel createDialog() {
    JPanel panel = new JPanel(new BorderLayout());
    panel.setBorder(GUIConstants.POPUP_BORDER);
    panel.setBackground(Color.WHITE);

    JLabel messageLabel = new JLabel(message, SwingConstants.CENTER);
    messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
    messageLabel.setFont(GUIConstants.NORMAL_FONT);
    messageLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    panel.add(messageLabel, BorderLayout.CENTER);
    return panel;
  }
}
