package it.units.sdm.quoridor.view.gui.dialogs;

import it.units.sdm.quoridor.utils.GUIConstants;
import it.units.sdm.quoridor.view.gui.managers.BorderManager;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class HelpDialogView implements DialogView {
  private final JFrame mainFrame;


  public HelpDialogView(JFrame mainFrame) {
    this.mainFrame = mainFrame;
  }

  @Override
  public void displayDialog() {
    JPanel helpPanel = createDialogContent();

    JOptionPane.showMessageDialog(
            mainFrame,
            helpPanel,
            "Quoridor Help",
            JOptionPane.INFORMATION_MESSAGE
    );
  }


  private JPanel createDialogContent() {
    JPanel helpPanel = new JPanel();
    helpPanel.setLayout(new BoxLayout(helpPanel, BoxLayout.Y_AXIS));

    JLabel instructionsLabel = getInstructionsLabel();
    helpPanel.add(instructionsLabel);

    helpPanel.add(Box.createVerticalStrut(GUIConstants.VERTICAL_SPACING));
    helpPanel.add(new JSeparator());
    helpPanel.add(Box.createVerticalStrut(GUIConstants.VERTICAL_SPACING));

    JLabel wallExplanationTitle = new JLabel("Wall Placement Convention:");
    wallExplanationTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
    wallExplanationTitle.setFont(GUIConstants.SMALL_FONT);
    helpPanel.add(wallExplanationTitle);
    helpPanel.add(Box.createVerticalStrut(GUIConstants.VERTICAL_SPACING));

    JPanel visualPanel = getVisualPanel();

    JPanel visualWrapperPanel = new JPanel();
    visualWrapperPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
    visualWrapperPanel.add(visualPanel);
    helpPanel.add(visualWrapperPanel);

    JLabel explanationLabel = getWallConventionLabel();
    helpPanel.add(Box.createVerticalStrut(10));
    helpPanel.add(explanationLabel);

    return helpPanel;
  }

  private JPanel getVisualPanel() {
    JPanel visualPanel = new JPanel(new GridLayout(2, 2, 0, 0));
    visualPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
    visualPanel.setMaximumSize(new Dimension(200, 200));

    JButton topLeft = new JButton();
    JButton topRight = new JButton();
    JButton bottomLeft = new JButton();
    JButton bottomRight = new JButton();

    bottomLeft.setBackground(GUIConstants.BUTTON_SELECTED_COLOR);

    Dimension buttonSize = new Dimension(60, 60);
    topLeft.setPreferredSize(buttonSize);
    topRight.setPreferredSize(buttonSize);
    bottomLeft.setPreferredSize(buttonSize);
    bottomRight.setPreferredSize(buttonSize);

    Map<JButton, BorderManager> borderManagers = new HashMap<>();

    BorderManager blBorder = borderManagers.computeIfAbsent(bottomLeft, k -> new BorderManager());
    blBorder.setBorderSide(BorderManager.LEFT, GUIConstants.WALL_COLOR, 5);
    blBorder.applyTo(bottomLeft);

    BorderManager tlBorder = borderManagers.computeIfAbsent(topLeft, k -> new BorderManager());
    tlBorder.setBorderSide(BorderManager.LEFT, GUIConstants.WALL_COLOR, 5);
    tlBorder.applyTo(topLeft);

    BorderManager brBorder = borderManagers.computeIfAbsent(bottomRight, k -> new BorderManager());
    brBorder.setBorderSide(BorderManager.BOTTOM, GUIConstants.WALL_COLOR, 5);
    brBorder.applyTo(bottomRight);

    BorderManager trBorder = new BorderManager();
    trBorder.applyTo(topRight);

    BorderManager blBottomBorder = borderManagers.computeIfAbsent(bottomLeft, k -> new BorderManager());
    blBottomBorder.setBorderSide(BorderManager.BOTTOM, GUIConstants.WALL_COLOR, 5);
    blBottomBorder.applyTo(bottomLeft);

    visualPanel.add(topLeft);
    visualPanel.add(topRight);
    visualPanel.add(bottomLeft);
    visualPanel.add(bottomRight);
    return visualPanel;
  }

  private JLabel getWallConventionLabel() {
    JLabel explanationLabel = new JLabel(GUIConstants.WALL_CONVENTION);
    explanationLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    return explanationLabel;
  }

  private JLabel getInstructionsLabel() {
    JLabel instructionsLabel = new JLabel(GUIConstants.INSTRUCTION);
    instructionsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    return instructionsLabel;
  }
}
