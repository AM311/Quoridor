package it.units.sdm.quoridor.GUI.button;

import it.units.sdm.quoridor.GUI.DialogManager;
import it.units.sdm.quoridor.GUI.GUIConstants;
import it.units.sdm.quoridor.GUI.GameBoardGUI;

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
