package pl.karasdominik.chessgame;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class Bishop extends ImageView {

    private final String WHITE_BISHOP_IMAGE_FILE = "C:\\Users\\domin\\IdeaProjects\\chessGame\\chessGame\\Images\\whiteBishop.png";
    private final String BLACK_BISHOP_IMAGE_FILE = "C:\\Users\\domin\\IdeaProjects\\chessGame\\chessGame\\Images\\blackBishop.png";

    private final boolean isWhite;
    private final Color color;

    public Bishop(boolean isWhite){
        this.isWhite = isWhite;
        this.color = isWhite ? Color.WHITE : Color.BLACK;
        String bishopImageFile = isWhite ? WHITE_BISHOP_IMAGE_FILE : BLACK_BISHOP_IMAGE_FILE;
        Image bishopImage = new Image(bishopImageFile);
        setImage(bishopImage);
    }
}
