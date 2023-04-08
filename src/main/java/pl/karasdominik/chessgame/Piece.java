package pl.karasdominik.chessgame;

import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

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

    protected final boolean isWhite;
    protected final Color color;
    protected String piecePosition;

    protected List<String> availableMoves;

    private double mouseX, mouseY;
    private int oldRow, oldCol;

    public Piece(boolean isWhite, String type, int row, int col) {
        this.isWhite = isWhite;
        this.color = isWhite ? Color.WHITE : Color.BLACK;
        this.availableMoves = new ArrayList<>();
        piecePosition = Chessboard.convertSquareToString(row, col);

        // Assign an unique value and image for each piece
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

        // Enable mouse dragging for each piece

        setOnMousePressed((MouseEvent event) -> {
            Chessboard chessboard = chessApplication.getChessboard();
            GridPane grid = (GridPane) getParent();

            // Remove any existing circles from the chessboard
            chessboard.removeCircles(grid);

            // Display possible moves
            for (String square : availableMoves){
                Circle circle = new Circle(30, Color.LIGHTGRAY);
                int circleRow = Chessboard.convertSquareToInts(square)[0];
                int circleCol = Chessboard.convertSquareToInts(square)[1];
                grid.add(circle, circleCol, circleRow);
                GridPane.setHalignment(circle, HPos.CENTER);
                GridPane.setValignment(circle, VPos.CENTER);
                chessboard.circles.add(circle);
            }

            mouseX = event.getSceneX();
            mouseY = event.getSceneY();

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
            if (rightTurn && canMoveTo(oldRow, oldCol, newRow, newCol, chessboard)) {
                ObservableList<Node> nodes = grid.getChildren();
                for (Node node : nodes){
                    if (node instanceof Piece && GridPane.getRowIndex(node) == newRow && GridPane.getColumnIndex(node) == newCol){
                        grid.getChildren().remove(node);
                        chessboard.piecesLeft.remove(node);
                        break;
                    }
                }
                grid.getChildren().remove(this);
                // Check if it was a passant capture
                if (this instanceof Pawn && chessboard.piecesOnBoard[newRow][newCol] == null && newCol != oldCol)
                {
                        Piece pawnToRemove = chessboard.piecesOnBoard[oldRow][newCol];
                        grid.getChildren().remove(pawnToRemove);
                        chessboard.piecesLeft.remove(pawnToRemove);
                        chessboard.piecesOnBoard[oldRow][newCol] = null;
                }
                if (this instanceof Pawn && ((isWhite && newRow == 0) || (!isWhite && newRow == 7))){
                    Piece promotedPawn = new Queen(isWhite, "queen", newRow, newCol);
                    grid.add(promotedPawn, newCol, newRow);
                    GridPane.setHalignment(promotedPawn, HPos.CENTER);
                    GridPane.setValignment(promotedPawn, VPos.CENTER);
                } else {
                    grid.add(this, newCol, newRow);
                }
                String initialSquare = Chessboard.convertSquareToString(oldRow, oldCol);
                chessboard.moves.add(new Move(initialSquare, targetSquare));
                chessboard.piecesOnBoard[newRow][newCol] = this;
                chessboard.piecesOnBoard[oldRow][oldCol] = null;
                piecePosition = Chessboard.convertSquareToString(newRow, newCol);
                chessboard.removeCircles(grid);
                updatePossibleMovesForEachPiece(chessboard);

            } else {
                grid.getChildren().remove(this);
                grid.add(this, oldCol, oldRow);
            }

            setTranslateX(0);
            setTranslateY(0);
        });
    }

    public boolean canMoveTo(int oldRow, int oldCol, int newRow, int newCol, Chessboard chessboard) {
        getPossibleMoves(oldRow, oldCol, chessboard);

        String targetSquare = Chessboard.convertSquareToString(newRow, newCol);
        for (String move : availableMoves) {
            if (move.equals(targetSquare)) return true;
        }
        return false;
    }

    public abstract void getPossibleMoves(int currentRow, int currentCol, Chessboard chessboard);

    public static void updatePossibleMovesForEachPiece(Chessboard chessboard){
        for (Piece piece : chessboard.piecesLeft){
            int pieceRow = Chessboard.convertSquareToInts(piece.piecePosition)[0];
            int pieceColumn = Chessboard.convertSquareToInts(piece.piecePosition)[1];
            piece.getPossibleMoves(pieceRow, pieceColumn, chessboard);
        }
    }
}
