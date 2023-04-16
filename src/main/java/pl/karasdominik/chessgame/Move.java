package pl.karasdominik.chessgame;

public class Move {

    protected Piece piece;
    protected Piece capturedPiece;
    protected String initialSquare;
    protected String targetSquare;

    public Move(Piece piece, String initialSquare, String targetSquare) {
        this.piece = piece;
        this.initialSquare = initialSquare;
        this.targetSquare = targetSquare;
    }

    public Move(Piece piece, Piece capturedPiece, String initialSquare, String targetSquare) {
        this(piece, initialSquare, targetSquare);
        this.capturedPiece = capturedPiece;
    }
}
