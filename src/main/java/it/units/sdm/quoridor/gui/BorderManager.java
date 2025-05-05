package it.units.sdm.quoridor.gui;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;

public class BorderManager {

  public static final int TOP = 0;
  public static final int LEFT = 1;
  public static final int BOTTOM = 2;
  public static final int RIGHT = 3;

  private final Color[] borderColors;
  private final int[] borderThickness;

  public BorderManager() {
    borderColors = new Color[]{Color.GRAY, Color.GRAY, Color.GRAY, Color.GRAY};
    borderThickness = new int[]{1, 1, 1, 1};
  }

  public void setBorderSide(int side, Color color, int thickness) {
    if (side < 0 || side > 3) {
      throw new IllegalArgumentException("Invalid side index. Use TOP, LEFT, BOTTOM, or RIGHT constants.");
    }
    borderColors[side] = color;
    borderThickness[side] = thickness;
  }

  public void applyTo(JButton button) {
    button.setMargin(new Insets(0, 0, 0, 0));
    button.setBorder(createCustomBorder());
  }

  private AbstractBorder createCustomBorder() {
    return new AbstractBorder() {
      @Override
      public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (borderThickness[TOP] > 0) {
          g2d.setColor(borderColors[TOP]);
          g2d.fillRect(x, y, width, borderThickness[TOP]);
        }

        if (borderThickness[LEFT] > 0) {
          g2d.setColor(borderColors[LEFT]);
          g2d.fillRect(x, y, borderThickness[LEFT], height);
        }

        if (borderThickness[BOTTOM] > 0) {
          g2d.setColor(borderColors[BOTTOM]);
          g2d.fillRect(x, y + height - borderThickness[BOTTOM], width, borderThickness[BOTTOM]);
        }

        if (borderThickness[RIGHT] > 0) {
          g2d.setColor(borderColors[RIGHT]);
          g2d.fillRect(x + width - borderThickness[RIGHT], y, borderThickness[RIGHT], height);
        }

        g2d.dispose();
      }

      @Override
      public Insets getBorderInsets(Component c) {
        return new Insets(
                borderThickness[TOP],
                borderThickness[LEFT],
                borderThickness[BOTTOM],
                borderThickness[RIGHT]
        );
      }

      @Override
      public Insets getBorderInsets(Component c, Insets insets) {
        insets.top = borderThickness[TOP];
        insets.left = borderThickness[LEFT];
        insets.bottom = borderThickness[BOTTOM];
        insets.right = borderThickness[RIGHT];
        return insets;
      }

      @Override
      public boolean isBorderOpaque() {
        return true;
      }
    };
  }
}