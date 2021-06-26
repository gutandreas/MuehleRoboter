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
            AxidrawMethods.xyMove(ebb,10,0, 3);
            AxidrawMethods.xyMove(ebb, 2,5, 3);
            AxidrawMethods.xyMove(ebb, -2,-5, 1);
            AxidrawMethods.xyMove(ebb,-10,0, 1);

        }
        catch (MotorException e){
            e.printStackTrace();
        }

        //ebb.setPenState(true);
        //ebb.setPenState(false);


    }
}
