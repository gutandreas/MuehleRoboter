package game;


import View.GameView;

public abstract class Player {

    private final String name;
    private final String uuid;
    private final STONECOLOR stonecolor;
    private final GameView gameView;

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


    abstract Move move(Board board, int playerIndex, boolean allowedToJump);
    abstract Position put(Board board, int playerIndex);
    abstract Position kill(Board board, int ownPlayerIndex, int otherPlayerIndex);


}
