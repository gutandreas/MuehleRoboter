package View;

import EiBotBoard.Connection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class StartMenuView extends View implements MouseListener {

    ViewManager viewManager;
    Connection connection;
    JPanel mainPanel, buttonPanel;
    JButton onlineButton, offlineButton;
    JLabel titleLabel;

    Color aliceblue = new Color(161, 210, 255);
    Color background = new Color(60, 60, 60);

    String[] args;


    public StartMenuView(ViewManager viewManager, String[] args, Connection connection) {

        this.viewManager = viewManager;
        this.connection = connection;
        this.args = args;
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setUndecorated(true);
        this.getContentPane().setBackground(background);

        mainPanel = new JPanel();
        mainPanel.setOpaque(false);

        titleLabel = new JLabel("Mühle – Startmenu");
        titleLabel.setFont(new Font("Roboto", 0, 30));
        titleLabel.setForeground(aliceblue);

        buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new FlowLayout());
        onlineButton = new JButton("Online");
        onlineButton.setPreferredSize(new Dimension(300, 300));
        onlineButton.setFont(new Font("Roboto", 0, 24));
        onlineButton.addMouseListener(this);
        offlineButton = new JButton("Offline");
        offlineButton.setFont(new Font("Roboto", 0, 24));
        offlineButton.addMouseListener(this);
        offlineButton.setPreferredSize(new Dimension(300, 300));

        buttonPanel.add(onlineButton);
        buttonPanel.add(offlineButton);
        mainPanel.add(titleLabel);
        mainPanel.add(buttonPanel);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setAlignmentY(Component.CENTER_ALIGNMENT);
        mainPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 10, 10, 10));

        this.add(mainPanel);
    }


    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == onlineButton) {
            OnlineMenuView onlineMenuView = new OnlineMenuView(viewManager, args, connection);
            viewManager.setCurrentView(onlineMenuView);
            onlineMenuView.setVisible(true);
            this.setVisible(false);
        }

        if (e.getSource() == offlineButton) {
            OfflineMenuView offlineMenuView = new OfflineMenuView(viewManager, args, connection);
            viewManager.setCurrentView(offlineMenuView);
            offlineMenuView.setVisible(true);
            this.setVisible(false);
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
