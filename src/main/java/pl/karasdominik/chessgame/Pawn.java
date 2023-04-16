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
    }

    public void getPossibleMoves(int currentRow, int currentCol, Chessboard chessboard) {

        availableMoves.clear();
        squaresAttacked.clear();

        int availableMoveForward = isWhite ? -1 : 1;

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
            if (Helper.convertSquareToInts(piecePosition)[0] == initialRow) {
                if (chessboard.piecesOnBoard[currentRow][currentCol - 1] instanceof Pawn || chessboard.piecesOnBoard[currentRow][currentCol + 1] instanceof Pawn) {
                    Move lastMove = chessboard.moves.get(chessboard.moves.size() - 1);
                    if (Math.abs(Helper.convertSquareToInts(lastMove.targetSquare)[0] - Helper.convertSquareToInts(lastMove.initialSquare)[0]) == 2) {
                        availableMoves.add(Helper.convertSquareToString(currentRow + availableMoveForward, Helper.convertSquareToInts(lastMove.targetSquare)[1]));
                    }
                }
            }
        } catch (ArrayIndexOutOfBoundsException ignored){}
    }

    public void promotePawn(int newRow, int newCol, Chessboard chessboard, GridPane grid)
    {
        List<Image> promotionImages = Helper.getPromotionImages(isWhite);
        int[] initialSquareLocation = Helper.convertSquareToInts(piecePosition);

        int row = 0;
        GridPane promotionPane = new GridPane();
        promotionPane.setStyle("-fx-background-color: lightgray;");
        promotionPane.setVgap(15);
        for (Image promotionImage : promotionImages){
            ImageView imageView = new ImageView(promotionImage);
            imageView.setOnMouseClicked(e -> {
                Image chosenImage = imageView.getImage();
                if (chosenImage.equals(promotionImages.get(0))) {
                    Piece promotedPawn = new Queen(isWhite, "queen", newRow, newCol);
                    chessboard.addPieceToTheBoard(promotedPawn, newRow, newCol, grid);
                    chessboard.generateMove(promotedPawn, newRow, newCol, initialSquareLocation[0], initialSquareLocation[1], grid);
                } else if (chosenImage.equals(promotionImages.get(1))){
                    Piece promotedPawn = new Bishop(isWhite, "bishop", newRow, newCol);
                    chessboard.addPieceToTheBoard(promotedPawn, newRow, newCol, grid);
                    chessboard.generateMove(promotedPawn, newRow, newCol, initialSquareLocation[0], initialSquareLocation[1], grid);
                } else if (chosenImage.equals(promotionImages.get(2))){
                    Piece promotedPawn = new Rook(isWhite, "rook", newRow, newCol);
                    chessboard.addPieceToTheBoard(promotedPawn, newRow, newCol, grid);
                    chessboard.generateMove(promotedPawn, newRow, newCol, initialSquareLocation[0], initialSquareLocation[1], grid);
                } else if (chosenImage.equals(promotionImages.get(3))){
                    Piece promotedPawn = new Knight(isWhite, "knight", newRow, newCol);
                    chessboard.addPieceToTheBoard(promotedPawn, newRow, newCol, grid);
                    chessboard.generateMove(promotedPawn, newRow, newCol, initialSquareLocation[0], initialSquareLocation[1], grid);
                }
                grid.getChildren().remove(promotionPane);
            });
            promotionPane.add(imageView, 0, row);
            GridPane.setHalignment(imageView, HPos.CENTER);
            GridPane.setValignment(imageView, VPos.CENTER);
            row++;
        }
        grid.add(promotionPane, newCol, isWhite ? newRow : newRow - 3, 1, 4);
    }
}
