package it.units.sdm.quoridor.view.gui.dialogs;

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

  private final ComponentAdapter resizeListener = new ComponentAdapter() {
    @Override
    public void componentResized(ComponentEvent e) {
      updateNotificationDialog();
    }
  };

  public NotificationDialogView(String message, boolean invalidActionFlag, JFrame mainFrame) {
    this.message = "<html><div>" + message + "</div></html>";
    this.invalidActionFlag = invalidActionFlag;
    this.mainFrame = mainFrame;
    mainFrame.addComponentListener(resizeListener);
  }

  @Override
  public void displayDialog() {
    notificationDialog = new JDialog(mainFrame);
    notificationDialog.setUndecorated(true);

    updateNotificationDialog();

    JPanel notificationPanel = createDialogContent();
    notificationDialog.add(notificationPanel);
    notificationDialog.setVisible(true);

    if (invalidActionFlag) {
      Timer timer = new Timer(3000, e -> dispose());
      timer.setRepeats(false);
      timer.start();
    }
  }

  public void dispose() {
    mainFrame.removeComponentListener(resizeListener);
    if (notificationDialog != null) {
      notificationDialog.dispose();
      notificationDialog = null;
    }
  }

  private void updateNotificationDialog() {
    if (notificationDialog == null) return;

    Dimension mainFrameSize = mainFrame.getSize();
    int mainFrameWidth = mainFrameSize.width;
    int mainFrameHeight = mainFrameSize.height;

    int dialogWidth = (int) (mainFrameWidth * 0.16);
    int dialogHeight = (int) (mainFrameHeight * 0.096);

    notificationDialog.setSize(dialogWidth, dialogHeight);

    int dialogX = invalidActionFlag ? (int) (mainFrameWidth * 0.78) : (int) (mainFrameWidth * 0.029);
    int dialogY = (int) (mainFrameHeight * 0.422);

    Point p = mainFrame.getLocationOnScreen();
    notificationDialog.setLocation(p.x + dialogX, p.y + dialogY);
  }

  private JPanel createDialogContent() {
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
