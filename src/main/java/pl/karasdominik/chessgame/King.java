package pl.karasdominik.chessgame;

public class King extends Piece {

    public King(boolean isWhite){
        super(isWhite, "king");
    }

    @Override
    public boolean canMoveTo(int row, int col) {
        return false;
    }
}
