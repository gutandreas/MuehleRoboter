package game;


import Communication.MessageHandler;
import View.GameView;

public abstract class Player implements MessageHandler {

    private String name;
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

    public void setName(String name) {
        this.name = name;
    }

    public STONECOLOR getStonecolor() {
        return stonecolor;
    }

    public String getUuid() {
        return uuid;
    }

}
