package game;

import View.GameView;
import org.json.JSONObject;

import javax.swing.*;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;

public class Game {

    private final Player player0;
    private final Player player1;
    private Player winner;
    private int round;
    private final int NUMBEROFSTONES = 9;
    private Player currentPlayer;
    private final Board board;
    private boolean putPhase = true;
    private boolean movePhase = false;
    private boolean movePhaseTake = true;
    private boolean movePhaseRelase = false;
    private boolean killPhase = false;
    private boolean player2starts;
    private boolean clickOkay = true;
    private String gameCode;
    private GameView gameView;


    ArrayList<Player> playerArrayList = new ArrayList<>();

    public Game(GameView gameView, Player player0, Player player1) {
        this.gameView = gameView;
        this.player0 = player0;
        this.player1 = player1;
        playerArrayList.add(0, player0);
        playerArrayList.add(1, player1);
        round = 0;
        currentPlayer=playerArrayList.get(0);
        board = new Board();

    }

    public Game(GameView gameView, Player player0, Player player1, String gameCode) {
        this.gameView = gameView;
        this.player0 = player0;
        this.player1 = player1;
        playerArrayList.add(0, player0);
        playerArrayList.add(1, player1);
        round = 0;
        currentPlayer=playerArrayList.get(0);
        this.gameCode = gameCode;
        board = new Board();
    }


    /*public Game(Player player0, boolean player2starts) {
        this.player0 = player0;
        this.player1 = new ComputerPlayer(viewManager, "COMPUTER");
        this.player2starts = player2starts;
        playerArrayList.add(0, player0);
        playerArrayList.add(1, player1);
        round = 0;
        if (player2starts){
            currentPlayer=playerArrayList.get(1);}
        else {
            currentPlayer=playerArrayList.get(0);}
        board = new Board(this);
    }*/


    public Player getCurrentPlayer() {
        return currentPlayer;
    }


    public Player getOtherPlayer() {
        return playerArrayList.get(getOtherPlayerIndex());
    }


    public Player getPlayer0() {
        return player0;
    }

    public int getRound() {
        return round;
    }

    public Player getPlayer1() {
        return player1;
    }

    public String getGameCode() {
        return gameCode;
    }

    public boolean isPutPhase() {
        return putPhase;
    }

    public boolean isMovePhase() {
        return movePhase;
    }

    public boolean isKillPhase() {
        return killPhase;
    }

    public void setKillPhase(boolean killPhase) {
        this.killPhase = killPhase;
    }

    public int getCurrentPlayerIndex(){
        return currentPlayer.equals(playerArrayList.get(0)) ? 0 : 1;
    }


    public int getOtherPlayerIndex(){
        return currentPlayer.equals(playerArrayList.get(0)) ? 1 : 0;
    }


    public void updateCurrentPlayer(){
        if(player2starts){
            currentPlayer = playerArrayList.get((round+1)%2);}
        else {
            currentPlayer = playerArrayList.get(round%2);
        }
    }


    public Board getBoard() {
        return board;
    }

    public void setGameCode(String gameCode) {
        this.gameCode = gameCode;
    }

    public void increaseRound(){
        round++;
        setGamesPhaseBooleans();
    }

    private void setGamesPhaseBooleans(){
        if (round >= NUMBEROFSTONES*2){
            putPhase = false;
            movePhase = true;}
    }

    public void changeToMovePhase(){
        movePhase = true;
    }

    /*





    public void nextStep(Position clickedPosition) {


        if (clickOkay){

            clickOkay = false;

            if (round == NUMBEROFSTONES*2){
                viewManager.getScoreView().updatePhase("Steine verschieben");
            }

            if (killPhase){
                if (board.checkKill(clickedPosition, getOtherPlayerIndex())){
                    ((HumanPlayer) currentPlayer).setClickedKillPosition(clickedPosition);
                    currentPlayer.kill(board, getCurrentPlayerIndex(), getOtherPlayerIndex());
                    viewManager.getFieldView().graphicKill(clickedPosition);
                    updateGameState(false, true);
                    if (currentPlayer instanceof ComputerPlayer){
                        callComputer();
                    }
                    clickOkay = true;
                    killPhase = false;
                    return;}
                else {
                    System.out.println("Ungültiger Kill");
                    viewManager.getLogView().setStatusLabel("Auf diesem Feld kann kein Stein entfernt werden.");
                    clickOkay = true;
                    return;
                }
            }

            if (putPhase){
                if (board.checkPut(clickedPosition)){

                    ((HumanPlayer) currentPlayer).setClickedPutPosition(clickedPosition);
                    currentPlayer.put(board, getCurrentPlayerIndex());
                    viewManager.getFieldView().graphicPut(clickedPosition, getCurrentPlayerIndex(), 0);

                    if (board.checkMorris(clickedPosition) && board.isThereStoneToKill(getOtherPlayerIndex())){
                        killPhase = true;
                        clickOkay = true;
                        viewManager.getFieldView().setKillCursor();
                        viewManager.getLogView().setStatusLabel(currentPlayer.getName() + " darf einen gegnerischen Stein entfernen.");
                        return;
                    }
                    else {
                        updateGameState(true, false);
                        if (currentPlayer instanceof ComputerPlayer){
                            callComputer();
                        }
                        else {
                            clickOkay = true;
                        }
                        return;
                    }

                }
                else {
                    System.out.println("Ungültiger Put, Feld ist nicht frei");
                    viewManager.getLogView().setStatusLabel("Dieses Feld ist nicht frei.");
                    clickOkay = true;
                    return;
                }
            }

            if (movePhase){
                if (movePhaseTake){
                    ((HumanPlayer) currentPlayer).setClickedMovePositionTakeStep(clickedPosition);
                    movePhaseTake = false;
                    movePhaseRelase = true;
                    clickOkay = true;
                    viewManager.getFieldView().setPutCursor();
                    viewManager.getFieldView().graphicKill(clickedPosition);
                    return;
                }
                if (movePhaseRelase){
                    ((HumanPlayer) currentPlayer).setClickedMovePositionReleaseStep(clickedPosition);
                    boolean allowedToJump = board.countPlayersStones(getCurrentPlayerIndex()) == 3;
                    Move move = currentPlayer.move(board, getCurrentPlayerIndex(), allowedToJump);
                    if (board.checkMove(move,allowedToJump)){
                        board.move(move, getCurrentPlayerIndex());
                        viewManager.getFieldView().graphicMove(move, getCurrentPlayerIndex());
                        updateGameState(false,false);
                        movePhaseTake = true;
                        movePhaseRelase = false;
                        if (board.checkMorris(move.getTo()) && board.isThereStoneToKill(getOtherPlayerIndex())){
                            killPhase = true;
                            clickOkay = true;
                            return;
                        }
                        if (currentPlayer instanceof ComputerPlayer){
                            callComputer();
                            return;
                        }
                    }
                    else {
                        System.out.println("Kein gültiger Move");
                        viewManager.getLogView().setStatusLabel("Das ist kein gültiger Zug");
                        clickOkay = true;
                    }
                }


            }

            System.out.println(board);



            //viewManager.getScoreView().setPlayerLabelEffects(); // funktioniert noch nicht wie gewünscht

            *//*Position positionToCheckMorris = null;

            if (putPhase){
                positionToCheckMorris = put(position);
            }

            if (movePhase && board.checkIfAbleToMove(getCurrentPlayerIndex())){
                positionToCheckMorris = move();
            }

            if (positionToCheckMorris != null
                    && board.checkMorris(positionToCheckMorris)
                    && (board.isThereStoneToKill(getOtherPlayerIndex())
                    || (board.countPlayersStones(getOtherPlayerIndex())==3) && movePhase)){
                kill();
            }

            if (movePhase &&
                    (board.countPlayersStones(getCurrentPlayerIndex()) <= 2
                            || !board.checkIfAbleToMove(getCurrentPlayerIndex()))){
                winGame();
                return;
            }

            increaseRound();
            updateCurrentPlayer();*//*}
        else {
            System.out.println("Kein Klick möglich");
            viewManager.getLogView().setStatusLabel("Warten Sie bis Sie an der Reihe sind.");
        }

    }

    public void callComputer(){
        if (round < NUMBEROFSTONES*2){
            boolean kill = false;
            Position computerPutPosition = currentPlayer.put(board,getCurrentPlayerIndex());
            board.putStone(computerPutPosition, getCurrentPlayerIndex());
            viewManager.getFieldView().graphicPut(computerPutPosition, getCurrentPlayerIndex(), 0);
            sendPutAsHTTP(computerPutPosition);
            if (board.checkMorris(computerPutPosition) && board.isThereStoneToKill(getOtherPlayerIndex())){
                Position computerKillPosition = currentPlayer.kill(board,getCurrentPlayerIndex(), getOtherPlayerIndex());
                board.clearStone(computerKillPosition);
                viewManager.getFieldView().graphicKill(computerKillPosition);
                kill = true;
            }
            clickOkay = true;
            updateGameState(true, kill);
            return;
        }
        else {
            boolean allowedToJump = board.countPlayersStones(getCurrentPlayerIndex()) == 3;
            boolean kill = false;
            Move computerMove = currentPlayer.move(board, getCurrentPlayerIndex(), allowedToJump);
            if (board.checkMove(computerMove, allowedToJump)){
                board.move(computerMove, getCurrentPlayerIndex());
                viewManager.getFieldView().graphicMove(computerMove, getCurrentPlayerIndex());
                if (board.checkMorris(computerMove.getTo()) && board.isThereStoneToKill(getOtherPlayerIndex())){
                    Position computerKillPosition = currentPlayer.kill(board,getCurrentPlayerIndex(), getOtherPlayerIndex());
                    board.clearStone(computerKillPosition);
                    viewManager.getFieldView().graphicKill(computerKillPosition);
                    kill = true;
                }
                clickOkay = true;
                updateGameState(false, kill);
                return;
            }
        }

    }

    private void sendPutAsHTTP(Position position){
        HttpClient client = HttpClient.newBuilder().build();
        JSONObject jsonObject = new JSONObject();
        System.out.println(viewManager.getGame().getCurrentPlayer().getUuid());

        jsonObject.put("gameCode", gameCode);
        jsonObject.put("playerUuid", getCurrentPlayer().getUuid());
        jsonObject.put("putRing", position.getRing());
        jsonObject.put("putField", position.getField());
        jsonObject.put("callComputer", false);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/controller/game/controller/put"))
                .POST(HttpRequest.BodyPublishers.ofString(jsonObject.toString()))
                .build();

        HttpResponse<?> response = null;

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }





    }

    private void updateGameState(boolean put, boolean kill){
        if (put){
            viewManager.getScoreView().increaseStonesPut();
        }
        if (kill){
            viewManager.getScoreView().increaseStonesKilled();
            viewManager.getScoreView().increaseStonesLost();
            checkWinner();
        }

        increaseRound();
        updateCurrentPlayer();
        setGamesPhaseBooleans();
        viewManager.getLogView().setStatusLabel(currentPlayer.getName() + " ist an der Reihe");

        if (round < NUMBEROFSTONES*2){
            viewManager.getFieldView().setPutCursor();
        }
        else {
            viewManager.getFieldView().setMoveCursor();
        }

        System.out.println(board);
    }


    private Position put(Position position){
        Position putPosition = currentPlayer.put(board, getCurrentPlayerIndex());
        Position positionToCheckMorris;

        if (board.checkPut(putPosition)){
            board.putStone(putPosition, getCurrentPlayerIndex());

            if (currentPlayer instanceof ComputerPlayer){
                viewManager.getFieldView().graphicPut(putPosition, getCurrentPlayerIndex(), 500);
            }


            viewManager.getScoreView().increaseStonesPut();
        }
        else {
            throw new InvalidPutException("Dieses Feld ist nicht frei.");
        }

        positionToCheckMorris = putPosition;

        return positionToCheckMorris;
    }


    private Position move(){
        Move move = currentPlayer.move(board, getCurrentPlayerIndex(),
                board.countPlayersStones(getCurrentPlayerIndex())==3);
        Position positionToCheckMorris;

        if (board.checkMove(move, board.countPlayersStones(getCurrentPlayerIndex())==3)){
            board.move(move, getCurrentPlayerIndex());

            if (currentPlayer instanceof ComputerPlayer){
                viewManager.getFieldView().graphicMove(move, getCurrentPlayerIndex());
            }
        }
        else {
            throw new InvalidMoveException("Das ist ein ungültiger Zug.");
        }

        positionToCheckMorris = move.getTo();

        return positionToCheckMorris;
    }


    private void kill(){
        System.out.println(currentPlayer.getName() + " darf einen gegnerischen Stein entfernen");
        viewManager.getLogView().setStatusLabel(currentPlayer.getName() +
                " darf einen gegnerischen Stein entfernen. Wähle den Stein, der entfernt werden soll");

        Position killPosition = currentPlayer.kill(board, getCurrentPlayerIndex(), getOtherPlayerIndex());

        if(board.checkKill(killPosition, getOtherPlayerIndex())){
            board.clearStone(killPosition);
            viewManager.getScoreView().increaseStonesKilled();
            viewManager.getScoreView().increaseStonesLost();

            if (currentPlayer instanceof ComputerPlayer){
                viewManager.getFieldView().graphicKill(killPosition);
            }
        }
        else {
            throw new InvalidKillException("Auf diesem Feld befindet sich kein gegnerischer Stein");
        }
    }





    private void checkWinner(){
        if (movePhase && board.countPlayersStones(getOtherPlayerIndex()) < 3){
            winner = getCurrentPlayer();
            viewManager.getLogView().setStatusLabel(winner.getName() + " hat das Spiel gewonnen");
            viewManager.getFieldView().setDisable(true);
            System.out.println(winner.getName() + " hat das Spiel gewonnen!");}
    }*/
}