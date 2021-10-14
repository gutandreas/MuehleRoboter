package EiBotBoard;

import game.Move;
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
        RingAndFieldCoordsCm ringAndFieldCoordsCm = new RingAndFieldCoordsCm();
        goHome(5);
        ebb.execute("SC," + 4 + "," + 30000);
    }

    public void move(Move move, boolean jump){

        Position from = move.getFrom();
        Position to = move.getTo();

        goToPositionDirectly(from, 10);
        connectToStone(true);
        wait(1);

        if (jump){
            try {
                xyMove(2,2,6);
                xyMove(0, RingAndFieldCoordsCm.getCoord(to).getY()+2-yCoord, 6);
                xyMove(RingAndFieldCoordsCm.getCoord(to).getX()-xCoord, 0, 6);
                xyMove(0,-2,6);
            } catch (MotorException e) {
                e.printStackTrace();
            }
        }
        else {
            goToPositionDirectly(to, 8);
            }

        goHome(15);

    }

    public void put(Position position, int playerIndex){


        Position startPosition;
        int shift;

        if (playerIndex == 1){
            shift = -2;
            startPosition = new Position(0-playerIndex, player1PutNumber);
            player1PutNumber++;
        }
        else {
            shift = 2;
            startPosition = new Position(0-playerIndex, player2PutNumber);
            player2PutNumber++;
        }

        goToPositionDirectly(startPosition, 15);
        connectToStone(true);
        wait(1);
        try {
            xyMove(shift,0, 6);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        goToPositionInLinesBring(position, playerIndex,6);
        goHome(15);

    }

    public void kill(Position position, int playerIndex){

        int shift;
        Position startPosition;

        goToPositionDirectly( position, 10);
        connectToStone(true);
        wait(1);

        if (playerIndex == 1){
            shift = -2;
            startPosition = new Position(0-playerIndex, player1KillNumber);
            player1KillNumber++;
        }
        else {
            shift = 2;
            startPosition = new Position(0-playerIndex, player2KillNumber);
            player2KillNumber++;
        }

        goToPositionInLinesBack(position, playerIndex,6);

        try {
            xyMove(RingAndFieldCoordsCm.getCoord(startPosition).getX()+shift-xCoord,
                    RingAndFieldCoordsCm.getCoord(startPosition).getY()-yCoord,
                    6);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        goToPositionDirectly(startPosition, 6);
        goHome(15);
    }



    public void xyMove(int x, int y, int speed) throws MotorException{

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

    private void  goToPositionDirectly(Position position, int speed){
        int x = RingAndFieldCoordsCm.getCoord(position).getX();
        int y = RingAndFieldCoordsCm.getCoord(position).getY();

        try {
            xyMove(x-xCoord, y-yCoord, speed);
        } catch (MotorException e) {
            e.printStackTrace();
        }

    }

    private void goToPositionInLinesBring(Position stonePosition, int playerindex, int speed){

        if ((stonePosition.getRing() == 0 || stonePosition.getRing() == 1)
                && (stonePosition.getField() <= 2)){
            System.out.println("B0");
            goToPositionDirectly(new Position(-10-playerindex, 0), 10);
        }

        if ((stonePosition.getRing() == 2 && stonePosition.getField() <= 2)
                && (stonePosition.getField() == 0 || stonePosition.getField() == 1 || stonePosition.getField() == 2)){
            System.out.println("B1");
            goToPositionDirectly(new Position(-10-playerindex, 1), 10);
        }

        if ((stonePosition.getField() == 7 || stonePosition.getField() == 3)
                || (stonePosition.getRing() == 2 && stonePosition.getField() >=4 && stonePosition.getField() <=6)){
            System.out.println("B2");
            goToPositionDirectly(new Position(-10-playerindex, 2), 10);
        }

        if ((stonePosition.getRing() == 1 && stonePosition.getField() >= 4 && stonePosition.getField() <= 6)
                || (stonePosition.getRing() == 0 && stonePosition.getField() >= 4 && stonePosition.getField() <= 6)){
            System.out.println("B3");
            goToPositionDirectly(new Position(-10-playerindex, 3), 10);
        }

        int x = RingAndFieldCoordsCm.getCoord(stonePosition).getX();
        int y = RingAndFieldCoordsCm.getCoord(stonePosition).getY();

        try {
            xyMove(x-xCoord, 0, speed);
            xyMove(0, y-yCoord, speed);
        } catch (MotorException e) {
            e.printStackTrace();
        }
    }

    private void goToPositionInLinesBack(Position stonePosition, int playerindex, int speed) {

        goToPositionDirectly(stonePosition, 10);

        int x = 0;
        int y = 0;

        if ((stonePosition.getRing() == 0 || stonePosition.getRing() == 1)
                && (stonePosition.getField() == 0 || stonePosition.getField() == 1 || stonePosition.getField() == 2)){
            System.out.println("B0");
            x = RingAndFieldCoordsCm.getCoord(new Position(-10-playerindex,0)).getX();
            y = RingAndFieldCoordsCm.getCoord(new Position(-10-playerindex,0)).getY();
        }

        if ((stonePosition.getRing() == 2 && stonePosition.getField() <= 2)
                && (stonePosition.getField() == 0 || stonePosition.getField() == 1 || stonePosition.getField() == 2)){
            x = RingAndFieldCoordsCm.getCoord(new Position(-10-playerindex,1)).getX();
            y = RingAndFieldCoordsCm.getCoord(new Position(-10-playerindex,1)).getY();
        }

        if ((stonePosition.getField() == 7 || stonePosition.getField() == 3)
                || (stonePosition.getRing() == 2 && stonePosition.getField() >=4 && stonePosition.getField() <=6)){
            x = RingAndFieldCoordsCm.getCoord(new Position(-10-playerindex,2)).getX();
            y = RingAndFieldCoordsCm.getCoord(new Position(-10-playerindex,2)).getY();
        }

        if ((stonePosition.getRing() == 1 && stonePosition.getField() >= 4 && stonePosition.getField() <= 6)
                || (stonePosition.getRing() == 0 && stonePosition.getField() >= 4 && stonePosition.getField() <= 6)){
            x = RingAndFieldCoordsCm.getCoord(new Position(-10-playerindex,3)).getX();
            y = RingAndFieldCoordsCm.getCoord(new Position(-10-playerindex,3)).getY();
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

    public Ebb getEbb() {
        return ebb;
    }
}
