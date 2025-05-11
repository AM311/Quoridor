package it.units.sdm.quoridor.GUI.view.managers;

import it.units.sdm.quoridor.GUI.controller.GameController;
import it.units.sdm.quoridor.GUI.view.dialogs.HelpDialogView;
import it.units.sdm.quoridor.GUI.view.dialogs.NotificationDialogView;
import it.units.sdm.quoridor.GUI.view.dialogs.GameFinishedDialogView;
import it.units.sdm.quoridor.GUI.view.dialogs.QuitDialogView;

import javax.swing.*;

public class DialogManager {
  private final JFrame mainFrame;
  private final GameController gameController;

  public DialogManager(JFrame mainFrame, GameController gameController) {
    this.mainFrame = mainFrame;
    this.gameController = gameController;
  }

  public void displayHelpDialog() {
    new HelpDialogView(mainFrame).displayDialog();
  }

  public void displayConfirmQuitDialog() {
    new QuitDialogView(gameController, mainFrame).displayDialog();
  }

  public void displayGameFinishedDialog() {
    new GameFinishedDialogView(gameController, mainFrame).displayDialog();
  }

  public void displayNotificationDialog(String message, boolean invalidActionFlag) {
    new NotificationDialogView(message, invalidActionFlag, mainFrame).displayDialog();
  }
}