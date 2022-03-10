package game;

import Communication.MessageHandler;
import View.GameView;
import View.ViewManager;


public class HumanPlayer extends Player implements MessageHandler {

    private final boolean local;

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

        gameView.enableScanButton(local);
    }

    @Override
    public void preparePutOrMove(ViewManager viewManager) {

        GameView gameView = ((GameView) viewManager.getCurrentView());

        gameView.enableScanButton(local);
    }

}
