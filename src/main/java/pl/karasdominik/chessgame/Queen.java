package pl.karasdominik.chessgame;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class Queen extends ImageView {

    private final String WHITE_QUEEN_IMAGE_FILE = "C:\\Users\\domin\\IdeaProjects\\chessGame\\chessGame\\Images\\whiteQueen.png";
    private final String BLACK_QUENN_IMAGE_FILE = "C:\\Users\\domin\\IdeaProjects\\chessGame\\chessGame\\Images\\blackQueen.png";

    private final boolean isWhite;
    private final Color color;

    public Queen(boolean isWhite){
        this.isWhite = isWhite;
        this.color = isWhite ? Color.WHITE : Color.BLACK;
        String queenImageFile = isWhite ? WHITE_QUEEN_IMAGE_FILE : BLACK_QUENN_IMAGE_FILE;
        Image queenImage = new Image(queenImageFile);
        setImage(queenImage);
    }
}
