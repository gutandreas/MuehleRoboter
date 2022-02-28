package Communication;

import View.GameView;
import View.StartMenuView;
import View.ViewManager;
import game.*;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
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

        if (game.isWatchGame()){
            System.out.println("watch");
            Messenger.sendWatchMessage(viewManager);

        }
        else {
            if (game.isJoiningToExistingGame()) {
                System.out.println("join");
                Messenger.sendJoinMessage(viewManager);
            } else {
                System.out.println("start");
                Messenger.sendStartMessage(viewManager);
            }
        }
    }

    @Override
    public void onMessage(String message) {
        Messenger.receiveMessage(viewManager, message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Serververbindung abgebrochen");
        ((GameView) viewManager.getCurrentView()).getConnection().resetVariables();
        StartMenuView startMenuView = new StartMenuView(viewManager,new String[0], ((GameView) viewManager.getCurrentView()).getConnection());
        startMenuView.setVisible(true);
        viewManager.getCurrentView().setVisible(false);
        viewManager.setCurrentView(startMenuView);
    }

    @Override
    public void onError(Exception ex) {
    }

}
