package Communication;

import View.GameView;
import View.StartMenuView;
import View.ViewManager;
import game.*;
import org.json.JSONObject;

public class Messenger {

    private static void sendMessage(ViewManager viewManager, String message) {
        if (((GameView) viewManager.getCurrentView()).getGame().getWebsocketClient() == null) {
            receiveMessage(viewManager, message);
        } else {
            ((GameView) viewManager.getCurrentView()).getGame().getWebsocketClient().send(message);
        }
    }

    public static void sendStartMessage(ViewManager viewManager) {
        JSONObject jsonObject = new JSONObject();
        Game game = ((GameView) viewManager.getCurrentView()).getGame();
        jsonObject.put("gameCode", game.getGameCode());
        jsonObject.put("command", "start");
        sendMessage(viewManager, jsonObject.toString());
        sendRoboterConnectionMessage(viewManager, true, false, true);
    }

    public static void sendJoinMessage(ViewManager viewManager) {
        JSONObject jsonObject = new JSONObject();
        Game game = ((GameView) viewManager.getCurrentView()).getGame();
        jsonObject.put("gameCode", game.getGameCode());
        jsonObject.put("command", "join");
        jsonObject.put("player2Name", game.getPlayer1().getName());
        sendMessage(viewManager, jsonObject.toString());
        sendRoboterConnectionMessage(viewManager, true, false, true);
    }

    public static void sendWatchMessage(ViewManager viewManager) {
        JSONObject jsonObject = new JSONObject();
        Game game = ((GameView) viewManager.getCurrentView()).getGame();
        jsonObject.put("gameCode", game.getGameCode());
        jsonObject.put("command", "watch");
        sendMessage(viewManager, jsonObject.toString());
        sendRoboterConnectionMessage(viewManager, true, true, false);
    }

    public static void sendPutMessage(ViewManager viewManager, Position position, boolean triggerAxidraw) {
        JSONObject jsonObject = new JSONObject();
        Game game = ((GameView) viewManager.getCurrentView()).getGame();
        jsonObject.put("gameCode", game.getGameCode());
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
        jsonObject.put("gameCode", game.getGameCode());
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
        jsonObject.put("gameCode", game.getGameCode());
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

    public static void sendGiveUpMessage(ViewManager viewManager) {

        JSONObject jsonObject = new JSONObject();
        Game game = ((GameView) viewManager.getCurrentView()).getGame();
        jsonObject.put("gameCode", game.getGameCode());
        jsonObject.put("command", "giveup");
        jsonObject.put("name", game.getOwnPlayer().getName());
        jsonObject.put("playerUuid", game.getOwnPlayer().getUuid());
        sendMessage(viewManager, jsonObject.toString());
    }

    public static void sendGameOverMessage(ViewManager viewManager, String details) {

        JSONObject jsonObject = new JSONObject();
        Game game = ((GameView) viewManager.getCurrentView()).getGame();
        jsonObject.put("gameCode", game.getGameCode());
        jsonObject.put("playerUuid", game.getCurrentPlayer().getUuid());
        jsonObject.put("playerIndex", game.getCurrentPlayerIndex());
        jsonObject.put("command", "gameOver");
        jsonObject.put("details", details);
        sendMessage(viewManager, jsonObject.toString());
    }

    public static void sendRoboterConnectionMessage(ViewManager viewManager, boolean connected, boolean watching, boolean playing) {

        JSONObject jsonObject = new JSONObject();
        Game game = ((GameView) viewManager.getCurrentView()).getGame();
        jsonObject.put("gameCode", game.getGameCode());
        jsonObject.put("playerUuid", game.getCurrentPlayer().getUuid());
        jsonObject.put("playerIndex", game.getCurrentPlayerIndex());
        jsonObject.put("command", "roboterConnection");
        jsonObject.put("connected", connected);
        jsonObject.put("watching", watching);
        jsonObject.put("playing", playing);
        sendMessage(viewManager, jsonObject.toString());
    }


    public static void receiveMessage(ViewManager viewManager, String message) {

        System.out.println(message);
        JSONObject jsonObject = new JSONObject(message);
        String command = jsonObject.getString("command");
        GameView gameView = ((GameView) viewManager.getCurrentView());
        Game game = gameView.getGame();
        Board board = game.getBoard();
        String ownUuid = game.getPlayerByIndex(game.getOwnIndex()).getUuid();

        switch (command) {
            case "join":
                if (!jsonObject.getString("playerUuid").equals(ownUuid)) {
                    System.out.println("Spiel beigetreten");
                    gameView.setEnemyLabel(jsonObject.getString("player2Name"));
                    game.getPlayer1().setName(jsonObject.getString("player2Name"));
                    gameView.enableScanButton(true);
                }
                break;

            case "chat":
                gameView.addChatMessageToTextarea(jsonObject.getString("name"), jsonObject.getString("message"));
                break;

            case "giveup":
                if (!jsonObject.getString("playerUuid").equals(ownUuid)) {
                    System.out.println("Gegner hat Spiel verlassen");
                    ((GameView) viewManager.getCurrentView()).getConnection().resetVariables();
                    StartMenuView startMenuView = new StartMenuView(viewManager, new String[0], ((GameView) viewManager.getCurrentView()).getConnection());
                    startMenuView.setVisible(true);
                    viewManager.getCurrentView().setVisible(false);
                    viewManager.setCurrentView(startMenuView);
                }
                break;

            case "gameOver":
                int index = jsonObject.getInt("playerIndex");
                String name = game.getPlayerByIndex(1 - index).getName();
                gameView.setInformationLabel(name + " hat das Spiel gewonnen!");
                gameView.enableScanButton(false);
                break;

            case "timeout":
                System.out.println("Timeout");
                ((GameView) viewManager.getCurrentView()).getConnection().resetVariables();
                StartMenuView startMenuView = new StartMenuView(viewManager, new String[0], ((GameView) viewManager.getCurrentView()).getConnection());
                startMenuView.setVisible(true);
                viewManager.getCurrentView().setVisible(false);
                viewManager.setCurrentView(startMenuView);
                break;

            case "update":

                if (jsonObject.getString("action").equals("put")) {

                    int ring = jsonObject.getInt("ring");
                    int field = jsonObject.getInt("field");
                    int playerIndex = jsonObject.getInt("playerIndex");
                    boolean triggerAxidraw = jsonObject.getBoolean("triggerAxidraw");

                    Position position = new Position(ring, field);
                    System.out.println(position);

                    if (board.isPutPossibleAt(position)) {
                        board.putStone(position, playerIndex);
                        System.out.println(board);

                        if (triggerAxidraw) {
                            gameView.getConnection().put(position, playerIndex + 1);
                        }

                        STONECOLOR stonecolor = evaluateStonecolor(gameView, playerIndex);
                        gameView.getBoardImage().put(position, stonecolor);
                        gameView.clearInformationLabel();
                        System.out.println("update GUI");

                        //f??hrt zu M??hle
                        if (board.isPositionPartOfMorris(position) && board.canPlayerKill(playerIndex)) {
                            game.updateGameState(true);
                            System.out.println("M??hle");

                            if (triggerAxidraw) {
                                waitToAvoidAxidrawEventQueueOverflow();
                            }

                            game.getCurrentPlayer().prepareKill(viewManager);
                        }
                        //f??hrt nicht zu M??hle
                        else {
                            game.updateGameState(false);
                            game.getCurrentPlayer().preparePutOrMove(viewManager);
                        }
                    } else {
                        System.out.println("Es wurde ein ung??ltiger Put ausgef??hrt");
                    }
                }

                if (jsonObject.getString("action").equals("move")) {

                    int moveFromRing = jsonObject.getInt("moveFromRing");
                    int moveFromField = jsonObject.getInt("moveFromField");
                    int moveToRing = jsonObject.getInt("moveToRing");
                    int moveToField = jsonObject.getInt("moveToField");
                    int playerIndex = jsonObject.getInt("playerIndex");
                    boolean triggerAxidraw = jsonObject.getBoolean("triggerAxidraw");

                    Move move = new Move(new Position(moveFromRing, moveFromField), new Position(moveToRing, moveToField));
                    boolean jump = board.numberOfStonesOf(game.getCurrentPlayerIndex()) == 3;

                    if (board.isMovePossibleAt(move, jump)) {
                        board.moveStone(move, playerIndex);
                        System.out.println(board);

                        if (triggerAxidraw) {
                            gameView.getConnection().move(move, jump);
                        }

                        gameView.getBoardImage().move(move);
                        gameView.clearInformationLabel();

                        //f??hrt zu M??hle
                        if (board.isPositionPartOfMorris(move.getTo()) && board.canPlayerKill(playerIndex)) {
                            game.updateGameState(true);
                            if (triggerAxidraw) {
                                waitToAvoidAxidrawEventQueueOverflow();
                            }
                            game.getCurrentPlayer().prepareKill(viewManager);
                        }
                        //f??hrt nicht zu M??hle
                        else {
                            game.updateGameState(false);
                            game.getCurrentPlayer().preparePutOrMove(viewManager);
                        }
                    } else {
                        System.out.println("Es wurde ein ung??ltiger Move ausgef??hrt");
                    }

                }

                if (jsonObject.getString("action").equals("kill")) {

                    int ring = jsonObject.getInt("ring");
                    int field = jsonObject.getInt("field");
                    boolean triggerAxidraw = jsonObject.getBoolean("triggerAxidraw");

                    Position position = new Position(ring, field);
                    int playerIndex = board.getNumberOnPosition(position);

                    if (board.isKillPossibleAt(position, playerIndex)) {
                        board.removeStone(position);
                        System.out.println(board);

                        if (triggerAxidraw) {
                            gameView.getConnection().kill(position, playerIndex + 1);
                        }

                        gameView.getBoardImage().kill(position);
                        gameView.clearInformationLabel();

                        game.updateGameState(false);
                        game.getCurrentPlayer().preparePutOrMove(viewManager);

                    } else {
                        System.out.println("Es wurde ein ung??ltiger Kill ausgef??hrt");
                    }
                }
                break;
        }
    }

    private static void waitToAvoidAxidrawEventQueueOverflow() {
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static STONECOLOR evaluateStonecolor(GameView gameView, int index) {
        if (index == 0) {
            return gameView.getPlayer0StoneColor();
        } else {
            return gameView.getPlayer1StoneColor();
        }
    }

}
