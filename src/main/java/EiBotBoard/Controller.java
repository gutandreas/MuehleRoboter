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

        try {
            ebb.setPenState(false);
            AxidrawMethods.xyMove(ebb,10,0, 8);
            AxidrawMethods.xyMove(ebb, 2,5, 13);
            AxidrawMethods.goHome(ebb, 4);

        }
        catch (MotorException e){
            e.printStackTrace();
        }

        //ebb.setPenState(true);
        //ebb.setPenState(false);


    }
}
