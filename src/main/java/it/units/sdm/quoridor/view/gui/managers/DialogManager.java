package it.units.sdm.quoridor.view.gui.managers;

import it.units.sdm.quoridor.controller.engine.gui.GUIQuoridorGameEngine;
import it.units.sdm.quoridor.view.gui.dialogs.*;

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

  public void displayStatisticsDialog() {
    new StatisticsDialogView(gameEngine, mainFrame).displayDialog();
  }

  public void displayQuitRestartDialog() {
    new QuitRestartDialogView(gameEngine, mainFrame).displayDialog();
  }

  public void displayNotificationDialog(String message, boolean invalidActionFlag) {
    new NotificationDialogView(message, invalidActionFlag, mainFrame).displayDialog();
  }
}