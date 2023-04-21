package pl.karasdominik.chessgame;

import java.util.ArrayList;
import java.util.List;

public class Engine {

    protected boolean playsWhite;
    private final Chessboard chessboard = chessApplication.getChessboard();
    public int positionsSearched;
    public List<Long> time = new ArrayList<>();

    public Engine(boolean playsWhite){
        this.playsWhite = playsWhite;
    }

    public void makeMove(){
        System.out.println("Starting to search...");
        long startTime = System.currentTimeMillis();
        Move move = findTheBestMove();
        long endTime = System.currentTimeMillis();
        System.out.println("Finding the best move took: " + (endTime - startTime) + "ms");
        time.add(endTime - startTime);
        System.out.println("Evaluation before move: " + chessboard.evaluate());
        chessboard.makeMove(move, true);
        System.out.println("Evaluation after move: " + chessboard.evaluate());
    }

    public boolean isMyTurn(){
        return playsWhite && chessboard.moves.size() % 2 == 0 || !playsWhite && chessboard.moves.size() % 2 != 0;
    }

    private Move findTheBestMove(){
        positionsSearched = 0;
        List<Move> movesAvailable = chessboard.moveGenerator();
        Move theBestMove = null;
        double theBestEvaluation = Double.MAX_VALUE;
        for(Move move : movesAvailable){
            chessboard.makeMove(move, false);
            double evaluation = chessboard.Search(3, !playsWhite, Double.MIN_VALUE, Double.MAX_VALUE);
            if (evaluation < theBestEvaluation){
                theBestMove = move;
                theBestEvaluation = evaluation;
            }
            chessboard.unmakeMove();
        }
        System.out.println("Positions searched: " + positionsSearched);
        return theBestMove;
    }
}
