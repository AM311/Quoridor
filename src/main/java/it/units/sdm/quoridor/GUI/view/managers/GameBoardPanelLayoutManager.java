package it.units.sdm.quoridor.GUI.view.managers;

import java.awt.*;

public class GameBoardPanelLayoutManager implements LayoutManager {

  @Override
  public void addLayoutComponent(String name, Component comp) {

  }

  @Override
  public void removeLayoutComponent(Component comp) {

  }

  @Override
  public Dimension preferredLayoutSize(Container parent) {
    return new Dimension(400, 400);
  }

  @Override
  public Dimension minimumLayoutSize(Container parent) {
    return new Dimension(100, 100);
  }

  @Override
  public void layoutContainer(Container parent) {
    if (parent.getComponentCount() == 0) return;

    Component comp = parent.getComponent(0);
    int width = parent.getWidth();
    int height = parent.getHeight();
    int size = Math.min(width, height);
    int x = (width - size) / 2;
    int y = (height - size) / 2;

    comp.setBounds(x, y, size, size);
  }
}