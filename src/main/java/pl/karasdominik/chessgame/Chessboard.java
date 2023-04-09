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

record Move(String initialSquare, String targetSquare) {
}

public class Chessboard extends GridPane {

    protected static final int SIZE = 8;
    public Piece[][] piecesOnBoard = new Piece[8][8];
    protected List<Move> moves = new ArrayList<>();
    protected List<Circle> circles = new ArrayList<>();
    protected List<Piece> piecesLeft = new ArrayList<>();
    protected List<Piece> whitePiecesLeft = new ArrayList<>();
    protected List<Piece> blackPiecesLeft = new ArrayList<>();
    protected List<String> squaresAttackedByWhite = new ArrayList<>();
    protected List<String> squaresAttackedByBlack = new ArrayList<>();
    protected King whiteKing;
    protected King blackKing;

    public Chessboard() {
        int rowCounter = 8;
        char colCounter = 'a';
        Font font = Font.font("Arial", FontWeight.BOLD, 16);

        for (int row = 0; row < SIZE; row++) {
            for (int column = 0; column < SIZE; column++) {
                String rowString = "";
                String colString = "";
                if (column == 0) {
                    rowString += rowCounter;
                    rowCounter -= 1;
                }
                if (row == SIZE - 1) {
                    colString += colCounter;
                    colCounter += 1;
                }
                Rectangle square = new Rectangle();
                Color color = (row + column) % 2 == 0 ? Color.rgb(208, 193, 175) : Color.rgb(89, 44, 24);
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
        updatePossibleMovesForEachPiece();
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
                        default -> {
                            piece = new King(isWhite, "king", row, column);
                            if (isWhite) whiteKing = (King) piece;
                            else blackKing = (King) piece;
                        }
                    }
                } else {
                    piece = new Pawn(isWhite, "pawn", row, column);
                }
                add(piece, column, row);
                if (isWhite) whitePiecesLeft.add(piece);
                else blackPiecesLeft.add(piece);
                piecesLeft.add(piece);
                piecesOnBoard[row][column] = piece;
                GridPane.setHalignment(piece, HPos.CENTER);
                GridPane.setValignment(piece, VPos.CENTER);
            }
        }
    }

    public void updatePossibleMoves() {
        for (Piece piece : piecesLeft) {
            int pieceRow = Chessboard.convertSquareToInts(piece.piecePosition)[0];
            int pieceColumn = Chessboard.convertSquareToInts(piece.piecePosition)[1];
            piece.getPossibleMoves(pieceRow, pieceColumn, this);
        }
    }

    public void updateSquaresAttackedByWhite() {
        squaresAttackedByWhite.clear();
        for (Piece piece : whitePiecesLeft) {
            piece.getPossibleMoves(Chessboard.convertSquareToInts(piece.piecePosition)[0], Chessboard.convertSquareToInts(piece.piecePosition)[1], this);
            if (piece instanceof Pawn) {
                squaresAttackedByWhite.addAll(((Pawn) piece).squaresAttacked);
            } else if (piece instanceof King) {
                squaresAttackedByWhite.addAll(((King) piece).squaresAttacked);
            } else {
                squaresAttackedByWhite.addAll(piece.getAvailableMoves());
            }
        }
    }

    public void updateSquaresAttackedByBlack() {
        squaresAttackedByBlack.clear();
        for (Piece piece : blackPiecesLeft) {
            piece.getPossibleMoves(Chessboard.convertSquareToInts(piece.piecePosition)[0], Chessboard.convertSquareToInts(piece.piecePosition)[1], this);
            if (piece instanceof Pawn) {
                squaresAttackedByBlack.addAll(((Pawn) piece).squaresAttacked);
            } else if (piece instanceof King) {
                squaresAttackedByBlack.addAll(((King) piece).squaresAttacked);
            } else {
                squaresAttackedByBlack.addAll(piece.getAvailableMoves());
            }
        }
    }

    public void updatePossibleMovesForEachPiece() {
        updatePiecesLeft();
        updatePossibleMoves();
        updateSquaresAttackedByBlack();
        updateSquaresAttackedByWhite();

        // Simulate every move to check if king will be safe afterwards
        if (moves.size() % 2 == 0) {
            for (Piece piece : whitePiecesLeft) {
                simulateEveryMovePiece(piece, piece.isWhite);
            }
        } else {
            for (Piece piece : blackPiecesLeft) {
                simulateEveryMovePiece(piece, piece.isWhite);
            }
        }

        // Check for the endings
        if (blackKing.isInCheck(this) && getPossibleMoves(false) == 0) {
            System.out.println("Checkmate, white wins");
        } else if (whiteKing.isInCheck(this) && getPossibleMoves(true) == 0) {
            System.out.println("Checkmate, black wins");
        } else if (blackPiecesLeft.size() == 1 && whitePiecesLeft.size() == 1) {
            System.out.println("Draw");
        } else if (getPossibleMoves(false) == 0 || getPossibleMoves(true) == 0) {
            System.out.println("Stalemate");
        }
    }


    public void simulateEveryMovePiece(Piece piece, boolean isWhite) {
        List<String> movesToRemove = new ArrayList<>();
        King kingToCheck = isWhite ? whiteKing : blackKing;
        for (String possibleMove : piece.availableMoves) {

            // Simulate move
            int initialSquareRow = convertSquareToInts(piece.piecePosition)[0];
            int initialSquareColumn = convertSquareToInts(piece.piecePosition)[1];

            int targetSquareRow = convertSquareToInts(possibleMove)[0];
            int targetSquareColumn = convertSquareToInts(possibleMove)[1];

            Piece tempPiece = piecesOnBoard[targetSquareRow][targetSquareColumn];
            piecesOnBoard[targetSquareRow][targetSquareColumn] = piece;
            piecesOnBoard[initialSquareRow][initialSquareColumn] = null;
            piece.piecePosition = convertSquareToString(targetSquareRow, targetSquareColumn);

            if (isWhite) {
                updateBlackPiecesLeft();
                updateSquaresAttackedByBlack();
            } else {
                updateWhitePiecesLeft();
                updateSquaresAttackedByWhite();
            }


            if (kingToCheck.isInCheck(this)) {
                movesToRemove.add(possibleMove);
            }

            piecesOnBoard[initialSquareRow][initialSquareColumn] = piece;
            piecesOnBoard[targetSquareRow][targetSquareColumn] = tempPiece;
            piece.piecePosition = convertSquareToString(initialSquareRow, initialSquareColumn);

            if (isWhite) {
                updateBlackPiecesLeft();
                updateSquaresAttackedByBlack();
            } else {
                updateWhitePiecesLeft();
                updateSquaresAttackedByWhite();
            }
        }
        piece.availableMoves.removeAll(movesToRemove);
    }

    public void updatePiecesLeft() {
        piecesLeft.clear();
        whitePiecesLeft.clear();
        blackPiecesLeft.clear();
        for (int row = 0; row < SIZE; row++) {
            for (int column = 0; column < SIZE; column++) {
                Piece piece = piecesOnBoard[row][column];
                if (piece != null) {
                    piecesLeft.add(piece);
                    if (piece.isWhite){
                        whitePiecesLeft.add(piece);
                    }
                    else {
                        blackPiecesLeft.add(piece);
                    }
                }
            }
        }
    }

    public void updateBlackPiecesLeft() {
        blackPiecesLeft.clear();
        for (int row = 0; row < SIZE; row++) {
            for (int column = 0; column < SIZE; column++) {
                Piece piece = piecesOnBoard[row][column];
                if (piece != null && !piece.isWhite) {
                    blackPiecesLeft.add(piece);
                }
            }
        }
    }

    public void updateWhitePiecesLeft() {
        whitePiecesLeft.clear();
        for (int row = 0; row < SIZE; row++) {
            for (int column = 0; column < SIZE; column++) {
                Piece piece = piecesOnBoard[row][column];
                if (piece != null && piece.isWhite) {
                    whitePiecesLeft.add(piece);
                }
            }
        }
    }

    public static String convertSquareToString(int row, int column) {
        String firstLetter = switch (column) {
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

    public static int[] convertSquareToInts(String square) {
        int row = 8 - (square.charAt(1) - '0');
        int col = switch (square.charAt(0)) {
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
        for (Circle circle : circles) {
            grid.getChildren().remove(circle);
        }
        circles.clear();
    }

    private int getPossibleMoves(boolean isWhite){
        List<Piece> piecesToCheck = isWhite ? whitePiecesLeft : blackPiecesLeft;
        int possibleMoves = 0;
        for (Piece piece : piecesToCheck){
            possibleMoves += piece.availableMoves.size();
        }
        return possibleMoves;
    }

//    public void printChessboard(){
//        for (int row = 0; row < 8; row ++){
//            for (int col = 0; col < 8; col++){
//                System.out.printf("%10s ", piecesOnBoard[row][col]);
//            }
//            System.out.println();
//        }
//    }
}
