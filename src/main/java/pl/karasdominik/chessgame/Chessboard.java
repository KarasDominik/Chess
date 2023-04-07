package pl.karasdominik.chessgame;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

record Move(String initialSquare, String targetSquare){}

public class Chessboard extends GridPane {

    private static final int SIZE = 8;
    public int[][] piecesOnBoard = new int[8][8];
    protected List<Move> moves = new ArrayList<>();

    public Chessboard() {
        int rowCounter = 8;
        char colCounter = 'a';
        Font font = Font.font("Arial", FontWeight.BOLD, 16);

        for (int row = 0; row < SIZE; row++) {
            for (int column = 0; column < SIZE; column++) {
                String rowString = "";
                String colString = "";
                if (column == 0){
                    rowString += rowCounter;
                    rowCounter -= 1;
                }
                if (row == SIZE - 1){
                    colString += colCounter;
                    colCounter += 1;
                }
                Rectangle square = new Rectangle();
                Color color = (row + column) % 2 == 0 ? Color.WHITE : Color.LIGHTGREEN;
                square.setFill(color);
                square.widthProperty().bind(widthProperty().divide(SIZE));
                square.heightProperty().bind(heightProperty().divide(SIZE));
                Text rowText = new Text(rowString);
                Text colText = new Text(colString);
                rowText.setFont(font);
                colText.setFont(font);
                StackPane stackPane = new StackPane(square, rowText, colText);
                StackPane.setMargin(rowText, new Insets(0, 75, 50, 0));
                StackPane.setMargin(colText, new Insets(50, 0, 0, 85));
                add(stackPane, column, row);
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
    public static String convertSquareToString(int row, int column){
        String firstLetter = switch(column){
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
}
