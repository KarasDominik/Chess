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

    private static final String WHITE_PAWN_IMAGE_FILE = "C:\\Users\\domin\\IdeaProjects\\Chess\\Images\\whitePawn.png";
    private static final String BLACK_PAWN_IMAGE_FILE = "C:\\Users\\domin\\IdeaProjects\\Chess\\Images\\blackPawn.png";

    private static final String WHITE_KNIGHT_IMAGE_FILE = "C:\\Users\\domin\\IdeaProjects\\Chess\\Images\\whiteKnight.png";
    private static final String BLACK_KNIGHT_IMAGE_FILE = "C:\\Users\\domin\\IdeaProjects\\Chess\\Images\\blackKnight.png";

    private static final String WHITE_ROOK_IMAGE_FILE = "C:\\Users\\domin\\IdeaProjects\\Chess\\Images\\whiteRook.png";
    private static final String BLACK_ROOK_IMAGE_FILE = "C:\\Users\\domin\\IdeaProjects\\Chess\\Images\\blackRook.png";

    private static final String WHITE_QUEEN_IMAGE_FILE = "C:\\Users\\domin\\IdeaProjects\\Chess\\Images\\whiteQueen.png";
    private static final String BLACK_QUEEN_IMAGE_FILE = "C:\\Users\\domin\\IdeaProjects\\Chess\\Images\\blackQueen.png";

    private static final String WHITE_KING_IMAGE_FILE = "C:\\Users\\domin\\IdeaProjects\\Chess\\Images\\whiteKing.png";
    private static final String BLACK_KING_IMAGE_FILE = "C:\\Users\\domin\\IdeaProjects\\Chess\\Images\\blackKing.png";

    private static final String WHITE_BISHOP_IMAGE_FILE = "C:\\Users\\domin\\IdeaProjects\\Chess\\Images\\whiteBishop.png";
    private static final String BLACK_BISHOP_IMAGE_FILE = "C:\\Users\\domin\\IdeaProjects\\Chess\\Images\\blackBishop.png";

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
        piecePosition = Chessboard.convertSquareToString(row, col);
        isFirstMove = true;

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
            if (chessboard.moves.size() % 2 == 0 && this.isWhite || chessboard.moves.size() % 2 != 0 && !this.isWhite) {
                for (String square : availableMoves) {
                    Circle circle = new Circle(30);
                    circle.setFill(Color.rgb(255, 255, 225));
                    int circleRow = Chessboard.convertSquareToInts(square)[0];
                    int circleCol = Chessboard.convertSquareToInts(square)[1];
                    grid.add(circle, circleCol, circleRow);
                    GridPane.setHalignment(circle, HPos.CENTER);
                    GridPane.setValignment(circle, VPos.CENTER);
                    chessboard.circles.add(circle);
                }
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
            if (rightTurn && canMoveTo(newRow, newCol)) {
                ObservableList<Node> nodes = grid.getChildren();
                for (Node node : nodes){
                    if (node instanceof Piece && GridPane.getRowIndex(node) == newRow && GridPane.getColumnIndex(node) == newCol){
                        grid.getChildren().remove(node);
                        if (((Piece) node).isWhite) chessboard.whitePiecesLeft.remove(node);
                        else chessboard.blackPiecesLeft.remove(node);
                        chessboard.piecesLeft.remove(node);
                        break;
                    }
                }
                grid.getChildren().remove(this);

                // Check if it was a passant capture
                if (this instanceof Pawn && chessboard.piecesOnBoard[newRow][newCol] == null && newCol != oldCol)
                {
                    System.out.println("passant capture");
                    Piece pawnToRemove = chessboard.piecesOnBoard[oldRow][newCol];
                    if (pawnToRemove.isWhite) chessboard.whitePiecesLeft.remove(pawnToRemove);
                    else chessboard.blackPiecesLeft.remove(pawnToRemove);
                    chessboard.piecesLeft.remove(pawnToRemove);
                    chessboard.piecesOnBoard[oldRow][newCol] = null;
                    grid.getChildren().remove(pawnToRemove);
                }

                // Check if it was castling
                if (this instanceof King && Math.abs(oldCol - newCol) == 2){
                    int rookColumn;
                    int movedRookColumn;
                    if (newCol > oldCol) {
                        rookColumn = newCol + 1;
                        movedRookColumn = newCol - 1;
                    } else {
                        rookColumn = newCol - 2;
                        movedRookColumn = newCol + 1;
                    }
                    Piece rookToMove = chessboard.piecesOnBoard[newRow][rookColumn];
                    grid.getChildren().remove(rookToMove);
                    grid.add(rookToMove, movedRookColumn, newRow);
                    chessboard.piecesOnBoard[newRow][rookColumn] = null;
                    chessboard.piecesOnBoard[newRow][movedRookColumn] = rookToMove;
                    rookToMove.piecePosition = Chessboard.convertSquareToString(newRow, movedRookColumn);
                }

                // Pawn promotion
                if (this instanceof Pawn && ((isWhite && newRow == 0) || (!isWhite && newRow == 7))){
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

                    int r = 0;
                    GridPane promotionPane = new GridPane();
                    promotionPane.setStyle("-fx-background-color: lightgray;");
                    promotionPane.setVgap(15);
                    for (Image promotionImage : promotionImages){
                        ImageView imageView = new ImageView(promotionImage);
                        imageView.setOnMouseClicked(e -> {
                            Image chosenImage = imageView.getImage();
                            if (chosenImage.equals(promotionImages.get(0))) {
                                Piece promotedPawn = new Queen(isWhite, "queen", newRow, newCol);
                                chessboard.piecesOnBoard[newRow][newCol] = promotedPawn;
                                grid.add(promotedPawn, newCol, newRow);
                                GridPane.setHalignment(promotedPawn, HPos.CENTER);
                                GridPane.setValignment(promotedPawn, VPos.CENTER);
                            } else if (chosenImage.equals(promotionImages.get(1))){
                                Piece promotedPawn = new Bishop(isWhite, "bishop", newRow, newCol);
                                chessboard.piecesOnBoard[newRow][newCol] = promotedPawn;
                                grid.add(promotedPawn, newCol, newRow);
                                GridPane.setHalignment(promotedPawn, HPos.CENTER);
                                GridPane.setValignment(promotedPawn, VPos.CENTER);
                            } else if (chosenImage.equals(promotionImages.get(2))){
                                Piece promotedPawn = new Rook(isWhite, "rook", newRow, newCol);
                                chessboard.piecesOnBoard[newRow][newCol] = promotedPawn;
                                grid.add(promotedPawn, newCol, newRow);
                                GridPane.setHalignment(promotedPawn, HPos.CENTER);
                                GridPane.setValignment(promotedPawn, VPos.CENTER);
                            } else if (chosenImage.equals(promotionImages.get(3))){
                                Piece promotedPawn = new Knight(isWhite, "knight", newRow, newCol);
                                chessboard.piecesOnBoard[newRow][newCol] = promotedPawn;
                                grid.add(promotedPawn, newCol, newRow);
                                GridPane.setHalignment(promotedPawn, HPos.CENTER);
                                GridPane.setValignment(promotedPawn, VPos.CENTER);
                            }
                            grid.getChildren().remove(promotionPane);
                            String initialSquare = Chessboard.convertSquareToString(oldRow, oldCol);
                            chessboard.moves.add(new Move(initialSquare, targetSquare));
                            chessboard.piecesOnBoard[oldRow][oldCol] = null;
                            piecePosition = Chessboard.convertSquareToString(newRow, newCol);
                            chessboard.removeCircles(grid);
                            chessboard.updatePossibleMovesForEachPiece();
                        });
                        promotionPane.add(imageView, 0, r);
                        GridPane.setHalignment(imageView, HPos.CENTER);
                        GridPane.setValignment(imageView, VPos.CENTER);
                        r++;
                    }
                    grid.add(promotionPane, newCol, newRow, 1, 4);

                } else {
                    grid.add(this, newCol, newRow);
                    chessboard.piecesOnBoard[newRow][newCol] = this;
                    String initialSquare = Chessboard.convertSquareToString(oldRow, oldCol);
                    chessboard.moves.add(new Move(initialSquare, targetSquare));
                    chessboard.piecesOnBoard[oldRow][oldCol] = null;
                    piecePosition = Chessboard.convertSquareToString(newRow, newCol);
                    chessboard.removeCircles(grid);
                    chessboard.updatePossibleMovesForEachPiece();
                }

            } else {
                grid.getChildren().remove(this);
                grid.add(this, oldCol, oldRow);
            }

            setTranslateX(0);
            setTranslateY(0);
        });
    }

    public boolean canMoveTo(int newRow, int newCol) {

        String targetSquare = Chessboard.convertSquareToString(newRow, newCol);
        for (String move : availableMoves) {
            if (move.equals(targetSquare)) {
                isFirstMove = false;
                return true;
            }
        }
        return false;
    }

    public List<String> getAvailableMoves() {
        return availableMoves;
    }

//    @Override
//    public String toString() {
//        return getClass().getSimpleName();
//    }

    public abstract void getPossibleMoves(int currentRow, int currentCol, Chessboard chessboard);
}
