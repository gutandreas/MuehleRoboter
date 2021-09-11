package game;


import View.GameView;

public class HumanPlayer extends Player {

    public HumanPlayer(GameView gameView, String name, String uuid, STONECOLOR stonecolor) {
        super(gameView, name, uuid, stonecolor);
    }


    @Override
    Position put(Board board, int playerIndex) {


        return null;
    }

    @Override
    Move move(Board board, int playerIndex, boolean allowedToJump) {

        return null;
    }

    @Override
    Position kill(Board board, int ownPlayerIndex, int otherPlayerIndex) {

        return null;
    }


}
