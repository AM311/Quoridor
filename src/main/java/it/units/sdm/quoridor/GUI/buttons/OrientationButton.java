package it.units.sdm.quoridor.GUI.buttons;

import it.units.sdm.quoridor.GUI.GUIConstants;
import it.units.sdm.quoridor.GUI.GameBoardGUI;
import it.units.sdm.quoridor.utils.WallOrientation;

import javax.swing.*;

public class OrientationButton extends JButton {
  private final GameBoardGUI gameBoardGUI;
  private final WallOrientation wallOrientation;

  public OrientationButton(GameBoardGUI gameBoardGUI, WallOrientation orientation) {
    this.gameBoardGUI = gameBoardGUI;
    this.wallOrientation = orientation;
    this.setText(orientation.toString().substring(0, 1).toUpperCase() + orientation.toString().substring(1).toLowerCase());
  }

  public void setActionListener(JButton oppositeButton) {
    this.addActionListener(e -> {
      this.setBackground(GUIConstants.BUTTON_SELECTED_COLOR);
      oppositeButton.setBackground(GUIConstants.BUTTON_BACKGROUND_COLOR);
      gameBoardGUI.setCurrentAction(
              wallOrientation.equals(WallOrientation.VERTICAL) ? GameBoardGUI.Action.PLACE_VERTICAL_WALL : GameBoardGUI.Action.PLACE_HORIZONTAL_WALL
      );
    });
  }
}
