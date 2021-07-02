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
        ebb.enableMotor(0,0);


            for (int j = 0; j<2; j++){
                Position testPosition = new Position(0, j);
                connection.put(testPosition, 1);
                connection.kill(testPosition, 1);
            }


        /*connection.put(ebb, new Position(1, 0), 1);
        Connection.put(ebb, new Position(0,2), 1);
        Connection.kill(ebb, new Position(1,0), 1);
        Connection.kill(ebb, new Position(0,2), 1);*/





        //ebb.setPenState(true);
        //ebb.setPenState(false);


    }
}
