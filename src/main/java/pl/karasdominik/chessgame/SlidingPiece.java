package pl.karasdominik.chessgame;

public abstract class SlidingPiece extends Piece {

    public SlidingPiece(boolean isWhite, String type, int row, int col) {
        super(isWhite, type, row, col);
    }

    @Override
    public void getPossibleMoves(Chessboard chessboard) {

        availableMoves.clear();
        int currentRow = Helper.convertSquareToInts(piecePosition)[0];
        int currentCol = Helper.convertSquareToInts(piecePosition)[1];
        if (this instanceof Rook || this instanceof Queen) {

            // Check moves upwards
            try {
                int row = currentRow - 1;
                while (chessboard.piecesOnBoard[row][currentCol] == null) {
                    availableMoves.add(Helper.convertSquareToString(row, currentCol));
                    row -= 1;
                }
                if (chessboard.piecesOnBoard[row][currentCol].color != color) {
                    availableMoves.add(Helper.convertSquareToString(row, currentCol));
                }
            } catch (ArrayIndexOutOfBoundsException ignored) {
            }

            // Check moves downwards
            try {
                int row = currentRow + 1;
                while (chessboard.piecesOnBoard[row][currentCol] == null) {
                    availableMoves.add(Helper.convertSquareToString(row, currentCol));
                    row += 1;
                }
                if (chessboard.piecesOnBoard[row][currentCol].color != color) {
                    availableMoves.add(Helper.convertSquareToString(row, currentCol));
                }
            } catch (ArrayIndexOutOfBoundsException ignored) {
            }

            // Check moves right
            try {
                int column = currentCol + 1;
                while (chessboard.piecesOnBoard[currentRow][column] == null) {
                    availableMoves.add(Helper.convertSquareToString(currentRow, column));
                    column += 1;
                }
                if (chessboard.piecesOnBoard[currentRow][column].color != color) {
                    availableMoves.add(Helper.convertSquareToString(currentRow, column));
                }
            } catch (ArrayIndexOutOfBoundsException ignored) {
            }

            // Check moves left
            try {
                int column = currentCol - 1;
                while (chessboard.piecesOnBoard[currentRow][column] == null) {
                    availableMoves.add(Helper.convertSquareToString(currentRow, column));
                    column -= 1;
                }
                if (chessboard.piecesOnBoard[currentRow][column].color != color) {
                    availableMoves.add(Helper.convertSquareToString(currentRow, column));
                }
            } catch (ArrayIndexOutOfBoundsException ignored) {
            }
        }

        if (this instanceof Bishop || this instanceof Queen) {

            // Check moves upper left
            int row = currentRow - 1;
            int col = currentCol - 1;
            try {
                while (chessboard.piecesOnBoard[row][col] == null) {
                    availableMoves.add(Helper.convertSquareToString(row, col));
                    row -= 1;
                    col -= 1;
                }
                if (chessboard.piecesOnBoard[row][col].color != color) availableMoves.add(Helper.convertSquareToString(row, col));
            } catch (ArrayIndexOutOfBoundsException ignored) {}

            // Check moves upper right
            try {
                row = currentRow - 1;
                col = currentCol + 1;
                while (chessboard.piecesOnBoard[row][col] == null) {
                    availableMoves.add(Helper.convertSquareToString(row, col));
                    row -= 1;
                    col += 1;
                }
                if (chessboard.piecesOnBoard[row][col].color != color) availableMoves.add(Helper.convertSquareToString(row, col));
            } catch (ArrayIndexOutOfBoundsException ignored){}

            // Check moves bottom left
            try {
                row = currentRow + 1;
                col = currentCol - 1;
                while (chessboard.piecesOnBoard[row][col] == null) {
                    availableMoves.add(Helper.convertSquareToString(row, col));
                    row += 1;
                    col -= 1;
                }
                if (chessboard.piecesOnBoard[row][col].color != color) availableMoves.add(Helper.convertSquareToString(row, col));
            } catch (ArrayIndexOutOfBoundsException ignored){}

            // Check moves bottom right
            try {
                row = currentRow + 1;
                col = currentCol + 1;
                while (chessboard.piecesOnBoard[row][col] == null) {
                    availableMoves.add(Helper.convertSquareToString(row, col));
                    row += 1;
                    col += 1;
                }
                if (chessboard.piecesOnBoard[row][col].color != color) availableMoves.add(Helper.convertSquareToString(row, col));
            } catch (ArrayIndexOutOfBoundsException ignored){}
        }
    }
}
