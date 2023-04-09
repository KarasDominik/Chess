package pl.karasdominik.chessgame;

public class Knight extends Piece {

    public Knight(boolean isWhite, String type, int row, int col){
        super(isWhite, type, row, col);
    }

    @Override
    public void getPossibleMoves(int currentRow, int currentCol, Chessboard chessboard) {

        availableMoves.clear();

        // Check upper left
        try {
            if (chessboard.piecesOnBoard[currentRow - 2][currentCol - 1] == null ||
                chessboard.piecesOnBoard[currentRow - 2][currentCol - 1].color != color){
                availableMoves.add(Chessboard.convertSquareToString(currentRow - 2, currentCol - 1));
            }
        } catch (ArrayIndexOutOfBoundsException ignored){}

        // Check upper right
        try {
            if (chessboard.piecesOnBoard[currentRow - 2][currentCol + 1] == null ||
                    chessboard.piecesOnBoard[currentRow - 2][currentCol + 1].color != color){
                availableMoves.add(Chessboard.convertSquareToString(currentRow - 2, currentCol + 1));
            }
        } catch (ArrayIndexOutOfBoundsException ignored){}

        // Check left up
        try {
            if (chessboard.piecesOnBoard[currentRow - 1][currentCol - 2] == null ||
                    chessboard.piecesOnBoard[currentRow - 1][currentCol - 2].color != color){
                availableMoves.add(Chessboard.convertSquareToString(currentRow - 1, currentCol - 2));
            }
        } catch (ArrayIndexOutOfBoundsException ignored){}

        // Check left down
        try {
            if (chessboard.piecesOnBoard[currentRow + 1][currentCol - 2] == null ||
                    chessboard.piecesOnBoard[currentRow + 1][currentCol - 2].color != color){
                availableMoves.add(Chessboard.convertSquareToString(currentRow + 1, currentCol - 2));
            }
        } catch (ArrayIndexOutOfBoundsException ignored){}

        // Check bottom left
        try {
            if (chessboard.piecesOnBoard[currentRow + 2][currentCol - 1] == null ||
                    chessboard.piecesOnBoard[currentRow + 2][currentCol - 1].color != color){
                availableMoves.add(Chessboard.convertSquareToString(currentRow + 2, currentCol - 1));
            }
        } catch (ArrayIndexOutOfBoundsException ignored){}

        // Check bottom right
        try {
            if (chessboard.piecesOnBoard[currentRow + 2][currentCol + 1] == null ||
                    chessboard.piecesOnBoard[currentRow + 2][currentCol + 1].color != color){
                availableMoves.add(Chessboard.convertSquareToString(currentRow + 2, currentCol + 1));
            }
        } catch (ArrayIndexOutOfBoundsException ignored){}

        // Check right up
        try {
            if (chessboard.piecesOnBoard[currentRow - 1][currentCol + 2] == null ||
                    chessboard.piecesOnBoard[currentRow - 1][currentCol + 2].color != color){
                availableMoves.add(Chessboard.convertSquareToString(currentRow - 1, currentCol + 2));
            }
        } catch (ArrayIndexOutOfBoundsException ignored){}

        // Check right down
        try {
            if (chessboard.piecesOnBoard[currentRow + 1][currentCol + 2] == null ||
                    chessboard.piecesOnBoard[currentRow + 1][currentCol + 2].color != color){
                availableMoves.add(Chessboard.convertSquareToString(currentRow + 1, currentCol + 2));
            }
        } catch (ArrayIndexOutOfBoundsException ignored){}

    }
}
