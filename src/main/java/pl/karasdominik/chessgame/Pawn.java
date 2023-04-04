package pl.karasdominik.chessgame;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class Pawn extends ImageView {

    private final String WHITE_PAWN_IMAGE_FILE = "C:\\Users\\domin\\IdeaProjects\\chessGame\\chessGame\\Images\\whitePawn.png";
    private final String BLACK_PAWN_IMAGE_FILE = "C:\\Users\\domin\\IdeaProjects\\chessGame\\chessGame\\Images\\blackPawn.png";

    private final boolean isWhite;
    private final Color color;

    public Pawn(boolean isWhite){
        this.isWhite = isWhite;
        this.color = isWhite ? Color.WHITE : Color.BLACK;
        String pawnImageFile = isWhite ? WHITE_PAWN_IMAGE_FILE : BLACK_PAWN_IMAGE_FILE;
        Image pawnImage = new Image(pawnImageFile);
        setImage(pawnImage);
    }
}
