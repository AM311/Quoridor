package it.units.sdm.quoridor.GUI.button;

import it.units.sdm.quoridor.GUI.DialogManager;

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
