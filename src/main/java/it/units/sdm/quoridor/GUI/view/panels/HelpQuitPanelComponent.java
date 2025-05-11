package it.units.sdm.quoridor.GUI.view.panels;

import it.units.sdm.quoridor.GUI.view.managers.DialogManager;
import it.units.sdm.quoridor.GUI.GUIConstants;

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

    JButton helpButton = new JButton("Help");
    helpButton.addActionListener(e -> dialogManager.displayHelpDialog());

    JButton quitButton = new JButton("Quit");
    quitButton.addActionListener(e -> dialogManager.displayConfirmQuitDialog());

    helpQuitPanel.add(helpButton);
    helpQuitPanel.add(quitButton);
    return helpQuitPanel;
  }
}