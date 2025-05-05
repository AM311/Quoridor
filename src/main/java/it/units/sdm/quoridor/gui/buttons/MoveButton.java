package it.units.sdm.quoridor.gui.buttons;

import it.units.sdm.quoridor.gui.DialogManager;
import it.units.sdm.quoridor.gui.GUIConstants;
import it.units.sdm.quoridor.gui.GameBoardGUI;

import javax.swing.*;

public class MoveButton extends JButton {
  private final GameBoardGUI gameBoardGUI;
  private final DialogManager dialogManager;

  public MoveButton(GameBoardGUI gameBoardGUI, DialogManager dialogManager) {
    this.gameBoardGUI = gameBoardGUI;
    this.dialogManager = dialogManager;
    this.setText("Move");
    this.setActionListener();
  }

  public void setActionListener() {
    this.addActionListener(e -> {
      gameBoardGUI.setCurrentAction(GameBoardGUI.Action.MOVE);
      this.setBackground(GUIConstants.BUTTON_SELECTED_COLOR);
      try {
        gameBoardGUI.highlightValidMoves();
      } catch (Exception ex) {
        dialogManager.showErrorDialog("Error highlighting moves: " + ex.getMessage());
      }
    });
  }
}
