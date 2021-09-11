package View;

import Camera.HoughCirclesRun;
import EiBotBoard.Connection;
import Websocket.WebsocketClient;
import game.*;
import org.json.JSONObject;
import org.opencv.core.Core;

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


public class MenuView extends View implements ActionListener, MouseListener
{
    JButton startButton;
    JButton joinButton;
    JButton watchButton;
    JTextField gamecodeTextfield, nameTextfield;
    JLabel informationLabel, gameCodeLabel, nameLabel, label;
    JPanel mainPanel, panelInformation, panelTop, panelCenter, panelBottom;
    JRadioButton gameModeRadioButtonStart, gameModeRadioButtonJoin, gameModeRadioButtonWatch;

    Keyboard keyboard;
    Color aliceblue = new Color(161, 210, 255);
    Color background = new Color(60,60,60);



    Connection connection;
    String[] args;
    String ipAdress = "192.168.0.11";

    public MenuView(String[] args, Connection connection){


        setUIFont(new FontUIResource(new Font("Roboto", 0, 20)));
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
        startButton = new JButton( "Spiel starten");
        joinButton = new JButton ("Einem Spiel beitreten");
        watchButton = new JButton ("Spiel beobachten");
        panelCenter.add(startButton);
        panelCenter.add(joinButton);
        panelCenter.add(watchButton);
        panelCenter.setOpaque(false);
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


            sendStartHTTPRequest();

            /*label.setText(("Button 1 wurde betätigt"));
            connection.put(new Position(1,1),1);*/
        }
        else if(ae.getSource() == this.joinButton){
            label.setText("Button 2 wurde betätigt");
            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
            new HoughCirclesRun().takePhoto(args);
        }
        else if (ae.getSource() == this.watchButton){
            label.setText(("Button 3 wurde betätigt"));
        }
        else if (ae.getSource() == this.gamecodeTextfield){
            keyboard.setActiveTextfield(gamecodeTextfield);
        }
        else if (ae.getSource() == this.nameTextfield){
            keyboard.setActiveTextfield(nameTextfield);
        }

    }

    private void sendStartHTTPRequest(){
        String urlAsString = "http://" + ipAdress + ":8080/index/controller/menschVsMensch/start";

        String gameCode = gamecodeTextfield.getText();
        String name = nameTextfield.getText();
        STONECOLOR stonecolor = STONECOLOR.BLACK;

        HttpClient client = HttpClient.newBuilder().build();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("gameCode", gameCode);
        jsonObject.put("player1Name", name);
        jsonObject.put("player1Color", STONECOLOR.valueOf("BLACK"));

        if (gamecodeTextfield.getText().length() == 0){
            informationLabel.setText("Geben Sie einen Spielernamen ein");
            return;
        }

        if (nameTextfield.getText().length() == 0){
            informationLabel.setText("Geben Sie einen Gamcode ein");
            return;
        }



        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlAsString))
                .POST(HttpRequest.BodyPublishers.ofString(jsonObject.toString()))
                .build();

        HttpResponse<?> response = null;
        String uuid = "";


        try {
            response = client.send(request,HttpResponse.BodyHandlers.ofString());

            String body = (String) response.body();
            JSONObject jsonResponseObject = new JSONObject(body);
            uuid = jsonResponseObject.getString("player1Uuid");
            System.out.println(uuid);


        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(response.statusCode());

        if (response.statusCode() == 200){
            try {
                URI uri = new URI("ws://" + ipAdress + ":8080/board");
                GameView gameView = new GameView(args, gameCode, connection);
                Game game = new Game(gameView, new HumanPlayer(gameView, nameTextfield.getText(), uuid, STONECOLOR.BLACK),
                        new OnlinePlayer(gameView, " "), gamecodeTextfield.getText());
                WebsocketClient websocketClient = new WebsocketClient(uri, connection, game);
                gameView.setGame(game);
                websocketClient.connect();
                gameView.setVisible(true);
                this.setVisible(false);

            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == this.gamecodeTextfield){
            keyboard.setActiveTextfield(gamecodeTextfield);
        }
        else if (e.getSource() == this.nameTextfield){
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