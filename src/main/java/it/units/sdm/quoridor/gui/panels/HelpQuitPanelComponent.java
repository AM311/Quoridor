package it.units.sdm.quoridor.gui.panels;

import it.units.sdm.quoridor.gui.DialogManager;
import it.units.sdm.quoridor.gui.GUIConstants;
import it.units.sdm.quoridor.gui.buttons.HelpButton;
import it.units.sdm.quoridor.gui.buttons.QuitButton;

import javax.swing.*;
import java.awt.*;

public class HelpQuitPanelComponent implements PanelComponent {
  private final DialogManager dialogManager;

  public HelpQuitPanelComponent(DialogManager dialogManager) {
    this.dialogManager = dialogManager;
  }

  @Override
  public JPanel createPanel() {
    JPanel helpQuitPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    helpQuitPanel.setBackground(GUIConstants.BACKGROUND_COLOR);

    JButton helpButton = new HelpButton(dialogManager);
    JButton quitButton = new QuitButton(dialogManager);

    helpQuitPanel.add(helpButton);
    helpQuitPanel.add(quitButton);
    return helpQuitPanel;
  }
}