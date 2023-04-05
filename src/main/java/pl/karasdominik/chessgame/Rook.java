package pl.karasdominik.chessgame;

public class Rook extends Piece {

    public Rook(boolean isWhite){
        super(isWhite, "rook");
    }

    @Override
    public boolean canMoveTo(int row, int col) {
        return false;
    }
}
