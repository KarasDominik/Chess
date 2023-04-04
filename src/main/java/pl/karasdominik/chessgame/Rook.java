package pl.karasdominik.chessgame;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class Rook extends ImageView {

    private final String WHITE_ROOK_IMAGE_FILE = "C:\\Users\\domin\\IdeaProjects\\chessGame\\chessGame\\Images\\whiteRook.png";
    private final String BLACK_ROOK_IMAGE_FILE = "C:\\Users\\domin\\IdeaProjects\\chessGame\\chessGame\\Images\\blackRook.png";

    private final boolean isWhite;
    private final Color color;

    public Rook(boolean isWhite){
        this.isWhite = isWhite;
        this.color = isWhite ? Color.WHITE : Color.BLACK;
        String rookImageFile = isWhite ? WHITE_ROOK_IMAGE_FILE : BLACK_ROOK_IMAGE_FILE;
        Image rookImage = new Image(rookImageFile);
        setImage(rookImage);
    }
}
