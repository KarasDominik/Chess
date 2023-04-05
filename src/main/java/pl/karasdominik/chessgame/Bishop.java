package pl.karasdominik.chessgame;

public class Bishop extends Piece {

    public Bishop(boolean isWhite){
        super(isWhite, "bishop");
    }

    @Override
    public boolean canMoveTo(int row, int col) {
        return true;
    }
}
