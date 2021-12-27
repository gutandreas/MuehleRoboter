package game;

import View.GameView;
import Websocket.Messenger;
import Websocket.WebsocketClient;
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
    private boolean joiningToExistingGame;
    private boolean gameOver = false;
    private String gameCode;
    private GameView gameView;
    private int ownIndex;
    private WebsocketClient websocketClient;


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

    public Game(GameView gameView, Player player0, Player player1, String gameCode, WebsocketClient websocketClient, boolean joiningToExistingGame) {
        this.gameView = gameView;
        this.player0 = player0;
        this.player1 = player1;
        this.websocketClient = websocketClient;
        playerArrayList.add(0, player0);
        playerArrayList.add(1, player1);
        round = 0;
        currentPlayer=playerArrayList.get(0);
        this.gameCode = gameCode;
        this.joiningToExistingGame = joiningToExistingGame;
        board = new Board();
        if (joiningToExistingGame){
            ownIndex = 1;
        }
        else {
            ownIndex = 0;
        }
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

    public Player getOwnPlayer() {
        return playerArrayList.get(getOwnIndex());
    }

    public Player getOtherPlayer() {
        return playerArrayList.get(getOtherPlayerIndex());
    }

    public boolean isJoiningToExistingGame() {
        return joiningToExistingGame;
    }

    public WebsocketClient getWebsocketClient() {
        return websocketClient;
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

    public Player getPlayerByIndex(int index){
        if (index == 0){
            return player0;
        }
        if (index == 1){
            return player1;
        }
        return null;
    }

    public void setWebsocketClient(WebsocketClient websocketClient) {
        this.websocketClient = websocketClient;
    }

    public void updateGameState(boolean killPhase){

        if (killPhase){
            gameView.setNextStepLabelKill(currentPlayer.getName());
            gameView.getGame().setKillPhase(true);
        }

        else {
            gameView.getGame().setKillPhase(false);
            increaseRound();
            updateCurrentPlayer();
            setGamesPhaseBooleans();
                if (putPhase) {
                    gameView.setNextStepLabelPut(currentPlayer.getName());
                }
                if (movePhase) {
                    gameView.setNextStepLabelMove(currentPlayer.getName());
                }
            }

        System.out.println(round);

        checkWinner();

    }

    private void checkWinner(){

        boolean lessThan3Stones = movePhase && board.countPlayersStones(getCurrentPlayerIndex()) < 3;
        boolean unableToMove = movePhase && !board.checkIfAbleToMove(getCurrentPlayerIndex());

        if (lessThan3Stones || unableToMove){
            gameOver = true;
        }

        if (websocketClient == null) {


            if (lessThan3Stones) {
                Messenger.sendGameOverMessage(gameView.getViewManager(), "Weniger als 3 Steine");
            }


            if (unableToMove) {
                Messenger.sendGameOverMessage(gameView.getViewManager(), "Keine möglichen Züge");
            }
        }

    }

    public int getOwnIndex() {
        return ownIndex;
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
        gameView.increaseRoundLabel();
    }

    private void setGamesPhaseBooleans(){
        if (round >= NUMBEROFSTONES*2){
            putPhase = false;
            movePhase = true;}
    }

    public void changeToMovePhase(){
        movePhase = true;
    }


}