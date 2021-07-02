package EiBotBoard;

import game.Position;

public class Controller {
    public static void main(String[] args) {
        Ebb ebb = new Ebb("/dev/cu.usbmodem141101");
        RingAndFieldCoords ringAndFieldCoords = new RingAndFieldCoords();
        ebb.execute("SC," + 4 + "," + 30000);

        Connection connection = new Connection(ebb);


        //ebb.setPenState(true);
        //ebb.togglePen();
        //ebb.stepperMotorMove(1000,1000,1000);
        //ebb.enableMotor(0,0);
        //ebb.setPenState(false);
        //ebb.version();


        //Connection.move(ebb, new Position(0,0), new Position(0,7));

        connection.put(new Position(0,6), 1);
        connection.move(new Position(0,6), new Position(0,2), true);

        /*ebb.enableMotor(0,0);


            for (int j = 0; j<1; j++){
                Position testPosition = new Position(0, j);
                connection.put(testPosition, 1);
                connection.move(testPosition, new Position(testPosition.getRing(), (testPosition.getField()+1)%8), false);
                connection.kill(new Position(testPosition.getRing(), (testPosition.getField()+1)%8), 1);
            }*/


        /*connection.put(ebb, new Position(1, 0), 1);
        Connection.put(ebb, new Position(0,2), 1);
        Connection.kill(ebb, new Position(1,0), 1);
        Connection.kill(ebb, new Position(0,2), 1);*/





        //ebb.setPenState(true);
        //ebb.setPenState(false);


    }
}
