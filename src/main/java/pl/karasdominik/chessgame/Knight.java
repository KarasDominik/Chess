package pl.karasdominik.chessgame;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class Knight extends ImageView {

    private final String WHITE_KNIGHT_IMAGE_FILE = "C:\\Users\\domin\\IdeaProjects\\chessGame\\chessGame\\Images\\whiteKnight.png";
    private final String BLACK_KNIGHT_IMAGE_FILE = "C:\\Users\\domin\\IdeaProjects\\chessGame\\chessGame\\Images\\blackKnight.png";

    private final boolean isWhite;
    private final Color color;

    public Knight(boolean isWhite){
        this.isWhite = isWhite;
        this.color = isWhite ? Color.WHITE : Color.BLACK;
        String knightImageFile = isWhite ? WHITE_KNIGHT_IMAGE_FILE : BLACK_KNIGHT_IMAGE_FILE;
        Image knightImage = new Image(knightImageFile);
        setImage(knightImage);
    }
}
