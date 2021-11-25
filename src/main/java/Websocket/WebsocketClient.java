package Websocket;

import EiBotBoard.Connection;
import View.BoardImage;
import View.GameView;
import View.ViewManager;
import game.*;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;
import java.net.URI;

public class WebsocketClient extends WebSocketClient {

    ViewManager viewManager;
    final private Game game;

    public WebsocketClient(ViewManager viewManager, URI serverUri, Game game) {
        super(serverUri);
        this.game = game;
        this.viewManager = viewManager;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("Verbunden mit Server");
        JSONObject jsonObject = new JSONObject();

        if (game.isJoiningToExistingGame()){
            System.out.println("join");
            jsonObject.put("gameCode", game.getGameCode());
            jsonObject.put("command", "join");
            jsonObject.put("player2Name", game.getPlayer1().getName());
        }
        else {
            System.out.println("start");
            jsonObject.put("gameCode", game.getGameCode());
            jsonObject.put("command", "start");
        }

        send(jsonObject.toString());
    }

    @Override
    public void onMessage(String message) {

        Messenger.receiveMessage(viewManager, message);

        /*System.out.println(message);
        JSONObject jsonObject = new JSONObject(message);
        String command = jsonObject.getString("command");
        Board board = game.getBoard();
        String ownUuid = game.getPlayerByIndex(game.getOwnIndex()).getUuid();
        GameView gameView = ((GameView) viewManager.getCurrentView());

        switch (command){
            case "join":
                if (!jsonObject.getString("playerUuid").equals(ownUuid)){
                    System.out.println("Spiel beigetreten");
                    gameView.setEnemyLabel(jsonObject.getString("player2Name"));
                    gameView.enableScanButton(true);}
                break;

            case "chat":
                System.out.println(message);
                break;

            case "update":

                if (jsonObject.getString("action").equals("put") && !jsonObject.getString("playerUuid").equals(ownUuid)){

                    int ring = jsonObject.getInt("ring");
                    int field = jsonObject.getInt("field");
                    int playerIndex = jsonObject.getInt("playerIndex");

                    Position position = new Position(ring, field);
                    System.out.println(position);

                    if (board.checkPut(position)){
                        board.putStone(position, playerIndex);
                        connection.put(position, playerIndex+1);
                        System.out.println(board);


                        BoardImage boardImage =  gameView.getBoardImage();
                        STONECOLOR stonecolor = evaluateStonecolor(gameView, playerIndex);
                        boardImage.put(position, stonecolor);

                        gameView.getContentPane().validate();
                        gameView.getContentPane().repaint();

                        game.increaseRound();
                        gameView.increaseRoundLabel();

                        if (board.checkMorris(position) && board.isThereStoneToKill(0)){ //Achtung: Playerindex hardcoded
                            gameView.enableScanButton(false);
                            gameView.setNextStepLabelKill(false);
                        }
                        else {
                            gameView.enableScanButton(true);
                            if (game.getRound() <= 18){
                                gameView.setNextStepLabelPut(true);
                            }
                            else {
                                gameView.setNextStepLabelMove(true);
                            }
                        }

                    }
                    else {
                        System.out.println("Es wurde ein ungültiger Put ausgeführt");
                    }
                }

                if (jsonObject.getString("action").equals("move") && !jsonObject.getString("playerUuid").equals(ownUuid)){

                    int moveFromRing = jsonObject.getInt("moveFromRing");
                    int moveFromField = jsonObject.getInt("moveFromField");
                    int moveToRing = jsonObject.getInt("moveToRing");
                    int moveToField = jsonObject.getInt("moveToField");
                    int playerIndex = jsonObject.getInt("playerIndex");

                    Move move = new Move(new Position(moveFromRing, moveFromField), new Position(moveToRing, moveToField));
                    boolean jump = board.countPlayersStones(0) == 3; //Achtung: Playerindex hardcoded


                    if (board.checkMove(move, jump)){
                        board.move(move, playerIndex);
                        connection.move(move, jump);
                        System.out.println(board);

                        BoardImage boardImage = gameView.getBoardImage();
                        boardImage.move(move);

                        gameView.getContentPane().validate();
                        gameView.getContentPane().repaint();

                        if (board.checkMorris(move.getTo()) && board.isThereStoneToKill(0)){ //Achtung: Playerindex hardcoded
                            gameView.enableScanButton(false);
                            gameView.setNextStepLabelKill(false);
                        }
                        else {
                            gameView.enableScanButton(true);
                            gameView.setNextStepLabelMove(true);
                        }
                    }
                    else {
                        System.out.println("Es wurde ein ungültiger Move ausgeführt");
                    }

                }

                if (jsonObject.getString("action").equals("kill") && !jsonObject.getString("playerUuid").equals(ownUuid)){

                    int ring = jsonObject.getInt("ring");
                    int field = jsonObject.getInt("field");

                    int playerIndex = board.getNumberOnPosition(ring, field);
                    Position position = new Position(ring, field);

                    if (board.checkKill(position, playerIndex)){
                        board.clearStone(position);
                        connection.kill(new Position(ring,field), playerIndex+1);
                        System.out.println(board);

                        BoardImage boardImage =  gameView.getBoardImage();
                        boardImage.kill(position);

                        gameView.getContentPane().validate();
                        gameView.getContentPane().repaint();
                        gameView.enableScanButton(true);

                        if (board.countPlayersStones(1-playerIndex) < 3 && game.getRound() > 18){
                            gameView.setInformationLabel("Sie haben das Spiel verloren!");
                            System.out.println("Spiel verloren");
                            gameView.enableScanButton(false);
                        }
                        else {
                            gameView.enableScanButton(true);
                            if (game.getRound() > 18)
                            {
                                gameView.setNextStepLabelPut(true);
                            }
                            else {
                                gameView.setNextStepLabelMove(true);
                            }
                        }


                    }
                    else {
                        System.out.println("Es wurde ein ungültiger Kill ausgeführt");
                    }
                }
                break;

            case "exception":
                System.out.println(jsonObject.get("details"));
        }*/
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {

    }

    @Override
    public void onError(Exception ex) {
        // Websocket wieder neu aufbauen
    }

    public void watchGame(String gameCode){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("command", "watch");
        jsonObject.put("gameCode", gameCode);
        send(jsonObject.toString());
    }


}
