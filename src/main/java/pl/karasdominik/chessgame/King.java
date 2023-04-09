package pl.karasdominik.chessgame;

import java.util.ArrayList;
import java.util.List;

public class King extends Piece {

    protected List<String> squaresAttacked = new ArrayList<>();

    public King(boolean isWhite, String type, int row, int col){
        super(isWhite, type, row, col);
    }

    @Override
    public void getPossibleMoves(int currentRow, int currentCol, Chessboard chessboard) {

        availableMoves.clear();
        squaresAttacked.clear();

        // Check move upwards
        int row = currentRow - 1;
        int col = currentCol;
        Piece squareToCheck;
        try {
            squareToCheck = chessboard.piecesOnBoard[row][col];
            if (squareToCheck == null || squareToCheck.color != color){
                availableMoves.add(Chessboard.convertSquareToString(row, col));
            }
            squaresAttacked.add(Chessboard.convertSquareToString(row, col));
        } catch (ArrayIndexOutOfBoundsException ignored){}

        // Check move upwards left
        row = currentRow - 1;
        col = currentCol - 1;
        try {
            squareToCheck = chessboard.piecesOnBoard[row][col];
            if (squareToCheck == null || squareToCheck.color != color){
                availableMoves.add(Chessboard.convertSquareToString(row, col));
            }
            squaresAttacked.add(Chessboard.convertSquareToString(row, col));
        } catch (ArrayIndexOutOfBoundsException ignored){}

        // Check move upwards right
        row = currentRow - 1;
        col = currentCol + 1;
        try {
            if (chessboard.piecesOnBoard[row][col] == null || chessboard.piecesOnBoard[row][col].color != color){
                availableMoves.add(Chessboard.convertSquareToString(row, col));
            }
            squaresAttacked.add(Chessboard.convertSquareToString(row, col));
        } catch (ArrayIndexOutOfBoundsException ignored){}

        // Check move left
        row = currentRow;
        col = currentCol - 1;
        try {
            if (chessboard.piecesOnBoard[row][col] == null || chessboard.piecesOnBoard[row][col].color != color){
                availableMoves.add(Chessboard.convertSquareToString(row, col));
            }
            squaresAttacked.add(Chessboard.convertSquareToString(row, col));
        } catch (ArrayIndexOutOfBoundsException ignored){}

        // Check move right
        row = currentRow;
        col = currentCol + 1;
        try {
            if (chessboard.piecesOnBoard[row][col] == null || chessboard.piecesOnBoard[row][col].color != color){
                availableMoves.add(Chessboard.convertSquareToString(row, col));
            }
            squaresAttacked.add(Chessboard.convertSquareToString(row, col));
        } catch (ArrayIndexOutOfBoundsException ignored){}

        // Check move downwards left
        row = currentRow + 1;
        col = currentCol - 1;
        try {
            if (chessboard.piecesOnBoard[row][col] == null || chessboard.piecesOnBoard[row][col].color != color){
                availableMoves.add(Chessboard.convertSquareToString(row, col));
            }
            squaresAttacked.add(Chessboard.convertSquareToString(row, col));
        } catch (ArrayIndexOutOfBoundsException ignored){}

        // Check move downwards right
        row = currentRow + 1;
        col = currentCol + 1;
        try {
            if (chessboard.piecesOnBoard[row][col] == null || chessboard.piecesOnBoard[row][col].color != color){
                availableMoves.add(Chessboard.convertSquareToString(row, col));
            }
            squaresAttacked.add(Chessboard.convertSquareToString(row, col));
        } catch (ArrayIndexOutOfBoundsException ignored){}

        // Check move downwards
        row = currentRow + 1;
        col = currentCol;
        try {
            if (chessboard.piecesOnBoard[row][col] == null || chessboard.piecesOnBoard[row][col].color != color){
                availableMoves.add(Chessboard.convertSquareToString(row, col));
            }
            squaresAttacked.add(Chessboard.convertSquareToString(row, col));
        } catch (ArrayIndexOutOfBoundsException ignored){}

        // Check if can castle kingside
        try {
            if (isFirstMove && chessboard.piecesOnBoard[currentRow][currentCol + 1] == null && chessboard.piecesOnBoard[currentRow][currentCol + 2] == null &&
                    chessboard.piecesOnBoard[currentRow][currentCol + 3] != null && chessboard.piecesOnBoard[currentRow][currentCol + 3].isFirstMove) {
                availableMoves.add(Chessboard.convertSquareToString(currentRow, currentCol + 2));
            }
        } catch (ArrayIndexOutOfBoundsException ignored){}

        // Check if can castle queenside
        try {
            if (isFirstMove && chessboard.piecesOnBoard[currentRow][currentCol - 1] == null && chessboard.piecesOnBoard[currentRow][currentCol - 2] == null &&
                    chessboard.piecesOnBoard[currentRow][currentCol - 3] == null && chessboard.piecesOnBoard[currentRow][currentCol - 4] != null &&
                    chessboard.piecesOnBoard[currentRow][currentCol - 4].isFirstMove) {
                availableMoves.add(Chessboard.convertSquareToString(currentRow, currentCol - 2));
            }
        } catch (ArrayIndexOutOfBoundsException ignored){}
    }

    public boolean isInCheck(Chessboard chessboard){
        return (isWhite && chessboard.squaresAttackedByBlack.contains(piecePosition)) || (!isWhite && chessboard.squaresAttackedByWhite.contains(piecePosition));
    }
}
