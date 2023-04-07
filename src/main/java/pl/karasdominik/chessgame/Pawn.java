package pl.karasdominik.chessgame;

public class Pawn extends Piece {

    private boolean isFirstMove;

    public Pawn(boolean isWhite) {
        super(isWhite, "pawn");
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

        // If it's pawn's first move
        if (isFirstMove) {
            if (isWhite) {
                availableMoves.add(Chessboard.convertSquareToString(4, currentCol));
                availableMoves.add(Chessboard.convertSquareToString(5, currentCol));
            } else {
                availableMoves.add(Chessboard.convertSquareToString(2, currentCol));
                availableMoves.add(Chessboard.convertSquareToString(3, currentCol));
            }
            return;
        }

        int availableMoveForward = isWhite ? -1 : 1;
        availableMoves.clear();

        // Check if it can move forward
        if (chessboard.piecesOnBoard[currentRow + availableMoveForward][currentCol] == 0) {
            availableMoves.add(Chessboard.convertSquareToString(currentRow + availableMoveForward, currentCol));
        }

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
