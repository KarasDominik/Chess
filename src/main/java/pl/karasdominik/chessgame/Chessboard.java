package pl.karasdominik.chessgame;

import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Chessboard extends GridPane{

    private static final int size = 8;

    public Chessboard(){
        for (int row = 0; row < size; row++){
            for (int column = 0; column < size; column++){
                Rectangle square = new Rectangle();
                Color color = (row + column) % 2 == 0 ? Color.WHITE : Color.LIGHTGREEN;
                square.setFill(color);
                square.widthProperty().bind(widthProperty().divide(size));
                square.heightProperty().bind(heightProperty().divide(size));

                add(square, column, row);
            }
        }
        addPieces();
    }

    private void addPieces(){
        boolean isWhite = false;
        for (int row = 0; row < size; row++){
            if (!(row==0||row==1||row==6||row==7)) continue;
            if (row == 6) isWhite = true;
            for (int column = 0; column < size; column++){
                if (row == 0 || row == 7){
                    switch(column){
                        case 0, 7 -> add(new Rook(isWhite), column, row);
                        case 1, 6 -> add(new Knight(isWhite), column, row);
                        case 2, 5 -> add(new Bishop(isWhite), column, row);
                        case 3 -> add(new Queen(isWhite), column, row);
                        default -> add(new King(isWhite), column, row);
                    };
                } else {
                    add(new Pawn(isWhite), column, row);
                }
            }
        }
    }
}
