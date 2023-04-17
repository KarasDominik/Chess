package pl.karasdominik.chessgame;

public class Bishop extends SlidingPiece {

    public Bishop(boolean isWhite, String type, int row, int col){
        super(isWhite, type, row, col);
        this.value = 3;
    }
}
