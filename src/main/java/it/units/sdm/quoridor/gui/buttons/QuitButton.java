package it.units.sdm.quoridor.gui.buttons;

import it.units.sdm.quoridor.gui.DialogManager;

import javax.swing.*;

public class QuitButton extends JButton {
  private final DialogManager dialogManager;

  public QuitButton(DialogManager dialogManager) {
    this.dialogManager = dialogManager;
    this.setText("Quit");
    this.setActionListener();
  }

  public void setActionListener() {
    this.addActionListener(e -> dialogManager.confirmQuitDialog());
  }
}
