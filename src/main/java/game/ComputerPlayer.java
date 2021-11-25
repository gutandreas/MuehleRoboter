package game;

import View.GameView;
import View.ViewManager;
import Websocket.MessageHandler;
import Websocket.Messenger;

import java.util.Stack;

public class ComputerPlayer extends Player implements MessageHandler {


    GameTree gameTree = new GameTree();
    ScorePoints putPoints;
    ScorePoints movePoints;
    int levelLimit;

    public ComputerPlayer(GameView gameView, String name, STONECOLOR stonecolor, ScorePoints putPoints, ScorePoints movePoints, int levelLimit) {
        super(gameView, name, " ", stonecolor);
        this.putPoints = putPoints;
        this.movePoints = movePoints;
        this.levelLimit = levelLimit;
    }

    Position put(Board board, int playerIndex) {

        gameTree.initializeRoot(board);

        recursivePutBfs(gameTree.getRoot(), putPoints, movePoints, playerIndex, playerIndex, levelLimit);

        System.out.println(gameTree);

        Stack<GameTreeNode> winningPath = gameTree.getPathToBestLeaf();
        System.out.println("Gewinnerpfad:");
        while (!winningPath.isEmpty()){
            System.out.println(winningPath.pop());
        }

        System.out.println("Gesetzter Stein: " + gameTree.getBestPut());

        //gameView.getConnection().put(gameTree.getBestPut(), playerIndex);

        return gameTree.getBestPut();
    }

    private void recursivePutBfs(GameTreeNode set, ScorePoints putPoints, ScorePoints movePoints, int scorePlayerIndex, int currentPlayerIndex, int levelLimit){

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

        for (Position freeField : Advisor.getAllFreeFields(set.getBoard())){
            pretendPut(set.getBoard(), freeField, putPoints, set, scorePlayerIndex, tempCurrentPlayerIndex, set.getLevel()+1);
        }

        if (set.getLevel()%2 == 0){
            gameTree.keepOnlyBestChildren(set, 10);}
        else {
            gameTree.keepOnlyWorstChildren(set, 1);
        }

        for (GameTreeNode child : set.getChildren()){
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
        gameTreeNode1.setScore(Advisor.getScore(gameTreeNode1, scorePoints, scorePlayerIndex, false));



        if (gameTreeNode1.getBoard().checkMorris(gameTreeNode1.getPut())){
            for (Position killPosition : Advisor.getAllPossibleKills(clonedBoard1,currentPlayerIndex)){
                GameTreeNode gameTreeNode2 = new GameTreeNode();
                gameTreeNode2.setPut(put);
                gameTreeNode2.setLevel(level);
                gameTreeNode2.setKill(killPosition);

                Board clonedBoard2 = (Board) clonedBoard1.clone();
                clonedBoard2.clearStone(killPosition);

                gameTreeNode2.setBoard(clonedBoard2);
                gameTreeNode2.setScore(Advisor.getScore(gameTreeNode2, scorePoints, scorePlayerIndex, false));
                gameTree.addSet(parent, gameTreeNode2);
            }
        }
        else {
            gameTree.addSet(parent, gameTreeNode1);
        }

    }


    Move move(Board board, int playerIndex, boolean allowedToJump) {


        gameTree.initializeRoot(board);

        recursiveMoveBfs(gameTree.getRoot(), movePoints, playerIndex, playerIndex, levelLimit);

        System.out.println(gameTree);

        Stack<GameTreeNode> winningPath = gameTree.getPathToBestLeaf();
        System.out.println("Gewinnerpfad:");
        while (!winningPath.isEmpty()){
            System.out.println(winningPath.pop());
        }

        System.out.println("Getätigter Zug: " + gameTree.getBestMove());

        //gameView.getConnection().move(gameTree.getBestMove(), allowedToJump);

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
            gameTree.keepOnlyBestChildren(set, 3);}
        else {
            gameTree.keepOnlyWorstChildren(set, 1);
        }


        for (GameTreeNode child : set.getChildren()){
            recursiveMoveBfs(child, scorePoints, scorePlayerIndex, tempCurrentPlayerIndex, levelLimit);
        }
    }


    private void pretendMove(Board board, Move move, ScorePoints scorePoints, GameTreeNode parent, int scorePlayerIndex, int currentPlayerIndex, int level){

        GameTreeNode gameTreeNode1 = new GameTreeNode();
        gameTreeNode1.setMove(move);
        gameTreeNode1.setLevel(level);

        Board clonedBoard1 = (Board) board.clone();
        clonedBoard1.move(move, currentPlayerIndex);

        gameTreeNode1.setBoard(clonedBoard1);
        gameTreeNode1.setScore(Advisor.getScore(gameTreeNode1, scorePoints, scorePlayerIndex, false));



        if (gameTreeNode1.getBoard().checkMorris(gameTreeNode1.getMove().getTo())){
            for (Position killPosition : Advisor.getAllPossibleKills(clonedBoard1,currentPlayerIndex)){
                GameTreeNode gameTreeNode2 = new GameTreeNode();
                gameTreeNode2.setMove(move);
                gameTreeNode2.setLevel(level);
                gameTreeNode2.setKill(killPosition);

                Board clonedBoard2 = (Board) clonedBoard1.clone();
                clonedBoard2.clearStone(killPosition);

                gameTreeNode2.setBoard(clonedBoard2);
                gameTreeNode2.setScore(Advisor.getScore(gameTreeNode2, scorePoints, scorePlayerIndex, false));
                gameTree.addSet(parent, gameTreeNode2);
            }
        }
        else {
            gameTree.addSet(parent, gameTreeNode1);
        }
    }



    Position kill(Board board, int ownPlayerIndex, int otherPlayerIndex) {

        //gameView.getConnection().kill(gameTree.getBestKill(), ownPlayerIndex);

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
        boolean allowedToJump = game.getBoard().countPlayersStones(game.getCurrentPlayerIndex()) == 3;
        Move move = move(game.getBoard(), game.getCurrentPlayerIndex(), allowedToJump);
        Messenger.sendMoveMessage(viewManager, move, true);
    }

    public void triggerKill(ViewManager viewManager){
        Game game = gameView.getGame();
        Position killPosition = kill(game.getBoard(), game.getCurrentPlayerIndex(), game.getOtherPlayerIndex());
        Messenger.sendKillMessage(viewManager, killPosition, true);
    }
}
