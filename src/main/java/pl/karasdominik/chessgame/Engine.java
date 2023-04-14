package pl.karasdominik.chessgame;

import javafx.scene.layout.GridPane;

import java.util.List;

public class Engine {

    protected boolean playsWhite;
    private Chessboard chessboard = chessApplication.getChessboard();
    private List<Piece> piecesAvailable;

    public Engine(boolean playsWhite){
        this.playsWhite = playsWhite;
        this.piecesAvailable = playsWhite ? chessboard.whitePiecesLeft : chessboard.blackPiecesLeft;
    }

    public void makeMove(GridPane grid){
        Piece piece = piecesAvailable.get(piecesAvailable.size() - 1);
        System.out.println(piecesAvailable);
        for (String move : piece.availableMoves){
            int newRow = Helper.convertSquareToInts(move)[0];
            int newCol = Helper.convertSquareToInts(move)[1];
            int oldRow = Helper.convertSquareToInts(piece.piecePosition)[0];
            int oldCol = Helper.convertSquareToInts(piece.piecePosition)[1];
            chessboard.generateMove(piece, newRow, newCol, oldRow, oldCol, grid);
            return;
        }
    }

    public boolean isMyTurn(){
        return playsWhite && chessboard.moves.size() % 2 == 0 || !playsWhite && chessboard.moves.size() % 2 != 0;
    }
}
