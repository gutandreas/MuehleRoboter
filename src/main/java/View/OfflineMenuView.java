package View;

import EiBotBoard.Connection;
import Websocket.WebsocketClient;
import game.*;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
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


public class OfflineMenuView extends View implements ActionListener, MouseListener
{
    ViewManager viewManager;

    JButton startButton;
    SwitchButton colorSwitchButton, startSwitchButton;
    JTextField nameTextfield;
    JLabel informationLabel, nameLabel, colorLabel, label, computerLevelLabel, startLabelHuman, startLabelComputer;
    JPanel mainPanel, panelInformation, panelTop, panelCenter, panelBottom, panelStartGame, panelColor;
    JRadioButton computerLevelRadioButton;

    Keyboard keyboard;
    Color aliceblue = new Color(161, 210, 255);
    Color background = new Color(60,60,60);

    Connection connection;
    String[] args;
    String ipAdress = "192.168.0.11";
    //String ipAdress = "localhost";

    ScorePoints putPoints = new ScorePoints(3000, 1000,30, 200, 300,6, -3000, -1000, -30, -200, -300, -6);
    ScorePoints movePoints = new ScorePoints(2000, 300,250, 200, 300,3, -2000, -300, -250, -200, -300, -3);


    public static void main(String[] args) {
        ViewManager viewManager = new ViewManager();
        OfflineMenuView onlineMenuView = new OfflineMenuView(viewManager, args, null);
        viewManager.setCurrentView(onlineMenuView);
        onlineMenuView.setVisible(true);


    }

    public OfflineMenuView(ViewManager viewManager, String[] args, Connection connection){

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

        colorSwitchButton = new SwitchButton(Color.BLACK, Color.WHITE, aliceblue);
        colorSwitchButton.addMouseListener(this);
        colorLabel = new JLabel("Farbe: ");
        colorLabel.setForeground(aliceblue);
        panelColor = new JPanel();
        panelColor.add(colorLabel);
        panelColor.add(colorSwitchButton);
        panelColor.setOpaque(false);

        startSwitchButton = new SwitchButton(new Color(180, 255, 150), new Color(255, 180, 140), Color.BLACK);
        startSwitchButton.addMouseListener(this);
        startLabelHuman = new JLabel("Beginnen:  Ja");
        startLabelHuman.setForeground(aliceblue);
        startLabelComputer = new JLabel("Nein");
        startLabelComputer.setForeground(aliceblue);
        panelStartGame = new JPanel();
        panelStartGame.setOpaque(false);
        panelStartGame.add(startLabelHuman);
        panelStartGame.add(startSwitchButton);
        panelStartGame.add(startLabelComputer);



        computerLevelLabel = new JLabel("Spielstärke Computer:");
        computerLevelRadioButton = new JRadioButton();
        computerLevelRadioButton.setText("Schwach");

        panelTop.add(nameLabel);
        panelTop.add(nameTextfield);
        panelTop.add(panelColor);
        panelTop.add(panelStartGame);

        panelTop.setOpaque(false);
        //panelTop.setBackground(background);


        //panelCenter
        startButton = new JButton("Spiel starten");
        panelCenter.setOpaque(false);
        panelCenter.add(Box.createHorizontalStrut(25));
        panelCenter.add(startButton);
        startButton.addActionListener(this);



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


            if (nameTextfield.getText().length() == 0){
                informationLabel.setText("Geben Sie einen Spielernamen ein");
                return;
            }

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

            int ownIndex = startSwitchButton.isSelected() ? 1 : 0;
            GameView gameView = new GameView(viewManager, args, nameTextfield.getText(), connection, player1Color, player2Color, ownIndex);
            Game game = null;
            int levelLimit = 3;

            switch (ownIndex){
                case 0:
                    game = new Game(gameView, new HumanPlayer(gameView, nameTextfield.getText(), " ", player1Color, true),
                            new ComputerPlayer(gameView, "Computer", player2Color, putPoints, movePoints, levelLimit),
                            "Offlinegame", null, false);
                    break;
                case 1:
                    game = new Game(gameView, new ComputerPlayer(gameView, "Computer", player1Color, putPoints, movePoints, levelLimit),
                            new HumanPlayer(gameView, nameTextfield.getText(), " ", player2Color, true),
                            "Offlinegame", null, false);
                    break;
            }

            viewManager.setCurrentView(gameView);
            gameView.setGame(game);
            gameView.setVisible(true);
            this.setVisible(false);
        }


    }


    @Override
    public void mouseClicked(MouseEvent e) {
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