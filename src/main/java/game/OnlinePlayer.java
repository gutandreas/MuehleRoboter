package game;

import View.GameView;


public class OnlinePlayer extends Player{


    public OnlinePlayer(GameView gameView, String name) {
        super(gameView, name, null, STONECOLOR.WHITE);
    }

    @Override
    Move move(Board board, int playerIndex, boolean allowedToJump) {
        return null;
    }

    @Override
    Position put(Board board, int playerIndex) {
        Object loopObject = new Object();



        return null;
    }

    @Override
    Position kill(Board board, int ownPlayerIndex, int otherPlayerIndex) {
        return null;
    }
}
