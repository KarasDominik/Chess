package pl.karasdominik.chessgame;

public class Queen extends Piece {

    public Queen(boolean isWhite){
        super(isWhite, "queen");
    }

    @Override
    public boolean canMoveTo(int oldRow, int oldCol, int newRow, int newCol, Chessboard chessboard) {
        return true;
    }
}
