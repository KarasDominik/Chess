package pl.karasdominik.chessgame;

public class Pawn extends Piece {

    private boolean isFirstMove = true;

    public Pawn(boolean isWhite){
        super(isWhite, "pawn");
    }

    @Override
    public boolean canMoveTo(int oldRow, int oldCol, int newRow, int newCol, Chessboard chessboard) {
        System.out.println("Old row: " + oldRow);
        System.out.println("New row: " + newRow);
        System.out.println("Piece on new square: " + chessboard.piecesOnBoard[newRow][newCol]);
        System.out.println("Column difference: " + Math.abs(oldCol));
        // Can never move backwards or sideways
        if (this.isWhite() && oldRow <= newRow || !this.isWhite() && oldRow >= newRow){
            System.out.println("Wrong move");
            return false;
        } // Can never move more than 1 column sideways
        if (Math.abs(oldCol - newCol) > 1){
            System.out.println("Wrong move");
            return false;
        }
        // Can never move on occupied square
        if (chessboard.piecesOnBoard[newRow][newCol] != 0){
            if (oldCol == newCol){
                return false;
            }
            if ((chessboard.piecesOnBoard[newRow][newCol] > 16 && getID() > 16) ||
                    (chessboard.piecesOnBoard[newRow][newCol] < 16 && getID() < 16))
            {
                System.out.println("Wrong move");
                return false;
            }
        }
        // Can never move to a different column if the square is empty or more than 1 column sideways
        if (Math.abs(oldCol - newCol) == 1 && chessboard.piecesOnBoard[newRow][newCol] == 0){
            System.out.println("Wrong move");
            return false;
        }
        // Can never move more than 1 square forward unless it is first move
        if (Math.abs(oldRow - newRow) > 1 && !isFirstMove || Math.abs(oldRow - newRow) > 2 && isFirstMove){
            System.out.println("Wrong move");
            return false;
        }
        //
        isFirstMove = false;
        return true;

    }


}
