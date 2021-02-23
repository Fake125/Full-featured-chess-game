package com.mycompany.chessgame.My;

import com.mycompany.chessgame.full_chess.ChessGameView;
import com.mycompany.chessgame.full_chess.Position;
import com.mycompany.chessgame.full_chess.ChessGame;
import java.awt.*;
import java.awt.Color;
import java.awt.event.*;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import com.mycompany.chessgame.full_chess.PieceInfo;
import javax.swing.*;


public class SwingView extends ChessGameView implements MouseListener {

    private JLayeredPane layeredPane;
    private JPanel chessBoard;
    private int DraggedPiece_Player;
    private int DraggedPieceNo;
    private int DraggedPiece_NextCell;
    private int PressCounter = 1;
    private JPanel FromCell;
    private JPanel ToCell;
    private Color OriginalColorOfFromCell;
    private JLabel Turn;
    private JFrame Frame;
    private ChessGame Model;

    public SwingView(String FirstPlayerName, String SecondPlayerName, int FirstPlayerColor, int SecondPlayerColor) {
        Model = new ChessGame(FirstPlayerName, SecondPlayerName, FirstPlayerColor, SecondPlayerColor);
        Frame = new JFrame();
        Frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Frame.setResizable(false);
        Frame.setVisible(true);
        Dimension boardSize = new Dimension(500, 500);
        layeredPane = new JLayeredPane();
        layeredPane.setLayout(new BorderLayout());
        Frame.add(layeredPane);
        layeredPane.addMouseListener(this);
        Turn = new JLabel("Turn");
        Turn.setHorizontalAlignment(JTextField.CENTER);

        chessBoard = new JPanel();
        chessBoard.setLayout(new GridLayout(8, 8));
        Dimension PanelSize = new Dimension(boardSize.width, boardSize.height);
        chessBoard.setPreferredSize(PanelSize);
        chessBoard.setBounds(0, 0, PanelSize.width, PanelSize.height);
        Color Brown = new Color(142, 109, 94);
        Color sandy = new Color(245, 217, 195);
        // adding chess board cells
        for (int i = 0; i < 64; i++) {
            JPanel square = new JPanel(new BorderLayout());
            chessBoard.add(square);
            int row = (i / 8) % 2;
            if (row == 0) {
                square.setBackground(i % 2 == 0 ? Brown : sandy);
            } else {
                square.setBackground(i % 2 == 0 ? sandy : Brown);
            }

        }

        layeredPane.add(chessBoard, 2);
        layeredPane.add(Turn, BorderLayout.SOUTH);
        Frame.pack();
        Frame.setLocationRelativeTo(null);
        setupChessBoard();
        Model.setView(this);
        getUpdated();
    }

    private void getUpdated() {
        String checked = Model.getCheckedPlayer();
        String turn = Model.getActivePlayerName();
        if (checked.length() > 0) {
            if (Model.getTheWinner().length() > 0) {
                int ok = JOptionPane.showConfirmDialog(Frame, Model.getTheWinner() + " won", "Game is Ended", JOptionPane.DEFAULT_OPTION);
                if (ok == 0) {
                    System.exit(-1);
                }

            }
        }
        String message = "Turn: " + turn + " " + checked;
        Turn.setText(message);
    }

    private int getCellNo(Component c) {
        int counter = 0;
        for (Component component : chessBoard.getComponents()) {
            if (component.equals(c)) {
                return counter;
            }
            counter++;
        }
        return counter;

    }

    public void mousePressed(MouseEvent e) {
        Component c = chessBoard.findComponentAt(e.getX(), e.getY());
        if (c == null) {
            return;
        }
        JPanel CellPanel = null;
        if (c.getParent().getParent() instanceof JLayeredPane) {
            if (PressCounter == 1) {
                return;
            }
            CellPanel = (JPanel) c;
        } else if (c.getParent().getParent() instanceof JPanel) {
            CellPanel = (JPanel) c.getParent();
        }

        if (PressCounter == 1) {
            FromCell = CellPanel;
            String MovedPiece[] = c.getName().split(":");
            DraggedPiece_Player = Integer.valueOf(MovedPiece[0]);
            if (!Model.checkTheTurn(DraggedPiece_Player)) {
                return;
            }
            DraggedPieceNo = Integer.valueOf(MovedPiece[1]);
            OriginalColorOfFromCell = CellPanel.getBackground();
            CellPanel.setBackground(Color.green);
        } else if (PressCounter == 2) {
            ToCell = CellPanel;
            if (ToCell.equals(FromCell)) {
                FromCell.setBackground(OriginalColorOfFromCell);
                PressCounter = 1;
                return;
            }
            PressCounter = 0;
            FromCell.setBackground(OriginalColorOfFromCell);
            int NextPosition = getCellNo(ToCell);

            boolean MoveConfirm = Model.handleDraggedPiece(DraggedPiece_Player,DraggedPieceNo, CalculatePositionForCellNo(NextPosition));
            if (MoveConfirm) {
                JLabel Piece = (JLabel) FromCell.getComponent(0);
                FromCell.remove(0);

                if (ToCell.getComponentCount() > 0) {
                    ToCell.remove(0);
                }
                ToCell.repaint();
                FromCell.repaint();
                ToCell.add(Piece);
                getUpdated();
            }
        }

        PressCounter++;

    }

    public void mouseReleased(MouseEvent e) {

    }

    public void mouseClicked(MouseEvent e) {

    }

    public void mouseMoved(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void changePiecePositionOnBoard(Position PositionBefore, Position PositionAfter) {
        JPanel FromCell = (JPanel) chessBoard.getComponent(CalculateCellNoForPosition(PositionBefore));
        JLabel PieceLabel = (JLabel) FromCell.getComponent(0);
        JPanel ToCell = (JPanel) chessBoard.getComponent(CalculateCellNoForPosition(PositionAfter));
        FromCell.remove(0);
        ToCell.add(PieceLabel);
        ToCell.repaint();
        FromCell.repaint();
    }

    @Override
    public String promptPromotionOptions(Position PiecePosition) {
        Object[] options = {"Queen", "Knight", "Rook", "Bishop"};
        Object selectionObject = JOptionPane.showInputDialog(Frame, "Choose", "Piece promotion",
                JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        String selected = selectionObject.toString().toLowerCase();
        JPanel Cell = (JPanel) chessBoard.getComponent(CalculateCellNoForPosition(PiecePosition));
        JLabel PieceLabel = (JLabel) Cell.getComponent(0);
        String PlayerColor = (Model.getActivePlayerColor().equals("LIGHT")) ? "w" : "b";
        PieceLabel.setIcon(getPieceImage(selected, PlayerColor));
        return selected;
    }

    private ImageIcon getPieceImage(String PieceName, String Color) {
        String Path = "./PiecesImages/";
        Path += PieceName.toLowerCase();
        Path += "_" + Color + ".png";
        return new ImageIcon(Path);
    }

    private void setupChessBoard() {
        ArrayList<PieceInfo> FirstPlayerPieces = Model.getPlayerPieces(ChessGame.FIRSTPLAYER);
        ArrayList<PieceInfo> SecondPlayerPieces = Model.getPlayerPieces(ChessGame.SECONDPLAYER);
        String ColorChar = (Model.getPlayerColor(ChessGame.FIRSTPLAYER)).equals("DARK") ? "b" : "w";
        String fpColorChar = "";
        String spColorChar = "";
        if (Model.getPlayerColor(ChessGame.FIRSTPLAYER).equals("DARK")) {
            fpColorChar = "b";
            spColorChar = "w";
        } else {
            fpColorChar = "w";
            spColorChar = "b";
        }
        addPiecesListToBoard(FirstPlayerPieces, ChessGame.FIRSTPLAYER, fpColorChar);
        addPiecesListToBoard(SecondPlayerPieces, ChessGame.SECONDPLAYER, spColorChar);
    }

    private void addPiecesListToBoard(ArrayList<PieceInfo> PiecesList, int Player, String ColorChar) {
        for (PieceInfo piece : PiecesList) {
            JLabel pieceLabel = new JLabel("");
            pieceLabel.setIcon(getPieceImage(piece.getType(), ColorChar));
            JPanel panel = (JPanel) chessBoard.getComponent(CalculateCellNoForPosition(piece.getPosition()));
            pieceLabel.setName(Player + ":" + piece.getIndex());
            panel.add(pieceLabel);
        }
    }

    private int CalculateCellNoForPosition(Position positon) {
        int formula = ((positon.getRow() - 1) * 8 + positon.getColumn()) - 1;
        return formula;
    }

    private Position CalculatePositionForCellNo(int CellNo) {
        int row = (int) Math.ceil((double) (CellNo + 1) / 8.0);
        int column = (CellNo + 1) % 8;
        if (column == 0) {
            column = 8;
        }
        Position ThePosition = new Position(column, row);
        return (ThePosition);
    }

}
