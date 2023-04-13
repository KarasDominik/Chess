package pl.karasdominik.chessgame;

import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;

public class Helper {

    protected static final String WHITE_PAWN_IMAGE_FILE = "C:\\Users\\domin\\IdeaProjects\\Chess\\Images\\whitePawn.png";
    protected static final String BLACK_PAWN_IMAGE_FILE = "C:\\Users\\domin\\IdeaProjects\\Chess\\Images\\blackPawn.png";

    protected static final String WHITE_KNIGHT_IMAGE_FILE = "C:\\Users\\domin\\IdeaProjects\\Chess\\Images\\whiteKnight.png";
    protected static final String BLACK_KNIGHT_IMAGE_FILE = "C:\\Users\\domin\\IdeaProjects\\Chess\\Images\\blackKnight.png";

    protected static final String WHITE_ROOK_IMAGE_FILE = "C:\\Users\\domin\\IdeaProjects\\Chess\\Images\\whiteRook.png";
    protected static final String BLACK_ROOK_IMAGE_FILE = "C:\\Users\\domin\\IdeaProjects\\Chess\\Images\\blackRook.png";

    protected static final String WHITE_QUEEN_IMAGE_FILE = "C:\\Users\\domin\\IdeaProjects\\Chess\\Images\\whiteQueen.png";
    protected static final String BLACK_QUEEN_IMAGE_FILE = "C:\\Users\\domin\\IdeaProjects\\Chess\\Images\\blackQueen.png";

    protected static final String WHITE_KING_IMAGE_FILE = "C:\\Users\\domin\\IdeaProjects\\Chess\\Images\\whiteKing.png";
    protected static final String BLACK_KING_IMAGE_FILE = "C:\\Users\\domin\\IdeaProjects\\Chess\\Images\\blackKing.png";

    protected static final String WHITE_BISHOP_IMAGE_FILE = "C:\\Users\\domin\\IdeaProjects\\Chess\\Images\\whiteBishop.png";
    protected static final String BLACK_BISHOP_IMAGE_FILE = "C:\\Users\\domin\\IdeaProjects\\Chess\\Images\\blackBishop.png";

    public static int[] convertSquareToInts(String square) {
        int row = 8 - (square.charAt(1) - '0');
        int col = switch (square.charAt(0)) {
            case 'a' -> 0;
            case 'b' -> 1;
            case 'c' -> 2;
            case 'd' -> 3;
            case 'e' -> 4;
            case 'f' -> 5;
            case 'g' -> 6;
            default -> 7;
        };
        return new int[]{row, col};
    }

    public static String convertSquareToString(int row, int column) {
        String firstLetter = switch (column) {
            case 0 -> "a";
            case 1 -> "b";
            case 2 -> "c";
            case 3 -> "d";
            case 4 -> "e";
            case 5 -> "f";
            case 6 -> "g";
            default -> "h";
        };
        String secondLetter = String.valueOf(Math.abs(row - 8));

        return firstLetter + secondLetter;
    }
    
    public static List<Image> getPromotionImages(boolean isWhite){
        List<Image> promotionImages = new ArrayList<>();
        if (isWhite){
            promotionImages.add(new Image(WHITE_QUEEN_IMAGE_FILE));
            promotionImages.add(new Image(WHITE_BISHOP_IMAGE_FILE));
            promotionImages.add(new Image(WHITE_ROOK_IMAGE_FILE));
            promotionImages.add(new Image(WHITE_KNIGHT_IMAGE_FILE));
        } else {
            promotionImages.add(new Image(BLACK_QUEEN_IMAGE_FILE));
            promotionImages.add(new Image(BLACK_BISHOP_IMAGE_FILE));
            promotionImages.add(new Image(BLACK_ROOK_IMAGE_FILE));
            promotionImages.add(new Image(BLACK_KNIGHT_IMAGE_FILE));
        }
        return promotionImages;
    }
}
