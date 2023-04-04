package pl.karasdominik.chessgame;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class King extends ImageView {

    private final String WHITE_KING_IMAGE_FILE = "C:\\Users\\domin\\IdeaProjects\\chessGame\\chessGame\\Images\\whiteKing.png";
    private final String BLACK_KIng_IMAGE_FILE = "C:\\Users\\domin\\IdeaProjects\\chessGame\\chessGame\\Images\\blackKing.png";

    private final boolean isWhite;
    private final Color color;

    public King(boolean isWhite){
        this.isWhite = isWhite;
        this.color = isWhite ? Color.WHITE : Color.BLACK;
        String kingImageFile = isWhite ? WHITE_KING_IMAGE_FILE : BLACK_KIng_IMAGE_FILE;
        Image kingImage = new Image(kingImageFile);
        setImage(kingImage);
    }
}
