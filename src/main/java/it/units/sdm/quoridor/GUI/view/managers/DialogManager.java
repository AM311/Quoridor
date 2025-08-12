package it.units.sdm.quoridor.GUI.view.managers;

import it.units.sdm.quoridor.GUI.view.dialogs.HelpDialogView;
import it.units.sdm.quoridor.GUI.view.dialogs.NotificationDialogView;
import it.units.sdm.quoridor.GUI.view.dialogs.GameFinishedDialogView;
import it.units.sdm.quoridor.GUI.view.dialogs.QuitDialogView;
import it.units.sdm.quoridor.cli.engine.GUIQuoridorGameEngine;

import javax.swing.*;

public class DialogManager {
  private final JFrame mainFrame;
  private final GUIQuoridorGameEngine gameEngine;

  public DialogManager(JFrame mainFrame, GUIQuoridorGameEngine gameEngine) {
    this.mainFrame = mainFrame;
    this.gameEngine = gameEngine;
  }

  public void displayHelpDialog() {
    new HelpDialogView(mainFrame).displayDialog();
  }

  public void displayConfirmQuitDialog() {
    new QuitDialogView(gameEngine, mainFrame).displayDialog();
  }

  public void displayGameFinishedDialog() {
    new GameFinishedDialogView(gameEngine, mainFrame).displayDialog();
  }

  public void displayNotificationDialog(String message, boolean invalidActionFlag) {
    new NotificationDialogView(message, invalidActionFlag, mainFrame).displayDialog();
  }
}