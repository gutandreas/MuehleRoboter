package EiBotBoard;

import gnu.io.CommPortIdentifier;

import java.io.File;
import java.util.Enumeration;
import java.util.Properties;

public class TestAxidrawConnection {

    public static void main(String[] args) {
        Enumeration e = CommPortIdentifier.getPortIdentifiers();
        while(e.hasMoreElements()) {
            CommPortIdentifier com = (CommPortIdentifier) e.nextElement();
            System.out.println("Test" + com.getName());
        }

        open("/dev/ttyUSB0", 9999);
    }

    static public void open(String port, Integer baud) {
        Properties properties = System.getProperties();
        String currentPorts = properties.getProperty("gnu.io.rxtx.SerialPorts", port);
        if (currentPorts.equals(port)) {
            properties.setProperty("gnu.io.rxtx.SerialPorts", port);
        } else {
            properties.setProperty("gnu.io.rxtx.SerialPorts", currentPorts + File.pathSeparator + port);
        }


    }


}
