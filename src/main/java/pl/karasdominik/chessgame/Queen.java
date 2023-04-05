package pl.karasdominik.chessgame;

public class Queen extends Piece {

    public Queen(boolean isWhite){
        super(isWhite, "queen");
    }

    @Override
    public boolean canMoveTo(int row, int col) {
        return false;
    }
}
