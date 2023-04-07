package pl.karasdominik.chessgame;

public class Rook extends Piece {

    public Rook(boolean isWhite){
        super(isWhite, "rook");
    }


    @Override
    public boolean canMoveTo(int oldRow, int oldCol, int newRow, int newCol, Chessboard chessboard) {
        return true;
    }
}
