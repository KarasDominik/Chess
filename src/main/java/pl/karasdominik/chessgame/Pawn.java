package pl.karasdominik.chessgame;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece {

    protected List<String> squaresAttacked = new ArrayList<>();

    public Pawn(boolean isWhite, String type, int row, int col) {
        super(isWhite, type, row, col);
    }

    public void getPossibleMoves(int currentRow, int currentCol, Chessboard chessboard) {

        availableMoves.clear();
        squaresAttacked.clear();

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
        } catch (ArrayIndexOutOfBoundsException ignored){}
        try {
            // Check if it can move upper left
            Piece leftTargetSquare = chessboard.piecesOnBoard[currentRow + availableMoveForward][currentCol - 1];
            if ((leftTargetSquare != null) && (leftTargetSquare.color != color)) {
                availableMoves.add(Chessboard.convertSquareToString(currentRow + availableMoveForward, currentCol - 1));
            }
            squaresAttacked.add(Chessboard.convertSquareToString(currentRow + availableMoveForward, currentCol - 1));
        } catch (ArrayIndexOutOfBoundsException ignored){}

        try {
            // Check if it can move upper right
            Piece rightTargetSquare = chessboard.piecesOnBoard[currentRow + availableMoveForward][currentCol + 1];
            if (rightTargetSquare != null && rightTargetSquare.color != color) {
                availableMoves.add(Chessboard.convertSquareToString(currentRow + availableMoveForward, currentCol + 1));
            }
            squaresAttacked.add(Chessboard.convertSquareToString(currentRow + availableMoveForward, currentCol + 1));
        } catch (ArrayIndexOutOfBoundsException ignored){}

        try {
            // Check if it can capture en passant
            int initialRow = isWhite ? 3 : 4;
            if (Chessboard.convertSquareToInts(piecePosition)[0] == initialRow) {
                if (chessboard.piecesOnBoard[currentRow][currentCol - 1] instanceof Pawn || chessboard.piecesOnBoard[currentRow][currentCol + 1] instanceof Pawn) {
                    Move lastMove = chessboard.moves.get(chessboard.moves.size() - 1);
                    if (Math.abs(Chessboard.convertSquareToInts(lastMove.targetSquare())[0] - Chessboard.convertSquareToInts(lastMove.initialSquare())[0]) == 2) {
                        availableMoves.add(Chessboard.convertSquareToString(currentRow + availableMoveForward, Chessboard.convertSquareToInts(lastMove.targetSquare())[1]));
                    }
                }
            }
        } catch (ArrayIndexOutOfBoundsException ignored){}
    }
}
