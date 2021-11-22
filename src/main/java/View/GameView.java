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
    JLabel informationLabel, gamcodeTitleLabel, gamecodeLabel, nameTitleLabel, nameLabel, roundTitleLabel, roundLabel, enemyTitleLabel, enemyLabel, nextStepLabel;
    JButton scanButton, putButton, putButton2, panicButton;
    JTextField fieldTextfield, ringTextfield, ringTextfield2, fieldTextfield2;
    BoardImage boardImage;
    Color aliceblue = new Color(161, 210, 255);
    Color background = new Color(60,60,60);
    STONECOLOR player0StoneColor;
    STONECOLOR player1StoneColor;
    private int ownIndex;

    String[] args;
    Game game;
    Connection connection;
    WebsocketClient websocketClient;

    public static void main(String[] args) {

        ViewManager viewManager = new ViewManager();
        GameView gameView = new GameView(viewManager, args, "000", "Peter", null, STONECOLOR.BLACK, STONECOLOR.WHITE, 0);
        viewManager.setCurrentView(gameView);
        gameView.setVisible(true);
    }


    //offline
    public GameView(ViewManager viewManager, String[] args, String name, STONECOLOR player0StoneColor, STONECOLOR player1StoneColor, int ownIndex){

        this.viewManager = viewManager;
        this.args = args;
        this.player0StoneColor = player0StoneColor;
        this.player1StoneColor = player1StoneColor;
        this.ownIndex = ownIndex;
        setupView(name, "Offlinegame");
        enemyLabel.setText("Computer");

        if (ownIndex == 0) {
            scanButton.setEnabled(true);
            nextStepLabel.setText("Beginnen Sie das Spiel");

        } else {
            scanButton.setEnabled(false);
        }



    }


    //online
    public GameView(ViewManager viewManager, String[] args, String gameCode, String name, Connection connection, STONECOLOR player0StoneColor, STONECOLOR player1StoneColor, int ownIndex) throws HeadlessException {

        this.viewManager = viewManager;
        this.args = args;
        this.connection = connection;
        this.player0StoneColor = player0StoneColor;
        this.player1StoneColor = player1StoneColor;
        this.ownIndex = ownIndex;

        setupView(name, gameCode);




    }

    public void setupView(String name, String gameCode){
        View.setUIFont(new FontUIResource(new Font("Roboto", 0, 20)));
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setUndecorated(false);

        //panelInformation
        panelInformation = new JPanel();
        panelInformation.setLayout(new BoxLayout(panelInformation, BoxLayout.Y_AXIS));

        //panelInformationTop
        panelInformationTop = new JPanel();
        panelInformationTop.setLayout(new BoxLayout(panelInformationTop, BoxLayout.X_AXIS));
        informationLabel = new JLabel("");
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
        panelCenterLeft.setLayout(new BoxLayout(panelCenterLeft, BoxLayout.Y_AXIS));
        panelCenterLeft.setOpaque(false);
        boardImage = new BoardImage();
        nextStepLabel = new JLabel("Auf Gegenspieler warten");
        nextStepLabel.setForeground(aliceblue);
        nextStepLabel.setFont(new Font("Roboto", 0, 13));

        panelCenterLeft.add(boardImage.getMainLabel());
        panelCenterLeft.add(nextStepLabel);



        //right
        panelCenterRight = new JPanel();
        panelCenterRight.setOpaque(false);
        scanButton = new JButton("SCAN");
        scanButton.setPreferredSize(new Dimension(300,300));
        scanButton.setEnabled(false);
        scanButton.addActionListener(this);

        putButton = new JButton("put");
        putButton.addActionListener(this);

            /*ringTextfield = new JTextField();
            fieldTextfield = new JTextField();


            JPanel putPanel = new JPanel();
            putPanel.setLayout(new BoxLayout(putPanel, BoxLayout.Y_AXIS));
            putPanel.add(ringTextfield);
            putPanel.add(fieldTextfield);
            putPanel.add(putButton);*/

           /* putButton2 = new JButton("move");
            putButton2.addActionListener(this);
            ringTextfield2 = new JTextField();
            fieldTextfield2 = new JTextField();*/


            /*JPanel putPanel2 = new JPanel();
            putPanel2.setLayout(new BoxLayout(putPanel2, BoxLayout.Y_AXIS));
            putPanel2.add(ringTextfield2);
            putPanel2.add(fieldTextfield2);
            putPanel2.add(putButton2);*/

            /*panicButton = new JButton("Panic");
            panicButton.addActionListener(this);*/

        panelCenterRight.add(scanButton);
        //panelCenterRight.add(putPanel);
        //panelCenterRight.add(putPanel2);
        //panelCenterRight.add(panicButton);


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

    public Game getGame() {
        return game;
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

    public Connection getConnection() {
        return connection;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if(e.getSource() == this.scanButton){

            if (game.isKillPhase()){
                killScan();
                return;
            }

            if (game.isPutPhase() && !game.isKillPhase()){
                putScan();
                return;
            }
            if (game.isMovePhase() && !game.isKillPhase()){
                moveScan();
                return;
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

            Position from = new Position(0,2);
            Position to = new Position(Integer.parseInt(ringTextfield2.getText()), Integer.parseInt(fieldTextfield2.getText()));
            Move move = new Move(from, to);
            if (game.getBoard().checkMove(move, true)){
                connection.move(move,true);
            }
            else {
                System.out.println("Unerlaubter Zug");
            }


        }

        if (e.getSource() == this.panicButton){
            connection.getEbb().enableMotor(0,0);
        }

    }

    private void sendPutMessage(Position position){

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("playerUuid", game.getPlayerByIndex(game.getOwnIndex()).getUuid());
        jsonObject.put("playerIndex", ownIndex);
        jsonObject.put("gameCode", game.getGameCode());
        jsonObject.put("command", "update");
        jsonObject.put("action", "put");
        jsonObject.put("ring", position.getRing());
        jsonObject.put("field", position.getField());
        jsonObject.put("callComputer", false);
        websocketClient.send(jsonObject.toString());
        System.out.println(jsonObject);
    }

    private void sendMoveMessage(Move move){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("playerUuid", game.getPlayerByIndex(game.getOwnIndex()).getUuid());
        jsonObject.put("playerIndex", ownIndex);
        jsonObject.put("gameCode", game.getGameCode());
        jsonObject.put("command", "update");
        jsonObject.put("action", "move");
        jsonObject.put("moveFromRing", move.getFrom().getRing());
        jsonObject.put("moveFromField", move.getFrom().getField());
        jsonObject.put("moveToRing", move.getTo().getRing());
        jsonObject.put("moveToField", move.getTo().getField());
        jsonObject.put("callComputer", false);
        websocketClient.send(jsonObject.toString());
        System.out.println(jsonObject);

    }

    private void sendKillMessage(Position position){

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("playerUuid", game.getPlayerByIndex(game.getOwnIndex()).getUuid());
        jsonObject.put("playerIndex", ownIndex);
        jsonObject.put("gameCode", game.getGameCode());
        jsonObject.put("command", "update");
        jsonObject.put("action", "kill");
        jsonObject.put("ring", position.getRing());
        jsonObject.put("field", position.getField());
        jsonObject.put("callComputer", false);
        websocketClient.send(jsonObject.toString());
        System.out.println(jsonObject);
    }

    private void putOnBoardImage(Position position, STONECOLOR stonecolor){

        boardImage.put(position, stonecolor);
        this.getContentPane().validate();
        this.getContentPane().repaint();
    }

    private void moveOnBoardImage(Move move){

        boardImage.move(move);
        this.getContentPane().validate();
        this.getContentPane().repaint();
    }

    private void killOnBoardImage(Position position){

        boardImage.kill(position);
        this.getContentPane().validate();
        this.getContentPane().repaint();
    }

    private void putScan(){

        System.out.println("Scan des Spielfelds");
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        HoughCirclesRun houghCirclesRun = new HoughCirclesRun(game.getBoard());

        STONECOLOR stonecolor;

        if (ownIndex == 0 ){
            stonecolor = player0StoneColor;
        }
        else {
            stonecolor = player1StoneColor;
        }



        try {
            Position position = houghCirclesRun.detectPut(args, game.getBoard());
            sendPutMessage(position);
            game.getBoard().putStone(position, ownIndex);
            putOnBoardImage(position, stonecolor);

            game.increaseRound();
            increaseRoundLabel();
            setInformationLabel(" ");
            if (game.getBoard().checkMorris(position) && game.getBoard().isThereStoneToKill(1-ownIndex)){
                game.setKillPhase(true);
                enableScanButton(true);
                setNextStepLabelKill(true);
            }
            else {
                enableScanButton(false);

                if (game.getRound() > 18){
                    game.changeToMovePhase();
                    setNextStepLabelMove(false);
                }
                else {
                    setNextStepLabelPut(false);
                }
            }


        }
        catch (InvalidBoardException ibe){
            setInformationLabel(ibe.getMessage());
        }
    }

    private void moveScan(){

        System.out.println("Scan des Spielfelds");
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        HoughCirclesRun houghCirclesRun = new HoughCirclesRun(game.getBoard());



        try {
            Move move = houghCirclesRun.detectMove(args, game.getBoard());
            sendMoveMessage(move);
            game.getBoard().move(move, ownIndex);
            moveOnBoardImage(move);

            game.increaseRound();
            increaseRoundLabel();
            setInformationLabel(" ");
            if (game.getBoard().checkMorris(move.getTo()) && game.getBoard().isThereStoneToKill(1-ownIndex)){
                game.setKillPhase(true);
                enableScanButton(true);
                setNextStepLabelKill(true);
            }
            else {
                enableScanButton(false);
                setNextStepLabelMove(false);
            }
        }
        catch (InvalidBoardException ibe){
            setInformationLabel(ibe.getMessage());
        }
    }

    private void killScan(){

        System.out.println("Scan des Spielfelds");
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        HoughCirclesRun houghCirclesRun = new HoughCirclesRun(game.getBoard());


        try {
            Position position = houghCirclesRun.detectKill(args, game.getBoard());
            sendKillMessage(position);
            game.getBoard().clearStone(position);
            killOnBoardImage(position);

            setInformationLabel(" ");
            game.setKillPhase(false);
            enableScanButton(false);

            if (game.getRound() < 18){
                setNextStepLabelPut(false);
            }
            else {
                setNextStepLabelMove(false);
            }


            if (game.getBoard().countPlayersStones(1-ownIndex) < 3 && game.getRound() > 18) {
                setInformationLabel("Sie haben das Spiel gewonnen!");
                System.out.println("Spiel gewonnen");
            }

        }
        catch (InvalidBoardException ibe){
            setInformationLabel(ibe.getMessage());
        }
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

    public void setNextStepLabelPut(boolean localPlayer) {
        if (localPlayer){
            nextStepLabel.setText(nameLabel.getText() + " darf einen Stein setzen");
        }
        else {
            nextStepLabel.setText(enemyLabel.getText() + " darf einen Stein setzen");
        }
    }

    public void setNextStepLabelMove(boolean localPlayer) {
        if (localPlayer){
        nextStepLabel.setText(nameLabel.getText() + " darf einen Stein verschieben");
        }
        else {
            nextStepLabel.setText(enemyLabel.getText() + " darf einen Stein verschieben");
        }
    }

    public void setNextStepLabelKill(boolean localPlayer) {
        if (localPlayer){
            nextStepLabel.setText(nameLabel.getText() + " darf einen Stein entfernen");
        }
        else {
            nextStepLabel.setText(enemyLabel.getText() + " darf einen Stein entfernen");
        }
    }

    public void enableScanButton(boolean enable){
        scanButton.setEnabled(enable);
    }
}
