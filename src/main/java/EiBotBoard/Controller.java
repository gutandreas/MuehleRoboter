package EiBotBoard;

public class Controller {
    public static void main(String[] args) {
        Ebb ebb = new Ebb("/dev/cu.usbmodem142201");

        //ebb.setPenState(true);
        //ebb.togglePen();
        //ebb.stepperMotorMove(1000,1000,1000);
        //ebb.enableMotor(0,0);
        //ebb.setPenState(false);
        //ebb.version();
        AxidrawMethods.xyMove(ebb,2000,-3000);
        //ebb.setPenState(true);
        //ebb.setPenState(false);


    }
}
