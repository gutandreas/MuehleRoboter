package View;

import Camera.HoughCirclesRun;
import EiBotBoard.Connection;
import Websocket.WebsocketClient;
import game.Game;
import game.Move;
import game.Position;
import game.STONECOLOR;
import org.json.JSONObject;
import org.opencv.core.Core;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameView extends View implements ActionListener {

    JPanel mainPanel, panelInformation, panelInformationTop, panelInformationCenter, panelInformationBottom, panelCenter, panelCenterLeft, panelCenterRight;
    JLabel informationLabel, gamcodeTitleLabel, gamecodeLabel, nameTitleLabel, nameLabel;
    JButton scanButton, putButton;
    JTextField fieldTextfield, ringTextfield;
    BoardImage boardImage;
    Color aliceblue = new Color(161, 210, 255);
    Color background = new Color(60,60,60);

    String[] args;
    Game game;
    Connection connection;
    WebsocketClient websocketClient;

    public static void main(String[] args) {
        GameView gameView = new GameView(args, "000", "Peter", null);
        gameView.setVisible(true);
    }




    public GameView(String[] args, String gameCode, String name, Connection connection) throws HeadlessException {

        this.args = args;
        this.connection = connection;

        View.setUIFont(new FontUIResource(new Font("Roboto", 0, 20)));
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setUndecorated(false);

        //panelInformation
        panelInformation = new JPanel();
        panelInformation.setLayout(new BoxLayout(panelInformation, BoxLayout.Y_AXIS));

            //panelInformationTop
            panelInformationTop = new JPanel();
            panelInformationTop.setLayout(new BoxLayout(panelInformationTop, BoxLayout.X_AXIS));
            informationLabel = new JLabel("BoardImage");
            informationLabel.setForeground(Color.RED);
            panelInformationTop.add(informationLabel);
            panelInformationTop.setOpaque(false);


            //panelInformationCenter
            panelInformationCenter = new JPanel();
            panelInformationCenter.setLayout(new BoxLayout(panelInformationCenter, BoxLayout.X_AXIS));
            gamcodeTitleLabel = new JLabel("Game: ");
            gamcodeTitleLabel.setForeground(aliceblue);
            gamecodeLabel = new JLabel(gameCode);
            gamecodeLabel.setForeground(aliceblue);
            panelInformationCenter.add(gamcodeTitleLabel);
            panelInformationCenter.add(gamecodeLabel);
            panelInformationCenter.setOpaque(false);

            //panelInformation Bottom
            panelInformationBottom = new JPanel();
            panelInformationBottom.setLayout(new BoxLayout(panelInformationBottom, BoxLayout.X_AXIS));
            nameTitleLabel = new JLabel("Name: ");
            nameTitleLabel.setForeground(aliceblue);
            nameLabel = new JLabel(name);
            nameLabel.setForeground(aliceblue);
            panelInformationBottom.add(nameTitleLabel);
            panelInformationBottom.add(nameLabel);
            panelInformationBottom.setOpaque(false);


        panelInformation.add(panelInformationTop);
        panelInformation.add(panelInformationCenter);
        panelInformation.add(panelInformationBottom);
        panelInformation.setOpaque(false);


        //panelCenter
        panelCenter = new JPanel();
        panelCenter.setLayout(new BoxLayout(panelCenter, BoxLayout.X_AXIS));
            //left
            panelCenterLeft = new JPanel();
            panelCenterLeft.setOpaque(false);
            boardImage = new BoardImage();







            panelCenterLeft.add(boardImage.getMainLabel());


            //
            panelCenterRight = new JPanel();
            panelCenterRight.setOpaque(false);
            scanButton = new JButton("SCAN");
            scanButton.setPreferredSize(new Dimension(300,300));
            scanButton.addActionListener(this);
            putButton = new JButton("put");
            putButton.addActionListener(this);

            ringTextfield = new JTextField();
            fieldTextfield = new JTextField();


            JPanel putPanel = new JPanel();
            putPanel.setLayout(new BoxLayout(putPanel, BoxLayout.Y_AXIS));
            putPanel.add(ringTextfield);
            putPanel.add(fieldTextfield);
            putPanel.add(putButton);

            panelCenterRight.add(scanButton);
            panelCenterRight.add(putPanel);


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

    public void setWebsocketClient(WebsocketClient websocketClient) {
        this.websocketClient = websocketClient;
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

            /*boardImage.put(new Position(0,0), STONECOLOR.WHITE);
            boardImage.move(new Move(new Position(0,0), new Position(0,1)));
            boardImage.put(new Position(2,3), STONECOLOR.BLACK);
            boardImage.put(new Position(2,1), STONECOLOR.BLACK);
            boardImage.put(new Position(2,2), STONECOLOR.BLACK);*/




            //connection.put(new Position(Integer.parseInt(ringTextfield.getText()), Integer.parseInt(fieldTextfield.getText())), 1);

            JSONObject jsonObject2 = new JSONObject();
            jsonObject2.put("playerUuid", game.getPlayer0().getUuid());
            jsonObject2.put("gameCode", game.getGameCode());
            jsonObject2.put("command", "update");
            jsonObject2.put("action", "put");
            jsonObject2.put("ring", ringTextfield.getText());
            jsonObject2.put("field", fieldTextfield.getText());
            jsonObject2.put("callComputer", false);
            websocketClient.send(jsonObject2.toString());

            boardImage.put(new Position(Integer.parseInt(ringTextfield.getText()), Integer.parseInt(fieldTextfield.getText())), STONECOLOR.BLACK );
            this.getContentPane().validate();
            this.getContentPane().repaint();


            /*System.out.println("Put wird ausgeführt");
            connection.put(new Position(0,5), 1);
            connection.move(new Position(0,5), new Position(2,1), false);*/

        }

    }
}
