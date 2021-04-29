import com.fazecast.jSerialComm.SerialPort;

import java.io.IOException;

//https://www.youtube.com/watch?v=BdzzyEuUWYk

public class ConnectionArduino {

    public static void main(String[] args) throws IOException, InterruptedException {


        SerialPort sp = SerialPort.getCommPort("/dev/cu.usbmodem143201");
        sp.setComPortParameters(9600, 8, 1, 0);
        sp.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING, 0, 0);
        int befehlnummer = 0;

        if (sp.openPort()){
            System.out.println("Port is opened");
        }
        else {
            System.out.println("Port is NOT open");
        }

        while(true) {

            int pin = sp.getInputStream().read();
            System.out.println(pin);


            sp.getOutputStream().write(++befehlnummer);
            sp.getOutputStream().flush();
            System.out.println("Befehlnummer: " + befehlnummer);

                Thread.sleep(10);
            }

            /*if (sp.closePort()) {
                System.out.println("Port is closed");
            } else {
                System.out.println("Port is NOT closed");
            }*/

    }
}
