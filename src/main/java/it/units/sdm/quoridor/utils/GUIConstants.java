package it.units.sdm.quoridor.utils;

import java.awt.Color;
import java.awt.Font;
import javax.swing.*;
import javax.swing.border.Border;


public final class GUIConstants {
  
  public static final Color BUTTON_BACKGROUND_COLOR = UIManager.getColor("Button.background");
  public static final Color BACKGROUND_COLOR = Color.DARK_GRAY;
  public static final Color TEXT_COLOR = Color.LIGHT_GRAY;
  public static final Color HIGHLIGHT_COLOR = Color.LIGHT_GRAY;
  public static final Color BUTTON_SELECTED_COLOR = Color.GREEN;
  public static final Color WALL_COLOR = Color.BLACK;
  public static final Color SCREEN_BACKGROUND = Color.DARK_GRAY;

  public static final Font HEADER_FONT = new Font("Arial", Font.BOLD, 24);
  public static final Font NORMAL_FONT = new Font("Arial", Font.PLAIN, 20);
  public static final Font SMALL_FONT = new Font("Arial", Font.PLAIN, 14);
  public static final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 16);

  public static final Border POPUP_BORDER = BorderFactory.createLineBorder(Color.GRAY, 1);

  public static final int ICON_SIZE = 50;
  public static final int VERTICAL_SPACING = 10;
  public static final int BUTTON_WIDTH = 120;
  public static final int BUTTON_HEIGHT = 40;

  public static final String INSTRUCTION = "<html><div style='width: 300px; text-align: left;'>" +
          "<h3>How to play Quoridor:</h3>" +
          "<ul>" +
          "<li>On your round you can move your pawn or place a wall</li>" +
          "<li>You win by reaching the opposite side of the board</li>" +
          "<li>Walls can be placed vertically or horizontally</li>" +
          "<li>You cannot completely block a path to destination</li>" +
          "</ul></div></html>";
  public static final String WALL_CONVENTION = "<html><div style='width: 300px; text-align: left;'>" +
          "The chosen tile is in the bottom-left.<br>" +
          "Walls are represented with black lines and they occupy two tiles:<br>" +
          "<ul>" +
          "<li>Vertical walls appear on the left side of tiles<br>" +
          "<li>Horizontal walls appear on the bottom side of tiles" +
          "</ul>" +
          "You cannot place walls on the margins</div></html>";


  private GUIConstants() {
    throw new AssertionError("UIConstants class should not be instantiated");
  }
}