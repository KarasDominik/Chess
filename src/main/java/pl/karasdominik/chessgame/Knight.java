package pl.karasdominik.chessgame;

public class Knight extends Piece {

    public Knight(boolean isWhite){
        super(isWhite, "knight");
    }

    @Override
    public boolean canMoveTo(int oldRow, int oldCol, int newRow, int newCol, Chessboard chessboard) {
        return true;
    }
}
