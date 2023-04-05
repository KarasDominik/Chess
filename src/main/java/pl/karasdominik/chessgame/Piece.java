package pl.karasdominik.chessgame;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

public abstract class Piece extends ImageView {

    private final String WHITE_PAWN_IMAGE_FILE = "C:\\Users\\domin\\IdeaProjects\\chessGame\\chessGame\\Images\\whitePawn.png";
    private final String BLACK_PAWN_IMAGE_FILE = "C:\\Users\\domin\\IdeaProjects\\chessGame\\chessGame\\Images\\blackPawn.png";

    private final String WHITE_KNIGHT_IMAGE_FILE = "C:\\Users\\domin\\IdeaProjects\\chessGame\\chessGame\\Images\\whiteKnight.png";
    private final String BLACK_KNIGHT_IMAGE_FILE = "C:\\Users\\domin\\IdeaProjects\\chessGame\\chessGame\\Images\\blackKnight.png";

    private final String WHITE_ROOK_IMAGE_FILE = "C:\\Users\\domin\\IdeaProjects\\chessGame\\chessGame\\Images\\whiteRook.png";
    private final String BLACK_ROOK_IMAGE_FILE = "C:\\Users\\domin\\IdeaProjects\\chessGame\\chessGame\\Images\\blackRook.png";

    private final String WHITE_QUEEN_IMAGE_FILE = "C:\\Users\\domin\\IdeaProjects\\chessGame\\chessGame\\Images\\whiteQueen.png";
    private final String BLACK_QUEEN_IMAGE_FILE = "C:\\Users\\domin\\IdeaProjects\\chessGame\\chessGame\\Images\\blackQueen.png";

    private final String WHITE_KING_IMAGE_FILE = "C:\\Users\\domin\\IdeaProjects\\chessGame\\chessGame\\Images\\whiteKing.png";
    private final String BLACK_KING_IMAGE_FILE = "C:\\Users\\domin\\IdeaProjects\\chessGame\\chessGame\\Images\\blackKing.png";

    private final String WHITE_BISHOP_IMAGE_FILE = "C:\\Users\\domin\\IdeaProjects\\chessGame\\chessGame\\Images\\whiteBishop.png";
    private final String BLACK_BISHOP_IMAGE_FILE = "C:\\Users\\domin\\IdeaProjects\\chessGame\\chessGame\\Images\\blackBishop.png";

    private final boolean isWhite;
    private final Color color;

    private double mouseX, mouseY;
    private int oldRow, oldCol;

    public Piece(boolean isWhite, String type) {
        this.isWhite = isWhite;
        this.color = isWhite ? Color.WHITE : Color.BLACK;

        String pieceImageFile = switch (type){
            case "pawn" -> isWhite ? WHITE_PAWN_IMAGE_FILE : BLACK_PAWN_IMAGE_FILE;
            case "rook" -> isWhite ? WHITE_ROOK_IMAGE_FILE : BLACK_ROOK_IMAGE_FILE;
            case "bishop" -> isWhite ? WHITE_BISHOP_IMAGE_FILE : BLACK_BISHOP_IMAGE_FILE;
            case "knight" -> isWhite ? WHITE_KNIGHT_IMAGE_FILE : BLACK_KNIGHT_IMAGE_FILE;
            case "queen" -> isWhite ? WHITE_QUEEN_IMAGE_FILE : BLACK_QUEEN_IMAGE_FILE;
            default -> isWhite ? WHITE_KING_IMAGE_FILE : BLACK_KING_IMAGE_FILE;
        };
        Image pieceImage = new Image(pieceImageFile);
        setImage(pieceImage);


        setOnMousePressed((MouseEvent event) -> {
            mouseX = event.getSceneX();
            mouseY = event.getSceneY();

            GridPane grid = (GridPane) getParent();
            oldRow = GridPane.getRowIndex(this);
            oldCol = GridPane.getColumnIndex(this);

//            grid.getChildren().remove(this);
//            grid.add(this, oldCol, oldRow);
        });

        setOnMouseDragged((MouseEvent event) -> {
            double deltaX = event.getSceneX() - mouseX;
            double deltaY = event.getSceneY() - mouseY;

            setTranslateX(deltaX);
            setTranslateY(deltaY);
        });

        setOnMouseReleased((MouseEvent event) -> {
//            GridPane grid = (GridPane) getParent();

            int newRow = (int) (event.getY() / (grid.getHeight() * 8));
            int newCol = (int) (event.getX() / (grid.getWidth() * 8));


            if (canMoveTo(newRow, newCol)) {
                grid.getChildren().remove(this);
                grid.add(this, newCol, newRow);
            } else {
                grid.getChildren().remove(this);
                grid.add(this, oldCol, oldRow);
            }

            setTranslateX(0);
            setTranslateY(0);
        });
    }

    public abstract boolean canMoveTo(int row, int col);
}
