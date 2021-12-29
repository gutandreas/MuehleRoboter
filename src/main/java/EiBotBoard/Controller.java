package EiBotBoard;

import Camera.HoughCirclesRun;
import com.fazecast.jSerialComm.SerialPort;
import game.Position;
import gnu.io.CommPortIdentifier;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.opencv.core.Core;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;

public class Controller {
    public static void main(String[] args) {


        //String usbDevice = getUSBDevice();
        Ebb ebb = new Ebb();
        //Ebb ebb = new Ebb("/dev/cu.usbmodem142101");
        //Ebb ebb = new Ebb("/dev/ttyACM0");



        RingAndFieldCoordsCm ringAndFieldCoordsCm = new RingAndFieldCoordsCm();
        //ebb.execute("SC," + 4 + "," + 19500);


        Connection connection = new Connection(ebb);



        //connection.put(new Position(2,0), 1);
        //connection.move(new Position(2,0), new Position(2,5), true);

        /*connection.connectToStone(true);
        try {
            connection.xyMove(2,2, 6);
            connection.connectToStone(false);
            connection.goHome(5);

        } catch (MotorException e) {
            e.printStackTrace();
        }*/

        //connection.connectToStone(true);
        connection.connectToStone(false);

        //ebb.enableMotor(0,0);


           /* for (int j = 0; j<1; j++){
                Position testPosition = new Position(0, j);
                connection.put(testPosition, 1);
                connection.move(testPosition, new Position(testPosition.getRing(), (testPosition.getField()+1)%8), false);
                connection.kill(new Position(testPosition.getRing(), (testPosition.getField()+1)%8), 1);
            }*/


    }





    static public String getUSBDevice(){


        SerialPort port = SerialPort.getCommPorts()[0];
        System.out.println(port.getSystemPortName());


        Enumeration e = CommPortIdentifier.getPortIdentifiers();
        while(e.hasMoreElements()) {
            CommPortIdentifier com = (CommPortIdentifier) e.nextElement();
            System.out.println("Gefundener Port: " + com.getName());
            return com.getName();
        }
        return null;
    }
}
