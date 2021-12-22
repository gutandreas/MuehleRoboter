package View;

import Camera.HoughCirclesRun;
import Camera.jrpicam.RPiCamera;
import Camera.jrpicam.exceptions.FailedToRunRaspistillException;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class CameraView extends View{

    ImageIcon previewIcon;
    JPanel mainPanel = new JPanel();
    JLabel brightnessLabel = new JLabel("Helligkeit (0 bis 100):");
    JTextField brightnessTextfield = new JTextField();
    JLabel contrastLabel = new JLabel("Kontrast (-100 bis 100):");
    JTextField contrastTextfield = new JTextField();
    JButton photoButton = new JButton("Preview");
    ViewManager viewManager;


    public CameraView(ViewManager viewManager) {
        this.viewManager = viewManager;
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        HoughCirclesRun houghCirclesRun = new HoughCirclesRun(viewManager.getrPiCamera());
        Mat src = houghCirclesRun.takePhoto(viewManager.rPiCamera, "vorschau");
        previewIcon = new ImageIcon(houghCirclesRun.paintCircles(src).getScaledInstance(630, 400, 0));

        /*JLabel imageLabel = new JLabel(previewIcon) {
            @Override
            public Dimension getPreferredSize() {
                *//*Dimension size = super.getPreferredSize();
                Dimension lmPrefSize = getLayout().preferredLayoutSize(this);
                size.width = Math.max(size.width, lmPrefSize.width);
                size.height = Math.max(size.height, lmPrefSize.height);*//*
                Dimension size = new Dimension(680, 400);
                return size;
            }
        };*/
        //mainPanel.add(imageLabel);
        mainPanel.add(brightnessLabel);
        mainPanel.add(brightnessTextfield);
        mainPanel.add(contrastLabel);
        mainPanel.add(contrastTextfield);
        mainPanel.add(photoButton);
        this.add(mainPanel);
        this.setVisible(true);
    }



}
