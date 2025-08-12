package it.units.sdm.quoridor.GUI.view.dialogs;

import it.units.sdm.quoridor.utils.GUIConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class NotificationDialogView implements DialogView {
  private final String message;
  private final boolean invalidActionFlag;
  private final JFrame mainFrame;
  private JDialog notificationDialog;

  public NotificationDialogView(String message, boolean invalidActionFlag, JFrame mainFrame) {
    this.message = "<html><div>" + message + "</div></html>";
    this.invalidActionFlag = invalidActionFlag;
    this.mainFrame = mainFrame;

    mainFrame.addComponentListener(new ComponentAdapter() {
      @Override
      public void componentResized(ComponentEvent e) {
        updateNotificationDialog();
      }
    });
  }

  @Override
  public void displayDialog() {
    notificationDialog = new JDialog(mainFrame);
    notificationDialog.setUndecorated(true);

    updateNotificationDialog();

    JPanel notificationPanel = createDialog();
    notificationDialog.add(notificationPanel);
    notificationDialog.setVisible(true);

    if (invalidActionFlag) {
      Timer timer = new Timer(3000, e -> notificationDialog.dispose());
      timer.setRepeats(false);
      timer.start();
    }
  }

  private void updateNotificationDialog() {
    Dimension mainFrameSize = mainFrame.getSize();
    int mainFrameWidth = mainFrameSize.width;
    int mainFrameHeight = mainFrameSize.height;

    int dialogWidth = (int) (mainFrameWidth * 0.16);
    int dialogHeight = (int) (mainFrameHeight * 0.096);

    notificationDialog.setSize(dialogWidth, dialogHeight);

    int dialogX = invalidActionFlag ? (int) (mainFrameWidth * 0.78) : (int) (mainFrameWidth * 0.029);
    int dialogY = (int) (mainFrameHeight * 0.422);

    notificationDialog.setLocation(dialogX, dialogY);
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
