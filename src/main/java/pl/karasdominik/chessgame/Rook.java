package pl.karasdominik.chessgame;

public class Rook extends Piece {

    public Rook(boolean isWhite, String type, int row, int col){
        super(isWhite, type, row, col);
    }


    @Override
    public boolean canMoveTo(int oldRow, int oldCol, int newRow, int newCol, Chessboard chessboard) {
        return true;
    }

    @Override
    public void getPossibleMoves(int currentRow, int currentCol, Chessboard chessboard) {

    }
}
