package View;

import Camera.HoughCirclesRun;
import EiBotBoard.Connection;
import game.Game;
import game.Position;
import org.opencv.core.Core;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GameView extends View implements ActionListener {

    JPanel mainPanel, panelInformation, panelCenter, panelCenterLeft, panelCenterRight;
    JLabel informationLabel, gamcodeTitleLabel, gamecodeLabel;
    JButton scanButton, putButton;
    Color aliceblue = new Color(161, 210, 255);
    Color background = new Color(60,60,60);

    String[] args;
    Game game;
    Connection connection;

    public static void main(String[] args) {
        GameView gameView = new GameView(args, "000", null);
        gameView.setVisible(true);
    }




    public GameView(String[] args, String gameCode, Connection connection) throws HeadlessException {

        this.args = args;
        this.connection = connection;

        View.setUIFont(new FontUIResource(new Font("Roboto", 0, 20)));
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setUndecorated(false);

        //panelInformation
        panelInformation = new JPanel();
        informationLabel = new JLabel("Test");
        informationLabel.setForeground(Color.RED);
        gamcodeTitleLabel = new JLabel("Game:");
        gamcodeTitleLabel.setForeground(aliceblue);
        gamecodeLabel = new JLabel(gameCode);
        gamecodeLabel.setForeground(aliceblue);
        panelInformation.add(gamcodeTitleLabel);
        panelInformation.add(gamecodeLabel);
        panelInformation.add(informationLabel);
        panelInformation.setOpaque(false);


        //panelCenter
        panelCenter = new JPanel();
        panelCenter.setLayout(new BoxLayout(panelCenter, BoxLayout.X_AXIS));
            //left
            panelCenterLeft = new JPanel();
            panelCenterLeft.setOpaque(false);
            JLabel imageLabel = new JLabel();
            ImageIcon imageIcon = new ImageIcon("/Users/andreasgut/Documents/EigenesProjekt/MuehleRoboter/src/main/resources/Spielfeld.png");
            imageLabel.setIcon(imageIcon);
            panelCenterLeft.add(imageLabel);


            //
            panelCenterRight = new JPanel();
            panelCenterRight.setOpaque(false);
            scanButton = new JButton("SCAN");
            scanButton.setPreferredSize(new Dimension(300,300));
            scanButton.addActionListener(this);
            putButton = new JButton("put");
            putButton.addActionListener(this);

            panelCenterRight.add(scanButton);
            panelCenterRight.add(putButton);

        panelCenter.add(panelCenterLeft);
        panelCenter.add(panelCenterRight);
        panelCenter.setOpaque(false);






        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(panelInformation);
        mainPanel.add(panelCenter);
        mainPanel.setBackground(background);
        this.add(mainPanel);

    }


    public void setGame(Game game) {
        this.game = game;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if(e.getSource() == this.scanButton){
            System.out.println("Scan des Spielfelds");
            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
            HoughCirclesRun houghCirclesRun = new HoughCirclesRun();
            if (game.isPutPhase()){
                Position position = houghCirclesRun.detectPut(args, game.getBoard(), 0);
                System.out.println(position);
            }

        }
        if (e.getSource() == this.putButton){
            System.out.println("Put wird ausgeführt");
            connection.put(new Position(0,5), 1);
            connection.move(new Position(0,5), new Position(2,1), false);

        }

    }
}
