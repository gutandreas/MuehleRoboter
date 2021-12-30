package View;

import Camera.HoughCirclesRun;
import Camera.InvalidBoardException;
import EiBotBoard.Connection;
import Websocket.Messenger;
import Websocket.WebsocketClient;
import com.sun.source.doctree.TextTree;
import game.*;
import org.opencv.core.Core;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


public class GameView extends View implements ActionListener {

    ViewManager viewManager;

    JPanel mainPanel, panelInformation, panelInformationTop, panelInformationCenter, panelInformationBottom, panelCenter, panelCenterLeft, panelCenterCenter, panelCenterRight;
    JLabel informationLabel, gamcodeTitleLabel, gamecodeLabel, nameTitleLabel, nameLabel, roundTitleLabel, roundLabel, enemyTitleLabel, enemyLabel, nextStepLabel;
    JButton scanButton, cameraSettingsButton, exitButton;
    JTextArea chatTextArea;
    String textAreaPromtText = "Chatnachrichten...";
    JScrollPane scroll;
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
        ScorePoints putPoints = new ScorePoints(0,0,0,0,0,0,0,0,0,0,0,0);
        ScorePoints movePoints = new ScorePoints(0,0,0,0,0,0,0,0,0,0,0,0);

        ComputerPlayer computerPlayer = new ComputerPlayer(gameView, "COMPUTER", STONECOLOR.WHITE, putPoints, movePoints, 3 );
        Game game = new Game(gameView, computerPlayer, new HumanPlayer(gameView, "Peter", "", STONECOLOR.BLACK, true),

                "gamecode", null, false);
        viewManager.setCurrentView(gameView);
        gameView.setGame(game);
        //gameView.enableScanButton(true);
        gameView.setVisible(true);
        computerPlayer.triggerPut(viewManager);
    }


    //offline
    public GameView(ViewManager viewManager, String[] args, String name, Connection connection, STONECOLOR player0StoneColor, STONECOLOR player1StoneColor, int ownIndex){

        this.viewManager = viewManager;
        this.args = args;
        this.connection = connection;
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
        informationLabel = new JLabel(" ");
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
        panelCenter.setLayout(new FlowLayout(0));

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



        //center
        panelCenterCenter = new JPanel();
        panelCenterCenter.setOpaque(false);
        scanButton = new JButton("SCAN");
        scanButton.setPreferredSize(new Dimension(290,290));
        scanButton.setEnabled(false);
        scanButton.addActionListener(this);


        panelCenterCenter.add(scanButton);

        //right
        panelCenterRight = new JPanel();
        panelCenterRight.setOpaque(false);
        panelCenterRight.setLayout(new BoxLayout(panelCenterRight, BoxLayout.Y_AXIS));
        chatTextArea = new JTextArea(textAreaPromtText, 12, 10);
        chatTextArea.setFont(new Font("Roboto", 0, 13));
        chatTextArea.setLineWrap(true);
        chatTextArea.setWrapStyleWord(true);
        chatTextArea.setEditable(false);
        chatTextArea.setHighlighter(null);
        scroll = new JScrollPane (chatTextArea,
                JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setAlignmentX(Component.CENTER_ALIGNMENT);


        cameraSettingsButton = new JButton("Kameraoptionen");
        cameraSettingsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        cameraSettingsButton.addActionListener(this);


        exitButton = new JButton("Spiel verlassen");
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.addActionListener(this);





        panelCenterRight.add(scroll);
        panelCenterRight.add(Box.createRigidArea(new Dimension(0, 5)));
        panelCenterRight.add(cameraSettingsButton);
        panelCenterRight.add(Box.createRigidArea(new Dimension(0, 5)));
        panelCenterRight.add(exitButton);



        panelCenter.add(panelCenterLeft);
        panelCenter.add(panelCenterCenter);
        panelCenter.add(panelCenterRight);
        panelCenter.setOpaque(false);


        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(panelInformation);
        mainPanel.add(panelCenter);
        mainPanel.setBackground(background);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
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

        if (e.getSource() == cameraSettingsButton){
            new CameraView(viewManager);
        }

        if (e.getSource() == exitButton){
            System.out.println("Spiel verlassen");
            Messenger.sendGiveUpMessage(viewManager);
            connection.resetVariables();
            StartMenuView startMenuView = new StartMenuView(viewManager, args, connection);
            viewManager.setCurrentView(startMenuView);
            startMenuView.setVisible(true);
            this.setVisible(false);
        }

    }


    private void putScan(){

        System.out.println("Scan des Spielfelds");
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        HoughCirclesRun houghCirclesRun = new HoughCirclesRun(this, viewManager.getrPiCamera());


        try {
            Position position = houghCirclesRun.detectPut(args, game.getBoard());
            Messenger.sendPutMessage(viewManager, position, false);

        }
        catch (InvalidBoardException ibe){
            setInformationLabel(ibe.getMessage());
        }
    }

    private void moveScan(){

        System.out.println("Scan des Spielfelds");
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        HoughCirclesRun houghCirclesRun = new HoughCirclesRun(this, viewManager.getrPiCamera());



        try {
            Move move = houghCirclesRun.detectMove(args, game.getBoard());
            Messenger.sendMoveMessage(viewManager, move, false);
        }
        catch (InvalidBoardException ibe){
            setInformationLabel(ibe.getMessage());
        }
    }

    private void killScan(){

        System.out.println("Scan des Spielfelds");
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        HoughCirclesRun houghCirclesRun = new HoughCirclesRun(this, viewManager.getrPiCamera());


        try {
            Position position = houghCirclesRun.detectKill(args, game.getBoard());
            Messenger.sendKillMessage(viewManager, position, false);

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

    public void clearInformationLabel(){
        informationLabel.setText(" ");
    }
    public void setNextStepLabelPut(String name) {
        nextStepLabel.setText(name + " darf einen Stein setzen");

    }

    public void setNextStepLabelMove(String name) {
        nextStepLabel.setText(name + " darf einen Stein verschieben");

    }

    public void setNextStepLabelKill(String name) {
        nextStepLabel.setText(name + " darf einen Stein entfernen");

    }

    public void enableScanButton(boolean enable){
        scanButton.setEnabled(enable);
    }

    public void addChatMessageToTextarea(String name, String message){

        if (chatTextArea.getText().equals(textAreaPromtText)){
            chatTextArea.setText(name + ": " + message + "\n");
        }
        else {
            chatTextArea.setText(name + ": " + message +"\n" + chatTextArea.getText());}
    }


    public ViewManager getViewManager() {
        return viewManager;
    }
}
