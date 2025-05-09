package it.units.sdm.quoridor.GUI.managers;

import it.units.sdm.quoridor.GUI.dialogs.HelpDialogView;
import it.units.sdm.quoridor.GUI.dialogs.NotificationDialogView;
import it.units.sdm.quoridor.GUI.dialogs.GameFinishedDialogView;
import it.units.sdm.quoridor.GUI.dialogs.QuitDialogView;

import javax.swing.*;

public class DialogManager {
  private final JFrame mainFrame;
  private final GameGUIManager gameManager;

  public DialogManager(JFrame mainFrame, GameGUIManager gameManager) {
    this.mainFrame = mainFrame;
    this.gameManager = gameManager;
  }

  public void showHelpDialog() {
    new HelpDialogView(mainFrame).displayDialog();
  }

  // TODO DA RIMUOVERE IL THIS CON IL CONTROLLER
  public void showConfirmQuitDialog() {
    new QuitDialogView(this, mainFrame).displayDialog();
  }

  public void showGameFinishedDialog() {
    new GameFinishedDialogView(gameManager, mainFrame).displayDialog();
  }

  public void showNotificationDialog(String message, boolean invalidActionFlag) {
    new NotificationDialogView(message, invalidActionFlag, mainFrame).displayDialog();
  }
}