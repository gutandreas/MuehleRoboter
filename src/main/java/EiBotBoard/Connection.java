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
    private Ebb ebb;

    public Connection(Ebb ebb) {
        this.ebb = ebb;

        goHome(5);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ebb.execute("SC," + 4 + "," + 12000); //pen position connect
        ebb.execute("SC," + 5 + "," + 25000); //pen position disconnect
    }

    public void put(Position position, int playerIndex){

        Position startPosition;

        if (playerIndex == 1){
            startPosition = new Position(0-playerIndex, player1PutNumber);
            player1PutNumber++;
        }
        else {
            startPosition = new Position(0-playerIndex, player2PutNumber);
            player2PutNumber++;
        }

        goToPositionDirectly(startPosition, 15);
        connectToStone(true);
        wait(1);

        goToPositionInLinesBring(position, playerIndex,6);
        goHome(15);
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
                xyMove(0, RingAndFieldToCm.getPositionInCm(to).getY()+2-yCoord, 6);
                xyMove(RingAndFieldToCm.getPositionInCm(to).getX()-xCoord, 0, 6);
                xyMove(0,-2,6);
            } catch (MotorException e) {
                System.out.println(e.getMessage());
            }
        }
        else {
            goToPositionDirectly(to, 8);
            }

        goHome(15);
    }



    public void kill(Position killPosition, int playerIndex){

        Position bringBackPosition;

        if (playerIndex == 1){
            bringBackPosition = new Position(0-playerIndex, player1KillNumber);
            player1KillNumber++;
        }
        else {
            bringBackPosition = new Position(0-playerIndex, player2KillNumber);
            player2KillNumber++;
        }

        goToPositionDirectly(killPosition, 10);
        connectToStone(true);
        wait(1);
        goToPositionInLinesBack(killPosition, bringBackPosition, playerIndex,6);
        goHome(15);
    }



    private void xyMove(int x, int y, int speed) throws MotorException{

        if (xCoord + x < 0 || yCoord + y < 0 || xCoord + x > 42 || yCoord + y > 28){
            throw new MotorException("Dieser Weg würde über den Rand hinaus führen");
        }
        else {

        xCoord += x;
        yCoord += y;

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
        int tempY2 = 0;

        tempX2 += tempX;
        tempY2 += tempX;
        tempY2 -= tempY;
        tempX2 += tempY;

        System.out.println("Aktuelle Position: x=" + xCoord + "cm / y=" + yCoord + "cm" );
        ebb.stepperMotorMove(duration, tempX2, tempY2);}
    }


    private void goToPositionDirectly(Position position, int speed){
        int x = RingAndFieldToCm.getPositionInCm(position).getX();
        int y = RingAndFieldToCm.getPositionInCm(position).getY();

        try {
            xyMove(x-xCoord, y-yCoord, speed);
        } catch (MotorException e) {
            System.out.println(e.getMessage());
        }
    }


    private void goToPositionInLinesBring(Position stonePosition, int playerindex, int speed){

        try {
            if (playerindex == 1){
                xyMove(-2,0, 6);
            }
            else {
                xyMove(2, 0, 6);
            }
        }
        catch (MotorException e){
            System.out.println(e.getMessage());
        }


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

        int x = RingAndFieldToCm.getPositionInCm(stonePosition).getX();
        int y = RingAndFieldToCm.getPositionInCm(stonePosition).getY();

        try {
            xyMove(x-xCoord, 0, speed);
            xyMove(0, y-yCoord, speed);
        } catch (MotorException e) {
            System.out.println(e.getMessage());
        }
    }


    private void goToPositionInLinesBack(Position killPosition, Position bringBackPosition, int playerindex, int speed) {

        goToPositionDirectly(killPosition, 10);

        int x = 0;
        int y = 0;

        if ((killPosition.getRing() == 0 || killPosition.getRing() == 1)
                && (killPosition.getField() == 0 || killPosition.getField() == 1 || killPosition.getField() == 2)){
            System.out.println("B0");
            x = RingAndFieldToCm.getPositionInCm(new Position(-10-playerindex,0)).getX();
            y = RingAndFieldToCm.getPositionInCm(new Position(-10-playerindex,0)).getY();
        }

        if ((killPosition.getRing() == 2 && killPosition.getField() <= 2)
                && (killPosition.getField() == 0 || killPosition.getField() == 1 || killPosition.getField() == 2)){
            x = RingAndFieldToCm.getPositionInCm(new Position(-10-playerindex,1)).getX();
            y = RingAndFieldToCm.getPositionInCm(new Position(-10-playerindex,1)).getY();
        }

        if ((killPosition.getField() == 7 || killPosition.getField() == 3)
                || (killPosition.getRing() == 2 && killPosition.getField() >=4 && killPosition.getField() <=6)){
            x = RingAndFieldToCm.getPositionInCm(new Position(-10-playerindex,2)).getX();
            y = RingAndFieldToCm.getPositionInCm(new Position(-10-playerindex,2)).getY();
        }

        if ((killPosition.getRing() == 1 && killPosition.getField() >= 4 && killPosition.getField() <= 6)
                || (killPosition.getRing() == 0 && killPosition.getField() >= 4 && killPosition.getField() <= 6)){
            x = RingAndFieldToCm.getPositionInCm(new Position(-10-playerindex,3)).getX();
            y = RingAndFieldToCm.getPositionInCm(new Position(-10-playerindex,3)).getY();
        }

        try {
            xyMove(0, y-yCoord, speed);
            xyMove(x-xCoord, 0, speed);

            if (playerindex == 1){
                xyMove(RingAndFieldToCm.getPositionInCm(bringBackPosition).getX()-2-xCoord,
                        RingAndFieldToCm.getPositionInCm(bringBackPosition).getY()-yCoord,
                        6);
            }
            else {
                xyMove(RingAndFieldToCm.getPositionInCm(bringBackPosition).getX()+2-xCoord,
                        RingAndFieldToCm.getPositionInCm(bringBackPosition).getY()-yCoord,
                        6);
            }
            goToPositionDirectly(bringBackPosition, 6);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }


    private void wait(int seconds){
        ebb.stepperMotorMove(seconds*1000, 0, 0);
    }


    private void goHome(int speed){
        connectToStone(false);
        ebb.stepperMotorMove(500,0,0); //warten für 0.5s
        try {
            xyMove(-(xCoord-1), -(yCoord-1), speed);
            xyMove(-1, -1, 1);
        } catch (MotorException e) {
            System.out.println(e.getMessage());
        }

    }


    public void resetVariables(){
        player1PutNumber = 0;
        player2PutNumber = 0;
        player1KillNumber = 0;
        player2KillNumber = 0;
    }


    private void disableMotors(){
        ebb.enableMotor(0,0);
    }


    public void connectToStone(boolean connect){
        if (connect){
            ebb.setPenState(true);
        }
        else {
            ebb.setPenState(false);
        }
    }
}
