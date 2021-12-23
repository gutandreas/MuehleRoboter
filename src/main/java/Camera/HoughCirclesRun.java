//Schritt damit es funktionierte: libopencv_java453.dylib von /usr/local/Cellar/opencv/4.5.3_1/share/java/opencv4 kopiert und in /Library/Java/Extensions/ eingefügt
package Camera;// https://docs.opencv.org/4.5.3/d4/d70/tutorial_hough_circle.html 30.7.2021

import Camera.jrpicam.RPiCamera;
import Camera.jrpicam.exceptions.FailedToRunRaspistillException;
import View.CameraView;
import View.GameView;
import game.Board;
import game.Move;
import game.Position;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedList;

public class HoughCirclesRun {


    GameView gameView;
    RPiCamera rPiCamera;


    public HoughCirclesRun(GameView gameView, RPiCamera camera) {
       this.gameView = gameView;
       rPiCamera = camera;

    }

    public HoughCirclesRun(RPiCamera camera) {
        rPiCamera = camera;
    }

    public Position detectPut(String[] args, Board board){
        Mat src = takePhoto(rPiCamera, "spielfoto");
        Position[] positions = detectCircles(src, true);
        Position[] changes = getChanges(board, positions);

        if (changes[0] != null && changes[1] != null){
            throw new InvalidBoardException("Es wurde unerlaubt ein Stein auf " + changes[1] + " verschoben");
        }

        if (changes[0] == null && changes[1] != null){
            throw new InvalidBoardException("Es wurde unerlaubt ein Stein von " + changes[1] + " entfernt");
        }

        if (changes[0] == null && changes[1] == null){
            throw new InvalidBoardException("Es wurde kein Stein hinzugefügt");
        }

        if (!board.checkPut(changes[0])){
            throw new InvalidBoardException("Es wurde ein unerlaubter Stein gesetzt");
        }

        return changes[0];
    }

    public Move detectMove(String[] args, Board board){
        Mat src = takePhoto(rPiCamera, "spielfoto");
        Position[] positions = detectCircles(src, false);
        Position[] changes = getChanges(board, positions);

        if (changes[0] != null && changes[1] == null){
            throw new InvalidBoardException("Es wurde unerlaubt ein Stein auf " + changes[0] + " hinzugefügt");
        }

        if (changes[0] == null && changes[1] != null){
            throw new InvalidBoardException("Es wurde unerlaubt ein Stein von " + changes[1] + " entfernt");
        }

        if (changes[0] == null && changes[1] == null){
            throw new InvalidBoardException("Es wurde kein Stein verschoben");
        }

        Move move = new Move(changes[1], changes[0]);

        boolean allowedToJump = board.countPlayersStones(0) == 3; // Achtung: PlayerIndex hardcoded
        if (!board.checkMove(move, allowedToJump)){
            throw new InvalidBoardException("Es wurde ein unerlaubter Zug gemacht");
        }

        return move;

    }

    public Position detectKill(String[] args, Board board){
        Mat src = takePhoto(rPiCamera, "spielfoto");
        Position[] positions = detectCircles(src, false);
        Position[] changes = getChanges(board, positions);

        if (changes[0] != null && changes[1] != null){
            throw new InvalidBoardException("Es wurde unerlaubt ein Stein auf " + changes[0] + " verschoben");
        }

        if (changes[0] != null && changes[1] == null){
            throw new InvalidBoardException("Es wurde unerlaubt ein Stein auf " + changes[0] + " hinzugefügt");
        }

        if (changes[0] == null && changes[1] == null){
            throw new InvalidBoardException("Es wurde kein Stein entfernt");
        }

        if (!board.checkKill(changes[1], gameView.getGame().getOtherPlayerIndex())){
            throw new InvalidBoardException("Es wurde ein unerlaubter Stein von " + changes[1] + " entfernt");
        }

        return changes[1];
    }





    public Mat takePhoto(RPiCamera camera, String name) {


        Mat src = null;

        try {
            camera.takeStill(name + ".png", 3280, 2464);
            src = Imgcodecs.imread("/home/pi/" + name + ".png");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return src;

    }

    public Position[] detectCircles(Mat src, boolean paintCircles){
        Mat gray = new Mat();
        Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY);
        Imgproc.medianBlur(gray, gray, 5);
        Mat circles = new Mat();
        Imgproc.HoughCircles(gray, circles, Imgproc.HOUGH_GRADIENT, 1.0,
                (double)gray.rows()/16, // change this value to detect circles with different distances to each other
                100.0, 30.0, 22, 40); // change the last two parameters


        Position[] positions = new Position[circles.cols()];
        int counter = 0;

        // (min_radius & max_radius) to detect larger circles
        for (int x = 0; x < circles.cols(); x++) {
            double[] c = circles.get(0, x);
            Point center = new Point(Math.round(c[0]), Math.round(c[1]));
            System.out.println("Kreis an Position x=" + center.x + " / y=" + center.y + " erkannt");


            Position position = getPosition(center.x, center.y, 100);
            if (position != null){
                positions[counter] = position;
                counter++;
            }
        }

        for (Position pos : positions){
            System.out.println(pos);
        }

        return positions;
    }

    public Image paintCircles(Mat src){
        Mat gray = new Mat();
        Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY);
        Imgproc.medianBlur(gray, gray, 5);
        Mat circles = new Mat();
        /*Imgproc.HoughCircles(gray, circles, Imgproc.HOUGH_GRADIENT, 1.0,
                (double)gray.rows()/16, // change this value to detect circles with different distances to each other
                100.0, 30.0, 22, 40); // change the last two parameters*/

        Imgproc.HoughCircles(gray, circles, Imgproc.HOUGH_GRADIENT, 1.0,
                (double)gray.rows()/16, // change this value to detect circles with different distances to each other
                100.0, 30.0, 70, 85); // change the last two parameters

        Position[] positions = new Position[circles.cols()];
        int counter = 0;

        // (min_radius & max_radius) to detect larger circles
        for (int x = 0; x < circles.cols(); x++) {
            double[] c = circles.get(0, x);
            Point center = new Point(Math.round(c[0]), Math.round(c[1]));
            System.out.println("Kreis an Position x=" + center.x + " / y=" + center.y + " erkannt");


            Position position = getPosition(center.x, center.y, 100);
            if (position != null){
                positions[counter] = position;


            Scalar scalar;

            if (positions[counter] != null) {
                scalar = new Scalar(0, 255, 0);
                for (Position pos : positions) {
                    if (pos != null && pos != positions[counter] && pos.equals(positions[counter])) {
                        scalar = new Scalar(0, 255, 255);
                    }
                }
            } else {
                scalar = new Scalar(0, 0, 255);
            }

            // circle center
            Imgproc.circle(src, center, 0, new Scalar(255, 255, 255), 3, 8, 0);
            // circle outline
            int radius = (int) Math.round(c[2]);
            Imgproc.circle(src, center, radius, scalar, 30, 8, 0);

            counter++;
            }

        }

        Image img = HighGui.toBufferedImage(src);

        return img;
    }

    private Position getPosition(double x, double y, double tolerance) {

        RingAndFieldCoordsPx pxMap = new RingAndFieldCoordsPx();

        for (int ring = 0; ring<3; ring++){
            for (int field = 0; field<8; field++){
                Position position = new Position(ring, field);
                XyCoordsPx xyCoordsPx = RingAndFieldCoordsPx.getCoord(position);
                int xPos = xyCoordsPx.getX();
                int yPos = xyCoordsPx.getY();
                double delta = Math.sqrt((x-xPos)*(x-xPos) + (y-yPos)*(y-yPos));
                if (delta <= tolerance){
                    return position;
                }
            }
        }

        return null;
    }

    private Position[] getChanges(Board board, Position[] positions) {

        Board tempBoard = new Board();

        for (Position p : positions){
            if (p != null){
                tempBoard.putStone(p, 3);}
        }

        System.out.println("Gescanntes Board: \n" + tempBoard);
        Position[] changes = new Position[2];

        for (int ring = 0; ring < 3; ring++) {
            for (int field = 0; field < 8; field++) {
                Position tempPosition = new Position(ring, field);
                if (board.isFieldFree(tempPosition) && !tempBoard.isFieldFree(tempPosition)) {
                    if (changes[0] == null){
                        changes[0] = tempPosition;
                        System.out.println("Stein hinzugefügt auf " + tempPosition);}
                    else {
                        throw new InvalidBoardException("Es wurde mehr als 1 Stein hinzugefügt");
                    }
                }
                if (!board.isFieldFree(tempPosition) && tempBoard.isFieldFree(tempPosition)) {
                    if (changes[1] == null){
                        changes[1] = tempPosition;
                        System.out.println("Stein entfernt auf " + tempPosition);}
                    else {
                        throw new InvalidBoardException("Es wurde mehr als 1 Stein entfernt");
                    }
                }
            }
        }
        return changes;
    }


}

