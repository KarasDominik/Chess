package pl.karasdominik.chessgame;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Chessboard extends GridPane {

    private static final int SIZE = 8;
    public int[][] piecesOnBoard = new int[8][8];

    public Chessboard() {
        for (int row = 0; row < SIZE; row++) {
            for (int column = 0; column < SIZE; column++) {
                Rectangle square = new Rectangle();
                Color color = (row + column) % 2 == 0 ? Color.WHITE : Color.LIGHTGREEN;
                square.setFill(color);
                square.widthProperty().bind(widthProperty().divide(SIZE));
                square.heightProperty().bind(heightProperty().divide(SIZE));
                add(square, column, row);
            }
        }
        addPieces();
    }

    private void addPieces() {
        boolean isWhite = false;
        for (int row = 0; row < SIZE; row++) {
            if (!(row == 0 || row == 1 || row == 6 || row == 7)) {
                continue;
            }
            if (row == 6) {
                isWhite = true;
            }
            for (int column = 0; column < SIZE; column++) {
                Piece piece;
                if (row == 0 || row == 7) {
                    switch (column) {
                        case 0, 7 -> piece = new Rook(isWhite);
                        case 1, 6 -> piece = new Knight(isWhite);
                        case 2, 5 -> piece = new Bishop(isWhite);
                        case 3 -> piece = new Queen(isWhite);
                        default -> piece = new King(isWhite);
                    }
                } else {
                    piece = new Pawn(isWhite);
                }
                add(piece, column, row);
                piecesOnBoard[row][column] = piece.getID();
                GridPane.setHalignment(piece, HPos.CENTER);
                GridPane.setValignment(piece, VPos.CENTER);
            }
        }
        printChessboard();
    }
    public void printChessboard() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                System.out.printf("%5d", piecesOnBoard[i][j]);
            }
            System.out.println();
        }
    }
}
