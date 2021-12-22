package View;

import Camera.HoughCirclesRun;
import Camera.jrpicam.RPiCamera;
import Camera.jrpicam.exceptions.FailedToRunRaspistillException;
import org.opencv.core.Core;
import org.opencv.core.Mat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CameraView extends View implements ActionListener {

    ImageIcon previewIcon;
    JPanel mainPanel = new JPanel();
    JLabel brightnessLabel = new JLabel("Helligkeit (0 bis 100):");
    JTextField brightnessTextfield = new JTextField();
    JLabel contrastLabel = new JLabel("Kontrast (-100 bis 100):");
    JLabel imageLabel;
    JTextField contrastTextfield = new JTextField();
    JButton previewButton = new JButton("Vorschau");
    JButton saveButton = new JButton("Speichern");
    ViewManager viewManager;
    HoughCirclesRun houghCirclesRun;
    RPiCamera previewCamera;


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
        JPanel brightnessPanel = new JPanel();
        brightnessPanel.add(brightnessLabel);
        brightnessTextfield.setColumns(3);
        brightnessPanel.add(brightnessTextfield);

        JPanel contrastPanel = new JPanel();
        contrastPanel.add(contrastLabel);
        contrastTextfield.setColumns(3);
        contrastPanel.add(contrastTextfield);

        previewButton.addActionListener(this);
        saveButton.addActionListener(this);

        mainPanel.add(brightnessPanel);
        mainPanel.add(contrastPanel);
        mainPanel.add(previewButton);
        mainPanel.add(saveButton);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
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
