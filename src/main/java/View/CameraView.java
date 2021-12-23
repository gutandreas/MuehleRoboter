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
    JLabel contrastLabel = new JLabel("Kontrast (-100 bis 100):");
    JLabel imageLabel;
    JLabel drcLabel = new JLabel("Dynamic Range Compression: ");
    SwitchButton drcSwitchButton = new SwitchButton(Color.RED, Color.GREEN, aliceblue);
    JLabel awbLabel = new JLabel("Automatic White Balance: ");
    SwitchButton awbSwitchButton = new SwitchButton(Color.RED, Color.GREEN, aliceblue);
    JTextField contrastTextfield = new JTextField();
    JButton previewButton = new JButton("Vorschau");
    JButton saveButton = new JButton("Speichern");
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
        Mat src = houghCirclesRun.takePhoto(viewManager.rPiCamera, "vorschau");
        loadAndAddOrUpdatePreviewImage(src, false);

        brightnessLabel.setFont(font);
        brightnessLabel.setForeground(aliceblue);
        JPanel brightnessPanel = new JPanel();
        brightnessPanel.add(brightnessLabel);
        brightnessTextfield.setColumns(3);
        brightnessPanel.add(brightnessTextfield);
        brightnessPanel.setOpaque(false);

        contrastLabel.setFont(font);
        contrastLabel.setForeground(aliceblue);
        JPanel contrastPanel = new JPanel();
        contrastPanel.add(contrastLabel);
        contrastTextfield.setColumns(3);
        contrastPanel.add(contrastTextfield);
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
        saveButton.addActionListener(this);

        mainPanel.add(brightnessPanel);
        mainPanel.add(contrastPanel);
        mainPanel.add(drcPanel);
        mainPanel.add(awbPanel);
        mainPanel.add(previewButton);
        mainPanel.add(saveButton);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
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
            Mat preview = houghCirclesRun.takePhoto(previewCamera, "preview");
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
        }

        if (e.getSource() == saveButton){
            System.out.println("save Button");
            viewManager.getrPiCamera().setBrightness(Integer.parseInt(brightnessTextfield.getText()));
            viewManager.getrPiCamera().setContrast(Integer.parseInt(contrastTextfield.getText()));

        }
    }

    private void loadAndAddOrUpdatePreviewImage(Mat src, boolean update){
        previewIcon = new ImageIcon(houghCirclesRun.paintCircles(src).getScaledInstance(230, 200, 0));

        imageLabel = new JLabel(previewIcon) {
            @Override
            public Dimension getPreferredSize() {
                /*Dimension size = super.getPreferredSize();
                Dimension lmPrefSize = getLayout().preferredLayoutSize(this);
                size.width = Math.max(size.width, lmPrefSize.width);
                size.height = Math.max(size.height, lmPrefSize.height);*/
                Dimension size = new Dimension(230, 200);
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
}
