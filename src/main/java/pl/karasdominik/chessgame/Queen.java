package pl.karasdominik.chessgame;

public class Queen extends SlidingPiece {

    public Queen(boolean isWhite, String type, int row, int col) {
        super(isWhite, type, row, col);
        this.value = 9;
    }
}
