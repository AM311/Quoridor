package it.units.sdm.quoridor.GUI;

import java.awt.Color;
import java.awt.Font;
import javax.swing.border.Border;
import javax.swing.BorderFactory;


public final class GUIConstants {

  public static final Color BACKGROUND_COLOR = Color.DARK_GRAY;
  public static final Color TEXT_COLOR = Color.ORANGE;
  public static final Color HIGHLIGHT_COLOR = Color.LIGHT_GRAY;
  public static final Color BUTTON_SELECTED_COLOR = Color.GREEN;
  public static final Color WALL_COLOR = Color.BLACK;
  public static final Color POPUP_BACKGROUND = Color.WHITE;
  public static final Color WIN_SCREEN_BACKGROUND = Color.GREEN;

  public static final Font HEADER_FONT = new Font("Arial", Font.BOLD, 24);
  public static final Font NORMAL_FONT = new Font("Arial", Font.PLAIN, 20);
  public static final Font SMALL_FONT = new Font("Arial", Font.PLAIN, 14);
  public static final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 16);

  public static final Border POPUP_BORDER = BorderFactory.createLineBorder(Color.GRAY, 1);

  public static final int ICON_SIZE = 50;
  public static final int VERTICAL_SPACING = 10;
  public static final int BUTTON_WIDTH = 120;
  public static final int BUTTON_HEIGHT = 40;
  public static final int POPUP_DURATION = 3000;


  private GUIConstants() {
    throw new AssertionError("UIConstants class should not be instantiated");
  }
}