package View;

import Camera.HoughCirclesRun;
import Camera.jrpicam.RPiCamera;
import Camera.jrpicam.enums.AWB;
import Camera.jrpicam.enums.DRC;
import Camera.jrpicam.exceptions.FailedToRunRaspistillException;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CameraView extends View implements ActionListener {

    private Color aliceblue = new Color(161, 210, 255);
    private Color background = new Color(60,60,60);
    private ImageIcon previewIcon;
    private JPanel mainPanel = new JPanel();
    private JLabel brightnessLabel = new JLabel("Helligkeit (0 bis 100):");
    private JTextField brightnessTextfield = new JTextField();
    private JButton brightnessPlusButton = new JButton("+");
    private JButton brightnessMinusButton = new JButton("–");
    private JLabel contrastLabel = new JLabel("Kontrast (-100 bis 100):");
    private JButton contrastPlusButton = new JButton("+");
    private JButton contrastMinusButton = new JButton("–");
    private JLabel imageLabel;
    private JLabel drcLabel = new JLabel("Dynamic Range Compression: ");
    private SwitchButton drcSwitchButton = new SwitchButton(Color.RED, Color.GREEN, aliceblue);
    private JLabel awbLabel = new JLabel("Automatic White Balance: ");
    private SwitchButton awbSwitchButton = new SwitchButton(Color.RED, Color.GREEN, aliceblue);
    private JTextField contrastTextfield = new JTextField();
    private JButton previewButton = new JButton("Vorschau");
    private JButton saveButton = new JButton("Speichern");
    private JLabel numberOfStonesLabel = new JLabel("Anzahl Steine:");
    private JTextField numberOfStonesTextfield = new JTextField();
    private JButton numberOfStonesPlusButton = new JButton("+");
    private JButton numberOfStonesMinusButton = new JButton("–");
    private JButton automaticScanButton = new JButton("Automatisch kalibrieren");
    private JButton closeWindowButton = new JButton("Zurück zum Spiel");
    private ViewManager viewManager;
    private HoughCirclesRun houghCirclesRun;
    private RPiCamera previewCamera;
    private Font font = new Font("Roboto", 0, 12);


    public CameraView(ViewManager viewManager) {
        this.viewManager = viewManager;
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);

        try {
            previewCamera = new RPiCamera("/home/pi/");
        } catch (FailedToRunRaspistillException e) {
            e.printStackTrace();
        }

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        houghCirclesRun = new HoughCirclesRun(viewManager.getrPiCamera());
        Mat src = houghCirclesRun.takePhoto(viewManager.getrPiCamera(), "vorschau", false);
        loadAndAddOrUpdatePreviewImage(src, false);

        brightnessLabel.setFont(font);
        brightnessLabel.setForeground(aliceblue);
        JPanel brightnessPanel = new JPanel();
        brightnessPanel.add(brightnessLabel);
        brightnessTextfield.setColumns(3);
        brightnessTextfield.setText(viewManager.getrPiCamera().getBrightness());
        brightnessPanel.add(brightnessTextfield);
        brightnessPlusButton.addActionListener(this);
        brightnessPlusButton.setFont(new Font("Roboto", 0, 13));
        brightnessPanel.add(brightnessPlusButton);
        brightnessMinusButton.addActionListener(this);
        brightnessMinusButton.setFont(new Font("Roboto", 0, 13));
        brightnessPanel.add(brightnessMinusButton);
        brightnessPanel.setOpaque(false);

        contrastLabel.setFont(font);
        contrastLabel.setForeground(aliceblue);
        JPanel contrastPanel = new JPanel();
        contrastPanel.add(contrastLabel);
        contrastTextfield.setColumns(3);
        contrastTextfield.setText(viewManager.getrPiCamera().getContrast());
        contrastPanel.add(contrastTextfield);
        contrastPlusButton.addActionListener(this);
        contrastPlusButton.setFont(new Font("Roboto", Font.BOLD, 13));
        contrastPanel.add(contrastPlusButton);
        contrastMinusButton.addActionListener(this);
        contrastMinusButton.setFont(new Font("Roboto", Font.BOLD, 13));
        contrastPanel.add(contrastMinusButton);
        contrastPanel.setOpaque(false);

        drcLabel.setFont(font);
        drcLabel.setForeground(aliceblue);
        JPanel drcPanel = new JPanel();
        drcPanel.add(drcLabel);
        drcPanel.add(drcSwitchButton);
        drcPanel.setOpaque(false);

        awbLabel.setFont(font);
        awbLabel.setForeground(aliceblue);
        JPanel awbPanel = new JPanel();
        awbPanel.add(awbLabel);
        awbPanel.add(awbSwitchButton);
        awbPanel.setOpaque(false);

        previewButton.addActionListener(this);
        previewButton.setPreferredSize(new Dimension(100, 50));
        previewButton.setFont(new Font("Roboto", 0, 13));
        saveButton.addActionListener(this);
        saveButton.setPreferredSize(new Dimension(100, 50));
        saveButton.setFont(new Font("Roboto", 0, 13));
        saveButton.setVisible(false);
        JPanel buttonPanel  = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(previewButton);
        buttonPanel.add(saveButton);
        buttonPanel.setOpaque(false);

        numberOfStonesLabel.setFont(font);
        numberOfStonesLabel.setForeground(aliceblue);
        numberOfStonesPlusButton.addActionListener(this);
        numberOfStonesPlusButton.setFont(new Font("Roboto", Font.BOLD, 13));
        numberOfStonesMinusButton.addActionListener(this);
        numberOfStonesMinusButton.setFont(new Font("Roboto", Font.BOLD, 13));
        numberOfStonesTextfield.setText("5");
        numberOfStonesTextfield.setColumns(2);
        JPanel automaticScanPanel1 = new JPanel();
        automaticScanPanel1.add(numberOfStonesLabel);
        automaticScanPanel1.add(numberOfStonesTextfield);
        automaticScanPanel1.add(numberOfStonesPlusButton);
        automaticScanPanel1.add(numberOfStonesMinusButton);
        automaticScanPanel1.setOpaque(false);

        JPanel automaticScanPanel2 = new JPanel();
        automaticScanButton.addActionListener(this);
        automaticScanButton.setPreferredSize(new Dimension(200, 50));
        automaticScanButton.setFont(new Font("Roboto", 0, 13));
        automaticScanPanel2.add(automaticScanButton);
        automaticScanPanel2.setOpaque(false);

        JPanel closeWindowPanel = new JPanel();
        closeWindowButton.addActionListener(this);
        closeWindowButton.setPreferredSize(new Dimension(200, 50));
        closeWindowButton.setFont(new Font("Roboto", 0, 13));
        closeWindowPanel.add(closeWindowButton);
        closeWindowPanel.setOpaque(false);

        JPanel settingsPanel = new JPanel();
        settingsPanel.add(brightnessPanel);
        settingsPanel.add(contrastPanel);
        settingsPanel.add(drcPanel);
        settingsPanel.add(awbPanel);
        settingsPanel.add(buttonPanel);
        settingsPanel.add(automaticScanPanel1);
        settingsPanel.add(automaticScanPanel2);
        settingsPanel.add(closeWindowPanel);
        settingsPanel.setOpaque(false);
        settingsPanel.setLayout(new BoxLayout(settingsPanel, BoxLayout.Y_AXIS));
        settingsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        mainPanel.add(imageLabel);
        mainPanel.add(settingsPanel);
        mainPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.setBackground(background);
        this.add(mainPanel);
        this.setVisible(true);
    }


    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == previewButton){
            System.out.println("preview Button");
            previewCamera.setBrightness(Integer.parseInt(brightnessTextfield.getText()));
            previewCamera.setContrast(Integer.parseInt(contrastTextfield.getText()));
            Mat preview = houghCirclesRun.takePhoto(previewCamera, "preview", false);
            loadAndAddOrUpdatePreviewImage(preview, true);

            if (drcSwitchButton.isSelected()){
                previewCamera.setDRC(DRC.HIGH);
            }
            else {
                previewCamera.setDRC(DRC.OFF);
            }

            if (awbSwitchButton.isSelected()){
                previewCamera.setAWB(AWB.FLASH);
            }
            else {
                previewCamera.setAWB(AWB.AUTO);
            }

            saveButton.setVisible(true);
        }

        if (e.getSource() == saveButton){
            System.out.println("save Button");
            viewManager.setrPiCamera(previewCamera);
            saveButton.setVisible(false);
        }

        if (e.getSource() == brightnessPlusButton){
            int value = Integer.parseInt(brightnessTextfield.getText());
            if (value + 5 <= 100){
                value = value + 5;
            }
            brightnessTextfield.setText("" + value);
        }

        if (e.getSource() == brightnessMinusButton){
            int value = Integer.parseInt(brightnessTextfield.getText());
            if (value - 5 >= 0){
                value = value - 5;
            }
            brightnessTextfield.setText("" + value);
        }

        if (e.getSource() == contrastPlusButton){
            int value = Integer.parseInt(contrastTextfield.getText());
            if (value + 5 <= 100){
                value = value + 5;
            }
            contrastTextfield.setText("" + value);
        }

        if (e.getSource() == contrastMinusButton){
            int value = Integer.parseInt(contrastTextfield.getText());
            if (value - 5 >= -100){
                value = value - 5;
            }
            contrastTextfield.setText("" + value);
        }

        if (e.getSource() == numberOfStonesPlusButton){
            int value = Integer.parseInt(numberOfStonesTextfield.getText());
            if (value + 1 <= 18){
                value = value + 1;
            }
            numberOfStonesTextfield.setText("" + value);
        }

        if (e.getSource() == numberOfStonesMinusButton){
            int value = Integer.parseInt(numberOfStonesTextfield.getText());
            if (value - 1 >= 1){
                value = value - 1;
            }
            numberOfStonesTextfield.setText("" + value);
        }

        if (e.getSource() == automaticScanButton){
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            calibrateAutomatically();
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }

        if (e.getSource() == closeWindowButton){
            this.dispose();
        }
    }

    private void loadAndAddOrUpdatePreviewImage(Mat src, boolean update){
        previewIcon = new ImageIcon(houghCirclesRun.paintCircles(src).getScaledInstance(460, 400, 0));

        imageLabel = new JLabel(previewIcon) {
            @Override
            public Dimension getPreferredSize() {
                Dimension size = new Dimension(460, 400);
                return size;
            }
        };

        if (update){
            mainPanel.remove(0);
        }

        mainPanel.add(imageLabel, 0);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private void calibrateAutomatically(){

        boolean confirmed = false;

        for (int brightness = 30; brightness <= 70; brightness += 10){
            for (int contrast = 20; contrast <= 80; contrast += 10){

                previewCamera.setBrightness(brightness);
                previewCamera.setContrast(contrast);

                Mat preview = houghCirclesRun.takePhoto(previewCamera, "preview", false);

                if (houghCirclesRun.detectCircles(preview).length == Integer.parseInt(numberOfStonesTextfield.getText())){
                    System.out.println("Alle Steine gefunden mit Helligkeit " + brightness + " und Kontrast " + contrast);
                    saveButton.setVisible(true);
                    loadAndAddOrUpdatePreviewImage(preview, true);

                    if (confirmed){
                        brightnessTextfield.setText("" + brightness);
                        contrastTextfield.setText("" + contrast);
                        automaticScanButton.setBackground(Color.GREEN);
                        return;}
                    else {
                        confirmed = true;
                    }
                }
                else {
                    System.out.println("Nicht alle Steine gefunden mit Helligkeit " + brightness + " und Kontrast " + contrast);
                    confirmed = false;
                }
            }
        }
        automaticScanButton.setBackground(Color.RED);
    }

}
