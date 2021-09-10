package View;

import Camera.HoughCirclesRun;
import EiBotBoard.Connection;
import game.Position;
import org.opencv.core.Core;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import javax.swing.*;
import javax.swing.plaf.FontUIResource;

// Damit Objekte der Klasse BeispielListener
// zum ActionListener werden kann, muss das Interface
// ActionListener implementiert werden
public class MainView extends JFrame implements ActionListener
{
    JButton button1;
    JButton button2;
    JButton button3;
    ButtonGroup radioButtonGroup;
    JTextField gamecodeTextfield, nameTextfield;
    JLabel gameCodeLabel, nameLabel, label;
    JPanel radioButtonPanel, mainPanel, panelTop, panelCenter, panelBottom;
    JRadioButton gameModeRadioButtonStart, gameModeRadioButtonJoin, gameModeRadioButtonWatch;

    Color aliceblue = new Color(161, 210, 255);
    Color background = new Color(60,60,60);



    Connection connection;
    String[] args;

    public MainView(){


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

        //panelTop


        nameLabel = new JLabel("Name:");
        nameLabel.setForeground(aliceblue);
        nameTextfield = new JTextField();
        nameTextfield.setColumns(10);
        gameCodeLabel = new JLabel("Gamecode:");
        gameCodeLabel.setForeground(aliceblue);
        gamecodeTextfield = new JTextField();
        gamecodeTextfield.setColumns(10);

        panelTop.add(nameLabel);
        panelTop.add(nameTextfield);
        panelTop.add(gameCodeLabel);
        panelTop.add(gamecodeTextfield);
        panelTop.setOpaque(false);
        //panelTop.setBackground(background);


        //Drei Buttons werden erstellt
        button1 = new JButton( "Spiel starten");
        button2 = new JButton ("Einem Spiel beitreten");
        button3 = new JButton ("Spiel beobachten");


        //Buttons werden dem Listener zugeordnet
        button1.addActionListener(this);
        button2.addActionListener(this);
        button3.addActionListener(this);

        //Buttons werden dem JPanel hinzugefügt

        panelCenter.add(button1);
        panelCenter.add(button2);
        panelCenter.add(button3);
        panelCenter.setOpaque(false);

        //panelBottom
        Keyboard keyboard = new Keyboard();
        panelBottom.add(keyboard.getKeyboard());
        panelBottom.setOpaque(false);


        //mainPanel füllen
        mainPanel.add(panelTop);
        mainPanel.add(panelCenter);
        mainPanel.add(panelBottom);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(background);




        this.add(mainPanel);

        this.getContentPane().setBackground( background );
        }



    public MainView(String[] args, Connection connection){
        this.setTitle("ActionListener Beispiel");
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setUndecorated(true);
        //GraphicsDevice dev = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice(); //Taskleiste ausblenden
        //dev.setFullScreenWindow(this);
        panelCenter = new JPanel();
        panelTop = new JPanel();
        panelBottom = new JPanel();

        //panelTop
        nameLabel = new JLabel("Name:");
        nameTextfield = new JTextField();
        gameCodeLabel = new JLabel("Gamecode:");
        gamecodeTextfield = new JTextField();

        panelTop.add(nameLabel);
        panelTop.add(nameTextfield);
        panelTop.add(gameCodeLabel);
        panelTop.add(gamecodeTextfield);




        // Leeres JLabel-Objekt wird erzeugt
        label = new JLabel();


        //Drei Buttons werden erstellt
        button1 = new JButton("Put");
        button2 = new JButton ("Photo");
        button3 = new JButton ("Button 3");


        //Buttons werden dem Listener zugeordnet
        button1.addActionListener(this);
        button2.addActionListener(this);
        button3.addActionListener(this);

        //Buttons werden dem JPanel hinzugefügt

        panelCenter.add(button1);
        panelCenter.add(button2);
        panelCenter.add(button3);


        //JLabel wird dem Panel hinzugefügt
        panelCenter.add(label);
        this.add(panelCenter);
        this.add(panelTop);

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
        if(ae.getSource() == this.button1){
            label.setText(("Button 1 wurde betätigt"));
            connection.put(new Position(1,1),1);
        }
        else if(ae.getSource() == this.button2){
            label.setText("Button 2 wurde betätigt");
            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
            new HoughCirclesRun().run(args);
        }
        else if (ae.getSource() == this.button3){
            label.setText(("Button 3 wurde betätigt"));
        }

    }
}