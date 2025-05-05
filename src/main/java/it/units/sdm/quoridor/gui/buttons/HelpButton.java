package it.units.sdm.quoridor.gui.buttons;

import it.units.sdm.quoridor.gui.DialogManager;

import javax.swing.*;

public class HelpButton extends JButton {
  private final DialogManager dialogManager;

  public HelpButton(DialogManager dialogManager) {
    this.dialogManager = dialogManager;
    this.setText("Help");
    this.setActionListener();
  }

  public void setActionListener() {
    this.addActionListener(e -> dialogManager.showHelpDialog());
  }
}
