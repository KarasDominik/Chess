package pl.karasdominik.chessgame;

public class King extends Piece {

    private boolean isFirstMove;

    public King(boolean isWhite, String type, int row, int col){
        super(isWhite, type, row, col);
        isFirstMove = true;
    }

    @Override
    public void getPossibleMoves(int currentRow, int currentCol, Chessboard chessboard) {

        availableMoves.clear();

        // Check move upwards
        int row = currentRow - 1;
        int col = currentCol;
        try {
            if (chessboard.piecesOnBoard[row][col] == null || chessboard.piecesOnBoard[row][col].color != color){
                availableMoves.add(Chessboard.convertSquareToString(row, col));
            }
        } catch (ArrayIndexOutOfBoundsException ignored){}

        // Check move upwards left
        row = currentRow - 1;
        col = currentCol - 1;
        try {
            if (chessboard.piecesOnBoard[row][col] == null || chessboard.piecesOnBoard[row][col].color != color){
                availableMoves.add(Chessboard.convertSquareToString(row, col));
            }
        } catch (ArrayIndexOutOfBoundsException ignored){}

        // Check move upwards right
        row = currentRow - 1;
        col = currentCol + 1;
        try {
            if (chessboard.piecesOnBoard[row][col] == null || chessboard.piecesOnBoard[row][col].color != color){
                availableMoves.add(Chessboard.convertSquareToString(row, col));
            }
        } catch (ArrayIndexOutOfBoundsException ignored){}

        // Check move left
        row = currentRow;
        col = currentCol - 1;
        try {
            if (chessboard.piecesOnBoard[row][col] == null || chessboard.piecesOnBoard[row][col].color != color){
                availableMoves.add(Chessboard.convertSquareToString(row, col));
            }
        } catch (ArrayIndexOutOfBoundsException ignored){}

        // Check move right
        row = currentRow;
        col = currentCol + 1;
        try {
            if (chessboard.piecesOnBoard[row][col] == null || chessboard.piecesOnBoard[row][col].color != color){
                availableMoves.add(Chessboard.convertSquareToString(row, col));
            }
        } catch (ArrayIndexOutOfBoundsException ignored){}

        // Check move downwards left
        row = currentRow + 1;
        col = currentCol - 1;
        try {
            if (chessboard.piecesOnBoard[row][col] == null || chessboard.piecesOnBoard[row][col].color != color){
                availableMoves.add(Chessboard.convertSquareToString(row, col));
            }
        } catch (ArrayIndexOutOfBoundsException ignored){}

        // Check move downwards right
        row = currentRow + 1;
        col = currentCol + 1;
        try {
            if (chessboard.piecesOnBoard[row][col] == null || chessboard.piecesOnBoard[row][col].color != color){
                availableMoves.add(Chessboard.convertSquareToString(row, col));
            }
        } catch (ArrayIndexOutOfBoundsException ignored){}

    }
}
