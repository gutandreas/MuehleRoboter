package EiBotBoard;

import Camera.HoughCirclesRun;
import game.Position;
import gnu.io.CommPortIdentifier;
import org.opencv.core.Core;

import java.util.Enumeration;

public class Controller {
    public static void main(String[] args) {

        System.out.println("hallo");
        String usbDevice = getUSBDevice();
        Ebb ebb = new Ebb(usbDevice);
        //Ebb ebb = new Ebb("/dev/cu.usbmodem142101");
        //Ebb ebb = new Ebb("/dev/ttyACM0");

        RingAndFieldCoordsCm ringAndFieldCoordsCm = new RingAndFieldCoordsCm();
        ebb.execute("SC," + 4 + "," + 30000);

        Connection connection = new Connection(ebb);

        connection.put(new Position(0,0), 1);

        /*ebb.enableMotor(0,0);


            for (int j = 0; j<1; j++){
                Position testPosition = new Position(0, j);
                connection.put(testPosition, 1);
                connection.move(testPosition, new Position(testPosition.getRing(), (testPosition.getField()+1)%8), false);
                connection.kill(new Position(testPosition.getRing(), (testPosition.getField()+1)%8), 1);
            }*/


    }

    static private String getUSBDevice(){
        Enumeration e = CommPortIdentifier.getPortIdentifiers();
        while(e.hasMoreElements()) {
            CommPortIdentifier com = (CommPortIdentifier) e.nextElement();
            if (com.getName().contains("cu.usbmodem")){
                return (com.getName());
            };
        }
        return null;
    }
}
