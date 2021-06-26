package EiBotBoard;

import game.Position;

public class Controller {
    public static void main(String[] args) {
        Ebb ebb = new Ebb("/dev/cu.usbmodem142101");
        RingAndFieldCoords ringAndFieldCoords = new RingAndFieldCoords();


        //ebb.setPenState(true);
        //ebb.togglePen();
        //ebb.stepperMotorMove(1000,1000,1000);
        //ebb.enableMotor(0,0);
        //ebb.setPenState(false);
        //ebb.version();


        AxidrawMethods.move(ebb, new Position(0,0), new Position(0,7));
        ebb.enableMotor(0,0);





        //ebb.setPenState(true);
        //ebb.setPenState(false);


    }
}
