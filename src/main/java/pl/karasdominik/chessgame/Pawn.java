package pl.karasdominik.chessgame;

public class Pawn extends Piece {

    public Pawn(boolean isWhite){
        super(isWhite, "pawn");
    }

    @Override
    public boolean canMoveTo(int row, int col) {
        return true;
    }


}
