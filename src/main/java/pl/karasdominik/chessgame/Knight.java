package pl.karasdominik.chessgame;

public class Knight extends Piece {

    public Knight(boolean isWhite){
        super(isWhite, "knight");
    }

    @Override
    public boolean canMoveTo(int row, int col) {
        return false;
    }
}
