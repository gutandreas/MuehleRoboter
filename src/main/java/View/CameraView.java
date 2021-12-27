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

    Color aliceblue = new Color(161, 210, 255);
    Color background = new Color(60,60,60);
    ImageIcon previewIcon;
    JPanel mainPanel = new JPanel();
    JLabel brightnessLabel = new JLabel("Helligkeit (0 bis 100):");
    JTextField brightnessTextfield = new JTextField();
    JButton brightnessPlusButton = new JButton("+");
    JButton brightnessMinusButton = new JButton("-");
    JLabel contrastLabel = new JLabel("Kontrast (-100 bis 100):");
    JButton contrastPlusButton = new JButton("+");
    JButton contrastMinusButton = new JButton("-");
    JLabel imageLabel;
    JLabel drcLabel = new JLabel("Dynamic Range Compression: ");
    SwitchButton drcSwitchButton = new SwitchButton(Color.RED, Color.GREEN, aliceblue);
    JLabel awbLabel = new JLabel("Automatic White Balance: ");
    SwitchButton awbSwitchButton = new SwitchButton(Color.RED, Color.GREEN, aliceblue);
    JTextField contrastTextfield = new JTextField();
    JButton previewButton = new JButton("Vorschau");
    JButton saveButton = new JButton("Speichern");
    JLabel numberOfStonesLabel = new JLabel("Anzahl Steine:");
    JTextField numberOfStonesTextfield = new JTextField();
    JButton numberOfStonesPlusButton = new JButton("+");
    JButton numberOfStonesMinusButton = new JButton("-");
    JButton automaticScanButton = new JButton("Automatisch einstellen");
    ViewManager viewManager;
    HoughCirclesRun houghCirclesRun;
    RPiCamera previewCamera;
    Font font = new Font("Roboto", 0, 12);

    public static void main(String[] args) {
        new CameraView(new ViewManager());
    }

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
        brightnessPanel.add(brightnessPlusButton);
        brightnessMinusButton.addActionListener(this);
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
        contrastPanel.add(contrastPlusButton);
        contrastMinusButton.addActionListener(this);
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
        numberOfStonesPlusButton.setFont(new Font("Roboto", 0, 13));
        numberOfStonesMinusButton.addActionListener(this);
        numberOfStonesMinusButton.setFont(new Font("Roboto", 0, 13));
        numberOfStonesTextfield.setText("5");
        numberOfStonesTextfield.setColumns(2);
        JPanel automaticScanPanel = new JPanel();
        automaticScanPanel.add(numberOfStonesLabel);
        automaticScanPanel.add(numberOfStonesTextfield);
        automaticScanPanel.add(numberOfStonesPlusButton);
        automaticScanPanel.add(numberOfStonesMinusButton);
        automaticScanPanel.setOpaque(false);

        automaticScanButton.addActionListener(this);
        automaticScanButton.setFont(new Font("Roboto", 0, 10));


        JPanel settingsPanel = new JPanel();
        settingsPanel.add(brightnessPanel);
        settingsPanel.add(contrastPanel);
        settingsPanel.add(drcPanel);
        settingsPanel.add(awbPanel);
        settingsPanel.add(buttonPanel);
        settingsPanel.add(automaticScanPanel);
        settingsPanel.add(automaticScanButton);
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
            Mat preview = houghCirclesRun.takePhoto(previewCamera, "preview", true);
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
            scanAutomatically();
        }
    }

    private void loadAndAddOrUpdatePreviewImage(Mat src, boolean update){
        previewIcon = new ImageIcon(houghCirclesRun.paintCircles(src).getScaledInstance(460, 400, 0));

        imageLabel = new JLabel(previewIcon) {
            @Override
            public Dimension getPreferredSize() {
                /*Dimension size = super.getPreferredSize();
                Dimension lmPrefSize = getLayout().preferredLayoutSize(this);
                size.width = Math.max(size.width, lmPrefSize.width);
                size.height = Math.max(size.height, lmPrefSize.height);*/
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

    private void scanAutomatically(){

        for (int brightness = 30; brightness <= 70; brightness += 10){
            for (int contrast = 20; contrast <= 80; contrast += 10){

                previewCamera.setBrightness(brightness);
                previewCamera.setContrast(contrast);

                Mat preview = houghCirclesRun.takePhoto(previewCamera, "preview", false);
                System.out.println(houghCirclesRun.detectCircles(preview).length);

                if (houghCirclesRun.detectCircles(preview).length == Integer.parseInt(numberOfStonesTextfield.getText())){
                    System.out.println("Alle Steine gefunden mit Helligkeit " + brightness + " und Kontrast " + contrast);
                    saveButton.setVisible(true);
                    brightnessTextfield.setText("" + brightness);
                    contrastTextfield.setText("" + contrast);
                    loadAndAddOrUpdatePreviewImage(preview, true);
                    return;
                }
                else {
                    System.out.println("Nicht alle Steine gefunden mit Helligkeit " + brightness + " und Kontrast " + contrast);
                }
            }
        }



    }
}
