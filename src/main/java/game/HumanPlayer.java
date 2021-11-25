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

    public boolean isLocal() {
        return local;
    }

    @Override
    public void prepareKill(ViewManager viewManager) {

        if (local){
            ((GameView) viewManager.getCurrentView()).enableScanButton(true);
        }
        else {
            ((GameView) viewManager.getCurrentView()).enableScanButton(false);
        }
    }

    @Override
    public void preparePutOrMove(ViewManager viewManager) {
        if (local){
            ((GameView) viewManager.getCurrentView()).enableScanButton(true);
        }
        else {
            ((GameView) viewManager.getCurrentView()).enableScanButton(false);
        }
    }
}
