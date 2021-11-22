package game;


import View.GameView;
import View.ViewManager;
import Websocket.MessageHandler;

public class HumanPlayer extends Player implements MessageHandler {

    boolean local;

    public HumanPlayer(GameView gameView, String name, String uuid, STONECOLOR stonecolor, boolean local) {
        super(gameView, name, uuid, stonecolor);
        this.local = local;
    }


    @Override
    Position put(Board board, int playerIndex) {


        return null;
    }

    @Override
    Move move(Board board, int playerIndex, boolean allowedToJump) {

        return null;
    }

    @Override
    Position kill(Board board, int ownPlayerIndex, int otherPlayerIndex) {

        return null;
    }


    @Override
    public void prepareKill(ViewManager viewManager) {
        ((GameView) viewManager.getCurrentView()).enableScanButton(true);
    }

    @Override
    public void preparePutOrMove(ViewManager viewManager) {
        ((GameView) viewManager.getCurrentView()).enableScanButton(true);
    }
}
