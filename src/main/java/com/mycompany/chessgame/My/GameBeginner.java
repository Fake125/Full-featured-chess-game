package com.mycompany.chessgame.My;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

import com.mycompany.chessgame.full_chess.ChessGame;
import com.mycompany.chessgame.full_chess.Player;

public class GameBeginner {

    private JFrame Frame;
    private JComboBox Colors;
    private JLabel SPlayerColor;
    private JTextField FPlayerName;
    private JTextField SPlayerName;

    public GameBeginner() {
        Frame = new JFrame();
        Frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Frame.setVisible(true);
        Dimension Size = new Dimension(400, 200);
        Frame.setLayout(new BorderLayout());
        Frame.setSize(Size);
        Frame.setLocationRelativeTo(null);
        JPanel Content = new JPanel(new GridLayout(2, 3));
        FPlayerName = new JTextField();
        SPlayerName = new JTextField();
        JLabel PlayerName = new JLabel("Player Name");
        SPlayerColor = new JLabel("Black");
        Colors = new JComboBox<String>();
        Colors.addItem("White");
        Colors.addItem("Black");
        Content.add(new JLabel("PlayerName : "));
        Content.add(FPlayerName);
        Content.add(Colors);
        Content.add(new JLabel("PlayerName : "));
        Content.add(SPlayerName);
        Content.add(SPlayerColor);
        Frame.add(BorderLayout.CENTER, Content);
        Frame.add(new JLabel("Starting new Chess game", JLabel.CENTER), BorderLayout.NORTH);
        JButton Start = new JButton("Start");
        Frame.add(Start, BorderLayout.SOUTH);
        Frame.pack();
        Colors.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (Colors.getSelectedIndex() == 0) {
                    SPlayerColor.setText("Black");
                } else {
                    SPlayerColor.setText("White");
                }

            }
        });
        Start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                start();

            }
        });

    }

    public void start() {

        int FPColor = convertStringColorToInt(Colors.getSelectedItem().toString());
        int SPColor = convertStringColorToInt(SPlayerColor.getText());
        Frame.dispatchEvent(new WindowEvent(Frame, WindowEvent.WINDOW_CLOSING));
        SwingView View = new SwingView(FPlayerName.getText(), SPlayerName.getText(), FPColor, SPColor);

    }

    private int convertStringColorToInt(String StringColor) {
        if (StringColor.toLowerCase().equals("white")) {
            return Player.LIGHT_COLOR;
        } else {
            return Player.DARK_COLOR;
        }
    }

    public static void main(String[] args) {
        GameBeginner Starting = new GameBeginner();
    }

}
