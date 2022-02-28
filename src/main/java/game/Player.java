package game;


import View.GameView;
import Websocket.MessageHandler;

public abstract class Player implements MessageHandler {

    private final String name;
    private final String uuid;
    private final STONECOLOR stonecolor;
    protected final GameView gameView;

    public Player(GameView gameView, String name, String uuid, STONECOLOR stonecolor) {
        this.name = name;
        this.uuid = uuid;
        this.stonecolor = stonecolor;
        this.gameView = gameView;
    }

    public String getName() {
        return name;
    }

    public STONECOLOR getStonecolor() {
        return stonecolor;
    }

    public String getUuid() {
        return uuid;
    }

}
