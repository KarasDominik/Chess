package pl.karasdominik.chessgame;

public class Bishop extends Piece {

    public Bishop(boolean isWhite){
        super(isWhite, "bishop");
    }

    @Override
    public boolean canMoveTo(int oldRow, int oldCol, int newRow, int newCol, Chessboard chessboard) {
        return true;
    }
}
