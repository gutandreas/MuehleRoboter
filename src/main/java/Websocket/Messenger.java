package Websocket;

import View.BoardImage;
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

    public static void sendPutMessage(ViewManager viewManager, Position position, boolean triggerAxidraw){
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
        jsonObject.put("triggerAxidraw", triggerAxidraw);
        jsonObject.put("playerIndex", game.getCurrentPlayerIndex());
        sendMessage(viewManager, jsonObject.toString());
    }

    public static void sendMoveMessage(ViewManager viewManager, Move move, boolean triggerAxidraw) {

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
        jsonObject.put("triggerAxidraw", triggerAxidraw);
        jsonObject.put("playerIndex", game.getCurrentPlayerIndex());
        sendMessage(viewManager, jsonObject.toString());
    }

    public static void sendKillMessage(ViewManager viewManager, Position position, boolean triggerAxidraw) {

        JSONObject jsonObject = new JSONObject();
        Game game = ((GameView) viewManager.getCurrentView()).getGame();

        jsonObject.put("gameCode", ((GameView) viewManager.getCurrentView()).getGame().getGameCode());
        jsonObject.put("command", "update");
        jsonObject.put("action", "kill");
        jsonObject.put("playerUuid", game.getCurrentPlayer().getUuid());
        jsonObject.put("ring", position.getRing());
        jsonObject.put("field", position.getField());
        jsonObject.put("callComputer", false);
        jsonObject.put("triggerAxidraw", triggerAxidraw);
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
                    boolean triggerAxidraw = jsonObject.getBoolean("triggerAxidraw");

                    Position position = new Position(ring, field);
                    System.out.println(position);

                    if (board.checkPut(position)){
                        board.putStone(position, playerIndex);
                        System.out.println(board);

                        if (triggerAxidraw){
                            gameView.getConnection().put(position, playerIndex+1);
                        }

                        STONECOLOR stonecolor = evaluateStonecolor(gameView, playerIndex);
                        gameView.getBoardImage().put(position, stonecolor);
                        gameView.clearInformationLabel();

                        //führt zu Mühle
                        if (board.checkMorris(position) && board.isThereStoneToKill(1-playerIndex)){
                            game.setKillPhase(true);
                            game.updateGameState(false);

                            if (triggerAxidraw){
                                waitToAvoidAxidrawEventQueueOverflow();
                            }

                            game.getCurrentPlayer().prepareKill(viewManager);
                        }
                        //führt nicht zu Mühle
                        else {
                            game.setKillPhase(false);
                            game.updateGameState(true);
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
                    boolean triggerAxidraw = jsonObject.getBoolean("triggerAxidraw");

                    Move move = new Move(new Position(moveFromRing, moveFromField), new Position(moveToRing, moveToField));
                    boolean jump = board.countPlayersStones(game.getCurrentPlayerIndex()) == 3;


                    if (board.checkMove(move, jump)){
                        board.move(move, playerIndex);
                        System.out.println(board);

                        if (triggerAxidraw){
                            gameView.getConnection().move(move, jump);
                        }

                        gameView.getBoardImage().move(move);
                        gameView.clearInformationLabel();

                        //führt zu Mühle
                        if (board.checkMorris(move.getTo()) && board.isThereStoneToKill(1-playerIndex)){
                            game.setKillPhase(true);
                            game.updateGameState(false);
                            if (triggerAxidraw){
                                waitToAvoidAxidrawEventQueueOverflow();
                            }
                            game.getCurrentPlayer().prepareKill(viewManager);
                        }
                        //führt nicht zu Mühle
                        else {
                            game.updateGameState( true);
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
                    boolean triggerAxidraw = jsonObject.getBoolean("triggerAxidraw");

                    int playerIndex = board.getNumberOnPosition(ring, field);
                    Position position = new Position(ring, field);

                    if (board.checkKill(position, playerIndex)){
                        board.clearStone(position);
                        System.out.println(board);

                        if (triggerAxidraw){
                            gameView.getConnection().kill(position, playerIndex+1);
                        }

                        gameView.getBoardImage().kill(position);
                        gameView.clearInformationLabel();

                        game.setKillPhase(false);
                        game.updateGameState( true);
                        game.getCurrentPlayer().preparePutOrMove(viewManager);

                    }
                    else {
                        System.out.println("Es wurde ein ungültiger Kill ausgeführt");
                    }
                }
                break;
        }
    }

    private static void waitToAvoidAxidrawEventQueueOverflow(){
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static STONECOLOR evaluateStonecolor(GameView gameView, int index){
        if (index == 0){
            return gameView.getPlayer0StoneColor();
        }
        else {
            return gameView.getPlayer1StoneColor();
        }
    }
}