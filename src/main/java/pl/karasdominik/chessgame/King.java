package pl.karasdominik.chessgame;

public class King extends Piece {

    public King(boolean isWhite){
        super(isWhite, "king");
    }

    @Override
    public boolean canMoveTo(int oldRow, int oldCol, int newRow, int newCol, Chessboard chessboard) {
        return true;
    }
}
