package game;

import View.GameView;
import View.ViewManager;
import Websocket.MessageHandler;
import Websocket.Messenger;


public class ComputerPlayer extends Player implements MessageHandler {


    private GameTree gameTree;
    private ScorePoints putPoints;
    private ScorePoints movePoints;
    private int levelLimit;


    public ComputerPlayer(GameView gameView, String name, STONECOLOR stonecolor, ScorePoints putPoints, ScorePoints movePoints, int levelLimit) {
        super(gameView, name, " ", stonecolor);
        this.putPoints = putPoints;
        this.movePoints = movePoints;
        this.levelLimit = levelLimit;
    }


    Position put(Board board, int playerIndex) {

        gameTree = new GameTree(board);
        recursivePutBfs(gameTree.getRoot(), putPoints, movePoints, playerIndex, playerIndex, levelLimit);

        return gameTree.getBestPut();
    }


    private void recursivePutBfs(GameTreeNode node, ScorePoints putPoints, ScorePoints movePoints, int scorePlayerIndex, int currentPlayerIndex, int levelLimit){

        if (node.getLevel()==levelLimit){
            return;
        }

        int tempCurrentPlayerIndex;

        if (node.getLevel()%2 == 0){
            tempCurrentPlayerIndex = scorePlayerIndex;
        }
        else {
            tempCurrentPlayerIndex = 1 - scorePlayerIndex;
        }

        for (Position freeField : Advisor.getFreePositions(node.getBoard())){
            pretendPut(node.getBoard(), freeField, putPoints, node, scorePlayerIndex, tempCurrentPlayerIndex, node.getLevel()+1);
        }

        if (node.getLevel()%2 == 0){
            gameTree.keepOnlyBestChildren(node, 15);}
        else {
            gameTree.keepOnlyWorstChildren(node, 15);
        }

        for (GameTreeNode child : node.getChildren()){
            if (gameView.getGame().getRound() + child.getLevel() < 18){
                recursivePutBfs(child, putPoints, movePoints, scorePlayerIndex, tempCurrentPlayerIndex, levelLimit);
            }
            else {
                recursiveMoveBfs(child, movePoints, scorePlayerIndex, tempCurrentPlayerIndex, levelLimit);
            }
        }
    }


    private void pretendPut(Board board, Position put, ScorePoints scorePoints, GameTreeNode parent, int scorePlayerIndex, int currentPlayerIndex, int level){

        GameTreeNode gameTreeNode1 = new GameTreeNode();
        gameTreeNode1.setPut(put);
        gameTreeNode1.setLevel(level);

        Board clonedBoard1 = (Board) board.clone();
        clonedBoard1.putStone(put, currentPlayerIndex);

        gameTreeNode1.setBoard(clonedBoard1);
        gameTreeNode1.setScore(Advisor.getScore(gameTreeNode1, scorePoints, scorePlayerIndex));

        if (gameTreeNode1.getBoard().isPositionPartOfMorris(gameTreeNode1.getPut())){
            for (Position killPosition : Advisor.getAllPossibleKills(clonedBoard1,currentPlayerIndex)){
                GameTreeNode gameTreeNode2 = new GameTreeNode();
                gameTreeNode2.setPut(put);
                gameTreeNode2.setLevel(level);
                gameTreeNode2.setKill(killPosition);

                Board clonedBoard2 = (Board) clonedBoard1.clone();
                clonedBoard2.removeStone(killPosition);

                gameTreeNode2.setBoard(clonedBoard2);
                gameTreeNode2.setScore(Advisor.getScore(gameTreeNode2, scorePoints, scorePlayerIndex));
                gameTree.addNode(parent, gameTreeNode2);
            }
        }
        else {
            gameTree.addNode(parent, gameTreeNode1);
        }

    }


    Move move(Board board, int playerIndex, boolean allowedToJump) {

        gameTree = new GameTree(board);
        recursiveMoveBfs(gameTree.getRoot(), movePoints, playerIndex, playerIndex, levelLimit);

        return gameTree.getBestMove();
    }


    private void recursiveMoveBfs(GameTreeNode set, ScorePoints scorePoints, int scorePlayerIndex, int currentPlayerIndex, int levelLimit){

        if (set.getLevel()==levelLimit){
            return;
        }

        int tempCurrentPlayerIndex;

        if (set.getLevel()%2 == 0){
            tempCurrentPlayerIndex = scorePlayerIndex;
        }
        else {
            tempCurrentPlayerIndex = 1 - scorePlayerIndex;
        }

        for (Move move : Advisor.getAllPossibleMoves(set.getBoard(), tempCurrentPlayerIndex)){
            pretendMove(set.getBoard(), move, scorePoints, set, scorePlayerIndex, tempCurrentPlayerIndex, set.getLevel()+1);
        }

        if (set.getLevel()%2 == 0){
            gameTree.keepOnlyBestChildren(set, 15);}
        else {
            gameTree.keepOnlyWorstChildren(set, 15);}


        for (GameTreeNode child : set.getChildren()){
            recursiveMoveBfs(child, scorePoints, scorePlayerIndex, tempCurrentPlayerIndex, levelLimit);
        }
    }


    private void pretendMove(Board board, Move move, ScorePoints scorePoints, GameTreeNode parent, int scorePlayerIndex, int currentPlayerIndex, int level){

        GameTreeNode gameTreeNode1 = new GameTreeNode();
        gameTreeNode1.setMove(move);
        gameTreeNode1.setLevel(level);

        Board clonedBoard1 = (Board) board.clone();
        clonedBoard1.moveStone(move, currentPlayerIndex);

        gameTreeNode1.setBoard(clonedBoard1);
        gameTreeNode1.setScore(Advisor.getScore(gameTreeNode1, scorePoints, scorePlayerIndex));

        if (gameTreeNode1.getBoard().isPositionPartOfMorris(gameTreeNode1.getMove().getTo())){
            for (Position killPosition : Advisor.getAllPossibleKills(clonedBoard1,currentPlayerIndex)){
                GameTreeNode gameTreeNode2 = new GameTreeNode();
                gameTreeNode2.setMove(move);
                gameTreeNode2.setLevel(level);
                gameTreeNode2.setKill(killPosition);

                Board clonedBoard2 = (Board) clonedBoard1.clone();
                clonedBoard2.removeStone(killPosition);

                gameTreeNode2.setBoard(clonedBoard2);
                gameTreeNode2.setScore(Advisor.getScore(gameTreeNode2, scorePoints, scorePlayerIndex));
                gameTree.addNode(parent, gameTreeNode2);
            }
        }
        else {
            gameTree.addNode(parent, gameTreeNode1);
        }
    }


    Position kill(Board board, int ownPlayerIndex, int otherPlayerIndex) {
        return gameTree.getBestKill();
    }


    public void prepareKill(ViewManager viewManager) {
        triggerKill(viewManager);
    }


    public void preparePutOrMove(ViewManager viewManager) {

        if (gameView.getGame().isPutPhase()){
                triggerPut(viewManager);
            }
        else {
            triggerMove(viewManager);
        }
    }


    public void triggerPut(ViewManager viewManager){
        Game game = gameView.getGame();
        Position putPosition = put(game.getBoard(), game.getCurrentPlayerIndex());
        Messenger.sendPutMessage(viewManager, putPosition, true);
    }


    public void triggerMove(ViewManager viewManager){
        Game game = gameView.getGame();
        boolean allowedToJump = game.getBoard().numberOfStonesOf(game.getCurrentPlayerIndex()) == 3;
        Move move = move(game.getBoard(), game.getCurrentPlayerIndex(), allowedToJump);
        Messenger.sendMoveMessage(viewManager, move, true);
    }


    public void triggerKill(ViewManager viewManager){
        Game game = gameView.getGame();
        Position killPosition = kill(game.getBoard(), game.getCurrentPlayerIndex(), game.getOtherPlayerIndex());
        Messenger.sendKillMessage(viewManager, killPosition, true);
    }
}
