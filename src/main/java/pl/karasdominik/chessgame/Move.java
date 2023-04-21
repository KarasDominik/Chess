package pl.karasdominik.chessgame;

public class Move {

    protected Piece piece;
    protected Piece capturedPiece;
    protected String initialSquare;
    protected String targetSquare;
    protected boolean wasPromoting;
    protected Piece pawnBeforePromotion;

    public Move(Piece piece, String initialSquare, String targetSquare) {
        this.piece = piece;
        this.initialSquare = initialSquare;
        this.targetSquare = targetSquare;
    }
}
