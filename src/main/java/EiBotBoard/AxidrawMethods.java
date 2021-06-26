package EiBotBoard;

import game.Position;

public class AxidrawMethods {

    static int xCoord=0;
    static int yCoord=0;

    public static void move(Ebb ebb, Position from, Position to){
        AxidrawMethods.goToPosition(ebb, from, 12);
        AxidrawMethods.connectToStone(ebb, true);
        AxidrawMethods.wait(ebb, 1);
        AxidrawMethods.goToPosition(ebb, to, 8);
        AxidrawMethods.connectToStone(ebb, false);
        AxidrawMethods.wait(ebb, 1);
        AxidrawMethods.goHome(ebb, 12);

    }

    private static void xyMove(Ebb ebb, int x, int y, int speed) throws MotorException{

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

    private static void goToPosition(Ebb ebb, Position position, int speed){
        System.out.println(RingAndFieldCoords.getCoord(position));
        int x = RingAndFieldCoords.getCoord(position).getX();
        int y = RingAndFieldCoords.getCoord(position).getY();

        try {
            xyMove(ebb, x-xCoord, y-yCoord, speed);
        } catch (MotorException e) {
            e.printStackTrace();
        }

    }

    private static void wait(Ebb ebb, int seconds){
        ebb.stepperMotorMove(seconds*1000, 0, 0);
    }


    private static void goHome(Ebb ebb, int speed){
        ebb.setPenState(true);
        ebb.stepperMotorMove(500,0,0);
        try {
            xyMove(ebb, -xCoord/2, -yCoord/2, speed);
            xyMove(ebb, -(xCoord-1), -(yCoord-1), speed/2);
            xyMove(ebb, -1, -1, 1);
        } catch (MotorException e) {
            e.printStackTrace();
        }

    }

    private static void disableMotors(Ebb ebb){
        ebb.enableMotor(0,0);
    }

    private static void connectToStone(Ebb ebb, boolean connect){
        if (connect){
            ebb.setPenState(false);
        }
        else {
            ebb.setPenState(true);
        }
    }
}
