package it.units.sdm.quoridor.GUI;

import it.units.sdm.quoridor.model.AbstractGame;
import it.units.sdm.quoridor.utils.Position;

import javax.swing.*;
import java.awt.*;


public class GameGUI {

  private final int numberOfPlayers;
  private final AbstractGame game;
  private GameBoardPanel gameBoardPanel;
  private JPanel controlPanel;

  public GameGUI(int numberOfPlayers, AbstractGame game) {
    this.numberOfPlayers = numberOfPlayers;
    this.game = game;
  }


  public void initializeAndShowUI() {
    JFrame mainFrame = new JFrame("Quoridor");
    mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    mainFrame.setSize(800, 600);

    JPanel rootPanel = createRootPanel();
    mainFrame.setContentPane(rootPanel);
    mainFrame.setVisible(true);
  }

  private JPanel createRootPanel() {
    JPanel rootPanel = new JPanel(new BorderLayout());

    Position[] pawnPositions = createInitialPawnPositions();

    gameBoardPanel = new GameBoardPanel(game, pawnPositions);
    rootPanel.add(gameBoardPanel, BorderLayout.CENTER);

    controlPanel = new JPanel(new FlowLayout());
    JLabel player1Label = new JLabel("Player 1");
    JLabel player2Label = new JLabel("Player 2");
    controlPanel.add(player1Label);
    controlPanel.add(player2Label);
    rootPanel.add(controlPanel, BorderLayout.SOUTH);

    showActionButtonsForPlayer();

    return rootPanel;
  }

  private Position[] createInitialPawnPositions() {
    Position[] positions = new Position[numberOfPlayers];
    positions[0] = new Position(0, 4);
    positions[1] = new Position(8, 4);

    return positions;
  }

  private void showActionButtonsForPlayer() {
    JPanel actionPanel = new JPanel();
    actionPanel.setLayout(new FlowLayout());

    JButton moveButton = new JButton("Move");
    moveButton.addActionListener(e -> gameBoardPanel.setCurrentAction(GameBoardPanel.Action.MOVE));

    JButton hWallButton = new JButton("Place Horizontal Wall");
    hWallButton.addActionListener(e -> gameBoardPanel.setCurrentAction(GameBoardPanel.Action.PLACE_HORIZONTAL_WALL));

    JButton vWallButton = new JButton("Place Vertical Wall");
    vWallButton.addActionListener(e -> gameBoardPanel.setCurrentAction(GameBoardPanel.Action.PLACE_VERTICAL_WALL));

    actionPanel.add(moveButton);
    actionPanel.add(hWallButton);
    actionPanel.add(vWallButton);

    controlPanel.removeAll();
    controlPanel.add(actionPanel);
    controlPanel.revalidate();
    controlPanel.repaint();
  }
}