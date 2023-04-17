package pl.karasdominik.chessgame;

public class Knight extends Piece {

    public Knight(boolean isWhite, String type, int row, int col){
        super(isWhite, type, row, col);
        this.value = 3;
    }

    @Override
    public void getPossibleMoves(Chessboard chessboard) {

        availableMoves.clear();

        int currentRow = Helper.convertSquareToInts(piecePosition)[0];
        int currentCol = Helper.convertSquareToInts(piecePosition)[1];

        // Check upper left
        try {
            if (chessboard.piecesOnBoard[currentRow - 2][currentCol - 1] == null ||
                chessboard.piecesOnBoard[currentRow - 2][currentCol - 1].color != color){
                availableMoves.add(Helper.convertSquareToString(currentRow - 2, currentCol - 1));
            }
        } catch (ArrayIndexOutOfBoundsException ignored){}

        // Check upper right
        try {
            if (chessboard.piecesOnBoard[currentRow - 2][currentCol + 1] == null ||
                    chessboard.piecesOnBoard[currentRow - 2][currentCol + 1].color != color){
                availableMoves.add(Helper.convertSquareToString(currentRow - 2, currentCol + 1));
            }
        } catch (ArrayIndexOutOfBoundsException ignored){}

        // Check left up
        try {
            if (chessboard.piecesOnBoard[currentRow - 1][currentCol - 2] == null ||
                    chessboard.piecesOnBoard[currentRow - 1][currentCol - 2].color != color){
                availableMoves.add(Helper.convertSquareToString(currentRow - 1, currentCol - 2));
            }
        } catch (ArrayIndexOutOfBoundsException ignored){}

        // Check left down
        try {
            if (chessboard.piecesOnBoard[currentRow + 1][currentCol - 2] == null ||
                    chessboard.piecesOnBoard[currentRow + 1][currentCol - 2].color != color){
                availableMoves.add(Helper.convertSquareToString(currentRow + 1, currentCol - 2));
            }
        } catch (ArrayIndexOutOfBoundsException ignored){}

        // Check bottom left
        try {
            if (chessboard.piecesOnBoard[currentRow + 2][currentCol - 1] == null ||
                    chessboard.piecesOnBoard[currentRow + 2][currentCol - 1].color != color){
                availableMoves.add(Helper.convertSquareToString(currentRow + 2, currentCol - 1));
            }
        } catch (ArrayIndexOutOfBoundsException ignored){}

        // Check bottom right
        try {
            if (chessboard.piecesOnBoard[currentRow + 2][currentCol + 1] == null ||
                    chessboard.piecesOnBoard[currentRow + 2][currentCol + 1].color != color){
                availableMoves.add(Helper.convertSquareToString(currentRow + 2, currentCol + 1));
            }
        } catch (ArrayIndexOutOfBoundsException ignored){}

        // Check right up
        try {
            if (chessboard.piecesOnBoard[currentRow - 1][currentCol + 2] == null ||
                    chessboard.piecesOnBoard[currentRow - 1][currentCol + 2].color != color){
                availableMoves.add(Helper.convertSquareToString(currentRow - 1, currentCol + 2));
            }
        } catch (ArrayIndexOutOfBoundsException ignored){}

        // Check right down
        try {
            if (chessboard.piecesOnBoard[currentRow + 1][currentCol + 2] == null ||
                    chessboard.piecesOnBoard[currentRow + 1][currentCol + 2].color != color){
                availableMoves.add(Helper.convertSquareToString(currentRow + 1, currentCol + 2));
            }
        } catch (ArrayIndexOutOfBoundsException ignored){}

    }
}
