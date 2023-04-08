package pl.karasdominik.chessgame;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

record Move(String initialSquare, String targetSquare){}

public class Chessboard extends GridPane {

    private static final int SIZE = 8;
    public Piece[][] piecesOnBoard = new Piece[8][8];
    protected List<Move> moves = new ArrayList<>();
    protected List<Circle> circles = new ArrayList<>();
    protected List<Piece> piecesLeft = new ArrayList<>();

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
                        case 0, 7 -> piece = new Rook(isWhite, "rook", row, column);
                        case 1, 6 -> piece = new Knight(isWhite, "knight", row, column);
                        case 2, 5 -> piece = new Bishop(isWhite, "bishop", row, column);
                        case 3 -> piece = new Queen(isWhite, "queen", row, column);
                        default -> piece = new King(isWhite, "king", row, column);
                    }
                } else {
                    piece = new Pawn(isWhite, "pawn", row, column);
                }
                add(piece, column, row);
                piece.getPossibleMoves(row, column, this);
                piecesLeft.add(piece);
                piecesOnBoard[row][column] = piece;
                GridPane.setHalignment(piece, HPos.CENTER);
                GridPane.setValignment(piece, VPos.CENTER);
            }
        }
//        printChessboard();
    }
//    public void printChessboard() {
//        for (int i = 0; i < 8; i++) {
//            for (int j = 0; j < 8; j++) {
//                System.out.printf("%5d", piecesOnBoard[i][j]);
//            }
//            System.out.println();
//        }
//    }
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
    public static int[] convertSquareToInts(String square){
        int row = 8 - (square.charAt(1) - '0');
        int col = switch (square.charAt(0)){
            case 'a' -> 0;
            case 'b' -> 1;
            case 'c' -> 2;
            case 'd' -> 3;
            case 'e' -> 4;
            case 'f' -> 5;
            case 'g' -> 6;
            default -> 7;
        };
        return new int[]{row, col};
    }
    public void removeCircles(GridPane grid) {
        for (Circle circle : circles){
            grid.getChildren().remove(circle);
        }
        circles.clear();
    }
}
