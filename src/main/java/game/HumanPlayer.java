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
    public void prepareKill(ViewManager viewManager) {
        ((GameView) viewManager.getCurrentView()).enableScanButton(true);
    }

    @Override
    public void preparePutOrMove(ViewManager viewManager) {
        ((GameView) viewManager.getCurrentView()).enableScanButton(true);
    }
}
