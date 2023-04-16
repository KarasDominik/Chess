package pl.karasdominik.chessgame;

import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
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

public class Chessboard extends GridPane {

    protected static final int SIZE = 8;
    protected Piece[][] piecesOnBoard = new Piece[8][8];
    protected List<Move> moves = new ArrayList<>();
    protected List<Circle> circles = new ArrayList<>();
    protected List<Piece> piecesLeft = new ArrayList<>();
    protected List<Piece> whitePiecesLeft = new ArrayList<>();
    protected List<Piece> blackPiecesLeft = new ArrayList<>();

    protected List<String> squaresAttackedByWhite = new ArrayList<>();
    protected List<String> squaresAttackedByBlack = new ArrayList<>();
    protected List<Move> possibleMovesForWhite = new ArrayList<>();
    protected List<Move> possibleMovesForBlack = new ArrayList<>();
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
        addStartingPieces();
        updatePossibleMovesForEachPiece();
    }

    public void generateMove(Piece piece, int newRow, int newCol, int oldRow, int oldCol, GridPane grid){
        ObservableList<Node> nodes = grid.getChildren();
        Engine engine = chessApplication.getEngine();
        for (Node node : nodes){
            if (node instanceof Piece && GridPane.getRowIndex(node) == newRow && GridPane.getColumnIndex(node) == newCol){
                grid.getChildren().remove(node);
                break;
            }
        }

        grid.getChildren().remove(piece);
        // Check if it was a passant capture
        if (piece instanceof Pawn && piecesOnBoard[newRow][newCol] == null && newCol != oldCol)
        {
            Piece pawnToRemove = piecesOnBoard[oldRow][newCol];
            grid.getChildren().remove(pawnToRemove);
        }

        // Check if it was castling
        if (piece instanceof King && Math.abs(oldCol - newCol) == 2){
            int rookColumn;
            int movedRookColumn;
            if (newCol > oldCol) {
                rookColumn = newCol + 1;
                movedRookColumn = newCol - 1;
            } else {
                rookColumn = newCol - 2;
                movedRookColumn = newCol + 1;
            }
            Piece rookToMove = piecesOnBoard[newRow][rookColumn];
            grid.getChildren().remove(rookToMove);
            grid.add(rookToMove, movedRookColumn, newRow);
            piecesOnBoard[newRow][rookColumn] = null;
            piecesOnBoard[newRow][movedRookColumn] = rookToMove;
            rookToMove.piecePosition = Helper.convertSquareToString(newRow, movedRookColumn);
        }

        // Pawn promotion
        if (piece instanceof Pawn && ((piece.isWhite && newRow == 0) || (!piece.isWhite && newRow == 7))){
            ((Pawn) piece).promotePawn(newRow, newCol, this, grid);
            return;
        }
        else {
            grid.add(piece, newCol, newRow);
        }

        piecesOnBoard[newRow][newCol] = piece;
        piecesOnBoard[oldRow][oldCol] = null;
        String initialSquare = piece.piecePosition;
        String targetSquare = Helper.convertSquareToString(newRow, newCol);
        moves.add(new Move(piece, initialSquare, targetSquare));
        piece.piecePosition = targetSquare;
        removeCircles(grid);
        updatePossibleMovesForEachPiece();
        updatePossibleMovesForBlackAndWhite();
        if (checkForEndings()) return;
        if (engine.isMyTurn() && !checkForEndings()){
            engine.makeMove(grid);
        }
    }

    private void addStartingPieces() {
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

    public void addPieceToTheBoard(Piece piece, int row, int column, GridPane grid){
        piecesOnBoard[row][column] = piece;
        grid.add(piece, column, row);
        GridPane.setHalignment(piece, HPos.CENTER);
        GridPane.setValignment(piece, VPos.CENTER);
    }

    public void updatePossibleMoves() {
        for (Piece piece : piecesLeft) {
            int pieceRow = Helper.convertSquareToInts(piece.piecePosition)[0];
            int pieceColumn = Helper.convertSquareToInts(piece.piecePosition)[1];
            piece.getPossibleMoves(pieceRow, pieceColumn, this);
        }
    }

    public void updateSquaresAttackedByWhite() {
        squaresAttackedByWhite.clear();
        for (Piece piece : whitePiecesLeft) {
            piece.getPossibleMoves(Helper.convertSquareToInts(piece.piecePosition)[0], Helper.convertSquareToInts(piece.piecePosition)[1], this);
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
            piece.getPossibleMoves(Helper.convertSquareToInts(piece.piecePosition)[0], Helper.convertSquareToInts(piece.piecePosition)[1], this);
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
    }

    public void simulateEveryMovePiece(Piece piece, boolean isWhite) {
        List<String> movesToRemove = new ArrayList<>();
        King kingToCheck = isWhite ? whiteKing : blackKing;
        for (String possibleMove : piece.availableMoves) {

            // Simulate move
            int initialSquareRow = Helper.convertSquareToInts(piece.piecePosition)[0];
            int initialSquareColumn = Helper.convertSquareToInts(piece.piecePosition)[1];

            int targetSquareRow = Helper.convertSquareToInts(possibleMove)[0];
            int targetSquareColumn = Helper.convertSquareToInts(possibleMove)[1];

            Piece tempPiece = piecesOnBoard[targetSquareRow][targetSquareColumn];
            piecesOnBoard[targetSquareRow][targetSquareColumn] = piece;
            piecesOnBoard[initialSquareRow][initialSquareColumn] = null;
            piece.piecePosition = Helper.convertSquareToString(targetSquareRow, targetSquareColumn);

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
            piece.piecePosition = Helper.convertSquareToString(initialSquareRow, initialSquareColumn);

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

    public void removeCircles(GridPane grid) {
        for (Circle circle : circles) {
            grid.getChildren().remove(circle);
        }
        circles.clear();
    }

    private void updatePossibleMovesForBlackAndWhite(){
        possibleMovesForWhite.clear();
        possibleMovesForBlack.clear();
        for (Piece piece : piecesLeft) {
            List<Move> listToUpdate = piece.isWhite ? possibleMovesForWhite : possibleMovesForBlack;
            for (String pieceMove : piece.availableMoves) {
                listToUpdate.add(new Move(piece, piece.piecePosition, pieceMove));
            }
        }
    }

    private boolean checkForEndings(){
        boolean isEnd = false;
        if (blackKing.isInCheck(this) && possibleMovesForBlack.size() == 0) {
            System.out.println("Checkmate, white wins");
            isEnd =  true;
        } else if (whiteKing.isInCheck(this) && possibleMovesForWhite.size() == 0) {
            System.out.println("Checkmate, black wins");
            isEnd =  true;
        } else if (blackPiecesLeft.size() == 1 && whitePiecesLeft.size() == 1) {
            System.out.println("Draw");
            isEnd =  true;
        } else if (possibleMovesForWhite.size() == 0 || possibleMovesForBlack.size() == 0) {
            System.out.println("Stalemate");
            isEnd =  true;
        }
        return isEnd;
    }
}
