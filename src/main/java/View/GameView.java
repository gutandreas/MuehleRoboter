package View;

import Camera.HoughCirclesRun;
import Camera.InvalidBoardException;
import Communication.Messenger;
import Communication.WebsocketClient;
import EiBotBoard.Connection;
import game.Game;
import game.Move;
import game.Position;
import game.STONECOLOR;
import org.opencv.core.Core;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameView extends View implements ActionListener {

    String[] args;
    Game game;
    Connection connection;
    WebsocketClient websocketClient;
    private ViewManager viewManager;
    private JPanel mainPanel, panelInformation, panelInformationTop, panelInformationCenter, panelInformationBottom, panelCenter, panelCenterLeft, panelCenterCenter, panelCenterRight;
    private JLabel informationLabel, gamcodeTitleLabel, gamecodeLabel, nameTitleLabel, nameLabel, roundTitleLabel, roundLabel, enemyTitleLabel, enemyLabel, nextStepLabel;
    private JButton scanButton, cameraSettingsButton, exitButton;
    private JTextArea chatTextArea;
    private String textAreaPromtText = "Chatnachrichten...";
    private JScrollPane scroll;
    private BoardImage boardImage;
    private Color aliceblue = new Color(161, 210, 255);
    private Color background = new Color(60, 60, 60);
    private STONECOLOR player0StoneColor;
    private STONECOLOR player1StoneColor;
    private int ownIndex;

    //offline
    public GameView(ViewManager viewManager, String[] args, String name, Connection connection, STONECOLOR player0StoneColor, STONECOLOR player1StoneColor, int ownIndex) {

        this.viewManager = viewManager;
        this.args = args;
        this.connection = connection;
        this.player0StoneColor = player0StoneColor;
        this.player1StoneColor = player1StoneColor;
        this.ownIndex = ownIndex;
        setupView(name, "Computer", "Offlinegame");

        if (ownIndex == 0) {
            scanButton.setEnabled(true);
            nextStepLabel.setText("Beginnen Sie das Spiel");

        } else {
            scanButton.setEnabled(false);
        }

    }


    //online play Game
    public GameView(ViewManager viewManager, String[] args, String gameCode, String name, Connection connection, STONECOLOR player0StoneColor, STONECOLOR player1StoneColor, int ownIndex) throws HeadlessException {

        this.viewManager = viewManager;
        this.args = args;
        this.connection = connection;
        this.player0StoneColor = player0StoneColor;
        this.player1StoneColor = player1StoneColor;
        this.ownIndex = ownIndex;

        setupView(name, "---", gameCode);
    }


    //online watch Game
    public GameView(ViewManager viewManager, String[] args, String gameCode, String name0, String name1, Connection connection, STONECOLOR player0StoneColor, STONECOLOR player1StoneColor, int ownIndex) throws HeadlessException {

        this.viewManager = viewManager;
        this.args = args;
        this.connection = connection;
        this.player0StoneColor = player0StoneColor;
        this.player1StoneColor = player1StoneColor;
        this.ownIndex = ownIndex;

        setupView(name0, name1, gameCode);

        scanButton.setVisible(false);
        cameraSettingsButton.setVisible(false);

    }


    public void setupView(String name0, String name1, String gameCode) {
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
        nameLabel = new JLabel(name0);
        nameLabel.setForeground(aliceblue);
        enemyTitleLabel = new JLabel(" / Gegner: ");
        enemyTitleLabel.setForeground(aliceblue);
        enemyLabel = new JLabel(name1);
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
        panelCenter.setLayout(new FlowLayout(FlowLayout.CENTER));

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
        scanButton.setPreferredSize(new Dimension(290, 290));
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
        scroll = new JScrollPane(chatTextArea, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
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

    private void putScan() {

        System.out.println("Scan des Spielfelds");
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        HoughCirclesRun houghCirclesRun = new HoughCirclesRun(this, viewManager.getrPiCamera());

        try {
            Position position = houghCirclesRun.detectPut(game.getBoard());
            Messenger.sendPutMessage(viewManager, position, false);

        } catch (InvalidBoardException ibe) {
            setInformationLabel(ibe.getMessage());
        }
    }

    private void moveScan() {

        System.out.println("Scan des Spielfelds");
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        HoughCirclesRun houghCirclesRun = new HoughCirclesRun(this, viewManager.getrPiCamera());

        try {
            Move move = houghCirclesRun.detectMove(game.getBoard());
            Messenger.sendMoveMessage(viewManager, move, false);
        } catch (InvalidBoardException ibe) {
            setInformationLabel(ibe.getMessage());
        }
    }

    private void killScan() {

        System.out.println("Scan des Spielfelds");
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        HoughCirclesRun houghCirclesRun = new HoughCirclesRun(this, viewManager.getrPiCamera());

        try {
            Position position = houghCirclesRun.detectKill(game.getBoard());
            Messenger.sendKillMessage(viewManager, position, false);

        } catch (InvalidBoardException ibe) {
            setInformationLabel(ibe.getMessage());
        }
    }

    public void increaseRoundLabel() {
        roundLabel.setText(String.valueOf(game.getRound()));
    }

    public void setEnemyLabel(String enemyName) {
        enemyLabel.setText(enemyName);
    }

    public void setInformationLabel(String information) {
        informationLabel.setText(information);
    }

    public void clearInformationLabel() {
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

    public void enableScanButton(boolean enable) {
        scanButton.setEnabled(enable);
    }

    public void addChatMessageToTextarea(String name, String message) {

        if (chatTextArea.getText().equals(textAreaPromtText)) {
            chatTextArea.setText(name + ": " + message + "\n");
        } else {
            chatTextArea.setText(name + ": " + message + "\n" + chatTextArea.getText());
        }
    }

    public Game getGame() {
        return game;
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

    public Connection getConnection() {
        return connection;
    }

    public ViewManager getViewManager() {
        return viewManager;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == this.scanButton) {

            if (game.isKillPhase()) {
                killScan();
                return;
            }

            if (game.isPutPhase() && !game.isKillPhase()) {
                putScan();
                return;
            }
            if (game.isMovePhase() && !game.isKillPhase()) {
                moveScan();
                return;
            }
        }

        if (e.getSource() == cameraSettingsButton) {
            new CameraView(viewManager);
        }

        if (e.getSource() == exitButton) {

            System.out.println("Spiel verlassen");

            if (!game.isWatchGame()) {
                Messenger.sendGiveUpMessage(viewManager);
            }

            Messenger.sendRoboterConnectionMessage(viewManager, false, false, false);

            connection.resetVariables();
            StartMenuView startMenuView = new StartMenuView(viewManager, args, connection);
            viewManager.setCurrentView(startMenuView);
            startMenuView.setVisible(true);
            this.setVisible(false);
        }
    }

}
