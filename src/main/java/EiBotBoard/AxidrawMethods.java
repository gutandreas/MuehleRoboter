package EiBotBoard;

public class AxidrawMethods {

    static void xyMove(Ebb ebb, int x, int y){

        int tempX = 0;
        int tempY = 0;
        int duration;
        int speed = 1000;


        tempX += x;
        tempY += x;
        tempY -= y;
        tempX += y;

        ebb.stepperMotorMove(1000, tempX, tempY);



    }
}
