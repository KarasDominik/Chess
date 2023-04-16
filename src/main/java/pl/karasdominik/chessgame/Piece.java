package pl.karasdominik.chessgame;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.List;

public abstract class Piece extends ImageView {

    protected final boolean isWhite;
    protected final Color color;
    protected String piecePosition;
    protected boolean isFirstMove;

    protected List<String> availableMoves;

    private double mouseX, mouseY;
    private int oldRow, oldCol;

    public Piece(boolean isWhite, String type, int row, int col) {
        this.isWhite = isWhite;
        this.color = isWhite ? Color.WHITE : Color.BLACK;
        this.availableMoves = new ArrayList<>();
        piecePosition = Helper.convertSquareToString(row, col);
        isFirstMove = true;

        // Assign an unique value and image for each piece
        String pieceImageFile = switch (type){
            case "pawn" -> isWhite ? Helper.WHITE_PAWN_IMAGE_FILE : Helper.BLACK_PAWN_IMAGE_FILE;
            case "rook" -> isWhite ? Helper.WHITE_ROOK_IMAGE_FILE : Helper.BLACK_ROOK_IMAGE_FILE;
            case "bishop" -> isWhite ? Helper.WHITE_BISHOP_IMAGE_FILE : Helper.BLACK_BISHOP_IMAGE_FILE;
            case "knight" -> isWhite ? Helper.WHITE_KNIGHT_IMAGE_FILE : Helper.BLACK_KNIGHT_IMAGE_FILE;
            case "queen" -> isWhite ? Helper.WHITE_QUEEN_IMAGE_FILE : Helper.BLACK_QUEEN_IMAGE_FILE;
            default -> isWhite ? Helper.WHITE_KING_IMAGE_FILE : Helper.BLACK_KING_IMAGE_FILE;
        };
        Image pieceImage = new Image(pieceImageFile);
        setImage(pieceImage);

        // Enable mouse dragging for each piece
        setOnMousePressed((MouseEvent event) -> {
            Chessboard chessboard = chessApplication.getChessboard();
            GridPane grid = (GridPane) getParent();

            // Remove any existing circles from the chessboard
            chessboard.removeCircles();

            // Display possible moves
            if (chessboard.moves.size() % 2 == 0 && this.isWhite || chessboard.moves.size() % 2 != 0 && !this.isWhite) {
                displayPossibleMoves(grid, chessboard);
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
            boolean rightTurn = isWhite && chessboard.moves.size() % 2 == 0 || !isWhite && chessboard.moves.size() % 2 != 0;
            if (rightTurn && canMoveTo(newRow, newCol)) {
                chessboard.generateMove(this, newRow, newCol, oldRow, oldCol);
            }
            else {
                grid.getChildren().remove(this);
                grid.add(this, oldCol, oldRow);
            }

            setTranslateX(0);
            setTranslateY(0);
        });
    }

    private boolean canMoveTo(int newRow, int newCol) {

        String targetSquare = Helper.convertSquareToString(newRow, newCol);
        for (String availableMove : availableMoves) {
            if (availableMove.equals(targetSquare)) {
                isFirstMove = false;
                return true;
            }
        }
        return false;
    }

    public List<String> getAvailableMoves() {
        return availableMoves;
    }

    private void displayPossibleMoves(GridPane grid, Chessboard chessboard){
        for (String square : availableMoves) {
            Circle circle = new Circle(30);
            circle.setFill(Color.rgb(255, 255, 225));
            int circleRow = Helper.convertSquareToInts(square)[0];
            int circleCol = Helper.convertSquareToInts(square)[1];
            grid.add(circle, circleCol, circleRow);
            GridPane.setHalignment(circle, HPos.CENTER);
            GridPane.setValignment(circle, VPos.CENTER);
            chessboard.circles.add(circle);
        }
    }

    public abstract void getPossibleMoves(int currentRow, int currentCol, Chessboard chessboard);
}
