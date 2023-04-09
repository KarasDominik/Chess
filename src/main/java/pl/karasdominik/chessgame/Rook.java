package pl.karasdominik.chessgame;

public class Rook extends Piece {

    protected boolean isFirstMove;

    public Rook(boolean isWhite, String type, int row, int col){
        super(isWhite, type, row, col);
        isFirstMove = true;
    }


    @Override
    public boolean canMoveTo(int oldRow, int oldCol, int newRow, int newCol, Chessboard chessboard) {
        if (super.canMoveTo(oldRow, oldCol, newRow, newCol, chessboard)) {
            isFirstMove = false;
            return true;
        }
        return false;
    }

    @Override
    public void getPossibleMoves(int currentRow, int currentCol, Chessboard chessboard) {

        availableMoves.clear();

        try {
            // Check moves upwards
            int row = currentRow - 1;
            while (row >= 0 && chessboard.piecesOnBoard[row][currentCol] == null) {
                availableMoves.add(Chessboard.convertSquareToString(row, currentCol));
                row -= 1;
            }
            if (chessboard.piecesOnBoard[row][currentCol].color != color) {
                availableMoves.add(Chessboard.convertSquareToString(row, currentCol));
            }
        } catch (ArrayIndexOutOfBoundsException ignored){}

        try {
            // Check move downwards
            int row = currentRow + 1;
            while (row < Chessboard.SIZE && chessboard.piecesOnBoard[row][currentCol] == null) {
                availableMoves.add(Chessboard.convertSquareToString(row, currentCol));
                row += 1;
            }
            if (chessboard.piecesOnBoard[row][currentCol].color != color) {
                availableMoves.add(Chessboard.convertSquareToString(row, currentCol));
            }
        } catch (ArrayIndexOutOfBoundsException ignored){}

        try {
            // Check move right
            int column = currentCol + 1;
            while (column < Chessboard.SIZE && chessboard.piecesOnBoard[currentRow][column] == null){
                availableMoves.add(Chessboard.convertSquareToString(currentRow, column));
                column += 1;
            }
            if (chessboard.piecesOnBoard[currentRow][column].color != color){
                availableMoves.add(Chessboard.convertSquareToString(currentRow, column));
            }
        } catch (ArrayIndexOutOfBoundsException ignored){}

        try {
            // Check move left
            int column = currentCol - 1;
            while (column >= 0 && chessboard.piecesOnBoard[currentRow][column] == null){
                availableMoves.add(Chessboard.convertSquareToString(currentRow, column));
                column -= 1;
            }
            if (chessboard.piecesOnBoard[currentRow][column].color != color){
                availableMoves.add(Chessboard.convertSquareToString(currentRow, column));
            }
        } catch (ArrayIndexOutOfBoundsException ignored){}
    }
}
