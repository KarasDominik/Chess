package pl.karasdominik.chessgame;

public class Rook extends SlidingPiece {

    protected boolean isFirstMove;

    public Rook(boolean isWhite, String type, int row, int col){
        super(isWhite, type, row, col);
        isFirstMove = true;
    }


    @Override
    public boolean canMoveTo(int oldRow, int oldCol, int newRow, int newCol, Chessboard chessboard) {
        return super.canMoveTo(oldRow, oldCol, newRow, newCol, chessboard);
    }
}
