package EiBotBoard;

import game.Position;

public class Connection {

    static int xCoord=0;
    static int yCoord=0;

    private static int player1PutNumber = 0;
    private static int player2PutNumber = 0;
    private static int player1KillNumber = 0;
    private static int player2KillNumber = 0;
    Ebb ebb;

    public Connection(Ebb ebb) {
        this.ebb = ebb;
    }

    public void move(Position from, Position to){
        goToPositionDirectly(from, 12);
        connectToStone(true);
        wait(1);
        goToPositionDirectly(to, 8);
        goHome(12);

    }

    public void put(Position position, int playerIndex){
        Position startPosition = new Position(0-playerIndex, player1PutNumber);
        player1PutNumber++;
        goToPositionDirectly(startPosition, 10);
        connectToStone(true);
        wait(1);
        try {
            xyMove(2,0, 4);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        goToPositionInLinesBring(position, 4);
        goHome(10);
    }

    public void kill(Position position, int playerIndex){

        goToPositionDirectly( position, 10);
        connectToStone(true);
        wait(1);
        Position startPosition = new Position(0-playerIndex, player1KillNumber);
        player1KillNumber++;
        goToPositionInLinesBack(position, 4);
        try {
            xyMove(RingAndFieldCoords.getCoord(startPosition).getX()+2-xCoord,
                    RingAndFieldCoords.getCoord(startPosition).getY()-yCoord,
                    4);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        goToPositionDirectly(startPosition, 4);
        goHome(10);
    }



    private void xyMove(int x, int y, int speed) throws MotorException{

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

    private void goToPositionDirectly(Position position, int speed){
        int x = RingAndFieldCoords.getCoord(position).getX();
        int y = RingAndFieldCoords.getCoord(position).getY();

        try {
            xyMove(x-xCoord, y-yCoord, speed);
        } catch (MotorException e) {
            e.printStackTrace();
        }

    }

    private void goToPositionInLinesBring(Position stonePosition, int speed){

        if ((stonePosition.getRing() == 0 || stonePosition.getRing() == 1)
                && (stonePosition.getField() <= 2)){
            System.out.println("B0");
            goToPositionDirectly(new Position(-10, 0), 10);
        }

        if ((stonePosition.getRing() == 2 && stonePosition.getField() <= 2)
                && (stonePosition.getField() == 0 || stonePosition.getField() == 1 || stonePosition.getField() == 2)){
            System.out.println("B1");
            goToPositionDirectly(new Position(-10, 1), 10);
        }

        if ((stonePosition.getField() == 7 || stonePosition.getField() == 3)
                || (stonePosition.getRing() == 2 && stonePosition.getField() >=4 && stonePosition.getField() <=6)){
            System.out.println("B2");
            goToPositionDirectly(new Position(-10, 2), 10);
        }

        if ((stonePosition.getRing() == 1 && stonePosition.getField() >= 4 && stonePosition.getField() <= 6)
                || (stonePosition.getRing() == 0 && stonePosition.getField() >= 4 && stonePosition.getField() <= 6)){
            System.out.println("B3");
            goToPositionDirectly(new Position(-10, 3), 10);
        }

        int x = RingAndFieldCoords.getCoord(stonePosition).getX();
        int y = RingAndFieldCoords.getCoord(stonePosition).getY();

        try {
            xyMove(x-xCoord, 0, speed);
            xyMove(0, y-yCoord, speed);
        } catch (MotorException e) {
            e.printStackTrace();
        }
    }

    private void goToPositionInLinesBack(Position stonePosition, int speed) {

        goToPositionDirectly(stonePosition, 10);

        int x = 0;
        int y = 0;

        if ((stonePosition.getRing() == 0 || stonePosition.getRing() == 1)
                && (stonePosition.getField() == 0 || stonePosition.getField() == 1 || stonePosition.getField() == 2)){
            System.out.println("B0");
            x = RingAndFieldCoords.getCoord(new Position(-10,0)).getX();
            y = RingAndFieldCoords.getCoord(new Position(-10,0)).getY();
        }

        if ((stonePosition.getRing() == 2 && stonePosition.getField() <= 2)
                && (stonePosition.getField() == 0 || stonePosition.getField() == 1 || stonePosition.getField() == 2)){
            x = RingAndFieldCoords.getCoord(new Position(-10,1)).getX();
            y = RingAndFieldCoords.getCoord(new Position(-10,1)).getY();
        }

        if ((stonePosition.getField() == 7 || stonePosition.getField() == 3)
                || (stonePosition.getRing() == 2 && stonePosition.getField() >=4 && stonePosition.getField() <=6)){
            x = RingAndFieldCoords.getCoord(new Position(-10,2)).getX();
            y = RingAndFieldCoords.getCoord(new Position(-10,2)).getY();
        }

        if ((stonePosition.getRing() == 1 && stonePosition.getField() >= 4 && stonePosition.getField() <= 6)
                || (stonePosition.getRing() == 0 && stonePosition.getField() >= 4 && stonePosition.getField() <= 6)){
            x = RingAndFieldCoords.getCoord(new Position(-10,3)).getX();
            y = RingAndFieldCoords.getCoord(new Position(-10,3)).getY();
        }

        try {
            xyMove(0, y-yCoord, speed);
            xyMove(x-xCoord, 0, speed);
        } catch (MotorException e) {
            e.printStackTrace();
        }

    }


    private void wait(int seconds){
        ebb.stepperMotorMove(seconds*1000, 0, 0);
    }


    private void goHome(int speed){
        connectToStone(false);
        ebb.stepperMotorMove(500,0,0);
        try {
            xyMove(-(xCoord-1), -(yCoord-1), speed);
            xyMove(-1, -1, 1);
        } catch (MotorException e) {
            e.printStackTrace();
        }

    }

    private void disableMotors(){
        ebb.enableMotor(0,0);
    }

    public void connectToStone(boolean connect){
        if (connect){
            ebb.setPenState(false);
        }
        else {
            ebb.setPenState(true);
        }
    }
}
