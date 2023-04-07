package pl.karasdominik.chessgame;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.List;

public abstract class Piece extends ImageView {

    private static final String WHITE_PAWN_IMAGE_FILE = "C:\\Users\\domin\\IdeaProjects\\chessGame\\chessGame\\Images\\whitePawn.png";
    private static final String BLACK_PAWN_IMAGE_FILE = "C:\\Users\\domin\\IdeaProjects\\chessGame\\chessGame\\Images\\blackPawn.png";

    private static final String WHITE_KNIGHT_IMAGE_FILE = "C:\\Users\\domin\\IdeaProjects\\chessGame\\chessGame\\Images\\whiteKnight.png";
    private static final String BLACK_KNIGHT_IMAGE_FILE = "C:\\Users\\domin\\IdeaProjects\\chessGame\\chessGame\\Images\\blackKnight.png";

    private static final String WHITE_ROOK_IMAGE_FILE = "C:\\Users\\domin\\IdeaProjects\\chessGame\\chessGame\\Images\\whiteRook.png";
    private static final String BLACK_ROOK_IMAGE_FILE = "C:\\Users\\domin\\IdeaProjects\\chessGame\\chessGame\\Images\\blackRook.png";

    private static final String WHITE_QUEEN_IMAGE_FILE = "C:\\Users\\domin\\IdeaProjects\\chessGame\\chessGame\\Images\\whiteQueen.png";
    private static final String BLACK_QUEEN_IMAGE_FILE = "C:\\Users\\domin\\IdeaProjects\\chessGame\\chessGame\\Images\\blackQueen.png";

    private static final String WHITE_KING_IMAGE_FILE = "C:\\Users\\domin\\IdeaProjects\\chessGame\\chessGame\\Images\\whiteKing.png";
    private static final String BLACK_KING_IMAGE_FILE = "C:\\Users\\domin\\IdeaProjects\\chessGame\\chessGame\\Images\\blackKing.png";

    private static final String WHITE_BISHOP_IMAGE_FILE = "C:\\Users\\domin\\IdeaProjects\\chessGame\\chessGame\\Images\\whiteBishop.png";
    private static final String BLACK_BISHOP_IMAGE_FILE = "C:\\Users\\domin\\IdeaProjects\\chessGame\\chessGame\\Images\\blackBishop.png";

    private static final int pawnID = 1;
    private static final int knightID = 2;
    private static final int bishopID = 3;
    private static final int rookID = 4;
    private static final int queenID = 5;
    private static final int kingID = 6;

    private final int id;
    protected final boolean isWhite;

    protected List<String> availableMoves;

    private double mouseX, mouseY;
    private int oldRow, oldCol;

    public Piece(boolean isWhite, String type) {
        this.isWhite = isWhite;
        this.availableMoves = new ArrayList<>();
        // Assign an unique value and image for each piece

        String pieceImageFile;
        switch (type){
            case "pawn" -> {
                if (isWhite){
                    pieceImageFile = WHITE_PAWN_IMAGE_FILE;
                    id = pawnID + 8;
                } else {
                    pieceImageFile = BLACK_PAWN_IMAGE_FILE;
                    id = pawnID + 16;
                }
            }
            case "rook" -> {
                if (isWhite) {
                    pieceImageFile = WHITE_ROOK_IMAGE_FILE;
                    id = rookID + 8;
                } else {
                    pieceImageFile = BLACK_ROOK_IMAGE_FILE;
                    id = rookID + 16;
                }
            }
            case "bishop" -> {
                if (isWhite) {
                    pieceImageFile = WHITE_BISHOP_IMAGE_FILE;
                    id = bishopID + 8;
                } else {
                    pieceImageFile = BLACK_BISHOP_IMAGE_FILE;
                    id = bishopID + 16;
                }
            }
            case "knight" -> {
                if (isWhite) {
                    pieceImageFile = WHITE_KNIGHT_IMAGE_FILE;
                    id = knightID + 8;
                } else {
                    pieceImageFile = BLACK_KNIGHT_IMAGE_FILE;
                    id = knightID + 16;
                }
            }
            case "queen" -> {
                if (isWhite) {
                    pieceImageFile = WHITE_QUEEN_IMAGE_FILE;
                    id = queenID + 8;
                } else {
                    pieceImageFile = BLACK_QUEEN_IMAGE_FILE;
                    id = queenID + 16;
                }
            }
            default -> {
                if (isWhite) {
                    pieceImageFile = WHITE_KING_IMAGE_FILE;
                    id = kingID + 8;
                } else {
                    pieceImageFile = BLACK_KING_IMAGE_FILE;
                    id = kingID + 16;
                }
            }
        }
        Image pieceImage = new Image(pieceImageFile);
        setImage(pieceImage);

        // Enable mouse dragging for each piece

        setOnMousePressed((MouseEvent event) -> {
            mouseX = event.getSceneX();
            mouseY = event.getSceneY();

            GridPane grid = (GridPane) getParent();
            oldRow = GridPane.getRowIndex(this);
            oldCol = GridPane.getColumnIndex(this);

            grid.getChildren().remove(this);
            grid.add(this, oldCol, oldRow);
        });

        setOnMouseDragged((MouseEvent event) -> {
            double deltaX = event.getSceneX() - mouseX;
            double deltaY = event.getSceneY() - mouseY;

            setTranslateX(deltaX);
            setTranslateY(deltaY);
        });

        setOnMouseReleased((MouseEvent event) -> {
            Chessboard chessboard = chessApplication.getChessboard();
            GridPane grid = (GridPane) getParent();

            int newRow = (int) (event.getSceneY() / (grid.getHeight() / 8));
            int newCol = (int) (event.getSceneX() / (grid.getWidth() / 8));
            String targetSquare = Chessboard.convertSquareToString(newRow, newCol);
            boolean rightTurn = isWhite && chessboard.moves.size() % 2 == 0 || !isWhite && chessboard.moves.size() % 2 != 0;
            if (canMoveTo(oldRow, oldCol, newRow, newCol, chessboard) && rightTurn) {
                ObservableList<Node> nodes = grid.getChildren();
                for (Node node : nodes){
                    if (node instanceof Piece && GridPane.getRowIndex(node) == newRow && GridPane.getColumnIndex(node) == newCol){
                        grid.getChildren().remove(node);
                        break;
                    }
                }
                grid.getChildren().remove(this);
                grid.add(this, newCol, newRow);
                String initialSquare = Chessboard.convertSquareToString(oldRow, oldCol);
                chessboard.moves.add(new Move(initialSquare, targetSquare));
                chessboard.piecesOnBoard[newRow][newCol] = id;
                chessboard.piecesOnBoard[oldRow][oldCol] = 0;
                System.out.println(chessboard.moves.size());

            } else {
                grid.getChildren().remove(this);
                grid.add(this, oldCol, oldRow);
            }

            setTranslateX(0);
            setTranslateY(0);
        });
    }

    public int getID() {
        return id;
    }

    public abstract boolean canMoveTo(int oldRow, int oldCol, int newRow, int newCol, Chessboard chessboard);
}
