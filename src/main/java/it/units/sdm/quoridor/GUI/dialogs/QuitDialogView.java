package it.units.sdm.quoridor.GUI.dialogs;

import it.units.sdm.quoridor.GUI.managers.DialogManager;
import it.units.sdm.quoridor.GUI.GUIConstants;

import javax.swing.*;
import java.awt.*;

public class QuitDialogView implements DialogView {
  private final JDialog confirmQuitDialog;
  private final DialogManager dialogManager;
  private final JFrame mainFrame;

  // TODO BISOGNERA METTERE IL CONTROLLER AL POSTO DEL DIALOGMANAGER
  public QuitDialogView(DialogManager dialogManager, JFrame mainFrame) {
    this.dialogManager = dialogManager;
    this.mainFrame = mainFrame;
    this.confirmQuitDialog = new JDialog(mainFrame, true);
  }


  @Override
  public void displayDialog() {
    confirmQuitDialog.setUndecorated(true);
    confirmQuitDialog.setSize(400, 200);
    confirmQuitDialog.setLocationRelativeTo(mainFrame);
    confirmQuitDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

    JPanel panel = createDialog();

    confirmQuitDialog.add(panel);
    confirmQuitDialog.setVisible(true);
  }


  private JPanel createDialog() {
    JPanel quitPanel = new JPanel(new BorderLayout());
    quitPanel.setBorder(GUIConstants.POPUP_BORDER);
    quitPanel.setBackground(GUIConstants.BACKGROUND_COLOR);

    JLabel messageLabel = new JLabel("<html>Are you sure you want to quit?<br><br></html>",
            SwingConstants.CENTER);
    messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
    messageLabel.setFont(GUIConstants.NORMAL_FONT);
    messageLabel.setForeground(GUIConstants.TEXT_COLOR);
    messageLabel.setBorder(BorderFactory.createEmptyBorder(30, 30, 0, 30));
    quitPanel.add(messageLabel, BorderLayout.NORTH);

    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
    buttonPanel.setBackground(GUIConstants.BACKGROUND_COLOR);
    buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 30, 30));

    JButton yesButton = new JButton("YES");
    yesButton.setFont(GUIConstants.BUTTON_FONT);
    yesButton.setPreferredSize(new Dimension(GUIConstants.BUTTON_WIDTH, GUIConstants.BUTTON_HEIGHT));
    yesButton.addActionListener(e -> {
      confirmQuitDialog.dispose();
      dialogManager.showGameFinishedDialog();
    });

    JButton noButton = new JButton("NO");
    noButton.setFont(GUIConstants.BUTTON_FONT);
    noButton.setPreferredSize(new Dimension(GUIConstants.BUTTON_WIDTH, GUIConstants.BUTTON_HEIGHT));
    noButton.addActionListener(e -> confirmQuitDialog.dispose());

    buttonPanel.add(yesButton);
    buttonPanel.add(noButton);
    quitPanel.add(buttonPanel, BorderLayout.CENTER);

    return quitPanel;
  }
}
