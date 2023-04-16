package pl.karasdominik.chessgame;

import javafx.scene.layout.GridPane;

import java.util.List;
import java.util.Random;

public class Engine {

    protected boolean playsWhite;
    private Chessboard chessboard = chessApplication.getChessboard();
    private List<Move> movesAvailable;

    public Engine(boolean playsWhite){
        this.playsWhite = playsWhite;
        this.movesAvailable = playsWhite ? chessboard.possibleMovesForWhite : chessboard.possibleMovesForBlack;
    }

    public void makeMove(GridPane grid){
        Random random = new Random();
        int randomMove = random.nextInt(movesAvailable.size());
        Move move = movesAvailable.get(randomMove);
        Piece piece = move.piece;
        int newRow = Helper.convertSquareToInts(move.targetSquare)[0];
        int newCol = Helper.convertSquareToInts(move.targetSquare)[1];
        int oldRow = Helper.convertSquareToInts(move.initialSquare)[0];
        int oldCol = Helper.convertSquareToInts(move.initialSquare)[1];
        chessboard.generateMove(piece, newRow, newCol, oldRow, oldCol, grid);
    }

    public boolean isMyTurn(){
        return playsWhite && chessboard.moves.size() % 2 == 0 || !playsWhite && chessboard.moves.size() % 2 != 0;
    }

//    private Move findTheBestMove(){
//
//    }
}
