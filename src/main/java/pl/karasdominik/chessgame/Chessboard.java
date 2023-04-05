package pl.karasdominik.chessgame;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Chessboard extends GridPane {

    private static final int SIZE = 8;

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
                if (row == 0 || row == 7) {
                    switch (column) {
                        case 0, 7 -> {
                            Rook rook = new Rook(isWhite);
                            add(rook, column, row);
                            GridPane.setHalignment(rook, HPos.CENTER);
                            GridPane.setValignment(rook, VPos.CENTER);
                        }
                        case 1, 6 -> {
                            Knight knight = new Knight(isWhite);
                            add(knight, column, row);
                            GridPane.setHalignment(knight, HPos.CENTER);
                            GridPane.setValignment(knight, VPos.CENTER);
                        }
                        case 2, 5 -> {
                            Bishop bishop = new Bishop(isWhite);
                            add(bishop, column, row);
                            GridPane.setHalignment(bishop, HPos.CENTER);
                            GridPane.setValignment(bishop, VPos.CENTER);
                        }
                        case 3 -> {
                            Queen queen = new Queen(isWhite);
                            add(queen, column, row);
                            GridPane.setHalignment(queen, HPos.CENTER);
                            GridPane.setValignment(queen, VPos.CENTER);
                        }
                        default -> {
                            King king = new King(isWhite);
                            add(king, column, row);
                            GridPane.setHalignment(king, HPos.CENTER);
                            GridPane.setValignment(king, VPos.CENTER);
                        }
                    }
                } else {
                    Pawn pawn = new Pawn(isWhite);
                    add(pawn, column, row);
                    GridPane.setHalignment(pawn, HPos.CENTER);
                    GridPane.setValignment(pawn, VPos.CENTER);
                }
            }
        }
    }
}
