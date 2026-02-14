package it.units.sdm.quoridor.utils;

import javax.swing.*;
import java.awt.*;

public class FlatColorButton extends JButton {
  public FlatColorButton() {
    super();
    setContentAreaFilled(false);
    setFocusPainted(false);
    setOpaque(false);
  }

  @Override
  protected void paintComponent(Graphics g) {
    Graphics2D g2 = (Graphics2D) g.create();
    g2.setColor(getBackground());
    g2.fillRect(0, 0, getWidth(), getHeight());
    g2.dispose();

    super.paintComponent(g);
  }
}
