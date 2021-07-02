package EiBotBoard;

import game.Position;

public class Controller {
    public static void main(String[] args) {
        Ebb ebb = new Ebb("/dev/cu.usbmodem141101");
        RingAndFieldCoords ringAndFieldCoords = new RingAndFieldCoords();
        ebb.execute("SC," + 4 + "," + 30000);

        Connection connection = new Connection(ebb);

        connection.put(new Position(0,1), 1);
        connection.move(new Position(0,1), new Position(1,4), true);
        connection.kill(new Position(1,4), 2);

        /*ebb.enableMotor(0,0);


            for (int j = 0; j<1; j++){
                Position testPosition = new Position(0, j);
                connection.put(testPosition, 1);
                connection.move(testPosition, new Position(testPosition.getRing(), (testPosition.getField()+1)%8), false);
                connection.kill(new Position(testPosition.getRing(), (testPosition.getField()+1)%8), 1);
            }*/


    }
}
