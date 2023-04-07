package pl.karasdominik.chessgame;

import java.util.Arrays;

public class Pawn extends Piece {

    private boolean isFirstMove;

    public Pawn(boolean isWhite){
        super(isWhite, "pawn");
        isFirstMove = true;
    }

    @Override
    public boolean canMoveTo(int oldRow, int oldCol, int newRow, int newCol, Chessboard chessboard) {
        getPossibleMoves(oldRow, oldCol, chessboard);

        boolean canMove = false;
        int[] move = {newRow, newCol};
        System.out.println(Arrays.toString(move));
        for (int[] i : availableMoves){
            System.out.println(Arrays.toString(i));
            if (Arrays.equals(i, move)){
                canMove = true;
                isFirstMove = false;
                break;
            }
        }
        return canMove;
    }

    public void getPossibleMoves(int currentRow, int currentCol, Chessboard chessboard){
        int availableMove = isWhite ? -1 : 1;
        availableMoves.clear();
        if (isFirstMove){
            if (isWhite){
                availableMoves.add(new int[]{4, currentCol});
                availableMoves.add(new int[]{5, currentCol});
            } else {
                availableMoves.add(new int[]{2, currentCol});
                availableMoves.add(new int[]{3, currentCol});
            }
            return;
        }
        if (chessboard.piecesOnBoard[currentRow + availableMove][currentCol] == 0){
            availableMoves.add(new int[]{})
        }
    }

}
