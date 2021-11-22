package Websocket;

import View.ViewManager;

public interface MessageHandler {

     void prepareKill(ViewManager viewManager);
     void preparePutOrMove(ViewManager viewManager);
}
