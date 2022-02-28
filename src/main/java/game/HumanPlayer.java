package game;

import View.GameView;
import View.ViewManager;
import Communication.MessageHandler;


public class HumanPlayer extends Player implements MessageHandler {

    private boolean local;

    public HumanPlayer(GameView gameView, String name, String uuid, STONECOLOR stonecolor, boolean local) {
        super(gameView, name, uuid, stonecolor);
        this.local = local;
    }

    public boolean isLocal() {
        return local;
    }

    @Override
    public void prepareKill(ViewManager viewManager) {

        GameView gameView = ((GameView) viewManager.getCurrentView());

        if (local){
            gameView.enableScanButton(true);
        }
        else {
            gameView.enableScanButton(false);
        }
    }

    @Override
    public void preparePutOrMove(ViewManager viewManager) {

        GameView gameView = ((GameView) viewManager.getCurrentView());

        if (local){
            gameView.enableScanButton(true);
        }
        else {
            gameView.enableScanButton(false);
        }
    }

}
