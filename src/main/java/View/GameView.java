package View;

import Camera.HoughCirclesRun;
import Camera.InvalidBoardException;
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

    ViewManager viewManager;

    JPanel mainPanel, panelInformation, panelInformationTop, panelInformationCenter, panelInformationBottom, panelCenter, panelCenterLeft, panelCenterRight;
    JLabel informationLabel, gamcodeTitleLabel, gamecodeLabel, nameTitleLabel, nameLabel, roundTitleLabel, roundLabel, enemyTitleLabel, enemyLabel;
    JButton scanButton, putButton, putButton2, panicButton;
    JTextField fieldTextfield, ringTextfield, ringTextfield2, fieldTextfield2;
    BoardImage boardImage;
    Color aliceblue = new Color(161, 210, 255);
    Color background = new Color(60,60,60);
    STONECOLOR player0StoneColor;
    STONECOLOR player1StoneColor;

    String[] args;
    Game game;
    Connection connection;
    WebsocketClient websocketClient;

    public static void main(String[] args) {

        ViewManager viewManager = new ViewManager();
        GameView gameView = new GameView(viewManager, args, "000", "Peter", null, STONECOLOR.BLACK, STONECOLOR.WHITE);
        viewManager.setCurrentView(gameView);
        gameView.setVisible(true);
    }




    public GameView(ViewManager viewManager, String[] args, String gameCode, String name, Connection connection, STONECOLOR player0StoneColor, STONECOLOR player1StoneColor) throws HeadlessException {

        this.viewManager = viewManager;
        this.args = args;
        this.connection = connection;
        this.player0StoneColor = player0StoneColor;
        this.player1StoneColor = player1StoneColor;

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
            roundTitleLabel = new JLabel(" / Runde: ");
            roundTitleLabel.setForeground(aliceblue);
            roundLabel = new JLabel("0");
            roundLabel.setForeground(aliceblue);


            panelInformationCenter.add(gamcodeTitleLabel);
            panelInformationCenter.add(gamecodeLabel);
            panelInformationCenter.add(roundTitleLabel);
            panelInformationCenter.add(roundLabel);
            panelInformationCenter.setOpaque(false);

            //panelInformation Bottom
            panelInformationBottom = new JPanel();
            panelInformationBottom.setLayout(new BoxLayout(panelInformationBottom, BoxLayout.X_AXIS));
            nameTitleLabel = new JLabel("Name: ");
            nameTitleLabel.setForeground(aliceblue);
            nameLabel = new JLabel(name);
            nameLabel.setForeground(aliceblue);
            enemyTitleLabel = new JLabel(" / Gegner: ");
            enemyTitleLabel.setForeground(aliceblue);
            enemyLabel = new JLabel("---");
            enemyLabel.setForeground(aliceblue);

            panelInformationBottom.add(nameTitleLabel);
            panelInformationBottom.add(nameLabel);
            panelInformationBottom.add(enemyTitleLabel);
            panelInformationBottom.add(enemyLabel);
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

            putButton2 = new JButton("put");
            putButton2.addActionListener(this);
            ringTextfield2 = new JTextField();
            fieldTextfield2 = new JTextField();


            JPanel putPanel2 = new JPanel();
            putPanel2.setLayout(new BoxLayout(putPanel2, BoxLayout.Y_AXIS));
            putPanel2.add(ringTextfield2);
            putPanel2.add(fieldTextfield2);
            putPanel2.add(putButton2);

            panicButton = new JButton("Panic");
            panicButton.addActionListener(this);

            panelCenterRight.add(scanButton);
            panelCenterRight.add(putPanel);
            panelCenterRight.add(putPanel2);
            panelCenterRight.add(panicButton);






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

    public BoardImage getBoardImage() {
        return boardImage;
    }

    public STONECOLOR getPlayer0StoneColor() {
        return player0StoneColor;
    }

    public STONECOLOR getPlayer1StoneColor() {
        return player1StoneColor;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if(e.getSource() == this.scanButton){
            System.out.println("Scan des Spielfelds");
            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
            HoughCirclesRun houghCirclesRun = new HoughCirclesRun(game.getBoard());
            if (game.isPutPhase()){

                int playerIndex = 0;
                STONECOLOR stonecolor = STONECOLOR.BLACK;


                try {
                    Position position = houghCirclesRun.detectPut(args, game.getBoard());
                    sendPutMessage(position);
                    game.getBoard().putStone(position, playerIndex);
                    putOnBoardImage(position, stonecolor);

                    game.increaseRound();
                    increaseRoundLabel();
                    setInformationLabel(" ");
                }
                catch (InvalidBoardException ibe){
                    setInformationLabel(ibe.getMessage());
                }



            }
        }

        if (e.getSource() == this.putButton){

            /*boardImage.put(new Position(0,0), STONECOLOR.WHITE);
            boardImage.move(new Move(new Position(0,0), new Position(0,1)));
            boardImage.put(new Position(2,3), STONECOLOR.BLACK);
            boardImage.put(new Position(2,1), STONECOLOR.BLACK);
            boardImage.put(new Position(2,2), STONECOLOR.BLACK);*/




            //connection.put(new Position(Integer.parseInt(ringTextfield.getText()), Integer.parseInt(fieldTextfield.getText())), 1);

            int ring = Integer.parseInt(ringTextfield.getText());
            int field = Integer.parseInt(fieldTextfield.getText());

            JSONObject jsonObject2 = new JSONObject();
            jsonObject2.put("playerUuid", game.getPlayer0().getUuid());
            jsonObject2.put("gameCode", game.getGameCode());
            jsonObject2.put("command", "update");
            jsonObject2.put("action", "put");
            jsonObject2.put("ring", ringTextfield.getText());
            jsonObject2.put("field", fieldTextfield.getText());
            jsonObject2.put("callComputer", false);
            websocketClient.send(jsonObject2.toString());

            Position position = new Position(ring,field);
            int playerIndex = 0;

            game.getBoard().putStone(position, playerIndex);

            boardImage.put(position, STONECOLOR.BLACK);
            this.getContentPane().validate();
            this.getContentPane().repaint();


            /*System.out.println("Put wird ausgeführt");
            connection.put(new Position(0,5), 1);
            connection.move(new Position(0,5), new Position(2,1), false);*/

        }

        if (e.getSource() == this.putButton2){
            connection.put(new Position(Integer.parseInt(ringTextfield2.getText()), Integer.parseInt(fieldTextfield2.getText())),1);
        }

        if (e.getSource() == this.panicButton){
            connection.getEbb().enableMotor(0,0);
        }

    }

    private void sendPutMessage(Position position){

        JSONObject jsonObject2 = new JSONObject();
        jsonObject2.put("playerUuid", game.getPlayer0().getUuid());
        jsonObject2.put("gameCode", game.getGameCode());
        jsonObject2.put("command", "update");
        jsonObject2.put("action", "put");
        jsonObject2.put("ring", position.getRing());
        jsonObject2.put("field", position.getField());
        jsonObject2.put("callComputer", false);
        websocketClient.send(jsonObject2.toString());
        System.out.println(jsonObject2);
    }

    private void putOnBoardImage(Position position, STONECOLOR stonecolor){

        boardImage.put(position, stonecolor);
        this.getContentPane().validate();
        this.getContentPane().repaint();
    }

    public void increaseRoundLabel(){
        roundLabel.setText(String.valueOf(game.getRound()));
    }

    public void setEnemyLabel(String enemyName){
        enemyLabel.setText(enemyName);
    }

    public void setInformationLabel(String information){
        informationLabel.setText(information);
    }
}
