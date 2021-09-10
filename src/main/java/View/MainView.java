package View;

import Camera.HoughCirclesRun;
import EiBotBoard.Connection;
import Websocket.WebsocketClient;
import game.Board;
import game.Position;
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
import java.util.Enumeration;
import javax.swing.*;
import javax.swing.plaf.FontUIResource;

// Damit Objekte der Klasse BeispielListener
// zum ActionListener werden kann, muss das Interface
// ActionListener implementiert werden
public class MainView extends JFrame implements ActionListener, MouseListener
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

    public MainView(String[] args, Connection connection){


        setUIFont(new FontUIResource(new Font("Roboto", 0, 20)));
        this.setBackground(background);

        mainPanel = new JPanel();

        this.setTitle("ActionListener Beispiel");
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


        //Drei Buttons werden erstellt
        startButton = new JButton( "Spiel starten");
        joinButton = new JButton ("Einem Spiel beitreten");
        watchButton = new JButton ("Spiel beobachten");


        //Buttons werden dem Listener zugeordnet
        startButton.addActionListener(this);
        joinButton.addActionListener(this);
        watchButton.addActionListener(this);

        //Buttons werden dem JPanel hinzugefügt

        panelCenter.add(startButton);
        panelCenter.add(joinButton);
        panelCenter.add(watchButton);
        panelCenter.setOpaque(false);

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



    public static void setUIFont(FontUIResource f) {
        Enumeration keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource) {
                FontUIResource orig = (FontUIResource) value;
                Font font = new Font(f.getFontName(), orig.getStyle(), f.getSize());
                UIManager.put(key, new FontUIResource(font));
            }
        }
    }



    public void actionPerformed (ActionEvent ae){
        // Die Quelle wird mit getSource() abgefragt und mit den
        // Buttons abgeglichen. Wenn die Quelle des ActionEvents einer
        // der Buttons ist, wird der Text des JLabels entsprechend geändert
        if(ae.getSource() == this.startButton){


            sendStartHTTPRequest();

            /*label.setText(("Button 1 wurde betätigt"));
            connection.put(new Position(1,1),1);*/
        }
        else if(ae.getSource() == this.joinButton){
            label.setText("Button 2 wurde betätigt");
            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
            new HoughCirclesRun().run(args);
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

        HttpClient client = HttpClient.newBuilder().build();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("gameCode", gamecodeTextfield.getText());
        jsonObject.put("player1Name", nameTextfield.getText());
        jsonObject.put("player1Color", "BLACK");

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


        try {
            response = client.send(request,HttpResponse.BodyHandlers.ofString());

            String body = (String) response.body();
            JSONObject jsonResponseObject = new JSONObject(body);
            String uuid = jsonResponseObject.getString("player1Uuid");
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
                WebsocketClient websocketClient = new WebsocketClient(uri, connection, new Board());
                websocketClient.connect();
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