package View;

import Websocket.Messenger;
import game.Game;

public class ViewManager {

    View currentView;
    Game game;

    public ViewManager() {
    }

    public View getCurrentView() {
        return currentView;
    }

    public void setCurrentView(View currentView) {
        this.currentView = currentView;
    }


}
