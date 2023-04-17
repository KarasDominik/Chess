package pl.karasdominik.chessgame;

import java.util.List;
import java.util.Random;

public class Engine {

    protected boolean playsWhite;
    private Chessboard chessboard = chessApplication.getChessboard();

    public Engine(boolean playsWhite){
        this.playsWhite = playsWhite;
    }

    public void makeMove(){
        long startTime = System.currentTimeMillis();
//        Move move = findTheBestMove();
        long endTime = System.currentTimeMillis();
        Random random = new Random();

        Move move = chessboard.possibleMoves.get(random.nextInt(chessboard.possibleMoves.size()));
        chessboard.makeMove(move, true);
    }

    public boolean isMyTurn(){
        return playsWhite && chessboard.moves.size() % 2 == 0 || !playsWhite && chessboard.moves.size() % 2 != 0;
    }

    private Move findTheBestMove(){
        List<Move> movesAvailable = chessboard.moveGenerator();
        Move theBestMove = null;
        int theBestEvaluation = Integer.MAX_VALUE;
        for(Move move : movesAvailable){
            chessboard.makeMove(move, false);
            int evaluation = chessboard.Search(3, !playsWhite, Integer.MIN_VALUE, Integer.MAX_VALUE);
            if (evaluation < theBestEvaluation){
                theBestMove = move;
                theBestEvaluation = evaluation;
            }
            chessboard.unmakeMove();
        }
        return theBestMove;
    }
}
