package pl.karasdominik.chessgame;

public class Pawn extends Piece {

    private boolean isFirstMove;

    public Pawn(boolean isWhite, String type, int row, int col){
        super(isWhite, type, row, col);
        isFirstMove = true;
    }

    @Override
    public boolean canMoveTo(int oldRow, int oldCol, int newRow, int newCol, Chessboard chessboard) {
        getPossibleMoves(oldRow, oldCol, chessboard);

        String targetSquare = Chessboard.convertSquareToString(newRow, newCol);
        for (String move : availableMoves) {
            if (move.equals(targetSquare)) {
                isFirstMove = false;
                return true;
            }
        }
        return false;
    }

    public void getPossibleMoves(int currentRow, int currentCol, Chessboard chessboard) {

        availableMoves.clear();

//        // If it's pawn's first move
//        if (isFirstMove) {
//            if (isWhite && chessboard.piecesOnBoard[currentRow - 2][currentCol] == 0) {
//                availableMoves.add(Chessboard.convertSquareToString(4, currentCol));
//            } else {
//                if (!isWhite && chessboard.piecesOnBoard[currentRow + 2][currentCol] == 0) {
//                    availableMoves.add(Chessboard.convertSquareToString(3, currentCol));
//                }
//            }
//        }

        int availableMoveForward = isWhite ? -1 : 1;

        // Check if it can move forward
        try {
            if (chessboard.piecesOnBoard[currentRow + availableMoveForward][currentCol] == 0) {
                availableMoves.add(Chessboard.convertSquareToString(currentRow + availableMoveForward, currentCol));
                if (isFirstMove) {
                    if (isWhite && chessboard.piecesOnBoard[currentRow - 2][currentCol] == 0) {
                        availableMoves.add(Chessboard.convertSquareToString(4, currentCol));
                    } else {
                        if (!isWhite && chessboard.piecesOnBoard[currentRow + 2][currentCol] == 0) {
                            availableMoves.add(Chessboard.convertSquareToString(3, currentCol));
                        }
                    }
                }
            }
        } catch (ArrayIndexOutOfBoundsException ignored){}

        // Check if it can move upper left
        try {
            int leftTargetSquare = chessboard.piecesOnBoard[currentRow + availableMoveForward][currentCol - 1];
            if ((leftTargetSquare != 0) &&
                    (leftTargetSquare > 15 && getID() < 15 || leftTargetSquare < 15 && getID() > 15)) {
                availableMoves.add(Chessboard.convertSquareToString(currentRow + availableMoveForward, currentCol - 1));
            }
        } catch (ArrayIndexOutOfBoundsException ignored){}

        // Check if it can move upper right
        try {
            int rightTargetSquare = chessboard.piecesOnBoard[currentRow + availableMoveForward][currentCol + 1];
            if ((rightTargetSquare != 0) &&
                    (rightTargetSquare > 15 && getID() < 15 || rightTargetSquare < 15 && getID() > 15)) {
                availableMoves.add(Chessboard.convertSquareToString(currentRow + availableMoveForward, currentCol + 1));
            }
        } catch (ArrayIndexOutOfBoundsException ignored){}
    }
}
