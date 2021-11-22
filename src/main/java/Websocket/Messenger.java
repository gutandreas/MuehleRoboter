package Websocket;

import View.GameView;
import View.ViewManager;
import game.*;
import org.json.JSONObject;

public class Messenger {

    private static void sendMessage(ViewManager viewManager, String message){
        if (((GameView) viewManager.getCurrentView()).getGame().getWebsocketClient() == null){
            receiveMessage(viewManager, message);
        }
        else {
            ((GameView) viewManager.getCurrentView()).getGame().getWebsocketClient().send(message);
        }
    }

    public static void sendPutMessage(ViewManager viewManager, Position position){
        JSONObject jsonObject = new JSONObject();
        Game game = ((GameView) viewManager.getCurrentView()).getGame();
        System.out.println(game.getCurrentPlayer().getUuid());


        jsonObject.put("gameCode", ((GameView) viewManager.getCurrentView()).getGame().getGameCode());
        jsonObject.put("command", "update");
        jsonObject.put("action", "put");
        jsonObject.put("playerUuid", (game.getCurrentPlayer().getUuid()));
        jsonObject.put("ring", position.getRing());
        jsonObject.put("field", position.getField());
        jsonObject.put("callComputer", false);
        jsonObject.put("playerIndex", game.getCurrentPlayerIndex());
        sendMessage(viewManager, jsonObject.toString());
    }

    public static void sendMoveMessage(ViewManager viewManager, Move move) {

        JSONObject jsonObject = new JSONObject();
        Game game = ((GameView) viewManager.getCurrentView()).getGame();

        jsonObject.put("gameCode", ((GameView) viewManager.getCurrentView()).getGame().getGameCode());
        jsonObject.put("command", "update");
        jsonObject.put("action", "move");
        jsonObject.put("playerUuid", game.getCurrentPlayer().getUuid());
        jsonObject.put("moveFromRing", move.getFrom().getRing());
        jsonObject.put("moveFromField", move.getFrom().getField());
        jsonObject.put("moveToRing", move.getTo().getRing());
        jsonObject.put("moveToField", move.getTo().getField());
        jsonObject.put("callComputer", false);
        jsonObject.put("playerIndex", game.getCurrentPlayerIndex());
        sendMessage(viewManager, jsonObject.toString());
    }

    public static void sendKillMessage(ViewManager viewManager, Position position) {

        JSONObject jsonObject = new JSONObject();
        Game game = ((GameView) viewManager.getCurrentView()).getGame();

        jsonObject.put("gameCode", ((GameView) viewManager.getCurrentView()).getGame().getGameCode());
        jsonObject.put("command", "update");
        jsonObject.put("action", "kill");
        jsonObject.put("playerUuid", game.getCurrentPlayer().getUuid());
        jsonObject.put("ring", position.getRing());
        jsonObject.put("field", position.getField());
        jsonObject.put("callComputer", false);
        jsonObject.put("playerIndex", game.getCurrentPlayerIndex());
        sendMessage(viewManager, jsonObject.toString());
    }


    public static void receiveMessage(ViewManager viewManager, String message){


        System.out.println(message);
        JSONObject jsonObject = new JSONObject(message);
        String command = jsonObject.getString("command");
        GameView gameView = ((GameView) viewManager.getCurrentView());
        Game game = gameView.getGame();
        Board board = game.getBoard();
        String ownUuid = game.getPlayerByIndex(game.getOwnIndex()).getUuid();

        switch (command){
            case "join":
                if (!jsonObject.getString("playerUuid").equals(ownUuid)){
                    System.out.println("Spiel beigetreten");
                    gameView.setEnemyLabel(jsonObject.getString("player2Name"));
                    gameView.enableScanButton(true);}
                break;

            case "update":

                if (jsonObject.getString("action").equals("put")){

                    int ring = jsonObject.getInt("ring");
                    int field = jsonObject.getInt("field");
                    int playerIndex = jsonObject.getInt("playerIndex");

                    Position position = new Position(ring, field);
                    System.out.println(position);

                    if (board.checkPut(position)){
                        board.putStone(position, playerIndex);
                        System.out.println(board);
                        //führt zu Mühle
                        if (board.checkMorris(position) && board.isThereStoneToKill(1-playerIndex)){
                            game.updateGameState(true, false, false);
                            game.getCurrentPlayer().prepareKill(viewManager);
                        }
                        //führt nicht zu Mühle
                        else {
                            game.updateGameState(true, false, true);
                            game.getCurrentPlayer().preparePutOrMove(viewManager);
                        }
                    }
                    else {
                        System.out.println("Es wurde ein ungültiger Put ausgeführt");
                    }
                }

                if (jsonObject.getString("action").equals("move")){

                    int moveFromRing = jsonObject.getInt("moveFromRing");
                    int moveFromField = jsonObject.getInt("moveFromField");
                    int moveToRing = jsonObject.getInt("moveToRing");
                    int moveToField = jsonObject.getInt("moveToField");
                    int playerIndex = jsonObject.getInt("playerIndex");

                    Move move = new Move(new Position(moveFromRing, moveFromField), new Position(moveToRing, moveToField));
                    boolean jump = board.countPlayersStones(game.getCurrentPlayerIndex()) == 3;


                    if (board.checkMove(move, jump)){
                        board.move(move, playerIndex);
                        System.out.println(board);
                        //führt zu Mühle
                        if (board.checkMorris(move.getTo()) && board.isThereStoneToKill(1-playerIndex)){
                            game.updateGameState(false, false, false);
                            game.getCurrentPlayer().prepareKill(viewManager);
                        }
                        //führt nicht zu Mühle
                        else {
                            game.updateGameState(false, false, true);
                            game.getCurrentPlayer().preparePutOrMove(viewManager);
                            }
                    }
                    else {
                        System.out.println("Es wurde ein ungültiger Move ausgeführt");
                    }

                }

                if (jsonObject.getString("action").equals("kill")){

                    int ring = jsonObject.getInt("ring");
                    int field = jsonObject.getInt("field");

                    int playerIndex = board.getNumberOnPosition(ring, field);
                    Position position = new Position(ring, field);

                    if (board.checkKill(position, playerIndex)){
                        board.clearStone(position);
                        System.out.println(board);

                        game.updateGameState(false, true, true);
                        game.setKillPhase(false);
                        game.getCurrentPlayer().preparePutOrMove(viewManager);

                    }
                    else {
                        System.out.println("Es wurde ein ungültiger Kill ausgeführt");
                    }
                }
                break;
        }
    }
}
