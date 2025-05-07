package it.units.sdm.quoridor.GUI.managers;

import it.units.sdm.quoridor.GUI.dialogs.HelpDialogComponent;
import it.units.sdm.quoridor.GUI.dialogs.NotificationDialogComponent;
import it.units.sdm.quoridor.GUI.dialogs.GameFinishedDialogComponent;
import it.units.sdm.quoridor.GUI.dialogs.QuitDialogComponent;

import javax.swing.*;

public class DialogManager {
  private final JFrame mainFrame;
  private final GameGUIManager gameManager;

  public DialogManager(JFrame mainFrame, GameGUIManager gameManager) {
    this.mainFrame = mainFrame;
    this.gameManager = gameManager;
  }

  public void showHelpDialog() {
    JPanel helpPanel = new HelpDialogComponent().createDialog();

    JOptionPane.showMessageDialog(
            mainFrame,
            helpPanel,
            "Quoridor Help",
            JOptionPane.INFORMATION_MESSAGE
    );
  }


  public void showConfirmQuitDialog() {
    JDialog confirmQuitDialog = new JDialog(mainFrame, true);
    confirmQuitDialog.setUndecorated(true);
    confirmQuitDialog.setSize(400, 200);
    confirmQuitDialog.setLocationRelativeTo(mainFrame);
    confirmQuitDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

    JPanel panel = new QuitDialogComponent(confirmQuitDialog, this).createDialog();

    confirmQuitDialog.add(panel);
    confirmQuitDialog.setVisible(true);
  }

  public void showGameFinishedDialog() {
    JDialog gameFinishedDialog = new JDialog(mainFrame, true);
    gameFinishedDialog.setUndecorated(true);
    gameFinishedDialog.setSize(400, 200);
    gameFinishedDialog.setLocationRelativeTo(mainFrame);
    gameFinishedDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

    JPanel panel = new GameFinishedDialogComponent(gameManager, mainFrame, gameFinishedDialog).createDialog();

    gameFinishedDialog.add(panel);
    gameFinishedDialog.setVisible(true);
  }

  public void showNotificationDialog(String message, boolean invalidActionFlag) {
    JDialog notificationDialog = new JDialog(mainFrame);
    notificationDialog.setUndecorated(true);
    notificationDialog.setSize(250, 80);

    if (!invalidActionFlag) {
      notificationDialog.setLocation(45, 350);
    } else {
      notificationDialog.setLocation(1210, 350);
    }

    JPanel notificationPanel = new NotificationDialogComponent(message).createDialog();

    notificationDialog.add(notificationPanel);
    notificationDialog.setVisible(true);

    if (invalidActionFlag) {
      Timer timer = new Timer(3000, e -> notificationDialog.dispose());
      timer.setRepeats(false);
      timer.start();
    }

  }

}