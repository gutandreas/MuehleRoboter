package View;

import EiBotBoard.Connection;
import Communication.WebsocketClient;
import game.*;
import org.json.JSONObject;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import javax.swing.*;
import javax.swing.plaf.FontUIResource;


public class OnlineMenuView extends View implements ActionListener, MouseListener {

    ViewManager viewManager;

    JButton startButton;
    JButton joinButton;
    JButton watchButton;
    SwitchButton colorSwitchButton;
    JTextField gamecodeTextfield, nameTextfield;
    JLabel informationLabel, gameCodeLabel, nameLabel, colorLabel;
    JPanel mainPanel, panelInformation, panelTop, panelCenter, panelBottom, panelStartGame, panelColor;

    Keyboard keyboard;
    Color aliceblue = new Color(161, 210, 255);
    Color background = new Color(60,60,60);

    Connection connection;
    String[] args;
    //String ipAdress = "192.168.0.11";
    String ipAdress = "217.160.10.113";

    String port = "443";

    public OnlineMenuView(ViewManager viewManager, String[] args, Connection connection){

        this.viewManager = viewManager;

        setUIFont(new FontUIResource(new Font("Roboto", 0, 16)));
        this.setBackground(background);

        mainPanel = new JPanel();

        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setUndecorated(true);
        //GraphicsDevice dev = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice(); //Taskleiste ausblenden
        //dev.setFullScreenWindow(this);
        panelCenter = new JPanel();
        panelTop = new JPanel();
        panelBottom = new JPanel();
        panelInformation = new JPanel();

        //panelInformation
        informationLabel = new JLabel(" ");
        informationLabel.setForeground(Color.RED);
        panelInformation.add(informationLabel);
        panelInformation.setOpaque(false);

        //panelTop
        nameLabel = new JLabel("Name:");
        nameLabel.setForeground(aliceblue);
        nameTextfield = new JTextField();
        nameTextfield.addMouseListener(this);
        nameTextfield.setColumns(10);
        gameCodeLabel = new JLabel("Gamecode:");
        gameCodeLabel.setForeground(aliceblue);
        gamecodeTextfield = new JTextField();
        gamecodeTextfield.addMouseListener(this);
        gamecodeTextfield.setColumns(10);

        panelTop.add(nameLabel);
        panelTop.add(nameTextfield);
        panelTop.add(gameCodeLabel);
        panelTop.add(gamecodeTextfield);
        panelTop.setOpaque(false);
        //panelTop.setBackground(background);

        //panelCenter
        startButton = new JButton("Spiel starten");
        colorSwitchButton = new SwitchButton(Color.BLACK, Color.WHITE, aliceblue);
        colorSwitchButton.addMouseListener(this);
        colorLabel = new JLabel("Farbe: ");
        colorLabel.setForeground(aliceblue);
        panelColor = new JPanel();
        panelColor.add(colorLabel);
        panelColor.add(colorSwitchButton);
        panelColor.setOpaque(false);
        panelStartGame = new JPanel();
        panelStartGame.add(panelColor);
        panelStartGame.add(startButton);
        panelStartGame.setLayout(new BoxLayout(panelStartGame, BoxLayout.X_AXIS));
        panelStartGame.setOpaque(false);
        joinButton = new JButton ("Einem Spiel beitreten");
        watchButton = new JButton ("Spiel beobachten");
        panelCenter.setOpaque(false);
        panelCenter.add(panelStartGame);
        panelCenter.add(Box.createHorizontalStrut(25));
        panelCenter.add(joinButton);
        panelCenter.add(watchButton);

        startButton.addActionListener(this);
        joinButton.addActionListener(this);
        watchButton.addActionListener(this);

        //panelBottom
        keyboard = new Keyboard(nameTextfield);
        panelBottom.add(keyboard.getKeyboard());
        panelBottom.setOpaque(false);

        //mainPanel füllen
        mainPanel.add(panelInformation);
        mainPanel.add(panelTop);
        mainPanel.add(panelCenter);
        mainPanel.add(panelBottom);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(background);

        this.add(mainPanel);
        this.getContentPane().setBackground( background );

        this.connection = connection;
        this.args = args;
    }


    public void actionPerformed (ActionEvent ae){

        if(ae.getSource() == this.startButton){
            sendHTTPRequest(0);
        }
        if(ae.getSource() == this.joinButton){
            sendHTTPRequest(1);
        }
        if (ae.getSource() == this.watchButton){
            sendHTTPRequest(2);
        }
        if (ae.getSource() == this.gamecodeTextfield){
            keyboard.setActiveTextfield(gamecodeTextfield);
        }
        if (ae.getSource() == this.nameTextfield){
            keyboard.setActiveTextfield(nameTextfield);
        }

    }

    private void sendHTTPRequest(int modus){

        String gameCode = gamecodeTextfield.getText();
        String name = nameTextfield.getText();

        STONECOLOR player1Color;
        STONECOLOR player2Color;
        if (colorSwitchButton.isSelected()){
            player1Color = STONECOLOR.WHITE;
            player2Color = STONECOLOR.BLACK;
        }
        else {
            player1Color = STONECOLOR.BLACK;
            player2Color = STONECOLOR.WHITE;
        }

        HttpClient client = HttpClient.newBuilder().build();
        JSONObject jsonObject = new JSONObject();

        String urlAsString = "";

        switch (modus){
            case 0:
                urlAsString = "http://" + ipAdress + ":" + port + "/index/controller/menschVsMensch/start";
                jsonObject.put("player1Name", name);
                jsonObject.put("player1Color", player1Color.toString());
                break;

            case 1:
                urlAsString = "http://" + ipAdress + ":" + port + "/index/controller/menschVsMensch/join";
                jsonObject.put("player2Name", name);
                break;

            case 2:
                urlAsString = "http://" + ipAdress + ":" + port + "/index/controller/gameWatch";
                jsonObject.put("modus", "Game Watch");
                break;
        }

        jsonObject.put("gameCode", gameCode);

        if (modus != 2 && nameTextfield.getText().length() == 0){
            informationLabel.setText("Geben Sie einen Spielernamen ein");
            return;
        }

        if (gamecodeTextfield.getText().length() == 0){
            informationLabel.setText("Geben Sie einen Gamecode ein");
            return;
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlAsString))
                .POST(HttpRequest.BodyPublishers.ofString(jsonObject.toString()))
                .build();

        HttpResponse<?> response = null;
        String uuid = "";

        JSONObject jsonResponseObject = null;

        try {
            response = client.send(request,HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 417) {
                informationLabel.setText(response.body().toString());
            }

            String body = (String) response.body();
            jsonResponseObject = new JSONObject(body);

            switch (modus){
                case 0: uuid = jsonResponseObject.getString("player1Uuid");
                    break;

                case 1: uuid = jsonResponseObject.getString("player2Uuid");
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (response.statusCode() == 200){
            try {
                URI uri = new URI("ws://" + ipAdress + ":" + port + "/board");

                GameView gameView = null;
                Game game = null;

                switch (modus){

                    case 0:
                        gameView = new GameView(viewManager, args, gameCode, name, connection, player1Color, player2Color, 0);
                        game = new Game(gameView, new HumanPlayer(gameView, nameTextfield.getText(), uuid, player1Color, true),
                                new HumanPlayer(gameView, " ", " ", player2Color, false),
                                gamecodeTextfield.getText(), null, false, false);
                        break;

                    case 1:
                        if (jsonResponseObject.getString("player2Color").equals("BLACK")){
                            player1Color = STONECOLOR.WHITE;
                            player2Color = STONECOLOR.BLACK;
                        }
                        else {
                            player1Color = STONECOLOR.BLACK;
                            player2Color = STONECOLOR.WHITE;
                        }
                        gameView = new GameView(viewManager, args, gameCode, name, connection, player1Color, player2Color, 1);
                        game = new Game(gameView, new HumanPlayer(gameView, jsonResponseObject.getString("player1Name"), " ", STONECOLOR.valueOf(jsonResponseObject.getString("player2Color" )), false),
                                new HumanPlayer(gameView, nameTextfield.getText(), uuid, player2Color, true), gamecodeTextfield.getText(), null, true, false);
                        gameView.setEnemyLabel(jsonResponseObject.getString("player1Name"));
                        break;

                    case 2:
                        player1Color = STONECOLOR.valueOf(jsonResponseObject.getString("player1Color"));
                        if (player1Color == STONECOLOR.BLACK){
                            player2Color = STONECOLOR.WHITE;}
                        else {
                            player2Color = STONECOLOR.BLACK;}
                        gameCode = jsonResponseObject.getString("gameCodeWatch");
                        gameView = new GameView(viewManager, args, gameCode, jsonResponseObject.getString("player1Name"), jsonResponseObject.getString("player2Name"), connection, player1Color, player2Color, 0);
                        game = new Game(gameView, new HumanPlayer(gameView, jsonResponseObject.getString("player1Name"), "", player1Color, false),
                                new HumanPlayer(gameView, jsonResponseObject.getString("player2Name"), "", player2Color, false), gameCode, null, true, true);
                        break;
                }

                WebsocketClient websocketClient = new WebsocketClient(viewManager, uri, game);
                game.setWebsocketClient(websocketClient);
                websocketClient.connect();
                gameView.setGame(game);
                gameView.setWebsocketClient(websocketClient);
                viewManager.setCurrentView(gameView);
                gameView.setVisible(true);
                this.setVisible(false);

            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        else {
            informationLabel.setText("Die Serveranfrage konnte nicht beantwortet werden...");
        }
    }


    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == this.gamecodeTextfield) {
            keyboard.setActiveTextfield(gamecodeTextfield);}
        if (e.getSource() == this.nameTextfield) {
            keyboard.setActiveTextfield(nameTextfield);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

}