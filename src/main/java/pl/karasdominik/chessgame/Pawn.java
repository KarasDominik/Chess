package pl.karasdominik.chessgame;

public class Pawn extends Piece {

    protected boolean isFirstMove;

    public Pawn(boolean isWhite, String type, int row, int col) {
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

    public void getPossibleMoves(int currentRow, int currentCol, Chessboard chessboard) {

        availableMoves.clear();

        int availableMoveForward = isWhite ? -1 : 1;

        // Check if it can move forward
        try {
            if (chessboard.piecesOnBoard[currentRow + availableMoveForward][currentCol] == null) {
                availableMoves.add(Chessboard.convertSquareToString(currentRow + availableMoveForward, currentCol));
                // If it can move 1 square forward, check if it can move 2 squares forward as first move
                if (isFirstMove) {
                    if (isWhite && chessboard.piecesOnBoard[currentRow - 2][currentCol] == null) {
                        availableMoves.add(Chessboard.convertSquareToString(4, currentCol));
                    } else {
                        if (!isWhite && chessboard.piecesOnBoard[currentRow + 2][currentCol] == null) {
                            availableMoves.add(Chessboard.convertSquareToString(3, currentCol));
                        }
                    }
                }
            }
        } catch (ArrayIndexOutOfBoundsException ignored) {}

        // Check if it can move upper left
        try {
            Piece leftTargetSquare = chessboard.piecesOnBoard[currentRow + availableMoveForward][currentCol - 1];
            if ((leftTargetSquare != null) && (leftTargetSquare.color != color)) {
                availableMoves.add(Chessboard.convertSquareToString(currentRow + availableMoveForward, currentCol - 1));
            }
        } catch (ArrayIndexOutOfBoundsException ignored) {}

        // Check if it can move upper right
        try {
            Piece rightTargetSquare = chessboard.piecesOnBoard[currentRow + availableMoveForward][currentCol + 1];
            if (rightTargetSquare != null && rightTargetSquare.color != color) {
                availableMoves.add(Chessboard.convertSquareToString(currentRow + availableMoveForward, currentCol + 1));
            }
        } catch (ArrayIndexOutOfBoundsException ignored) {}
        // Check if it can capture en passant
        int initialRow = isWhite ? 3 : 4;
        if (Chessboard.convertSquareToInts(piecePosition)[0] == initialRow){
            try{
                if (chessboard.piecesOnBoard[currentRow][currentCol - 1] instanceof Pawn || chessboard.piecesOnBoard[currentRow][currentCol + 1] instanceof Pawn){
                    Move lastMove = chessboard.moves.get(chessboard.moves.size() - 1);
                    if (Math.abs(Chessboard.convertSquareToInts(lastMove.targetSquare())[0] - Chessboard.convertSquareToInts(lastMove.initialSquare())[0]) == 2){
                        availableMoves.add(Chessboard.convertSquareToString(currentRow + availableMoveForward, Chessboard.convertSquareToInts(lastMove.targetSquare())[1]));
                    }
                }
            } catch (ArrayIndexOutOfBoundsException ignored){}
        }
    }
}
