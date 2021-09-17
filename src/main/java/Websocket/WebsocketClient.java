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
    final private Connection connection;
    final private Game game;

    public WebsocketClient(ViewManager viewManager, URI serverUri, Connection connection, Game game) {
        super(serverUri);
        this.connection = connection;
        this.game = game;
        this.viewManager = viewManager;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("Verbunden mit Server");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("gameCode", game.getGameCode());
        jsonObject.put("command", "start");
        send(jsonObject.toString());
    }

    @Override
    public void onMessage(String message) {
        System.out.println(message);
        JSONObject jsonObject = new JSONObject(message);
        String command = jsonObject.getString("command");
        Board board = game.getBoard();
        String uuid = game.getPlayer0().getUuid();

        switch (command){
            case "join":
                System.out.println("Spiel beigetreten");
                break;

            case "chat":
                System.out.println(message);
                break;

            case "update":

                if (jsonObject.getString("action").equals("put") && !jsonObject.getString("playerUuid").equals(uuid)){

                    int ring = jsonObject.getInt("ring");
                    int field = jsonObject.getInt("field");
                    int playerIndex = jsonObject.getInt("playerIndex");

                    Position position = new Position(ring, field);
                    System.out.println(position);

                    if (board.checkPut(position)){
                        board.putStone(position, playerIndex);
                        connection.put(position, playerIndex+1);
                        System.out.println(board);

                        GameView gameView = ((GameView) viewManager.getCurrentView());
                        BoardImage boardImage =  gameView.getBoardImage();
                        STONECOLOR stonecolor = evaluateStonecolor(gameView, playerIndex);
                        boardImage.put(position, stonecolor);

                        gameView.getContentPane().validate();
                        gameView.getContentPane().repaint();
                    }
                    else {
                        System.out.println("Es wurde ein ungültiger Put ausgeführt");
                    }
                }

                if (jsonObject.getString("action").equals("move") && !jsonObject.getString("playerUuid").equals(uuid)){

                    int moveFromRing = jsonObject.getInt("moveFromRing");
                    int moveFromField = jsonObject.getInt("moveFromField");
                    int moveToRing = jsonObject.getInt("moveToRing");
                    int moveToField = jsonObject.getInt("moveToField");
                    int playerIndex = jsonObject.getInt("playerIndex");

                    Move move = new Move(new Position(moveFromRing, moveFromField), new Position(moveToRing, moveToField));
                    boolean jump = board.countPlayersStones(0) == 3;


                    if (board.checkMove(move, jump)){
                        board.move(move, playerIndex);
                        connection.move(move, false);
                        System.out.println(board);

                        GameView gameView = ((GameView) viewManager.getCurrentView());
                        BoardImage boardImage =  gameView.getBoardImage();
                        boardImage.move(move);

                        gameView.getContentPane().validate();
                        gameView.getContentPane().repaint();
                    }
                    else {
                        System.out.println("Es wurde ein ungültiger Move ausgeführt");
                    }

                }

                if (jsonObject.getString("action").equals("kill") && !jsonObject.getString("playerUuid").equals(uuid)){

                    int ring = jsonObject.getInt("ring");
                    int field = jsonObject.getInt("field");

                    int playerIndex = board.getNumberOnPosition(ring, field);
                    Position position = new Position(ring, field);

                    if (board.checkKill(position, playerIndex)){
                        board.clearStone(position);
                        connection.kill(new Position(ring,field), playerIndex+1);
                        System.out.println(board);

                        GameView gameView = ((GameView) viewManager.getCurrentView());
                        BoardImage boardImage =  gameView.getBoardImage();
                        boardImage.kill(position);

                        gameView.getContentPane().validate();
                        gameView.getContentPane().repaint();
                    }
                    else {
                        System.out.println("Es wurde ein ungültiger Kill ausgeführt");
                    }
                }
                break;

            case "exception":
                System.out.println(jsonObject.get("details"));
        }
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

    private STONECOLOR evaluateStonecolor(GameView gameView, int index){
        if (index == 0){
            return gameView.getPlayer0StoneColor();
        }
        else {
            return gameView.getPlayer1StoneColor();
        }
    }
}
