//Schritt damit es funktionierte: libopencv_java453.dylib von /usr/local/Cellar/opencv/4.5.3_1/share/java/opencv4 kopiert und in /Library/Java/Extensions/ eingefügt
package Camera;// https://docs.opencv.org/4.5.3/d4/d70/tutorial_hough_circle.html 30.7.2021

import Camera.jrpicam.RPiCamera;
import Camera.jrpicam.exceptions.FailedToRunRaspistillException;
import game.Board;
import game.Position;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedList;

public class HoughCirclesRun {





    private Board board = new Board();
    private Board oldBoard = new Board();
    Position[] changes = new Position[2];


    public Position detectPut(String[] args, Board board, int playerIndex){
        Mat src = takePhoto(args);
        Position[] positions = detectCircles(src);
        Position[] changes = getChanges(board, positions);
        if (changes[0] == null){
            System.out.println("Kein Stein gesetzt");
            throw new InvalidBoardException("Es wurde kein Stein hinzugefügt");
        }
        board.checkPut(changes[0]);
        return changes[0];
    }


    public Mat takePhoto(String[] args) {



        /*//String default_file = "/Users/andreasgut/Documents/EigenesProjekt/MuehleRoboter/Bilder/11.png";
        String default_file = "/Bilder/11.png";
        String filename = ((args.length > 0) ? args[0] : default_file);
        // Load an image
        Mat src = Imgcodecs.imread(filename, Imgcodecs.IMREAD_COLOR);
        // Check if image is loaded fine
        if( src.empty() ) {
            System.out.println("Error opening image!");
            System.out.println("Program Arguments: [image_name -- default "
                    + default_file +"] \n");
            System.exit(-1);
        }

        */

        RPiCamera rPiCamera = null;
        Mat src = null;

        try {
            rPiCamera = new RPiCamera("/home/pi/");
            rPiCamera.setContrast(0);
            rPiCamera.takeStill("test.png", 3280, 2464);
            src = Imgcodecs.imread("/home/pi/test.png");
        } catch (FailedToRunRaspistillException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return src;

        /*detectCircles(src);
        getChanges(board, oldBoard);


        Size size = new Size(1000, 500);
        Mat resizeImage = new Mat();
        Imgproc.resize(src, resizeImage, size);
        //HighGui.imshow("detected circles", resizeImage);
        //HighGui.waitKey();
        //System.exit(0);*/
    }

    private Position[] detectCircles(Mat src){
        Mat gray = new Mat();
        Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY);
        Imgproc.medianBlur(gray, gray, 5);
        Mat circles = new Mat();
        Imgproc.HoughCircles(gray, circles, Imgproc.HOUGH_GRADIENT, 1.0,
                (double)gray.rows()/16, // change this value to detect circles with different distances to each other
                100.0, 30.0, 10, 100); // change the last two parameters


        Position[] positions = new Position[circles.cols()];
        int counter = 0;

        // (min_radius & max_radius) to detect larger circles
        for (int x = 0; x < circles.cols(); x++) {
            double[] c = circles.get(0, x);
            Point center = new Point(Math.round(c[0]), Math.round(c[1]));
            System.out.println("Kreis an Position x=" + center.x + " / y=" + center.y + " erkannt");

            positions[counter] = getPosition(center.x, center.y, 100, 1);
            Scalar scalar;

            if (positions[counter] != null){
                scalar = new Scalar(0,255,0);
                for (Position pos : positions){
                    if (pos != null && pos!=positions[counter] && pos.equals(positions[counter])){
                        scalar = new Scalar(0,255,255);
                    }
                }
            }
            else {
                scalar = new Scalar(0,0,255);
            }
            counter++;

            // circle center
            Imgproc.circle(src, center, 0, new Scalar(255,255,255), 3, 8, 0 );
            // circle outline
            int radius = (int) Math.round(c[2]);
            Imgproc.circle(src, center, radius, scalar, 2, 8, 0 );


        }

        for (Position pos : positions){
            System.out.println(pos);
        }

        return positions;
    }

    private Position getPosition(double x, double y, double tolerance, int playerIndex) {

        RingAndFieldCoordsPx pxMap = new RingAndFieldCoordsPx();

        for (int ring = 0; ring<3; ring++){
            for (int field = 0; field<8; field++){
                Position position = new Position(ring, field);
                XyCoordsPx xyCoordsPx = RingAndFieldCoordsPx.getCoord(position);
                int xPos = xyCoordsPx.getX();
                int yPos = xyCoordsPx.getY();
                double delta = Math.sqrt((x-xPos)*(x-xPos) + (y-yPos)*(y-yPos));
                if (delta <= tolerance){
                    board.putStone(position, playerIndex);
                    System.out.println(board);
                    board.putStone(position, playerIndex);
                    return position;
                }
            }
        }

        return null;
    }

    private Position[] getChanges(Board board, Position[] positions) {

        for (int ring = 0; ring < 3; ring++) {
            for (int field = 0; field < 8; field++) {
                Position tempPosition = new Position(ring, field);
                if (oldBoard.isFieldFree(tempPosition) && !board.isFieldFree(tempPosition) && changes[0] == null) {
                    if (changes[0] == null){
                        changes[0] = tempPosition;
                        System.out.println("Stein hinzugefügt auf " + tempPosition);}
                    else {
                        throw new InvalidBoardException("Es wurde mehr als 1 Stein hinzugefügt");
                    }
                }
                if (!oldBoard.isFieldFree(tempPosition) && board.isFieldFree(tempPosition)) {
                    if (changes[1] == null){
                        changes[1] = tempPosition;
                        System.out.println("Stein entfernt auf " + tempPosition);}
                    else {
                        throw new InvalidBoardException("Es wurde mehr als 1 Stein entfernt");
                    }
                }
            }
        }
        return positions;
    }


}

