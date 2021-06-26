package EiBotBoard;

public class AxidrawMethods {

    static int xCoord=0;
    static int yCoord=0;

    static void xyMove(Ebb ebb, int x, int y, int speed) throws MotorException{

        xCoord += x;
        yCoord += y;

        if (xCoord < 0 || yCoord < 0 || xCoord > 42 || yCoord > 28){
            throw new MotorException("Dieser Weg würde über den Rand hinaus führen");
        }
        else {

        int factorForCm = 800;
        int duration = (int) (Math.sqrt(x*x + y*y) / speed * 1000);
        int tempX;
        int tempY;

        if (Math.abs(x) > Math.abs(y)){
            double relation = (double) y/ (double) x;
            tempX = (factorForCm*x);
            tempY = (int) (tempX*relation);
        }
        else {
            double relation = (double) x/ (double) y;
            tempY = (factorForCm*y);
            tempX = (int) (tempY*relation);
        }


        int tempX2 = 0;
        int tempY2= 0;

        tempX2 += tempX;
        tempY2 += tempX;
        tempY2 -= tempY;
        tempX2 += tempY;

        System.out.println("Aktuelle Position: x=" + xCoord + "cm / y=" + yCoord + "cm" );
        ebb.stepperMotorMove(duration, tempX2, tempY2);}
    }
}
