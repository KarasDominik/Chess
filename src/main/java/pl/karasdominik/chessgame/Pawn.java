package pl.karasdominik.chessgame;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece {

    protected List<String> squaresAttacked = new ArrayList<>();

    public Pawn(boolean isWhite, String type, int row, int col) {
        super(isWhite, type, row, col);
        this.value = 1;
    }

    public void getPossibleMoves(Chessboard chessboard) {

        availableMoves.clear();
        squaresAttacked.clear();

        int availableMoveForward = isWhite ? -1 : 1;

        int currentRow = Helper.convertSquareToInts(piecePosition)[0];
        int currentCol = Helper.convertSquareToInts(piecePosition)[1];

        // Check if it can move forward
        try {
            if (chessboard.piecesOnBoard[currentRow + availableMoveForward][currentCol] == null) {
                availableMoves.add(Helper.convertSquareToString(currentRow + availableMoveForward, currentCol));
                // If it can move 1 square forward, check if it can move 2 squares forward as first move
                if (isFirstMove) {
                    if (isWhite && chessboard.piecesOnBoard[currentRow - 2][currentCol] == null) {
                        availableMoves.add(Helper.convertSquareToString(4, currentCol));
                    } else {
                        if (!isWhite && chessboard.piecesOnBoard[currentRow + 2][currentCol] == null) {
                            availableMoves.add(Helper.convertSquareToString(3, currentCol));
                        }
                    }
                }
            }
        } catch (ArrayIndexOutOfBoundsException ignored){}
        try {
            // Check if it can move upper left
            Piece leftTargetSquare = chessboard.piecesOnBoard[currentRow + availableMoveForward][currentCol - 1];
            if ((leftTargetSquare != null) && (leftTargetSquare.color != color)) {
                availableMoves.add(Helper.convertSquareToString(currentRow + availableMoveForward, currentCol - 1));
            }
            squaresAttacked.add(Helper.convertSquareToString(currentRow + availableMoveForward, currentCol - 1));
        } catch (ArrayIndexOutOfBoundsException ignored){}

        try {
            // Check if it can move upper right
            Piece rightTargetSquare = chessboard.piecesOnBoard[currentRow + availableMoveForward][currentCol + 1];
            if (rightTargetSquare != null && rightTargetSquare.color != color) {
                availableMoves.add(Helper.convertSquareToString(currentRow + availableMoveForward, currentCol + 1));
            }
            squaresAttacked.add(Helper.convertSquareToString(currentRow + availableMoveForward, currentCol + 1));
        } catch (ArrayIndexOutOfBoundsException ignored){}

        try {
            // Check if it can capture en passant
            int initialRow = isWhite ? 3 : 4;
            Move lastMove = chessboard.moves.get(chessboard.moves.size() - 1);
            if (Helper.convertSquareToInts(piecePosition)[0] == initialRow && lastMove.piece instanceof Pawn) {
                if (chessboard.piecesOnBoard[currentRow][currentCol - 1] == lastMove.piece || chessboard.piecesOnBoard[currentRow][currentCol + 1] == lastMove.piece) {
                    if (Math.abs(Helper.convertSquareToInts(lastMove.targetSquare)[0] - Helper.convertSquareToInts(lastMove.initialSquare)[0]) == 2) {
                        availableMoves.add(Helper.convertSquareToString(currentRow + availableMoveForward, Helper.convertSquareToInts(lastMove.targetSquare)[1]));
                    }
                }
            }
        } catch (Exception ignored){}
    }

    public void promotePawn(Move move, Chessboard chessboard, boolean isFinal)
    {
        Engine engine = chessApplication.getEngine();

        int newRow = Helper.convertSquareToInts(move.targetSquare)[0];
        int newCol = Helper.convertSquareToInts(move.targetSquare)[1];

        if (engine.isMyTurn() || !isFinal) {
            move.piece = new Queen(isWhite, "queen", newRow, newCol);
            move.wasPromoting = true;
            move.pawnBeforePromotion = this;
            chessboard.makeMove(move, isFinal);
        } else {
            List<Image> promotionImages = Helper.getPromotionImages(isWhite);

            int row = 0;
            GridPane promotionPane = new GridPane();
            promotionPane.setStyle("-fx-background-color: lightgray;");
            promotionPane.setVgap(15);
            for (Image promotionImage : promotionImages) {
                ImageView imageView = new ImageView(promotionImage);
                imageView.setOnMouseClicked(e -> {
                    Image chosenImage = imageView.getImage();
                    if (chosenImage.equals(promotionImages.get(0))) {
                        move.piece = new Queen(isWhite, "queen", newRow, newCol);
                    } else if (chosenImage.equals(promotionImages.get(1))) {
                        move.piece = new Bishop(isWhite, "bishop", newRow, newCol);
                    } else if (chosenImage.equals(promotionImages.get(2))) {
                        move.piece = new Rook(isWhite, "rook", newRow, newCol);
                    } else if (chosenImage.equals(promotionImages.get(3))) {
                        move.piece = new Knight(isWhite, "knight", newRow, newCol);
                    }
                    chessboard.getChildren().remove(promotionPane);
                    move.wasPromoting = true;
                    move.pawnBeforePromotion = this;
                    chessboard.makeMove(move, true);
                });
                promotionPane.add(imageView, 0, row);
                GridPane.setHalignment(imageView, HPos.CENTER);
                GridPane.setValignment(imageView, VPos.CENTER);
                row++;
            }
            chessboard.add(promotionPane, newCol, isWhite ? newRow : newRow - 3, 1, 4);
        }
    }
}
